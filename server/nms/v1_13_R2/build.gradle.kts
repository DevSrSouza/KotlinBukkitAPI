dependencies {
    compileOnly(files(File(projectDir.parentFile, "/server_jars/spigot-1.13.2.jar")))
    
    compileOnly(project(":server:api"))
}