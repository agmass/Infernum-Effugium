package org.agmas.infernum_effugium.block.blockEntities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.util.math.BlockPos;
import org.agmas.infernum_effugium.ModEntities;

public class BushBedrockDispenserBlockEntity extends DispenserBlockEntity {
    public BushBedrockDispenserBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(ModEntities.BUSHBEDROCK_DISPENSER_BLOCK_ENTITY, blockPos, blockState);
    }
    public BushBedrockDispenserBlockEntity( BlockPos blockPos, BlockState blockState) {
        super(ModEntities.BUSHBEDROCK_DISPENSER_BLOCK_ENTITY, blockPos, blockState);
    }
}
