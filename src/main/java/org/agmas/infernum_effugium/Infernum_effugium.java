package org.agmas.infernum_effugium;

import eu.pb4.polymer.networking.api.PolymerNetworking;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemGroups;
import net.minecraft.nbt.NbtInt;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.agmas.infernum_effugium.status_effects.ExtremeFireStatusEffect;
import org.agmas.infernum_effugium.status_effects.NetherPactStatusEffect;
import org.agmas.infernum_effugium.util.NetherPactUpdates;

public class Infernum_effugium implements ModInitializer {

    public static String MOD_ID = "infernumeffugium";
    public static final StatusEffect EXTREME_FIRE;
    public static final StatusEffect NETHER_PACT;
    public static Identifier REGISTER_PACKET = Identifier.of("infernumeffugium", "register_packet");


    static {
        NETHER_PACT = Registry.register(Registries.STATUS_EFFECT, Identifier.of(MOD_ID, "nether_pact"), new NetherPactStatusEffect());
        EXTREME_FIRE = Registry.register(Registries.STATUS_EFFECT, Identifier.of(MOD_ID, "extreme_fire"), new ExtremeFireStatusEffect());
    }

    @Override
    public void onInitialize() {
        ModBlocks.init();
        ModItems.initialize();
        PolymerServerNetworking.setServerMetadata(REGISTER_PACKET, NbtInt.of(1));
        PolymerNetworking.registerS2CVersioned(NetherPactUpdates.NetherPactModePayload.ID, 1, NetherPactUpdates.NetherPactModePayload.CODEC);
        ModEntities.init();


        PolymerResourcePackUtils.addModAssets("infernum_effugium");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((t)->{
            t.add(ModItems.BLACKSTONE_PEBBLE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((t)->{
            t.add(ModItems.BLACKSTONE_PEBBLE);
            t.add(ModItems.NETHER_PACT);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((t)->{
            t.add(ModItems.BEDROCK_SICKLES);
            t.add(ModItems.INFERNUM_MACE);
            t.add(ModItems.NETHERITE_INFUSED_BEDROCK_SICKLES);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register((t)->{
            t.add(ModBlocks.CHISELED_BEDROCK);
            t.add(ModBlocks.BEDROCK_BRICKS);
            t.add(ModBlocks.BEDROCK_LADDER);
            t.add(ModBlocks.BEDROCK_DISPENSER);
            t.add(ModBlocks.CORRUPTED_BEDROCK_BRICKS);
            t.add(ModBlocks.ROCKY_BUSH);
        });


    }
}
