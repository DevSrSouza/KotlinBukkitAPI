# KotlinBukkitAPI

KotlinBukkitAPI is a API for Bukkit using the cool features of Kotlin to make your lifes much easely. Edit
Add topics

* Need help? contact me on [Twitter](twitter.com/DevSrSouza)


## Prerequisites
* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

## Clone
The following steps will ensure your project is cloned properly.

`git clone --recursive https://github.com/DevSrSouza/KotlinBukkitAPI.git`

## Building
#### Unix (Linux / Mac)
```
./gradlew shadowJar
```

#### Windows
```
./gradlew.bat shadowJar
```


# Setup for development

### Adding KotlinBukkitAPI to maven local

#### Unix (Linux / Mac)
```
./gradlew publishToMavenLocal
```

#### Windows

```
./gradlew.bat publishToMavenLocal
```

### Maven

```xml
<dependency>
  <groupId>br.com.devsrsouza</groupId>
  <artifactId>kotlinbukkitapi</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <scope>provided</scope>
</dependency>
```

### Gradle

```groovy
repositories {
  mavenLocal()
}

dependencies {
  compileOnly 'br.com.devsrsouza:kotlinbukkitapi:0.0.1-SNAPSHOT'
}
```
