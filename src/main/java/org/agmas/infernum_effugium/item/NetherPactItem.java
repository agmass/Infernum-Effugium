package org.agmas.infernum_effugium.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.utils.PolymerClientDecoded;
import eu.pb4.polymer.core.api.utils.PolymerKeepModel;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
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
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModEffects;
import org.agmas.infernum_effugium.state.StateSaverAndLoader;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import javax.swing.*;

public class NetherPactItem extends Item implements PolymerItem, PolymerKeepModel, PolymerClientDecoded {


    public NetherPactItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient) {
            user.addStatusEffect(new StatusEffectInstance(ModEffects.NETHER_PACT, Integer.MAX_VALUE, 0));
            user.sendMessage(Text.literal("You have made a deal with hell.").formatted(Formatting.RED), false);
            user.sendMessage(Text.literal("Dying to powdered snow will clear you of your pact.").formatted(Formatting.RED), false);

            StateSaverAndLoader.getPlayerState(user).netherPacted = true;
            if (!user.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
        }
        return ActionResult.CONSUME;
    }


    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        return Identifier.of(Infernum_effugium.MOD_ID, "nether_pact");
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        if (packetContext.getPlayer() == null) return Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE;
        if (PolymerServerNetworking.getMetadata(packetContext.getPlayer().networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) == NbtInt.of(1)) {
            return this;
        } else {
            return Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE;
        }
    }

}
