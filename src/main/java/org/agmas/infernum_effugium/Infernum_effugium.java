package org.agmas.infernum_effugium;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.agmas.infernum_effugium.state.StateSaverAndLoader;
import org.agmas.infernum_effugium.status_effects.ExtremeFireStatusEffect;
import org.agmas.infernum_effugium.status_effects.NetherPactStatusEffect;

public class Infernum_effugium implements ModInitializer {

    public static String MOD_ID = "infernumeffugium";
    public static final StatusEffect EXTREME_FIRE = new ExtremeFireStatusEffect();
    public static final StatusEffect NETHER_PACT = new NetherPactStatusEffect();



    @Override
    public void onInitialize() {
        ModBlocks.init();
        ModItems.initialize();
        ModEntities.init();
        Registry.register(Registries.STATUS_EFFECT, new Identifier(MOD_ID, "extreme_fire"), EXTREME_FIRE);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(MOD_ID, "nether_pact"), NETHER_PACT);

        ServerTickEvents.START_WORLD_TICK.register((serverWorld -> {
            serverWorld.getPlayers().forEach((p)->{
                if (StateSaverAndLoader.getPlayerState(p).netherPacted) {
                    if (!p.hasStatusEffect(NETHER_PACT)) {
                        p.addStatusEffect(new StatusEffectInstance(NETHER_PACT, Integer.MAX_VALUE, 0));
                    }
                }
                if (p.isTouchingWaterOrRain() && p.hasStatusEffect(NETHER_PACT)) {
                    p.damage(p.getDamageSources().drown(), 1);
                }
            });
        }));

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
