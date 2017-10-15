package io.fuchs.gradle.collisiondetector

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskExecutionException
import org.gradle.api.tasks.util.PatternFilterable
import org.gradle.api.tasks.util.PatternSet
import java.io.File
import java.util.*

open class DetectCollisionsTask : DefaultTask() {

    @get:InputFiles
    var configurations: List<Configuration> = ArrayList()

    @get:Internal
    val collisionFilter: PatternFilterable = PatternSet()

    @TaskAction
    fun detectCollisions() {
        val collisionDetector = CollisionDetector(findClasspathArtifacts())

        val collisions = collisionDetector.detectCollisions()

        if (collisions.isNotEmpty()) {
            collisions.forEach { logCollisionMessage(it) }
            throw TaskExecutionException(this, GradleException("Detected unexpected collisions!"))
        }
    }

    private fun logCollisionMessage(collision: ClasspathCollision) {
        val name = collision.classpathEntry
        val collisionJars = collision.containingArtifacts
                .map { it.artifact }
                .sorted()
                .joinToString("\n")
                .prependIndent("   ")

        val message = "Collision detected! Entry ${name} present in following JARs:\n${collisionJars}"
        project.logger.error(message)
    }

    private fun findClasspathArtifacts(): List<ClasspathArtifact> {
        return configurations
                .flatMap { it.files }
                .filter { it.name.endsWith(".jar") }
                .map { toClassPathArtifact(it) }
    }

    private fun toClassPathArtifact(file: File): ClasspathArtifact {
        val artifactContents = project.zipTree(file)
        val filteredContents = artifactContents.matching(collisionFilter)
        return ClasspathArtifact(file, filteredContents)
    }


}