package org.agmas.infernum_effugium;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.agmas.infernum_effugium.block.blockEntities.GreedVaultBlockEntity;
import org.agmas.infernum_effugium.entity.MagmaPebbleEntity;
import org.agmas.infernum_effugium.entity.PebbleEntity;

public class ModEntities {

    public static final EntityType<PebbleEntity> PEBBLE = register(
            "pebble",
            EntityType.Builder.<PebbleEntity>create(PebbleEntity::new, SpawnGroup.MISC).setDimensions(0.25F, 0.25F).maxTrackingRange(4).trackingTickInterval(10)
    );
    public static final EntityType<MagmaPebbleEntity> MAGMA_PEBBLE = register(
            "magmapebble",
            EntityType.Builder.<MagmaPebbleEntity>create(MagmaPebbleEntity::new, SpawnGroup.MISC).setDimensions(0.25F, 0.25F).maxTrackingRange(4).trackingTickInterval(10)
    );

    public static <T extends BlockEntityType<?>> T registerBlockEntity(String path, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("tutorial", path), blockEntityType);
    }

    public static final BlockEntityType<GreedVaultBlockEntity> GREED_VAULT = registerBlockEntity(
            "greed_vault",
            BlockEntityType.Builder.create(GreedVaultBlockEntity::new, ModBlocks.GREED_VAULT).build(null)
    );


    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, new Identifier(Infernum_effugium.MOD_ID, id), type.build(id));
    }

    public static void init() {}

}
