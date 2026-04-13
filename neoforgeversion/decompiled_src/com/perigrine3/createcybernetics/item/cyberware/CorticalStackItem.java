/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.component.CustomData
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.item.generic.XPCapsuleItem;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;

public class CorticalStackItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;

    public CorticalStackItem(Item.Properties props, int humanityCost) {
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

    @Override
    public Set<Item> incompatibleCyberware(ItemStack installedStack, CyberwareSlot slot) {
        if (ModItems.BRAINUPGRADES_CONSCIOUSNESSTRANSMITTER != null) {
            return Set.of((Item)ModItems.BRAINUPGRADES_CONSCIOUSNESSTRANSMITTER.get());
        }
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
        if (player.level().isClientSide) {
            return;
        }
    }

    @Override
    public boolean dropsOnDeath(ItemStack installedStack, CyberwareSlot slot) {
        return false;
    }

    @Override
    public void onInstalled(Player player, ItemStack installedStack) {
        if (player.level().isClientSide) {
            return;
        }
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer sp = (ServerPlayer)player;
        if (installedStack == null || installedStack.isEmpty()) {
            return;
        }
        int xp = XPCapsuleItem.getStoredXp(installedStack);
        if (xp > 0) {
            sp.giveExperiencePoints(xp);
        }
        CorticalStackItem.clearXpPayload(installedStack);
    }

    private static void clearXpPayload(ItemStack st) {
        CustomData cd = (CustomData)st.get(DataComponents.CUSTOM_DATA);
        if (cd != null && !cd.isEmpty()) {
            CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)st, tag -> {
                tag.remove("cc_xp_capsule_points");
                tag.remove("cc_xp_capsule_owner");
            });
            CustomData after = (CustomData)st.get(DataComponents.CUSTOM_DATA);
            if (after == null || after.isEmpty()) {
                st.remove(DataComponents.CUSTOM_DATA);
            }
        }
        st.remove(DataComponents.CUSTOM_NAME);
    }
}

