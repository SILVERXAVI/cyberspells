/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.food.FoodData
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Blocks
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent$Finish
 *  net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent$Start
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$RightClickBlock
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$RightClickItem
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid="createcybernetics")
public class GrassfedStomachItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final String NBT_INSTALLED = "cc_grassfed_stomach_installed";
    private static final int GRASS_FOOD = 4;
    private static final float GRASS_SAT = 0.6f;

    public GrassfedStomachItem(Item.Properties props, int humanityCost) {
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
        return Set.of(CyberwareSlot.ORGANS);
    }

    @Override
    public boolean replacesOrgan() {
        return true;
    }

    @Override
    public Set<CyberwareSlot> getReplacedOrgans() {
        return Set.of(CyberwareSlot.ORGANS);
    }

    @Override
    public TagKey<Item> getReplacedOrganItemTag(ItemStack installedStack, CyberwareSlot slot) {
        return ModTags.Items.INTESTINES_ITEMS;
    }

    @Override
    public void onInstalled(Player player) {
        if (!player.level().isClientSide) {
            player.getPersistentData().putBoolean(NBT_INSTALLED, true);
        }
    }

    @Override
    public void onRemoved(Player player) {
        if (!player.level().isClientSide) {
            player.getPersistentData().putBoolean(NBT_INSTALLED, false);
        }
    }

    @Override
    public void onTick(Player player) {
    }

    private static boolean isInstalled(Player player) {
        return player.getPersistentData().getBoolean(NBT_INSTALLED);
    }

    private static void fillHunger(Player player) {
        FoodData food = player.getFoodData();
        food.setFoodLevel(20);
        food.setSaturation(20.0f);
    }

    @SubscribeEvent
    public static void onUseItemStart(LivingEntityUseItemEvent.Start event) {
        boolean edible;
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }
        Player player = (Player)entity;
        if (!GrassfedStomachItem.isInstalled(player)) {
            return;
        }
        ItemStack using = event.getItem();
        boolean bl = edible = using.getItem().getFoodProperties(using, (LivingEntity)player) != null;
        if (!edible) {
            return;
        }
        if (!using.is(Items.WHEAT)) {
            event.setCanceled(true);
            player.stopUsingItem();
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        if (!GrassfedStomachItem.isInstalled(player)) {
            return;
        }
        ItemStack stack = event.getItemStack();
        if (!stack.is(Items.WHEAT)) {
            return;
        }
        if (player.level().isClientSide) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
            return;
        }
        if (!player.canEat(false)) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
            return;
        }
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        GrassfedStomachItem.fillHunger(player);
        player.level().playSound(null, player.blockPosition(), SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8f, 1.0f);
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    @SubscribeEvent
    public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }
        Player player = (Player)entity;
        if (!GrassfedStomachItem.isInstalled(player)) {
            return;
        }
        ItemStack used = event.getItem();
        if (!used.is(Items.WHEAT)) {
            return;
        }
        if (!player.level().isClientSide) {
            GrassfedStomachItem.fillHunger(player);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        if (player.isCreative()) {
            return;
        }
        if (!GrassfedStomachItem.isInstalled(player)) {
            return;
        }
        if (!player.getMainHandItem().isEmpty()) {
            return;
        }
        if (!level.getBlockState(pos).is(Blocks.GRASS_BLOCK)) {
            return;
        }
        if (!player.canEat(false)) {
            return;
        }
        if (level.isClientSide) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
            return;
        }
        if (!level.mayInteract(player, pos)) {
            return;
        }
        level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 3);
        player.getFoodData().eat(4, 0.6f);
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }
}

