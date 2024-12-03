package org.agmas.infernum_effugium.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.utils.PolymerClientDecoded;
import eu.pb4.polymer.core.api.utils.PolymerKeepModel;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import eu.pb4.polymer.resourcepack.api.PolymerModelData;
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
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.jetbrains.annotations.Nullable;

public class DeathWhistleItem extends Item implements PolymerItem, PolymerKeepModel, PolymerClientDecoded {

    PolymerModelData modelData;
    public DeathWhistleItem(Settings settings) {
        super(settings);
        modelData = PolymerResourcePackUtils.requestModel(Items.MACE, Identifier.of(Infernum_effugium.MOD_ID, "item/death_whistle"));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        SoundEvent soundEvent = SoundEvent.of(Identifier.of(Infernum_effugium.MOD_ID, "whistle"), 200);
        world.playSoundFromEntity(user, user, soundEvent, SoundCategory.RECORDS, 1f, 1.0F);
        user.getItemCooldownManager().set(this, 300);
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public ItemStack getPolymerItemStack(ItemStack itemStack, TooltipType tooltipType, RegistryWrapper.WrapperLookup lookup, @Nullable ServerPlayerEntity player) {
        var itemStack1 = PolymerItem.super.getPolymerItemStack(itemStack, tooltipType, lookup, player);
        itemStack1.set(DataComponentTypes.CUSTOM_MODEL_DATA, modelData.asComponent());
        return itemStack1;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity serverPlayerEntity) {
        if (serverPlayerEntity == null) return Items.GOAT_HORN;
        if (PolymerServerNetworking.getMetadata(serverPlayerEntity.networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) == NbtInt.of(1)) {
            return this;
        } else {
            return Items.GOAT_HORN;
        }
    }
}
