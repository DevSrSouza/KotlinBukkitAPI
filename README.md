![logo](logo.png)

[![Build Status](http://jenkins.devsrsouza.com.br/buildStatus/icon?job=KotlinBukkitAPI)](http://jenkins.devsrsouza.com.br/job/KotlinBukkitAPI/)

KotlinBukkitAPI is an API for Bukkit/SpigotAPI using the cool and nifty features Kotlin has to make your life more easier.

* Need help? contact me on [Twitter](https://twitter.com/DevSrSouza)

# Project

## Dependencies
| Name | Version |
| --- | --- |
| [Spigot API](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/spigot/) | 1.8.8+ |

## Dependencies Embed
| Name | Version |
| --- | --- |
| Kotlin STD + JDK8 | 1.3.71 |
| [Kotlinx-coroutines](https://github.com/Kotlin/kotlinx.coroutines/) | 1.3.5 |
| [Skedule](https://github.com/okkero/Skedule) | 1.2.6 |
| [Kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) | 0.20.0  |
| [KAML](https://github.com/charleskorn/kaml) | 0.18.1 |

## Modules
| Module | Description |
| --- | --- |
| Core | The heart of the project containing the important API and extensions |
| Architecture | Help you with the Architecture of your plugin providing KotlinPlugin base and Lifecycle Listener |
| Plugins | Extensions for others plugins like Vault, PlaceholderAPI and others |
| Exposed(0.21.1) | Extensions for SQL framework [Exposed](https://github.com/JetBrains/Exposed/) |
| Serialization(0.20.0) | Extensions for [Kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) |

## [Documentation](https://github.com/DevSrSouza/KotlinBukkitAPI/wiki/)

## [Download](http://jenkins.devsrsouza.com.br/job/KotlinBukkitAPI/)

## Others resources
- [Starter project using KotlinBukkitAPI](https://github.com/KotlinMinecraft/KBAPI-StarterProject/)

## [Setup for development](https://github.com/DevSrSouza/KotlinBukkitAPI/wiki/Getting-Started)

# Samples

KotlinBukkitAPI goes beyond this samples, and you can find all of it in the [wiki/documentation](https://github.com/DevSrSouza/KotlinBukkitAPI/wiki/).

Event DSL sample
```kotlin
plugin.events {
  event<PlayerJoinEvent> {
    player.msg("&3Welcome ${player.name}".translateColor()) 
  }
  
  event<PlayerQuitEvent> {
    broadcast("&eThe player &c${player.name} &eleft :(".translateColor())
  }
}
```

Simple Command DSL example
```kotlin
plugin.simpleCommand("twitter") {
  sender.msg("&eFollow me on Twitter :D &ahttps://twitter.com/DevSrSouza".translateColor())
}
```

Item meta DSL and other stuff
```kotlin
val gem = item(Material.DIAMOND).apply {
  amount = 5
  meta<ItemMeta> {
    displayName = "&bGem".translateColor()
  }
}
val encbook = item(Material.ENCHANTED_BOOK).meta<EnchantmentStorageMeta> {
  displayName = "&4&lThe powerful BOOK".translateColor()
  addStoredEnchant(Enchantment.DAMAGE_ALL, 10, true) // putting sharpness 10 to the book
}
```

Another approach:
```
val gem = item(Material.DIAMOND, amount = 5).displayName("&bGem".translateColor())

val encbook = metadataItem<EnchantmentStorageMeta>(Material.ENCHANTED_BOOK) {
  displayName = "&4&lThe powerful BOOK".translateColor()
    addStoredEnchant(Enchantment.DAMAGE_ALL, 10, true) // putting sharpness 10 to the book
}
```

Menu creator DSL
```kotlin
val myMenu = menu(+"&cWarps", 3, true) {

  val arenaPvP = item(Material.DIAMOND_SWORD) {
      addEnchant(Enchantment.DAMAGE_ALL, 5, true)
      displayName = "&4Arena PvP".translateColor()
  }

  slot(2, 4, arenaPvP) { // Line, Slot
    onClick {
      player.teleport(Location(player.world, 250, 70, -355))
      close() // close the menu
    }
  }

  slot(2, 6, item(Material.GOLD).displayName("&6Shop".translateColor())) {
    onClick {
      player.teleport(Location(player.world, 2399, 70, -1234))
      close() // close the menu
    }
  }

  // when the menu renders to a player, will show the Paper item with their name.
  slot(3, 9, item(Material.PAPER).displayName("Hello {player}")) {
    onRender {
      showingItem?.meta<ItemMeta> {
         displayName = displayName.replace("{player}", player.name)
      } 
    }
  }
}

// open to player
myMenu.openToPlayer(player)
```

You can find more examples in the [Documentation](https://github.com/DevSrSouza/KotlinBukkitAPI/wiki/)
