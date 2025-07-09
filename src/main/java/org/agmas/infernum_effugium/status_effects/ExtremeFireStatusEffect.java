package org.agmas.infernum_effugium.status_effects;

import eu.pb4.polymer.core.api.other.PolymerStatusEffect;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtInt;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class ExtremeFireStatusEffect extends StatusEffect implements PolymerStatusEffect {
    public ExtremeFireStatusEffect() {
        // category: StatusEffectCategory - describes if the effect is helpful (BENEFICIAL), harmful (HARMFUL) or useless (NEUTRAL)
        // color: int - Color is the color assigned to the effect (in RGB)
        super(StatusEffectCategory.HARMFUL, 0xe9b8b3);
    }

    // Called every tick to check if the effect can be applied or not
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        // In our case, we just make it return true so that it applies the effect every tick
        return true;
    }

    @Override
    public @Nullable StatusEffect getPolymerReplacement(ServerPlayerEntity player) {
        if (player == null) return PolymerStatusEffect.super.getPolymerReplacement(player);
        if (PolymerServerNetworking.getMetadata(player.networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) != null) {
            return this;
        } else {
            return PolymerStatusEffect.super.getPolymerReplacement(player);
        }
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        entity.timeUntilRegen = 0;
        entity.removeStatusEffect(StatusEffects.FIRE_RESISTANCE);
        entity.damage(entity.getDamageSources().magic(), 1);
        entity.setFireTicks(5);
        return true;
    }

}