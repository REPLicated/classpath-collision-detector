![Build Status](https://github.com/REPLicated/classpath-collision-detector/actions/workflows/build.yml/badge.svg)
[![Download](https://img.shields.io/gradle-plugin-portal/v/io.fuchs.gradle.classpath-collision-detector)](https://plugins.gradle.org/plugin/io.fuchs.gradle.classpath-collision-detector)

# Gradle Classpath Collision Detector

Gradle plugin to detect potential classpath collisions between library jars. 

Fails the build whenever an unexpected duplicate entry is found in two or more artifacts.

## Usage

The plugin requires Gradle 6.6 or newer.

You can apply the plugin with the plugins block
```kotlin
plugins {
    id("io.fuchs.gradle.classpath-collision-detector") version "1.0.0"
}
```

Then run `./gradlew detectCollisions` to run the detection.

If the project also applies the `java` plugin the task searches the `runtimeClasspath` for collisions.

### Customization

You can also explicitly specify the configuration
```kotlin
tasks.named<DetectCollisionsTask>("detectCollisions").configure {
    configurations.from(project.configurations.runtimeClasspath)
}
```

To ignore certain conflicts you can add exclude patterns that match collisions you are not interested in e.g.

```kotlin
tasks.named<DetectCollisionsTask>("detectCollisions").configure {
    collisionFilter {
        exclude("**.html", "**.txt", "**.properties")
    }
}
```
