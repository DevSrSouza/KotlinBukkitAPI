import org.gradle.api.Action
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.*

val serializationDependency = "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.serialization}"

fun DependencyHandlerScope.baseDependencies(): List<Any> = listOf(
        kotlin("stdlib-jdk8"),
        kotlin("reflect"),
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.corouties}"
)

fun DependencyHandlerScope.coreDependencies(): List<String> = listOf(
        serializationDependency,
        "com.okkero.skedule:skedule:${Versions.skedule}"
)

fun DependencyHandlerScope.exposedDependencies(): List<String> = listOf(
        serializationDependency,
        "org.jetbrains.exposed:exposed-core:${Versions.exposed}",
        "org.jetbrains.exposed:exposed-jdbc:${Versions.exposed}",
        "org.jetbrains.exposed:exposed-java-time:${Versions.exposed}",
        "org.jetbrains.exposed:exposed-dao:${Versions.exposed}",
        "com.zaxxer:HikariCP:${Versions.hikari}"
)

fun DependencyHandlerScope.serializationDependencies(): List<String> = listOf(
        serializationDependency,
        "com.charleskorn.kaml:kaml:${Versions.kaml}"
)

val changing = Action<ExternalModuleDependency> { isChanging = true }

val excludeKotlin = Action<ExternalModuleDependency> {
    exclude(group = "org.jetbrains.kotlin")
}