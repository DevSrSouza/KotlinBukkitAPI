package br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.impl.encoder

import br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.SerializationEncodeInterceptor
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.encoding.Encoder

internal class SerializationStrategyInterceptor<T>(
        val interceptor: SerializationEncodeInterceptor,
        val delegate: SerializationStrategy<T>
) : SerializationStrategy<T> by delegate {
    override fun serialize(encoder: Encoder, value: T) {
        delegate.serialize(EncoderInterceptor(interceptor, encoder), value)
    }
}