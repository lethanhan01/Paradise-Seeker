# Paradise_Seeker
# SimpleRPG - Role-playing Game

**TOPIC 2**  
**Exercise code:** OOP.002  
**Difficulty:** 4/5

## Project Description

Build a SimpleRPG (Role-playing game).

## Core Features

### Character Control
The player controls one or more characters in a map stored in a data structure similar to the one shown below, where each cell corresponds to a different map type (land, grass, water, etc.). On the map there are monsters that can move.

### Combat System
- Player characters and monsters have stats that determine their status and stamina (e.g. HP, MP, Attack, Defense, Speed...).
- Players can attack monsters and use special skills.
- Likewise, monsters can also approach and attack players.

### Map Navigation
Players can move back and forth between different maps (for example when entering areas M0, M1, M2... on the map) or go to the end of the game (for example when entering area END on the map).

## Sample Map

| M1 | 1 | 1 | 1 | 2 | 2 | 0 | 0 | 0 | END |
|----|---|---|---|---|---|---|---|---|-----|
| 1  | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0   |
| 1  | 3 | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 5   |
| 1  | 3 | 3 | 3 | 3 | 0 | 0 | 0 | 5 | 5   |
| 1  | 3 | 3 | 3 | 3 | 0 | 0 | 0 | 0 | 0   |
| 0  | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0   |
| 0  | 0 | 0 | 0 | 0 | 0 | 1 | 0 | 1 | 0   |
| 0  | 0 | 0 | 0 | 0 | 1 | 0 | 0 | 0 | 1   |
| M0 | 0 | 1 | 1 | 1 | 1 | 0 | 4 | 0 | 1   |
| 1  | 1 | 1 | 1 | 1 | 1 | 1 | 1 | 1 | 1   |

### Map Legend:
- **M1, M0**: Portals for switching between maps
- **END**: Game ending point
- **0-5**: Different terrain types (land, grass, water, etc.)

## Additional Features

**Note:** Students can develop additional features such as:
- Graphics
- Inventory system
- Plot system
- And other features

## Technology Stack

This project is built with a focus on Object-Oriented Programming (OOP) with a difficulty level of 4/5.

## Getting Started

This is an educational project aimed at practicing Object-Oriented Programming concepts through building a simple RPG game.

---

*This project serves as a practical implementation of OOP principles in game development.*
A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and a main class extending `Game` that sets the first screen.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
