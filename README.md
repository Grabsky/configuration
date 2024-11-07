# configuration
<span>
    <a href=""><img alt="Build Status" src="https://img.shields.io/github/actions/workflow/status/Grabsky/configuration/gradle.yml?style=for-the-badge&logo=github&logoColor=white&label=%20"></a>
    <a href=""><img alt="CodeFactor Grade" src="https://img.shields.io/codefactor/grade/github/Grabsky/configuration/main?style=for-the-badge&logo=codefactor&logoColor=white&label=%20"></a>
</span>
<p></p>

Small configuration library based on **[square/moshi](https://github.com/square/moshi)** which aims to provide an easy way to map JSON files to static fields.

<br />

## Requirements
### [Core](#usage)
Requires **Java 21** (or higher).  

### [Paper Module](https://github.com/Grabsky/configuration/blob/main/PAPER_MODULE.md)
Requires **Java 21** (or higher) and **Paper 1.21.3** (or higher).

<br />

## Getting Started
Library is published to the **[GitHub Packages Registry](https://github.com/Grabsky/configuration/packages/)** and may require additional configuration. You can find more details **[here](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry#using-a-published-package)**.
```groovy
repositories {
    maven { url = "https://maven.pkg.github.com/grabsky/configuration"
        credentials {
            username = findProperty("gpr.actor") ?: System.getenv("GITHUB_ACTOR")
            password = findProperty("gpr.token") ?: System.getenv("GITHUB_TOKEN")
        }
    }
}
```

```groovy
dependencies {
    // CORE
    implementation 'cloud.grabsky:configuration:[_VERSION_]'
    // OPTIONAL PAPER MODULE
    implementation 'cloud.grabsky:configuration-paper:[_VERSION_]'
}
```

<br />

## Usage

This is contents of example JSON file located at `classpath/resources/settings.json`:
```json5
{
    "settings": {
        "debug": false,
        "nullable": null,
        "positive_integer": 35,
        "static_final": "Ignored."
    }
}
```

This is how our `ApplicationSettings` can look like, please read comments:
```java
public final class ApplicationSettings extends Configuration {

    // @JsonPath specifies a path to the value.
    @JsonPath("settings.debug")
    public static Boolean DEBUG;
    
    // @JsonNullable indicates that JsonAdapter#fromJson returning null is 100% valid and no exception should be thrown.
    @JsonNullable
    @JsonPath("settings.nullable")
    public static String NULLABLE;
    
    // @JsonAdapter allows you to specify on-demand JsonAdapter that is going to be used for that field.
    @JsonPath("settings.positive_integer")
    @JsonAdapter(fromJson = PositiveIntegerAdapter.class)
    public static Integer POSITIVE_INTEGER;
    
    // This field is ignored because it is not annotated with @JsonPath.
    public static Boolean NOT_ANNOTATED;
    
    // This field is ignored because:
    //   1. It's not static.
    //   2. It's final.
    // Only public, static, non-final fields can be used for mapping.
    @JsonPath("settings.static_final")
    public final Boolean STATIC_FINAL = true;
    
    // Override this method to run some code after re-mapping have finished.
    @Override
    public void onReload() {
        System.out.println("Reload succeeded with no errors.");
    }
    
}
```

Mapping example, please read comments:
```java
public class MainApplication extends Application {
    
    private Moshi moshi;
    private ConfigurationMapper mapper;

    @Override
    public void initialize() {
        // Creating an instance of Moshi.
        this.moshi = new Moshi.Builder().build();

        // Creating an instance of ConfigurationMapper.
        this.mapper = ConfigurationMapper.create(moshi);
        
        try {
            // Running some logic to copy file from 'resources' to desired directory.
            final File file = ensureResourceExistence("settings.json", new File("./config/settings.json"));
            // Mapping file contents. Below method is going to fail when:
            //   1. An exception is thrown during the JSON parsing. It's usually JsonSyntaxException or JsonDataException
            //        but method is going to fail on ANY exception.
            //   2. An exception is thrown during reflection access. It's very unlikely yo happen unless something
            //        modifies your field declarations (name, type, etc.) at the runtime.
            this.mapper.map(ApplicationSettings.class, file);
        } catch (final ConfigurationMappingException | IOException e) {
            // Printing stack trace is helpful to know what went wrong:
            e.printStackTrace();
            // Generally when configuration loading fails and you don't have any kind of "fallback" values,
            //   it may be a good idea to prevent application from starting.
            final String shutdownMessage = (e.getCause() instanceof JsonSyntaxException || e.getCause() instanceof JsonDataException)
                    ? "There might be a syntax error somewhere. Closing..."
                    : (e.getCause() instanceof IllegalAccessException)
                            ? "Weird to see that happen, perhaps something modified a field declaration? Closing..."
                            : "Something went wrong. Closing...";
            this.shutdown(shutdownMessage);
        }
        // Looks like everything loaded properly, you should be able to access your static fields now:
        System.out.println("Is DEBUG enabled? " + (ExampleConfig.DEBUG == true) ? "yes." : "no.");
        System.out.println("Is NULLABLE null? " + (ExampleConfig.NULLABLE == null) ? "yes." : "no.");
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
This project is open for contributions. Help in regards of improving performance, adding new features or fixing bugs is greatly appreciated.
