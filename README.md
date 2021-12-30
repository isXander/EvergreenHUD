 <!--suppress HtmlDeprecatedAttribute -->
<div align="center">

# `EvergreenHUD`

[![](https://jitpack.io/v/dev.isXander/EvergreenHUD.svg)](https://jitpack.io/#dev.isXander/EvergreenHUD)
[![CC BY-NC-SA 4.0 License](https://img.shields.io/badge/License-CC%20BY--NC--SA%204.0-%23ff3333)](https://creativecommons.org/licenses/by-nc-sa/4.0/)
[![Github All Releases](https://img.shields.io/github/downloads/isXander/EvergreenHUD/total.svg?style=flat)](https://github.com/isXander/EvergreenHUD/releases)
[![Donate](https://img.shields.io/badge/donate-patreon-ff6666.svg?style=flat)](https://paypal.me/isxander)
[![Discord Chat](https://img.shields.io/discord/780023008668287017.svg)](https://short.isxander.dev/discord)

EvergreenHUD is a 1.18.1 fabric and 1.8.9 forge mod to improve and add upon your heads up display.
Used and loved by over a thousand people, with the most customizable HUD
system ever created for Minecraft.

#### Links
[Latest Stable Release](https://github.com/isXander/EvergreenHUD/releases/latest)  
[Hypixel Thread](https://hypixel.net/threads/v2-beta-out-now-dev.isxander.evergreenhud-1-3-1.3787277/)  
[Discord invite (click to get support)](https://discord.gg/AJv5ZnNT8q)

## Summary

[Getting Started](#getting-started)  
[Deployment](#deployment)  
[Contributing](#contributing)  
[Authors](#authors)  
[License](#license)  
[Credits](#credits)  

## Getting Started

These instructions will help you get started to help develop
or test the mod.

### Installing

This is how you can get a working development environment.

Decompile Minecraft

    gradle setupDevWorkspace genSources

Now you will be able to open the folder as a project in your chosen IDE.

## Testing

To test the mod, you will need to use a run configuration.

Add an application run configuration with the following details

    Classpath: EvergreenHUD.main
    Main Class: GradleStart
    Program Arguments: --tweakClass org.spongepowered.asm.launch.MixinTweaker --mixin mixins.dev.isxander.evergreenhud.json

## Deployment

When deploying, you will need to use gradle to build the project.

Add the following line to a new gradle run configuration

    clean build

Once built, add the jar file to the `mods` folder in your minecraft directory commonly found in `%appdata%/.minecraft`

## Contributing

Please make sure to make a useful contribution that will benefit either the user or fellow developers in a noticeable way.

## Authors

**isXander** - *Founder of the project* -
  [isXander](https://github.com/isXander)  

## License

This project is licensed under the [LGPL 3.0](LICENSE.md)
GNU Lesser General Public License v3.0 - see the [LICENSE](LICENSE.md) file for
details.

## Credits

**EvergreenHUD would not be possible without the help from these softwares:**

![YourKit Logo](https://www.yourkit.com/images/yklogo.png) 

YourKit supports open source projects with innovative and intelligent tools
for monitoring and profiling Java and .NET applications.
YourKit is the creator of 
[YourKit Java Profiler](https://www.yourkit.com/java/profiler/),
[YourKit .NET Profiler](https://www.yourkit.com/.net/profiler/),
[YourKit YouMonitor](https://www.yourkit.com/youmonitor).


Copyright (C) 2021 isXander with love <3
</div>
