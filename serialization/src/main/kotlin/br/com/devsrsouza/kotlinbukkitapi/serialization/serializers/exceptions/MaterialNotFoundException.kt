package br.com.devsrsouza.kotlinbukkitapi.serialization.serializers.exceptions

import java.lang.Exception

public class MaterialNotFoundException(public val material: String): Exception("Could not deserialize the Material=$material because it do not exist.")