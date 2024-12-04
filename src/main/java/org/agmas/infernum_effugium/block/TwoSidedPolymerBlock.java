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
import net.minecraft.util.Identifier;
import org.agmas.infernum_effugium.Infernum_effugium;
import xyz.nucleoid.packettweaker.PacketContext;

public class TwoSidedPolymerBlock extends SimplePolymerBlock implements PolymerKeepModel, PolymerClientDecoded {

    public TwoSidedPolymerBlock(Settings settings, Block polymerBlock, String modelName) {
        super(settings, polymerBlock);
    }


    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        if (packetContext.getPlayer() == null) return super.getPolymerBlockState(blockState,packetContext);;
        if (PolymerServerNetworking.getMetadata(packetContext.getPlayer().networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) == NbtInt.of(1)) {
            return blockState;
        } else {
            return super.getPolymerBlockState(blockState,packetContext);
        }
    }
}
