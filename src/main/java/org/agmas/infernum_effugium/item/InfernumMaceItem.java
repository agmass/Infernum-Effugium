package org.agmas.infernum_effugium.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.utils.PolymerClientDecoded;
import eu.pb4.polymer.core.api.utils.PolymerKeepModel;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtInt;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModEffects;
import org.agmas.infernum_effugium.ModItems;
import org.agmas.infernum_effugium.entity.MagmaPebbleEntity;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class InfernumMaceItem extends MaceItem implements PolymerItem, PolymerKeepModel, PolymerClientDecoded {

    public InfernumMaceItem(Settings settings, int attackDamage) {
        super(settings);
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
        super.postDamageEntity(stack, target, attacker);
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

                target.addStatusEffect(new StatusEffectInstance(ModEffects.EXTREME_FIRE, (int) totalDamage, 0));

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
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        return 0;
    }

    public static boolean shouldSetOnFire(LivingEntity attacker) {
        return attacker.fallDistance > 1.5F && !attacker.isGliding();
    }

    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        if (context.getPlayer() == null) return Identifier.of("minecraft", "mace");
        if (PolymerServerNetworking.getMetadata(context.getPlayer().networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) != null) {
            return Identifier.of(Infernum_effugium.MOD_ID, "infernum_mace");
        } else {
            if (PolymerResourcePackUtils.hasMainPack(context)) {
                return Identifier.of(Infernum_effugium.MOD_ID, "infernum_mace");

            } else {
                return Identifier.of("minecraft", "mace");
            }
        }
    }
    private static double getKnockback(PlayerEntity player, LivingEntity attacked, Vec3d distance) {
        return (3.5 - distance.length())
                * 0.7F
                * (double)(player.fallDistance > 5.0F ? 2 : 1)
                * (1.0 - attacked.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE));
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        if (packetContext.getPlayer() == null) return Items.MACE;
        if (PolymerServerNetworking.getMetadata(packetContext.getPlayer().networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) == NbtInt.of(1)) {
            return this;
        } else {
            return Items.MACE;
        }
    }
}
