/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerBossEvent
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.biome.Biomes
 *  net.minecraft.world.level.levelgen.structure.Structure
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.ModList
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.living.LivingDamageEvent$Post
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.util.CyberwareAttributeHelper;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class ManaSkinItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final String ISB_MODID = "irons_spellbooks";
    private static final String TAG_INSTALLED = "cc_has_mana_skin";
    private static final String TAG_LAST_TICK = "cc_mana_skin_last_spellhit_tick";
    private static final TagKey<Structure> ISB_STRUCTURES = TagKey.create((ResourceKey)Registries.STRUCTURE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"structures"));

    public ManaSkinItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.skinupgrades_manaskin.energy").withStyle(ChatFormatting.DARK_GREEN));
        }
    }

    @Override
    public int getEnergyGeneratedPerTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        ServerLevel sl;
        Optional key;
        ServerPlayer sp;
        if (player.level().isClientSide) {
            return 0;
        }
        if (player instanceof ServerPlayer && ManaSkinItem.hasBossBar(sp = (ServerPlayer)player)) {
            return 50;
        }
        if (player instanceof ServerPlayer && ManaSkinItem.isInsideIronsStructure(sp = (ServerPlayer)player)) {
            return 10;
        }
        Level level = player.level();
        if (level instanceof ServerLevel && (key = (sl = (ServerLevel)level).getBiome(player.blockPosition()).unwrapKey()).isPresent() && ((ResourceKey)key.get()).equals(Biomes.DARK_FOREST)) {
            return 7;
        }
        return 3;
    }

    private static boolean hasBossBar(ServerPlayer player) {
        MinecraftServer server = player.server;
        if (server == null) {
            return false;
        }
        for (ServerBossEvent event : server.getCustomBossEvents().getEvents()) {
            if (!event.getPlayers().contains(player)) continue;
            return true;
        }
        return false;
    }

    private static boolean isInsideIronsStructure(ServerPlayer player) {
        if (!ModList.get().isLoaded(ISB_MODID)) {
            return false;
        }
        ServerLevel level = player.serverLevel();
        BlockPos pos = player.blockPosition();
        try {
            return level.structureManager().getStructureWithPieceAt(pos, ISB_STRUCTURES).isValid();
        }
        catch (Throwable ignored) {
            return false;
        }
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.SKIN);
    }

    @Override
    public boolean replacesOrgan() {
        return false;
    }

    @Override
    public Set<CyberwareSlot> getReplacedOrgans() {
        return Set.of();
    }

    @Override
    public int maxStacksPerSlotType(ItemStack stack, CyberwareSlot slotType) {
        return 1;
    }

    @Override
    public void onInstalled(Player player) {
        CyberwareAttributeHelper.applyModifier((LivingEntity)player, "irons_spell_resist_manaskin");
        player.getPersistentData().putBoolean(TAG_INSTALLED, true);
    }

    @Override
    public void onRemoved(Player player) {
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "irons_spell_resist_manaskin");
        player.getPersistentData().remove(TAG_INSTALLED);
        player.getPersistentData().remove(TAG_LAST_TICK);
    }

    @Override
    public void onTick(Player player) {
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class ManaSkinSpellHitHandler {
        @SubscribeEvent
        public static void onLivingHurt(LivingDamageEvent.Post event) {
            LivingEntity livingEntity = event.getEntity();
            if (!(livingEntity instanceof Player)) {
                return;
            }
            Player player = (Player)livingEntity;
            if (player.level().isClientSide) {
                return;
            }
            if (!player.getPersistentData().getBoolean(ManaSkinItem.TAG_INSTALLED)) {
                return;
            }
            if (!ModList.get().isLoaded(ManaSkinItem.ISB_MODID)) {
                return;
            }
            int tick = player.tickCount;
            int last = player.getPersistentData().getInt(ManaSkinItem.TAG_LAST_TICK);
            if (last == tick) {
                return;
            }
            DamageSource src = player.getLastDamageSource();
            if (!ManaSkinSpellHitHandler.isIronsSpellDamage(src)) {
                return;
            }
            player.getPersistentData().putInt(ManaSkinItem.TAG_LAST_TICK, tick);
            if (!player.hasData(ModAttachments.CYBERWARE)) {
                return;
            }
            PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            data.receiveEnergy(player, 35);
        }

        private static boolean isIronsSpellDamage(DamageSource src) {
            if (src == null) {
                return false;
            }
            return ManaSkinSpellHitHandler.isIronsEntity(src.getDirectEntity()) || ManaSkinSpellHitHandler.isIronsEntity(src.getEntity());
        }

        private static boolean isIronsEntity(Entity e) {
            if (e == null) {
                return false;
            }
            ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey((Object)e.getType());
            return id != null && ManaSkinItem.ISB_MODID.equals(id.getNamespace());
        }
    }
}

