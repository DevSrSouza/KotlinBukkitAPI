repositories {
    maven {
        name = "perfectdreams"
        url = uri("https://repo.perfectdreams.net/")
    }
}


dependencies {
    compileOnly(project(":core"))
    compileOnly(project(":attributestorage"))
    compileOnly("org.jetbrains.exposed:exposed:0.11.2")
}