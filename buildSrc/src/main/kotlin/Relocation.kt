import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

fun ShadowJar.relocateKotlinBukkitAPI() {
    val kbapiPackage = "br.com.devsrsouza.kotlinbukkitapi.libraries"

    relocate("kotlin", "$kbapiPackage.kotlin")
    relocate("kotlinx", "$kbapiPackage.kotlinx")
    relocate("kotlin", "$kbapiPackage.kotlin")
    relocate("com.charleskorn.kaml", "$kbapiPackage.kaml")
    relocate("org.jetbrains", "$kbapiPackage.jetbrains")
    relocate("org.intellij", "$kbapiPackage.intellij")
    relocate("org.snakeyaml", "$kbapiPackage.snakeyaml")
    relocate("com.zaxxer.hikari", "$kbapiPackage.hikari")
    relocate("com.okkero", "$kbapiPackage.okkero")
}