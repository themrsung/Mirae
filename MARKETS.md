# Markets

How Mirae's two market types set prices, how active-price markets deliberately support
**negative prices**, and the bugs that had to be patched before that feature was safe.

> Everything below is read from the code as it stands at `HEAD`. Commit hashes are cited
> for the historical claims so they can be checked with `git show`.

## Table of contents

- [The two market types](#the-two-market-types)
- [Fixed-price markets](#fixed-price-markets)
- [Active-price markets](#active-price-markets)
  - [The order chain](#the-order-chain)
  - [Price discovery](#price-discovery)
  - [The server order ladder](#the-server-order-ladder)
  - [Tick sizes](#tick-sizes)
  - [Volatility levels](#volatility-levels)
  - [Fees and rounding](#fees-and-rounding)
- [Sub-zero prices](#sub-zero-prices)
  - [An intended feature](#an-intended-feature)
  - [How the ladder reaches below zero](#how-the-ladder-reaches-below-zero)
  - [What a negative price means](#what-a-negative-price-means)
  - [What the market actually priced](#what-the-market-actually-priced)
- [Bugs and hotpatches](#bugs-and-hotpatches)
- [Residual observations](#residual-observations)

---

## The two market types

`MarketType` has two values, both extending `AbstractMarket`:

| Type | Class | Price source |
|---|---|---|
| `FIXED_PRICE` | `market/fixed/FixedPriceMarket` | Two admin-set numbers |
| `ACTIVE_PRICE` | `market/active/ActivePriceMarket` | A live order book |

Both expose the same `Market` interface — `getBuyPrice(long)`, `getSellPrice(long)`,
`buy(...)`, `sell(...)` — so `MarketMenu` drives them through one code path. Markets are
tagged with a `MarketCategory` (agriculture, raw materials, timber, seafood, foods, block
families, custom items, luxury items, speculative goods, music discs, furniture, enchanted
books) for discovery through `/findmarket`.

---

## Fixed-price markets

A fixed market holds two independent prices:

```java
private double buyPrice;   // what the player pays to buy
private double sellPrice;  // what the player receives to sell
```

**A negative price is a sentinel meaning "this side is disabled."** It is not a tradeable
price:

```java
public boolean isBuyable()  { return buyPrice  >= 0; }
public boolean isSellable() { return sellPrice >= 0; }
```

`MarketMenu` honours this directly (`boolean canSell = sellPrice >= 0`), so a market with
`sellPrice = -1` displays and behaves as sell-disabled. This is the convention behind the
`-1` default discussed in [Bugs and hotpatches](#bugs-and-hotpatches).

Fixed markets charge `FIXED_FEE_RATE = 0.01` (1%).

---

## Active-price markets

An active market is a small continuous double-auction. It carries no price field at all —
only a `defaultPrice` used to seed an empty book, plus the book itself.

```java
private double defaultPrice;
private final OrderChain orderChain;
private VolatilityLevel volatilityLevel;
```

### The order chain

`OrderChain` holds two sorted lists (`buyOrders`, `sellOrders`) and a `fulfillmentQueue` of
recent `Fulfillment` records. Orders are either:

- **Player orders** — `Order.player(account, type, quantity)`, market orders, placed and
  then immediately cancelled after matching, so they never rest on the book.
- **Server orders** — `Order.server(type, quantity, price)`, limit orders, the standing
  liquidity that makes a market quotable at all. Identified by `!o.hasSender()`.

`OrderType` covers `BUY_MARKET`, `SELL_MARKET`, `BUY_LIMIT`, `SELL_LIMIT`.

### Price discovery

`getBuyPrice(quantity)` and `getSellPrice(quantity)` are **volume-weighted average price**
walks over the opposite side of the book:

```java
for (Order o : orderChain.getSellOrders()) {   // getBuyPrice walks asks
    if (remaining <= 0) break;
    double p = o.getPriceOrdered();
    long   q = Math.min(remaining, o.getQuantityRemaining());
    sumProduct += p * q;
    remaining  -= q;
}
long fulfillable = quantity - remaining;
double price = fulfillable != 0 ? sumProduct / fulfillable : 0;
```

The returned `PriceQueryResult(market, quantity, price)` reports the quantity actually
fillable — which may be less than requested — and `volume()` is simply `price * quantity`.
Quoting a large size walks deeper into the book and returns a worse average, so depth is
priced in automatically.

### The server order ladder

`updateServerOrders()` is the market maker. `MarketUpdateTask` runs it over every active
market on a timer:

```java
s.runTaskTimer(p, MARKET_UPDATE_TASK, 200, 50);   // every 50 ticks ≈ 2.5s
```

It refuses to act while the book is still well stocked:

```java
double remainingOrderRatio = (double) existingOrders / volatilityLevel.getTotalOrderCount(stackSize);
if (remainingOrderRatio > SERVER_ORDER_UPDATE_THRESHOLD   // 0.8
        || existingOrders > volatilityLevel.getTotalOrderCount(stackSize)) return;
```

Once depleted past that threshold it re-anchors and rebuilds. The anchor is the weighted
average of recent fulfillments, falling back to `defaultPrice` when nothing has traded:

```java
double averageRaw = orderChain.getWeightedAveragePrice();
boolean hasAverage = Double.isFinite(averageRaw);
double average = hasAverage ? averageRaw : defaultPrice;
double basePrice = Markets.snapToNearestTick(hasAverage ? average : defaultPrice + (marketBuying ? tickSize : 0));
```

It then clears the old server orders and lays two ladders outward from `basePrice` — bids
stepping **down**, asks stepping **up**:

```java
double buyPrice = basePrice - Markets.getTickSizeAt(basePrice);
for (int i = 0; i < numSteps; i++) {
    double p = buyPrice;
    buyPrice -= Markets.getTickSizeAt(buyPrice);
    orderChain.placeOrder(Order.server(OrderType.BUY_LIMIT, quantityPerStep, p));
}

double sellPrice = basePrice;
for (int i = 0; i < numSteps; i++) {
    double p = sellPrice;
    sellPrice += Markets.getTickSizeAt(sellPrice);
    orderChain.placeOrder(Order.server(OrderType.SELL_LIMIT, quantityPerStep, p));
}
```

**There is no lower bound on the bid ladder**, by design. That is what lets the market quote
below zero — see [Sub-zero prices](#sub-zero-prices).

### Tick sizes

`Markets.getTickSizeAt(price)` is a step function on the **absolute** value of the price:

| \|price\| | Tick |
|---|---|
| < 100 | 1 |
| < 1,000 | 10 |
| < 10,000 | 25 |
| < 50,000 | 50 |
| < 200,000 | 100 |
| < 500,000 | 200 |
| < 1,000,000 | 500 |
| ≥ 1,000,000 | 1,000 |

Because it takes `Math.abs(price)`, the tick size is mirrored around zero: it never returns
zero or a negative, so a descending ladder marches through zero and onward without ever
stalling or reversing.

### Volatility levels

`VolatilityLevel(numSteps, stacksPerStep)` controls ladder depth and size:

| Level | Steps | Stacks/step |
|---|---|---|
| `VERY_STABLE` | 20 | 4 |
| `STABLE` | 10 | 3 |
| `MODERATE` | 10 | 2 |
| `ILLIQUID` | 5 | 1 |
| `RARE` | 2 | 0.5 |
| `SPECULATIVE` | 20 | 0.25 |

`MODERATE` is the default. Step count drives two things at once: how deep the book is, and
how far the bid ladder reaches below the anchor. `VERY_STABLE` and `SPECULATIVE` both run 20
steps and so reach twice as far down as `MODERATE`.

The naming is worth reading carefully, because the intuition inverts. `VERY_STABLE` does not
mean "price moves little" — it means "quote a deep, thick book", 20 rungs of 4 stacks each.
On a cheap commodity that is precisely the configuration that plants the most bids below
zero. The iron ingot market was `VERY_STABLE`, and it is the market that went negative; see
[What the market actually priced](#what-the-market-actually-priced).

### Fees and rounding

```java
private static final double ACTIVE_FEE_RATE = 0.005;   // 0.5%
private static final double FIXED_FEE_RATE  = 0.01;    // 1%

public static double getActiveFeeRateFor(Account account) {
    long level = account.getSkillLevel(SkillType.TRADING);
    return ACTIVE_FEE_RATE * Math.pow(0.99, level);
}
```

The `TRADING` skill shaves 1% off the fee rate per level, compounding — it never reaches
zero.

Fees are always computed on `Math.abs(volume)` and always work against the player, and the
rounding is deliberately house-favouring:

```java
double amountToWithdraw = Math.ceil (quantityFulfilled * priceFulfilled + fees);  // buy
double amountToDeposit  = Math.floor(quantityFulfilled * priceFulfilled - fees);  // sell
```

Buys round up, sells round down.

---

## Sub-zero prices

### An intended feature

Active markets are built to price goods below zero. A negative price means the market will
pay you to take an item away, and charge you to hand one over — the correct quote for
something genuinely worth less than nothing, where the real cost is disposal rather than
acquisition. In a survival economy with automated farms, that describes a lot of bulk
material.

The support for it is deliberate and visible throughout the pricing path:

- `Markets.getTickSizeAt` operates on `Math.abs(price)`, so the tick scale is mirrored
  around zero and the ladder keeps a sane step size on either side of it.
- `MarketMenu` computes `minimumBalance` as a **signed** quantity and skips the
  affordability check when it is negative, because a negative-price buy requires no funds.
- The same file says so outright: `// Calculations are complex to allow negative price trades`.
- Nothing in `Order`, `OrderChain` or either market clamps a price at zero.

None of that is accidental — quoting below zero is a designed capability, and the arithmetic
was written to carry the sign through. What *was* accidental is covered in
[Bugs and hotpatches](#bugs-and-hotpatches): the feature shipped before the sell side
checked whether a player could settle a trade that charged them.

### How the ladder reaches below zero

In practice negative quotes appear whenever the anchor sits closer to zero than the bid
ladder is deep.

Worked example — a `MODERATE` market (10 steps) anchored at `basePrice = 5`. Tick size
below 100 is 1, so the bid ladder starts at `5 - 1 = 4` and walks down:

```
step:   0   1   2   3   4   5   6   7   8   9
price:  4   3   2   1   0  -1  -2  -3  -4  -5
```

Half the ladder rests at or below zero. On a `SPECULATIVE` market (20 steps) the tail
reaches `-15`. Any cheap commodity — anything whose traded average sits in single digits —
grows a permanent shelf of zero and negative bids, and they persist on the book until
consumed, because `updateServerOrders()` only rebuilds once the book is 80% drained.

Three choices combine to produce this:

1. Tick size uses `Math.abs`, so it never shrinks as the price approaches zero.
2. The ladder is a fixed step count, not a proportion of the anchor.
3. Neither the ladder nor `Order` clamps price at zero.

The effect is that cheap goods get a standing bid shelf reaching below zero, and the market
discovers a negative clearing price on its own the moment supply outruns demand there. No
operator action sets it — the book finds it.

### What a negative price means

A negative price is not rejected anywhere in the transaction path — it flows straight
through the same arithmetic as a positive one, with the sign inverting who pays whom:

| Side | Price > 0 | Price < 0 |
|---|---|---|
| **Buy** | Player pays, receives items | Player **is paid** and receives items |
| **Sell** | Player is paid, gives up items | Player **pays** and gives up items |

Selling into a negative bid is the direction that needed guarding. `amountToDeposit` goes
negative, `modifyBalance` is handed a negative delta, and fees — subtracted regardless of
sign — push it further down.

The buy side handles it gracefully. `minimumBalance` is computed signed, and the
affordability check is skipped entirely when it comes out negative, because a negative-price
buy needs no funds:

```java
double absoluteVolume = Math.abs(pqr.volume());
double safetyMargin   = absoluteVolume * BUY_SAFETY_MARGIN;   // 0.005
double minimumBalance = pqr.volume() + safetyMargin;

if (minimumBalance > 0 && account.getBalance() < minimumBalance) { /* reject */ }
```

The sell side had no equivalent, which is what broke.

### What the market actually priced

Observations from the live server, recorded here because they are the clearest evidence the
mechanism worked as intended. These are operator observations of a running economy, not
values derivable from the code.

**Iron ingots settled at a negative fair market price.** The book found a sustained clearing
price below zero — the market paid players to take iron away. This is the designed behaviour
arriving on its own: iron is the most automatable material in the game, and once players had
mob farms running, supply so far outran demand that disposal became the service being priced.
Iron is also cheap enough per unit that its anchor sat well inside the range where the bid
ladder extends past zero, so the negative shelf was there to be found.

**Diamonds priced significantly lower than on comparable servers**, and **netherite
significantly higher**. Both are consequences of pricing by order book rather than by
operator fiat. A hand-set shop price tends to inherit convention — what diamonds "should"
cost, carried over from server to server. An order book has no such anchor: it prices what
is actually scarce for the players on that server. Diamonds are renewable at volume once
players are established, so the book marked them down. Netherite is gated behind ancient
debris, has no farm, and is consumed permanently by upgrades, so the book marked it up.

The ranking that emerged — netherite far above diamond, diamond below convention, iron below
zero — tracks genuine scarcity more closely than the price lists it diverged from. That
divergence is the feature working, not a calibration error. It is also the reason
`defaultPrice` matters so little in the end: it seeds an empty book, and from the first
fulfilment onward `getWeightedAveragePrice()` takes over and the market re-anchors to
whatever players will actually trade at.

---

## Bugs and hotpatches

### 1. Negative balance on negative-price sells

**Commit:** `3292fe4` — *Prevent negative balance on negative price sells*, merged as
PR #7 from `codex/add-exception-for-negative-balance-in-activepricemarket` (`bf2a01d`),
2025-09-20.

**The bug.** `ActivePriceMarket.sell()` computed a deposit and applied it unconditionally.
When the book's best bids were negative the deposit was negative, and nothing stopped it:

- `MarketMenu.onSellClick` checks economy freeze, wallet freeze and item count — **but never
  balance**. It is the buy path that holds the affordability check, not the sell path.
- `SynchronizedAccount.modifyBalance` validates only that the change is finite:

  ```java
  if (!Double.isFinite(change)) throw new IllegalArgumentException("Balance must be finite.");
  double balanceBefore = balance;
  balance += change;
  ```

  There is no non-negative floor at the account layer, by design — balances are free to go
  negative so the economy can represent debt.

With no guard at either end, a player could sell items into a negative bid and be driven
below zero, paying money they did not have for the privilege of giving away items. On a
cheap commodity with a deep negative shelf this needed no exploit or timing — just clicking
*sell all*.

**The fix.** Eleven lines in `ActivePriceMarket.sell()`, placed after the economy-freeze
check and before the order is placed:

```java
double estimatedVolume          = Math.abs(pqr.volume());
double estimatedFees            = estimatedVolume * Markets.getActiveFeeRateFor(account);
double estimatedAmountToDeposit = Math.floor(pqr.volume() - estimatedFees);

if (estimatedAmountToDeposit < 0) {
    double requiredAmount = -estimatedAmountToDeposit;
    if (account.getBalance() < requiredAmount) {
        return new OrderResult(this, account, pqr.quantity(), 0, pqr.price(), 0);
    }
}
```

It pre-prices the trade from the same query the caller already made, and if the result is a
net debit the account cannot cover, it returns a zero-fill `OrderResult` rather than
throwing. The GUI's sell loop treats a zero fill as a stop condition:

```java
if (fulfilled <= 0) break;
```

so the order simply stops rather than erroring. Negative-price sells remain possible — they
are a legitimate market state — but only for accounts that can actually settle them.

Note that the guard mirrors the real calculation exactly, including `Math.floor` and the
account's skill-adjusted fee rate, so the estimate and the eventual charge agree.

### 2. Fixed markets defaulting to a live sell price

**Commit:** `f98689a` — *v1.0.2 - Fixed price market creation defaults to sell price of -1*,
2025-08-31.

**The bug.** `CreateMarketCommand` takes an optional sell-price argument. When it was
omitted or unparseable, the fallback reused the market's initial buy price:

```java
} catch (IllegalArgumentException | IndexOutOfBoundsException e) {
    fpm.setSellPrice(initialPrice);
}
```

So creating a fixed market without naming a sell price silently opened a buy-back window at
the full purchase price — a zero-spread round trip, and with the 1% fee the only friction,
an admin creating a shop the short way handed players a near-free item sink and money
source.

**The fix.** One character of intent — fall back to the disabled sentinel instead:

```java
fpm.setSellPrice(-1);
```

Since `isSellable()` is `sellPrice >= 0`, the market now defaults to buy-only and an admin
must opt in to buy-back explicitly.

### 3. Market UI ignoring item storage on sell

**Commit:** `bc776e4` — *Include stored items when selling from market UI*, merged as PR #19
from `codex/update-shop-ui-to-include-inventory` (`c489eac`), 2025-11-02. 164 insertions in
`MarketMenu.java`.

**The bug.** The sell path counted only the player's inventory, so items held in account
`ItemStorage` were invisible to the market UI — *sell all* left stored stock behind.

**The fix.** The sell handler now totals both sources and pulls from storage as it goes:

```java
long initialInventoryCount = MX.countItems(player.getInventory(), template);
long storageCount          = storage.getAmount(template);
long totalAvailable        = initialInventoryCount + storageCount;
```

The rewritten loop withdraws from storage into free inventory space in batches, selling each
batch and tracking `deliveredFromStorage`, stopping early if space runs out (`lackedSpace`)
or a fill comes back short. The batching exists because `market.sell()` takes a `long` but
`ItemStack` amounts are `int`-bounded, so large sells are chunked.

---

## Residual observations

Not bugs with a paper trail — things the current code does that are worth knowing. These are
read off `HEAD`, not drawn from history.

**Guarding lives at two different layers.** Sells are guarded inside the model
(`ActivePriceMarket.sell`), buys are guarded in the view (`MarketMenu.onBuyClick`).
`ActivePriceMarket.buy()` performs no balance check of its own, so any future caller that
isn't `MarketMenu` — a command, an API consumer, an automated task — would bypass
affordability entirely. The two calls into the market model are both in `MarketMenu` today,
so nothing is exposed, but the asymmetry is load-bearing and undocumented in the code.

**The guard is all-or-nothing.** A player who can cover part of a negative-price sell is
rejected outright rather than filled up to what they can afford. Deliberate or not, it is
the safer direction to fail.

**Estimate and execution are decoupled in principle.** The guard prices the trade from
`getSellPrice()`, then the actual charge uses `order.getPriceFulfilled()` after matching.
These agree only because both run on the main server thread with no interleaving —
`MarketUpdateTask` is a Bukkit scheduler task and GUI clicks are main-thread events, so the
book cannot shift between query and execution. If any market path is ever moved
off-thread or made async, this guard stops being sound.

**The ladder's depth below zero is incidental even though the feature is not.** Negative
pricing is intended, but *how far* the ladder reaches past zero is a side effect of a fixed
step count rather than a tuned parameter: a `MODERATE` market anchored at 5 puts five rungs
below zero, and a `SPECULATIVE` one reaches `-15`, for no reason other than arithmetic. If
the depth of the negative shelf ever needs controlling, scale `numSteps` to the anchor
price — that bounds it without disabling the feature. Clamping at zero would disable it.

**`OrderChain.getMarketPrice()` defends against the wrong failure mode.** It reads the top
of each side and then null-checks the results:

```java
Order firstBuy  = buyOrders.getFirst();
Order firstSell = sellOrders.getFirst();

if (firstBuy == null) { ... }
```

But `buyOrders` is an `ArrayList` behind `Collections.synchronizedList`, and
`List.getFirst()` throws `NoSuchElementException` on an empty list rather than returning
`null`. The null branches are unreachable, and the empty-book case they were written to
handle would throw one line earlier. Harmless today: the method has no callers anywhere in
the plugin, and the mid-price it computes is unused — `getWeightedAveragePrice()` is what
`updateServerOrders()` actually anchors on. Worth fixing or deleting before anything starts
calling it.
