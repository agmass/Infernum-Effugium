package org.agmas.infernum_effugium.client;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.agmas.infernum_effugium.ModBlocks;
import org.agmas.infernum_effugium.block.blockEntities.GreedVaultBlockEntity;

import java.util.ArrayList;

@Environment(EnvType.CLIENT)
public class GreedVaultBlockEntityRenderer implements BlockEntityRenderer<GreedVaultBlockEntity> {
    private final ItemRenderer itemRenderer;

    public GreedVaultBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public boolean isInRenderDistance(GreedVaultBlockEntity blockEntity, Vec3d pos) {
        return BlockEntityRenderer.super.isInRenderDistance(blockEntity, pos);
    }

    @Override
    public int getRenderDistance() {
        return BlockEntityRenderer.super.getRenderDistance();
    }

    @Override
    public boolean rendersOutsideBoundingBox(GreedVaultBlockEntity blockEntity) {
        return BlockEntityRenderer.super.rendersOutsideBoundingBox(blockEntity);
    }

    public void render(GreedVaultBlockEntity greedVaultBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {

        ArrayList<Item> itemsLeft = new ArrayList<Item>();
        itemsLeft.add(Items.BLAZE_ROD);
        itemsLeft.add(Items.GHAST_TEAR);
        itemsLeft.add(Items.NETHER_STAR);
        itemsLeft.add(Items.NETHERITE_SCRAP);
        matrixStack.push();
        matrixStack.translate(0.5F, 0.5F, 0.5F);
        float g = 0.7f;


        matrixStack.scale(g, g, g);
        if (greedVaultBlockEntity.getWorld().getBlockState(greedVaultBlockEntity.getPos()).getBlock().equals(ModBlocks.GREED_VAULT)) {
            Direction direction = greedVaultBlockEntity.getWorld().getBlockState(greedVaultBlockEntity.getPos()).get(HorizontalFacingBlock.FACING);
            if (direction.equals(Direction.WEST) || direction.equals(Direction.EAST)) {
                matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(-90));
            }
        }
        if (itemsLeft.size() > greedVaultBlockEntity.getCurrentStage()) {
            this.itemRenderer.renderItem(itemsLeft.get(greedVaultBlockEntity.getCurrentStage()).getDefaultStack(), ModelTransformationMode.FIXED, 0, 0, matrixStack, vertexConsumerProvider, greedVaultBlockEntity.getWorld(), 0);

        }
        matrixStack.pop();
    }



}
