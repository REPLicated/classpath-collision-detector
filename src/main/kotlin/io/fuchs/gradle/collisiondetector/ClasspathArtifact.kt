package io.fuchs.gradle.collisiondetector

import org.gradle.api.file.FileTree
import java.io.File

class ClasspathArtifact(val artifact: File, val artifactContents: FileTree)

class ClasspathCollision(val classpathEntry: String, val containingArtifacts: Collection<ClasspathArtifact>)

