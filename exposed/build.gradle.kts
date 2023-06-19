plugins {
    id(libs.plugins.kotlinbukkitapi.build.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    compileOnly(libs.spigot.api)
    implementation(libs.kotlinx.serialization.core)

    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.hikaricp)

    api(projects.architecture)
    api(projects.utility)

    api(libs.coroutines)
}