package org.agmas.infernum_effugium.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.utils.PolymerClientDecoded;
import eu.pb4.polymer.core.api.utils.PolymerKeepModel;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtInt;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.entity.MagmaPebbleEntity;
import org.agmas.infernum_effugium.entity.PebbleEntity;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class MagmaPebbleItem extends PebbleItem implements PolymerItem, PolymerKeepModel, PolymerClientDecoded {


    public MagmaPebbleItem(Settings settings) {
        super(settings);
    }


    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(
                null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (!world.isClient) {
            MagmaPebbleEntity pebbleEntity = new MagmaPebbleEntity(world, user);
            pebbleEntity.setItem(itemStack);
            pebbleEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 0.75F, 1.0F);
            pebbleEntity.setPosition(user.getEyePos().add(user.getRotationVector()));
            world.spawnEntity(pebbleEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        if (context.getPlayer() == null) return Identifier.of("minecraft", "magma_cream");
        if (PolymerServerNetworking.getMetadata(context.getPlayer().networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) != null) {
            return Identifier.of(Infernum_effugium.MOD_ID, "magma_pebble");
        } else {
            if (PolymerResourcePackUtils.hasMainPack(context)) {
                return Identifier.of(Infernum_effugium.MOD_ID, "magma_pebble");

            } else {
                return Identifier.of("minecraft", "magma_cream");
            }
        }
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        if (packetContext.getPlayer() == null) return Items.FIRE_CHARGE;
        if (PolymerServerNetworking.getMetadata(packetContext.getPlayer().networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) == NbtInt.of(1)) {
            return this;
        } else {
            return Items.FIRE_CHARGE;
        }
    }

}
