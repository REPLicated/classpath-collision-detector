package io.fuchs.gradle.collisiondetector

class CollisionDetector(private val classpathArtifacts: List<ClasspathArtifact>) {

    private val entryToArtifacts: MutableMap<String, MutableCollection<ClasspathArtifact>> = HashMap()

    fun detectCollisions(): Collection<ClasspathCollision> {
        classpathArtifacts.forEach { scanArtifactContents(it) }
        return findCollidingEntries()
    }

    private fun findCollidingEntries(): Collection<ClasspathCollision> {
        return entryToArtifacts.filterValues { it.size > 1 }
                .map { ClasspathCollision(it.key, it.value) }
    }

    private fun scanArtifactContents(artifact: ClasspathArtifact) {
        artifact.artifactContents.visit { contentElement ->
            if (!contentElement.isDirectory) {
                val elementPath = contentElement.path
                val artifactsWithFile = entryToArtifacts.getOrPut(elementPath) { mutableListOf() }
                artifactsWithFile.add(artifact)
                entryToArtifacts[elementPath] = artifactsWithFile
            }
        }
    }

}