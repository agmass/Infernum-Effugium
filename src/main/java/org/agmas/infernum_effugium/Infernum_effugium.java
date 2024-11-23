package org.agmas.infernum_effugium;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class Infernum_effugium implements ModInitializer {

    public static String MOD_ID = "infernumeffugium";

    @Override
    public void onInitialize() {
        ModBlocks.init();
        ModItems.initialize();
    }
}
