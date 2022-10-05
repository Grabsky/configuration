# grabsky/configuration
[![](https://github.com/Grabsky/configuration/actions/workflows/gradle.yml/badge.svg)](https://github.com/Grabsky/configuration/actions/workflows/gradle.yml)
[![](https://jitpack.io/v/Grabsky/configuration.svg)](https://jitpack.io/#Grabsky/configuration)  
Experimental [SpongePowered/Configurate](https://github.com/SpongePowered/Configurate) 'wrapper' that aims to reduce time spent on creating HOCON configurations for [PaperMC/Paper](https://github.com/PaperMC/Paper) plugins.

### Serializers
There are a bunch of type serializers built-in but you need to add them to your `ConfigurationOptions` manually. Keep in mind that deserialization is not implemented.
```
+-------------------------------------------------------+-------------------------------------------------------------+
| Type (class)                                          | Serializer (class)                                          |
+-------------------------------------------------------+-------------------------------------------------------------+
| org.bukkit.NamespacedKey                              | grabsky.configuration.serializers.NamespacedKeySerializer   |
| org.bukkit.Material                                   | grabsky.configuration.serializers.MaterialSerializer        |
| org.bukkit.entity.EntityType                          | grabsky.configuration.serializers.EntityTypeSerializer      |
| org.bukkit.enchantments.Enchantment                   | grabsky.configuration.serializers.EnchantmentSerializer     |
| org.bukkit.inventory.ItemStack                        | grabsky.configuration.serializers.ItemStackSerializer       |
|                                                       |                                                             |
| net.kyori.adventure.text.Component                    | grabsky.configuration.serializers.ComponentSerializer       |
| net.kyori.adventure.sound.Sound                       | grabsky.configuration.serializers.SoundSerializer           |
|                                                       |                                                             |
| grabsky.configuration.serializers.ItemEnchantment     | grabsky.configuration.serializers.ItemEnchantmentSerializer |
| grabsky.configuration.serializers.PersistentDataEntry | grabsky.configuration.serializers.PersistentDataSerializer  |
+-------------------------------------------------------+-------------------------------------------------------------+
```
Most of built-in type serializers depend on eachother.

### Formatting Guide
Not familiar with HOCON? Give [this](https://github.com/lightbend/config/blob/main/HOCON.md) a read.

```hocon
# NamespacedKey (NamespacedKeySerializer)
namespacedkey = "namespaced:key"

# Material (MaterialSerializer)
material = "minecraft:stone"

# EntityType (EntityTypeSerializer)
entity_type = "minecraft:cow"

# Enchantment (EnchantmentSerializer)
enchantment = "minecraft:sharpness"

# ItemEnchantment (ItemEnchantmentSerializer)
item_enchantment = { key = "minecraft:sharpness", level = 5 }

# PersistentDataEntry (PersistentDataSerializer)
persistent_data_entry = { key = "configuration:test/string", type = "string", value = "I am a String." }

# Component (ComponentSerializer)
component = "<red>It uses <rainbow>MiniMessage<red>!"

# Sound (SoundSerializer)
sound = "minecraft:block.note_block.banjo"

# ItemStack (ItemStackSerializer)
item_example {
    # Material (MaterialSerializer) - required
    material = "minecraft:sword"
    
    # Integer - optional, among with all 'meta' entries
    amount = 1
    
    # Component (ComponentSerializer)
    meta.name = "<red>You can use <rainbow>MiniMessage<red> here!"
    
    # List<Component> (ComponentSerializer)
    meta.lore = ["<red>and here too."]
    
    # List<ItemEnchantment> (ItemEnchantmentSerializer)
    meta.enchantments = [
        { key = "minecraft:infinity", level = 1 }
    ]
    
    # List<ItemFlag>
    meta.flags = ["HIDE_ATTRIBUTES", "HIDE_DESTROYS", "HIDE_DYE", "HIDE_ENCHANTS", "HIDE_PLACED_ON", "HIDE_POTION_EFFECTS", "HIDE_UNBREAKABLE"]
    
    # Integer
    meta.custom_model_data = 7 
    
    # List<PersistentDataEntry> (PersistentDataSerializer)
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
    
    # List<ItemEnchantment> (ItemEnchantmentSerializer) - exclusive for enchanted books
    meta.enchantment_storage = [
        { key = "minecraft:efficiency", level = 5 }
        { key = "minecraft:durability", level = 3 }
        { key = "minecraft:fortune", level = 10 },
    ]
    
    # Integer - exclusive for 'damageable' items like tools and armor
    meta.durability = 1
    
    # String - exclusive for Material.PLAYER_HEAD.
    meta.skull_texture = "BASE64_ENCODED_VALUE"
    
    # EntityType (EntityTypeSerialize) - exclusive for spawners
    meta.spawner_type = "minecraft:cow"
    
    # Integer - exclusive for spawners
    meta.spawner_activation_range = 16  # block radius
    
    # Integer - exclusive for spawners
    meta.spawner_min_spawn_delay = 300  # in ticks
    
    # Integer - exclusive for spawners
    meta.spawner_max_spawn_delay = 500  # in ticks
    
    # Integer - exclusive for spawners
    meta.spawner_max_nearby_entities = 3  # in ticks
    
    # Integer - exclusive for spawners
    meta.spawner_spawn_range = 5  # block radius
    
    # Integer - exclusive for spawners
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
    implementation 'com.github.grabsky:configuration:0.9.3-pre'
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

```hocon
# CONFIGURATION FILE (resources/settings.conf)

settings {
    debug = false  # You should not touch this entry unless you know what you're doing.
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