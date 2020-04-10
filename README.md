![logo](logo.png)

[![Build Status](http://jenkins.devsrsouza.com.br/buildStatus/icon?job=KotlinBukkitAPI)](http://jenkins.devsrsouza.com.br/job/KotlinBukkitAPI/)

KotlinBukkitAPI is an API for Bukkit/SpigotAPI using the cool and nifty features Kotlin has to make your life more easier.

* Need help? contact me on [Twitter](https://twitter.com/DevSrSouza)

# Project

## Dependencies
| Name | Version |
| --- | --- |
| [Spigot API](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/spigot/) | 1.8.8 |

## Dependencies Embed
| Name | Version |
| --- | --- |
| Kotlin STD | 1.3.71 |
| Kotlin Reflect | 1.3.71 |
| [Kotlinx-coroutines](https://github.com/Kotlin/kotlinx.coroutines/) | 1.3.5 |
| [Skedule](https://github.com/okkero/Skedule) | 1.2.6 |

## Modules
| Module | Description |
| --- | --- |
| Core | The heart of the project containing the important API and extensions |
| Architecture | Help you with the Architecture of your plugin providing KotlinPlugin base and Lifecycle Listener |
| Plugins | Extensions for others plugins like Vault, PlaceholderAPI and others |
| Exposed(0.20.1) | Extensions for SQL framework [Exposed](https://github.com/JetBrains/Exposed/) |

# Links
- [Examples and **documentation**](https://github.com/DevSrSouza/KotlinBukkitAPI/wiki/)
- [Clone and building](https://github.com/DevSrSouza/KotlinBukkitAPI/wiki/Clone-and-build)

# [Download](http://jenkins.devsrsouza.com.br/job/KotlinBukkitAPI/)

# Setup for development

First of all, you need to put KotlinBukkitAPI as a dependency on your **plugin.yml**
```yaml
depend: [KotlinBukkitAPI]
```

### Gradle

```groovy
repositories {
  maven {
    name = "KotlinBukkitAPI"
    url = "http://nexus.devsrsouza.com.br/repository/maven-public/"
  }
}

dependencies {
  compileOnly("br.com.devsrsouza.kotlinbukkitapi:core:0.1.0-SNAPSHOT") // core
}
```

# Examples

Event DSL example
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
  sender.msg(+"&eFollow me on Twitter :D &ahttps://twitter.com/DevSrSouza")
}
```

Item meta DSL and other stuff
```kotlin
val gem = item(Material.DIAMOND).apply {
  amount = 5
  meta<ItemMeta> {
    displayName = +"&bGem"
  }
}
val encbook = item(Material.ENCHANTED_BOOK).meta<EnchantmentStorageMeta> {
  displayName = +"&4&lThe powerful BOOK"
  addStoredEnchant(Enchantment.DAMAGE_ALL, 10, true) // putting sharpness 10 to the book
}
```

Menu creator DSL
```kotlin
val myMenu = menu(+"&cWarps", 3, true) {

  slot(2, 4) { // Line, Slot
    item = ItemStack(Material.DIAMOND_SWORD).apply {
      addEnchantment(Enchantment.DAMAGE_ALL, 5)

      meta<ItemMeta> {
        displayName = +"&4Arena PvP"
      }
    }

    onClick {
      player.teleport(Location(player.world, 250, 70, -355))
      close() // close the menu
    }
  }

  slot(2, 6) {
    item = ItemStack(Material.GOLD).apply {
      meta<ItemMeta> {
        displayName = +"&6Shop"
      }
    }
    
    onClick {
      player.teleport(Location(player.world, 2399, 70, -1234))
      close() // close the menu
    }
  }
}

// open to player
myMenu.openToPlayer(player)
```

You can find more examples in the [Documentation](https://github.com/DevSrSouza/KotlinBukkitAPI/wiki/)
