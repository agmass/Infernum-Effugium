package org.agmas.infernum_effugium.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.ModItems;
import org.agmas.infernum_effugium.entity.PebbleEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class PebbleCannonItem extends Item {
    public PebbleCannonItem(Settings settings) {
        super(settings);
    }


    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity user) {
            if (user.getActiveItem().equals(stack)) {
                boolean bl = user.getAbilities().creativeMode;

                ItemStack pebble = null;
                for (int i = 0; i < user.getInventory().size(); i++) {
                    ItemStack itemStack2 = user.getInventory().getStack(i);
                    if (itemStack2.getItem() instanceof PebbleItem) {
                        pebble = itemStack2;
                    }
                }
                if (pebble == null && bl) {
                    pebble = ModItems.BLACKSTONE_PEBBLE.getDefaultStack();
                }
                if (pebble != null || bl) {
                    world.playSound(
                            null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
                    );
                    if (!world.isClient) {
                        PebbleEntity pebbleEntity = new PebbleEntity(world, user);
                        pebbleEntity.setItem(pebble);
                        pebbleEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 0.0F);
                        world.spawnEntity(pebbleEntity);
                        stack.damage(1,user,(e)->{
                            e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
                        });
                        pebble.decrement(1);
                        if (new Random().nextInt(0,500) == 0) {
                            user.getItemCooldownManager().set(stack.getItem(), 20*12);
                            user.stopUsingItem();
                        }
                    }

                    user.incrementStat(Stats.USED.getOrCreateStat(this));
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.of("Uses blackstone pebbles as a projectile."));
        tooltip.add(Text.of("Small chance to get jammed on use."));
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        boolean bl = user.getAbilities().creativeMode;

        ItemStack pebble = null;
        if (!bl) {
            for (int i = 0; i < user.getInventory().size(); i++) {
                ItemStack itemStack2 = user.getInventory().getStack(i);
                if (itemStack2.getItem() instanceof PebbleItem) {
                    pebble = itemStack2;
                }
            }
        }
        if (pebble != null || bl) {
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }
}
