package org.agmas.infernum_effugium.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.state.StateSaverAndLoader;

public class NetherPactItem extends Item {
    public NetherPactItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient) {
            user.addStatusEffect(new StatusEffectInstance(Infernum_effugium.NETHER_PACT, Integer.MAX_VALUE, 0));
            user.sendMessage(Text.literal("You have made a deal with hell.").formatted(Formatting.RED));
            user.sendMessage(Text.literal("Dying to powdered snow will clear you of your pact.").formatted(Formatting.RED));

            StateSaverAndLoader.getPlayerState(user).netherPacted = true;
            if (!user.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
        }
        return TypedActionResult.consume(itemStack);
    }
}
