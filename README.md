# Mirae

The server plugin that ran **Mirae Server**, a Korean-language Minecraft survival
server built around a player-driven economy.

Mirae is a single monolithic PaperMC plugin. Rather than assembling a stack of
third-party plugins, it implements the server's economy, markets, custom items,
skills, land-adjacent social systems and moderation tooling in one codebase with
one shared, transactional state object.

> **Status: archived.** The server is no longer running and this repository is
> published as a reference, not as a maintained product. See
> [Operational notes](#operational-notes) before deploying it anywhere.

---

## Table of contents

- [Requirements](#requirements)
- [Building](#building)
- [Installing](#installing)
- [Architecture](#architecture)
- [Features](#features)
  - [Economy](#economy)
  - [Banknotes](#banknotes)
  - [Markets](#markets)
  - [Custom items and upgrades](#custom-items-and-upgrades)
  - [Skills](#skills)
  - [Cooking](#cooking)
  - [Daily quests](#daily-quests)
  - [Social and teleportation](#social-and-teleportation)
  - [Moderation and anti-abuse](#moderation-and-anti-abuse)
- [Commands](#commands)
- [Persistence](#persistence)
- [Operational notes](#operational-notes)
- [Third-party assets](#third-party-assets)
- [License](#license)

---

## Requirements

| | |
|---|---|
| Java | 21 |
| Server | PaperMC 1.21 (`api-version: '1.21'`, built against `paper-api:1.21.5-R0.1-SNAPSHOT`) |
| Build | Maven 3.9+ |

**Required plugins** — the server will not start without these:

- [Vault](https://www.spigotmc.org/resources/vault.34315/) — Mirae *registers itself as*
  the Vault economy provider, so other plugins read balances through it.
- [ItemsAdder](https://www.spigotmc.org/resources/itemsadder.73355/) — supplies every
  custom item's texture and model. Paid resource.
- [GriefPrevention](https://www.spigotmc.org/resources/griefprevention.1884/) — claim
  lookups used by mining and chop-tree behaviour.

**Optional:**

- [PlayerAuctions](https://www.spigotmc.org/resources/playerauctions.61836/) — loaded
  after Mirae if present.

## Building

```bash
mvn clean package
```

Produces a shaded JAR in `target/`. The Maven Shade plugin bundles Gson; `paper-api`,
ItemsAdder, Vault and GriefPrevention are all `provided` scope and resolved from the
PaperMC, JitPack, devs.beer and matteodev repositories declared in `pom.xml`.

## Installing

1. Drop the shaded JAR into `plugins/`.
2. Install Vault, ItemsAdder and GriefPrevention.
3. Install the ItemsAdder content packs that provide the namespaces listed under
   [Third-party assets](#third-party-assets). **Without them, custom items will not
   resolve.**
4. Start the server. Mirae creates `plugins/Mirae/` on first run.

## Architecture

```
com.themrsung.mirae
├── Mirae.java        Plugin entry point — wires listeners, commands, tasks, Vault
├── MX.java           Shared styles, colours and message constants
├── state/            State interface + SynchronizedState (the single source of truth)
├── gson/             Serializers for state, ItemStacks, LocalDateTime, coordinates
├── account/          Per-player account: balances, tier, title, homes, settings
├── economy/          Currencies, transaction causes/results, Vault adapter
├── banknote/         Physical banknote items and HMAC signing
├── market/           Fixed- and active-price markets, orders, fulfillment
├── item/             Custom item definitions and registry
├── upgrade/          Upgrade/crafting recipes (weapons, tools, enchants, composition)
├── cooking/          Cooking recipes and station logic
├── skill/            Skill types and levelling
├── gui/              Inventory-based UIs
├── command/          ~70 commands, grouped by domain
├── listener/         Event handlers, grouped by domain
├── task/             Scheduled tasks (economy ticks, quests, autosave, anti-abuse)
├── social/           Direct messages, teleport requests
├── event/            Custom Bukkit events
└── webhook/          HTTP endpoint for external donation processing
```

Three design decisions shape the rest:

**One state object.** `Mirae.getState()` returns a process-wide `State`. The only
implementation, `SynchronizedState`, guards every mutation with locks, so commands,
listeners and scheduled tasks can all touch the economy concurrently without
interleaving writes.

**Registries over annotations.** `Listeners.getListeners()`, `Commands.getCommands()`
and `Tasks.registerTasks()` each return or register an explicit list. Adding a feature
means adding a line to a registry — nothing is discovered by reflection, so the whole
surface of the plugin is greppable.

**Commands are registered dynamically.** `Commands` registers into Paper's `CommandMap`
under the `mirae` namespace at enable time, which is why `paper-plugin.yml` declares no
`commands:` block.

## Features

### Economy

Two currencies are held per-account:

| Currency | Type | Purpose |
|---|---|---|
| **Money** (원) | `double` | Primary in-game currency. Exposed to other plugins via Vault. |
| **Coin** | `long` | Premium currency, credited by donation. |

A third concept, the **equity token**, is scaffolded but incomplete. The `EquityToken`
enum defines one token (`GAMMA` / 감마 토큰), and there is a `/changetoken` command, a
`AccountTokenBalanceModifiedEvent` and an `EquityTokenLongPair` Gson adapter — but
`Account` exposes no token balance API and the Gson pair is referenced nowhere. Treat it
as an unfinished feature rather than a working currency.

Every balance change carries an `EconomyCause` and returns an `EconomyResult`, so
transactions are auditable rather than silent arithmetic. Wallets can be frozen
per-account (`/freezeeconomy`), and `InterestPayoutTask` accrues interest on a schedule.
`/moneysupply` reports aggregate money in circulation.

### Banknotes

Money can be withdrawn into physical banknote items (`/withdraw`) and redeemed by use.
Each note is an ItemsAdder item carrying its denomination and a version in persistent
data, plus an **HMAC-SHA256 signature** so forged or edited notes are rejected on
redemption. The `Banknote.Version` enum lets the signing key be rotated across server
versions while older notes remain individually valid or invalid. Total issuance is
tracked in state (`trackedBanknoteIssuance`) so notes in circulation stay accounted for
against the money supply.

### Markets

Player- and admin-operated shops in two flavours:

- **Fixed price** — a set buy/sell price per item.
- **Active price** — an order book. `Order`, `OrderChain`, `OrderType` and `Fulfillment`
  match buy and sell orders; `VolatilityLevel` governs how sharply price responds to
  volume. `MarketUpdateTask` advances prices on a tick.

Markets are tagged with a `MarketCategory` (agriculture, raw materials, timber, seafood,
foods, block families, custom items, luxury items, speculative goods, music discs,
furniture, enchanted books) for discovery via `/findmarket`.

### Custom items and upgrades

Custom items extend `ModifiableItemsAdderItem`, binding a Java class to an ItemsAdder
asset ID and a `mirae.item.*` permission node. Weapons include beam swords in several
colours, a hero shield, mythic and storm hammers, metal claws and a baseball bat, plus
utility items like the magnet and EMP shield driven by scheduled tasks.

The `upgrade` package implements an upgrade bench (`/upgrade`) with recipes across four
families: **composition** (coal→diamond, wood→netherite, netherite→darksteel),
**weapon** (building and recolouring weapons), **enchant** (acquiring and applying
custom enchants) and **tool** (combining tools, magnets). Imprints can be removed with a
dedicated recipe.

### Skills

Five skills — `MINING`, `FARMING`, `CARPENTRY`, `TRADING`, `UPGRADING` — level
independently per account and gate or improve the corresponding activity.

### Cooking

A recipe system separate from vanilla crafting, with its own GUI and recipe registry,
reached through `/cooking`.

### Daily quests

`DailyQuestTask` generates a quest location each day; `DailyQuestBroadcastTask`
announces it hourly until it is claimed. Reward chests are generated lazily when the
chest is first opened. `/dailyquestregen` forces regeneration.

### Social and teleportation

Direct messages with `/reply` threading, per-player ignore lists, nicknames, display
names, prefixes, titles, local chat toggling, and a scoreboard sidebar that can be
hidden per player. Teleportation covers `/home` (with purchasable extra home slots),
`/warp`, `/spawn`, `/back` to a death or departure point, and request-based player
teleports with accept/deny/cancel.

### Moderation and anti-abuse

Mutes with expiry, silencing, an account tier system (`DEFAULT` → `GREEN` → `GOLD` →
`PLATINUM` → `BLACK`, plus `DEVELOPER`), `AntiSpamBanTask`, and
`TransferAmountMonitorTask` which watches for suspicious economic transfers.

## Commands

Roughly 70 commands, registered under the `mirae` namespace. Many carry Korean aliases
(e.g. `/upgrade` is also `/강화`).

| Group | Commands |
|---|---|
| **Economy** | `balance` `pay` `withdraw` `coin` `changemoney` `changecoin` `changetoken` `setmoney` `setcoin` `freezeeconomy` |
| **Market** | `market` `createmarket` `editmarket` `removemarket` `findmarket` |
| **Home / warp** | `home` `homes` `sethome` `deletehome` `back` `spawn` `setspawn` `warp` `warps` `setwarp` `deletewarp` |
| **Teleport** | `teleportask` `teleportaskhere` `teleportaccept` `teleportdeny` `teleportaskcancel` |
| **Social** | `directmessage` `reply` `ignore` `nickname` `title` `localchat` |
| **Skill** | `skilllevel` `setskilllevel` |
| **Misc** | `upgrade` `cooking` `storage` `helmet` `height` `flex` `minelist` `discord` `donorshop` `scoreboardtoggle` `togglechoptree` `mendingbookrecall` `silence` |
| **Stats** | `moneysupply` |
| **Admin** | `createaccount` `settier` `setprefix` `setdisplayname` `itemname` `enchant` `mute` `givetitle` `taketitle` `gettitle` `settitle` `setstarterkitreceived` `dailyquestregen` `givemeop` |
| **Debug** | `test` |

## Persistence

State is written as JSON under `plugins/Mirae/`:

```
plugins/Mirae/
├── data.json          Global state: spawn, warps, banknote issuance, daily quest
├── accounts/<uuid>.json
├── markets/<uuid>.json
└── backups/<timestamp>.zip
```

`AutoSaveTask` saves periodically and `onDisable` saves on shutdown; each save is
preceded by a timestamped zip backup of the previous data. Custom Gson adapters handle
`ItemStack`, `LocalDateTime` and coordinate serialization.

## Operational notes

This code ran one specific server and carries assumptions from that deployment. If you
intend to actually run it, read this section.

### The donation webhook needs attention

`webhook/Webhook.java` starts an **unconditional plain-HTTP server on port 1234**,
exposing `POST /donation` to credit coins to a player. Because `Mirae` holds it in a
`static final` field, the socket binds as soon as the class loads — there is no config
flag and no way to disable it short of editing the source. Three further problems:

1. **The shared auth key is a hardcoded literal in this repository.** It must be moved
   to config and rotated before this endpoint is exposed to a network.
2. **The auth check does not stop the request.** On mismatch it writes a 403 response
   but does not `return`, so execution falls through to the crediting path.
3. Coin crediting is currently commented out, which is the only reason (2) is not
   presently exploitable.

There is no TLS and no rate limiting. Treat the endpoint as disabled and unfinished.
If you don't need it, remove the `Webhook` construction from `Mirae`.

### Other notes

- **`/givemeop`** is a hardcoded privilege-escalation path. Its allowlist is now
  intentionally empty, so the command always denies; populate it from config, or delete
  the command, before use.
- **Hardcoded server links.** Discord and Minelist URLs in `gui/convenience/MainMenu.java`
  point at the original server.
- **Korean-only player-facing strings.** Messages are not externalized; there is no
  localization layer.
- **No test suite.** There are no automated tests in this repository.

## Third-party assets

Custom items reference ItemsAdder asset IDs from external content packs — namespaces
include `stellar_heroes`, `iaspecial_swords`, `ultimate_armors`, `crystals`, `food`,
`mcicons`, `iageneric`, `magnet` and `drugs`. **Those textures and models are not in
this repository.** They belong to their respective pack authors, several are paid
resources, and their licenses — not this one — govern their redistribution and use.

The MIT license below covers the Java source in this repository only.

## License

[MIT](LICENSE) © 2025-2026 Min Jun Sung
