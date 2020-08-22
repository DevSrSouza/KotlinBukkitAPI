import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import org.gradle.api.tasks.bundling.Jar

plugins {
    id("java")
    id("maven-publish")
    kotlin("jvm") version "1.4.0"
    id("com.github.johnrengelman.shadow") version "4.0.3"
    id("net.minecrell.plugin-yml.bukkit") version "0.3.0"
    id("com.jfrog.bintray") version "1.8.4"
}

val groupPrefix = "br.com.devsrsouza.kotlinbukkitapi"
val pVersion = KotlinBukkitAPI.version
group = groupPrefix
version = pVersion

val jcenter = loadProperties("jcenter.properties")

subprojects {
    plugins.apply("org.jetbrains.kotlin.jvm")
    plugins.apply("maven-publish")
    plugins.apply("com.github.johnrengelman.shadow")
    plugins.apply("com.jfrog.bintray")

    group = groupPrefix
    version = pVersion

    repositories {
        jcenter()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("http://nexus.devsrsouza.com.br/repository/maven-public/")
    }

    dependencies {
        compileOnly(kotlin("stdlib-jdk8"))

        compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    }

    kotlin {
        sourceSets.all {
            languageSettings.useExperimentalAnnotation("kotlin.contracts.ExperimentalContracts")
        }
    }

    tasks {
        test {
            useJUnitPlatform()
        }
        compileKotlin {
            kotlinOptions.jvmTarget = "1.8"
            kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.time.ExperimentalTime,kotlin.ExperimentalStdlibApi,kotlinx.coroutines.ExperimentalCoroutinesApi"
        }

        shadowJar {
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
                    description.set(KotlinBukkitAPI.github)
                    url.set(KotlinBukkitAPI.github)
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
                                project.path.removePrefix(":").replace(":", "/"))
                    }
                }
            }
        }
    }

    if(jcenter != null) {
        bintray {
            user = jcenter["bintray_user"] as String
            key = jcenter["bintray_key"] as String
            setPublications("maven")
            with(pkg) {
                repo = "KotlinBukkitAPI"
                name = project.name
                websiteUrl = KotlinBukkitAPI.github
                vcsUrl = "${KotlinBukkitAPI.github}.git"
                issueTrackerUrl = "${KotlinBukkitAPI.github}/issues"
                setLicenses("MIT")

                with(version) {
                    name = project.version.toString()

                    with(gpg) {
                        sign = (jcenter["gpg_sign"] as String).toBoolean()
                        passphrase = jcenter["gpg_passphrase"] as String
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
    api(kotlin("stdlib-jdk8"))

    subprojects.forEach {
        api(project(it.path, configuration = "shadow"))
    }
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
    shadowJar {
        baseName = project.name
        version += "-b${System.getenv("BUILD_NUMBER")}"
        classifier = null
    }
}

bukkit {
    name = project.name
    version = project.version.toString()
    main = "br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI"
    description = KotlinBukkitAPI.description

    website = KotlinBukkitAPI.github
    authors = listOf("DevSrSouza")

    softDepend = KotlinBukkitAPI.plugins.map { it.name }

    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
}