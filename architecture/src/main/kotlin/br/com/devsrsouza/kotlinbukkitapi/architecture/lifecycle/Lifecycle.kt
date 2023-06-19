package br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle

/**
 * Holds a lifecycle listener class and its priority
 */
public data class Lifecycle(
        val priority: Int,
        val listener: PluginLifecycleListener
) : Comparable<Lifecycle> {

    override fun compareTo(
            other: Lifecycle
    ): Int = other.priority.compareTo(priority)
}