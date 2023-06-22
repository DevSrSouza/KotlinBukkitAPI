plugins {
    id(libs.plugins.kotlinbukkitapi.build.get().pluginId)
    alias(libs.plugins.maven)
}

dependencies {
    compileOnly(libs.spigot.api)

    api(projects.architecture)
    api(projects.coroutines)

    api(libs.coroutines)
}