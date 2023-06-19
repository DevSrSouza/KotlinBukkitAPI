plugins {
    id(libs.plugins.kotlinbukkitapi.build.get().pluginId)
}

dependencies {
    compileOnly(libs.spigot.api)

    api(libs.coroutines)
}