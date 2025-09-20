package org.agmas.infernum_effugium;

import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.agmas.infernum_effugium.block.*;

import java.util.function.Function;

public class ModBlocks {

    public static final Block BEDROCK_LADDER = register(
            new LadderBlock(AbstractBlock.Settings.create().notSolid().strength(-1.0F, 3600000.0F).sounds(BlockSoundGroup.STONE).pistonBehavior(PistonBehavior.BLOCK).nonOpaque()),
            "bedrock_ladder",
            true,
            Items.LADDER
    );
    public static final Block BEDROCK_BRICKS = register(
            new Block(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).sounds(BlockSoundGroup.STONE).pistonBehavior(PistonBehavior.BLOCK).mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).dropsNothing().allowsSpawning(Blocks::never)),
            "bedrock_bricks",
            true,
            Items.DEEPSLATE_BRICKS
    );
    public static final Block CORRUPTED_BEDROCK_BRICKS = register(
            new Block(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).sounds(BlockSoundGroup.STONE).pistonBehavior(PistonBehavior.BLOCK).mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).dropsNothing().allowsSpawning(Blocks::never)),
            "corrupted_bedrock_bricks",
            true,
            Items.DEEPSLATE_BRICKS
    );
    public static final Block CHISELED_BEDROCK = register(
            new Block(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).sounds(BlockSoundGroup.STONE).pistonBehavior(PistonBehavior.BLOCK).mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).dropsNothing().allowsSpawning(Blocks::never)),
            "chiseled_bedrock",
            true,
            Items.BEDROCK
    );
    public static final Block GREED_VAULT = register(
            new GreedVault(AbstractBlock.Settings.create().sounds(BlockSoundGroup.STONE).pistonBehavior(PistonBehavior.BLOCK).mapColor(MapColor.STONE_GRAY).nonOpaque().allowsSpawning(Blocks::never)),
            "greed_vault",
            true,
            Items.VAULT
    );
    public static final Block BEDROCK_DISPENSER = register(
            new BedrockDispenser(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).sounds(BlockSoundGroup.STONE).pistonBehavior(PistonBehavior.BLOCK).mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).dropsNothing().allowsSpawning(Blocks::never)),
            "bedrock_dispenser",
            true,
            Items.DISPENSER
    );
    public static final Block ROCKY_BUSH = register(
            new DeadBedrockBush(AbstractBlock.Settings.create().sounds(BlockSoundGroup.STONE).pistonBehavior(PistonBehavior.BLOCK).mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).noCollision().breakInstantly().allowsSpawning(Blocks::never)),
            "rocky_bush",
            true,
            Items.DEAD_BUSH
    );
    public static final Block ROCKY_BUSH_BUT_ITS_ACTUALLY_A_DISPENSER = register(
            new BedrockDispenserBush(AbstractBlock.Settings.create().sounds(BlockSoundGroup.STONE).pistonBehavior(PistonBehavior.BLOCK).mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).noCollision().breakInstantly().allowsSpawning(Blocks::never)),
            "rocky_bush_but_its_actually_a_dispenser",
            true,
            Items.DEAD_BUSH
    );

    public static final Block BLACKSTONE_MAGMA_BLOCK = register(
            new MagmaBlock(AbstractBlock.Settings.create().mapColor(MapColor.DARK_RED).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().luminance((state) -> {
                return 3;
            }).strength(0.5F).allowsSpawning((state, world, pos, entityType) -> {
                return entityType.isFireImmune();
            }).postProcess(Blocks::always).emissiveLighting(Blocks::always)),
            "blackstone_magma_block",
            true,
            Items.MAGMA_BLOCK
    );
    public static final Block BLACKSTONE_PILLAR = register(
            new PillarBlock(AbstractBlock.Settings.copy(Blocks.BLACKSTONE)),
            "blackstone_pillar",
            true,
            Items.BLACKSTONE
    );
    public static final Block GILDED_BLACKSTONE_PILLAR = register(
            new PillarBlock(AbstractBlock.Settings.copy(Blocks.BLACKSTONE).sounds(BlockSoundGroup.GILDED_BLACKSTONE)),
            "gilded_blackstone_pillar",
            true,
            Items.GILDED_BLACKSTONE
    );


    public static Block register(Block block, String name, boolean shouldRegisterItem, Item polyblock) {
        Identifier id = Identifier.of(Infernum_effugium.MOD_ID, name);

        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, id, blockItem);
        }
        return Registry.register(Registries.BLOCK, id, block);
    }




    private static RegistryKey<Item> itemkeyOf(String id) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Infernum_effugium.MOD_ID, id));
    }
    private static RegistryKey<Block> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Infernum_effugium.MOD_ID, id));
    }

    public static void init() {}
}