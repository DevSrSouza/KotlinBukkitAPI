repositories {}

dependencies {
    compileOnly(project(":core", configuration = "shadow"))
    compileOnly(project(":server"))
    compileOnly("org.jetbrains.exposed:exposed:0.11.2")
    compileOnly("com.zaxxer:HikariCP:3.3.1")
}