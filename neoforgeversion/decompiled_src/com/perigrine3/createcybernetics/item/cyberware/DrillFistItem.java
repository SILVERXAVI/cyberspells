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
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
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
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.EventPriority
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.InputEvent$InteractionKeyMappingTriggered
 *  net.neoforged.neoforge.client.event.RenderArmEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$BreakSpeed
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$HarvestCheck
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$LeftClickBlock
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$RightClickBlock
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
import com.perigrine3.createcybernetics.client.model.DrillFistAttachmentModel;
import com.perigrine3.createcybernetics.client.model.PlayerAttachmentManager;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.List;
import java.util.Set;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderArmEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class DrillFistItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final float DIAMOND_PICK_SPEED = 8.0f;

    public DrillFistItem(Item.Properties props, int humanityCost) {
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
        return switch (slot) {
            case CyberwareSlot.RARM -> Set.of(ModTags.Items.RIGHTARM_REPLACEMENTS);
            case CyberwareSlot.LARM -> Set.of(ModTags.Items.LEFTARM_REPLACEMENTS);
            default -> Set.of();
        };
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.RARM, CyberwareSlot.LARM);
    }

    @Override
    public boolean replacesOrgan() {
        return false;
    }

    @Override
    public Set<CyberwareSlot> getReplacedOrgans() {
        return Set.of();
    }

    private static boolean hasDrillInstalled(Player player, CyberwareSlot slot) {
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return false;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return false;
        }
        return data.hasSpecificItem((Item)ModItems.ARMUPGRADES_DRILLFIST.get(), slot);
    }

    private static boolean hasAnyDrillInstalled(Player player) {
        return DrillFistItem.hasDrillInstalled(player, CyberwareSlot.RARM) || DrillFistItem.hasDrillInstalled(player, CyberwareSlot.LARM);
    }

    private static InteractionHand handForSlot(Player player, CyberwareSlot slot) {
        HumanoidArm main = player.getMainArm();
        return switch (slot) {
            case CyberwareSlot.RARM -> {
                if (main == HumanoidArm.RIGHT) {
                    yield InteractionHand.MAIN_HAND;
                }
                yield InteractionHand.OFF_HAND;
            }
            case CyberwareSlot.LARM -> {
                if (main == HumanoidArm.RIGHT) {
                    yield InteractionHand.OFF_HAND;
                }
                yield InteractionHand.MAIN_HAND;
            }
            default -> InteractionHand.MAIN_HAND;
        };
    }

    private static InteractionHand pickDrillSwingHandDeterministic(Player player) {
        boolean right = DrillFistItem.hasDrillInstalled(player, CyberwareSlot.RARM);
        boolean left = DrillFistItem.hasDrillInstalled(player, CyberwareSlot.LARM);
        if (right && !left) {
            return DrillFistItem.handForSlot(player, CyberwareSlot.RARM);
        }
        if (left && !right) {
            return DrillFistItem.handForSlot(player, CyberwareSlot.LARM);
        }
        return player.getMainArm() == HumanoidArm.RIGHT ? DrillFistItem.handForSlot(player, CyberwareSlot.RARM) : DrillFistItem.handForSlot(player, CyberwareSlot.LARM);
    }

    private static boolean drillBlocksMainHand(Player player) {
        HumanoidArm mainArm = player.getMainArm();
        boolean rightInstalled = DrillFistItem.hasDrillInstalled(player, CyberwareSlot.RARM);
        boolean leftInstalled = DrillFistItem.hasDrillInstalled(player, CyberwareSlot.LARM);
        return mainArm == HumanoidArm.RIGHT ? rightInstalled : leftInstalled;
    }

    private static boolean drillBlocksOffhand(Player player) {
        HumanoidArm mainArm = player.getMainArm();
        boolean rightInstalled = DrillFistItem.hasDrillInstalled(player, CyberwareSlot.RARM);
        boolean leftInstalled = DrillFistItem.hasDrillInstalled(player, CyberwareSlot.LARM);
        return mainArm == HumanoidArm.RIGHT ? leftInstalled : rightInstalled;
    }

    private static boolean drillBlocksHand(Player player, InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND ? DrillFistItem.drillBlocksMainHand(player) : DrillFistItem.drillBlocksOffhand(player);
    }

    private static void dropAndClearHand(ServerSideDropper dropper, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty()) {
            return;
        }
        ItemStack toDrop = stack.copy();
        player.setItemInHand(hand, ItemStack.EMPTY);
        dropper.drop(player, toDrop);
    }

    private static CyberwareSlot slotForArm(HumanoidArm arm) {
        return arm == HumanoidArm.LEFT ? CyberwareSlot.LARM : CyberwareSlot.RARM;
    }

    private static HumanoidArm offArm(HumanoidArm main) {
        return main == HumanoidArm.LEFT ? HumanoidArm.RIGHT : HumanoidArm.LEFT;
    }

    @FunctionalInterface
    private static interface ServerSideDropper {
        public void drop(Player var1, ItemStack var2);
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
            if (!ClientFirstPerson.hasDrillInSlot(data, slot)) {
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
            DrillFistAttachmentModel model = PlayerAttachmentManager.drillModel();
            ResourceLocation tex = PlayerAttachmentManager.DRILL_FIST_TEXTURE;
            pose.pushPose();
            try {
                if (arm == player.getMainArm()) {
                    pose.translate(armPart.x / 20.0f, armPart.y / 32.0f, armPart.z / 16.0f);
                    pose.scale(1.15f, 1.15f, 1.15f);
                    pose.mulPose(Axis.XP.rotationDegrees(0.0f));
                    pose.mulPose(Axis.YP.rotationDegrees(0.0f));
                    pose.mulPose(Axis.ZP.rotationDegrees(5.0f));
                } else {
                    pose.translate(armPart.x / 20.0f, armPart.y / 32.0f, armPart.z / 16.0f);
                    pose.scale(1.15f, 1.15f, 1.15f);
                    pose.mulPose(Axis.XP.rotationDegrees(0.0f));
                    pose.mulPose(Axis.YP.rotationDegrees(0.0f));
                    pose.mulPose(Axis.ZP.rotationDegrees(-5.0f));
                }
                AttachmentAnchor anchor = arm == HumanoidArm.LEFT ? AttachmentAnchor.LEFT_ARM : AttachmentAnchor.RIGHT_ARM;
                PlayerAttachmentManager.applyDrillFistTransform(pose, anchor);
                VertexConsumer vc = buffers.getBuffer(model.renderType(tex));
                model.renderToBuffer(pose, vc, light, OverlayTexture.NO_OVERLAY, -1);
            }
            finally {
                pose.popPose();
            }
        }

        private static boolean hasDrillInSlot(PlayerCyberwareData data, CyberwareSlot slot) {
            InstalledCyberware[] arr = data.getAll().get((Object)slot);
            if (arr == null) {
                return false;
            }
            for (int i = 0; i < arr.length; ++i) {
                ItemStack st;
                InstalledCyberware cw = arr[i];
                if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || !st.is((Item)ModItems.ARMUPGRADES_DRILLFIST.get())) continue;
                return true;
            }
            return false;
        }
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME, value={Dist.CLIENT})
    public static final class DrillClientHooks {
        private DrillClientHooks() {
        }

        @SubscribeEvent(priority=EventPriority.HIGHEST)
        public static void onInteractionKey(InputEvent.InteractionKeyMappingTriggered event) {
            if (!event.isAttack()) {
                return;
            }
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (player == null) {
                return;
            }
            if (!DrillFistItem.hasAnyDrillInstalled((Player)player)) {
                return;
            }
            HitResult hit = mc.hitResult;
            if (!(hit instanceof BlockHitResult)) {
                return;
            }
            InteractionHand drillHand = DrillFistItem.pickDrillSwingHandDeterministic((Player)player);
            event.setSwingHand(false);
            player.swing(drillHand, true);
        }
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class DrillHooks {
        private DrillHooks() {
        }

        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent.Post event) {
            Player player = event.getEntity();
            if (player.level().isClientSide()) {
                return;
            }
            boolean blocksMain = DrillFistItem.drillBlocksMainHand(player);
            boolean blocksOff = DrillFistItem.drillBlocksOffhand(player);
            if (!blocksMain && !blocksOff) {
                return;
            }
            ServerSideDropper dropper = (p, stack) -> p.drop(stack, true);
            if (blocksMain) {
                DrillFistItem.dropAndClearHand(dropper, player, InteractionHand.MAIN_HAND);
            }
            if (blocksOff) {
                DrillFistItem.dropAndClearHand(dropper, player, InteractionHand.OFF_HAND);
            }
        }

        @SubscribeEvent(priority=EventPriority.HIGHEST)
        public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
            Player player = event.getEntity();
            if (player.level().isClientSide()) {
                return;
            }
            if (!DrillFistItem.hasAnyDrillInstalled(player)) {
                return;
            }
            InteractionHand drillHand = DrillFistItem.pickDrillSwingHandDeterministic(player);
            if (player instanceof ServerPlayer) {
                ServerPlayer sp = (ServerPlayer)player;
                sp.swing(drillHand, true);
            }
        }

        @SubscribeEvent(priority=EventPriority.HIGHEST)
        public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            boolean isUiBlock;
            Player player = event.getEntity();
            Level level = player.level();
            if (level.isClientSide()) {
                return;
            }
            if (!DrillFistItem.drillBlocksHand(player, event.getHand())) {
                return;
            }
            BlockState state = level.getBlockState(event.getPos());
            boolean bl = isUiBlock = state.getMenuProvider(level, event.getPos()) != null;
            if (!isUiBlock) {
                return;
            }
            if (player.getRandom().nextBoolean()) {
                return;
            }
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
        }

        @SubscribeEvent
        public static void onHarvestCheck(PlayerEvent.HarvestCheck event) {
            if (!DrillFistItem.hasAnyDrillInstalled(event.getEntity())) {
                return;
            }
            event.setCanHarvest(true);
        }

        @SubscribeEvent
        public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
            Player player = event.getEntity();
            if (!DrillFistItem.hasAnyDrillInstalled(player)) {
                return;
            }
            float original = event.getOriginalSpeed();
            event.setNewSpeed(Math.max(original, 8.0f));
        }
    }
}

