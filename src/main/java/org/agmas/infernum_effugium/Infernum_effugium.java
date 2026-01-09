package org.agmas.infernum_effugium;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
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
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
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
        PayloadTypeRegistry.playS2C().register(NetherPactUpdates.NetherPactModePayload.ID, NetherPactUpdates.NetherPactModePayload.CODEC);
        ModBlocks.init();
        ModItems.initialize();
        ModEntities.init();
        ModEffects.init();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("un_nether_pact").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2)).executes(context -> {
                if (context.getSource().getPlayer() != null) {
                    if (StateSaverAndLoader.getPlayerState(context.getSource().getPlayer()).netherPacted) {
                        context.getSource().getPlayer().sendMessage(Text.literal("You are no longer bound by the nether pact.").formatted(Formatting.BLUE), false);
                        NetherPactUpdates.sendHumanModeUpdate(context.getSource().getPlayer());
                    }
                    StateSaverAndLoader.getPlayerState(context.getSource().getPlayer()).netherPacted = false;
                    context.getSource().getPlayer().removeStatusEffect(ModEffects.NETHER_PACT);
                }
                return 1;
            }));
        });

        ServerLivingEntityEvents.AFTER_DAMAGE.register(((livingEntity, damageSource, v, v1, b) -> {
            if (livingEntity.isHolding(ModItems.PEBBLE_CANNON)) {
                if (livingEntity instanceof ServerPlayerEntity player) {
                    if (player.getItemCooldownManager().getCooldownProgress(ModItems.PEBBLE_CANNON, 0) <= 40 && player.isUsingItem()) {

                        Registry<Enchantment> enchantRegistry = player.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT);

                        if (player.getActiveItem().hasEnchantments()) {
                            if (EnchantmentHelper.getLevel(enchantRegistry.getEntry(enchantRegistry.get(ModEnchants.SHOTGUN)), player.getActiveItem()) != 0 ||
                                    EnchantmentHelper.getLevel(enchantRegistry.getEntry(enchantRegistry.get(ModEnchants.ENDER)), player.getActiveItem()) != 0) {
                                return;
                            }
                        }
                        player.getItemCooldownManager().set(ModItems.PEBBLE_CANNON, 40);
                        player.getWorld().playSound(player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.MASTER, 1, 1,true);
                    }
                }
            }
        }));

        ServerTickEvents.START_WORLD_TICK.register((serverWorld -> {
            serverWorld.getPlayers().forEach((p)->{
                if (StateSaverAndLoader.getPlayerState(p).netherPacted) {
                    if (!p.hasStatusEffect(ModEffects.NETHER_PACT)) {
                        p.addStatusEffect(new StatusEffectInstance(ModEffects.NETHER_PACT, Integer.MAX_VALUE, 0));
                    }
                }
                if (p.isOnGround() && p.hasStatusEffect(ModEffects.AIRBORNE)) {
                    p.removeStatusEffect(ModEffects.AIRBORNE);
                }
                if (p.isTouchingWaterOrRain() && p.hasStatusEffect(ModEffects.NETHER_PACT)) {
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
            t.add(ModItems.PEBBLE_CANNON);
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
            t.add(ModBlocks.BLACKSTONE_PILLAR);
            t.add(ModBlocks.GILDED_BLACKSTONE_PILLAR);
            t.add(ModBlocks.ROCKY_BUSH);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SEARCH).register((t)->{
            t.add(ModItems.BLACKSTONE_PEBBLE);
            t.add(ModItems.NETHER_PACT);
            t.add(ModBlocks.CHISELED_BEDROCK);
            t.add(ModBlocks.BEDROCK_BRICKS);
            t.add(ModBlocks.BEDROCK_LADDER);
            t.add(ModBlocks.BEDROCK_DISPENSER);
            t.add(ModBlocks.CORRUPTED_BEDROCK_BRICKS);
            t.add(ModBlocks.BLACKSTONE_PILLAR);
            t.add(ModBlocks.GILDED_BLACKSTONE_PILLAR);
            t.add(ModBlocks.ROCKY_BUSH);
            t.add(ModItems.DEATH_WHISTLE);
            t.add(ModItems.BEDROCK_SICKLES);
            t.add(ModItems.INFERNUM_MACE);
            t.add(ModItems.NETHERITE_INFUSED_BEDROCK_SICKLES);
            t.add(ModItems.PEBBLE_CANNON);
        });


    }
}
