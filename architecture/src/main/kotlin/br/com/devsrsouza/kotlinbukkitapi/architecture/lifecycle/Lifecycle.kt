package br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle

import br.com.devsrsouza.kotlinbukkitapi.architecture.KotlinPlugin

/**
 * Holds a lifecycle listener class and its priority
 */
data class Lifecycle(
        val priority: Int,
        val listener: LifecycleListener<KotlinPlugin>
) : Comparable<Lifecycle> {

    override fun compareTo(
            other: Lifecycle
    ): Int = other.priority.compareTo(priority)
}