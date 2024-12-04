package org.agmas.infernum_effugium.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.item.BedrockSickle;
import org.agmas.infernum_effugium.state.StateSaverAndLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class BonusAttackDamageForThisOldStupidFuckingVersionMixin {
    @Inject(method = "onDeath", at = @At("HEAD"))
    private void death(DamageSource damageSource, CallbackInfo ci) {
        if (damageSource.equals(me().getDamageSources().freeze())) {
            if (me().hasStatusEffect(Infernum_effugium.NETHER_PACT)) {
                StateSaverAndLoader.getPlayerState(me()).netherPacted = false;
            }
        }
    }
    @ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), index = 1)
    private float injected(float amount) {
        ItemStack itemStack = me().getStackInHand(Hand.MAIN_HAND);
        ItemStack itemStack2 = me().getStackInHand(Hand.OFF_HAND);
        if (!itemStack.getItem().equals(itemStack2.getItem()) && (itemStack.getItem() instanceof BedrockSickle)) {
            return 0;
        }
        return amount;
    }

    PlayerEntity me() {
        return (PlayerEntity) (Object) this;
    }
}
