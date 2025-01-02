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
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class DeathWhistleItem extends Item implements PolymerItem, PolymerKeepModel, PolymerClientDecoded {

    public DeathWhistleItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        SoundEvent soundEvent = SoundEvent.of(Identifier.of(Infernum_effugium.MOD_ID, "whistle"), 200);
        world.playSoundFromEntity(user, user, soundEvent, SoundCategory.RECORDS, 1f, 1.0F);
        user.getItemCooldownManager().set(itemStack, 300);
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return ActionResult.CONSUME;
    }


    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        if (packetContext.getPlayer() == null) return Items.GOAT_HORN;
        if (PolymerServerNetworking.getMetadata(packetContext.getPlayer().networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) == NbtInt.of(1)) {
            return this;
        } else {
            return Items.GOAT_HORN;
        }
    }


    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        if (context.getPlayer() == null) return Identifier.of("minecraft", "goat_horn");
        if (PolymerServerNetworking.getMetadata(context.getPlayer().networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) != null) {
            return Identifier.of(Infernum_effugium.MOD_ID, "death_whistle");
        } else {
            if (PolymerResourcePackUtils.hasMainPack(context)) {
                return Identifier.of(Infernum_effugium.MOD_ID, "death_whistle");

            } else {
                return Identifier.of("minecraft", "goat_horn");
            }
        }
    }

}
