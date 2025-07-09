package org.agmas.infernum_effugium.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.utils.PolymerClientDecoded;
import eu.pb4.polymer.core.api.utils.PolymerKeepModel;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import eu.pb4.polymer.resourcepack.api.PolymerModelData;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtInt;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModEffects;
import org.agmas.infernum_effugium.state.StateSaverAndLoader;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class NetherPactItem extends Item implements PolymerItem, PolymerKeepModel, PolymerClientDecoded {


    PolymerModelData modelData;


    public NetherPactItem(Settings settings) {
        super(settings);
        modelData = PolymerResourcePackUtils.requestModel(Items.FIRE_CHARGE, Identifier.of(Infernum_effugium.MOD_ID, "item/magma_pebble"));

    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient) {
            user.addStatusEffect(new StatusEffectInstance(ModEffects.NETHER_PACT, Integer.MAX_VALUE, 0));
            user.sendMessage(Text.literal("You have made a deal with hell.").formatted(Formatting.RED), false);
            user.sendMessage(Text.literal("Use /un_nether_pact to cure yourself.").formatted(Formatting.RED), false);
            if (PolymerServerNetworking.getMetadata(((ServerPlayerEntity) user).networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) != NbtInt.of(1)) {
                user.sendMessage(Text.literal("!! NOTE !!").formatted(Formatting.DARK_RED), false);
                user.sendMessage(Text.literal("Some features of the Nether Pact (walking on lava, redder skin) only work with the mod installed.").formatted(Formatting.GRAY), false);
                user.sendMessage(Text.literal("Install the mod on modrinth at https://modrinth.com/mod/infernum-effugium.").formatted(Formatting.BLUE), false);
            }

            StateSaverAndLoader.getPlayerState(user).netherPacted = true;
            if (!user.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
        }
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
        if (serverPlayerEntity == null) return Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE;
        if (PolymerServerNetworking.getMetadata(serverPlayerEntity.networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) == NbtInt.of(1)) {
            return this;
        } else {
            return Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE;
        }
    }
}
