package br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.impl.decoder

import br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.SerializationDecodeInterceptor
import kotlinx.serialization.*
import kotlinx.serialization.modules.SerialModule

class DecoderInterceptor(
        val interceptor: SerializationDecodeInterceptor,
        val delegate: Decoder
) : Decoder by delegate {
    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T {
        return deserializer.deserialize(this)
    }

    override fun beginStructure(descriptor: SerialDescriptor, vararg typeParams: KSerializer<*>): CompositeDecoder {
        return CompositeDecoderInterceptor(interceptor, delegate.beginStructure(descriptor, *typeParams))
    }

}