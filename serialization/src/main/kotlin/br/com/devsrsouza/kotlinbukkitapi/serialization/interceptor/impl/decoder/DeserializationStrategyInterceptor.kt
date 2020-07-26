package br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.impl.decoder

import br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.SerializationDecodeInterceptor
import kotlinx.serialization.Decoder
import kotlinx.serialization.DeserializationStrategy

class DeserializationStrategyInterceptor<T>(
        val interceptor: SerializationDecodeInterceptor,
        val delegate: DeserializationStrategy<T>
) : DeserializationStrategy<T> by delegate {
    override fun deserialize(decoder: Decoder): T {
        return delegate.deserialize(DecoderInterceptor(interceptor, decoder))
    }
}