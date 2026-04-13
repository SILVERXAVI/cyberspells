/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.Level$ExplosionInteraction
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.living.LivingDeathEvent
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.advancement.ModCriteria;
import com.perigrine3.createcybernetics.advancement.triggers.DestroyerOfWorldsTrigger;
import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public class CreeperheartItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;

    public CreeperheartItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
        }
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public Set<Item> requiresCyberware(ItemStack installedStack, CyberwareSlot slot) {
        return Set.of((Item)ModItems.BODYPART_HEART.get());
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.HEART);
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
    public void onInstalled(Player player) {
    }

    @Override
    public void onRemoved(Player player) {
    }

    @Override
    public void onTick(Player player) {
    }

    @Override
    public boolean dropsOnDeath(ItemStack installedStack, CyberwareSlot slot) {
        return false;
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class Events {
        @SubscribeEvent
        public static void onLivingDeath(LivingDeathEvent event) {
            LivingEntity livingEntity = event.getEntity();
            if (!(livingEntity instanceof Player)) {
                return;
            }
            Player player = (Player)livingEntity;
            if (player.level().isClientSide) {
                return;
            }
            Level level = player.level();
            if (!(level instanceof ServerLevel)) {
                return;
            }
            ServerLevel level2 = (ServerLevel)level;
            Entity killer = event.getSource().getEntity();
            if (killer == null) {
                return;
            }
            PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            if (data.hasSpecificItem((Item)ModItems.HEARTUPGRADES_CREEPERHEART.get(), CyberwareSlot.HEART)) {
                if (data.hasSpecificItem((Item)ModItems.ORGANSUPGRADES_MAGICCATALYST.get(), CyberwareSlot.ORGANS)) {
                    level2.explode((Entity)player, player.getX(), player.getY() - 2.0, player.getZ(), 50.0f, true, Level.ExplosionInteraction.MOB);
                    LivingEntity livingEntity2 = event.getEntity();
                    if (!(livingEntity2 instanceof ServerPlayer)) {
                        return;
                    }
                    ServerPlayer sp = (ServerPlayer)livingEntity2;
                    ((DestroyerOfWorldsTrigger)((Object)ModCriteria.DESTROYER_OF_WORLDS.get())).trigger(sp);
                } else if (data.hasSpecificItem((Item)ModItems.ORGANSUPGRADES_DUALISTICCONVERTER.get(), CyberwareSlot.ORGANS)) {
                    level2.explode((Entity)player, player.getX(), player.getY() - 2.0, player.getZ(), 25.0f, true, Level.ExplosionInteraction.MOB);
                } else {
                    level2.explode((Entity)player, player.getX(), player.getY(), player.getZ(), 6.0f, false, Level.ExplosionInteraction.MOB);
                }
            }
        }

        private Events() {
        }
    }
}

