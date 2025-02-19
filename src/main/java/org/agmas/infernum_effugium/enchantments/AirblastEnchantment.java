package org.agmas.infernum_effugium.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.agmas.infernum_effugium.ModItems;

public class AirblastEnchantment extends Enchantment implements InfernumEnchant {
    public AirblastEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.VANISHABLE, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return !(other instanceof EnderEnchantment) &&!(other instanceof FlamethrowerEnchantment) &&!(other instanceof ShotgunEnchantment) &&!(other instanceof BackburnerEnchantment) && super.canAccept(other);
    }


    @Override
    public int getMinPower(int level) {
        return 0;
    }

    @Override
    public int getMaxPower(int level) {
        return 20;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.isOf(ModItems.PEBBLE_CANNON);
    }
}