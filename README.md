# GlobalVillagerDiscounts

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21%2B-brightgreen.svg)](https://www.minecraft.net/)
[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Spigot](https://img.shields.io/badge/Spigot%20%2F%20Paper-Compatible-blue.svg)](https://www.spigotmc.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**Synchronize villager curing discounts across all players on your server!**

## 📖 Description

GlobalVillagerDiscounts shares villager trade discounts among all players. When any player earns a discount by **curing a zombie villager**, that discount becomes available to **everyone** on the server.

### How It Works

1. Player cures a zombie villager → earns discount
2. Plugin captures and stores the discount (by recipe hash)
3. Any other player trading with that villager gets the same discount
4. Best discount always wins
5. Discounts auto-clear on profession change

> **Note:** Hero of the Village discounts are **not** shared (temporary effect).

## ✨ Features

- 🔌 **Plug & Play** - No configuration needed
- 💾 **Persistent Storage** - Discounts survive restarts
- 🛡️ **Safe Pricing** - Prices never below 1 emerald
- ⚡ **Lightweight** - Minimal performance impact
- 🔧 **Admin Controls** - Per-villager management
- 🔄 **Smart Storage** - Recipe-based matching (not index-based)
- 🧹 **Auto-Cleanup** - Clears on profession change

## 🔧 Admin Commands

| Command | Description |
|---------|-------------|
| `/gvd info` | Show synced discount info |
| `/gvd clear` | Clear discounts (single villager) |
| `/gvd clearall` | Clear ALL discounts (all villagers) |
| `/gvd disable` | Disable sync for villager |
| `/gvd enable` | Enable sync for villager |

**Permission:** `gvd.admin` (default: OP)

## 📋 Requirements

- **Minecraft Server:** Spigot, Bukkit, Paper, or Purpur 1.21+
- **Java:** 21 (LTS)

## 📥 Installation

1. Download the latest JAR from [Modrinth](https://modrinth.com/plugin/globalvillagerdiscounts)
2. Place in your `plugins` folder
3. Restart server
4. Done!

## 🔧 Building from Source

```bash
git clone https://github.com/murqin/GlobalVillagerDiscounts.git
cd GlobalVillagerDiscounts
mvn clean package
```

## 🤝 Compatibility

| Server Software | Supported |
|-----------------|-----------|
| Spigot          | ✅        |
| Bukkit          | ✅        |
| Paper           | ✅        |
| Purpur          | ✅        |
| Folia           | ❌        |

## ⚠️ Important Notes

- Discounts stored **separately** from vanilla gossip
- If plugin removed, synced discounts stop working
- Profession change = discounts cleared automatically

## 📜 License

MIT License - see [LICENSE](LICENSE)

## 👤 Author

**murqin** - [@murqin](https://github.com/murqin)

---

<p align="center">Made with ❤️ for the Minecraft community</p>
