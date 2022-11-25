# grabsky/configuration
[![](https://github.com/Grabsky/configuration/actions/workflows/gradle.yml/badge.svg)](https://github.com/Grabsky/configuration/actions/workflows/gradle.yml)
[![](https://jitpack.io/v/Grabsky/configuration.svg)](https://jitpack.io/#Grabsky/configuration)
[![](https://www.codefactor.io/repository/github/grabsky/configuration/badge/main)](https://www.codefactor.io/repository/github/grabsky/configuration/overview/main)  
Experimental library based on [google/gson](https://github.com/google/gson) that lets you map JSON configuration files to static fields. Breaking changes are likely to happen before a stable release, use at your own risk.

<br />

## Requirements
Requires **Java 17** or higher.

<br />

## Paper
If you're planning to use this library for your [PaperMC/Paper](https://github.com/PaperMC/Paper) plugins, you may find [paper module](https://github.com/Grabsky/configuration/blob/main/PAPER_MODULE.md) useful.

<br />

## Getting Started
Just take a look at this example.

```groovy
/* GRADLE BUILD SCRIPT (build.gradle) */

plugins {
    // ...other plugins
    id 'com.github.johnrengelman.shadow' version '7.1.2'
}

repositories {
    // ...other repositories
    maven { url = 'https://jitpack.io' }
}

dependencies {
    // ...other dependencies
    implementation 'com.github.grabsky.configuration:configuration-core:0.9.6-pre'
}

tasks {
    // ...other tasks
    shadowJar {
        // ...other configurations
        relocate('com.google.gson', 'com.example.libs.gson')
        relocate('grabsky.configuration', 'com.example.libs.configuration')
    }
}
```

```json5
/* CONFIGURATION FILE (resources/settings.json) */

{
    "settings": {
        "debug": false  // you should not touch this property unless you know what you're doing.
    }
}
```

```java
/* CONFIGURATION CLASS */

public final class ApplicationSettings extends Configuration {

    @JsonPath("settings.debug")
    public static Boolean DEBUG;
    
    // this field is ignored because it's not annotated with @JsonPath 
    public static Boolean NOT_ANNOTATED;
    
    // this field is ignored because (1) it's not static (2) it's final
    @JsonPath("settings.static")
    public final Boolean STATIC = true;
    
    // override this method to run some code after re-mapping have finished
    @Override
    public void onReload() {
        System.out.println("no error");
    }
    
}
```

```java
/* APPLICATION CLASS */

public class MainApplication extends Application {
    private final Gson gson;
    private final ConfigurationMapper mapper;

    @Override
    public void initialize() {
        // creating instance of Gson
        this.gson = new GsonBuilder()
                .setLenient() // (1) adds support for comments
                .disableHtmlEscaping()
                .registerTypeAdapter(UUID.class, UUIDSerializer.INSTANCE)
                .create();

        // creating instance of ConfigurationMapper
        this.mapper = ConfigurationMapper.create(gson);

        try {
            // running some logic to copy file from 'resources' to desired directory
            final File file = ensureResourcePresence("settings.json", new File("./config/settings.json"));
            // mapping file contents to fields in provided class
            this.mapper.map(ApplicationSettings.class, file);
            // this can fail for several reasons:
            //   (1) syntax errors - no field values are overridden, ensuring your application does not break
            //   (2) reflection access errors - very unlikely to happen unless something modifies your field 
            //       declarations (name, type etc.) at the runtime; ex. javassist
        } catch (final ConfigurationException | IOException e) {
            // usually contains useful information
            e.printStackTrace();
            // you probably want to shutdown the application when loading of initial configuration fails;
            // might be safe to ignore once "something" is already loaded
            if (e.getCause() instanceof JsonParseError)
                this.shutdown("there might be a syntax error somewhere");
            else
                this.shutdown("weird to see that happen, perhaps something modified a field declaration?");
        }

        System.out.println("debug mode is " + (ExampleConfig.DEBUG == true) ? "enabled" : "disabled");
    }
}
```

<br />

## Building (Linux)
```shell
# Cloning repository
$ git clone https://github.com/Grabsky/configuration.git
# Entering cloned repository
$ cd ./configuration
# Compiling and publishing to maven local
$ ./gradlew clean test publishToMavenLocal
```

<br />

## Contributing
This project is open for contributions. Help in regards of improving performance and safety is very appreciated!
