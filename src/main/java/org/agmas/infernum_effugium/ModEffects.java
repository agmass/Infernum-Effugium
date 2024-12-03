package org.agmas.infernum_effugium;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.agmas.infernum_effugium.status_effects.ExtremeFireStatusEffect;
import org.agmas.infernum_effugium.status_effects.NetherPactStatusEffect;

public class ModEffects {
    public static final RegistryEntry<StatusEffect> EXTREME_FIRE;
    public static final RegistryEntry<StatusEffect> NETHER_PACT;

    static {
        NETHER_PACT = Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Infernum_effugium.MOD_ID, "nether_pact"), new NetherPactStatusEffect());
        EXTREME_FIRE = Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Infernum_effugium.MOD_ID, "extreme_fire"), new ExtremeFireStatusEffect());
    }

    public static void init() {}
}
