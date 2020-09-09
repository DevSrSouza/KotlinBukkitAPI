plugins {
    `kotlin-dsl`
}
repositories {
    jcenter()
    google()
    gradlePluginPortal()
}
dependencies {
    compileOnly(gradleApi())
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.0")
    implementation("com.github.jengelman.gradle.plugins:shadow:6.0.0")
    implementation("me.bristermitten:pdm-gradle:0.0.28")
}