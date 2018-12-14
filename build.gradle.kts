import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import org.apache.xerces.dom.DeepNodeListImpl
import org.gradle.api.tasks.bundling.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("bukkit-dependecies")
    id("java")
    id("maven-publish")
    kotlin("jvm") version "1.3.0"
    id("com.github.johnrengelman.shadow") version "2.0.3"
}


group = "br.com.devsrsouza"
version = "0.1.0-SNAPSHOT"

bukkitPlugins {
    repository("vault-repo", "http://nexus.hc.to/content/repositories/pub_releases") {
        plugin("Vault", "net.milkbowl.vault:VaultAPI:1.6")
    }
    repository("placeholderapi", "http://repo.extendedclip.com/content/repositories/placeholderapi/") {
        plugin("PlaceholderAPI", "me.clip:placeholderapi:2.8.5")
    }
    repository("mvdw-software", "http://repo.mvdw-software.be/content/groups/public/") {
        plugin("MVdWPlaceholderAPI", "be.maximvdw:MVdWPlaceholderAPI:2.5.2-SNAPSHOT")
    }
    repository("direct-from-github", "https://jitpack.io") {
        plugin("ActionBarAPI", "com.github.ConnorLinfoot:ActionBarAPI:d60c2aedb9")
        plugin("TitleAPI", "com.github.ConnorLinfoot:TitleAPI:1.7.6")
    }
    repository("WorldEdit", "http://maven.sk89q.com/repo/") {
        plugin("WorldEdit", "com.sk89q.worldedit:worldedit-bukkit:6.1.5")
    }
    repository("ViaVersion", "https://repo.viaversion.com/") {
        plugin("ViaVersion", "us.myles:viaversion-common:1.4.1")
    }
    repository("inventive-repo", "https://repo.inventivetalent.org/content/groups/public/") {
        plugin("PacketListenerApi", "org.inventivetalent.packetlistener:api:3.7.1-SNAPSHOT")
        plugin("HologramAPI", "org.inventivetalent:hologramapi:1.6.0")
        plugin("BossBarAPI", "org.inventivetalent:bossbarapi:2.4.1")
    }
}

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
}

dependencies {
    compile(kotlin("stdlib"))
    compile(kotlin("reflect"))
    compile("com.comphenix.attribute:AttributeStorage:0.0.2-SNAPSHOT")

    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    for (plugin in bukkitPlugins.plugins.flatMap { it.plugins.map { it.artifact } }) {
        compile(plugin) {
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

    softDepend = bukkitPlugins.plugins.flatMap { it.plugins.filter { it.softDepend } }.map { it.name }
    depend = bukkitPlugins.plugins.flatMap { it.plugins.filter { it.depend } }.map { it.name }

    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
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

val sourcesJar by tasks.registering(Jar::class) {
    classifier = "sources"
    from(sourceSets.main.get().allSource)
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar.get())
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