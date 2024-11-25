package org.agmas.infernum_effugium.block;

import com.mojang.serialization.MapCodec;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.block.blockEntities.BushBedrockDispenserBlockEntity;

public class BedrockDispenserBush extends DispenserBlock implements PolymerBlock {
    public BedrockDispenserBush(Settings settings) {
        super(settings);
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
    public MapCodec<? extends BedrockDispenserBush> getCodec() {
        return createCodec(BedrockDispenserBush::new);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BushBedrockDispenserBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }


    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity && entity.getType() != EntityType.FOX && entity.getType() != EntityType.BEE) {
            entity.slowMovement(state, new Vec3d(0.8F, 0.75, 0.8F));
            if (!world.isClient && (entity.lastRenderX != entity.getX() || entity.lastRenderZ != entity.getZ())) {
                double d = Math.abs(entity.getX() - entity.lastRenderX);
                double e = Math.abs(entity.getZ() - entity.lastRenderZ);
                if (d >= 0.003F || e >= 0.003F) {
                    entity.damage(world.getDamageSources().sweetBerryBush(), 2.0F);
                }
            }
        }
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState) {
        return Blocks.DEAD_BUSH.getDefaultState();
    }
}
