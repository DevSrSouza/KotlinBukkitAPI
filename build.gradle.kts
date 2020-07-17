import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import org.gradle.api.tasks.bundling.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("maven-publish")
    kotlin("jvm") version "1.3.71"
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
        maven {
            name = "devsrsouza"
            url = uri("http://nexus.devsrsouza.com.br/repository/maven-public/")
        }
    }

    dependencies {
        compileOnly(kotlin("stdlib-jdk8"))

        compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

        testRuntime(kotlin("stdlib"))
        testRuntime("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
        testRuntime("org.junit.platform:junit-platform-launcher:1.3.2")
        testRuntime("org.junit.jupiter:junit-jupiter-engine:5.3.2")
        testRuntime("org.junit.vintage:junit-vintage-engine:5.3.2")

        testRuntime("org.mockito:mockito-core:2.24.0")
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
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "1.8"
        }

        withType<ShadowJar> {
            baseName = "KotlinBukkitAPI-${project.name}"
            classifier = null
            version = null

            relocate("org.bstats", "br.com.devsrsouza.kotlinbukkitapi.bstats")
        }
    }

    val sources by tasks.registering(Jar::class) {
        baseName = "KotlinBukkitAPI-${project.name}"
        classifier = "sources"
        version = null
        from(sourceSets.main.get().allSource)
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
                artifact(sources.get())
                groupId = project.group.toString()
                artifactId = project.path.removePrefix(":")
                        .replace(":", "-").toLowerCase()
                version = project.version.toString()
                pom {
                    name.set("KotlinBukkitAPI-${project.name}")
                    description.set("KotlinBukkitAPI is an API for Bukkit/SpigotAPI using the cool and nifty features Kotlin has to make your life more easier.")
                    url.set("https://github.com/DevSrSouza/KotlinBukkitAPI")
                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://github.com/DevSrSouza/KotlinBukkitAPI/blob/master/LICENSE")
                            distribution.set("repo")
                        }
                    }
                    developers {
                        developer {
                            id.set("DevSrSouza")
                            name.set("Gabriel Souza")
                            email.set("devsrsouza@gmail.com")
                        }
                    }
                    scm {
                        url.set("https://github.com/DevSrSouza/KotlinBukkitAPI/tree/master/" +
                                "${project.path.removePrefix(":").replace(":", "/")}")
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
    compile(kotlin("stdlib-jdk8"))

    subprojects.forEach {
        compile(project(it.path, configuration = "shadow"))
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
        version += "-b${System.getenv("BUILD_NUMBER")}"
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