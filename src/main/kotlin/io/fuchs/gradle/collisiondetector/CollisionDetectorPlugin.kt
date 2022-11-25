package io.fuchs.gradle.collisiondetector

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.util.GradleVersion

import org.gradle.api.plugins.JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME

abstract class CollisionDetectorPlugin : Plugin<Project> {

    private val taskName: String = "detectCollisions"

    override fun apply(project: Project) {
        if (GradleVersion.current() < GradleVersion.version("6.6")) {
            throw GradleException("This plugin only supports Gradle 6.6 or newer versions")
        }

        val task = project.tasks.register(taskName, DetectCollisionsTask::class.java) {
            // convention useful for most Jars, can be replaced completely using 'setExcludes()'
            it.collisionFilter.exclude("META-INF/**", "module-info.class")
        }

        project.pluginManager.withPlugin("java") {
            val runtimeClasspath = project.configurations.getByName(RUNTIME_CLASSPATH_CONFIGURATION_NAME)
            task.configure {
                // for standard Java projects: by default, analyze the runtime classpath
                // can be replaced completely using 'setFrom'
                it.configurations.from(runtimeClasspath)
            }
        }
    }

}