import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.apache.xerces.dom.DeepNodeListImpl
import org.gradle.api.tasks.bundling.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("maven-publish")
    kotlin("jvm") version "1.2.60"
    id("com.github.johnrengelman.shadow") version "2.0.3"
    id("net.minecrell.plugin-yml.bukkit") version "0.2.1"
}

group = "br.com.devsrsouza"
version = "0.1.0-SNAPSHOT"

val plugins = listOf(
        PluginDependency("vault-repo", "http://nexus.hc.to/content/repositories/pub_releases",
                mapOf("Vault" to "net.milkbowl.vault:VaultAPI:1.6")),
        PluginDependency("placeholderapi", "http://repo.extendedclip.com/content/repositories/placeholderapi/",
                mapOf("PlaceholderAPI" to "me.clip:placeholderapi:2.8.5")),
        PluginDependency("mvdw-software", "http://repo.mvdw-software.be/content/groups/public/",
                mapOf("MVdWPlaceholderAPI" to "be.maximvdw:MVdWPlaceholderAPI:2.5.2-SNAPSHOT")),
        PluginDependency("inventive-repo", "https://repo.inventivetalent.org/content/groups/public/",
                mapOf(
                        "PacketListenerApi" to "org.inventivetalent.packetlistener:api:3.7.0-SNAPSHOT",
                        "HologramAPI" to "org.inventivetalent:hologramapi:1.6.0",
                        "BossBarAPI" to "org.inventivetalent:bossbarapi:2.4.1"
                )
        ),
        PluginDependency("direct-from-github", "https://jitpack.io",
                mapOf(
                        "ActionBarAPI" to "com.github.ConnorLinfoot:ActionBarAPI:e6e6ea2037",
                        "TitleAPI" to "com.github.ConnorLinfoot:TitleAPI:d5095196bb"
                )
        ),
        PluginDependency("WorldEdit", "http://maven.sk89q.com/repo/",
                mapOf("WorldEdit" to "com.sk89q.worldedit:worldedit-bukkit:6.1.5")
        ),
        PluginDependency("ViaVersion", "https://repo.viaversion.com/",
                mapOf("ViaVersion" to "us.myles:viaversion-common:1.4.1")
        )
)

repositories {
    jcenter()
    maven {
        name = "spigot"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    maven {
        name = "comphenix"
        url = uri("http://repo.comphenix.net/content/repositories/snapshots/")
    }
    plugins.forEach {
        maven {
            name = it.repoName
            url = uri(it.repo)
        }
    }
}

dependencies {
    compile(kotlin("stdlib"))
    compile(kotlin("reflect"))
    compile("com.comphenix.attribute:AttributeStorage:0.0.2-SNAPSHOT")

    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    plugins.map { it.plugins }.flatMap { it.entries }.map { it.value }.forEach {
        compileOnly(it) {
            exclude("org.spigotmc", "spigot")
            exclude("org.bukkit", "bukkit")
            exclude("org.mcstats.bukkit", "metrics-lite")
            exclude("org.inventivetalent.packetlistener", "api")
        }
    }
}

bukkit {
    name = project.name
    version = project.version.toString()
    main = "br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI"

    website = "https://github.com/DevSrSouza/KotlinBukkitAPI"
    authors = listOf("DevSrSouza")

    softDepend = plugins.map { it.plugins }.flatMap { it.entries }.map { it.key }
}

class PluginDependency(val repoName: String, val repo: String, val plugins: Map<String, String>)

tasks {
    "compileKotlin"(KotlinCompile::class) {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
    "shadowJar"(ShadowJar::class) {
        baseName = project.name
        classifier = null
    }
}

val sourcesJar by tasks.creating(Jar::class) {
    classifier = "sources"
    from(java.sourceSets["main"].allSource)
}

publishing {
    (publications) {
        "mavenJava"(MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar)
            groupId = project.group.toString()
            artifactId = project.name.toLowerCase()
            version = project.version.toString()
            pom.withXml {
                asNode().apply {
                    appendNode(
                            "description",
                            "KotlinBukkitAPI is a API for Bukkit using the cool features of Kotlin to make your lifes much easely."
                    )
                    appendNode("name", project.name)
                    appendNode("url", "https://github.com/DevSrSouza/KotlinBukkitAPI")

                    appendNode("licenses").appendNode("license").apply {
                        appendNode("name", "MIT License")
                        appendNode("url", "https://github.com/DevSrSouza/KotlinBukkitAPI/blob/master/LICENSE")
                        appendNode("distribution", "repo")
                    }
                    appendNode("developers").apply {
                        appendNode("developer").apply {
                            appendNode("id", "DevSrSouza")
                            appendNode("name", "Gabriel Souza")
                            appendNode("email", "devsrsouza@gmail.com")
                        }
                    }
                    appendNode("scm").appendNode("url", "https://github.com/DevSrSouza/KotlinBukkitAPI/tree/master")
                }
                asElement().apply {
                    removeChild(getElementsByTagName("dependencies").item(0))
                }
            }
        }
    }
}