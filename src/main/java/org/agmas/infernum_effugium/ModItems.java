package org.agmas.infernum_effugium;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.agmas.infernum_effugium.item.*;

public class ModItems {

    public static final Item BEDROCK_SICKLES = register(
            new BedrockSickle(new FabricItemSettings().fireproof(), 2),
            "bedrock_sickles"
    );
    public static final Item BLACKSTONE_PEBBLE = register(
            new PebbleItem(new FabricItemSettings()),
            "blackstone_pebble"
    );
    public static final Item NETHER_PACT = register(
            new NetherPactItem(new FabricItemSettings()),
            "nether_pact"
    );
    public static final Item MAGMA_PEBBLE = register(
            new PebbleItem(new FabricItemSettings()),
            "magma_pebble"
    );
    public static final Item MARKET_GARDENER = register(
            new MarketGardenerItem(new FabricItemSettings(), 1),
            "market_gardener"
    );
    public static final Item INFERNUM_MACE = register(
            new InfernumMaceItem(new FabricItemSettings(), 2),
            "infernum_mace"
    );

    public static final Item NETHERITE_INFUSED_BEDROCK_SICKLES = register(
            new BedrockSickle(new FabricItemSettings().fireproof(), 4),
            "netherite_infused_bedrock_sickles"
    );

    public static final Item DEATH_WHISTLE = register(
            new DeathWhistleItem(new FabricItemSettings()),
            "death_whistle"
    );
    public static final Item PEBBLE_CANNON = register(
            new PebbleCannonItem(new FabricItemSettings().maxDamage(800)),
            "pebble_cannon"
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
