package br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.bukkit

import kotlinx.serialization.descriptors.SerialDescriptor

internal inline fun <reified A: Annotation> SerialDescriptor.findElementAnnotation(
        elementIndex: Int
): A? {
    return getElementAnnotations(elementIndex).find { it is A } as A?
}

internal inline fun <reified A: Annotation> SerialDescriptor.findEntityAnnotation(): A? {
    return annotations.find { it is A } as A?
}