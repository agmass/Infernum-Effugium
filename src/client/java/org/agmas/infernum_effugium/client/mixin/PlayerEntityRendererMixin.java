package org.agmas.infernum_effugium.client.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.agmas.infernum_effugium.client.Infernum_effugiumClient;
import org.agmas.infernum_effugium.client.LivingEntityRenderAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {
    @Inject(method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V", at = @At("HEAD"))
    public void visibilityMixin3(AbstractClientPlayerEntity abstractClientPlayerEntity, PlayerEntityRenderState playerEntityRenderState, float f, CallbackInfo ci) {
        ((LivingEntityRenderAccess)playerEntityRenderState).infernumEffugium$setRedSkin(Infernum_effugiumClient.pactPlayers.contains(abstractClientPlayerEntity.getUuid()));
    }
}
