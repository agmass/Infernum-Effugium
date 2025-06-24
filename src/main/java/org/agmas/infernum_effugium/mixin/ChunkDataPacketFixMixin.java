package org.agmas.infernum_effugium.mixin;

import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.nbt.NbtInt;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ChunkData;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.light.LightingProvider;
import org.agmas.infernum_effugium.ChunkDataAccess;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModEntities;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.BitSet;

@Mixin(ServerCommonNetworkHandler.class)
public class ChunkDataPacketFixMixin {

    @Inject(method = "send", at = @At("HEAD"))
    private void skipPolymerEntriesForBedrock(Packet<?> packet, ChannelFutureListener channelFutureListener, CallbackInfo ci)  {
        PacketContext context = PacketContext.create((ServerCommonNetworkHandler) (Object) this);
        if (context.getPlayer() != null) {
            if (packet instanceof ChunkDataS2CPacket chunkDataS2CPacket) {
                if (PolymerServerNetworking.getMetadata(context.getPlayer().networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) != NbtInt.of(1)) {
                    ((ChunkDataAccess) chunkDataS2CPacket.getChunkData()).infernumeffugium$getBlockEntities().removeIf((blockEntityData -> ((BlockEntityDataAccessor) blockEntityData).getType().equals(ModEntities.GREED_VAULT)));
                    // Fucking kill me
                }
            }
        }
    }
}
