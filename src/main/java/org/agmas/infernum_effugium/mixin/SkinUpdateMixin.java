package org.agmas.infernum_effugium.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.agmas.infernum_effugium.util.NetherPactUpdates;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class SkinUpdateMixin {
    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    public void sendShellUpdate(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        NetherPactUpdates.refreskSkinsOnClient(player);
    }
}
