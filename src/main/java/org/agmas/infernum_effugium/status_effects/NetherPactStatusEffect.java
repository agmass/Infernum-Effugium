package org.agmas.infernum_effugium.status_effects;

import eu.pb4.polymer.core.api.other.PolymerStat;
import eu.pb4.polymer.core.api.other.PolymerStatusEffect;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtInt;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.util.NetherPactUpdates;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class NetherPactStatusEffect extends StatusEffect implements PolymerStatusEffect {
    public NetherPactStatusEffect() {
        // category: StatusEffectCategory - describes if the effect is helpful (BENEFICIAL), harmful (HARMFUL) or useless (NEUTRAL)
        // color: int - Color is the color assigned to the effect (in RGB)
        super(StatusEffectCategory.NEUTRAL, 0xe9b8b3);
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
    public void onEntityRemoval(LivingEntity entity, int amplifier, Entity.RemovalReason reason) {
        if (entity instanceof PlayerEntity p) {
            NetherPactUpdates.sendHumanModeUpdate(p);
        }
        super.onEntityRemoval(entity, amplifier, reason);
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity p) {
            NetherPactUpdates.sendNetherModeUpdate(p);
        }
        super.onApplied(entity, amplifier);
    }
}