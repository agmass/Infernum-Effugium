package org.agmas.infernum_effugium.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModEffects;
import org.agmas.infernum_effugium.item.BedrockSickle;
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
                renderOverlay(context, FIRE_VIGNETTE_TEXTURE, 1f);
                ci.cancel();
            }
        }
    }
    @Inject(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;getScaledWindowWidth()I", ordinal = 2))
    public void infernum$colorCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (client.getCameraEntity() instanceof ClientPlayerEntity le) {
            if (le.getMainHandStack().getItem() instanceof BedrockSickle) {
                if (client.targetedEntity != null) {
                    float distance = le.distanceTo(client.targetedEntity);
                    float value = 1;
                    if (distance <= 1) value = 0;
                    if (distance > 1) {
                        if (distance < 3.25) {
                            value = (float) MathHelper.lerp((distance - 1) / 2.25, 0f, 0.777777f);
                        } else {
                            value = (float) MathHelper.lerp((distance - 3.25) / 1.5, 0.777777f, 1f);
                        }
                    }
                    value = Math.clamp(value,0,1);
                    context.setShaderColor(MathHelper.lerp(value, 0f, 1f), MathHelper.lerp(value, 1f, 0f), 0f, 1f);
                } else {
                    context.setShaderColor(1f,0f,0f,1f);
                }
            }
        }
    }
    @Inject(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;defaultBlendFunc()V"))
    public void infernum$reset(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        context.setShaderColor(1f,1f,1f,1f);
    }

}