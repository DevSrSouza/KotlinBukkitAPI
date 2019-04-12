dependencies {
    compileOnly(project(":core"))
    compileOnly(project(":server:api"))

    // spigot-api versions
    compileOnly(project(":server:bukkit:1_9"))
    compileOnly(project(":server:bukkit:1_10"))
    compileOnly(project(":server:bukkit:1_11"))
    compileOnly(project(":server:bukkit:1_12"))

    // nms versions
    compileOnly(project(":server:nms:v1_8_R3"))
}