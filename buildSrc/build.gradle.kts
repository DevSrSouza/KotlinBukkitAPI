plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("bukkit-dependecies-plugin") {
            id = "bukkit-dependecies"
            implementationClass = "BukkitPlugins"
        }
    }
}

repositories {
    jcenter()
    gradlePluginPortal()
}

dependencies {
    compile("gradle.plugin.net.minecrell:plugin-yml:0.3.0")
}