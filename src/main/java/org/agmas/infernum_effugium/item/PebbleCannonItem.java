package org.agmas.infernum_effugium.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtInt;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModEffects;
import org.agmas.infernum_effugium.ModEnchants;
import org.agmas.infernum_effugium.ModItems;
import org.agmas.infernum_effugium.entity.PebbleEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class PebbleCannonItem extends Item{

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
                            null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 0.5F, 0.9F + (world.getRandom().nextFloat() * 0.2f)
                    );
                    if (!world.isClient) {
                        int usedPebbles = 1;
                        boolean firstPebble = true;
                        user.getItemCooldownManager().set(this,3);
                        Registry<Enchantment> enchantRegistry = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT);

                        if (stack.hasEnchantments()) {
                            if (EnchantmentHelper.getLevel(enchantRegistry.getEntry(enchantRegistry.get(ModEnchants.SHOTGUN)), stack) != 0) {
                                usedPebbles = Math.min(8, pebbleAmount);
                                user.getItemCooldownManager().set(this,15);
                            }
                            if (EnchantmentHelper.getLevel(enchantRegistry.getEntry(enchantRegistry.get(ModEnchants.ENDER)), stack) != 0) {
                                user.getItemCooldownManager().set(this,70);
                                if (EnchantmentHelper.getLevel(enchantRegistry.getEntry(enchantRegistry.get(ModEnchants.SHOTGUN)), stack) != 0) {
                                    user.getItemCooldownManager().set(this,120);
                                }
                            }
                        }
                        for (int i = 0; i < usedPebbles; i++) {
                            PebbleEntity pebbleEntity = new PebbleEntity(world, user);
                            pebbleEntity.setItem(pebble);
                            pebbleEntity.shotFromCannon = true;
                            pebbleEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 0.0F);
                            pebbleEntity.setPosition(user.getEyePos().add(user.getRotationVector()));
                            if (stack.hasEnchantments()) {
                                if (EnchantmentHelper.getLevel(enchantRegistry.getEntry(enchantRegistry.get(ModEnchants.FLAMETHROWER)), stack) != 0) {
                                    pebbleEntity.setItem(ModItems.MAGMA_PEBBLE.getDefaultStack());
                                }
                                if (EnchantmentHelper.getLevel(enchantRegistry.getEntry(enchantRegistry.get(ModEnchants.BACKBURNER)), stack) != 0) {
                                    pebbleEntity.shotFromBackburner = true;
                                    pebbleEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 2F, 0.0F);
                                }
                            }
                            if (!firstPebble) {
                                pebbleEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.15F, 1.5F, 0.9F);
                            }

                            if (stack.hasEnchantments()) {
                                if (EnchantmentHelper.getLevel(enchantRegistry.getEntry(enchantRegistry.get(ModEnchants.ENDER)), stack) != 0) {
                                    pebbleEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 4F, 0.0F);
                                    pebbleEntity.setNoGravity(true);
                                    if (EnchantmentHelper.getLevel(enchantRegistry.getEntry(enchantRegistry.get(ModEnchants.SHOTGUN)), stack) != 0) {
                                        pebbleEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 2F, 0.0F);
                                    }
                                }
                            }
                            pebbleEntity.setOwner(user);
                            world.spawnEntity(pebbleEntity);
                            pebbleEntity.setYaw(user.getYaw() + 90);
                            firstPebble = false;
                        }
                        stack.damage(1,user,EquipmentSlot.MAINHAND);
                        pebble.decrement(usedPebbles);
                    }

                    user.incrementStat(Stats.USED.getOrCreateStat(this));
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }


    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.of("Uses blackstone pebbles as a projectile."));
        tooltip.add(Text.of("Gets jammed when attacked while firing."));
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantability() {
        return 4;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (itemStack.hasEnchantments()) {
            Registry<Enchantment> enchantRegistry = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT);
            if (EnchantmentHelper.getLevel(enchantRegistry.getEntry(enchantRegistry.get(ModEnchants.AIRBLAST)), itemStack) != 0) {
                if (!world.isClient) {
                    if (!user.hasStatusEffect(ModEffects.AIRBORNE)) {
                        user.getItemCooldownManager().set(itemStack.getItem(), 100);
                        Vec3d velocity = user.getRotationVec(0).multiply(1.4f);
                        for (int i = 0; i < user.getRandom().nextBetween(20, 30); i++) {
                            Vec3d position = user.getEyePos().add(user.getRandom().nextBetween(-100, 100) / 100f, user.getRandom().nextBetween(-100, 100) / 100f, user.getRandom().nextBetween(-100, 100) / 100f);
                            user.getWorld().addParticle(ParticleTypes.CLOUD, position.x, position.y, position.z, velocity.x, velocity.y, velocity.z);
                        }
                        user.getWorld().playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_WIND_CHARGE_WIND_BURST.value(), SoundCategory.MASTER, 1f, 1f);
                        user.getWorld().getOtherEntities(user, new Box(user.getEyePos().add(user.getRotationVec(0f).multiply(4)).add(-4, -4, -4), user.getEyePos().add(user.getRotationVec(0f).multiply(4)).add(4, 4, 4))).forEach((e) -> {
                            e.setVelocity(e.getPos().add(user.getPos().multiply(-1)).add(0, 1, 0).multiply(0.5));
                            e.velocityModified = true;
                            e.velocityDirty = true;
                            if (e instanceof ServerPlayerEntity spe) {
                                spe.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(spe));
                            }
                        });
                        if (!user.isSneaking()) {
                            user.addStatusEffect(new StatusEffectInstance(ModEffects.AIRBORNE, 20 * 30, 0));
                            user.setVelocity(user.getRotationVec(0f).multiply(-1.4f));
                            user.velocityModified = true;
                            user.velocityDirty = true;
                            if (user instanceof ServerPlayerEntity spe) {
                                spe.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(spe));
                            }
                        }
                    }
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
            return TypedActionResult.success(itemStack);
        }
        return TypedActionResult.success(itemStack);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }


}
