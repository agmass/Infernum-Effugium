package org.agmas.infernum_effugium;


import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEnchants {
    public static final RegistryKey<Enchantment> FLAMETHROWER = of("flamethrower");
    public static final RegistryKey<Enchantment> BACKBURNER = of("backburner");
    public static final RegistryKey<Enchantment> ENDER = of("ender");
    public static final RegistryKey<Enchantment> SHOTGUN = of("shotgun");
    public static final RegistryKey<Enchantment> AIRBLAST = of("airblast");
    private static RegistryKey<Enchantment> of(String name) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of("infernumeffugium", name));
    }

    public static void initialize() {

    }

}
