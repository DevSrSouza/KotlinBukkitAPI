package br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.impl

import br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.SerializationDecodeInterceptor
import br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.SerializationEncodeInterceptor
import br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.impl.decoder.DeserializationStrategyInterceptor
import br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.impl.encoder.SerializationStrategyInterceptor
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat

class StringFormatInterceptor(
        private val delegate: StringFormat,
        private val encodeInterceptor: SerializationEncodeInterceptor,
        private val decodeInterceptor: SerializationDecodeInterceptor
) : StringFormat by delegate {

    override fun <T> parse(deserializer: DeserializationStrategy<T>, raw: String): T {
        return delegate.parse(DeserializationStrategyInterceptor(decodeInterceptor, deserializer), raw)
    }

    override fun <T> stringify(serializer: SerializationStrategy<T>, value: T): String {
        return delegate.stringify(SerializationStrategyInterceptor(encodeInterceptor, serializer), value)
    }
}