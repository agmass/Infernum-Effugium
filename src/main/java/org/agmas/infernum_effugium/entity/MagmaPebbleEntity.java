package org.agmas.infernum_effugium.entity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModEntities;
import org.agmas.infernum_effugium.ModItems;

import java.util.Random;

public class MagmaPebbleEntity extends PebbleEntity {

    public MagmaPebbleEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }
    public MagmaPebbleEntity(World world, LivingEntity owner) {
        super(ModEntities.MAGMA_PEBBLE, owner, world);
    }


    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {

        getWorld().setBlockState(blockHitResult.getBlockPos().offset(blockHitResult.getSide()), Blocks.FIRE.getDefaultState());

        super.onBlockHit(blockHitResult);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        DamageSource damageSource = new DamageSource(
                entityHitResult.getEntity().getEntityWorld().getRegistryManager()
                        .get(RegistryKeys.DAMAGE_TYPE)
                        .entryOf(PEBBLE_DAMAGE));
        entityHitResult.getEntity().damage(damageSource, 1);
        entityHitResult.getEntity().timeUntilRegen = 0;
        entityHitResult.getEntity().setVelocity(0,0,0);
        entityHitResult.getEntity().setFireTicks(120);
        entityHitResult.getEntity().velocityDirty = true;
        entityHitResult.getEntity().velocityModified = true;
        super.onEntityHit(entityHitResult);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.MAGMA_PEBBLE;
    }
}
