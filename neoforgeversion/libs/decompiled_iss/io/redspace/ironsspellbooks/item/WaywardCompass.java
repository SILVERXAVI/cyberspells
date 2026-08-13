/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.GlobalPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.api.item.WaywardCompassData;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import io.redspace.ironsspellbooks.util.ModTags;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class WaywardCompass
extends Item {
    private static final Component description = Component.translatable((String)"item.irons_spellbooks.wayward_compass_desc").withStyle(ChatFormatting.DARK_AQUA);

    public WaywardCompass() {
        super(ItemPropertiesHelper.equipment());
    }

    @Nullable
    public static GlobalPos getCatacombsLocation(Entity entity, ItemStack stack) {
        if (entity.level.dimension() != Level.OVERWORLD || !stack.has(ComponentRegistry.WAYWARD_COMPASS)) {
            return null;
        }
        return GlobalPos.of((ResourceKey)entity.level.dimension(), (BlockPos)((WaywardCompassData)stack.get(ComponentRegistry.WAYWARD_COMPASS)).blockPos());
    }

    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        WaywardCompass.findCatacombs(pStack, pLevel, pPlayer);
    }

    private static void findCatacombs(ItemStack pStack, Level pLevel, Player pPlayer) {
        ServerLevel serverlevel;
        BlockPos blockpos;
        if (pLevel instanceof ServerLevel && (blockpos = (serverlevel = (ServerLevel)pLevel).findNearestMapStructure(ModTags.WAYWARD_COMPASS_LOCATOR, pPlayer.blockPosition(), 100, false)) != null) {
            pStack.set(ComponentRegistry.WAYWARD_COMPASS, (Object)new WaywardCompassData(blockpos));
        }
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (this.missingWarning(itemStack)) {
            WaywardCompass.findCatacombs(itemStack, pLevel, pPlayer);
            pPlayer.getCooldowns().addCooldown((Item)ItemRegistry.WAYWARD_COMPASS.get(), 200);
            return InteractionResultHolder.sidedSuccess((Object)itemStack, (boolean)pLevel.isClientSide);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public boolean missingWarning(ItemStack itemStack) {
        return !itemStack.has(ComponentRegistry.WAYWARD_COMPASS);
    }

    public void appendHoverText(ItemStack pStack, Item.TooltipContext context, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, context, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(description);
        if (this.missingWarning(pStack)) {
            pTooltipComponents.add((Component)Component.translatable((String)"item.irons_spellbooks.wayward_compass.error", (Object[])new Object[]{Minecraft.getInstance().options.keyUse.getTranslatedKeyMessage()}).withStyle(ChatFormatting.RED));
        }
    }
}

