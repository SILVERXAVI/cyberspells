/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.model.PlayerModel
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.player.PlayerRenderer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.core.Holder
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.EquipmentSlotGroup
 *  net.minecraft.world.entity.HumanoidArm
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.AxeItem
 *  net.minecraft.world.item.BowItem
 *  net.minecraft.world.item.CrossbowItem
 *  net.minecraft.world.item.DiggerItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.MaceItem
 *  net.minecraft.world.item.SwordItem
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.TridentItem
 *  net.minecraft.world.item.component.ItemAttributeModifiers
 *  net.minecraft.world.item.component.ItemAttributeModifiers$Entry
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.EventPriority
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.RenderArmEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerLoggedOutEvent
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.client.model.AttachmentAnchor;
import com.perigrine3.createcybernetics.client.model.ClawAttachmentModel;
import com.perigrine3.createcybernetics.client.model.PlayerAttachmentManager;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.sound.ModSounds;
import com.perigrine3.createcybernetics.util.CyberwareAttributeHelper;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderArmEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class RetractableClawsItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;

    public RetractableClawsItem(Item.Properties props, int humanityCost) {
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
        return Set.of(CyberwareSlot.LARM, CyberwareSlot.RARM);
    }

    @Override
    public Set<TagKey<Item>> requiresCyberwareTags(ItemStack installedStack, CyberwareSlot slot) {
        return switch (slot) {
            case CyberwareSlot.RARM -> Set.of(ModTags.Items.RIGHTARM_REPLACEMENTS);
            case CyberwareSlot.LARM -> Set.of(ModTags.Items.LEFTARM_REPLACEMENTS);
            default -> Set.of();
        };
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
    public void onTick(Player player) {
    }

    private static boolean hasEnabledClawsInSlot(PlayerCyberwareData data, CyberwareSlot slot) {
        InstalledCyberware[] arr = data.getAll().get((Object)slot);
        if (arr == null) {
            return false;
        }
        for (int i = 0; i < arr.length; ++i) {
            ItemStack st;
            InstalledCyberware cw = arr[i];
            if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || !(st.getItem() instanceof RetractableClawsItem) || !data.isEnabled(slot, i)) continue;
            return true;
        }
        return false;
    }

    @EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT}, bus=EventBusSubscriber.Bus.GAME)
    public static final class ClientFirstPerson {
        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @SubscribeEvent(priority=EventPriority.LOWEST, receiveCanceled=true)
        public static void onRenderArm(RenderArmEvent event) {
            CyberwareSlot slot;
            AbstractClientPlayer player = event.getPlayer();
            if (player == null) {
                return;
            }
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer viewer = mc.player;
            if (viewer != null ? player.isInvisibleTo((Player)viewer) : player.isInvisible()) {
                return;
            }
            if (!player.hasData(ModAttachments.CYBERWARE)) {
                return;
            }
            PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            HumanoidArm arm = event.getArm();
            CyberwareSlot cyberwareSlot = slot = arm == HumanoidArm.LEFT ? CyberwareSlot.LARM : CyberwareSlot.RARM;
            if (!RetractableClawsItem.hasEnabledClawsInSlot(data, slot)) {
                return;
            }
            EntityRenderer renderer = mc.getEntityRenderDispatcher().getRenderer((Entity)player);
            if (!(renderer instanceof PlayerRenderer)) {
                return;
            }
            PlayerRenderer pr = (PlayerRenderer)renderer;
            PlayerModel pm = (PlayerModel)pr.getModel();
            ModelPart armPart = arm == HumanoidArm.LEFT ? pm.leftArm : pm.rightArm;
            PoseStack pose = event.getPoseStack();
            MultiBufferSource buffers = event.getMultiBufferSource();
            int light = event.getPackedLight();
            ClawAttachmentModel model = PlayerAttachmentManager.clawsModel();
            ResourceLocation tex = PlayerAttachmentManager.CLAWS_TEXTURE;
            pose.pushPose();
            try {
                pose.translate(armPart.x / 16.0f, armPart.y / 16.0f, armPart.z / 16.0f);
                pose.mulPose(Axis.XP.rotationDegrees(0.0f));
                pose.mulPose(Axis.YP.rotationDegrees(0.0f));
                pose.mulPose(Axis.ZP.rotationDegrees(5.0f));
                AttachmentAnchor anchor = arm == HumanoidArm.LEFT ? AttachmentAnchor.LEFT_ARM : AttachmentAnchor.RIGHT_ARM;
                PlayerAttachmentManager.applyKnuckleClawTransform(pose, anchor);
                VertexConsumer vc = buffers.getBuffer(model.renderType(tex));
                model.renderToBuffer(pose, vc, light, OverlayTexture.NO_OVERLAY, -1);
            }
            finally {
                pose.popPose();
            }
        }
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class ServerHandler {
        private static final Map<UUID, Boolean> LAST_LEFT = new HashMap<UUID, Boolean>();
        private static final Map<UUID, Boolean> LAST_RIGHT = new HashMap<UUID, Boolean>();

        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent.Post event) {
            boolean prevRight;
            Player player = event.getEntity();
            if (player.level().isClientSide) {
                return;
            }
            if (!player.hasData(ModAttachments.CYBERWARE)) {
                return;
            }
            PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            boolean leftEnabled = ServerHandler.isLeftEnabled(data);
            boolean rightEnabled = ServerHandler.isRightEnabled(data);
            UUID id = player.getUUID();
            boolean prevLeft = LAST_LEFT.getOrDefault(id, false);
            if (leftEnabled != prevLeft) {
                LAST_LEFT.put(id, leftEnabled);
                ServerHandler.playToggleSound(player, leftEnabled);
            }
            if (rightEnabled != (prevRight = LAST_RIGHT.getOrDefault(id, false).booleanValue())) {
                LAST_RIGHT.put(id, rightEnabled);
                ServerHandler.playToggleSound(player, rightEnabled);
            }
            boolean anyEnabled = leftEnabled || rightEnabled;
            boolean weaponEquipped = ServerHandler.isHoldingWeapon(player);
            if (anyEnabled && !weaponEquipped) {
                if (rightEnabled && leftEnabled) {
                    CyberwareAttributeHelper.applyModifier((LivingEntity)player, "claws_attack1");
                    CyberwareAttributeHelper.applyModifier((LivingEntity)player, "claws_attack2");
                } else {
                    CyberwareAttributeHelper.applyModifier((LivingEntity)player, "claws_attack1");
                    CyberwareAttributeHelper.removeModifier((LivingEntity)player, "claws_attack2");
                }
            } else {
                CyberwareAttributeHelper.removeModifier((LivingEntity)player, "claws_attack1");
                CyberwareAttributeHelper.removeModifier((LivingEntity)player, "claws_attack2");
            }
        }

        private static boolean isHoldingWeapon(Player player) {
            return ServerHandler.isWeaponLike(player.getMainHandItem()) || ServerHandler.isWeaponLike(player.getOffhandItem());
        }

        private static boolean isWeaponLike(ItemStack stack) {
            if (stack == null || stack.isEmpty()) {
                return false;
            }
            Item it = stack.getItem();
            if (it instanceof BowItem || it instanceof CrossbowItem || it instanceof TridentItem) {
                return true;
            }
            if (it instanceof SwordItem || it instanceof AxeItem || it instanceof MaceItem || it instanceof DiggerItem) {
                return true;
            }
            ItemAttributeModifiers mods = (ItemAttributeModifiers)stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
            if (mods == null) {
                return false;
            }
            for (ItemAttributeModifiers.Entry e : mods.modifiers()) {
                boolean mainhand;
                Holder attr = e.attribute();
                boolean attackDamage = attr != null && attr.value() == Attributes.ATTACK_DAMAGE;
                boolean bl = mainhand = e.slot() == EquipmentSlotGroup.MAINHAND || e.slot().test(EquipmentSlot.MAINHAND);
                if (!attackDamage || !mainhand || e.modifier().amount() == 0.0) continue;
                return true;
            }
            return false;
        }

        private static void playToggleSound(Player player, boolean enabledNow) {
            SoundEvent snd = ServerHandler.clawsToggleSound();
            if (snd == null) {
                return;
            }
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), snd, SoundSource.PLAYERS, 0.9f, enabledNow ? 1.15f : 0.95f);
        }

        private static SoundEvent clawsToggleSound() {
            return ModSounds.RETRACTABLE_CLAWS_SNIKT.get();
        }

        @SubscribeEvent
        public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
            UUID id = event.getEntity().getUUID();
            LAST_LEFT.remove(id);
            LAST_RIGHT.remove(id);
        }

        private static boolean isLeftEnabled(PlayerCyberwareData data) {
            return ServerHandler.hasEnabledInSlot(data, CyberwareSlot.LARM);
        }

        private static boolean isRightEnabled(PlayerCyberwareData data) {
            return ServerHandler.hasEnabledInSlot(data, CyberwareSlot.RARM);
        }

        private static boolean hasEnabledInSlot(PlayerCyberwareData data, CyberwareSlot slot) {
            InstalledCyberware[] arr = data.getAll().get((Object)slot);
            if (arr == null) {
                return false;
            }
            for (int i = 0; i < arr.length; ++i) {
                ItemStack st;
                InstalledCyberware cw = arr[i];
                if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || !(st.getItem() instanceof RetractableClawsItem) || !data.isEnabled(slot, i)) continue;
                return true;
            }
            return false;
        }
    }
}

