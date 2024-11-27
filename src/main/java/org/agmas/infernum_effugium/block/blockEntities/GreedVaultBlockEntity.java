package org.agmas.infernum_effugium.block.blockEntities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.agmas.infernum_effugium.ModEntities;

public class GreedVaultBlockEntity extends BlockEntity {
    private DefaultedList<Item> itemsLeft = DefaultedList.ofSize(4, Items.AIR);
    public GreedVaultBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public GreedVaultBlockEntity(BlockPos pos, BlockState state) {
        super(ModEntities.GREED_VAULT, pos, state);
    }

    public void resetItems() {
        itemsLeft.set(0, Items.ENCHANTED_GOLDEN_APPLE);
        itemsLeft.set(1, Items.COBBLESTONE_WALL);
        itemsLeft.set(2, Items.BEACON);
        itemsLeft.set(3, Items.NETHERITE_BLOCK);
    }

}
