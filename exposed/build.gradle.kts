repositories {}

dependencies {
    compileOnly(project(":core", configuration = "shadow"))
    compileOnly(project(":attributestorage", configuration = "shadow"))
    compileOnly("org.jetbrains.exposed:exposed:0.11.2")
}