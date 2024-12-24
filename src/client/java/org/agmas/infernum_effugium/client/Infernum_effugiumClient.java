package org.agmas.infernum_effugium.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.entity.EntityType;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModBlocks;
import org.agmas.infernum_effugium.ModEntities;

import java.util.ArrayList;
import java.util.UUID;

public class Infernum_effugiumClient implements ClientModInitializer {

    public static ArrayList<UUID> pactPlayers = new ArrayList<>();
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BEDROCK_LADDER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ROCKY_BUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ROCKY_BUSH_BUT_ITS_ACTUALLY_A_DISPENSER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GREED_VAULT, RenderLayer.getCutout());
        EntityRendererRegistry.register(ModEntities.PEBBLE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.MAGMA_PEBBLE, FlyingItemEntityRenderer::new);
        BlockEntityRendererFactories.register(ModEntities.GREED_VAULT, GreedVaultBlockEntityRenderer::new);


        ClientPlayNetworking.registerGlobalReceiver(Infernum_effugium.NETHER_PACT_MODE, (client, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
            boolean bl = buf.readBoolean();
            if (bl) {
                if (!pactPlayers.contains(uuid))
                    pactPlayers.add(uuid);
            } else {
                pactPlayers.remove(uuid);
            }
        });
        ClientPlayConnectionEvents.DISCONNECT.register(((clientPlayNetworkHandler, minecraftClient) -> {
            pactPlayers.clear();
        }));
    }
}
