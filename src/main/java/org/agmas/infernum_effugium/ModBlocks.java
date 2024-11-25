package org.agmas.infernum_effugium;

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
import org.agmas.infernum_effugium.block.BedrockDispenser;
import org.agmas.infernum_effugium.block.DeadBedrockBush;

public class ModBlocks {

    public static final Block BEDROCK_LADDER = register(
            new LadderBlock(AbstractBlock.Settings.create().notSolid().strength(-1.0F, 3600000.0F).sounds(BlockSoundGroup.STONE).pistonBehavior(PistonBehavior.BLOCK).nonOpaque()),
            "bedrock_ladder",
            true
    );
    public static final Block BEDROCK_BRICKS = register(
            new Block(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).sounds(BlockSoundGroup.STONE).pistonBehavior(PistonBehavior.BLOCK).mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).dropsNothing().allowsSpawning(Blocks::never)),
            "bedrock_bricks",
            true
    );
    public static final Block CORRUPTED_BEDROCK_BRICKS = register(
            new Block(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).sounds(BlockSoundGroup.STONE).pistonBehavior(PistonBehavior.BLOCK).mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).dropsNothing().allowsSpawning(Blocks::never)),
            "corrupted_bedrock_bricks",
            true
    );
    public static final Block CHISELED_BEDROCK = register(
            new Block(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).sounds(BlockSoundGroup.STONE).pistonBehavior(PistonBehavior.BLOCK).mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).dropsNothing().allowsSpawning(Blocks::never)),
            "chiseled_bedrock",
            true
    );
    public static final Block BEDROCK_DISPENSER = register(
            new BedrockDispenser(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).sounds(BlockSoundGroup.STONE).pistonBehavior(PistonBehavior.BLOCK).mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).dropsNothing().allowsSpawning(Blocks::never)),
            "bedrock_dispenser",
            true
    );
    public static final Block ROCKY_BUSH = register(
            new DeadBedrockBush(AbstractBlock.Settings.create().sounds(BlockSoundGroup.STONE).pistonBehavior(PistonBehavior.BLOCK).mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).noCollision().breakInstantly().allowsSpawning(Blocks::never)),
            "rocky_bush",
            true
    );
    public static final Block ROCKY_BUSH_BUT_ITS_ACTUALLY_A_DISPENSER = register(
            new BedrockDispenser(AbstractBlock.Settings.create().sounds(BlockSoundGroup.STONE).pistonBehavior(PistonBehavior.BLOCK).mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).noCollision().breakInstantly().allowsSpawning(Blocks::never)),
            "rocky_bush_but_its_actually_a_dispenser",
            true
    );


    public static Block register(Block block, String name, boolean shouldRegisterItem) {
        Identifier id = Identifier.of(Infernum_effugium.MOD_ID, name);

        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, id, blockItem);
        }

        return Registry.register(Registries.BLOCK, id, block);
    }

    public static void init() {}
}