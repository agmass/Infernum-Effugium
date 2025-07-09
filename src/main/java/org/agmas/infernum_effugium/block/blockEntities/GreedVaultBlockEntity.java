package org.agmas.infernum_effugium.block.blockEntities;

import eu.pb4.polymer.core.api.utils.PolymerClientDecoded;
import eu.pb4.polymer.core.api.utils.PolymerSyncedObject;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
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
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.ArrayList;

public class GreedVaultBlockEntity extends BlockEntity implements PolymerSyncedObject<BlockEntity>, PolymerClientDecoded {
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

    @Override
    public boolean canSyncRawToClient(@Nullable ServerPlayerEntity player) {
        return true;
    }

    @Override
    public BlockEntity getPolymerReplacement(ServerPlayerEntity serverPlayerEntity) {
        if (serverPlayerEntity == null) return null;
        if (PolymerServerNetworking.getMetadata(serverPlayerEntity.networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) == NbtInt.of(1)) {
            return this;
        } else {
            return null;
        }
    }
}
