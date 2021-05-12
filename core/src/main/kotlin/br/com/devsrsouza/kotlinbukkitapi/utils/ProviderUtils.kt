package br.com.devsrsouza.kotlinbukkitapi.utils

import br.com.devsrsouza.kotlinbukkitapi.controllers.provideProviderController
import org.bukkit.plugin.Plugin
import kotlin.reflect.KClass

val Plugin.provider: Provider get() = Provider(this)

inline fun <reified T : Any> Plugin.provider(): T? = provider(T::class)

fun <T : Any> Plugin.provider(kclass: KClass<T>): T? {
    return provideProviderController().find(this, kclass)
}

@JvmInline
value class Provider(val plugin: Plugin) {
    // false if already has a instance with the same class.
    fun register(any: Any): Boolean = provideProviderController().register(plugin, any)

    // false if was not found
    fun unregister(any: Any): Boolean = provideProviderController().unregister(plugin, any)
}
