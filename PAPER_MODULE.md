# grabsky/configuration-paper
[![](https://github.com/Grabsky/configuration/actions/workflows/gradle.yml/badge.svg)](https://github.com/Grabsky/configuration/actions/workflows/gradle.yml)
[![](https://jitpack.io/v/Grabsky/configuration.svg)](https://jitpack.io/#Grabsky/configuration)  
This module adds serializers for common Bukkit objects. Check out [Serializers](#serializers) and [Syntax](#syntax) sections below for more reference.

<br />

## Requirements
Requires **Java 17** or higher.  
Requires **Paper 1.19** or higher.

<br />

## Getting Started
Simply add `configuration-core` and `configuration-paper` modules to your dependencies.
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
    implementation 'com.github.grabsky:configuration-core:0.9.4-pre'
    implementation 'com.github.grabsky:configuration-paper:0.9.4-pre'
}

tasks {
    // ...other tasks
    shadowJar {
        // ...other configurations
        relocate('grabsky.configuration', 'com.example.libs.configuration')
    }
}

```
## Serializers
Serializers for following types are included:
```
+---------------------------------------------------------+-------------------------------------------------------------+
| Type (class)                                            | Serializer (class)                                          |
+---------------------------------------------------------+-------------------------------------------------------------+
| org.bukkit.NamespacedKey                                | grabsky.configuration.serializers.NamespacedKeySerializer   |
| org.bukkit.Material                                     | grabsky.configuration.serializers.MaterialSerializer        |
| org.bukkit.entity.EntityType                            | grabsky.configuration.serializers.EntityTypeSerializer      |
| org.bukkit.enchantments.Enchantment                     | grabsky.configuration.serializers.EnchantmentSerializer     |
| org.bukkit.inventory.ItemFlag                           | grabsky.configuration.serializers.ItemFlagSerializer        |
| org.bukkit.inventory.ItemStack                          | grabsky.configuration.serializers.ItemStackSerializer       |
|                                                         |                                                             |
| net.kyori.adventure.text.Component                      | grabsky.configuration.serializers.ComponentSerializer       |
| net.kyori.adventure.sound.Sound                         | grabsky.configuration.serializers.SoundSerializer           |
| net.kyori.adventure.sound.Sound.Source                  | grabsky.configuration.serializers.SoundSourceSerializer     |
|                                                         |                                                             |
| grabsky.configuration.paper.EnchantmentEntry            | grabsky.configuration.paper.ItemEnchantmentSerializer       |
| grabsky.configuration.paper.PersistentDataEntry         | grabsky.configuration.paper.PersistentDataSerializer        |
+---------------------------------------------------------+-------------------------------------------------------------+
```
You need to add them to your `Gson` instance manually. Keep in mind that deserialization is not implemented.
Most of built-in serializers depend on each other.

<br />

## Syntax
This is an example of what serializers are capable of:
```json5
{
    // NamespacedKey
    "key": "namespaced:key",

    // Material
    "material": "minecraft:diamond",

    // EntityType
    "entity_type": "minecraft:cow",

    // Enchantment=
    "enchantment": "minecraft:sharpness",

    // EnchantmentEntry
    "enchantment_entry": { "key": "minecraft:sharpness", "level": 5 },

    // PersistentDataEntry
    "persistent_data_entry": { "key": "configuration:test/string", "type": "string",  "value": "I am a String." },

    // Component
    "component": "<red>It uses <rainbow>MiniMessage<red>!",

    // Sound
    "sound": { "key": "minecraft:block.note_block.banjo", "source": "master", "volume": 1.0, "pitch": 1.0 },

    // ItemStack
    "item_example": {

        // Material - required
        "material": "minecraft:diamond_sword",

        // Integer - optional, among with all 'meta' entries
        "amount": 1,

        "meta": {

            // Component
            "name": "<red>You can use <rainbow>MiniMessage<red> here!",

            // List<Component>
            "lore": ["<red>and here too."],

            // List<EnchantmentEntry>
            "enchantments": [
                { "key": "minecraft:infinity", "level": 1 }
            ],

            // List<EnchantmentEntry> - exclusive for enchanted books
            "enchantment_storage": [
                { "key": "minecraft:infinity", "level": 1 }
            ],

            // List<ItemFlag>
            "flags": ["HIDE_ATTRIBUTES", "HIDE_DESTROYS", "HIDE_DYE", "HIDE_ENCHANTS", "HIDE_PLACED_ON", "HIDE_POTION_EFFECTS", "HIDE_UNBREAKABLE"],

            // Integer
            "custom_model_data": 7,

            // List<PersistentDataEntry>
            "persistent_data_container":[
                { "key": "configuration:test/byte", "type": "byte", "value": 0 },
                { "key": "configuration:test/byte_array", "type": "byte_array", "value": [0, 1, 1, 0, 1] },
                { "key": "configuration:test/integer", "type": "integer", "value": 0 },
                { "key": "configuration:test/integer_array", "type": "integer_array", "value": [-5, 0, 5] },
                { "key": "configuration:test/long", "type": "long", "value": 173 },
                { "key": "configuration:test/long_array", "type": "long_array", "value": [-50, 0, 50] },
                { "key": "configuration:test/float", "type": "float", "value": 7.5 },
                { "key": "configuration:test/double", "type": "double", "value": 7.5005 },
                { "key": "configuration:test/string", "type": "string", "value": "I am a String." }
            ],

            // Integer - exclusive for 'damageable' items like tools and armor
            "durability": 1,

            // String - exclusive for player head
            "skull_texture": "BASE64_ENCODED_VALUE",

            // EntityType - exclusive for spawners
            "spawner_type": "minecraft:cow",

            // Integer - exclusive for spawners (block radius)
            "spawner_activation_range": 16,

            // Integer - exclusive for spawners (ticks)
            "spawner_min_spawn_delay": 300,

            // Integer - exclusive for spawners (ticks)
            "spawner_max_spawn_delay": 500,

            // Integer - exclusive for spawners (ticks)
            "spawner_max_nearby_entities": 3,

            // Integer - exclusive for spawners (block radius)
            "spawner_spawn_range": 5,

            // Integer - exclusive for spawners
            "spawner_spawn_count": 2
        }
    }
}
```
