import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "br.com.devsrsouza.kotlinbukkitapi.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
//java {
//    sourceCompatibility = JavaVersion.VERSION_17
//    targetCompatibility = JavaVersion.VERSION_17
//}
//tasks.withType<KotlinCompile>().configureEach {
//    kotlinOptions {
//        jvmTarget = JavaVersion.VERSION_17.toString()
//    }
//}

dependencies {
    implementation(libs.plugin.kotlin)
}

gradlePlugin {
    plugins {
        register("kotlinBukkitApiBuild") {
            id = "kotlinbukkitapi.build"
            implementationClass = "br.com.devsrsouza.kotlinbukkitapi.buildlogic.KBAPIBuildPlugin"
        }
    }
}