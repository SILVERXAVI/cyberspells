/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.model.BakedModel
 *  net.minecraft.client.resources.model.ModelBakery
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemStack
 */
package io.redspace.ironsspellbooks.render;

import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.render.NBTOverrideItemModel;
import java.util.Optional;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ScrollModel
extends NBTOverrideItemModel {
    public ScrollModel(BakedModel original, ModelBakery loader) {
        super(original, loader);
    }

    @Override
    Optional<ResourceLocation> getModelFromStack(ItemStack itemStack) {
        if (ISpellContainer.isSpellContainer(itemStack)) {
            SchoolType school = ISpellContainer.get(itemStack).getSpellAtIndex(0).getSpell().getSchoolType();
            return Optional.of(ScrollModel.getScrollModelLocation(school));
        }
        return Optional.empty();
    }

    public static ResourceLocation getScrollModelLocation(SchoolType schoolType) {
        return ResourceLocation.fromNamespaceAndPath((String)schoolType.getId().getNamespace(), (String)String.format("item/scroll_%s", schoolType.getId().getPath()));
    }
}

