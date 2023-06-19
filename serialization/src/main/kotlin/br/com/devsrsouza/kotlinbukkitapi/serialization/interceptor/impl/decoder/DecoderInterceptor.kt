package br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.impl.decoder

import br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.SerializationDecodeInterceptor
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder

internal class DecoderInterceptor(
    val interceptor: SerializationDecodeInterceptor,
    val delegate: Decoder,
) : Decoder by delegate {
    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T {
        return deserializer.deserialize(this)
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        return CompositeDecoderInterceptor(interceptor, delegate.beginStructure(descriptor))
    }
}
