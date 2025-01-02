package org.agmas.infernum_effugium.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.item.BedrockSickle;
import org.agmas.infernum_effugium.state.StateSaverAndLoader;
import org.agmas.infernum_effugium.util.NetherPactUpdates;
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

    @ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), index = 1)
    private float injected(float amount) {
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
                target.timeUntilRegen = 0;
            }
        }
    }

    @Unique
    PlayerEntity me() {
        return (PlayerEntity) (Object) this;
    }
}
