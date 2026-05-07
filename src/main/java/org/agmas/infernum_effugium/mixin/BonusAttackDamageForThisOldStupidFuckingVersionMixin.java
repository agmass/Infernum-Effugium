package org.agmas.infernum_effugium.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
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

    @ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private float infernumeffugium$noDamage(float amount, @Local DamageSource damageSource, @Local(argsOnly = true) Entity target) {
        ItemStack itemStack = me().getStackInHand(Hand.MAIN_HAND);
        ItemStack itemStack2 = me().getStackInHand(Hand.OFF_HAND);
        if (!itemStack.getItem().equals(itemStack2.getItem()) && (itemStack.getItem() instanceof BedrockSickle)) {
            return 0;
        }
        if (itemStack.getItem() instanceof BedrockSickle) {
            float targetDistance =(float) me().getPos().multiply(1,0,1).distanceTo(target.getPos().multiply(1,0,1));
            if (targetDistance <= 1) {
                return amount;
            }
            if (targetDistance >= 1) {
                if (targetDistance < 3.25) {
                    return (float) MathHelper.lerp((targetDistance - 1) / 2.25, amount, amount * 0.777777778);
                } else {
                    return (float) MathHelper.lerp(Math.clamp((targetDistance - 3.25) / 1.5, 0 ,1), amount * 0.777777778, amount * 0.35);
                }
            }
        }
        if (me().blockedByShield(damageSource)) {
            return 0f;
        }
        return amount;
    }
    @ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private float infernumeffugium$noSweepDamage(float amount, @Local DamageSource damageSource, @Local(argsOnly = true) Entity target) {
        ItemStack itemStack = me().getStackInHand(Hand.MAIN_HAND);
        ItemStack itemStack2 = me().getStackInHand(Hand.OFF_HAND);
        if (!itemStack.getItem().equals(itemStack2.getItem()) && (itemStack.getItem() instanceof BedrockSickle)) {
            return 0;
        }
        if (itemStack.getItem() instanceof BedrockSickle) {
            float targetDistance =(float) me().getPos().multiply(1,0,1).distanceTo(target.getPos().multiply(1,0,1));
            if (targetDistance <= 1) {
                return amount;
            }
            if (targetDistance >= 1) {
                if (targetDistance < 2.5) {
                    return (float) MathHelper.lerp((targetDistance - 1) / 1.5, amount, amount * 0.777777778);
                } else {
                    return (float) MathHelper.lerp((targetDistance - 1) / 1.5, amount * 0.777777778, amount * 0.1);
                }
            }
        }
        if (me().blockedByShield(damageSource)) {
            return 0f;
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
