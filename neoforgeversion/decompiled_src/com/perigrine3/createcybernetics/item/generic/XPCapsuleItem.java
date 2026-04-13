/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 */
package com.perigrine3.createcybernetics.item.generic;

import com.perigrine3.createcybernetics.item.ModItems;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class XPCapsuleItem
extends Item {
    public static final String NBT_XP_POINTS = "cc_xp_capsule_points";
    public static final String NBT_OWNER = "cc_xp_capsule_owner";

    public XPCapsuleItem(Item.Properties props) {
        super(props);
    }

    public static ItemStack makeCapsule(String ownerName, int xpPoints) {
        ItemStack stack = new ItemStack((ItemLike)ModItems.XP_CAPSULE.get());
        int clamped = Math.max(0, xpPoints);
        CustomData.update((DataComponentType)DataComponents.CUSTOM_DATA, (ItemStack)stack, tag -> {
            tag.putInt(NBT_XP_POINTS, clamped);
            tag.putString(NBT_OWNER, ownerName);
        });
        stack.set(DataComponents.CUSTOM_NAME, (Object)Component.translatable((String)"item.createcybernetics.expcapsule.playername", (Object[])new Object[]{ownerName}).withStyle(ChatFormatting.GREEN));
        return stack;
    }

    public static int getStoredXp(ItemStack stack) {
        CustomData cd = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        if (cd == null || cd.isEmpty()) {
            return 0;
        }
        CompoundTag tag = cd.copyTag();
        return Math.max(0, tag.getInt(NBT_XP_POINTS));
    }

    public static String getOwner(ItemStack stack) {
        CustomData cd = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        if (cd == null || cd.isEmpty()) {
            return "";
        }
        return cd.copyTag().getString(NBT_OWNER);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) {
            return InteractionResultHolder.success((Object)stack);
        }
        if (!(player instanceof ServerPlayer)) {
            return InteractionResultHolder.pass((Object)stack);
        }
        ServerPlayer sp = (ServerPlayer)player;
        int xp = XPCapsuleItem.getStoredXp(stack);
        if (xp <= 0) {
            return InteractionResultHolder.pass((Object)stack);
        }
        sp.giveExperiencePoints(xp);
        stack.shrink(1);
        return InteractionResultHolder.success((Object)stack);
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        String owner;
        int xp = XPCapsuleItem.getStoredXp(stack);
        if (xp > 0) {
            tooltip.add((Component)Component.translatable((String)"item.createcybernetics.expcapsule.stored", (Object[])new Object[]{xp}).withStyle(ChatFormatting.GRAY));
        }
        if (!(owner = XPCapsuleItem.getOwner(stack)).isEmpty()) {
            tooltip.add((Component)Component.translatable((String)"item.createcybernetics.expcapsule.owner", (Object[])new Object[]{owner}).withStyle(ChatFormatting.GRAY));
        }
    }
}

