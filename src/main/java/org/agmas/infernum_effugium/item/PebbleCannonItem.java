package org.agmas.infernum_effugium.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.utils.PolymerClientDecoded;
import eu.pb4.polymer.core.api.utils.PolymerKeepModel;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.consume.UseAction;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtInt;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModItems;
import org.agmas.infernum_effugium.entity.PebbleEntity;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;
import java.util.Random;

public class PebbleCannonItem extends Item implements PolymerItem, PolymerKeepModel, PolymerClientDecoded {
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
                        pebbleEntity.shotFromCannon = true;
                        pebbleEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 0.75F, 1.0F);
                        pebbleEntity.setPosition(user.getEyePos().add(user.getRotationVector()));
                        world.spawnEntity(pebbleEntity);
                        stack.damage(1,user,EquipmentSlot.MAINHAND);
                        pebble.decrement(1);
                        if (new Random().nextInt(0,500) == 0) {
                            user.getItemCooldownManager().set(stack, 20*12);
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
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.of("Uses blackstone pebbles as a projectile."));
        tooltip.add(Text.of("Small chance to get jammed on use."));
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
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
            return ActionResult.CONSUME;
        }
        return ActionResult.FAIL;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        if (packetContext.getPlayer() == null) return Items.BOW;
        if (PolymerServerNetworking.getMetadata(packetContext.getPlayer().networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) == NbtInt.of(1)) {
            return this;
        } else {
            return Items.BOW;
        }
    }


    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        if (context.getPlayer() == null) return Identifier.of("minecraft", "bow");
        if (PolymerServerNetworking.getMetadata(context.getPlayer().networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) != null) {
            return Identifier.of(Infernum_effugium.MOD_ID, "pebble_cannon");
        } else {
            if (PolymerResourcePackUtils.hasMainPack(context)) {
                return Identifier.of(Infernum_effugium.MOD_ID, "pebble_cannon");

            } else {
                return Identifier.of("minecraft", "bow");
            }
        }
    }
}
