package br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.impl.encoder

import br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.SerializationEncodeInterceptor
import kotlinx.serialization.*
import kotlinx.serialization.modules.SerialModule

class EncoderInterceptor(
        val interceptor: SerializationEncodeInterceptor,
        val delegate: Encoder
) : Encoder by delegate {

    override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T) {
        serializer.serialize(this, value)
    }

    override fun beginStructure(descriptor: SerialDescriptor, vararg typeSerializers: KSerializer<*>): CompositeEncoder {
        return CompositeEncoderInterceptor(interceptor, delegate.beginStructure(descriptor, *typeSerializers))
    }

    override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int, vararg typeSerializers: KSerializer<*>): CompositeEncoder {
        return CompositeEncoderInterceptor(interceptor, delegate.beginCollection(descriptor, collectionSize, *typeSerializers))
    }
}