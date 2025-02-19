package org.agmas.infernum_effugium;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.agmas.infernum_effugium.enchantments.AirblastEnchantment;
import org.agmas.infernum_effugium.enchantments.BackburnerEnchantment;
import org.agmas.infernum_effugium.enchantments.FlamethrowerEnchantment;
import org.agmas.infernum_effugium.enchantments.ShotgunEnchantment;

public class ModEnchantments {
    public static Enchantment FLAMETHROWER = new FlamethrowerEnchantment();
    public static Enchantment BACKBURNER = new BackburnerEnchantment();
    public static Enchantment SHOTGUN = new ShotgunEnchantment();
    public static Enchantment ENDER = new ShotgunEnchantment();
    public static Enchantment AIRBLAST = new AirblastEnchantment();

    public static void init() {
        Registry.register(Registries.ENCHANTMENT, new Identifier(Infernum_effugium.MOD_ID, "flamethrower"), FLAMETHROWER);
        Registry.register(Registries.ENCHANTMENT, new Identifier(Infernum_effugium.MOD_ID, "backburner"), BACKBURNER);
        Registry.register(Registries.ENCHANTMENT, new Identifier(Infernum_effugium.MOD_ID, "shotgun"), SHOTGUN);
        Registry.register(Registries.ENCHANTMENT, new Identifier(Infernum_effugium.MOD_ID, "ender"), ENDER);
        Registry.register(Registries.ENCHANTMENT, new Identifier(Infernum_effugium.MOD_ID, "airblast"), AIRBLAST);
    }
}
