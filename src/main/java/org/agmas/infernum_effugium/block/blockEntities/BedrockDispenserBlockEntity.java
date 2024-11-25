package org.agmas.infernum_effugium.block.blockEntities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.util.math.BlockPos;
import org.agmas.infernum_effugium.ModEntities;

public class BedrockDispenserBlockEntity extends DispenserBlockEntity {
    public BedrockDispenserBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(ModEntities.BEDROCK_DISPENSER_BLOCK_ENTITY, blockPos, blockState);
    }
    public BedrockDispenserBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModEntities.BEDROCK_DISPENSER_BLOCK_ENTITY, blockPos, blockState);
    }
}
