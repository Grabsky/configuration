# grabsky/configuration
Experimental [SpongePowered/Configurate](https://github.com/SpongePowered/Configurate) 'wrapper' that aims to reduce time spent on creating HOCON configurations for [PaperMC/Paper](https://github.com/PaperMC/Paper) plugins.

### Serializers
There are a bunch of type serializers built-in but you need to add them to your `ConfigurationOptions` manually. Keep in mind that deserialization is not implemented.

| Type (class)                                            | Serializer (class)                                           | Notes                                                                                                                      |
|---------------------------------------------------------|--------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------|
| `org.bukkit.NamespacedKey`                              | `grabsky.configuration.serializers.NamespacedKeySerializer`  | Uses `namespaced:keys` as values.                                                                                          |
| `org.bukkit.Material`                                   | `grabsky.configuration.serializers.MaterialSerializer`       | Uses `namespaced:keys` as identifiers.                                                                                     |
| `org.bukkit.entity.EntityType`                          | `grabsky.configuration.serializers.EntityTypeSerializer`     | Uses `namespaced:keys` as identifiers.                                                                                     |
| `org.bukkit.enchantments.Enchantment`                   | `grabsky.configuration.serializers.EnchantmentSerializer`    | Uses `namespaced:keys` as identifiers.                                                                                     |
| `grabsky.configuration.serializers.ItemEnchantment`     | `grabsky.configuration.serializers.ItemEnchantment`          | See [formatting guide](#formatting-guide) below.                                                                           |
| `grabsky.configuration.serializers.PersistentDataEntry` | `grabsky.configuration.serializers.PersistentDataSerializer` | See [formatting guide](#formatting-guide) below.                                                                           |
| `org.bukkit.inventory.ItemStack`                        | `grabsky.configuration.serializers.ItemStackSerializer`      | Requires some serializers to work properly. (2)                                                                            |
| `net.kyori.adventure.text.Component`                    | `grabsky.configuration.serializers.ComponentSerializer`      | Uses [MiniMessage](https://docs.adventure.kyori.net/minimessage/index.html) for String -> Component conversion by default. |
| `net.kyori.adventure.sound.Sound`                       | `grabsky.configuration.serializers.SoundSerializer`          | Uses `namespaced:keys` as identifiers.                                                                                     |

1. All serializers using `namespaced:keys` as identifiers require `NamespacedKeySerializer`.
2. Serialization of `ItemStack` requires `MaterialSerializer`, `EnchantmentSerializer`, `ItemEnchantmentSerializer`, `EntityTypeSerializer`, `PersistentDataSerializer` and `ComponentSerializer`.

### Formatting Guide
Not familiar with HOCON? Give [this](https://github.com/lightbend/config/blob/main/HOCON.md) a read.
```hocon
# NamespacedKey <- NamespacedKeySerializer
namespacedkey = "namespaced:key"

# Material <- MaterialSerializer
material = "minecraft:stone"

# EntityType <- EntityTypeSerializer
entity_type = "minecraft:cow"

# Enchantment <- EnchantmentSerializer
enchantment = "minecraft:sharpness"

# ItemEnchantment <- ItemEnchantmentSerializer
item_enchantment = { key = "minecraft:sharpness", level = 5 }

# PersistentDataEntry <- PersistentDataSerializer (byte, byte_array, integer, integer_array, long, long_array, float, double, string)
persistent_data_entry = { key = "configuration:test/string", type = "string", value = "I am a String." }

# Component  <- ComponentSerializer
component = "<red>It uses <rainbow>MiniMessage<red>!"

# Sound <- SoundSerializer
sound = "minecraft:block.note_block.banjo"

# ItemStack <- ItemStackSerializer
item_example {
    # Material <- MaterialSerializer (required)
    material = "minecraft:sword"
    
    # Integer (optional, among with all 'meta' entries)
    amount = 1
    
    # Component <- ComponentSerializer
    meta.name = "<red>You can use <rainbow>MiniMessage<red> here!"
    
    # List<Component> <- ComponentSerializer
    meta.lore = ["<red>and here too."]
    
    # List<ItemEnchantment> <- ItemEnchantmentSerializer
    meta.enchantments = [
        { key = "minecraft:infinity", level = 1 }
    ]
    
    # List<ItemFlag> <- EnumSerializer (?)
    meta.flags = ["HIDE_ENCHANTS"]
    
    # Integer
    meta.custom_model_data = 7 
    
    # List<PersistentDataEntry> <- PersistentDataSerializer
    meta.persistent_data_container = [
        { key = "configuration:test/byte",          type = "byte",          value = 0                                               },
        { key = "configuration:test/byte_array",    type = "byte_array",    value = [0, 1, 1, 0, 0]                                 },
        { key = "configuration:test/integer",       type = "integer",       value = 18                                              },
        { key = "configuration:test/integer_array", type = "integer_array", value = [-2147483648, 0, 2147483647]                    },
        { key = "configuration:test/long",          type = "long",          value = 2374627346327                                   },
        { key = "configuration:test/long_array",    type = "long_array",    value = [-9223372036854775808, 0, 9223372036854775807]  },
        { key = "configuration:test/float",         type = "float",         value = 7.5                                             },
        { key = "configuration:test/double",        type = "double",        value = 7.500000000000009                               },
        { key = "configuration:test/string",        type = "string",        value = "I am a String."                                }
    ] 
    
    # List<ItemEnchantment> <- ItemEnchantmentSerializer (instanceof EnchantmentStorageMeta)
    meta.enchantment_storage = [
        { key = "minecraft:efficiency", level = 5 }
        { key = "minecraft:durability", level = 3 }
        { key = "minecraft:fortune", level = 10 },
    ]
    
    # Integer (instance of Damageable)
    meta.durability = 1
    
    # String (instanceof SkullMeta)
    meta.skull_texture = "BASE64_ENCODED_VALUE"
    
    # EntityType <- EntityTypeSerializer (instanceof BlockStateMeta && instanceof CreatureSpawner)
    meta.spawner_type = "minecraft:cow"
    
    # Integer (instanceof BlockStateMeta && instanceof CreatureSpawner)
    meta.spawner_activation_range = 16  # block radius
    
    # Integer (instanceof BlockStateMeta && instanceof CreatureSpawner)
    meta.spawner_min_spawn_delay = 300  # in ticks
    
    # Integer (instanceof BlockStateMeta && instanceof CreatureSpawner)
    meta.spawner_max_spawn_delay = 500  # in ticks
    
    # Integer (instanceof BlockStateMeta && instanceof CreatureSpawner)
    meta.spawner_max_nearby_entities = 3  # in ticks
    
    # Integer (instanceof BlockStateMeta && instanceof CreatureSpawner)
    meta.spawner_spawn_range = 5  # block radius
    
    # Integer (instanceof BlockStateMeta && instanceof CreatureSpawner)
    meta.spawner_spawn_count = 2
    
}
```


### Getting Started
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
    implementation 'io.github.grabsky:configuration:_version_'
}

tasks {
    // ...other tasks
    shadowJar {
        // ...other configurations
        relocate('io.leangen.geantyref', 'com.example.libs.geantyref')
        relocate('org.spongepowered.configurate', 'com.example.libs.configurate')
        relocate('grabsky.configuration', 'com.example.libs.configuration')
    }
}
```

```java
/* CONFIGURATION CLASS */

// (1) expected to implement grabsky.configuration.Configuration
// (2) can also be defined using 'enum singleton pattern'
public class PluginConfig implements Configuration {

    @ConfigProperty("settings.debug")
    public static Boolean DEBUG;
    
    // This field is ignored because it's not annotated with @ConfigProperty 
    public static Boolean NOT_ANNOTATED;
    
    // This field is ignored because (1) it's not static (2) it's final
    @ConfigProperty("settings.static")
    public final Boolean STATIC = true;
    
}
```

```java
/* PLUGIN MAIN CLASS */

public class PluginMain extends JavaPlugin {

    @Override
    public void onEnable() {
        final ConfigurationOptions options = HoconConfigurationLoader
            .defaultOptions()
            .serializers(s -> {
                // Serializers for primitive and basic types are included by default.
                s.register(Component.class, ComponentSerializer.INSTANCE);
                s.register(Material.class, MaterialSerializer.INSTANCE);
                // ...
            });
        final ConfigurationManager manager = new ConfigurationManager(
            this, // (1) your plugin instance
            options // (2) instance of ConfigurationOptions object
        );
        
        manager.reload(
            PluginConfig.class, // (1) your configuration class
            "settings.conf" // (2) your configuration file name
        );
        
        this.getLogger().info("Debugging mode is " + (PluginConfig.DEBUG == true) ? "enabled." : "disabled."; 
    }
}
```
### Customization
Some of built-in serializers can be partially customizable.

Supplying your own `String` to `Component` serializer:
```java
final ComponentSerializer componentSerializer = new ComponentSerializer((string) -> YourComponentSerializer.parse(string));
```
Supplying your own `namespaced:key` registries:
```java
final MaterialSerializer materialSerializer = new MaterialSerializer((key) -> YourMaterialRegistry.get(key));
final EntityTypeSerializer entityTypeSerializer = new EntityTypeSerializer((key) -> YourEntityTypeRegistry.get(key));
final EnchantmentSerializer enchantmentSerializer = new EnchantmentSerializer((key) -> YourEnchantmentRegistry.get(key));
```
Supporting your own `PersistentDataType` types:
```java
PersistentDataSerializer.REGISTRY.add("itemstack", YourItemStackDataType.INSTANCE);
```