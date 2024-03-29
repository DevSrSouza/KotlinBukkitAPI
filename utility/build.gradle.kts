plugins {
    id(libs.plugins.kotlinbukkitapi.build.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.maven)
}

dependencies {
    compileOnly(libs.spigot.api)
    implementation(libs.kotlinx.serialization.core)

    api(projects.architecture)
    api(projects.mcExtensions)

    api(libs.coroutines)
}