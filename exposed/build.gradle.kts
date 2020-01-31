repositories {}

val exposed_version = "0.20.1"

dependencies {
    compileOnly(project(":core", configuration = "shadow"))

    compileOnly("org.jetbrains.exposed:exposed-core:$exposed_version")
    compileOnly("org.jetbrains.exposed:exposed-dao:$exposed_version")

    compileOnly("com.zaxxer:HikariCP:3.3.1")
}