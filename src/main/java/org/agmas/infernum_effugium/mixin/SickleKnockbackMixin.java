package org.agmas.infernum_effugium.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.agmas.infernum_effugium.item.BedrockSickle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class SickleKnockbackMixin {
    @Shadow public abstract ItemStack getStackInHand(Hand hand);

    @ModifyVariable(method = "swingHand(Lnet/minecraft/util/Hand;)V", at = @At(value = "HEAD"), argsOnly = true)
    private Hand injected2(Hand hand) {
        ItemStack itemStack = getStackInHand(Hand.MAIN_HAND);
        ItemStack itemStack2 = getStackInHand(Hand.OFF_HAND);
        if (itemStack.getItem() instanceof BedrockSickle && itemStack2.getItem() instanceof BedrockSickle) {
           return new Random().nextInt(0,100) < 50 ? Hand.MAIN_HAND : Hand.OFF_HAND;
        }
        return hand;
    }
    @Redirect(method = "takeKnockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isOnGround()Z"))
    private boolean injected(LivingEntity instance) {
        if (instance.getAttacker() != null) {
            if (instance.getAttacker().getStackInHand(Hand.MAIN_HAND).getItem() instanceof BedrockSickle) {
                return true;
            }
        }
        return instance.isOnGround();
    }

}
