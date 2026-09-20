# Findings from the final server data

An analysis of the Mirae server's `plugins/Mirae` data directory as it stood when the server
shut down, plus the 38,728 autosave archives covering its entire operational life.

> **Anonymization.** No usernames, UUIDs or other identifying values appear in this document.
> Accounts are labelled `User A`, `User B`, … ranked by closing balance (`User A` = highest).
> The mapping is not reproduced here. Market names are item identifiers, not people.

> **Provenance.** Everything below is computed from the data files. Where a finding is an
> interpretation rather than a measurement, it says so.

## Table of contents

- [What was analysed](#what-was-analysed)
- [Server lifetime](#server-lifetime)
- [The August hyperinflation and reset](#the-august-hyperinflation-and-reset)
- [The save-loop anomaly](#the-save-loop-anomaly)
- [The economy at shutdown](#the-economy-at-shutdown)
- [Skills](#skills)
- [Market prices](#market-prices)
- [Evidence for the negative-balance bug](#evidence-for-the-negative-balance-bug)
- [Data integrity issues](#data-integrity-issues)
- [Feature adoption](#feature-adoption)
- [Caveats](#caveats)

---

## What was analysed

| Source | Count |
|---|---|
| `data.json` | 1 global state file |
| `accounts/*.json` | 188 accounts |
| `markets/*.json` | 335 markets |
| `backups/*.zip` | 38,728 archives |

Each archive is a complete snapshot — `data.json` plus every account and market file at that
instant. The final archive contains 524 files. This makes the backup directory a full
time series of the economy at roughly five-minute resolution for 107 days.

The trend analysis below samples one archive per day (103 usable days) and reads
`trackedBanknoteIssuance`, every account balance, and the `recentPrice` of twelve commodity
markets from each.

---

## Server lifetime

| | |
|---|---|
| First autosave | 2025-08-01 19:49:34 |
| Last autosave | 2025-11-17 06:00:24 |
| Span | 107.4 days |
| Archives | 38,728 |
| Uptime (gaps > 15 min counted as down) | **92.2%** |

Four outages exceeded an hour:

| Started | Duration |
|---|---|
| 2025-08-10 13:13 | 140.9 h (5.9 days) |
| 2025-08-16 12:23 | 47.5 h |
| 2025-11-09 22:03 | 6.7 h |
| 2025-09-01 06:00 | 1.4 h |

The two long August outages sit inside the incident described next.

---

## The August hyperinflation and reset

The money supply over the server's first month:

| Date | Total money | Accounts | Markets |
|---|---:|---:|---:|
| 2025-08-01 | 115,850,222 | 23 | 0 |
| 2025-08-06 | 737,574,545 | 30 | 80 |
| 2025-08-07 | 1,100,010,414,121,287 | 30 | 85 |
| **2025-08-08** | **2.09 × 10⁴⁰** | 30 | 87 |
| 2025-08-10 | 2.09 × 10⁴⁰ | 30 | 87 |
| *(140.9 h outage)* | | | |
| 2025-08-16 | 6.41 × 10³⁹ | 30 | 87 |
| 2025-08-18 | 2.11 × 10²⁷ | 30 | 89 |
| 2025-08-24 | 2.11 × 10²⁷ | 30 | **0** |
| 2025-08-26 | 2.11 × 10²⁷ | 30 | 68 |
| 2025-08-27 | 1.00 × 10²⁴ | 30 | 68 |
| **2025-08-28** | **931,135,135** | **11** | 86 |
| 2025-08-31 | 310,811,593 | 16 | 165 |

The supply rose through **31.5 orders of magnitude in 48 hours**, peaking at 2.09 × 10⁴⁰ — a
41-digit figure, in an economy whose eventual steady state was around 2 × 10¹⁰. It then came
down in discrete steps (10⁴⁰ → 10³⁹ → 10²⁷ → 10²⁴ → 10⁸) rather than continuously,
with every market deleted on 2025-08-24 and rebuilt from 2025-08-26, before a reset on
2025-08-28 dropped account count from 30 to 11 and money to 931 M.

**Interpretation.** The stepwise decline and the shape of the numbers point to deliberate
admin intervention rather than a runaway exploit. The intermediate values are strikingly
regular — `2,111,111,111,011,269,772,008,488,960` and `1,100,010,414,121,287` — repeating-digit
patterns characteristic of values typed by hand into `/setmoney` or `/changemoney`, not of
compound interest or a duplication loop. Combined with a stable 30-account population
throughout, the total market wipe on 08-24, and the population drop to 11 on 08-28, the
most economical reading is a **pre-launch testing phase that was wiped for production
launch** — not an incident that cost players their savings. The parent directory being named
`Capitalism6` supports this: the shutdown data is the sixth iteration of the economy.

Everything from 2025-08-28 onward looks like a single continuous production run: accounts
climb monotonically from 11 to 188, markets from 86 to 335, and money from 931 M to 22 B
without further discontinuity.

---

## The save-loop anomaly

`AutoSaveTask` is scheduled at `20 * 60 * 5` ticks — one save every five minutes — and the
only other save path is `onDisable`. The gap distribution between consecutive archives:

| Gap | Count | Share |
|---|---:|---:|
| < 60 s | 11,077 | 28.6% |
| 60–290 s | 87 | 0.2% |
| ~5 min (290–310 s) | 25,019 | **64.6%** |
| 310 s – 1 h | 2,540 | 6.6% |
| > 1 h | 4 | 0.0% |

The 64.6% at exactly five minutes is the task behaving as designed. The 28.6% under a minute
should not be possible, and it is almost entirely one event:

- **11,056 of the 11,077** sub-minute saves fall in a single unbroken run across
  2025-08-19 and 2025-08-20.
- The most common gaps inside that run are **6 seconds (6,472 times)** and
  **5 seconds (3,878 times)**.
- Outside that window, sub-minute saves occur 21 times in 107 days.

A crash-restart loop would not produce 5–6 second cycles — a Paper server cannot start and
stop that fast. A save firing every 5–6 seconds instead of every 300 is consistent with the
autosave timer having been registered many times over, which is what repeated plugin reloads
against a single `Tasks.registerTasks` call would do: each reload adds another timer, and
fifty concurrent timers on a 300-second period average one save every six seconds.

This is interpretation, not measurement — the archives record only timestamps. But the run
sits squarely inside the pre-launch window and immediately before the economy reset, which
fits a period of heavy reload-driven iteration.

Worth noting regardless: **each save writes a full zip of every account and market first**.
That run produced roughly 11,000 archives in 19 hours.

---

## The economy at shutdown

### Money

| | |
|---|---:|
| Total money in circulation | 22,131,176,828 |
| Accounts | 188 |
| Mean balance | 117,719,026 |
| **Median balance** | **0** |
| Highest balance | 11,166,918,628 |
| Lowest balance | −3,386 |
| Accounts with exactly zero | 116 (61.7%) |
| Accounts with a positive balance | 71 (37.8%) |
| Accounts above 1 M | 25 |
| Accounts above 1 B | 3 |

Banknotes in circulation totalled 13,630,001 — about **0.06%** of the money supply. The
physical-cash feature saw real but marginal use.

### Concentration

| Holders | Share of all money |
|---|---:|
| Top 1 | 50.5% |
| Top 3 | 91.5% |
| Top 5 | 95.3% |
| Top 10 | 99.1% |
| Top 20 | 99.9% |

**Gini coefficient: 0.984.**

That figure needs a caveat. `User A` is a `DEVELOPER`-tier account, and developer accounts
hold 51.0% of all money between them. Excluding both developer accounts:

| | |
|---|---:|
| Player-held money | 10,852,887,357 |
| Top player's share of it | 55.3% |
| Top 3 players' share | 88.5% |
| Gini (players only) | **0.984** |

The inequality is not an artifact of admin balances — it is the same either way. Three
players held essentially the entire player economy, and roughly six in ten accounts finished
with nothing at all.

### Population and retention

| | |
|---|---:|
| Accounts ever created | 188 |
| Active within 30 days of shutdown | 59 |
| Active within 7 days | 17 |
| Active within 1 day | 6 |

Account creation continued to the end — 176 accounts by 2025-10-30, 188 by shutdown — so the
server was still acquiring players while its active population sat near a dozen.

---

## Skills

| Skill | Players above 0 | Max | Mean (of those above 0) |
|---|---:|---:|---:|
| `CARPENTRY` | 56 | 82 | 5.9 |
| `MINING` | 54 | 314 | 25.5 |
| `FARMING` | 16 | 1,093 | 93.6 |
| `TRADING` | 16 | 1,027 | 138.0 |
| `UPGRADING` | 1 | 10 | 10.0 |

Two distinct shapes. `MINING` and `CARPENTRY` are broad and shallow — about a third of all
accounts touched them, nobody went deep. `FARMING` and `TRADING` are narrow and extremely
deep: a sixth as many players, but maxima over a thousand.

Spearman rank correlation against closing balance:

| Skill | ρ |
|---|---:|
| `MINING` | +0.729 |
| `CARPENTRY` | +0.656 |
| `FARMING` | +0.498 |
| `TRADING` | +0.484 |
| `UPGRADING` | +0.330 |

`MINING` correlates most strongly across the whole population, but that reflects *engagement*
more than wealth generation — mining is what active players did, and active players had money.
The tail tells a different story: the two highest `TRADING` levels on the server (1,027 and
1,012) belong to the two wealthiest accounts, and the highest `FARMING` level (1,093) belongs
to the third. Broad participation ran through mining; the fortunes were made in trading and
farming.

`UPGRADING` was effectively unused — one player, level 10.

---

## Market prices

335 markets at shutdown: 169 active-price, 166 fixed-price.

**Fixed markets confirm the `f98689a` default took effect.** Of 166, **158 have
`sellPrice < 0`** — the disabled sentinel — and only 7 have both sides enabled. Markets
created after that commit default to buy-only, exactly as intended.

### Closing prices

| Item | Close | Notes |
|---|---:|---|
| `netherite_ingot` | 49,200 | Stable all run |
| `gold_ingot` | 700 | Rose from 380 |
| `emerald` | 240 | Volatile: 800 → 4 → 240 |
| `coal` | 57 | Slow decline from 86 |
| `copper_ingot` | 44 | Declined from 69 |
| `diamond` | **19** | Collapsed from 860 |
| `iron_ingot` | 3 | Average fill price **−0.5** |
| `cobblestone` | 3 | |
| `wheat` | 0 | |
| `carrot` | −2 | |
| `potato` | −2 | |
| `dirt` | **−14** | |

Four active markets closed at a negative `recentPrice`: `dirt` (−14), `carrot` (−2),
`potato` (−2) and one `enchanted_book` market (−1). Three closed at exactly zero.

### Iron went negative — confirmed

The iron ingot market's persisted fulfilment history gives an **average fill price of −0.5**
across 128 recorded fills totalling 6,174 units. Iron did not merely dip below zero; its
mean realised trade price over the retained window was negative. It first touched zero or
below on 2025-09-21 and spent much of the run there, reaching **−81 on 2025-11-01** and −66
on 2025-11-06 before closing at +3.

This is the mechanism in MARKETS.md working exactly as described, and the configuration
explains the magnitude. The iron market's volatility level is **`VERY_STABLE`**, which is 20
ladder steps — the joint-deepest setting in the enum. With the anchor in low single digits
and a tick size of 1 below 100, a 20-step bid ladder reaches roughly 17 units below zero. The
observed −81 is deeper still, implying the anchor itself had gone negative and the ladder
descended from there.

The commodities that went negative are precisely the ones players can automate: iron, dirt,
wheat, carrot, potato, cobblestone. The ones that never did are the ones that stay scarce:
diamond, netherite, gold, copper, coal, emerald. The order book priced abundance correctly
without anyone configuring it to.

### Diamond collapsed, netherite did not

| Date | Diamond | Netherite | Ratio |
|---|---:|---:|---:|
| 2025-08-16 | 910 | 49,650 | 55× |
| 2025-09-06 | 850 | 50,100 | 59× |
| 2025-09-16 | 15 | 50,200 | **3,347×** |
| 2025-10-16 | 20 | 49,200 | 2,460× |
| 2025-11-15 | 19 | 49,200 | **2,589×** |

Diamond fell 98% in the five days around 2025-09-11 to 2025-09-16 (860 → 63 → 15) and never
recovered, settling in the teens. Its configured `defaultPrice` was 2,000; it closed at 19,
about 1% of the seeded value.

Netherite moved barely at all — 50,100 to 49,200 over ten weeks, under 2% drift — and its
`defaultPrice` of 50,000 turned out to be almost exactly right.

The ratio between them went from **55× at the admin-seeded defaults to 2,589× at
market-discovered prices**, a fifty-fold repricing. This is the clearest single piece of
evidence that the order book was doing real work: left to the seeded numbers, netherite was
worth 55 diamonds; left to players, it was worth over 2,500. Diamonds are renewable at volume
once a player is established. Netherite is gated behind ancient debris, cannot be farmed, and
is consumed permanently by upgrades. The book found that difference on its own.

---

## Evidence for the negative-balance bug

The count of accounts with a negative balance, per daily snapshot:

| Date | Negative accounts |
|---|---:|
| 2025-09-09 | 0 |
| **2025-09-10** | **1** ← first appearance |
| 2025-09-15 | 1 |
| 2025-09-16 | 2 |
| 2025-09-17 | 1 |
| 2025-09-19 | 2 |
| **2025-09-20** | 2 ← `3292fe4` merged 01:26 |
| 2025-09-21 | 2 |
| **2025-09-22** | **1** |
| … through 2025-11-17 | 1 |

The first negative balance appears on 2025-09-10, ten days before the guard was added. The
count fluctuates between 1 and 2 through the pre-fix window. After the fix, it reaches 2 on
exactly one day (2025-09-21, the day after the merge) and then holds at 1 permanently for the
remaining 57 days.

One account — `User GF`, last seen 2025-09-16, four days before the patch — closed the
server's life at **−3,386**. It went underwater during the vulnerable window, never logged in
again, and was never corrected. The lone residual negative balance in the final data is a
direct artifact of the bug MARKETS.md documents.

The shape of the data matches the fix cleanly: transient negatives before, one permanent
casualty, nothing new after.

---

## Data integrity issues

**`NaN` is persisted into the JSON files.** Five markets closed with `recentPrice` literally
serialized as `NaN`:

```json
"orderChain": {
    "recentPrice": NaN,
    "fulfillmentStack": []
}
```

All five are wool variants (`orange`, `black`, `light_blue`, `brown`, `magenta`) with empty
fulfilment stacks — markets that were created and never traded. `NaN` is **not valid JSON**;
the specification has no such literal. Gson writes and reads it in lenient mode, so the
plugin round-trips these files fine, but any standards-compliant parser will reject them.
Anything that ever needs to read this data outside the plugin — a web dashboard, an analytics
script, a migration tool — will fail on those five files.

The plugin itself is defended against the value: `updateServerOrders()` checks
`Double.isFinite(averageRaw)` and falls back to `defaultPrice`, so a `NaN` anchor degrades
gracefully rather than poisoning the ladder. The defence is in the right place; the
serialization is not.

**Custom items persist as their base vanilla material.** Three separate markets share the
item id `minecraft:iron_ingot`:

| Market name | Volatility | Close |
|---|---|---:|
| `iron_ingot` | `VERY_STABLE` | 3 |
| `uranium_ingot` | `ILLIQUID` | 83,500 |
| `vibranium_ingot` | `RARE` | 147,900 |

The two custom ItemsAdder items serialize with `"id": "minecraft:iron_ingot"` and are
distinguished only by NBT. Item matching uses `isSimilar`, which compares NBT, so the plugin
behaves correctly — but any analysis keyed on `item.id` alone silently conflates them, and a
price chart built that way would show iron ingots trading at 147,900.

(Note that `vibranium_ingot` is a stored market name and is unaffected by the source-level
rename to darksteel — the data still carries the old label.)

**`VERY_STABLE` is a real enum value.** Worth recording because it is easy to miss: the
`VolatilityLevel` enum declares `VERY_STABLE(20, 4)` as its first constant, and 23 markets use
it. An earlier pass of MARKETS.md omitted it from the volatility table; that has been
corrected. The name is misleading — it means "deep, thick book", not "stable price", and it
is the setting most likely to plant bids below zero on a cheap commodity. It is what the iron
market was configured with.

---

## Feature adoption

Measured across all 188 accounts at shutdown:

| Feature | Accounts using it |
|---|---:|
| Item storage (non-empty) | 10 (453 stacks total) |
| Homes set | 43 |
| Extra home slots purchased | 28 |
| Titles owned beyond the default | 10 |
| Title actually equipped | 7 |
| Custom display name | 6 |
| Local chat enabled | 6 |
| Scoreboard hidden | 8 |
| Custom prefix | 3 |
| Ignoring another player | 3 |
| Daily attendance recorded | 27 |
| Coins held | 6 accounts, 6,744 coins |
| Mail received | **1 account, 1 message** |
| Muted at shutdown | 0 |
| Wallets frozen at shutdown | 0 |

The starter kit reached 187 of 188 accounts, so nearly everyone completed first contact. After
that, adoption falls off sharply — most of the social and cosmetic surface was touched by
under 5% of accounts, and the mail system carried a single message in 107 days.

Item storage is the interesting exception: only 10 accounts used it, but those 10 stored 453
stacks between them, with the largest holding 196. It was a power-user feature, used heavily
by the few who found it.

Two accounts hold `DEVELOPER` tier, one `GOLD`, one `GREEN`, and the remaining 184 `DEFAULT`
— the tier system existed but was essentially unused in production.

---

## Caveats

**Fulfilment history is truncated.** `OrderChain` persists only `recentPrice` and a bounded
`fulfillmentStack`; the order lists themselves are `transient` and rebuilt on load. Most
markets show exactly 128 retained fills, which is a cap, not a total. Volume figures here are
therefore lower bounds on what actually traded, and the "average fill price" for iron is an
average over the retained window, not over the server's life.

**Daily sampling hides intraday movement.** The trend tables take one snapshot per day from a
five-minute archive series. Short-lived price spikes and balance swings between samples are
invisible. The full-resolution data remains in the archives if finer analysis is ever wanted.

**Lifetime activity cannot be reconstructed.** Accounts store only `lastSeenTime`, so
"active within N days" measures recency, not session count or playtime. A player who logged
in daily for a month and a player who logged in once are indistinguishable if they stopped on
the same day.

**The August reading is inference.** The hyperinflation and reset are measured; the
explanation — pre-launch testing rather than an exploit — is interpretation from the shape of
the numbers, the stable account count, and the directory name. No logs were available to
confirm it.

**No server logs were examined.** Everything here comes from plugin state. Chat, commands,
logins, crashes and console output are not represented in this data, and the save-loop
explanation in particular would be settled immediately by a server log from 2025-08-19.
