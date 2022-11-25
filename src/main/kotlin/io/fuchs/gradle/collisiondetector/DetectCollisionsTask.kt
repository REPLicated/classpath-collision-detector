package io.fuchs.gradle.collisiondetector

import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.ArchiveOperations
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskExecutionException
import org.gradle.api.tasks.util.PatternSet
import org.slf4j.LoggerFactory
import java.io.File
import javax.inject.Inject

@CacheableTask
abstract class DetectCollisionsTask : DefaultTask() {

    private val logger = LoggerFactory.getLogger(DetectCollisionsTask::class.java)

    @get:InputFiles
    @get:Classpath
    abstract val configurations: ConfigurableFileCollection

    @get:Input
    val collisionFilter: PatternSet = PatternSet()

    @get:Inject
    protected abstract val archiveOperations: ArchiveOperations

    fun collisionFilter(filterConfig: Action<PatternSet>) {
        filterConfig.execute(collisionFilter)
    }

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

        val message = "Collision detected! Entry $name present in following JARs:\n${collisionJars}"
        logger.error(message)
    }

    private fun findClasspathArtifacts(): List<ClasspathArtifact> {
        return configurations.files
                .filter { it.name.endsWith(".jar") }
                .map { toClassPathArtifact(it) }
    }

    private fun toClassPathArtifact(file: File): ClasspathArtifact {
        val artifactContents = archiveOperations.zipTree(file)
        val filteredContents = artifactContents.matching(collisionFilter)
        return ClasspathArtifact(file, filteredContents)
    }


}