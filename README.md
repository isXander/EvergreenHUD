# EvergreenHUD

EvergreenHUD is a forge mod to improve and add upon your heads up display.
I, Xander, have been working on this mod for over 3 months and believe it really
is the best HUD mod out there for 1.8.

#### Announcement
I would love if someone could work on a multi-version system for the mod as I
think it could become very popular for fabric players and maybe even modpacks.

*Contact me on discord @ isXander#0162*

#### Links
- [Latest Stable Release](https://github.com/Evergreen-Client/EvergreenHUD/releases/latest)
- [Hypixel Thread](https://hypixel.net/threads/v2-beta-out-now-evergreenhud-1-3-1.3787277/)

## Summary

- [Getting Started](#getting-started)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [Authors](#authors)
- [License](#license)
- [Credits](#credits)

## Getting Started

These instructions will help you get started to help develop
or test the mod.

### Installing

This is how you can get a working development environment.

Decompile Minecraft

    gradlew setupDecompWorkspace

If you use [Intellij](https://www.jetbrains.com/idea/), then run this command

    gradlew idea genIntellijRuns

If you use [Eclipse](https://www.eclipse.org/) (not recommended), use this

    gradlew eclipse

Now you will be able to open the folder as a project in your chosen IDE.

## Testing

To test the mod, you will need to use a run configuration.

Add an application run configuration with the following details

    Classpath: EvergreenHUD.main
    Main Class: GradleStart

## Deployment

When deploying, you will need to use gradle to build the project.

Add the following line to a new gradle run configuration

    clean build

Once built, add the jar file to the `mods` folder in your minecraft directory commonly found in `%appdata%/.minecraft`

## Contributing

Please make sure to make a useful contribution that will benefit either the user or fellow developers in a noticeable way.

## Authors

- **isXander** - *Founder of the project* -
  [isXander](https://github.com/isXander)

## License

This project is licensed under the [GPL 3.0](LICENSE)
GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for
details

## Credits

#### EvergreenHUD would not be possible without the help from these softwares:

![YourKit Logo](https://www.yourkit.com/images/yklogo.png) 

YourKit supports open source projects with innovative and intelligent tools
for monitoring and profiling Java and .NET applications.
YourKit is the creator of 
[YourKit Java Profiler](https://www.yourkit.com/java/profiler/),
[YourKit .NET Profiler](https://www.yourkit.com/.net/profiler/),
[YourKit YouMonitor](https://www.yourkit.com/youmonitor).
