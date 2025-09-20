package org.agmas.infernum_effugium.item;

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

public class NetherPactItem extends Item {




    public NetherPactItem(Settings settings) {
        super(settings);

    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient) {
            user.addStatusEffect(new StatusEffectInstance(ModEffects.NETHER_PACT, Integer.MAX_VALUE, 0));
            user.sendMessage(Text.literal("You have made a deal with hell.").formatted(Formatting.RED), false);
            user.sendMessage(Text.literal("Use /un_nether_pact to cure yourself.").formatted(Formatting.RED), false);
            StateSaverAndLoader.getPlayerState(user).netherPacted = true;
            if (!user.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
        }
        return TypedActionResult.consume(itemStack);
    }

}
