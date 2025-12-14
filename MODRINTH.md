# GlobalVillagerDiscounts

**Synchronize villager curing discounts across all players on your server!**

## Description

GlobalVillagerDiscounts is a lightweight plugin that shares villager trade discounts among all players. When any player earns a discount by **curing a zombie villager**, that discount becomes available to **everyone** on the server.

### How It Works

1. Player cures a zombie villager → earns discount
2. Plugin captures and stores the discount (in PersistentDataContainer)
3. Any other player trading with that villager gets the same discount
4. Best discount always wins

> **Note:** Hero of the Village discounts are **not** shared (they're temporary player effects).

## Features

- 🔌 **Plug & Play** - No configuration needed
- 💾 **Persistent** - Discounts survive server restarts
- 🛡️ **Safe** - Prices never go below 1 emerald
- ⚡ **Lightweight** - Minimal performance impact
- 🔧 **Admin Controls** - Per-villager management
- 📦 **Zero Dependencies** - Only requires Spigot/Paper API

## Admin Commands

| Command | Description |
|---------|-------------|
| `/gvd info` | Show synced discount info for looked-at villager |
| `/gvd clear` | Clear synced discounts from looked-at villager |
| `/gvd disable` | Disable sync for specific villager |
| `/gvd enable` | Enable sync for specific villager |

**Permission:** `gvd.admin` (default: OP)

## Requirements

- **Server:** Spigot, Paper, or Purpur 1.21+
- **Java:** 21

## Installation

1. Download the JAR from the files section
2. Place in your `plugins` folder
3. Restart server
4. Done! Works automatically.

## Compatibility

| Server | Supported |
|--------|-----------|
| Spigot | ✅ |
| Paper | ✅ |
| Purpur | ✅ |
| Folia | ❌ |

## Important Notes

- Discounts are stored **separately** from vanilla gossip
- If plugin is removed, synced discounts stop working
- Vanilla reputation system is **not modified**

## Source Code

Open source on [GitHub](https://github.com/murqin/GlobalVillagerDiscounts)

## License

MIT License
