package io.fuchs.gradle.collisiondetector

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.util.GradleVersion

import org.gradle.api.plugins.JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME
import org.gradle.language.base.plugins.LifecycleBasePlugin.VERIFICATION_GROUP

abstract class CollisionDetectorPlugin : Plugin<Project> {

    private val taskName: String = "detectCollisions"

    override fun apply(project: Project) {
        if (GradleVersion.current() < GradleVersion.version("6.6")) {
            throw GradleException("This plugin only supports Gradle 6.6 or newer versions")
        }

        val task = project.tasks.register(taskName, DetectCollisionsTask::class.java) {
            description = "Detect potential classpath collisions between library jars."
            group = VERIFICATION_GROUP
            // convention useful for most Jars, can be replaced completely using 'setExcludes()'
            collisionFilter.exclude("META-INF/**", "module-info.class")
        }

        project.pluginManager.withPlugin("java") {
            val runtimeClasspath = project.configurations.getByName(RUNTIME_CLASSPATH_CONFIGURATION_NAME)
            task.configure {
                // for standard Java projects: by default, analyze the runtime classpath
                // can be replaced completely using 'setFrom'
                configurations.from(runtimeClasspath)
            }
        }
    }

}
