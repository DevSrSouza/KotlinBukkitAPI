plugins {
    id(libs.plugins.kotlinbukkitapi.build.get().pluginId)
}

dependencies {
    compileOnly(libs.spigot.api)

    api(projects.mcExtensions)
    api(projects.architecture)
    api(projects.utility)

    api(libs.coroutines)
}