package org.agmas.infernum_effugium.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.agmas.infernum_effugium.item.BedrockSickle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class BonusAttackDamageForThisOldStupidFuckingVersionMixin {
    @Shadow public abstract float getAttackCooldownProgress(float baseTime);

    @ModifyArg(method = "attack ", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;sidedDamage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), index = 1)
    private float injected(float amount, @Local(argsOnly = true) Entity target) {
        ItemStack itemStack = me().getStackInHand(Hand.MAIN_HAND);
        ItemStack itemStack2 = me().getStackInHand(Hand.OFF_HAND);
        if (!itemStack.getItem().equals(itemStack2.getItem()) && (itemStack.getItem() instanceof BedrockSickle)) {
            return 0;
        }
        return amount;
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttackCooldownProgress(F)F", shift = At.Shift.AFTER))
    private void injected(Entity target, CallbackInfo ci) {
        ItemStack itemStack = me().getStackInHand(Hand.MAIN_HAND);
        ItemStack itemStack2 = me().getStackInHand(Hand.OFF_HAND);
        if (getAttackCooldownProgress(0.5F) == 1.0F) {
            if (itemStack.getItem().equals(itemStack2.getItem()) && (itemStack.getItem() instanceof BedrockSickle)) {
                target.timeUntilRegen = target.timeUntilRegen / 2;
            }
        }
    }
    @Unique
    PlayerEntity me() {
        return (PlayerEntity) (Object) this;
    }
}
