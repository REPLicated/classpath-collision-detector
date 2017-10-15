package io.fuchs.gradle.collisiondetector.testkit

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class CollisionDetectorPluginTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()


    @Test
    fun `plugin applies without error`() {
        copyBuildFileToTempDir("apply_plugin_only.gradle")

        GradleRunner.create()
                .withProjectDir(temporaryFolder.root)
                .withTestKitDir(temporaryFolder.newFolder())
                .withPluginClasspath()
                .build()
    }

    @Test
    fun `task detects no collision`() {
        copyBuildFileToTempDir("apply_plugin_only.gradle")

        val buildResult = GradleRunner.create()
                .withProjectDir(temporaryFolder.root)
                .withTestKitDir(temporaryFolder.newFolder())
                .withPluginClasspath()
                    .withArguments(":detectCollisions")
                .build()

        val detectCollisionsTask = buildResult.task(":detectCollisions")
        assertEquals(TaskOutcome.SUCCESS, detectCollisionsTask?.outcome)
    }

    @Test
    fun `detects collisions and fails`() {
        copyBuildFileToTempDir("found_collisions.gradle")

        val buildResult = GradleRunner.create()
                .withProjectDir(temporaryFolder.root)
                .withTestKitDir(temporaryFolder.newFolder())
                .withPluginClasspath()
                .withArguments(":detectCollisions")
                .buildAndFail()

        val detectCollisionsTask = buildResult.task(":detectCollisions")
        assertEquals(TaskOutcome.FAILED, detectCollisionsTask?.outcome)
    }

    @Test
    fun `all collisions are ignored`() {
        copyBuildFileToTempDir("ignored_collisions.gradle")

        val buildResult = GradleRunner.create()
                .withProjectDir(temporaryFolder.root)
                .withTestKitDir(temporaryFolder.newFolder())
                .withPluginClasspath()
                .withArguments(":detectCollisions")
                .build()

        val detectCollisionsTask = buildResult.task(":detectCollisions")
        assertEquals(TaskOutcome.SUCCESS, detectCollisionsTask?.outcome)
    }

    fun copyBuildFileToTempDir(buildFile: String) {
        val file = temporaryFolder.newFile("build.gradle")
        ClassLoader.getSystemResourceAsStream(buildFile).use { inputStream ->
            file.outputStream().use { inputStream.copyTo(it) }
        }
    }

}