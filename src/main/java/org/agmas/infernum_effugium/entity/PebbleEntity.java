package org.agmas.infernum_effugium.entity;

import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.core.api.utils.PolymerClientDecoded;
import eu.pb4.polymer.core.api.utils.PolymerKeepModel;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtInt;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModEffects;
import org.agmas.infernum_effugium.ModEntities;
import org.agmas.infernum_effugium.ModItems;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.Random;

public class PebbleEntity extends ThrownItemEntity implements PolymerEntity, PolymerKeepModel, PolymerClientDecoded {

    public static final RegistryKey<DamageType> PEBBLE_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(Infernum_effugium.MOD_ID, "pebble"));
    public boolean shotFromCannon = false;
    public boolean shotFromBackburner = false;

    public PebbleEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }
    public PebbleEntity(World world, LivingEntity owner) {
        super(ModEntities.PEBBLE, world);
    }


    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {

        if (!getWorld().isClient) {
            if (getStack().isOf(ModItems.MAGMA_PEBBLE)) {
                getWorld().setBlockState(blockHitResult.getBlockPos().offset(blockHitResult.getSide()), Blocks.FIRE.getDefaultState());
            }
        }
        super.onBlockHit(blockHitResult);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            this.discard();
        }
    }
    @Override
    public boolean updateMovementInFluid(TagKey<Fluid> tag, double speed) {
        if (super.updateMovementInFluid(tag, speed)) {
            addVelocity(0,0.5,0);
            if (new Random().nextInt(0,100) > 90) {
                discard();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity().getEntityWorld() instanceof ServerWorld) {
            DamageSource damageSource = new DamageSource(
                    entityHitResult.getEntity().getEntityWorld().getRegistryManager()
                            .getOrThrow(RegistryKeys.DAMAGE_TYPE)
                            .getEntry(PEBBLE_DAMAGE.getValue()).get());
            if (shotFromBackburner) {
                Vec3d directionHit = getPos().relativize(entityHitResult.getEntity().getPos()).normalize();
                if (directionHit.subtract(entityHitResult.getEntity().getRotationVec(0f)).length() <= 1.2f) {
                    if (getOwner() != null) {
                        if (getOwner() instanceof PlayerEntity pe) {
                            pe.sendMessage(Text.literal("Backburn!").formatted(Formatting.GREEN),true);
                            pe.playSound(SoundEvents.ENTITY_ARROW_HIT_PLAYER, 1f, 1.5f);
                        }
                    }
                    if (entityHitResult.getEntity() instanceof LivingEntity le) {
                        le.addStatusEffect(new StatusEffectInstance(ModEffects.EXTREME_FIRE, 10, 1));
                    }
                }
            }
            entityHitResult.getEntity().damage((ServerWorld) entityHitResult.getEntity().getEntityWorld(), damageSource, shotFromCannon ? 3.5f : 1);
            entityHitResult.getEntity().setVelocity(0, 0, 0);
            if (getStack().isOf(ModItems.MAGMA_PEBBLE)) {
                entityHitResult.getEntity().setFireTicks(120);
            }
            entityHitResult.getEntity().velocityDirty = true;
            entityHitResult.getEntity().timeUntilRegen = 0;
            entityHitResult.getEntity().velocityModified = true;
        }
        super.onEntityHit(entityHitResult);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.BLACKSTONE_PEBBLE;
    }



    @Override
    public EntityType<?> getPolymerEntityType(PacketContext packetContext) {

        if (getStack().isOf(ModItems.MAGMA_PEBBLE)) {
            if (packetContext.getPlayer() == null) return EntityType.SMALL_FIREBALL;
            if (PolymerServerNetworking.getMetadata(packetContext.getPlayer().networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) == NbtInt.of(1)) {
                return ModEntities.PEBBLE;
            } else {
                return EntityType.SMALL_FIREBALL;
            }
        }
        if (packetContext.getPlayer() == null) return EntityType.EGG;
        if (PolymerServerNetworking.getMetadata(packetContext.getPlayer().networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) == NbtInt.of(1)) {
            return ModEntities.PEBBLE;
        } else {
            return EntityType.EGG;
        }
    }
}
