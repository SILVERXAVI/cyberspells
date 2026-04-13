/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.model.Model
 *  net.minecraft.client.model.PlayerModel
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.resources.PlayerSkin$Model
 *  net.minecraft.resources.ResourceLocation
 */
package com.perigrine3.createcybernetics.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.perigrine3.createcybernetics.client.model.AttachmentAnchor;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;

public interface PlayerAttachment {
    public AttachmentAnchor anchor();

    public ResourceLocation texture(PlayerSkin.Model var1);

    public Model model(PlayerSkin.Model var1);

    default public int color() {
        return -1;
    }

    default public void setupPose(PoseStack poseStack, AbstractClientPlayer player, PlayerModel<AbstractClientPlayer> parentModel, PlayerSkin.Model modelType, float partialTick) {
    }

    default public boolean respectsInvisibility() {
        return true;
    }

    default public boolean thirdPersonOnly() {
        return false;
    }
}

