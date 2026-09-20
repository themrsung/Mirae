# Special enchants

Mirae ships five custom enchantments. None of them are Bukkit `Enchantment` registrations —
they are integers in an item's persistent data container, read by listeners and scheduled
tasks that apply the effect themselves.

> Read from the code as it stands at `HEAD`.

## Table of contents

- [The five enchants](#the-five-enchants)
- [How they are stored](#how-they-are-stored)
- [How they are obtained](#how-they-are-obtained)
- [Implementations](#implementations)
  - [3x3 mining](#3x3-mining)
  - [Super shovel](#super-shovel)
  - [Seeker bow](#seeker-bow)
  - [Booster bow](#booster-bow)
  - [EMP shield](#emp-shield)
- [Quirks and gaps](#quirks-and-gaps)

---

## The five enchants

`CustomEnchantment.Value` is the enum of every custom enchant in the plugin:

| Constant | Key | Display name | Natural max | Applies to |
|---|---|---|---|---|
| `THREE_BY_THREE_MINING` | `mirae.enchantment.three_by_three` | `3x3 채굴` | 1 | Pickaxes |
| `SUPER_SHOVEL` | `mirae.enchantment.super_shovel` | `자갈 관통` | 1 | Pickaxes |
| `SEEKER_BOW` | `mirae.enchantment.seeker_bow` | `화살 유도` | 1 | Bow, crossbow |
| `BOOSTER_BOW` | `mirae.enchantment.booster_bow` | `화살 추진` | 3 | Bow, crossbow |
| `EMP_SHIELD` | `mirae.enchantment.emp_shield` | `전자기파 방패` | 1 | Shields, `HERO_SHIELD` |

Four are declared with the two-argument constructor, which defaults the natural max to 1:

```java
Value(@NotNull String key, @NotNull String displayName) {
    this(key, Component.text(displayName).style(MX.STYLE_NORMAL), 1);
}
```

`BOOSTER_BOW` is the only one declared with an explicit max, `3`. See
[Quirks and gaps](#quirks-and-gaps) — that number is never enforced, and never reached
through normal play.

Implementations are split across two packages by mechanism: `listener/enchant/` for the
block-breaking pair, which are event-driven, and `task/item/` for the projectile trio, which
need to act on entities already in flight.

---

## How they are stored

An enchant is one integer under a namespaced key on the item's meta:

```java
Integer result = container.get(new NamespacedKey(Mirae.getInstance(), key), PersistentDataType.INTEGER);
```

`hasEnchant(item)` is `hasEnchant(item, 1)` — level ≥ 1 counts as present. `setEnchantLevel`
writes the value (removing the key when the level drops to zero) and then calls
`updateEnchantmentLore(item)`.

Lore is regenerated rather than appended. Every known enchant's display text is stripped from
the existing lore first, then the current set is re-rendered as `<display name> <level>`:

```java
Arrays.stream(Value.values()).forEach(v -> {
    String enchantmentName = ((TextComponent) v.getDisplayName()).content();
    lore.removeIf(l -> ((TextComponent) l).content().contains(enchantmentName));
});
```

This keeps lore idempotent across repeated enchanting, at the cost of assuming every lore
line is a `TextComponent` — a line built any other way would throw on the cast.

`incrementEnchantLevel` and `decrementEnchantLevel` exist as helpers; decrement floors at
zero (`Math.max(current - 1, 0)`), increment does not cap.

---

## How they are obtained

Two recipes in the `/upgrade` bench, both with `getSuccessRate() == 1` — neither can fail.

**Acquisition** — `CustomEnchantAcquisitionRecipe` rolls a random enchant onto a book:

- Left slot: `BOOK` or `ENCHANTED_BOOK`
- Right slot: exactly one `NETHER_STAR`

It collects the enchants the book does **not** already have, shuffles, takes the first, and
writes it at level 1. A plain book is converted to an enchanted book; an enchanted book is
cloned and added to. If the book already carries all five, the recipe returns `null` and the
bench rejects it.

```java
List<CustomEnchantment> possibleEnchantments = Arrays.stream(CustomEnchantment.Value.values())
        .filter(e -> !existing.contains(e))
        .map(v -> (CustomEnchantment) v)
        .toList();
...
Collections.shuffle(enchants);
enchant.setEnchantLevel(result, 1);
```

**Application** — `CustomEnchantApplicationRecipe` transfers from book to tool:

- Left slot: anything whose meta is `Damageable`
- Right slot: an `ENCHANTED_BOOK`

Every enchant on the book is checked against the target's type before transferring:

```java
if ((e == SUPER_SHOVEL || e == THREE_BY_THREE_MINING) && !PICKAXES.contains(left.getType())) return;
if ((e == SEEKER_BOW   || e == BOOSTER_BOW)           && !LAUNCHERS.contains(left.getType())) return;
if (e == EMP_SHIELD && !isShield(left)) return;
if (e.getEnchantLevel(left) >= l) return;   // no downgrades, no redundant applies
```

`PICKAXES` is the five vanilla tiers; `LAUNCHERS` is `BOW` and `CROSSBOW`; `isShield` accepts
vanilla `SHIELD` or the custom `HERO_SHIELD`. If nothing transferred, the recipe returns
`null` rather than consuming the book.

Admins can set any level directly with `/enchant <name> <level>` (`EnchantCommand`, op-gated).
The same command also toggles `unbreakable`.

---

## Implementations

### 3x3 mining

`listener/enchant/ThreeByThreeMiningListener` — `BlockBreakEvent`.

Breaks the 3×3 plane perpendicular to the face being mined — the eight blocks surrounding the
one the event is already breaking. The plane is chosen by switching on
`player.getTargetBlockFace(5)`, so a downward dig takes a horizontal slab and a horizontal dig
takes a vertical wall:

```java
switch (facing) {
    case UP, DOWN     -> { /* vary x and z */ }
    case NORTH, SOUTH -> { /* vary x and y */ }
    case EAST, WEST   -> { /* vary y and z */ }
}
```

Two protections apply per block, not per swing:

**Material blacklist** — a static `EnumSet` guarding against breaking the unbreakable and
against collateral damage to custom content:

```java
blacklist.add(Material.BEDROCK);
blacklist.add(Material.BARRIER);
blacklist.add(Material.VAULT);
blacklist.add(Material.REINFORCED_DEEPSLATE);
blacklist.add(Material.NOTE_BLOCK);   // Used by ItemsAdder
```

The `NOTE_BLOCK` entry matters: ItemsAdder encodes custom blocks as note block states, so
without it a 3×3 swing would shred custom furniture and machines adjacent to the target.

**Claim check** — every block is tested against GriefPrevention before breaking:

```java
Supplier<String> result = claim.checkPermission(e.getPlayer(), ClaimPermission.Build, e);
```

Blocks are broken with `breakNaturally(mainHandItem, true)`, so drops and durability respect
the tool actually held.

### Super shovel

`listener/enchant/SuperShovelListener` — `PlayerInteractEvent`, left click only.

Instant-breaks soft ground in one click, bypassing the dig animation:

```java
if (!e.getAction().isLeftClick()) return;
...
if (!TARGET_BLOCKS.contains(block.getType())) return;

player.breakBlock(block);
e.setCancelled(true);
```

`TARGET_BLOCKS` covers `GRASS_BLOCK`, `DIRT`, `DIRT_PATH`, `ROOTED_DIRT`, `COARSE_DIRT`,
`FARMLAND`, `SAND`, `RED_SAND`, `SUSPICIOUS_SAND`, `GRAVEL`, `SUSPICIOUS_GRAVEL`, `PODZOL`
and `MYCELIUM`.

The enchant is read from the main hand only. Note that this listener performs **no claim
check** — unlike 3×3 mining, it relies on `player.breakBlock()` and the cancelled event, so
protection depends on whatever else handles the resulting break.

### Seeker bow

`task/item/ArrowSeekerTask` — scheduled every 4 ticks (`runTaskTimer(p, ARROW_SEEKER_TASK, 200, 4)`).

Homing arrows. Each pass sweeps live projectiles, keeping only `ARROW` and `SPECTRAL_ARROW`
still moving faster than `SEEKER_TERMINATION_VELOCITY = 0.01`, fired by a player whose bow
carries the enchant.

Guidance does not engage immediately — the arrow must first clear
`SEEKER_ACTIVATION_DISTANCE = 7` blocks from its shooter, so point-blank shots fly straight:

```java
if (playerArrowDiff.lengthSquared() < SEEKER_ACTIVATION_DISTANCE_SQUARED) return;
```

It then picks the nearest valid target within `SEEKER_SEEK_DISTANCE = 7` of the **arrow**,
excluding the arrow itself and the shooter. The candidate filter is the most characterful
piece of code in the plugin — it implements countermeasures:

```java
.filter(e -> e.getType().isAlive() ||               // Targets
             e.getType() == EntityType.PLAYER ||    // Targets
             e.getType() == EntityType.ITEM ||      // Chaff
             e.getType() == EntityType.FIREWORK_ROCKET || // Flares
             e.getType() == EntityType.END_CRYSTAL)
```

Dropped items act as chaff and fireworks as flares: because targeting is strictly
nearest-first, throwing a stack of dirt or launching a rocket pulls a homing arrow off a
player.

Steering preserves speed. A pull vector toward the target is added to current velocity, then
the sum is normalised and rescaled to the original magnitude — the arrow turns but never
accelerates:

```java
Vector directionToTarget = possiblyTarget.clone().subtract(arrowPos)
        .multiply(SEEKER_SEEK_VELOCITY * (boosterLevel + 1));   // 3 * (level + 1)
Vector sum           = velocityBefore.clone().add(directionToTarget);
Vector velocityAfter = sum.clone().normalize().multiply(velocityBefore.length());
```

The `boosterLevel` term couples the two bow enchants: a bow carrying **both** Seeker and
Booster turns harder, scaling the steering authority by `level + 1`. Booster alone makes
arrows fast; Seeker alone makes them turn; together they turn *and* keep up.

### Booster bow

`task/item/ArrowPropulsionTask` — scheduled every 3 ticks (`runTaskTimer(p, ARROW_PROPULSION_TASK, 200, 3)`).

Continuous in-flight acceleration along the arrow's existing heading:

```java
public static final double ACCELERATION = 0.5;
public static final double MAX_SPEED    = 50;
...
Vector p = v.normalize().multiply(Math.min(speed + ACCELERATION * level, MAX_SPEED));
```

Direction is untouched — only magnitude grows, `0.5` per level per tick of the task, capped
at `50`. This is the one enchant whose level is read as a magnitude rather than a boolean
(`if (level < 1) return;`), which is why it is also the only one with a natural max above 1.

### EMP shield

`task/item/EmpShieldTask` — scheduled every 2 ticks (`runTaskTimer(p, EMP_SHIELD_TASK, 200, 2)`),
the tightest interval of the three.

The shield is read from the **off hand** — `player.getInventory().getItemInOffHand()` — which
is where shields are held anyway, but it does mean a main-hand shield is inert.

It deflects incoming projectiles within `EMP_RANGE = 3` blocks, excluding the holder's own
shots:

```java
player.getNearbyEntities(EMP_RANGE, EMP_RANGE, EMP_RANGE).stream()
        .filter(e -> e instanceof Projectile)
        .map(e -> (Projectile) e)
        .filter(p -> !Objects.equals(p.getShooter(), player))
```

The deflection pushes each projectile along the vector pointing *away* from the player,
scaled by `DISTRACT_VECTOR_MULTIPLIER = 5`, with a small random jitter so repeated hits do
not deflect identically:

```java
Vector r = new Vector(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5)
        .normalize().multiply(0.1);
Vector d = projectile.getLocation().toVector()
        .subtract(player.getLocation().toVector())
        .add(r);                                        // Add randomness
Vector p = v.clone().add(d.clone().multiply(DISTRACT_VECTOR_MULTIPLIER));
Vector m = p.clone().normalize().multiply(v.length());

projectile.setVelocity(m);
```

Same speed-preserving idiom as the seeker: steer hard, then renormalise to the original
speed. The projectile is redirected, never slowed — a deflected arrow is still lethal to
whatever it now points at.

Unlike Seeker and Booster, this fires on **any** `Projectile`, not just arrows.

---

## Quirks and gaps

Things the code does that are worth knowing before touching it.

**Super shovel only applies to pickaxes.** The application recipe groups it with 3×3 mining
against the `PICKAXES` set:

```java
if ((e == SUPER_SHOVEL || e == THREE_BY_THREE_MINING) && !PICKAXES.contains(left.getType())) return;
```

So despite the name, and despite targeting dirt, sand and gravel — shovel materials — it
cannot be applied to a shovel. Adding a `SHOVELS` set and splitting the condition is a
two-line change if this was unintended.

**`getNaturalMaxLevel()` is never enforced or read.** It is declared on the interface,
implemented on the enum, and called from nowhere in the plugin. Nothing clamps against it —
`incrementEnchantLevel` has no cap, and `/enchant <name> <level>` writes whatever integer it
is given. `BOOSTER_BOW`'s `3` is documentation, not a constraint.

**Levels above 1 are unreachable in normal play.** The acquisition recipe hardcodes
`setEnchantLevel(result, 1)`, and the application recipe only transfers the book's existing
level. There is no combine-two-books-to-level-up path. `BOOSTER_BOW` levels 2 and 3 exist
only via the op-only `/enchant` command — so the only enchant with a meaningful level range
is one players cannot level up. Adding a level-up branch to the acquisition recipe (matching
book + book, incrementing on collision) is the natural fix, and `incrementEnchantLevel` is
already written and unused, apparently for exactly that.

**Enchanted books are explicitly excluded at every effect site.** All three tasks repeat the
same guard:

```java
if (possiblyBow.getType() == Material.ENCHANTED_BOOK) return;
```

Without it, holding an enchanted book would trigger the effect — the tasks read the item in
hand and test for the key, and a book carrying the enchant satisfies that test. The
block-breaking listeners do not need the guard because they are gated on block interaction
with a tool.

**Arrows are guided by the bow you are holding now, not the bow that fired them.** Both bow
tasks resolve the enchant from the shooter's *current* main-hand item every pass:

```java
ProjectileSource shooter = arrow.getShooter();
if (!(shooter instanceof Player player)) return;
ItemStack possiblyBow = player.getInventory().getItemInMainHand();
```

Nothing records which bow launched the arrow. Swapping to another item mid-flight silently
drops guidance and propulsion on arrows already in the air; swapping *to* an enchanted bow
picks up arrows fired from an unenchanted one. `BOOSTER_BOW`'s level is re-read the same way,
so the seeker's turn rate can change mid-flight too. Stashing the bow state on the arrow's
own persistent data at launch would fix it.

**The projectile tasks poll rather than hook events.** Seeker, Booster and EMP all sweep
world entities on a timer (every 4, 3 and 2 ticks respectively) instead of responding to
`ProjectileLaunchEvent` and tracking state. Simpler, and necessary for continuous steering,
but the cost scales with the number of live projectiles server-wide, not with the number of
players actually using the enchants. `EmpShieldTask` additionally allocates a `new Random()`
per projectile per pass, inside the `forEach` — hoisting it to a field would be free.

**Custom enchants do not interact with the vanilla enchanting table or anvils**, do not
appear in `/enchant` tab completion as vanilla enchants, and are invisible to any plugin
reading `ItemMeta.getEnchants()`. They exist only to code that knows the namespaced keys.
