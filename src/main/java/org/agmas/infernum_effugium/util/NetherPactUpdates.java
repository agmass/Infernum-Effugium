package org.agmas.infernum_effugium.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModEffects;

import java.util.List;
import java.util.UUID;

public class NetherPactUpdates {


    public static void sendHumanModeUpdate(PlayerEntity player) {

        MinecraftServer server = player.getServer();
        if (server != null) {
            server.execute(() -> {
                server.getPlayerManager().getPlayerList().forEach((p) -> {
                    ServerPlayNetworking.send(p, new NetherPactModePayload(player.getUuid(), false));
                });
            });
        }
    }

    public static void refreskSkinsOnClient(ServerPlayerEntity player) {

        MinecraftServer server = player.getServer();

        if (server != null) {
            server.execute(() -> {
                server.getPlayerManager().getPlayerList().forEach((p) -> {
                    if (p.hasStatusEffect(ModEffects.NETHER_PACT)) {
                        ServerPlayNetworking.send(player, new NetherPactModePayload(p.getUuid(), true));
                    }
                });
            });
        }
    }


    public static void sendNetherModeUpdate(PlayerEntity player) {
        MinecraftServer server = player.getServer();

        if (server != null) {
            server.execute(() -> {
                server.getPlayerManager().getPlayerList().forEach((p) -> {
                    ServerPlayNetworking.send(p, new NetherPactModePayload(player.getUuid(), true));
                });
            });
        }
    }

    public record NetherPactModePayload(UUID playerToAdd, boolean nethered) implements CustomPayload {
        public static final Identifier NETHER_PACT_PAYLOAD = Identifier.of(Infernum_effugium.MOD_ID, "nether_pact_mode");
        public static final Id<NetherPactModePayload> ID = new Id<>(NETHER_PACT_PAYLOAD);
        public static final PacketCodec<RegistryByteBuf, NetherPactModePayload> CODEC;

        public NetherPactModePayload(UUID playerToAdd, boolean nethered) {
            this.playerToAdd = playerToAdd;
            this.nethered = nethered;
        }

        public CustomPayload.Id<? extends CustomPayload> getId() {
            return ID;
        }

        public void write(PacketByteBuf buf) {
            buf.writeUuid(this.playerToAdd);
            buf.writeBoolean(this.nethered);
        }

        public static NetherPactModePayload read(PacketByteBuf buf) {
            return new NetherPactModePayload(buf.readUuid(), buf.readBoolean());
        }

        public boolean nethered() {
            return this.nethered;
        }

        public UUID playerToAdd() {
            return this.playerToAdd;
        }


        static {
            CODEC = PacketCodec.of(NetherPactModePayload::write, NetherPactModePayload::read);
        }
    }

}
