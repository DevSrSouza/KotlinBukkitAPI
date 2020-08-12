data class Plugin(
        val name: String,
        val repositories: Set<String>,
        val dependencies: Set<String>
)