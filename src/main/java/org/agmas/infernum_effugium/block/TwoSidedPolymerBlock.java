package org.agmas.infernum_effugium.block;

import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.core.api.block.SimplePolymerBlock;
import eu.pb4.polymer.core.api.utils.PolymerClientDecoded;
import eu.pb4.polymer.core.api.utils.PolymerKeepModel;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtInt;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.agmas.infernum_effugium.Infernum_effugium;
import xyz.nucleoid.packettweaker.PacketContext;

public class TwoSidedPolymerBlock extends SimplePolymerBlock implements PolymerKeepModel, PolymerClientDecoded, PolymerTexturedBlock {

    BlockState polymerState;
    public TwoSidedPolymerBlock(Settings settings, Block polymerBlock, String modelName) {
        super(settings, polymerBlock);
        polymerState = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, PolymerBlockModel.of(Identifier.of(Infernum_effugium.MOD_ID, "block/" + modelName)));
    }


    @Override
    public BlockState getPolymerBlockState(BlockState state, ServerPlayerEntity player) {
        if (player == null) return super.getPolymerBlockState(state,player);
        if (PolymerServerNetworking.getMetadata(player.networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) == NbtInt.of(1)) {
            return state;
        } else {
            if (PolymerResourcePackUtils.hasMainPack(player)) {
                return polymerState;
            }
            return super.getPolymerBlockState(state,player);
        }
    }

}
