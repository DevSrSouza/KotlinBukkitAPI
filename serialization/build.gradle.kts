plugins {
    id(libs.plugins.kotlinbukkitapi.build.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    compileOnly(libs.spigot.api)
    implementation(libs.kotlinx.serialization.core)

    api(projects.architecture)
    api(projects.mcExtensions)
}