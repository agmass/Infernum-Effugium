package org.agmas.infernum_effugium.mixin;

import net.minecraft.network.packet.s2c.play.ChunkData;
import org.agmas.infernum_effugium.ChunkDataAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ChunkData.class)
public class ChunkDataMixin implements ChunkDataAccess {


    @Shadow @Final private List<ChunkData.BlockEntityData> blockEntities;

    @Override
    public List<ChunkData.BlockEntityData> infernumeffugium$getBlockEntities() {
        return blockEntities;
    }
}
