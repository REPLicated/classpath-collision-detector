plugins {
    `embedded-kotlin`
    id("com.gradle.plugin-publish") version "1.0.0"
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.assertj:assertj-core:3.23.1")
}

repositories {
    mavenCentral()
}

group = "io.fuchs.gradle.classpath-collision-detector"
version = "0.2"

gradlePlugin {
    plugins.create("classpathCollisionDetectorPlugin") {
        id = "io.fuchs.gradle.classpath-collision-detector"
        implementationClass = "io.fuchs.gradle.collisiondetector.CollisionDetectorPlugin"
        displayName = "Classpath Collision Detector Plugin"
    }
}

pluginBundle {
    website = "https://github.com/REPLicated/classpath-collision-detector"
    vcsUrl = "https://github.com/REPLicated/classpath-collision-detector"
    description = "A Gradle plugin to detect potential classpath collisions between library jars."
    tags = listOf("classpath", "collision", "duplicate", "detector")
}
