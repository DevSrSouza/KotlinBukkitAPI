dependencies {
    compileOnly(project(":core", configuration = "shadow"))
    compileOnly(project(":serialization", configuration = "shadow"))

    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
}