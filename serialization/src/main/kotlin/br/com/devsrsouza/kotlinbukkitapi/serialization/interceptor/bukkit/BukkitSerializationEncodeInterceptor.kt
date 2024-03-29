package br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.bukkit

import br.com.devsrsouza.kotlinbukkitapi.extensions.reverseTranslateColor
import br.com.devsrsouza.kotlinbukkitapi.serialization.ChangeColor
import br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.ClearSerializationEncodeInterceptor
import br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.SerializationEncodeInterceptor
import kotlinx.serialization.descriptors.SerialDescriptor

public object BukkitSerializationEncodeInterceptor : SerializationEncodeInterceptor by ClearSerializationEncodeInterceptor() {

    override fun encodeString(descriptor: SerialDescriptor, index: Int, value: String): String {
        return useChangeColor(descriptor, index, value)
    }

    private fun useChangeColor(descriptor: SerialDescriptor, index: Int, value: String): String {
        descriptor.findElementAnnotation<ChangeColor>(index) ?: return value

        return value.reverseTranslateColor()
    }
}
