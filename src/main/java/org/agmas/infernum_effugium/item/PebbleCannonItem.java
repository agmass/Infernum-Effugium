package org.agmas.infernum_effugium.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Vanishable;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModEnchantments;
import org.agmas.infernum_effugium.ModItems;
import org.agmas.infernum_effugium.entity.PebbleEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class PebbleCannonItem extends Item implements Vanishable {
    public PebbleCannonItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity user) {
            if (user.getActiveItem().equals(stack) && !user.getItemCooldownManager().isCoolingDown(this)) {
                boolean bl = user.getAbilities().creativeMode;

                ItemStack pebble = null;
                int pebbleAmount = 64;
                for (int i = 0; i < user.getInventory().size(); i++) {
                    ItemStack itemStack2 = user.getInventory().getStack(i);
                    if (itemStack2.getItem() instanceof PebbleItem) {
                        pebble = itemStack2;
                        pebbleAmount = itemStack2.getCount();
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
                        boolean immuneToJamming = false;
                        int usedPebbles = 1;
                        boolean firstPebble = true;
                        user.getItemCooldownManager().set(this,2);
                        if (stack.hasEnchantments()) {
                            if (EnchantmentHelper.getLevel(ModEnchantments.SHOTGUN, stack) != 0) {
                                immuneToJamming = true;
                                usedPebbles = Math.min(8, pebbleAmount);
                                user.getItemCooldownManager().set(this,10);
                            }
                            if (EnchantmentHelper.getLevel(ModEnchantments.ENDER, stack) != 0) {
                                user.getItemCooldownManager().set(this,70);

                            }
                        }
                        for (int i = 0; i < usedPebbles; i++) {
                            PebbleEntity pebbleEntity = new PebbleEntity(world, user);
                            pebbleEntity.setItem(pebble);
                            pebbleEntity.shotFromCannon = true;
                            pebbleEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 0.0F);
                            if (stack.hasEnchantments()) {
                                if (EnchantmentHelper.getLevel(ModEnchantments.FLAMETHROWER, stack) != 0) {
                                    pebbleEntity.setItem(ModItems.MAGMA_PEBBLE.getDefaultStack());
                                    world.playSound(
                                            null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 0.5F, 0.9F + (world.getRandom().nextFloat() * 0.2f)
                                    );
                                }
                                if (EnchantmentHelper.getLevel(ModEnchantments.BACKBURNER, stack) != 0) {
                                    pebbleEntity.shotFromBackburner = true;
                                    pebbleEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 2.5F, 0.0F);
                                }
                            }
                            if (!firstPebble) {
                                pebbleEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.15F, 1.5F, 0.9F);
                            }

                            if (stack.hasEnchantments()) {
                                if (EnchantmentHelper.getLevel(ModEnchantments.ENDER, stack) != 0) {
                                    pebbleEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 4F, 0.0F);
                                    pebbleEntity.setNoGravity(true);
                                }
                            }
                            world.spawnEntity(pebbleEntity);
                            pebbleEntity.setYaw(user.getYaw() + 90);
                            firstPebble = false;
                        }
                        stack.damage(1,user,(e)->{
                            e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
                        });
                        pebble.decrement(usedPebbles);
                        if (new Random().nextInt(0,500) == 0 && !immuneToJamming) {
                            if (stack.hasEnchantments()) {
                                if (EnchantmentHelper.getLevel(ModEnchantments.FLAMETHROWER, stack) != 0) {
                                    user.setFireTicks(user.getFireTicks()+10);
                                }
                            }
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
    public int getEnchantability() {
        return 5;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (itemStack.hasEnchantments()) {
            if (EnchantmentHelper.getLevel(ModEnchantments.AIRBLAST, itemStack) != 0) {
                if (!user.hasStatusEffect(Infernum_effugium.AIRBORNE)) {
                    user.getItemCooldownManager().set(this, 25);
                    user.getWorld().getOtherEntities(user, new Box(user.getEyePos().add(user.getRotationVec(0f).multiply(4)).add(-4, -4, -4), user.getEyePos().add(user.getRotationVec(0f).multiply(4)).add(4, 4, 4))).forEach((e) -> {
                        e.setVelocity(e.getPos().add(user.getPos().multiply(-1)).add(0, 1, 0).multiply(0.5));
                    });
                    user.addStatusEffect(new StatusEffectInstance(Infernum_effugium.AIRBORNE, 20*30,0 ));
                    user.setVelocity(user.getRotationVec(0f).multiply(-2f));
                }
                return TypedActionResult.success(itemStack);
            }
        }

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
