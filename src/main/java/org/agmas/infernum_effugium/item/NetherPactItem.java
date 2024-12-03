package org.agmas.infernum_effugium.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
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
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.state.StateSaverAndLoader;
import org.jetbrains.annotations.Nullable;

public class NetherPactItem extends Item implements PolymerItem {


    PolymerModelData modelData;
    public NetherPactItem(Settings settings) {
        super(settings);
        modelData = PolymerResourcePackUtils.requestModel(Items.MACE, Identifier.of(Infernum_effugium.MOD_ID, "item/nether_pact"));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient) {
            user.addStatusEffect(new StatusEffectInstance(RegistryEntry.of(Infernum_effugium.NETHER_PACT), Integer.MAX_VALUE, 0));
            user.sendMessage(Text.literal("You have made a deal with hell.").formatted(Formatting.RED));

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
