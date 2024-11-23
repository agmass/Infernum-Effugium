package org.agmas.infernum_effugium.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.agmas.infernum_effugium.item.BedrockSickle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerEntity.class)
public class BonusAttackDamageForThisOldStupidFuckingVersionMixin {
    @ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), index = 1)
    private float injected(float amount) {
        ItemStack itemStack = me().getStackInHand(Hand.MAIN_HAND);
        ItemStack itemStack2 = me().getStackInHand(Hand.OFF_HAND);
        if (itemStack.getItem() instanceof BedrockSickle && itemStack2.getItem() instanceof BedrockSickle) {
            return 7.5f;
        }
        return amount;
    }

    PlayerEntity me() {
        return (PlayerEntity) (Object) this;
    }
}
