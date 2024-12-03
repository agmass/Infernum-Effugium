package org.agmas.infernum_effugium.block.blockEntities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.agmas.infernum_effugium.ModEntities;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class GreedVaultBlockEntity extends BlockEntity {
    public ArrayList<Item> itemsLeft = new ArrayList<Item>();
    public int currentStage = 0;
    public GreedVaultBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        resetItems();
    }
    public GreedVaultBlockEntity(BlockPos pos, BlockState state) {
        super(ModEntities.GREED_VAULT, pos, state);
        itemsLeft.add(Items.BLAZE_ROD);
        itemsLeft.add(Items.GHAST_TEAR);
        itemsLeft.add(Items.NETHER_STAR);
        itemsLeft.add(Items.NETHERITE_SCRAP);
    }

    public void resetItems() {
        itemsLeft.clear();
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putInt("stage", currentStage);
        super.writeNbt(nbt, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        currentStage = nbt.getInt("stage");
        super.readNbt(nbt, registryLookup);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

}
