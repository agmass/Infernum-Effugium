package org.agmas.infernum_effugium.block;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.nbt.NbtInt;
import net.minecraft.server.network.ServerPlayerEntity;
import org.agmas.infernum_effugium.Infernum_effugium;

public class PolymerLadderBlock extends LadderBlock implements PolymerBlock {
    public PolymerLadderBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState) {
        return Blocks.LADDER.getDefaultState();
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, ServerPlayerEntity player) {
        if (player == null) return Blocks.LADDER.getStateWithProperties(state);
        if (PolymerServerNetworking.getMetadata(player.networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) == NbtInt.of(1)) {
            return state;
        } else {
            return Blocks.LADDER.getStateWithProperties(state);
        }
    }
}
