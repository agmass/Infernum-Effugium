package org.agmas.infernum_effugium.mixin;

import eu.pb4.polymer.core.impl.networking.PacketPatcher;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtInt;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModEntities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.nucleoid.packettweaker.PacketContext;

@Mixin(PacketPatcher.class)
public class PolymerBlockEntityFixMixin {

    @Inject(method = "prevent", at = @At(value = "HEAD"), cancellable = true)
    private static void injected2(ServerCommonNetworkHandler handler, Packet<?> packet, CallbackInfoReturnable<Boolean> cir) {
        if (packet instanceof BlockEntityUpdateS2CPacket be) {
            if (handler.getClass() == ServerPlayNetworkHandler.class && be.getBlockEntityType().equals(ModEntities.GREED_VAULT)) {
                var player = PacketContext.of(handler);
                if (PolymerServerNetworking.getMetadata(player.getPlayer().networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) == NbtInt.of(1)) {
                    cir.setReturnValue(false);
                    cir.cancel();
                } else {
                    cir.setReturnValue(true);
                    cir.cancel();
                }
            }
        }
    }
}
