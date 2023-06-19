package br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.impl.encoder

import br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.SerializationEncodeInterceptor
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder

internal class CompositeEncoderInterceptor(
        val interceptor: SerializationEncodeInterceptor,
        val delegate: CompositeEncoder
) : CompositeEncoder by delegate {
    override fun encodeBooleanElement(descriptor: SerialDescriptor, index: Int, value: Boolean) {
         delegate.encodeBooleanElement(descriptor, index, interceptor.encodeBoolean(descriptor, index, value))
    }

    override fun encodeByteElement(descriptor: SerialDescriptor, index: Int, value: Byte) {
        delegate.encodeByteElement(descriptor, index, interceptor.encodeByte(descriptor, index, value))
    }

    override fun encodeCharElement(descriptor: SerialDescriptor, index: Int, value: Char) {
        delegate.encodeCharElement(descriptor, index, interceptor.encodeChar(descriptor, index, value))
    }

    override fun encodeDoubleElement(descriptor: SerialDescriptor, index: Int, value: Double) {
        delegate.encodeDoubleElement(descriptor, index, interceptor.encodeDouble(descriptor, index, value))
    }

    override fun encodeFloatElement(descriptor: SerialDescriptor, index: Int, value: Float) {
        delegate.encodeFloatElement(descriptor, index, interceptor.encodeFloat(descriptor, index, value))
    }

    override fun encodeIntElement(descriptor: SerialDescriptor, index: Int, value: Int) {
        delegate.encodeIntElement(descriptor, index, interceptor.encodeInt(descriptor, index, value))
    }

    override fun encodeLongElement(descriptor: SerialDescriptor, index: Int, value: Long) {
        delegate.encodeLongElement(descriptor, index, interceptor.encodeLong(descriptor, index, value))
    }

    override fun encodeShortElement(descriptor: SerialDescriptor, index: Int, value: Short) {
        delegate.encodeShortElement(descriptor, index, interceptor.encodeShort(descriptor, index, value))
    }

    override fun encodeStringElement(descriptor: SerialDescriptor, index: Int, value: String) {
        delegate.encodeStringElement(descriptor, index, interceptor.encodeString(descriptor, index, value))
    }

    override fun <T> encodeSerializableElement(descriptor: SerialDescriptor, index: Int, serializer: SerializationStrategy<T>, value: T) {
        delegate.encodeSerializableElement(descriptor, index, SerializationStrategyInterceptor(interceptor, serializer), value)
    }

}