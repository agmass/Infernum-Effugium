package org.agmas.infernum_effugium.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.agmas.infernum_effugium.client.Infernum_effugiumClient;
import org.agmas.infernum_effugium.client.LivingEntityRenderAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntityRenderer.class)
public abstract class NetherSkinMixin {


    @ModifyArg(method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"), index = 4)
    public int visibilityMixin3(int par3, @Local(argsOnly = true)LivingEntityRenderState renderState) {
        if (renderState instanceof PlayerEntityRenderState) {
            LivingEntityRenderAccess renderAccess = ((LivingEntityRenderAccess) (Object)renderState);
            if (renderAccess.infernumEffugium$getRedSkin()) {
                return Infernum_effugiumClient.netherSkinColor;
            }
        }
        return par3;
    }
}
