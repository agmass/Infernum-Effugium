package org.agmas.infernum_effugium;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.core.api.block.SimplePolymerBlock;
import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.agmas.infernum_effugium.block.*;

public class ModBlocks {

    public static final Block BEDROCK_LADDER = register(
            new PolymerLadderBlock(AbstractBlock.Settings.create().notSolid().strength(-1.0F, 3600000.0F).sounds(BlockSoundGroup.STONE).pistonBehavior(PistonBehavior.BLOCK).nonOpaque()),
            "bedrock_ladder",
            true,
            Items.LADDER
    );
    public static final Block BEDROCK_BRICKS = register(
            new TwoSidedPolymerBlock(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).sounds(BlockSoundGroup.STONE).pistonBehavior(PistonBehavior.BLOCK).mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).dropsNothing().allowsSpawning(Blocks::never), Blocks.DEEPSLATE_BRICKS,"bedrock_bricks"),
            "bedrock_bricks",
            true,
            Items.DEEPSLATE_BRICKS
    );
    public static final Block CORRUPTED_BEDROCK_BRICKS = register(
            new TwoSidedPolymerBlock(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).sounds(BlockSoundGroup.STONE).pistonBehavior(PistonBehavior.BLOCK).mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).dropsNothing().allowsSpawning(Blocks::never), Blocks.DEEPSLATE_BRICKS,"corrupted_bedrock_bricks"),
            "corrupted_bedrock_bricks",
            true,
            Items.DEEPSLATE_BRICKS
    );
    public static final Block CHISELED_BEDROCK = register(
            new TwoSidedPolymerBlock(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).sounds(BlockSoundGroup.STONE).pistonBehavior(PistonBehavior.BLOCK).mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).dropsNothing().allowsSpawning(Blocks::never), Blocks.DEEPSLATE_BRICKS,"chiseled_bedrock"),
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


    public static Block register(Block block, String name, boolean shouldRegisterItem, Item polyblock) {
        Identifier id = Identifier.of(Infernum_effugium.MOD_ID, name);

        if (shouldRegisterItem) {
            BlockItem blockItem = new TwoSidedPolymerBlockItem(block, new Item.Settings(), polyblock);
            Registry.register(Registries.ITEM, id, blockItem);
        }

        return Registry.register(Registries.BLOCK, id, block);
    }

    public static void init() {}
}