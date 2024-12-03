package org.agmas.infernum_effugium.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.DropperBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.ModItems;
import org.agmas.infernum_effugium.block.blockEntities.GreedVaultBlockEntity;
import org.jetbrains.annotations.Nullable;

public class GreedVault extends BlockWithEntity {
    public static final DirectionProperty FACING;

    public GreedVault(Settings settings) {
        super(settings);
        this.setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
    }


    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof GreedVaultBlockEntity greedVaultBlockEntity) {
                if (greedVaultBlockEntity.itemsLeft.get(greedVaultBlockEntity.currentStage).equals(player.getMainHandStack().getItem())) {
                    greedVaultBlockEntity.currentStage++;
                    greedVaultBlockEntity.markDirty();
                    world.updateListeners(pos, state, state, 0);
                    if (greedVaultBlockEntity.currentStage >= greedVaultBlockEntity.itemsLeft.size()) {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState());
                        world.spawnEntity(new ItemEntity(world,pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5, ModItems.NETHER_PACT.getDefaultStack(),0,0.35,0));
                        world.playSoundAtBlockCenter(pos, SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.BLOCKS,1,0.5f,true);
                    }
                }
            }

            return ActionResult.CONSUME;
        }
    }


    static {
        FACING = HorizontalFacingBlock.FACING;
    }


    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GreedVaultBlockEntity(pos, state);
    }

}
