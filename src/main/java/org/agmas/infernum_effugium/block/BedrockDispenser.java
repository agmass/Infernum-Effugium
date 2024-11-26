package org.agmas.infernum_effugium.block;

import com.mojang.serialization.MapCodec;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.core.api.utils.PolymerClientDecoded;
import eu.pb4.polymer.core.api.utils.PolymerKeepModel;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtInt;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.block.blockEntities.BedrockDispenserBlockEntity;

public class BedrockDispenser extends DispenserBlock implements PolymerBlock, PolymerKeepModel, PolymerClientDecoded {
    public BedrockDispenser(Settings settings) {
        super(settings);
    }

    @Override
    public MapCodec<? extends BedrockDispenser> getCodec() {
        return createCodec(BedrockDispenser::new);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BedrockDispenserBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            if (player instanceof ServerPlayerEntity spe) {
                if (spe.interactionManager.isSurvivalLike()) {
                    return ActionResult.FAIL;
                }
            }
        }
        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState) {
        return Blocks.DISPENSER.getDefaultState();
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, ServerPlayerEntity player) {
        if (player == null) return Blocks.DISPENSER.getDefaultState();
        if (PolymerServerNetworking.getMetadata(player.networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) == NbtInt.of(1)) {
            return state;
        } else {
            return Blocks.DISPENSER.getDefaultState();
        }
    }
}
