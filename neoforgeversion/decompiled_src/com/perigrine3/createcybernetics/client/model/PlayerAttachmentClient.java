/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.PlayerModel
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.client.renderer.entity.player.PlayerRenderer
 *  net.minecraft.client.resources.PlayerSkin$Model
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.EntityRenderersEvent$AddLayers
 *  net.neoforged.neoforge.client.event.EntityRenderersEvent$RegisterLayerDefinitions
 */
package com.perigrine3.createcybernetics.client.model;

import com.perigrine3.createcybernetics.client.model.CalfPropellerAttachmentModel;
import com.perigrine3.createcybernetics.client.model.ClawAttachmentModel;
import com.perigrine3.createcybernetics.client.model.DrillFistAttachmentModel;
import com.perigrine3.createcybernetics.client.model.GuardianEyeAttachmentModel;
import com.perigrine3.createcybernetics.client.model.OcelotPawsAttachmentModel;
import com.perigrine3.createcybernetics.client.model.PlayerAttachmentLayer;
import com.perigrine3.createcybernetics.client.model.SpursAttachmentModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT}, bus=EventBusSubscriber.Bus.MOD)
public final class PlayerAttachmentClient {
    private PlayerAttachmentClient() {
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ClawAttachmentModel.LAYER, ClawAttachmentModel::createLayer);
        event.registerLayerDefinition(DrillFistAttachmentModel.LAYER, DrillFistAttachmentModel::createLayer);
        event.registerLayerDefinition(OcelotPawsAttachmentModel.LAYER, OcelotPawsAttachmentModel::createLayer);
        event.registerLayerDefinition(CalfPropellerAttachmentModel.LAYER, CalfPropellerAttachmentModel::createLayer);
        event.registerLayerDefinition(SpursAttachmentModel.LAYER, SpursAttachmentModel::createLayer);
        event.registerLayerDefinition(GuardianEyeAttachmentModel.LAYER, GuardianEyeAttachmentModel::createLayer);
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        PlayerRenderer slim;
        PlayerRenderer wide = (PlayerRenderer)event.getSkin(PlayerSkin.Model.WIDE);
        if (wide != null) {
            wide.addLayer((RenderLayer)new PlayerAttachmentLayer((RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>)wide));
        }
        if ((slim = (PlayerRenderer)event.getSkin(PlayerSkin.Model.SLIM)) != null) {
            slim.addLayer((RenderLayer)new PlayerAttachmentLayer((RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>)slim));
        }
    }
}

