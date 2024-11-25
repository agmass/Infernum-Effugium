package org.agmas.infernum_effugium.block;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DeadBushBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtInt;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModEntities;

public class DeadBedrockBush extends DeadBushBlock implements PolymerBlock {
    public DeadBedrockBush(Settings settings) {
        super(settings);
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
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isIn(TagKey.of(RegistryKeys.BLOCK, Identifier.of("infernumeffugium", "bedrock_blocks"))) || super.canPlantOnTop(floor,world,pos);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState) {
        return Blocks.DEAD_BUSH.getDefaultState();
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, ServerPlayerEntity player) {
        if (player == null) return Blocks.DEAD_BUSH.getDefaultState();
        if (PolymerServerNetworking.getMetadata(player.networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) == NbtInt.of(1)) {
            return state;
        } else {
            return Blocks.DEAD_BUSH.getDefaultState();
        }
    }
}
