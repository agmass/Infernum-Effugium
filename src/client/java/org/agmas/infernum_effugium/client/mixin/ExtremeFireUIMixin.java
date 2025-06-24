package org.agmas.infernum_effugium.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModEffects;
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
    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow protected abstract int getHeartCount(LivingEntity entity);

    @Shadow protected abstract LivingEntity getRiddenEntity();

    @Shadow @Final private MinecraftClient client;

    @Shadow protected abstract void renderOverlay(DrawContext context, Identifier texture, float opacity);

    @Unique
    private static final Identifier FIRE_VIGNETTE_TEXTURE = Identifier.of("infernumeffugium", "textures/misc/extreme_fire_vignette.png");

    @Inject(method = "renderMiscOverlays", at = @At("HEAD"), cancellable = true)
    public void shellVignette(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (client.getCameraEntity() instanceof LivingEntity le) {
            if (le.hasStatusEffect(ModEffects.EXTREME_FIRE)) {
                renderOverlay(context,FIRE_VIGNETTE_TEXTURE,1.0f);
                ci.cancel();
            }
        }
    }

}