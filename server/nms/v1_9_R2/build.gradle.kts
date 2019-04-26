dependencies {
    compileOnly(files(File(projectDir.parentFile, "/server_jars/spigot-1.9.4-R0.1-SNAPSHOT.jar")))
    
    compileOnly(project(":server:api"))
}