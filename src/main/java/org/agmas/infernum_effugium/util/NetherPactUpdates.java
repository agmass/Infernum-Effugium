package org.agmas.infernum_effugium.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.agmas.infernum_effugium.Infernum_effugium;

import java.util.List;

public class NetherPactUpdates {


    public static void sendHumanModeUpdate(PlayerEntity player) {

        MinecraftServer server = player.getServer();

        PacketByteBuf data = PacketByteBufs.create();
        data.writeUuid(player.getUuid());
        data.writeBoolean(false);

        server.execute(() -> {
            server.getPlayerManager().getPlayerList().forEach((p)->{
                ServerPlayNetworking.send(p, Infernum_effugium.NETHER_PACT_MODE, data);
            });
        });
    }

    public static void refreskSkinsOnClient(ServerPlayerEntity player) {

        MinecraftServer server = player.getServer();

        server.execute(() -> {
            server.getPlayerManager().getPlayerList().forEach((p)->{
                if (p.hasStatusEffect(Infernum_effugium.NETHER_PACT)) {
                    PacketByteBuf data = PacketByteBufs.create();
                    data.writeUuid(p.getUuid());
                    data.writeBoolean(true);
                    ServerPlayNetworking.send(player, Infernum_effugium.NETHER_PACT_MODE, data);
                }
            });
        });
    }


    public static void sendNetherModeUpdate(PlayerEntity player) {

        MinecraftServer server = player.getServer();

        PacketByteBuf data = PacketByteBufs.create();
        data.writeUuid(player.getUuid());
        data.writeBoolean(true);

        server.execute(() -> {
            server.getPlayerManager().getPlayerList().forEach((p)->{
                ServerPlayNetworking.send(p, Infernum_effugium.NETHER_PACT_MODE, data);
            });
        });
    }
}
