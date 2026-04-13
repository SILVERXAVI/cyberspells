/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.HumanoidModel$ArmPose
 *  net.minecraft.client.model.PlayerModel
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.player.PlayerRenderer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.client.resources.PlayerSkin$Model
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.FastColor$ARGB32
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.HumanoidArm
 *  net.minecraft.world.entity.LivingEntity
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.EventPriority
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.RenderArmEvent
 *  net.neoforged.neoforge.client.event.RenderLivingEvent$Post
 *  net.neoforged.neoforge.client.event.RenderLivingEvent$Pre
 */
package com.perigrine3.createcybernetics.client.skin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.perigrine3.createcybernetics.client.skin.SkinModifier;
import com.perigrine3.createcybernetics.client.skin.SkinModifierManager;
import com.perigrine3.createcybernetics.client.skin.SkinModifierState;
import com.perigrine3.createcybernetics.compat.bettercombat.BetterCombatCompat;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderArmEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;

public final class SkinLayerRender {
    private static final Map<UUID, boolean[]> PREV_WEAR_VIS = new HashMap<UUID, boolean[]>();
    private static final Map<UUID, SkinModifierState> SKIN_STATES = new HashMap<UUID, SkinModifierState>();
    private static final Map<UUID, Boolean> REPLACE_FP_RIGHT = new HashMap<UUID, Boolean>();
    private static final Map<UUID, Boolean> REPLACE_FP_LEFT = new HashMap<UUID, Boolean>();
    private static final Map<UUID, Boolean> FP_CANCEL_RIGHT = new HashMap<UUID, Boolean>();
    private static final Map<UUID, Boolean> FP_CANCEL_LEFT = new HashMap<UUID, Boolean>();
    private static final Map<UUID, Boolean> FP_HIDE_SLEEVE_RIGHT = new HashMap<UUID, Boolean>();
    private static final Map<UUID, Boolean> FP_HIDE_SLEEVE_LEFT = new HashMap<UUID, Boolean>();

    private SkinLayerRender() {
    }

