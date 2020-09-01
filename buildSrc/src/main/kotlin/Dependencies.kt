import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.kotlin

fun DependencyHandlerScope.baseDependencies(): List<Any> = listOf(
        kotlin("stdlib-jdk8"),
        kotlin("reflect"),
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.corouties}"
)

fun DependencyHandlerScope.coreDependencies(): List<String> = listOf(
        "com.okkero.skedule:skedule:${Versions.skedule}"
)

fun DependencyHandlerScope.exposedDependencies(): List<String> = listOf(
        "org.jetbrains.exposed:exposed-core:${Versions.exposed}",
        "org.jetbrains.exposed:exposed-jdbc:${Versions.exposed}",
        "org.jetbrains.exposed:exposed-java-time:${Versions.exposed}",
        "org.jetbrains.exposed:exposed-dao:${Versions.exposed}",
        "com.zaxxer:HikariCP:${Versions.hikari}"
)

fun DependencyHandlerScope.serializationDependencies(): List<String> = listOf(
        "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.serialization}",
        "com.charleskorn.kaml:kaml:${Versions.kaml}"
)