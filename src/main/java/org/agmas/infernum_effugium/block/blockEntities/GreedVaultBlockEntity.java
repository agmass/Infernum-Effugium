package org.agmas.infernum_effugium.block.blockEntities;

import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.agmas.infernum_effugium.ModEntities;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class GreedVaultBlockEntity extends BlockEntity {
    public ArrayList<Item> itemsLeft = new ArrayList<Item>();
    public int currentStage = 0;
    public GreedVaultBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        itemsLeft.add(Items.BLAZE_ROD);
        itemsLeft.add(Items.GHAST_TEAR);
        itemsLeft.add(Items.NETHER_STAR);
        itemsLeft.add(Items.NETHERITE_SCRAP);
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
        super.readNbt(nbt, registryLookup);
        currentStage = nbt.getInt("stage");
    }
    public void incrementStage() {
        currentStage++;
        Log.info(LogCategory.GENERAL, getCurrentStage()+"");
        markDirty();
        this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
    }

    public int getCurrentStage() {
        return currentStage;
    }


    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putInt("stage", currentStage);
        return nbtCompound;
    }

}
