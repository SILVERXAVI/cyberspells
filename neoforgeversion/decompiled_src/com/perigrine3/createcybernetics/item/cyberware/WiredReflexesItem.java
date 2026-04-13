/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.commands.arguments.EntityAnchorArgument$Anchor
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientboundPlayerLookAtPacket
 *  net.minecraft.network.protocol.game.ClientboundRotateHeadPacket
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.tags.TagKey
 *  net.minecraft.util.Mth
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.living.LivingDamageEvent$Pre
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerLookAtPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public class WiredReflexesItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final String NBT_INSTALLED = "cc_wired_reflexes_installed";

    public WiredReflexesItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.muscleupgrades_wiredreflexes.energy").withStyle(ChatFormatting.RED));
        }
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
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public Set<TagKey<Item>> requiresCyberwareTags(ItemStack installedStack, CyberwareSlot slot) {
        return Set.of(ModTags.Items.MUSCLE_ITEMS);
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.MUSCLE);
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
        if (player.level().isClientSide) {
            return;
        }
        player.getPersistentData().putBoolean(NBT_INSTALLED, true);
    }

    @Override
    public void onRemoved(Player player) {
        if (player.level().isClientSide) {
            return;
        }
        player.getPersistentData().remove(NBT_INSTALLED);
    }

    @Override
    public void onTick(Player player) {
        if (player.level().isClientSide) {
            return;
        }
        player.getPersistentData().putBoolean(NBT_INSTALLED, true);
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer player = (ServerPlayer)livingEntity;
        if (!player.getPersistentData().getBoolean(NBT_INSTALLED)) {
            return;
        }
        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();
        if (attacker == null) {
            attacker = source.getDirectEntity();
        }
        if (attacker == null || attacker == player) {
            return;
        }
        double px = player.getX();
        double py = player.getEyeY();
        double pz = player.getZ();
        double ax = attacker.getX();
        double ay = attacker.getY() + (double)attacker.getBbHeight() * 0.5;
        double az = attacker.getZ();
        double dx = ax - px;
        double dy = ay - py;
        double dz = az - pz;
        double horiz = Math.sqrt(dx * dx + dz * dz);
        if (horiz < 1.0E-6) {
            return;
        }
        float yaw = (float)(Mth.atan2((double)dz, (double)dx) * 57.29577951308232) - 90.0f;
        float pitch = (float)(-(Mth.atan2((double)dy, (double)horiz) * 57.29577951308232));
        yaw = Mth.wrapDegrees((float)yaw);
        pitch = Mth.clamp((float)pitch, (float)-90.0f, (float)90.0f);
        player.setYRot(yaw);
        player.setXRot(pitch);
        player.setYBodyRot(yaw);
        player.setYHeadRot(yaw);
        player.connection.send((Packet)new ClientboundPlayerLookAtPacket(EntityAnchorArgument.Anchor.EYES, attacker, EntityAnchorArgument.Anchor.EYES));
        byte headByte = (byte)Mth.floor((float)(yaw * 256.0f / 360.0f));
        player.connection.send((Packet)new ClientboundRotateHeadPacket((Entity)player, headByte));
    }
}

