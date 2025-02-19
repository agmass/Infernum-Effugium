package org.agmas.infernum_effugium.mixin;

import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class WalkOnLavaMixin {

    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow public abstract boolean hasNoDrag();

    @Shadow public abstract float getYaw(float tickDelta);

    @Shadow public abstract float getBodyYaw();

    @Shadow public abstract float getHeadYaw();

    @Unique
    public boolean isTouchingLava = false;

    @Inject(method = "canWalkOnFluid", at = @At("HEAD"), cancellable = true)
    public void waterFireOne(FluidState state, CallbackInfoReturnable<Boolean> cir) {
        if (hasStatusEffect(Infernum_effugium.NETHER_PACT) && state.isIn(FluidTags.LAVA)) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
    @Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setVelocity(DDD)V", ordinal = 3, shift = At.Shift.AFTER))
    public void airborne(Vec3d movementInput, CallbackInfo ci) {
        if (hasStatusEffect(Infernum_effugium.AIRBORNE) && !((Entity)(Object)this).isOnGround()) {
            ((Entity) (Object) this).setVelocity(((Entity) (Object) this).getVelocity().add(airMovement(movementInput,getHeadYaw()).multiply(0.05)));
        }
    }

    @Unique
    public Vec3d airMovement(Vec3d movementInput, float yaw) {
        double d = movementInput.lengthSquared();
        if (d < 1.0E-7) {
            return Vec3d.ZERO;
        } else {
            Vec3d vec3d = (d > (double)1.0F ? movementInput.normalize() : movementInput);
            float f = MathHelper.sin(yaw * ((float)Math.PI / 180F));
            float g = MathHelper.cos(yaw * ((float)Math.PI / 180F));
            return new Vec3d(vec3d.x * (double)g - vec3d.z * (double)f, vec3d.y, vec3d.z * (double)g + vec3d.x * (double)f);
        }
    }

}
