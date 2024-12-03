package org.agmas.infernum_effugium;

import eu.pb4.polymer.core.api.block.PolymerBlockUtils;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.agmas.infernum_effugium.block.blockEntities.BedrockDispenserBlockEntity;
import org.agmas.infernum_effugium.block.blockEntities.BushBedrockDispenserBlockEntity;
import org.agmas.infernum_effugium.block.blockEntities.GreedVaultBlockEntity;
import org.agmas.infernum_effugium.entity.MagmaPebbleEntity;
import org.agmas.infernum_effugium.entity.PebbleEntity;

public class ModEntities {

    public static final EntityType<PebbleEntity> PEBBLE = register(
            "pebble",
            EntityType.Builder.<PebbleEntity>create(PebbleEntity::new, SpawnGroup.MISC).dimensions(0.25F, 0.25F).maxTrackingRange(4).trackingTickInterval(10)
    );
    public static final EntityType<MagmaPebbleEntity> MAGMA_PEBBLE = register(
            "magmapebble",
            EntityType.Builder.<MagmaPebbleEntity>create(MagmaPebbleEntity::new, SpawnGroup.MISC).dimensions(0.25F, 0.25F).maxTrackingRange(4).trackingTickInterval(10)
    );
    public static final BlockEntityType<GreedVaultBlockEntity> GREED_VAULT = blockEntityRegister(
            "greed_vault",
            BlockEntityType.Builder.create(GreedVaultBlockEntity::new, ModBlocks.GREED_VAULT).build(null)
    );



    public static final BlockEntityType<BedrockDispenserBlockEntity> BEDROCK_DISPENSER_BLOCK_ENTITY = blockEntityRegister(
            "bedrock_dispenser_block_entity",
            BlockEntityType.Builder.create(BedrockDispenserBlockEntity::new, ModBlocks.BEDROCK_DISPENSER).build()
    );
    public static final BlockEntityType<BushBedrockDispenserBlockEntity> BUSHBEDROCK_DISPENSER_BLOCK_ENTITY = blockEntityRegister(
            "bush_bedrock_dispenser_block_entity",
            BlockEntityType.Builder.create(BushBedrockDispenserBlockEntity::new, ModBlocks.ROCKY_BUSH_BUT_ITS_ACTUALLY_A_DISPENSER).build()
    );



    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, Identifier.of(Infernum_effugium.MOD_ID, id), type.build(id));
    }
    public static <T extends BlockEntityType<?>> T blockEntityRegister(String path, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Infernum_effugium.MOD_ID, path), blockEntityType);
    }


    public static void init() {
        PolymerEntityUtils.registerType(MAGMA_PEBBLE, PEBBLE);
        PolymerBlockUtils.registerBlockEntity(BEDROCK_DISPENSER_BLOCK_ENTITY, BUSHBEDROCK_DISPENSER_BLOCK_ENTITY, GREED_VAULT);
    }

}
