package org.agmas.infernum_effugium.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtInt;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModItems;
import org.agmas.infernum_effugium.entity.MagmaPebbleEntity;
import org.jetbrains.annotations.Nullable;

public class InfernumMaceItem extends SwordItem implements PolymerItem {

    public InfernumMaceItem(Settings settings, int attackDamage) {
        super(ToolMaterials.DIAMOND, settings);
    }

    public static AttributeModifiersComponent createAttributeModifiers() {
        return AttributeModifiersComponent.builder().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, 5.0, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).add(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, -3.4000000953674316, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).build();
    }


    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof ServerPlayerEntity spe) {
            if (shouldSetOnFire(spe)) {
                target.setVelocity(new Vec3d(0, spe.fallDistance*0.075,0));


                float totalDamage;
                if (spe.fallDistance <= 3.0F) {
                    totalDamage = 4.0F + spe.fallDistance;
                } else if (spe.fallDistance <= 8.0F) {
                    totalDamage = 12.0F + 2.0F * (spe.fallDistance - 3.0F);
                } else {
                    totalDamage = 22.0F + spe.fallDistance - 8.0F;
                }

                target.addStatusEffect(new StatusEffectInstance(RegistryEntry.of(Infernum_effugium.EXTREME_FIRE), (int) totalDamage, 0));

                for (int i = 0; i < spe.fallDistance*2; i++) {
                    MagmaPebbleEntity pebbleEntity = new MagmaPebbleEntity(target.getWorld(), attacker);
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

    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity serverPlayerEntity) {
        if (serverPlayerEntity == null) return Items.MACE;
        if (PolymerServerNetworking.getMetadata(serverPlayerEntity.networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) == NbtInt.of(1)) {
            return this;
        } else {
            return Items.MACE;
        }
    }
}
