package org.agmas.infernum_effugium.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.Infernum_effugium;

public class DeathWhistleItem extends Item {

    public DeathWhistleItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        SoundEvent soundEvent = SoundEvent.of(new Identifier(Infernum_effugium.MOD_ID, "whistle"), 200);
        world.playSoundFromEntity(user, user, soundEvent, SoundCategory.RECORDS, 1f, 1.0F);
        user.getItemCooldownManager().set(this, 300);
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return TypedActionResult.consume(itemStack);
    }


}
