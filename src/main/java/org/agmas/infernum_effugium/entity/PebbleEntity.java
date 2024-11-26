package org.agmas.infernum_effugium.entity;

import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.core.api.utils.PolymerClientDecoded;
import eu.pb4.polymer.core.api.utils.PolymerKeepModel;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtInt;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModEntities;
import org.agmas.infernum_effugium.ModItems;

import java.util.Random;

public class PebbleEntity extends ThrownItemEntity implements PolymerEntity, PolymerKeepModel, PolymerClientDecoded {

    public static final RegistryKey<DamageType> PEBBLE_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(Infernum_effugium.MOD_ID, "pebble"));

    public PebbleEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }
    public PebbleEntity(World world, LivingEntity owner) {
        super(ModEntities.PEBBLE, owner, world);
    }

    public PebbleEntity(EntityType<? extends ThrownItemEntity> thrownItemEntityEntityType, LivingEntity owner, World world) {
        super(thrownItemEntityEntityType,owner,world);
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
        DamageSource damageSource = new DamageSource(
                entityHitResult.getEntity().getEntityWorld().getRegistryManager()
                        .get(RegistryKeys.DAMAGE_TYPE)
                        .entryOf(PEBBLE_DAMAGE));
        entityHitResult.getEntity().damage(damageSource, 1);
        entityHitResult.getEntity().setVelocity(0,0,0);
        entityHitResult.getEntity().velocityDirty = true;
        entityHitResult.getEntity().velocityModified = true;
        super.onEntityHit(entityHitResult);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.BLACKSTONE_PEBBLE;
    }


    @Override
    public EntityType<?> getPolymerEntityType(ServerPlayerEntity serverPlayerEntity) {
        if (serverPlayerEntity == null) return EntityType.EGG;
        if (PolymerServerNetworking.getMetadata(serverPlayerEntity.networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) == NbtInt.of(1)) {
            return ModEntities.PEBBLE;
        } else {
            return EntityType.EGG;
        }
    }
}
