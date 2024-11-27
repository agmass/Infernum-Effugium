package org.agmas.infernum_effugium.block.blockEntities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import org.agmas.infernum_effugium.ModEntities;

public class GreedVaultBlockEntity extends BlockEntity {
    public GreedVaultBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public GreedVaultBlockEntity(BlockPos pos, BlockState state) {
        super(ModEntities.GREED_VAULT, pos, state);
    }

}
