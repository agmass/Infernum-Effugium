package org.agmas.infernum_effugium;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.agmas.infernum_effugium.item.BedrockSickle;
import org.agmas.infernum_effugium.item.InfernumMaceItem;
import org.agmas.infernum_effugium.item.MagmaPebbleItem;
import org.agmas.infernum_effugium.item.PebbleItem;

public class ModItems {

    public static final Item BEDROCK_SICKLES = register(
            new BedrockSickle(new Item.Settings().attributeModifiers(BedrockSickle.createAttributeModifiers(ToolMaterials.DIAMOND, 3, -1)).fireproof(), ToolMaterials.DIAMOND, "bedrock_sickles"),
            "bedrock_sickles"
    );
    public static final Item BLACKSTONE_PEBBLE = register(
            new PebbleItem(new Item.Settings()),
            "blackstone_pebble"
    );
    public static final Item MAGMA_PEBBLE = register(
            new MagmaPebbleItem(new Item.Settings()),
            "magma_pebble"
    );
    public static final Item INFERNUM_MACE = register(
            new InfernumMaceItem(new Item.Settings().attributeModifiers(InfernumMaceItem.createAttributeModifiers(ToolMaterials.DIAMOND, 2, -3.5f)), 2),
            "infernum_mace"
    );

    public static final Item NETHERITE_INFUSED_BEDROCK_SICKLES = register(
            new BedrockSickle(new Item.Settings().attributeModifiers(BedrockSickle.createAttributeModifiers(ToolMaterials.DIAMOND, 4, -1)).fireproof(), ToolMaterials.NETHERITE, "netherite_infused_bedrock_sickles"),
            "netherite_infused_bedrock_sickles"
    );


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
