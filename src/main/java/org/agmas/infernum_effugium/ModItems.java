package org.agmas.infernum_effugium;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.agmas.infernum_effugium.item.BedrockSickle;

public class ModItems {

    public static final Item BEDROCK_SICKLES = register(
            new BedrockSickle(new FabricItemSettings().fireproof()),
            "bedrock_sickles"
    );

    public static Item register(Item item, String id) {
        // Create the identifier for the item.
        Identifier itemID = new Identifier(Infernum_effugium.MOD_ID, id);

        // Register the item.
        Item registeredItem = Registry.register(Registries.ITEM, itemID, item);

        // Return the registered item!
        return registeredItem;
    }

    public static void initialize() {
    }
}
