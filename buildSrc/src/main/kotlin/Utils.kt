import org.gradle.api.Action
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.kotlin.dsl.*
import java.io.File
import java.util.*

// null if the file does not exist
fun loadProperties(file: String): Properties? {
    val file = File(file).takeIf { it.exists() } ?: return null

    return Properties().apply {
        load(file.inputStream())
    }
}

val changing = Action<ExternalModuleDependency> { isChanging = true }

val excludeKotlin = Action<ExternalModuleDependency> {
    exclude(group = "org.jetbrains.kotlin")
}