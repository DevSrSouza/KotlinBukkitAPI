import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import org.apache.xerces.dom.DeepNodeListImpl
import org.gradle.api.tasks.bundling.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("maven-publish")
    kotlin("jvm") version "1.3.20"
    id("com.github.johnrengelman.shadow") version "4.0.3"
    id("net.minecrell.plugin-yml.bukkit") version "0.3.0"
}

val softPlugins by extra(mutableListOf<String>())

val groupPrefix = "br.com.devsrsouza.kotlinbukkitapi"
val pVersion = "0.1.0-SNAPSHOT"
group = groupPrefix
version = pVersion

subprojects {
    plugins.apply("org.jetbrains.kotlin.jvm")
    plugins.apply("maven-publish")
    plugins.apply("com.github.johnrengelman.shadow")

    group = groupPrefix
    version = pVersion

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
    }

    dependencies {
        api(kotlin("stdlib"))
        api(kotlin("reflect"))

        api("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

        testRuntime("org.junit.platform:junit-platform-launcher:1.3.2")
        testRuntime("org.junit.jupiter:junit-jupiter-engine:5.3.2")
        testRuntime("org.junit.vintage:junit-vintage-engine:5.3.2")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    kotlin {
        sourceSets.all {
            languageSettings.useExperimentalAnnotation("kotlin.contracts.ExperimentalContracts")
        }
    }

    tasks {
        "shadowJar"(ShadowJar::class) {
            baseName = "KotlinBukkitAPI-${project.name}"
            classifier = null
            version = null
        }
    }

    val sources by tasks.registering(Jar::class) {
        baseName = "KotlinBukkitAPI-${project.name}"
        classifier = "sources"
        version = null
        from(sourceSets.main.get().allSource)
    }

    fun <T> T.print() = apply { println(this.toString()) }

    publishing {
        publications {
            register("mavenJava", MavenPublication::class) {
                from(components["java"])
                artifact(sources.get())
                groupId = project.group.toString()
                artifactId = project.name.toLowerCase()
                version = project.version.toString()
                pom.withXml {
                    asNode().apply {
                        appendNode(
                                "description",
                                "KotlinBukkitAPI is an API for Bukkit/SpigotAPI using the cool and nifty features Kotlin has to make your life more easier."
                        )
                        appendNode("name", "KotlinBukkitAPI-${project.name}")
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
                        appendNode("scm").appendNode("url", "https://github.com/DevSrSouza/KotlinBukkitAPI/tree/master/${project.name}")
                    }
                    asElement().apply {
                        getElementsByTagName("dependencies")?.item(0)?.also { removeChild(it) }
                    }
                }
            }
        }
    }
}

repositories {
    jcenter()
}

dependencies {
    compile(kotlin("stdlib"))
    compile(kotlin("reflect"))

    subprojects.forEach {
        compile(project(":${it.name}", configuration = "shadow"))
    }
}

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

bukkit {
    name = project.name
    version = project.version.toString()
    main = "br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI"

    website = "https://github.com/DevSrSouza/KotlinBukkitAPI"
    authors = listOf("DevSrSouza")

    softDepend = softPlugins

    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
}