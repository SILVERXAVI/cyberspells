/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.HumanoidArm
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.SoundType
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
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public class ArmSpinneretteItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;

    public ArmSpinneretteItem(Item.Properties props, int humanityCost) {
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
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.RARM, CyberwareSlot.LARM);
    }

    @Override
    public boolean replacesOrgan() {
        return true;
    }

    @Override
    public Set<CyberwareSlot> getReplacedOrgans() {
        return Set.of(CyberwareSlot.RARM, CyberwareSlot.LARM);
    }

    @Override
    public Set<TagKey<Item>> incompatibleCyberwareTags(ItemStack installedStack, CyberwareSlot slot) {
        if (slot == CyberwareSlot.LARM) {
            return Set.of(ModTags.Items.LEFTARM_REPLACEMENTS);
        }
        if (slot == CyberwareSlot.RARM) {
            return Set.of(ModTags.Items.RIGHTARM_REPLACEMENTS);
        }
        return Set.of();
    }

    private static boolean hasSpinneretteOnSide(Player player, CyberwareSlot slot) {
        if (player == null) {
            return false;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return false;
        }
        InstalledCyberware[] arr = data.getAll().get((Object)slot);
        if (arr == null) {
            return false;
        }
        for (int i = 0; i < arr.length; ++i) {
            ItemStack st;
            InstalledCyberware cw = arr[i];
            if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || !(st.getItem() instanceof ArmSpinneretteItem) || !data.isEnabled(slot, i)) continue;
            return true;
        }
        return false;
    }

    private static boolean tryPlaceCobweb(Level level, Player player, BlockPos placePos) {
        if (!level.isInWorldBounds(placePos)) {
            return false;
        }
        if (!level.mayInteract(player, placePos)) {
            return false;
        }
        BlockState at = level.getBlockState(placePos);
        if (!at.canBeReplaced()) {
            return false;
        }
        BlockState cobweb = Blocks.COBWEB.defaultBlockState();
        level.setBlock(placePos, cobweb, 3);
        SoundType sound = cobweb.getSoundType((LevelReader)level, placePos, (Entity)player);
        level.playSound(null, placePos, sound.getPlaceSound(), SoundSource.BLOCKS, (sound.getVolume() + 1.0f) / 2.0f, sound.getPitch() * 0.8f);
        return true;
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        BlockPos placePos;
        Player player = event.getEntity();
        if (player == null || !player.isCrouching()) {
            return;
        }
        CyberwareSlot slot = ArmSpinneretteItem.slotForHand(player, event.getHand());
        if (!ArmSpinneretteItem.hasSpinneretteOnSide(player, slot)) {
            return;
        }
        Level level = event.getLevel();
        if (level.isClientSide) {
            return;
        }
        BlockPos clicked = event.getPos();
        BlockPos blockPos = placePos = level.getBlockState(clicked).canBeReplaced() ? clicked : clicked.relative(event.getFace());
        if (ArmSpinneretteItem.tryPlaceCobweb(level, player, placePos)) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.CONSUME);
        }
    }

    @SubscribeEvent
    public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        BlockPos placePos;
        Player player = event.getEntity();
        if (player == null || !player.isCrouching()) {
            return;
        }
        CyberwareSlot slot = ArmSpinneretteItem.slotForHand(player, event.getHand());
        if (!ArmSpinneretteItem.hasSpinneretteOnSide(player, slot)) {
            return;
        }
        Level level = player.level();
        if (level.isClientSide) {
            return;
        }
        BlockPos base = event.getTarget().blockPosition();
        BlockPos blockPos = placePos = level.getBlockState(base).canBeReplaced() ? base : base.above();
        if (ArmSpinneretteItem.tryPlaceCobweb(level, player, placePos)) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.CONSUME);
        }
    }

    private static HumanoidArm armForHand(Player player, InteractionHand hand) {
        HumanoidArm main = player.getMainArm();
        if (hand == InteractionHand.MAIN_HAND) {
            return main;
        }
        return main == HumanoidArm.RIGHT ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
    }

    private static CyberwareSlot slotForHand(Player player, InteractionHand hand) {
        return ArmSpinneretteItem.armForHand(player, hand) == HumanoidArm.LEFT ? CyberwareSlot.LARM : CyberwareSlot.RARM;
    }
}

