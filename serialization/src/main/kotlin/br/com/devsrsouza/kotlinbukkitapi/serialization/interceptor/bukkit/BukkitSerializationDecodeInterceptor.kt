package br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.bukkit

import br.com.devsrsouza.kotlinbukkitapi.extensions.translateColor
import br.com.devsrsouza.kotlinbukkitapi.serialization.ChangeColor
import br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.ClearSerializationDecodeInterceptor
import br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.SerializationDecodeInterceptor
import kotlinx.serialization.descriptors.SerialDescriptor

public object BukkitSerializationDecodeInterceptor : SerializationDecodeInterceptor by ClearSerializationDecodeInterceptor() {

    override fun decodeString(descriptor: SerialDescriptor, index: Int, value: String): String {
        return useChangeColor(descriptor, index, value)
    }

    private fun useChangeColor(descriptor: SerialDescriptor, index: Int, value: String): String {
        descriptor.findElementAnnotation<ChangeColor>(index) ?: return value

        return value.translateColor()
    }
}