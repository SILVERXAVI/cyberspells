/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.state.BlockState
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
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MetalDetectorItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final int ENERGY_PER_TICK = 3;

    public MetalDetectorItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
        tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.legupgrades_metaldetector.energy").withStyle(ChatFormatting.RED));
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public int getEnergyUsedPerTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return 3;
    }

    @Override
    public boolean requiresEnergyToFunction(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return true;
    }

    @Override
    public Set<TagKey<Item>> requiresCyberwareTags(ItemStack installedStack, CyberwareSlot slot) {
        return switch (slot) {
            case CyberwareSlot.RLEG -> Set.of(ModTags.Items.RIGHTLEG_REPLACEMENTS);
            case CyberwareSlot.LLEG -> Set.of(ModTags.Items.LEFTLEG_REPLACEMENTS);
            default -> Set.of();
        };
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.RLEG, CyberwareSlot.LLEG);
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

    public static DetectionResult scanForMetal(Level level, Player player) {
        BlockPos onPos = BlockPos.containing((double)player.getX(), (double)(player.getY() - 0.05), (double)player.getZ());
        int bestDy = Integer.MAX_VALUE;
        boolean bestDirectColumn = false;
        for (int dy = 0; dy <= 15; ++dy) {
            for (int dx = -1; dx <= 1; ++dx) {
                for (int dz = -1; dz <= 1; ++dz) {
                    boolean direct;
                    BlockPos checkPos = onPos.offset(dx, -dy, dz);
                    BlockState state = level.getBlockState(checkPos);
                    if (!state.is(ModTags.Blocks.METAL_DETECTABLE)) continue;
                    boolean bl = direct = dx == 0 && dz == 0;
                    if (dy >= bestDy && (dy != bestDy || !direct || bestDirectColumn)) continue;
                    bestDy = dy;
                    bestDirectColumn = direct;
                }
            }
            if (bestDy == 0 && bestDirectColumn) break;
        }
        return new DetectionResult(bestDy != Integer.MAX_VALUE, bestDy, bestDirectColumn);
    }

    public static boolean isAnyMetalDetectorPowered(Player player) {
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return false;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return false;
        }
        for (CyberwareSlot slot : Set.of(CyberwareSlot.RLEG, CyberwareSlot.LLEG)) {
            for (int i = 0; i < slot.size; ++i) {
                ItemStack st;
                InstalledCyberware cw = data.get(slot, i);
                if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || !(st.getItem() instanceof MetalDetectorItem) || !cw.isPowered()) continue;
                return true;
            }
        }
        return false;
    }

    public record DetectionResult(boolean detected, int dy, boolean direct) {
    }
}

