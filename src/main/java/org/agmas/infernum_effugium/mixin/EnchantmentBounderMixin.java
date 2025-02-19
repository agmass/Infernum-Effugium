package org.agmas.infernum_effugium.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.agmas.infernum_effugium.ModItems;
import org.agmas.infernum_effugium.enchantments.InfernumEnchant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnchantmentHelper.class)
public class EnchantmentBounderMixin {
    @Inject(method = "getPossibleEntries", at=@At("RETURN"))
    private static void onlyInfernumOnPebbleCannons(int power, ItemStack stack, boolean treasureAllowed, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
        if (!stack.isOf(Items.BOOK) && !stack.isOf(ModItems.PEBBLE_CANNON)) {
            List<EnchantmentLevelEntry> enchants = cir.getReturnValue();
            enchants.removeIf((e)->{
                return e.enchantment instanceof InfernumEnchant;
            });
        }
    }
}
