/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.world.item.ArmorItem$Type
 *  net.minecraft.world.item.ArmorMaterial
 *  net.minecraft.world.item.Item$Properties
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  software.bernie.geckolib.renderer.GeoArmorRenderer
 */
package io.redspace.ironsspellbooks.item.armor;

import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.entity.armor.WanderingMagicianModel;
import io.redspace.ironsspellbooks.item.armor.ExtendedArmorItem;
import io.redspace.ironsspellbooks.registries.ArmorMaterialRegistry;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class WanderingMagicianArmorItem
extends ExtendedArmorItem {
    public WanderingMagicianArmorItem(ArmorItem.Type slot, Item.Properties settings) {
        super((Holder<ArmorMaterial>)ArmorMaterialRegistry.WANDERING_MAGICIAN, slot, settings, WanderingMagicianArmorItem.withManaAttribute(25));
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer<WanderingMagicianArmorItem>(new WanderingMagicianModel());
    }
}

