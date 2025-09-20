package org.agmas.infernum_effugium.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtInt;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModBlocks;
import org.agmas.infernum_effugium.ModEntities;
import org.agmas.infernum_effugium.util.NetherPactUpdates;

import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;

public class Infernum_effugiumClient implements ClientModInitializer {

    public static ArrayList<UUID> pactPlayers = new ArrayList<>();
    public static int netherSkinColor = new Color(255, 194,194,255).getRGB();
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BEDROCK_LADDER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ROCKY_BUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ROCKY_BUSH_BUT_ITS_ACTUALLY_A_DISPENSER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GREED_VAULT, RenderLayer.getCutout());
        EntityRendererRegistry.register(ModEntities.PEBBLE, FlyingItemEntityRenderer::new);
        BlockEntityRendererFactories.register(ModEntities.GREED_VAULT, GreedVaultBlockEntityRenderer::new);


        ClientPlayNetworking.registerGlobalReceiver(NetherPactUpdates.NetherPactModePayload.ID, (payload, context) -> {
            if (payload.nethered()) {
                if (!pactPlayers.contains(payload.playerToAdd()))
                    pactPlayers.add(payload.playerToAdd());
            } else {
                pactPlayers.remove(payload.playerToAdd());
            }
        });
        ClientPlayConnectionEvents.DISCONNECT.register(((clientPlayNetworkHandler, minecraftClient) -> {
            pactPlayers.clear();
        }));
    }
}
