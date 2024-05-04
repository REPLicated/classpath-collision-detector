package io.fuchs.gradle.collisiondetector

import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.ArchiveOperations
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
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

    @get:Inject
    abstract val archiveOperations: ArchiveOperations

    @get:OutputFile
    abstract val reportFile: RegularFileProperty

    @get:Internal
    val collisionFilter: PatternSet = PatternSet()

    init {
        reportFile.convention(project.layout.buildDirectory.file("reports/classpath-collisions.txt"))
    }

    fun collisionFilter(filterConfig: Action<PatternSet>) {
        filterConfig.execute(collisionFilter)
    }

    @TaskAction
    fun detectCollisions() {
        val collisionDetector = CollisionDetector(findClasspathArtifacts())
        val collisions = collisionDetector.detectCollisions()

        val resolvedReportFile = reportFile.get().asFile
        if (collisions.isNotEmpty()) {
            val collisionMessages = collisions.map { toCollisionMessage(it) }
            collisionMessages.forEach { logger.error(it) }
            resolvedReportFile.writeText(collisionMessages.joinToString("\n"))
            throw TaskExecutionException(this, GradleException("Detected unexpected collisions!"))
        } else {
            resolvedReportFile.writeText("No collisions detected")
        }
    }

    private fun toCollisionMessage(collision: ClasspathCollision): String {
        val name = collision.classpathEntry
        val collisionJars = collision.containingArtifacts
            .map { it.artifact }
            .sorted()
            .joinToString("\n")
            .prependIndent("   ")
        return "Collision detected! Entry $name present in following JARs:\n${collisionJars}"
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
