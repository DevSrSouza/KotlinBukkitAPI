import org.gradle.api.Plugin
import org.gradle.api.Project

import org.gradle.kotlin.dsl.*

class BukkitPlugins : Plugin<Project> {

    override fun apply(project: Project): Unit = project.run {

        val bukkitPlugins = BukkitPlugins()

        extensions.add("bukkitPlugins", bukkitPlugins)

        repositories {
            for (plugin in bukkitPlugins.plugins) {
                maven {
                    name = plugin.repoName
                    url = uri(plugin.repo)
                }
            }
        }

        /*dependencies { TODO
            for (plugin in bukkitPlugins.plugins.flatMap { it.plugins.map { it.artifact } }) {
                compile(plugin) {
                    exclude("org.spigotmc", "spigot")
                    exclude("org.bukkit", "bukkit")
                    exclude("org.mcstats.bukkit", "metrics-lite")
                    exclude("org.inventivetalent.packetlistener", "api")
                }
            }
        }

        bukkit {
            softDepend = (softDepend ?: mutableListOf()) + plugins.flatMap { it.plugins.filter { it.softDepend } }.map { it.name }
            depend = (softDepend ?: mutableListOf()) + plugins.flatMap { it.plugins.filter { it.depend } }.map { it.name }
        }*/
    }

    class BukkitPlugins(val plugins: MutableList<PluginRepository> = mutableListOf()) {
        inline fun repository(repoName: String, repo: String, block: PluginRepository.() -> Unit = {}) {
            plugins.add(PluginRepository(repoName, repo).apply(block))
        }
    }
    class PluginRepository(val repoName: String, val repo: String, val plugins: MutableList<Plugin> = mutableListOf()) {
        inline fun plugin(name: String, artifact: String, block: Plugin.() -> Unit = {}) {
            plugins.add(Plugin(name, artifact).apply(block))
        }
    }
    class Plugin(val name: String, val artifact: String, val depend: Boolean = false, val softDepend: Boolean = true)
}