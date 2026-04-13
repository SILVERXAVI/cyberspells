/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.EntityTeleportEvent
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;

public class EnderJammerItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final double JAM_RADIUS = 10.0;
    private static final double JAM_RADIUS_SQ = 100.0;
    private static final int ENERGY_PER_TICK = 5;

    public EnderJammerItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.brainupgrades_enderjammer.energy").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public boolean requiresEnergyToFunction(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return true;
    }

    @Override
    public int getEnergyUsedPerTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return 5;
    }

    @Override
    public Set<TagKey<Item>> requiresCyberwareTags(ItemStack installedStack, CyberwareSlot slot) {
        return Set.of(ModTags.Items.BRAIN_ITEMS);
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.BRAIN);
    }

    @Override
    public boolean replacesOrgan() {
        return false;
    }

    @Override
    public Set<CyberwareSlot> getReplacedOrgans() {
        return Set.of();
    }

    private static boolean hasEnderJammerInstalledAndPowered(ServerPlayer player) {
        if (player == null) {
            return false;
        }
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return false;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return false;
        }
        InstalledCyberware[] arr = data.getAll().get((Object)CyberwareSlot.BRAIN);
        if (arr == null) {
            return false;
        }
        for (int idx = 0; idx < arr.length; ++idx) {
            ItemStack st;
            InstalledCyberware installed = arr[idx];
            if (installed == null || (st = installed.getItem()) == null || st.isEmpty() || !st.is((Item)ModItems.BRAINUPGRADES_ENDERJAMMER.get())) continue;
            if (!data.isEnabled(CyberwareSlot.BRAIN, idx)) {
                return false;
            }
            return installed.isPowered();
        }
        return false;
    }

    private static boolean isPointProtected(ServerLevel level, Vec3 point) {
        if (level == null || point == null) {
            return false;
        }
        AABB box = new AABB(point.x - 10.0, point.y - 10.0, point.z - 10.0, point.x + 10.0, point.y + 10.0, point.z + 10.0);
        List players = level.getEntitiesOfClass(ServerPlayer.class, box, EnderJammerItem::hasEnderJammerInstalledAndPowered);
        if (players.isEmpty()) {
            return false;
        }
        for (ServerPlayer p : players) {
            if (!(p.position().distanceToSqr(point) <= 100.0)) continue;
            return true;
        }
        return false;
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class Events {
        @SubscribeEvent
        public static void onAnyEntityTeleport(EntityTeleportEvent event) {
            Entity entity = event.getEntity();
            if (entity == null) {
                return;
            }
            Level level = entity.level();
            if (!(level instanceof ServerLevel)) {
                return;
            }
            ServerLevel level2 = (ServerLevel)level;
            Vec3 prev = event.getPrev();
            Vec3 target = event.getTarget();
            if (EnderJammerItem.isPointProtected(level2, prev) || EnderJammerItem.isPointProtected(level2, target)) {
                event.setCanceled(true);
            }
        }
    }
}

