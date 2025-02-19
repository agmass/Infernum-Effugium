package org.agmas.infernum_effugium.status_effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;

import java.awt.*;

public class AirborneStatusEffect extends StatusEffect {
    public AirborneStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, Color.getHSBColor(209, 48, 95).getRGB());
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.isOnGround()) {
            entity.removeStatusEffectInternal(this);
        }
        super.applyUpdateEffect(entity, amplifier);
    }
}
