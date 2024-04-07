package io.fuchs.gradle.collisiondetector.testkit

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

class CollisionDetectorPluginTest {

    @Test
    fun `plugin applies without error`(@TempDir tempDir: Path) {
        copyBuildFileToTempDir("apply_plugin_only.gradle", tempDir)

        GradleRunner.create()
                .withProjectDir(tempDir.toFile())
                .withPluginClasspath()
                .build()
    }

    @Test
    fun `plugin applies without error on minimal supported Gradle version`(@TempDir tempDir: Path) {
        copyBuildFileToTempDir("apply_plugin_only.gradle", tempDir)

        GradleRunner.create()
                .withProjectDir(tempDir.toFile())
                .withPluginClasspath()
                .withGradleVersion("6.6.1")
                .build()
    }

    @Test
    fun `task is compatible with configuration cache`(@TempDir tempDir: Path) {
        copyBuildFileToTempDir("apply_plugin_only.gradle", tempDir)

        GradleRunner.create()
                .withProjectDir(tempDir.toFile())
                .withPluginClasspath()
                .withArguments(":detectCollisions", "--configuration-cache")
                .build()
    }

    @Test
    fun `task detects no collision`(@TempDir tempDir: Path) {
        copyBuildFileToTempDir("apply_plugin_only.gradle", tempDir)

        val buildResult = GradleRunner.create()
                .withProjectDir(tempDir.toFile())
                .withPluginClasspath()
                    .withArguments(":detectCollisions")
                .build()

        val detectCollisionsTask = buildResult.task(":detectCollisions")
        assertEquals(TaskOutcome.SUCCESS, detectCollisionsTask?.outcome)
    }

    @Test
    fun `detects collisions and fails`(@TempDir tempDir: Path) {
        copyBuildFileToTempDir("found_collisions.gradle", tempDir)

        val buildResult = GradleRunner.create()
                .withProjectDir(tempDir.toFile())
                .withPluginClasspath()
                .withArguments(":detectCollisions")
                .buildAndFail()

        val detectCollisionsTask = buildResult.task(":detectCollisions")
        assertEquals(TaskOutcome.FAILED, detectCollisionsTask?.outcome)
    }

    @Test
    fun `all collisions are ignored`(@TempDir tempDir: Path) {
        copyBuildFileToTempDir("ignored_collisions.gradle", tempDir)

        val buildResult = GradleRunner.create()
                .withProjectDir(tempDir.toFile())
                .withPluginClasspath()
                .withArguments(":detectCollisions")
                .build()

        val detectCollisionsTask = buildResult.task(":detectCollisions")
        assertEquals(TaskOutcome.SUCCESS, detectCollisionsTask?.outcome)
    }

    private fun copyBuildFileToTempDir(buildFile: String, tempDir: Path) {
        val file = tempDir.resolve("build.gradle").toFile()
        ClassLoader.getSystemResourceAsStream(buildFile).use { inputStream ->
            file.outputStream().use { inputStream.copyTo(it) }
        }
    }

}
