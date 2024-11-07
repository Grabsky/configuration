# configuration-paper
<span>
    <a href=""><img alt="Build Status" src="https://img.shields.io/github/actions/workflow/status/Grabsky/configuration/gradle.yml?style=for-the-badge&logo=github&logoColor=white&label=%20"></a>
    <a href=""><img alt="CodeFactor Grade" src="https://img.shields.io/codefactor/grade/github/Grabsky/configuration/main?style=for-the-badge&logo=codefactor&logoColor=white&label=%20"></a>
</span>
<p></p>

This module adds serializers for common Bukkit objects. Check out **[Adapters](#adapters)** and **[Syntax](#syntax)** sections below for more reference.

<br />

## Requirements
Requires **Java 17** (or higher) and **Paper 1.20.1** (or higher).

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
    implementation 'cloud.grabsky:configuration-paper:[_VERSION_]'
}
```

<br />

## Adapters
Adapters for following types are included:

```
cloud.grabsky.configuration.paper.adapter
├─ MaterialAdapter(Factory) ───────────── (org.bukkit.Material)
├─ EntityTypeAdapter(Factory) ─────────── (org.bukkit.entity.EntityType)
├─ EnchantmentAdapter(Factory) ────────── (org.bukkit.enchantments.Enchantment)
├─ ItemFlagAdapter ────────────────────── (org.bukkit.inventory.ItemFlag)
├─ ItemStackAdapter(Factory) ──────────── (org.bukkit.inventory.ItemStack)
├─ PersistentDataTypeAdapter ──────────── (org.bukkit.persistence.PersistentDataType)
│
├─ StringComponentAdapter ─────────────── (java.lang.String)
├─ ComponentAdapter ───────────────────── (net.kyori.adventure.text.Component)
├─ SoundAdapter(Factory)  ─────────────── (net.kyori.adventure.sound.Sound)
├─ SoundSourceAdapter ─────────────────── (net.kyori.adventure.sound.Sound.Source)
│
├─ EnchantmentEntryAdapter(Factory) ───── (cloud.grabsky.configuration.paper.object.EnchantmentEntry)
└─ PersistentDataEntryAdapter(Factory) ── (cloud.grabsky.configuration.paper.object.PersistentDataEntry)
```

You can add them to your `Moshi` instance manually:
```java
final Moshi moshi = new Moshi.Builder()
        .add(NamespacedKey.class, NamespacedKeyAdapter.INSTANCE)
        .add(MaterialAdapterFactory.INSTANCE)
        .build();
final ConfigurationMapper mapper = ConfigurationMapper.create(moshi);
```

or use `PaperConfigurationMapper#create`:
```java
final PaperConfigurationMapper mapper = PaperConfigurationMapper.create();
```

Keep in mind that **①** deserialization is ***not*** implemented, **②** most of built-in serializers depend on each other.

<br />

## Syntax
This is an example of what serializers are capable of:

- **[NamespacedKeyAdapter](#adapters)** does not depend on any adapter.

  ```json5
  "key": "namespaced:key"
  ```
- **[MaterialAdapterFactory](#adapters)** depends on **[NamespacedKeyAdapter](#adapters)**.

  ```json5
  "material": "minecraft:diamond"
  ```
- **[EntityTypeAdapterFactory](#adapters)** depends on **[NamespacedKeyAdapter](#adapters)**.

  ```json5
  "entity_type": "minecraft:cow"
  ```
- **[EnchantmentAdapterFactory](#adapters)** depends on **[NamespacedKeyAdapter](#adapters)**.

  ```json5
  "enchantment": "minecraft:sharpness"
  ```
- **[EnchantmentEntryAdapterFactory](#adapters)** depends on **[EnchantmentAdapterFactory](#adapters)**.

  ```json5
  "enchantment_entry": { "key": "minecraft:sharpness", "level": 5 }
  ```
- **[PersistentDataTypeAdapter](#adapters)** does not depend on any adapter.

  ```json5
  "persistent_data_type": { "string" }
  ```
- **[PersistentDataEntryAdapterFactory](#adapters)** depends on **[NamespacedKeyAdapter](#adapters)** and **[PersistentDataTypeAdapter](#adapters)**.

  ```json5
  "persistent_data_entry": { "key": "claims:claim_level", "type": "integer", "value": 1 }
  ```
- **[ComponentAdapter](#adapters)** does not depend on any adapter.

  ```json5
  "string_component": ["<red>First Line", "<green>Second Line"]  
  ```
- **[ComponentAdapter](#adapters)** does not depend on any adapter.

  ```json5
  "component": "<red>It uses <rainbow>MiniMessage<red>!"
  ```
- **[SoundSourceAdapter](#adapters)** does not depend on any adapter.

  ```json5
  "sound_source": "master"
  ```
- **[SoundAdapterFactory](#adapters)** depends on **[NamespacedKeyAdapter](#adapters)** and **[SoundSourceAdapter](#adapters)**.

  ```json5
  "sound": { "key": "minecraft:block.note_block.banjo", "source": "master", "volume": 1.0, "pitch": 1.0 }
  ```
- **[ItemStackAdapterFactory](#adapters)** depends on ***all*** adapters listed above, ***except*** **[StringComponentAdapter](#adapters)**, **[SoundSourceAdapter](#adapters)** and **[SoundAdapter](#adapters)**.

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

          "item_flags": ["HIDE_ATTRIBUTES", "HIDE_DESTROYS", "HIDE_DYE", "HIDE_ENCHANTS", "HIDE_PLACED_ON", "HIDE_POTION_EFFECTS", "HIDE_UNBREAKABLE"],

          "custom_model_data": 7,

          "persistent_data_container":[
              { "key": "configuration:a", "type": "byte", "value": 0 },
              { "key": "configuration:b", "type": "byte_array", "value": [-120, 0, 120 ] },
              { "key": "configuration:c", "type": "integer", "value": 0 },
              { "key": "configuration:d", "type": "integer_array", "value": [-5, 0, 5] },
              { "key": "configuration:e", "type": "long", "value": 173 },
              { "key": "configuration:f", "type": "long_array", "value": [-50, 0, 50] },
              { "key": "configuration:g", "type": "float", "value": 7.5 },
              { "key": "configuration:h", "type": "double", "value": 7.5000005 },
              { "key": "configuration:i", "type": "string", "value": "Hello, World!" }
          ],
  
          // accepts NBT on <= 1.20.4 or item data components on >= 1.20.5
          "components": "[food={nutrition:3,saturation:1}]"

          // exclusive to 'damageable' items like tools and armor
          "durability": 1,

          // exclusive to player heads
          "skull_texture": "BASE64_ENCODED_VALUE",

          // exclusive to spawners
          "spawner_entity_type": "minecraft:cow",

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
