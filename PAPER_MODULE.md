# grabsky/configuration-paper
[![](https://github.com/Grabsky/configuration/actions/workflows/gradle.yml/badge.svg)](https://github.com/Grabsky/configuration/actions/workflows/gradle.yml)
[![](https://www.codefactor.io/repository/github/grabsky/configuration/badge/main)](https://www.codefactor.io/repository/github/grabsky/configuration/overview/main)  
This module adds serializers for common Bukkit objects. Check out **[Serializers](#serializers)** and **[Syntax](#syntax)** sections below for more reference.

<br />

## Requirements
Requires **Java 17** (or higher) and **Paper 1.19** (or higher).

<br />

## Getting Started

To use this project in your plugin, add following repository:

```groovy
repositories {
  maven { url = 'https://repo.grabsky.cloud/releases' }
}
```

Then specify dependency:

```groovy
dependencies {
  implementation 'cloud.grabsky:configuration-core:[version]'
  implementation 'cloud.grabsky:configuration-paper:[version]'
}
```

Consider **[relocating](https://imperceptiblethoughts.com/shadow/configuration/relocation/)** to prevent version
mismatch issues.

<br />

## Serializers

Serializers for following types are included:

```
cloud.grabsky.configuration.paper
├── MaterialSerializer             (org.bukkit.Material)
├── EntityTypeSerializer           (org.bukkit.entity.EntityType)
├── EnchantmentSerializer          (org.bukkit.enchantments.Enchantment)
├── ItemFlagSerializer             (org.bukkit.inventory.ItemFlag)
├── ItemStackSerializer            (org.bukkit.inventory.ItemStack)
│
├── ComponentSerializer            (net.kyori.adventure.text.Component)
├── SoundSerializer                (net.kyori.adventure.sound.Sound)
├── SoundSourceSerializer          (net.kyori.adventure.sound.Sound.Source)
│
├── EnchantmentEntrySerializer     (cloud.grabsky.configuration.paper.object.EnchantmentEntry)
└── PersistentDataEntrySerializer  (cloud.grabsky.configuration.paper.object.PersistentDataEntry)
```

You need to add them to your `Gson` instance manually:

```java
final Gson gson=new GsonBuilder()
        // other options
        .registerTypeAdapter(NamespacedKey.class,NamespacedKeySerializer.INSTANCE)
        .create();
```

Keep in mind that deserialization is ***not*** implemented. Most of built-in serializers depend on each other.

<br />

## Syntax

This is an example of what serializers are capable of:

- **[NamespacedKeySerializer](#serializers)** does not depend on any serializer.

  ```json5
  "key": "namespaced:key"
  ```
- **[MaterialSerializer](#serializers)** depends on **[NamespacedKeySerializer](#serializers)**.

  ```json5
  "material": "minecraft:diamond"
  ```
- **[EntityTypeSerializer](#serializers)** depends on **[NamespacedKeySerializer](#serializers)**.

  ```json5
  "entity_type": "minecraft:cow"
  ```
- **[EnchantmentSerializer](#serializers)** depends on **[NamespacedKeySerializer](#serializers)**.

  ```json5
  "enchantment": "minecraft:sharpness"
  ```
- **[EnchantmentEntrySerializer](#serializers)** depends on **[EnchantmentSerializer](#serializers)**.

  ```json5
  "enchantment_entry": { "key": "minecraft:sharpness", "level": 5 }
  ```
- **[PersistentDataEntrySerializer](#serializers)** depends on **[NamespacedKeySerializer](#serializers)**.

  ```json5
  "persistent_data_entry": { "key": "configuration:test/string", "type": "string",  "value": "I am a String." }
  ```
- **[ComponentSerializer](#serializers)** does not depend on any serializer.

  ```json5
  "component": "<red>It uses <rainbow>MiniMessage<red>!"
  ```
- **[SoundSourceSerializer](#serializers)** does not depend on any serializer.

  ```json5
  "sound_source": "master"
  ```
- **[SoundSerializer](#serializers)** depends on **[NamespacedKeySerializer](#serializers)** and **[SoundSourceSerializer](#serializers)**.

  ```json5
  "sound": { "key": "minecraft:block.note_block.banjo", "source": "master", "volume": 1.0, "pitch": 1.0 }
  ```
- **[ItemStackSerializer](#serializers)** depends on ***all*** serializers listed above, ***except*** **[SoundSourceSerializer](#serializers)** and **[SoundSerializer](#serializers)**.

  ```json5
  "item_example": {

      // required
      "material": "minecraft:diamond_sword",

      // optional (defaults to 1)
      "amount": 1,

      // optional (defaults to none, empty meta)
      "meta": {
          "name": "<red>You can use <rainbow>MiniMessage<red> here!",

          "lore": ["<red>and here too."],

          "enchantments": [
              { "key": "minecraft:infinity", "level": 1 }
          ],

          // exclusive to enchanted books
          "enchantment_storage": [
              { "key": "minecraft:infinity", "level": 1 }
          ],

          "flags": ["HIDE_ATTRIBUTES", "HIDE_DESTROYS", "HIDE_DYE", "HIDE_ENCHANTS", "HIDE_PLACED_ON", "HIDE_POTION_EFFECTS", "HIDE_UNBREAKABLE"],

          "custom_model_data": 7,

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

          // exclusive to 'damageable' items like tools and armor
          "durability": 1,

          // exclusive to player heads
          "skull_texture": "BASE64_ENCODED_VALUE",

          // exclusive to spawners
          "spawner_type": "minecraft:cow",

          // exclusive to spawners (block radius)
          "spawner_activation_range": 16,

          // exclusive to spawners (ticks)
          "spawner_min_spawn_delay": 300,

          // exclusive to spawners (ticks)
          "spawner_max_spawn_delay": 500,

          // exclusive to spawners (ticks)
          "spawner_max_nearby_entities": 3,

          // exclusive to spawners (block radius)
          "spawner_spawn_range": 5,

          // exclusive to spawners
          "spawner_spawn_count": 2
      }
  }
  ```
