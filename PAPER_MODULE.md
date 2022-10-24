# grabsky/configuration-paper
This module adds serializers for common Bukkit objects.

### Serializers
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

### Syntax
Built-in serializers expects following format:
```json
{
    // NamespacedKey (NamespacedKeySerializer)
    "key": "namespaced:key",

    // Material (MaterialSerializer)
    "material": "minecraft:diamond",

    // EntityType (EntityTypeSerializer)
    "entity_type": "minecraft:cow",

    // Enchantment (EnchantmentSerializer)
    "enchantment": "minecraft:sharpness",

    // EnchantmentEntry (EnchantmentEntrySerializer)
    "enchantment_entry": { "key": "minecraft:sharpness", "level": 5 },

    // PersistentDataEntry (PersistentDataSerializer)
    "persistent_data_entry": { "key": "configuration:test/string", "type": "string",  "value": "I am a String." },

    // Component (ComponentSerializer)
    "component": "<red>It uses <rainbow>MiniMessage<red>!",

    // Sound (SoundSerializer)
    "sound": { "key": "minecraft:block.note_block.banjo", "source": "MASTER", "volume": 1.0, "pitch": 1.0 },

    // ItemStack (ItemStackSerializer)
    "item_example": {

        // Material (MaterialSerializer) - required
        "material": "minecraft:diamond_sword",

        // Integer - optional, among with all 'meta' entries
        "amount": 1,

        "meta": {

            // Component (ComponentSerializer)
            "name": "<red>You can use <rainbow>MiniMessage<red> here!",

            // List<Component> (ComponentSerializer)
            "lore": ["<red>and here too."],

            // List<ItemEnchantment> (ItemEnchantmentSerializer)
            "enchantments": [
                { "key": "minecraft:infinity", "level": 1 }
            ],

            // List<ItemEnchantment> (ItemEnchantmentSerializer) - exclusive for enchanted books
            "enchantment_storage": [
                { "key": "minecraft:infinity", "level": 1 }
            ],

            // List<ItemFlag>
            "flags": ["HIDE_ATTRIBUTES", "HIDE_DESTROYS", "HIDE_DYE", "HIDE_ENCHANTS", "HIDE_PLACED_ON", "HIDE_POTION_EFFECTS", "HIDE_UNBREAKABLE"],

            // Integer
            "custom_model_data": 7,

            // List<PersistentDataEntry> (PersistentDataSerializer)
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

            // String - exclusive for Material.PLAYER_HEAD.
            "skull_texture": "BASE64_ENCODED_VALUE",

            // EntityType (EntityTypeSerialize) - exclusive for spawners
            "spawner_type": "minecraft:cow",

            // Integer - exclusive for spawners
            "spawner_activation_range": 16, // block radius

            // Integer - exclusive for spawners
            "spawner_min_spawn_delay": 300, // in ticks

            // Integer - exclusive for spawners
            "spawner_max_spawn_delay": 500, // in ticks

            // Integer - exclusive for spawners
            "spawner_max_nearby_entities": 3, // in ticks

            // Integer - exclusive for spawners
            "spawner_spawn_range": 5, // block radius

            // Integer - exclusive for spawners
            "spawner_spawn_count": 2
        }
    }
}
```