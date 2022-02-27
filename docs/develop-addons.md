# Addons API
EvergreenHUD addons are a unique feature to HUD mods. It allows
users to add extra elements that may not fit in the base mod.

## Addons for Developers
If you are looking to create an addon yourself, you have come to the
right place. It's super easy!

**It is recommended you use Kotlin to create addons however Java can be
used if required.**

### Gradle Setup

Firstly, make sure you have the jitpack maven repository.

_Gradle Kotlin DSL (build.gradle.kts)_
```kotlin
repositories {
    maven(url = "https://repo.woverflow.cc")
}
```
_Gradle Groovy (build.gradle)_
```groovy
repositories {
    maven { url = 'https://repo.woverflow.cc' }
}
```

Next, you need Kotlin + KSP gradle plugins
**(this is still required if you are using Java)**

_Gradle Kotlin DS (build.gradle.kts)_
```kotlin
plugins {
    kotlin("jvm") version "1.6.10" // use latest
    id("com.google.devtools.ksp") version "1.6.10-1.0.+" // use latest matching kotlin version
}
```

_Gradle Groovy (build.gradle)_
```groovy
plugins {
    id 'org.jetbrains.kotlin.jvm' version '1.6.10' // use latest kotlin version
    id 'com.google.devtools.ksp' version '1.6.10-1.0.+' // use latest matching kotlin version
}
```

KSP is used to pre-process your EvergreenHUD elements to speed up
loading times for users.

Now we need a few dependencies

_Gradle Kotlin DSL (build.gradle.kts)_
```kotlin
val evergreenVersion = "2.0.+" // you can store this however you want

ksp("dev.isxander:evergreenhud-ap:$evergreenVersion:fabric-$minecraftVersion")
modImplementation("dev.isxander:evergreenhud:$evergreenVersion:fabric-$minecraftVersion")
```

_Gradle Groovy (build.gradle)_
```groovy
def evergreenVersion = '2.0.+' // you can store this however you want

ksp "dev.isxander:evergreenhud-ap:$evergreenVersion:fabric-$minecraftVersion"
modImplementation "dev.isxander:evergreenhud:$evergreenVersion:fabric-$minecraftVersion"
```
### Fabric Setup

Now we need to let the Fabric Loader know that we require
EvergreenHUD as a dependency.

_(fabric.mod.json)_
```json
"depends": {
  "evergreenhud": "2.0.x"
}
```

Optionally, you can add entrypoints that hook into the initialization
stages of EvergreenHUD if you so require.

```json5
{
  "entrypoints": {
    "evergreenhud": [
      {
        "adapter": "kotlin", // only if you are using kotlin
        "value": "com.example.myaddon.MyAddon"
      }
    ]
  }
}
```

_Kotlin_
```kotlin
import dev.isxander.evergreenhud.addon.AddonInitializer

object MyAddon : AddonInitializer {
    override fun onPreInitialize() {
        // ...
    }

    override fun onInitialize() {
        // ...
    }
}
```

_Java_
```java
import dev.isxander.evergreenhud.addon.AddonInitializer;

public class MyAddon implements AddonInitializer {
    @Override
    public void onPreInitialize() {
        // ...
    }

    @Override
    public void onInitialize() {
        // ...
    }
}
```

### Creating an element
Creating an is extremely quick and easy.
You can fully implement a `SimpleTextElement` in just a few minutes!

**There are a few rules to elements though...**
* Elements must not have any constructor parameters
* Element IDs must be completely unique, even between addons

In this example, we will be creating an element that displays a string
of the user's choosing.

The most important part of an Element is the annotation.

```kotlin
@ElementMeta(id = "examplemod:exampleelement", name = "Text Element", description = "Displays the text of your choosing!", category = "My Category")
```

This dictates to the annotation processor that the annotated class
is an Element, without it, the element would not be recognised. The ID
of the element must be unique, so it is recommended to use the Minecraft
identifier format: `modid:elementid`.

Create a class anywhere, it doesn't matter, this is another benefit
of the annotation.

_Kotlin_
```kotlin
@ElementMeta(id = "examplemod:exampleelement", name = "Text Element", description = "Displays the text of your choosing!", category = "My Category")
class ElementText : SimpleTextElement("Your Text")
```

_Java_
```java
@ElementMeta(id = "examplemod:exampleelement", name = "Text Element", description = "Displays the text of your choosing!", category = "My Category")
class ElementText extends SimpleTextElement {
    public ElementText() {
        super("Your Text");
    }
}
```

`"Your Text"` is the title of the element. It will be displayed in-game as
`Your Text: $value`

Next, we have to give a value to display. And this is as simple as overriding a method.

_Kotlin_
```kotlin
@ElementMeta(id = "examplemod:exampleelement", name = "Text Element", description = "Displays the text of your choosing!", category = "My Category")
class ElementText : SimpleTextElement("Your Text") {
    override fun calculateValue(): String {
        return "Sample Text"
    }
}
```

_Java_
```java
@ElementMeta(id = "examplemod:exampleelement", name = "Text Element", description = "Displays the text of your choosing!", category = "My Category")
class ElementText extends SimpleTextElement {
    public ElementText() {
        super("Your Text");
    }

    @Override
    public String calculateValue() {
        return "Sample Text";
    }
}
```

#### Implementing Settxi

Settxi is a delegate orientated settings library. Delegates are a kotlin feature
meaning that it's about to get a bit rough for Java users.

We want the user to be able to decide what is displayed rather than our `"Sample Text"`,
so let's add a setting!

_Kotlin_
```kotlin
@ElementMeta(id = "examplemod:exampleelement", name = "Text Element", description = "Displays the text of your choosing!", category = "My Category")
class ElementText : SimpleTextElement("Your Text") {
    var text by string("Sample Text") {
        name = "Text"
        description = "Text to display on our element."
        category = "My Category"
    }

    override fun calculateValue(): String {
        return text
    }
}
```

_Java_
```java
@ElementMeta(id = "examplemod:exampleelement", name = "Text Element", description = "Displays the text of your choosing!", category = "My Category")
class ElementText extends SimpleTextElement {
    public StringSetting text = StringSettingKt.stringSetting(this, "Sample Text", (setting) -> {
        setting.name = "Text";
        setting.description = "Text to display on our element.";
        setting.category = "My Category";
    });

    public ElementText() {
        super("Your Text");
    }

    @Override
    public String calculateValue() {
        return text.get();
    }
}
```

In this example, we have created a new setting with a default value of `"Sample Text"`
with the properties you can see above. This setting is automatically registered.

Make sure to give each setting a unique name if in the same category so EvergreenHUD
doesn't get confused when deserializing.

#### Using the Event Bus

A custom event bus is used because I didn't feel like any existing library really
captures the modularity that EvergreenHUD demands.

With this event bus, you can:
* specify the predicate to call the event
* specify return values

_Kotlin_
```kotlin
@ElementMeta(id = "examplemod:exampleelement", name = "Text Element", description = "Displays the text of your choosing!", category = "My Category")
class ElementText : SimpleTextElement("Your Text") {
    // delegated return is Unit, so we don't technically need to delegate it
    val clientTickEvent by event<ClientTickEvent>(predicate = { /* true by default */ true }) {
        /* my amazing code! */
    }

    val cachedFunction by eventReturnable<ClientTickEvent, Float> {
        myReallyComplicatedFunctionThatReturnsFloat()
    }

    override fun calculateValue(): String {
        return cachedFunction
    }
}
```

_Java_
not supported yet!

#### Other Features

EvergreenHUD caches calculated values for a certain amount of ticks. You can configure
the default amount of ticks in the element constructor arguments.
