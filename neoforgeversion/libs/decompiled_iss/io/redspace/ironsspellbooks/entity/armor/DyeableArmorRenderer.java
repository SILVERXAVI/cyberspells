/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.Minecraft
 *  net.minecraft.world.item.Item
 *  software.bernie.geckolib.cache.object.GeoBone
 *  software.bernie.geckolib.model.GeoModel
 */
package io.redspace.ironsspellbooks.entity.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

public class DyeableArmorRenderer<T extends Item>
extends GenericCustomArmorRenderer<T> {
    public DyeableArmorRenderer(GeoModel<T> model) {
        super(model);
    }

    public void renderCubesOfBone(PoseStack poseStack, GeoBone bone, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        color = -1;
        if (bone.getName().startsWith("dye") && this.currentStack != null) {
            color = Minecraft.getInstance().getItemColors().getColor(this.currentStack, 0) | 0xFF000000;
        }
        super.renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, color);
    }
}

