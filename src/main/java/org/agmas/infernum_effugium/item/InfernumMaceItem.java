package org.agmas.infernum_effugium.item;

import net.minecraft.block.BlockState;
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
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModItems;
import org.agmas.infernum_effugium.entity.PebbleEntity;

public class InfernumMaceItem extends SwordItem {

    public InfernumMaceItem(Settings settings, int attackDamage) {
        super(ToolMaterials.DIAMOND, attackDamage, -3.5F, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1,attacker,(e)->{
            e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
        });
        if (attacker instanceof ServerPlayerEntity spe) {
            if (shouldSetOnFire(spe)) {
                target.setVelocity(new Vec3d(0, spe.fallDistance*0.075,0));
                if (target instanceof ServerPlayerEntity serverPlayerEntity) {
                    serverPlayerEntity.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(serverPlayerEntity));
                }


                float totalDamage;
                if (spe.fallDistance <= 3.0F) {
                    totalDamage = 4.0F + spe.fallDistance;
                } else if (spe.fallDistance <= 8.0F) {
                    totalDamage = 12.0F + 2.0F * (spe.fallDistance - 3.0F);
                } else {
                    totalDamage = 22.0F + spe.fallDistance - 8.0F;
                }

                target.addStatusEffect(new StatusEffectInstance(Infernum_effugium.EXTREME_FIRE, (int) totalDamage, 0));

                for (int i = 0; i < spe.fallDistance*2; i++) {
                    PebbleEntity pebbleEntity = new PebbleEntity(target.getWorld(), attacker);
                    pebbleEntity.setItem(ModItems.MAGMA_PEBBLE.getDefaultStack());
                    pebbleEntity.setVelocity(0,1.5,0,Math.min(spe.fallDistance/9,3),20);
                    pebbleEntity.setPosition(target.getPos().add(0,2,0));
                    target.getWorld().spawnEntity(pebbleEntity);
                }

                target.getWorld().getEntitiesByClass(LivingEntity.class, target.getBoundingBox().expand(3.5), (e)->{
                    if (e == target) {
                        return false;
                    }
                    Vec3d vec3d = e.getPos().subtract(target.getPos());
                    double knockback = getKnockback(spe, e, vec3d);
                    Vec3d vec3d2 = vec3d.normalize().multiply(knockback);

                    if (knockback > 0.0) {
                        e.addVelocity(vec3d2.x, 0.7F, vec3d2.z);
                        if (e instanceof ServerPlayerEntity serverPlayerEntity) {
                            serverPlayerEntity.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(serverPlayerEntity));
                        }
                    }
                    return true;
                });
                spe.fallDistance = 0;
            }
        }
        return true;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        return true;
    }


    public static boolean shouldSetOnFire(LivingEntity attacker) {
        return attacker.fallDistance > 1.5F && !attacker.isFallFlying();
    }


    private static double getKnockback(PlayerEntity player, LivingEntity attacked, Vec3d distance) {
        return (3.5 - distance.length())
                * 0.7F
                * (double)(player.fallDistance > 5.0F ? 2 : 1)
                * (1.0 - attacked.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
    }
}
