package org.agmas.infernum_effugium.block;

import com.mojang.serialization.MapCodec;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.core.api.block.SimplePolymerBlock;
import eu.pb4.polymer.core.api.utils.PolymerClientDecoded;
import eu.pb4.polymer.core.api.utils.PolymerKeepModel;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.nbt.NbtInt;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.agmas.infernum_effugium.Infernum_effugium;
import xyz.nucleoid.packettweaker.PacketContext;

public class TwoSidedPillarPolymerBlock extends SimplePolymerBlock implements PolymerKeepModel, PolymerClientDecoded, PolymerTexturedBlock {

    public static final MapCodec<PillarBlock> CODEC = createCodec(PillarBlock::new);
    public static final EnumProperty<Direction.Axis> AXIS;

    public MapCodec<? extends PillarBlock> getCodec() {
        return CODEC;
    }

    BlockState polymerState = null;
    BlockState polymerStateX = null;
    BlockState polymerStateZ = null;


    public TwoSidedPillarPolymerBlock(Settings settings, Block polymerBlock, String modelName) {
        super(settings, polymerBlock);

        polymerState = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, PolymerBlockModel.of(Identifier.of(Infernum_effugium.MOD_ID, "block/" +modelName)));
        polymerState = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, PolymerBlockModel.of(Identifier.of(Infernum_effugium.MOD_ID, "block/" +modelName)));
        polymerState = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, PolymerBlockModel.of(Identifier.of(Infernum_effugium.MOD_ID, "block/" +modelName)));
        this.setDefaultState((BlockState)this.getDefaultState().with(AXIS, Direction.Axis.Y));
    }


    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        if (packetContext.getPlayer() == null) return super.getPolymerBlockState(blockState,packetContext);;
        if (PolymerServerNetworking.getMetadata(packetContext.getPlayer().networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) == NbtInt.of(1)) {
            return blockState;
        } else {
            if (PolymerResourcePackUtils.hasMainPack(packetContext.getPlayer())) {
                return polymerState;
            }
            return super.getPolymerBlockState(blockState,packetContext);
        }
    }

    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return changeRotation(state, rotation);
    }

    public static BlockState changeRotation(BlockState state, BlockRotation rotation) {
        switch (rotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch ((Direction.Axis)state.get(AXIS)) {
                    case X: return (BlockState)state.with(AXIS, Direction.Axis.Z);
                    case Z : return (BlockState)state.with(AXIS, Direction.Axis.X);
                    default: return state;
                }
            default:
                return state;
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AXIS});
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(AXIS, ctx.getSide().getAxis());
    }

    static {
        AXIS = Properties.AXIS;
    }
}
