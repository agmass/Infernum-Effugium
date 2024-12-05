package org.agmas.infernum_effugium.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModItems;
import org.agmas.infernum_effugium.item.BedrockSickle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class SickleKnockbackMixin {
    @Shadow public abstract ItemStack getStackInHand(Hand hand);

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @ModifyVariable(method = "swingHand(Lnet/minecraft/util/Hand;)V", at = @At(value = "HEAD"), argsOnly = true)
    private Hand injected2(Hand hand) {
        ItemStack itemStack = getStackInHand(Hand.MAIN_HAND);
        ItemStack itemStack2 = getStackInHand(Hand.OFF_HAND);
        if (itemStack.getItem() instanceof BedrockSickle && itemStack2.getItem() instanceof BedrockSickle) {
           return new Random().nextInt(0,100) < 50 ? Hand.MAIN_HAND : Hand.OFF_HAND;
        }
        return hand;
    }
    @Redirect(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z"))
    private boolean injected2(LivingEntity instance, StatusEffect effect) {
        return instance.hasStatusEffect(StatusEffects.FIRE_RESISTANCE) || instance.hasStatusEffect(Infernum_effugium.NETHER_PACT);
    }

    @Inject(method = "onDeath", at= @At(value = "HEAD"))
    public void catWhistle(DamageSource damageSource, CallbackInfo ci) {
        if (new Random().nextInt(0,10) == 0) {
            if (damageSource.getAttacker() instanceof PlayerEntity pe) {
                if (pe.getStackInHand(Hand.MAIN_HAND).getItem() instanceof BedrockSickle) {
                    if (me() instanceof CatEntity) {
                        me().getWorld().spawnEntity(new ItemEntity(me().getWorld(), me().getX(), me().getY(), me().getZ(), ModItems.DEATH_WHISTLE.getDefaultStack(), 0, 0, 0));
                    }
                }
            }
        }
    }
    @Inject(method = "damage", at= @At(value = "INVOKE", target = "Lnet/minecraft/entity/LimbAnimator;setSpeed(F)V"))
    public void fireaspect(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getAttacker() instanceof LivingEntity le) {
            if (le.hasStatusEffect(Infernum_effugium.NETHER_PACT)) {
                me().setFireTicks(20*4);
            }
        }
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

    @Unique
    LivingEntity me() {
        return (LivingEntity) (Object) this;
    }
}
