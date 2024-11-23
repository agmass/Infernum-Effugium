package org.agmas.infernum_effugium;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.util.Identifier;

public class Infernum_effugium implements ModInitializer {

    public static String MOD_ID = "infernumeffugium";

    @Override
    public void onInitialize() {
        ModBlocks.init();
        ModItems.initialize();

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((t)->{
            t.add(ModItems.BEDROCK_SICKLES);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register((t)->{
            t.add(ModBlocks.CHISELED_BEDROCK);
            t.add(ModBlocks.BEDROCK_BRICKS);
            t.add(ModBlocks.BEDROCK_LADDER);
            t.add(ModBlocks.CORRUPTED_BEDROCK_BRICKS);
        });
    }
}
