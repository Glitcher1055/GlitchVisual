# GlitchVisual

Client-side Fabric mod project targeting Minecraft **1.21.11** (Java 21).

## Current prototype
- Red/black GlitchVisual Click GUI, inspired by the supplied NovaVisuals-style client layout.
- Right Shift opens/closes the menu; Escape closes it.
- Category tabs: Combat, Visuals, World, HUD.
- Module rows have clickable visual toggle states.

**Important:** this prototype implements the GUI shell and demo toggle states only. The listed effects are not yet wired to actual Minecraft rendering/gameplay. The supplied JAR declares Minecraft 1.16.5 and contains compiled classes, so it cannot be made compatible with 1.21.11 by simply renaming it; each feature needs porting/reimplementation.

## Build
Install JDK 21 and Gradle 8.14+ (or use an IDE with Gradle support), then run:

```sh
gradle build
```

The remapped mod JAR will be under `build/libs/`.
