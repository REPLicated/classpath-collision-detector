# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).
This project does NOT use semantic versioning.

## [Unreleased]

## 1.0.0

### Added

- [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).
- The plugin now produces a simple text file reporting the collision detection result.
  Having such a task output
  also [fixes incremental builds](https://github.com/REPLicated/classpath-collision-detector/issues/15).

### Changed

- Make JAR of plugin reproducible
- Upgrade plugin testing dependencies and use Gradle 8.7
- Use a version catalog and dependency verification for all plugin dependencies.
- Add Github Actions workflow
