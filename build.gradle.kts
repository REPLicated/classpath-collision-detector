plugins {
    `embedded-kotlin`
    alias(libs.plugins.pluginPublish)
}

val javaVersion = JavaVersion.toVersion(
        providers.fileContents(layout.projectDirectory.file(".java-version")).asText.get())

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

dependencies {
    testImplementation(libs.junit)
    testImplementation(libs.assertj.core)
}

repositories {
    mavenCentral()
}

tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

group = "io.fuchs.gradle.classpath-collision-detector"
version = "0.3"

gradlePlugin {
    website = "https://github.com/REPLicated/classpath-collision-detector"
    vcsUrl = "https://github.com/REPLicated/classpath-collision-detector"

    plugins {
        create("classpathCollisionDetectorPlugin") {
            id = "io.fuchs.gradle.classpath-collision-detector"
            implementationClass = "io.fuchs.gradle.collisiondetector.CollisionDetectorPlugin"
            displayName = "Classpath Collision Detector Plugin"
            description = "A Gradle plugin to detect potential classpath collisions between library jars."
            tags = listOf("classpath", "collision", "duplicate", "detector")
        }
    }
}
