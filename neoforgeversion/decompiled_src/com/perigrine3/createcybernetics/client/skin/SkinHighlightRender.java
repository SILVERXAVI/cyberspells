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
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.client.renderer.entity.player.PlayerRenderer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.client.resources.PlayerSkin$Model
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.HumanoidArm
 *  net.minecraft.world.entity.LivingEntity
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.EventPriority
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.EntityRenderersEvent$AddLayers
 *  net.neoforged.neoforge.client.event.RenderArmEvent
 */
package com.perigrine3.createcybernetics.client.skin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.perigrine3.createcybernetics.client.skin.SkinHighlight;
import com.perigrine3.createcybernetics.client.skin.SkinHighlightLayer;
import com.perigrine3.createcybernetics.client.skin.SkinModifier;
import com.perigrine3.createcybernetics.client.skin.SkinModifierManager;
import com.perigrine3.createcybernetics.client.skin.SkinModifierState;
import com.perigrine3.createcybernetics.client.skin.SkinRenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RenderArmEvent;

@EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT})
public final class SkinHighlightRender {
    private SkinHighlightRender() {
    }

    public static void apply(SkinModifierState state, boolean enabled, ResourceLocation wide, ResourceLocation slim, int color, boolean emissive) {
        SkinHighlightRender.apply(state, enabled, wide, slim, color, emissive, false);
    }

    public static void apply(SkinModifierState state, boolean enabled, ResourceLocation wide, ResourceLocation slim, int color, boolean emissive, boolean tintOnEmissive) {
        if (state == null) {
            return;
        }
        if (!enabled) {
            state.clearHighlights();
            return;
        }
        state.addHighlight(new SkinHighlight(wide, slim, color, emissive, tintOnEmissive));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @SubscribeEvent(priority=EventPriority.LOWEST, receiveCanceled=true)
    public static void onRenderArm(RenderArmEvent event) {
        AbstractClientPlayer player = event.getPlayer();
        SkinModifierState state = SkinModifierManager.getPlayerSkinState(player);
        if (state == null || !state.hasHighlights()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        EntityRenderer renderer = mc.getEntityRenderDispatcher().getRenderer((Entity)player);
        if (!(renderer instanceof PlayerRenderer)) {
            return;
        }
        PlayerRenderer playerRenderer = (PlayerRenderer)renderer;
        HumanoidArm arm = event.getArm();
        PlayerModel model = (PlayerModel)playerRenderer.getModel();
        PlayerSkin.Model modelType = player.getSkin().model();
        ModelPart armPart = arm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
        ModelPart sleevePart = arm == HumanoidArm.RIGHT ? model.rightSleeve : model.leftSleeve;
        boolean hideSleeve = arm == HumanoidArm.RIGHT ? state.getHideMask().contains((Object)SkinModifier.HideVanilla.RIGHT_SLEEVE) : state.getHideMask().contains((Object)SkinModifier.HideVanilla.LEFT_SLEEVE);
        HumanoidModel.ArmPose prevRightPose = model.rightArmPose;
        HumanoidModel.ArmPose prevLeftPose = model.leftArmPose;
        boolean prevCrouching = model.crouching;
        float prevSwimAmount = model.swimAmount;
        float prevAttackTime = model.attackTime;
        boolean prevRightArmVis = model.rightArm.visible;
        boolean prevLeftArmVis = model.leftArm.visible;
        boolean prevRightSleeveVis = model.rightSleeve.visible;
        boolean prevLeftSleeveVis = model.leftSleeve.visible;
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource buffer = event.getMultiBufferSource();
        poseStack.pushPose();
        try {
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
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            for (SkinHighlight highlight : state.getHighlights()) {
                int color;
                RenderType rt;
                int light;
                if (highlight == null) continue;
                ResourceLocation tex = highlight.getTexture(modelType);
                boolean emissive = highlight.isEmissive();
                boolean tintOnEmissive = highlight.tintOnEmissive();
                if (emissive) {
                    light = 0xF000F0;
                    if (tintOnEmissive) {
                        rt = SkinRenderTypes.emissiveTinted(tex);
                        color = highlight.getColor();
                    } else {
                        rt = RenderType.entityTranslucent((ResourceLocation)tex);
                        color = -1;
                    }
                } else {
                    light = event.getPackedLight();
                    rt = RenderType.entityTranslucent((ResourceLocation)tex);
                    color = highlight.getColor();
                }
                VertexConsumer vc = buffer.getBuffer(rt);
                armPart.render(poseStack, vc, light, OverlayTexture.NO_OVERLAY, color);
                if (hideSleeve) continue;
                sleevePart.render(poseStack, vc, light, OverlayTexture.NO_OVERLAY, color);
            }
        }
        finally {
            RenderSystem.disableBlend();
            model.rightArmPose = prevRightPose;
            model.leftArmPose = prevLeftPose;
            model.crouching = prevCrouching;
            model.swimAmount = prevSwimAmount;
            model.attackTime = prevAttackTime;
            model.rightArm.visible = prevRightArmVis;
            model.leftArm.visible = prevLeftArmVis;
            model.rightSleeve.visible = prevRightSleeveVis;
            model.leftSleeve.visible = prevLeftSleeveVis;
            poseStack.popPose();
        }
    }

    @EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT}, bus=EventBusSubscriber.Bus.MOD)
    public static final class Layers {
        private Layers() {
        }

        @SubscribeEvent
        public static void addLayers(EntityRenderersEvent.AddLayers event) {
            PlayerRenderer slim;
            PlayerRenderer wide = (PlayerRenderer)event.getSkin(PlayerSkin.Model.WIDE);
            if (wide != null) {
                wide.addLayer((RenderLayer)new SkinHighlightLayer((RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>)wide));
            }
            if ((slim = (PlayerRenderer)event.getSkin(PlayerSkin.Model.SLIM)) != null) {
                slim.addLayer((RenderLayer)new SkinHighlightLayer((RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>)slim));
            }
        }
    }
}

