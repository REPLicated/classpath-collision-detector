[![Download](https://img.shields.io/gradle-plugin-portal/v/io.fuchs.gradle.classpath-collision-detector)](https://plugins.gradle.org/plugin/io.fuchs.gradle.classpath-collision-detector)

# Gradle Classpath Collision Detector

Experimental Gradle plugin to detect potential classpath collisions between library jars. 

Fails the build whenever an unexpected duplicate entry is found in two or more artifacts.

## Usage

The plugin requires Gradle 6.6 or newer.

You can either apply the plugin with the plugins block
```groovy
plugins {
	id "io.fuchs.gradle.classpath-collision-detector" version "0.3"
}
```
or using the legacy version
```groovy
buildscript {
	repositories {
		maven {
			url "https://plugins.gradle.org/m2/"
		}
	}
	dependencies {
		classpath "io.fuchs.gradle.classpath-collision-detector:classpath-collision-detector:0.3"
	}
}

apply plugin: "io.fuchs.gradle.classpath-collision-detector"
```

Then run `./gradlew detectCollisions` to run the detection.

If the project also applies the `java` plugin the task searches the `runtimeClasspath` for collisions.

### Customization

You can also explicitly specify the configuration
```groovy
tasks.named('detectCollisions', DetectCollisionsTask).configure {
	configurations.from(project.configurations.runtimeClasspath)
}
```

To ignore certain conflicts you can add exclude patterns that match collisions you are not interested in e.g.

```groovy
tasks.named('detectCollisions', DetectCollisionsTask).configure {
	collisionFilter {
		exclude('**.html', '**.txt', '**.properties')
	}
}
```
