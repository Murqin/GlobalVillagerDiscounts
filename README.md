# GlobalVillagerDiscounts

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21%2B-brightgreen.svg)](https://www.minecraft.net/)
[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Spigot](https://img.shields.io/badge/Spigot%20%2F%20Paper-Compatible-blue.svg)](https://www.spigotmc.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**Synchronize villager curing discounts across all players on your server!**

## 📖 Description

GlobalVillagerDiscounts is a lightweight plugin that shares villager trade discounts among all players. When any player earns a discount by **curing a zombie villager**, that discount becomes available to **everyone** on the server.

### How It Works

1. Player cures a zombie villager → earns discount
2. Plugin captures and stores the discount (in `PersistentDataContainer`)
3. Any other player trading with that villager gets the same discount
4. Best discount always wins

> **Note:** Hero of the Village discounts are **not** shared as they are temporary player effects.

## ✨ Features

- 🔌 **Plug & Play** - No configuration needed
- 💾 **Persistent Storage** - Discounts survive server restarts
- 🛡️ **Safe Pricing** - Prices never go below 1 emerald
- ⚡ **Lightweight** - Minimal performance impact
- 🔧 **Admin Controls** - Per-villager management
- 📦 **Zero Dependencies** - Only requires Spigot/Paper API
- 🚫 **No Vanilla Modification** - Vanilla gossip untouched

## 🔧 Admin Commands

| Command | Description |
|---------|-------------|
| `/gvd info` | Show synced discount info for looked-at villager |
| `/gvd clear` | Clear synced discounts from looked-at villager |
| `/gvd disable` | Disable sync for specific villager |
| `/gvd enable` | Enable sync for specific villager |

**Permission:** `gvd.admin` (default: OP)

## 📋 Requirements

- **Minecraft Server:** Spigot, Paper, or Purpur 1.21+
- **Java:** 21 (LTS)

## 📥 Installation

1. Download the latest `GlobalVillagerDiscounts-1.0.0.jar` from [Releases](https://github.com/murqin/GlobalVillagerDiscounts/releases)
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. Done! Works automatically.

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
| Paper           | ✅        |
| Purpur          | ✅        |
| Folia           | ❌        |

## ⚠️ Important Notes

- Discounts are stored **separately** from vanilla gossip
- If plugin is removed, synced discounts stop working
- Vanilla reputation system is **not modified**

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👤 Author

**murqin**

- GitHub: [@murqin](https://github.com/murqin)

---

<p align="center">
  Made with ❤️ for the Minecraft community
</p>
