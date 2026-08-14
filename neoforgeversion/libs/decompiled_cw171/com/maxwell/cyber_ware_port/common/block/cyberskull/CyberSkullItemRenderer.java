/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.model.SkullModel
 *  net.minecraft.client.model.SkullModelBase
 *  net.minecraft.client.model.geom.EntityModelSet
 *  net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher
 *  net.minecraft.client.renderer.blockentity.SkullBlockRenderer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.packs.resources.ResourceManager
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.minecraft.world.item.ItemStack
 */
package com.maxwell.cyber_ware_port.common.block.cyberskull;

import com.maxwell.cyber_ware_port.client.ModClientEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CyberSkullItemRenderer
extends BlockEntityWithoutLevelRenderer {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/entity/cyber_wither_skeleton.png");
    private final SkullModel model;

    public CyberSkullItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
        this.model = new SkullModel(modelSet.bakeLayer(ModClientEvents.CYBER_SKULL_LAYER));
    }

    public void onResourceManagerReload(ResourceManager manager) {
    }

    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        SkullBlockRenderer.renderSkull(null, (float)180.0f, (float)0.0f, (PoseStack)poseStack, (MultiBufferSource)buffer, (int)packedLight, (SkullModelBase)this.model, (RenderType)RenderType.entityTranslucent((ResourceLocation)TEXTURE));
    }
}

