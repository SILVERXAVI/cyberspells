/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ArmorItem$Type
 *  net.minecraft.world.item.ArmorMaterial
 *  net.minecraft.world.item.Item$Properties
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  software.bernie.geckolib.renderer.GeoArmorRenderer
 */
package io.redspace.ironsspellbooks.item.armor;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.entity.armor.PyromancerArmorModel;
import io.redspace.ironsspellbooks.item.armor.IArmorCapeProvider;
import io.redspace.ironsspellbooks.item.armor.ImbuableChestplateArmorItem;
import io.redspace.ironsspellbooks.registries.ArmorMaterialRegistry;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class PyromancerArmorItem
extends ImbuableChestplateArmorItem
implements IArmorCapeProvider {
    public PyromancerArmorItem(ArmorItem.Type slot, Item.Properties settings) {
        super((Holder<ArmorMaterial>)ArmorMaterialRegistry.SCHOOL, slot, settings, PyromancerArmorItem.schoolAttributes(AttributeRegistry.FIRE_SPELL_POWER));
    }

    @Override
    public ResourceLocation getCapeResourceLocation() {
        return ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/models/armor/pyromancer_cape.png");
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer<PyromancerArmorItem>(new PyromancerArmorModel());
    }
}

