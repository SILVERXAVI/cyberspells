/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.core.Holder
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeInstance
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.bus.api.EventPriority
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.util.CyberwareAttributeHelper;
import com.perigrine3.createcybernetics.util.ModTags;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class CyberlegItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private final CyberwareSlot side;

    public CyberlegItem(Item.Properties props, int humanityCost, CyberwareSlot side) {
        super(props);
        this.humanityCost = humanityCost;
        this.side = side;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.basecyberware_cyberleg.energy").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public boolean isDyeable(ItemStack stack, CyberwareSlot slot) {
        return slot == this.side;
    }

    @Override
    public boolean isDyeable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnergyUsedPerTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return 10;
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
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(this.side);
    }

    @Override
    public boolean replacesOrgan() {
        return true;
    }

    @Override
    public Set<CyberwareSlot> getReplacedOrgans() {
        return Set.of(this.side);
    }

    @Override
    public void onInstalled(Player player) {
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data.hasAnyTagged(ModTags.Items.RIGHT_CYBERLEG, CyberwareSlot.RLEG) && data.hasAnyTagged(ModTags.Items.LEFT_CYBERLEG, CyberwareSlot.LLEG)) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "cyberleg_speed1");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "cyberleg_jump1");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "cyberleg_speed2");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "cyberleg_jump2");
        } else if (data.hasAnyTagged(ModTags.Items.RIGHT_CYBERLEG, CyberwareSlot.RLEG) || data.hasAnyTagged(ModTags.Items.LEFT_CYBERLEG, CyberwareSlot.LLEG)) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "cyberleg_speed1");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "cyberleg_jump2");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "cyberleg_speed2");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "cyberleg_jump2");
        }
    }

    @Override
    public void onRemoved(Player player) {
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "cyberleg_speed1");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "cyberleg_jump1");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "cyberleg_speed2");
        CyberwareAttributeHelper.removeModifier((LivingEntity)player, "cyberleg_jump2");
        this.onInstalled(player);
    }

    @Override
    public void onTick(Player player) {
        ICyberwareItem.super.onTick(player);
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class PowerFailHooks {
        private static final ResourceLocation UNPOWERED_LEG_SPEED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberleg_unpowered_speed");
        private static final ResourceLocation UNPOWERED_LEG_JUMP = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberleg_unpowered_jump");

        private PowerFailHooks() {
        }

        @SubscribeEvent(priority=EventPriority.HIGHEST)
        public static void onPlayerTick(PlayerTickEvent.Post event) {
            boolean rightDead;
            Player player = event.getEntity();
            if (player.level().isClientSide) {
                return;
            }
            PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            boolean leftDead = PowerFailHooks.isCyberlegUnpowered(player, data, CyberwareSlot.LLEG);
            int deadCount = (leftDead ? 1 : 0) + ((rightDead = PowerFailHooks.isCyberlegUnpowered(player, data, CyberwareSlot.RLEG)) ? 1 : 0);
            if (deadCount <= 0) {
                PowerFailHooks.setOrClearModifier(player, (Holder<Attribute>)Attributes.MOVEMENT_SPEED, UNPOWERED_LEG_SPEED, null);
                PowerFailHooks.setOrClearModifier(player, (Holder<Attribute>)Attributes.JUMP_STRENGTH, UNPOWERED_LEG_JUMP, null);
                return;
            }
            if (deadCount == 1) {
                PowerFailHooks.setOrClearModifier(player, (Holder<Attribute>)Attributes.MOVEMENT_SPEED, UNPOWERED_LEG_SPEED, -0.5);
                PowerFailHooks.setOrClearModifier(player, (Holder<Attribute>)Attributes.JUMP_STRENGTH, UNPOWERED_LEG_JUMP, -0.5);
                return;
            }
            PowerFailHooks.setOrClearModifier(player, (Holder<Attribute>)Attributes.MOVEMENT_SPEED, UNPOWERED_LEG_SPEED, -1.0);
            PowerFailHooks.setOrClearModifier(player, (Holder<Attribute>)Attributes.JUMP_STRENGTH, UNPOWERED_LEG_JUMP, -0.75);
            player.setSprinting(false);
            Vec3 v = player.getDeltaMovement();
            if (v.x != 0.0 || v.z != 0.0) {
                player.setDeltaMovement(0.0, v.y, 0.0);
                player.hurtMarked = true;
            }
        }

        private static void setOrClearModifier(Player player, Holder<Attribute> attrHolder, ResourceLocation id, Double amountOrNull) {
            AttributeInstance attr = player.getAttribute(attrHolder);
            if (attr == null) {
                return;
            }
            if (amountOrNull == null) {
                attr.removeModifier(id);
                return;
            }
            AttributeModifier existing = attr.getModifier(id);
            if (existing != null) {
                if (Double.compare(existing.amount(), amountOrNull) == 0 && existing.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                    return;
                }
                attr.removeModifier(id);
            }
            attr.addTransientModifier(new AttributeModifier(id, amountOrNull.doubleValue(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }

        private static boolean isCyberlegUnpowered(Player player, PlayerCyberwareData data, CyberwareSlot legSlot) {
            InstalledCyberware[] arr = data.getAll().get((Object)legSlot);
            if (arr == null) {
                return false;
            }
            for (int idx = 0; idx < arr.length; ++idx) {
                Item item;
                ItemStack st;
                InstalledCyberware installed = arr[idx];
                if (installed == null || (st = installed.getItem()) == null || st.isEmpty() || !((item = st.getItem()) instanceof CyberlegItem)) continue;
                CyberlegItem cyberleg = (CyberlegItem)item;
                if (cyberleg.side != legSlot || !data.isEnabled(legSlot, idx)) continue;
                if (!cyberleg.requiresEnergyToFunction(player, st, legSlot)) {
                    return false;
                }
                boolean powered = PowerFailHooks.readInstalledPowered(installed);
                return !powered;
            }
            return false;
        }

        private static boolean readInstalledPowered(Object installedCyberware) {
            try {
                Object v2;
                Method m;
                try {
                    m = installedCyberware.getClass().getMethod("isPowered", new Class[0]);
                    v2 = m.invoke(installedCyberware, new Object[0]);
                    if (v2 instanceof Boolean) {
                        Boolean b = (Boolean)v2;
                        return b;
                    }
                }
                catch (NoSuchMethodException v2) {
                    // empty catch block
                }
                try {
                    m = installedCyberware.getClass().getMethod("getPowered", new Class[0]);
                    v2 = m.invoke(installedCyberware, new Object[0]);
                    if (v2 instanceof Boolean) {
                        Boolean b = (Boolean)v2;
                        return b;
                    }
                }
                catch (NoSuchMethodException v3) {
                    // empty catch block
                }
                try {
                    m = installedCyberware.getClass().getMethod("powered", new Class[0]);
                    v2 = m.invoke(installedCyberware, new Object[0]);
                    if (v2 instanceof Boolean) {
                        Boolean b = (Boolean)v2;
                        return b;
                    }
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    // empty catch block
                }
                return true;
            }
            catch (Throwable t) {
                return true;
            }
        }
    }
}

