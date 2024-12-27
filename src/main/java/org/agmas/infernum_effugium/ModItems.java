package org.agmas.infernum_effugium;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.agmas.infernum_effugium.item.*;

public class ModItems {

    public static final Item BEDROCK_SICKLES = register(
            new BedrockSickle(new Item.Settings().fireproof().registryKey(keyOf("bedrock_sickles")).useItemPrefixedTranslationKey(), 2, "bedrock_sickles"),
            "bedrock_sickles"
    );
    public static final Item BLACKSTONE_PEBBLE = register(
            new PebbleItem(new Item.Settings().registryKey(keyOf("blackstone_pebble")).useItemPrefixedTranslationKey()),
            "blackstone_pebble"
    );
    public static final Item MAGMA_PEBBLE = register(
            new MagmaPebbleItem(new Item.Settings().registryKey(keyOf("magma_pebble")).useItemPrefixedTranslationKey()),
            "magma_pebble"
    );
    public static final Item INFERNUM_MACE = register(
            new InfernumMaceItem(new Item.Settings().registryKey(keyOf("infernum_mace")).maxCount(1).useItemPrefixedTranslationKey().attributeModifiers(InfernumMaceItem.createAttributeModifiers()), 2),
            "infernum_mace"
    );

    public static final Item NETHERITE_INFUSED_BEDROCK_SICKLES = register(
            new BedrockSickle(new Item.Settings().fireproof().registryKey(keyOf("netherite_infused_bedrock_sickles")).useItemPrefixedTranslationKey(), 4, "netherite_infused_bedrock_sickles"),
            "netherite_infused_bedrock_sickles"
    );
    public static final Item DEATH_WHISTLE = register(
            new DeathWhistleItem(new Item.Settings().registryKey(keyOf("death_whistle")).useItemPrefixedTranslationKey()),
            "death_whistle"
    );
    public static final Item NETHER_PACT = register(
            new NetherPactItem(new Item.Settings().registryKey(keyOf("nether_pact")).useItemPrefixedTranslationKey()),
            "nether_pact"
    );
    public static final Item PEBBLE_CANNON = register(
            new PebbleCannonItem(new Item.Settings().registryKey(keyOf("pebble_cannon")).maxDamage(800)),
            "pebble_cannon"
    );



    private static RegistryKey<Item> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Infernum_effugium.MOD_ID, id));
    }

    public static Item register(Item item, String id) {
        // Create the identifier for the item.
        Identifier itemID = Identifier.of(Infernum_effugium.MOD_ID, id);

        // Register the item.
        Item registeredItem = Registry.register(Registries.ITEM, itemID, item);

        // Return the registered item!
        return registeredItem;
    }

    public static void initialize() {
    }
}
