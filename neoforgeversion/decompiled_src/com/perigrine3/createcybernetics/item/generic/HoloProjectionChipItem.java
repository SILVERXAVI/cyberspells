/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.component.CustomData
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$EntityInteract
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$EntityInteractSpecific
 */
package com.perigrine3.createcybernetics.item.generic;

import java.util.List;
import java.util.Set;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public class HoloProjectionChipItem
extends Item {
    public static final String TAG_ROOT = "cc_holo_chip";
    public static final String TAG_ENTITY_TYPE = "entity_type";
    public static final String TAG_ENTITY_NBT = "entity_nbt";
    public static final String TAG_ENTITY_NAME = "entity_name";
    private static final String TAG_LAST_IMPRINT_TICK = "last_imprint_tick";
    private static final Set<String> STRIP_KEYS = Set.of("UUID", "Pos", "Motion", "Rotation", "FallDistance", "Fire", "Air", "OnGround", "PortalCooldown", "Dimension", "Invulnerable", "Silent", "Passengers", "Vehicle", "Leash", "Tags", "Brain", "HandItems", "HandDropChances", "ArmorItems", "ArmorDropChances", "Inventory", "Items", "Offers", "Attributes");

    public HoloProjectionChipItem(Item.Properties props) {
        super(props);
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (HoloProjectionChipItem.performImprint(event.getEntity(), event.getTarget(), event.getItemStack(), event.getHand())) {
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEntityInteractSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
        if (HoloProjectionChipItem.performImprint(event.getEntity(), event.getTarget(), event.getItemStack(), event.getHand())) {
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

    private static boolean performImprint(Player player, Entity target, ItemStack stack, InteractionHand hand) {
        ServerPlayer sp;
        boolean wrote;
        LivingEntity livingTarget;
        block7: {
            block6: {
                if (!(stack.getItem() instanceof HoloProjectionChipItem)) {
                    return false;
                }
                if (!(target instanceof LivingEntity)) break block6;
                livingTarget = (LivingEntity)target;
                if (!(target instanceof Player)) break block7;
            }
            return false;
        }
        if (player.level().isClientSide) {
            return true;
        }
        if (player instanceof ServerPlayer && (wrote = HoloProjectionChipItem.imprintFromEntity(stack, livingTarget, (sp = (ServerPlayer)player).level().getGameTime()))) {
            HoloProjectionChipItem.syncHeldStack(sp, hand, stack);
        }
        return true;
    }

    private static boolean imprintFromEntity(ItemStack stack, LivingEntity target, long gameTime) {
        CompoundTag chip;
        ResourceLocation typeId = BuiltInRegistries.ENTITY_TYPE.getKey((Object)target.getType());
        if (typeId == null) {
            return false;
        }
        CompoundTag root = HoloProjectionChipItem.getCustomDataCopy(stack);
        CompoundTag compoundTag = chip = root.contains(TAG_ROOT, 10) ? root.getCompound(TAG_ROOT) : new CompoundTag();
        if (chip.contains(TAG_LAST_IMPRINT_TICK, 4) && chip.getLong(TAG_LAST_IMPRINT_TICK) == gameTime) {
            return false;
        }
        CompoundTag entityTag = new CompoundTag();
        target.saveWithoutId(entityTag);
        HoloProjectionChipItem.sanitizeEntityTag(entityTag);
        chip.putString(TAG_ENTITY_TYPE, typeId.toString());
        if (!entityTag.isEmpty()) {
            chip.put(TAG_ENTITY_NBT, (Tag)entityTag);
        } else {
            chip.remove(TAG_ENTITY_NBT);
        }
        chip.putString(TAG_ENTITY_NAME, target.getName().getString());
        chip.putLong(TAG_LAST_IMPRINT_TICK, gameTime);
        root.put(TAG_ROOT, (Tag)chip);
        HoloProjectionChipItem.setCustomData(stack, root);
        return true;
    }

    private static void syncHeldStack(ServerPlayer player, InteractionHand hand, ItemStack stack) {
        player.setItemInHand(hand, stack);
        player.getInventory().setChanged();
        player.containerMenu.broadcastChanges();
    }

    private static void sanitizeEntityTag(CompoundTag tag) {
        for (String k : STRIP_KEYS) {
            tag.remove(k);
        }
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        if (!HoloProjectionChipItem.hasEntityData(stack)) {
            return;
        }
        CompoundTag chip = HoloProjectionChipItem.getChipTag(stack);
        String name = chip.getString(TAG_ENTITY_NAME);
        if (!name.isBlank()) {
            tooltip.add((Component)Component.literal((String)name).withStyle(s -> s.withColor(0x55FFFF)));
        }
    }

    private static CompoundTag getCustomDataCopy(ItemStack stack) {
        CustomData cd = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
        return cd == null ? new CompoundTag() : cd.copyTag();
    }

    private static void setCustomData(ItemStack stack, CompoundTag tag) {
        stack.set(DataComponents.CUSTOM_DATA, (Object)CustomData.of((CompoundTag)tag));
    }

    public static boolean hasEntityData(ItemStack stack) {
        CompoundTag root = HoloProjectionChipItem.getCustomDataCopy(stack);
        return root.contains(TAG_ROOT, 10) && root.getCompound(TAG_ROOT).contains(TAG_ENTITY_TYPE, 8);
    }

    public static CompoundTag getChipTag(ItemStack stack) {
        CompoundTag root = HoloProjectionChipItem.getCustomDataCopy(stack);
        if (!root.contains(TAG_ROOT, 10)) {
            return new CompoundTag();
        }
        return root.getCompound(TAG_ROOT).copy();
    }
}

