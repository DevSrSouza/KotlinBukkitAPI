import java.io.File
import java.util.*

// null if the file does not exist
fun loadProperties(file: String): Properties? {
    val file = File(file).takeIf { it.exists() } ?: return null

    return Properties().apply {
        load(file.inputStream())
    }
}