package org.agmas.infernum_effugium.entity;

import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModEntities;
import org.agmas.infernum_effugium.ModItems;

import java.util.Random;

public class PebbleEntity extends ThrownItemEntity {

    public static final RegistryKey<DamageType> PEBBLE_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(Infernum_effugium.MOD_ID, "pebble"));
    public boolean shotFromCannon = false;

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
        entityHitResult.getEntity().damage(damageSource, shotFromCannon ? 3.5f : 1);
        entityHitResult.getEntity().timeUntilRegen = 0;
        entityHitResult.getEntity().setVelocity(0,0,0);
        entityHitResult.getEntity().velocityDirty = true;
        entityHitResult.getEntity().velocityModified = true;
        super.onEntityHit(entityHitResult);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.BLACKSTONE_PEBBLE;
    }
}