    @EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT})
    public final class FirstPersonSkinOverlayRenderer {
        private static final Map<UUID, Boolean> FP_CANCEL_RIGHT = new HashMap<UUID, Boolean>();
        private static final Map<UUID, Boolean> FP_CANCEL_LEFT = new HashMap<UUID, Boolean>();
        private static final Map<UUID, Boolean> FP_HIDE_SLEEVE_RIGHT = new HashMap<UUID, Boolean>();
        private static final Map<UUID, Boolean> FP_HIDE_SLEEVE_LEFT = new HashMap<UUID, Boolean>();

        private FirstPersonSkinOverlayRenderer(SkinLayerRender this$0) {
        }

        @SubscribeEvent(priority=EventPriority.HIGHEST, receiveCanceled=true)
        public static void onRenderArmCancel(RenderArmEvent event) {
            boolean cancel;
            if (BetterCombatCompat.LOADED) {
                return;
            }
            AbstractClientPlayer player = event.getPlayer();
            SkinModifierState state = SkinModifierManager.getPlayerSkinState(player);
            if (state == null || !state.hasModifiers()) {
                return;
            }
            HumanoidArm arm = event.getArm();
            UUID id = player.getUUID();
            boolean replace = false;
            for (SkinModifier m : state.getModifiers()) {
                if (!m.replacesVanillaArm(arm)) continue;
                replace = true;
                break;
            }
            EnumSet<SkinModifier.HideVanilla> hide = state.getHideMask();
            boolean hideSleeve = arm == HumanoidArm.RIGHT ? hide.contains((Object)SkinModifier.HideVanilla.RIGHT_SLEEVE) : hide.contains((Object)SkinModifier.HideVanilla.LEFT_SLEEVE);
            boolean bl = cancel = replace || hideSleeve;
            if (arm == HumanoidArm.RIGHT) {
                REPLACE_FP_RIGHT.put(id, replace);
                FP_HIDE_SLEEVE_RIGHT.put(id, hideSleeve);
                FP_CANCEL_RIGHT.put(id, cancel);
            } else {
                REPLACE_FP_LEFT.put(id, replace);
                FP_HIDE_SLEEVE_LEFT.put(id, hideSleeve);
                FP_CANCEL_LEFT.put(id, cancel);
            }
            if (cancel) {
                event.setCanceled(true);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @SubscribeEvent(priority=EventPriority.LOWEST, receiveCanceled=true)
        public static void onRenderArm(RenderArmEvent event) {
            AbstractClientPlayer player = event.getPlayer();
            SkinModifierState state = SkinModifierManager.getPlayerSkinState(player);
            if (state == null || !state.hasModifiers()) {
                return;
            }
            Minecraft mc = Minecraft.getInstance();
            EntityRenderer renderer = mc.getEntityRenderDispatcher().getRenderer((Entity)player);
            if (!(renderer instanceof PlayerRenderer)) {
                return;
            }
            PlayerRenderer playerRenderer = (PlayerRenderer)renderer;
            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource buffer = event.getMultiBufferSource();
            int light = event.getPackedLight();
            HumanoidArm arm = event.getArm();
            UUID id = player.getUUID();
            boolean replaceVanillaArm = arm == HumanoidArm.RIGHT ? Boolean.TRUE.equals(REPLACE_FP_RIGHT.get(id)) : Boolean.TRUE.equals(REPLACE_FP_LEFT.get(id));
            boolean cancel = arm == HumanoidArm.RIGHT ? Boolean.TRUE.equals(FP_CANCEL_RIGHT.get(id)) : Boolean.TRUE.equals(FP_CANCEL_LEFT.get(id));
            boolean hideSleeve = arm == HumanoidArm.RIGHT ? Boolean.TRUE.equals(FP_HIDE_SLEEVE_RIGHT.get(id)) : Boolean.TRUE.equals(FP_HIDE_SLEEVE_LEFT.get(id));
            SkinModifier baseArmModifier = null;
            if (replaceVanillaArm) {
                for (SkinModifier m2 : state.getModifiers()) {
                    if (!m2.replacesVanillaArm(arm)) continue;
                    baseArmModifier = m2;
                    break;
                }
            }
            PlayerModel model = (PlayerModel)playerRenderer.getModel();
            PlayerSkin.Model modelType = player.getSkin().model();
            ModelPart armPart = arm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
            ModelPart sleevePart = arm == HumanoidArm.RIGHT ? model.rightSleeve : model.leftSleeve;
            HumanoidModel.ArmPose prevRightPose = model.rightArmPose;
            HumanoidModel.ArmPose prevLeftPose = model.leftArmPose;
            boolean prevCrouching = model.crouching;
            float prevSwimAmount = model.swimAmount;
            float prevAttackTime = model.attackTime;
            boolean prevRightArmVis = model.rightArm.visible;
            boolean prevLeftArmVis = model.leftArm.visible;
            boolean prevRightSleeveVis = model.rightSleeve.visible;
            boolean prevLeftSleeveVis = model.leftSleeve.visible;
            poseStack.pushPose();
            try {
                boolean needsPlayerUnderlay;
                poseStack.scale(1.005f, 1.005f, 1.005f);
                model.attackTime = 0.0f;
                model.crouching = false;
                model.swimAmount = 0.0f;
                model.rightArmPose = HumanoidModel.ArmPose.EMPTY;
                model.leftArmPose = HumanoidModel.ArmPose.EMPTY;
                model.rightArm.visible = arm == HumanoidArm.RIGHT;
                model.rightSleeve.visible = arm == HumanoidArm.RIGHT;
                model.leftArm.visible = arm == HumanoidArm.LEFT;
                model.leftSleeve.visible = arm == HumanoidArm.LEFT;
                model.setupAnim((LivingEntity)player, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
                if (cancel) {
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    RenderSystem.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
                    if (replaceVanillaArm && baseArmModifier != null) {
                        ResourceLocation baseTex = baseArmModifier.getTexture(modelType);
                        baseVc = buffer.getBuffer(RenderType.entityTranslucent((ResourceLocation)baseTex));
                        whiteOpaque = -1;
                        armPart.render(poseStack, baseVc, light, OverlayTexture.NO_OVERLAY, whiteOpaque);
                        sleevePart.render(poseStack, baseVc, light, OverlayTexture.NO_OVERLAY, whiteOpaque);
                    } else {
                        ResourceLocation baseSkinTex = player.getSkin().texture();
                        baseVc = buffer.getBuffer(RenderType.entitySolid((ResourceLocation)baseSkinTex));
                        whiteOpaque = -1;
                        armPart.render(poseStack, baseVc, light, OverlayTexture.NO_OVERLAY, whiteOpaque);
                        if (!hideSleeve) {
                            sleevePart.render(poseStack, baseVc, light, OverlayTexture.NO_OVERLAY, whiteOpaque);
                        }
                    }
                }
                if (!replaceVanillaArm && (needsPlayerUnderlay = state.getModifiers().stream().anyMatch(m -> m.needsPlayerSkinUnderlay() && FastColor.ARGB32.alpha((int)m.getColor()) < 255))) {
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    RenderSystem.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
                    ResourceLocation baseSkinTex = player.getSkin().texture();
                    VertexConsumer underlayVc = buffer.getBuffer(RenderType.entityTranslucent((ResourceLocation)baseSkinTex));
                    int whiteOpaque = -1;
                    armPart.render(poseStack, underlayVc, light, OverlayTexture.NO_OVERLAY, whiteOpaque);
                    if (!hideSleeve) {
                        sleevePart.render(poseStack, underlayVc, light, OverlayTexture.NO_OVERLAY, whiteOpaque);
                    }
                }
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)false);
                for (SkinModifier modifier : state.getModifiers()) {
                    ResourceLocation overlayTex = modifier.getTexture(modelType);
                    VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent((ResourceLocation)overlayTex));
                    int color = modifier.getColor();
                    armPart.render(poseStack, vc, light, OverlayTexture.NO_OVERLAY, color);
                    sleevePart.render(poseStack, vc, light, OverlayTexture.NO_OVERLAY, color);
                    if (!modifier.hasGlint()) continue;
                    VertexConsumer glintVc = buffer.getBuffer(RenderType.entityGlint());
                    armPart.render(poseStack, glintVc, light, OverlayTexture.NO_OVERLAY, -1);
                    sleevePart.render(poseStack, glintVc, light, OverlayTexture.NO_OVERLAY, -1);
                }
                RenderSystem.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
                RenderSystem.disableBlend();
            }
            finally {
                model.rightArmPose = prevRightPose;
                model.leftArmPose = prevLeftPose;
                model.crouching = prevCrouching;
                model.swimAmount = prevSwimAmount;
                model.attackTime = prevAttackTime;
                model.rightArm.visible = prevRightArmVis;
                model.leftArm.visible = prevLeftArmVis;
                model.rightSleeve.visible = prevRightSleeveVis;
                model.leftSleeve.visible = prevLeftSleeveVis;
                if (arm == HumanoidArm.RIGHT) {
                    REPLACE_FP_RIGHT.remove(id);
                    FP_CANCEL_RIGHT.remove(id);
                    FP_HIDE_SLEEVE_RIGHT.remove(id);
                } else {
                    REPLACE_FP_LEFT.remove(id);
                    FP_CANCEL_LEFT.remove(id);
                    FP_HIDE_SLEEVE_LEFT.remove(id);
                }
                poseStack.popPose();
            }
        }
    }

    @EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT})
    public static final class Wear {
        private static final Map<UUID, boolean[]> PREV_WEAR_VIS = new HashMap<UUID, boolean[]>();

        @SubscribeEvent
        public static void onRenderLivingPre(RenderLivingEvent.Pre<?, ?> event) {
            LivingEntity livingEntity = event.getEntity();
            if (!(livingEntity instanceof AbstractClientPlayer)) {
                return;
            }
            AbstractClientPlayer player = (AbstractClientPlayer)livingEntity;
            if (!(event.getRenderer() instanceof PlayerRenderer)) {
                return;
            }
            SkinModifierState state = SkinModifierManager.getPlayerSkinState(player);
            if (state == null || !state.hasModifiers()) {
                return;
            }
            EnumSet<SkinModifier.HideVanilla> hide = state.getHideMask();
            if (hide.isEmpty()) {
                return;
            }
            PlayerModel parentModel = (PlayerModel)((PlayerRenderer)event.getRenderer()).getModel();
            if (!(parentModel instanceof PlayerModel)) {
                return;
            }
            PlayerModel pmAny = parentModel;
            PlayerModel model = pmAny;
            PREV_WEAR_VIS.put(player.getUUID(), new boolean[]{model.hat.visible, model.jacket.visible, model.leftSleeve.visible, model.rightSleeve.visible, model.leftPants.visible, model.rightPants.visible});
            if (hide.contains((Object)SkinModifier.HideVanilla.HAT)) {
                model.hat.visible = false;
            }
            if (hide.contains((Object)SkinModifier.HideVanilla.JACKET)) {
                model.jacket.visible = false;
            }
            if (hide.contains((Object)SkinModifier.HideVanilla.LEFT_SLEEVE)) {
                model.leftSleeve.visible = false;
            }
            if (hide.contains((Object)SkinModifier.HideVanilla.RIGHT_SLEEVE)) {
                model.rightSleeve.visible = false;
            }
            if (hide.contains((Object)SkinModifier.HideVanilla.LEFT_PANTS)) {
                model.leftPants.visible = false;
            }
            if (hide.contains((Object)SkinModifier.HideVanilla.RIGHT_PANTS)) {
                model.rightPants.visible = false;
            }
        }

        @SubscribeEvent
        public static void onRenderLivingPost(RenderLivingEvent.Post<?, ?> event) {
            LivingEntity livingEntity = event.getEntity();
            if (!(livingEntity instanceof AbstractClientPlayer)) {
                return;
            }
            AbstractClientPlayer player = (AbstractClientPlayer)livingEntity;
            if (!(event.getRenderer() instanceof PlayerRenderer)) {
                return;
            }
            boolean[] prev = PREV_WEAR_VIS.remove(player.getUUID());
            if (prev == null) {
                return;
            }
            PlayerModel parentModel = (PlayerModel)((PlayerRenderer)event.getRenderer()).getModel();
            if (!(parentModel instanceof PlayerModel)) {
                return;
            }
            PlayerModel pmAny = parentModel;
            PlayerModel model = pmAny;
            model.hat.visible = prev[0];
            model.jacket.visible = prev[1];
            model.leftSleeve.visible = prev[2];
            model.rightSleeve.visible = prev[3];
            model.leftPants.visible = prev[4];
            model.rightPants.visible = prev[5];
        }
    }
}

