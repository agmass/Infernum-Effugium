package org.agmas.infernum_effugium.client.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

@Mixin(InGameHud.class)
public abstract class ExtremeFireUIMixin {
    @Shadow private int scaledWidth;

    @Shadow private int scaledHeight;

    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow protected abstract int getHeartCount(LivingEntity entity);

    @Shadow protected abstract LivingEntity getRiddenEntity();

    @Shadow @Final private static Identifier ICONS;
    @Unique
    private static final Identifier FIRE_VIGNETTE_TEXTURE = new Identifier("infernumeffugium", "textures/misc/extreme_fire_vignette.png");

    @Inject(method = "renderVignetteOverlay", at = @At("HEAD"), cancellable = true)
    public void shellVignette(DrawContext context, Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity le) {
            if (le.hasStatusEffect(Infernum_effugium.EXTREME_FIRE)) {
                context.drawTexture(FIRE_VIGNETTE_TEXTURE, 0, 0, -90, 0.0F, 0.0F, scaledWidth, scaledHeight, scaledWidth, scaledHeight);
                ci.cancel();
            }
        }
    }

}
