dependencies {
    compileOnly(files(File(projectDir.parentFile, "/server_jars/spigot-1.11.2.jar")))
    
    compileOnly(project(":server:api"))
}