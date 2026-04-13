/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.BaseFireBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$EntityInteract
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$RightClickBlock
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class FirestarterItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final int ENERGY_COST_PER_USE = 3;
    private static final int FIRE_SECONDS_ON_ENTITY = 100;
    private static final ConcurrentHashMap<UUID, Long> LAST_USE_TICK = new ConcurrentHashMap();

    public FirestarterItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.armupgrades_firestarter.energy").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public Set<TagKey<Item>> requiresCyberwareTags(ItemStack installedStack, CyberwareSlot slot) {
        return switch (slot) {
            case CyberwareSlot.RLEG -> Set.of(ModTags.Items.RIGHTARM_ITEMS);
            case CyberwareSlot.LLEG -> Set.of(ModTags.Items.LEFTARM_ITEMS);
            default -> Set.of();
        };
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.LARM, CyberwareSlot.RARM);
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

    private static InstalledRef findEnabledInstalledFirestarter(ServerPlayer player, PlayerCyberwareData data) {
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            CyberwareSlot slot = entry.getKey();
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (int i = 0; i < arr.length; ++i) {
                ItemStack st;
                InstalledCyberware inst = arr[i];
                if (inst == null || (st = inst.getItem()) == null || st.isEmpty() || !(st.getItem() instanceof FirestarterItem) || !data.isEnabled(slot, i)) continue;
                return new InstalledRef(slot, i);
            }
        }
        return null;
    }

    private static boolean canPlaceFire(Level level, BlockPos pos) {
        if (!level.isInWorldBounds(pos)) {
            return false;
        }
        if (!level.getBlockState(pos).isAir()) {
            return false;
        }
        BlockState fireState = BaseFireBlock.getState((BlockGetter)level, (BlockPos)pos);
        if (fireState == null) {
            return false;
        }
        return fireState.canSurvive((LevelReader)level, pos);
    }

    private static boolean tryConsumeOncePerTick(ServerPlayer sp) {
        long tick = sp.level().getGameTime();
        UUID id = sp.getUUID();
        Long last = LAST_USE_TICK.get(id);
        if (last != null && last == tick) {
            return false;
        }
        LAST_USE_TICK.put(id, tick);
        return true;
    }

    private record InstalledRef(CyberwareSlot slot, int index) {
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class Events {
        @SubscribeEvent
        public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            Player player = event.getEntity();
            if (!(player instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer sp = (ServerPlayer)player;
            if (event.getHand() != InteractionHand.MAIN_HAND) {
                return;
            }
            if (!sp.hasData(ModAttachments.CYBERWARE)) {
                return;
            }
            PlayerCyberwareData data = (PlayerCyberwareData)sp.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            if (FirestarterItem.findEnabledInstalledFirestarter(sp, data) == null) {
                return;
            }
            BlockPos placePos = event.getPos().relative(event.getFace());
            if (!FirestarterItem.canPlaceFire(sp.level(), placePos)) {
                return;
            }
            if (!data.tryConsumeEnergy(3)) {
                return;
            }
            if (!FirestarterItem.tryConsumeOncePerTick(sp)) {
                return;
            }
            BlockState fireState = BaseFireBlock.getState((BlockGetter)sp.level(), (BlockPos)placePos);
            if (fireState == null || !fireState.canSurvive((LevelReader)sp.level(), placePos)) {
                return;
            }
            sp.level().setBlock(placePos, fireState, 11);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }

        @SubscribeEvent
        public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
            Player player = event.getEntity();
            if (!(player instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer sp = (ServerPlayer)player;
            if (event.getHand() != InteractionHand.MAIN_HAND) {
                return;
            }
            Entity target = event.getTarget();
            if (target == null) {
                return;
            }
            if (target.fireImmune()) {
                return;
            }
            if (target.isOnFire()) {
                return;
            }
            if (!sp.hasData(ModAttachments.CYBERWARE)) {
                return;
            }
            PlayerCyberwareData data = (PlayerCyberwareData)sp.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            if (FirestarterItem.findEnabledInstalledFirestarter(sp, data) == null) {
                return;
            }
            if (!data.tryConsumeEnergy(3)) {
                return;
            }
            if (!FirestarterItem.tryConsumeOncePerTick(sp)) {
                return;
            }
            target.setRemainingFireTicks(100);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }
}

