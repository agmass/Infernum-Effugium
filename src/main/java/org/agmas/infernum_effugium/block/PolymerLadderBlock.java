package org.agmas.infernum_effugium.block;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.utils.PolymerClientDecoded;
import eu.pb4.polymer.core.api.utils.PolymerKeepModel;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtInt;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import org.agmas.infernum_effugium.Infernum_effugium;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class PolymerLadderBlock extends LadderBlock implements PolymerBlock, PolymerKeepModel, PolymerClientDecoded {
    public PolymerLadderBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        if (packetContext.getPlayer() == null) return Blocks.LADDER.getDefaultState();
        if (PolymerServerNetworking.getMetadata(packetContext.getPlayer().networkHandler, Infernum_effugium.REGISTER_PACKET, NbtInt.TYPE) == NbtInt.of(1)) {
            return blockState;
        } else {
            return Blocks.LADDER.getDefaultState();
        }
    }
}
