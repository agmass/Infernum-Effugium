package org.agmas.infernum_effugium.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtInt;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModEntities;
import org.agmas.infernum_effugium.entity.PebbleEntity;
import org.jetbrains.annotations.Nullable;

public class PebbleItem extends Item implements PolymerItem {
    public PebbleItem(Settings settings) {
        super(settings);
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(
                null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (!world.isClient) {
            PebbleEntity pebbleEntity = new PebbleEntity(world, user);
            pebbleEntity.setItem(itemStack);
            pebbleEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 0.75F, 1.0F);
            world.spawnEntity(pebbleEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity serverPlayerEntity) {
        if (serverPlayerEntity == null) return Items.BLACKSTONE_SLAB;
        if (PolymerServerNetworking.getMetadata(serverPlayerEntity.networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) == NbtInt.of(1)) {
            return this;
        } else {
            return Items.BLACKSTONE_SLAB;
        }
    }
}
