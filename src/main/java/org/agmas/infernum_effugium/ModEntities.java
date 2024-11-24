package org.agmas.infernum_effugium;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.agmas.infernum_effugium.entity.PebbleEntity;

public class ModEntities {

    public static final EntityType<PebbleEntity> PEBBLE = register(
            "pebble",
            EntityType.Builder.<PebbleEntity>create(PebbleEntity::new, SpawnGroup.MISC).setDimensions(0.25F, 0.25F).maxTrackingRange(4).trackingTickInterval(10)
    );


    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, id, type.build(id));
    }

    public static void init() {}

}
