# Note

- **The coding of this Plugin was done entirely by AI.**
- **If some people have security concerns about this project because it involves AI, and they understand security, they can contribute to this project. I generally insist that the code should comply with S.O.L.I.D. to make it readable and to make it easy to add features. I think the code is readable.**


# GlobalVillagerDiscounts

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21%2B-brightgreen.svg)](https://www.minecraft.net/)
[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Spigot](https://img.shields.io/badge/Spigot%20%2F%20Paper-Compatible-blue.svg)](https://www.spigotmc.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![bStats](https://img.shields.io/bstats/servers/28448)](https://bstats.org/plugin/bukkit/AdvancedAFK/28505)

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
- 🌍 **Multi-Language** - English & Turkish included, add your own!
- 🔒 **Villager Locking** - Lock villagers to prevent player changes
- 📊 **Tab Completion** - Easy command usage

## 🔧 Commands

| Command | Permission | Description |
|---------|------------|-------------|
| `/gvd info` | - | Show villager info (UUID only for admins) |
| `/gvd share on/off` | - | Enable/disable discount sharing |
| `/gvd clear` | `gvd.admin` | Clear target villager's discounts |
| `/gvd clearall` | `gvd.admin` | Clear ALL discounts |
| `/gvd lock` | `gvd.admin` | Lock villager (players can't change share) |
| `/gvd unlock` | `gvd.admin` | Unlock villager |

> **Usage:** Look at a villager first, then run the command.

## 🌍 Language Support

Built-in languages: **English (en)** & **Turkish (tr)**

### Configuration
```yaml
# config.yml
language: en  # or tr
```

### Add Your Own Language
1. Copy `plugins/GlobalVillagerDiscounts/lang/en.yml`
2. Create `plugins/GlobalVillagerDiscounts/lang/de.yml`
3. Translate all messages
4. Set `language: de` in config.yml
5. Restart server

## 📋 Requirements

- **Minecraft Server:** Spigot, Paper, or Purpur 1.21+
- **Java:** 21 (LTS)

## 📥 Installation

1. Download the latest JAR from [Modrinth](https://modrinth.com/plugin/globalvillagerdiscounts)
2. Place in your `plugins` folder
3. Restart server
4. Done!

## 🤝 Compatibility

| Server | Supported |
|--------|-----------|
| Spigot | ✅ |
| Paper | ✅ |
| Purpur | ✅ |
| Folia | ❌ |

## 📜 License

MIT License - see [LICENSE](LICENSE)

## 👤 Author

**murqin** - [@murqin](https://github.com/murqin)

---

<p align="center">Made with ❤️ for the Minecraft community</p>
