![logo](logo.png)

KotlinBukkitAPI is a API for Bukkit/SpigotAPI using the cool features of Kotlin to make your lifes much easely.

* Need help? contact me on [Twitter](twitter.com/DevSrSouza)


## Prerequisites
* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

## Clone
The following steps will ensure your project is cloned properly.

`git clone --recursive https://github.com/DevSrSouza/KotlinBukkitAPI.git`

## Building
#### Unix (Linux / Mac)
```
./gradlew shadowJar
```

#### Windows
```
gradlew shadowJar
```


# Setup for development

### Adding KotlinBukkitAPI to maven local

#### Unix (Linux / Mac)
```
./gradlew publishToMavenLocal
```

#### Windows

```
gradlew publishToMavenLocal
```

### Maven

```xml
<dependency>
  <groupId>br.com.devsrsouza</groupId>
  <artifactId>kotlinbukkitapi</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <scope>provided</scope>
</dependency>
```

### Gradle

```groovy
repositories {
  mavenLocal()
}

dependencies {
  compileOnly 'br.com.devsrsouza:kotlinbukkitapi:0.1.0-SNAPSHOT'
}
```

# Getting Started

First of all, you need to put KotlinBukkitAPI as dependency on your **plugin.yml**
```yaml
depend: [KotlinBukkitAPI]
```

Event DSL example
```kotlin
events { 
  // inside of the block "events" is a Listener
  // the "event" method need to be on a Listener class
  event<PlayerJoinEvent> {
    // inside of this block is the Event type you chose, we chose PlayerJoinEvent
    player.sendMessage(+"&3Welcome ${player.name}") // The plus sign converts the "&" prefixed characters to Minecraft text formatting
  }
  
  // you can put more of one event method inside of "events" block
  event<PlayerQuitEvent> {
    broadcast(+"&eThe player &c${player.name} &eleft :(") // broadcast method send message to player players
  }
}

```

Simple Command DSL example
```kotlin
simpleCommand("twitter") {
  // in this block you have the class CommandMaker, which have the properties:
  // sender - CommandSender
  // command - Command
  // label - String
  // args - Array<String>
  
  sender.sendMessage(+"&eFollow me on Twitter :D &atwitter.com/DevSrSouza")
}
```

Item meta DSL and another stuffs
```kotlin
simpleCommand("some-name") {

  if(sender is Player) { // checking if CommandSender is a player
    val player = sender as Player
    player.vault.economy.deposit(2500.0) // you can see more about the vault api on KVault.kt
    
    // lets make a item more easy with kotlin and ItemMeta DSL
    val gem = ItemStack(Material.DIAMOND).apply {
      amount = 5
      meta<ItemMeta> { // here you can put any meta type you wont, like BannerMeta(if the item is a banner)
        // here is the same idea of event block but here you only que put the ItemMeta type, like BannerMeta, BookMeta
        displayName = +"&bGem"
      }
    }
    
    player.inventory.addItem(gem) // putting the item to the inventory
    
    // okay, now lets use the real power of meta block
    
    if(player.hasPermission("powerful.book")) { // here he verify if player has the permission to get our book
      val encbook = ItemStack(Material.ENCHANTED_BOOK).apply {
        meta<EnchantmentStorageMeta> { // the EnchantmentStorageMeta implement ItemMeta, then we have the methods of ItemMeta and EnchantmentStorageMeta on this block
          displayName = +"&4&lThe powerful BOOK"
          addStoredEnchant(Enchantment.DAMAGE_ALL, 10, true) // putting sharpness 10 to the book
        }
      }
      player.inventory.addItem(encbook)
    }
    
  } else sender.sendMessage("Command just for players")
}
```

Menu creator DSL
```kotlin
// okay, lets make a Menu
// fun createMenu(displayname: String, lines: Int = 3, cancel: Boolean = false, block: Menu.() -> Unit)
val myMenu = createMenu(+"&cWarps", cancel = true) { // cancel true to cancel player interact with inventory by default
  // this menu will be a menu with 3 lines (27 slots) and the name "Warps" in red
  // this block is a Menu

  // registering a slot
  slot(2, 4) { // Line, Slot
    // inside of this block will be a Slot
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

// now we need a command to open the menu to the player
simpleCommand("warps") {
  if(sender is Player) {
    val player = sender as Player
    myMenu.openToPlayer(player) // here we open the menu to de Player
  }
}
```


