package org.agmas.infernum_effugium.mixin;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.network.packet.s2c.play.ChunkData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ChunkData.BlockEntityData.class)
public interface BlockEntityDataAccessor {
    @Accessor
    @Final
    BlockEntityType<?> getType();
}
