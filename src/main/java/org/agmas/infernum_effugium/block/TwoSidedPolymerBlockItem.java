package org.agmas.infernum_effugium.block;

import eu.pb4.polymer.core.api.block.SimplePolymerBlock;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtInt;
import net.minecraft.server.network.ServerPlayerEntity;
import org.agmas.infernum_effugium.Infernum_effugium;

public class TwoSidedPolymerBlockItem extends SimplePolymerBlock {
    public TwoSidedPolymerBlockItem(Settings settings, Block polymerBlock) {
        super(settings, polymerBlock);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, ServerPlayerEntity player) {
        if (player == null) return getPolymerBlockState(state);
        if (PolymerServerNetworking.getMetadata(player.networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) == NbtInt.of(1)) {
            return state;
        } else {
            return getPolymerBlockState(state);
        }
    }
}
