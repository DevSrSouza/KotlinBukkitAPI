package br.com.devsrsouza.kotlinbukkitapi.serialization

import kotlinx.serialization.SerialInfo

/**
 * Annotation used to automatically translate color codes from the configuration texts
 * when using Kotlinx.serialization and KotlinBukkitAPI [SerializtionConfig] ([KotlinPlugin.config]).
 */
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
public annotation class ChangeColor