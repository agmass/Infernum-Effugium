package org.agmas.infernum_effugium.client.mixin;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.LivingEntity;
import org.agmas.infernum_effugium.client.Infernum_effugiumClient;
import org.agmas.infernum_effugium.client.LivingEntityRenderAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderState.class)
public abstract class LivingEntityRenderStateMixin implements LivingEntityRenderAccess {
    @Unique
    public boolean redSkin;

    @Unique
    public void infernumEffugium$setRedSkin(boolean redSkin) {
        this.redSkin = redSkin;
    }

    @Override
    public boolean infernumEffugium$getRedSkin() {
        return this.redSkin;
    }
}
