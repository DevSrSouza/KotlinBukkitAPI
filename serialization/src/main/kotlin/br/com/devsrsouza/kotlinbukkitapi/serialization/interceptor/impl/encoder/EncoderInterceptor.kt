package br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.impl.encoder

import br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.SerializationEncodeInterceptor
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder

class EncoderInterceptor(
        val interceptor: SerializationEncodeInterceptor,
        val delegate: Encoder
) : Encoder by delegate {

    override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T) {
        serializer.serialize(this, value)
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        return CompositeEncoderInterceptor(interceptor, delegate.beginStructure(descriptor))
    }

    override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder {
        return CompositeEncoderInterceptor(interceptor, delegate.beginCollection(descriptor, collectionSize))
    }

}