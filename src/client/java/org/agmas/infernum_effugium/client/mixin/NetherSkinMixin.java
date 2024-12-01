package org.agmas.infernum_effugium.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntityRenderer.class)
public abstract class NetherSkinMixin {

    @ModifyArg(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"), index = 5)
    public float visibilityMixin3(float par5, @Local(argsOnly = true) LivingEntity livingEntity) {

        if (livingEntity.hasStatusEffect(Infernum_effugium.NETHER_PACT)) {
            return 0.65f;
        }
        return par5;
    }
    @ModifyArg(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"), index = 6)
    public float visibilityMixin2(float par5, @Local(argsOnly = true) LivingEntity livingEntity) {

        if (livingEntity.hasStatusEffect(Infernum_effugium.NETHER_PACT)) {
            return 0.65f;
        }
        return par5;
    }
}
