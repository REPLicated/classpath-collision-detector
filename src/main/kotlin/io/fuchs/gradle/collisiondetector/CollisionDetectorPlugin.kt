package io.fuchs.gradle.collisiondetector

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.util.GradleVersion

open class CollisionDetectorPlugin : Plugin<Project> {

    val taskName: String = "detectCollisions"

    override fun apply(project: Project) {
        if (GradleVersion.current() < GradleVersion.version("4.0")) {
            throw GradleException("This plugin only supports Gradle 4.0 or newer versions")
        }

        project.pluginManager.withPlugin("java") {
            project.tasks.create(taskName, DetectCollisionsTask::class.java)
        }
    }

}