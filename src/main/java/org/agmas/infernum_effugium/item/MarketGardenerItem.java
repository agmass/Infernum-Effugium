package org.agmas.infernum_effugium.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModItems;
import org.agmas.infernum_effugium.entity.PebbleEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MarketGardenerItem extends SwordItem {

    public MarketGardenerItem(Settings settings, int attackDamage) {
        super(ToolMaterials.DIAMOND, attackDamage, -1.5F, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1,attacker,(e)->{
            e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
        });
        if (attacker instanceof ServerPlayerEntity spe) {
            if (shouldCrit(spe)) {
                target.damage(attacker.getDamageSources().mobAttack(attacker), 50);
                spe.sendMessage(Text.literal("Crit!").formatted(Formatting.GREEN), true);
                spe.playSound(SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.MASTER, 1f, 1.5f);
            }
        }
        return true;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.of("Deal critical damage when airborne from an airblast-enchanted pebble cannon."));
        super.appendTooltip(stack, world, tooltip, context);
    }

    public static boolean shouldCrit(LivingEntity attacker) {
        return attacker.hasStatusEffect(Infernum_effugium.AIRBORNE) && attacker.fallDistance > 0.25F && !attacker.isFallFlying();
    }
}
