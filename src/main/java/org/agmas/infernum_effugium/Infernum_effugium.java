package org.agmas.infernum_effugium;

import eu.pb4.polymer.networking.api.PolymerNetworking;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtInt;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.agmas.infernum_effugium.state.StateSaverAndLoader;
import org.agmas.infernum_effugium.status_effects.ExtremeFireStatusEffect;
import org.agmas.infernum_effugium.status_effects.NetherPactStatusEffect;
import org.agmas.infernum_effugium.util.NetherPactUpdates;

public class Infernum_effugium implements ModInitializer {

    public static String MOD_ID = "infernumeffugium";
    public static Identifier REGISTER_PACKET = Identifier.of("infernumeffugium", "register_packet");


    @Override
    public void onInitialize() {
        ModBlocks.init();
        ModItems.initialize();
        PolymerServerNetworking.setServerMetadata(REGISTER_PACKET, NbtInt.of(1));
        PolymerNetworking.registerS2CVersioned(NetherPactUpdates.NetherPactModePayload.ID, 1, NetherPactUpdates.NetherPactModePayload.CODEC);
        ModEntities.init();
        ModEffects.init();


        ServerTickEvents.START_WORLD_TICK.register((serverWorld -> {
            serverWorld.getPlayers().forEach((p)->{
                if (StateSaverAndLoader.getPlayerState(p).netherPacted) {
                    if (!p.hasStatusEffect(ModEffects.NETHER_PACT)) {
                        p.addStatusEffect(new StatusEffectInstance(ModEffects.NETHER_PACT, Integer.MAX_VALUE, 0));
                    }
                }
                if (p.isTouchingWaterOrRain() && p.hasStatusEffect(ModEffects.NETHER_PACT)) {
                    p.damage(serverWorld, p.getDamageSources().drown(), 1);
                }
            });
        }));

        ServerLivingEntityEvents.ALLOW_DEATH.register(((livingEntity, damageSource, v) -> {
            if (damageSource.isOf(DamageTypes.FREEZE)) {
                if (StateSaverAndLoader.getPlayerState(livingEntity).netherPacted) {
                    if (livingEntity instanceof PlayerEntity player) {
                        player.sendMessage(Text.literal("You are no longer bound by the nether pact.").formatted(Formatting.BLUE), false);
                        NetherPactUpdates.sendHumanModeUpdate(player);
                    }
                    StateSaverAndLoader.getPlayerState(livingEntity).netherPacted = false;
                    livingEntity.removeStatusEffect(ModEffects.NETHER_PACT);
                }
            }
            return true;
        }));
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
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((t)->{
            t.add(ModItems.DEATH_WHISTLE);
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
