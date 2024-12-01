package org.agmas.infernum_effugium.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class WalkOnLavaMixin {

    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Unique
    public boolean isTouchingLava = false;

    @Inject(method = "canWalkOnFluid", at = @At("HEAD"), cancellable = true)
    public void waterFireOne(FluidState state, CallbackInfoReturnable<Boolean> cir) {
        if (hasStatusEffect(Infernum_effugium.NETHER_PACT) && state.isIn(FluidTags.LAVA)) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
