/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemStack
 */
package io.redspace.ironsspellbooks.item;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public interface ILecternPlaceable {
    public List<Component> getPages(ItemStack var1);

    default public Optional<ResourceLocation> simpleTextureOverride(ItemStack stack) {
        return Optional.empty();
    }

    default public void handleCustomLecternPosing(PoseStack poseStack) {
    }
}

