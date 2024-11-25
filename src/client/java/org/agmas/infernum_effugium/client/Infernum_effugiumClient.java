package org.agmas.infernum_effugium.client;

import eu.pb4.polymer.networking.api.client.PolymerClientNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtInt;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModBlocks;
import org.agmas.infernum_effugium.ModEntities;

public class Infernum_effugiumClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PolymerClientNetworking.setClientMetadata(Infernum_effugium.REGISTER_PACKET, NbtInt.of(1));
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BEDROCK_LADDER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ROCKY_BUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ROCKY_BUSH_BUT_ITS_ACTUALLY_A_DISPENSER, RenderLayer.getCutout());
        EntityRendererRegistry.register(ModEntities.PEBBLE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.MAGMA_PEBBLE, FlyingItemEntityRenderer::new);
    }
}
