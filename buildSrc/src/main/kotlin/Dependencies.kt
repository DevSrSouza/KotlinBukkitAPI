import org.gradle.api.Action
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.*

fun DependencyHandlerScope.baseDependencies(): List<Any> = listOf(
        kotlin("stdlib-jdk8"),
        kotlin("reflect"),
        Dep.coroutinesCore
)

fun DependencyHandlerScope.coreDependencies(): List<String> = listOf(
        Dep.serializationCore,
        Dep.skedule
)

fun DependencyHandlerScope.exposedDependencies(): List<String> = listOf(
        Dep.serializationCore,
        Dep.Exposed.core,
        Dep.Exposed.jdbc,
        Dep.Exposed.javaTime,
        Dep.Exposed.dao,
        Dep.hikariCp
)

fun DependencyHandlerScope.serializationDependencies(): List<String> = listOf(
        Dep.serializationCore,
        Dep.kaml
)

val changing = Action<ExternalModuleDependency> { isChanging = true }

val excludeKotlin = Action<ExternalModuleDependency> {
    exclude(group = "org.jetbrains.kotlin")
}