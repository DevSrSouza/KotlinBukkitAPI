dependencies {
    compileOnly(files(File(projectDir.parentFile, "/server_jars/Spigot-1.14-1eece4f-20190430-1146.jar")))
    
    compileOnly(project(":server:api"))
}