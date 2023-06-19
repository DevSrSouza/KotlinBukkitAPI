plugins {
    id(libs.plugins.kotlinbukkitapi.build.get().pluginId)
}

dependencies {
    compileOnly(libs.spigot.api)

    api(projects.architecture)
    api(projects.mcExtensions)
    api(projects.utility)

    api(libs.coroutines)
}