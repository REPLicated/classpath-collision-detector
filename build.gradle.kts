plugins {
    `embedded-kotlin`
    id("com.gradle.plugin-publish") version "1.2.1"
}

val javaVersion = JavaVersion.toVersion(
        providers.fileContents(layout.projectDirectory.file(".java-version")).asText.get())

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.assertj:assertj-core:3.23.1")
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
