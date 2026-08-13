/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.world.item.ArmorItem
 *  net.minecraft.world.item.ArmorItem$Type
 *  net.minecraft.world.item.ArmorMaterial
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.renderer.GeoArmorRenderer
 */
package io.redspace.ironsspellbooks.item.armor;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.armor.DyeableArmorRenderer;
import io.redspace.ironsspellbooks.entity.armor.GenericArmorModel;
import io.redspace.ironsspellbooks.item.armor.IDisableJacket;
import io.redspace.ironsspellbooks.item.armor.ImbuableChestplateArmorItem;
import io.redspace.ironsspellbooks.registries.ArmorMaterialRegistry;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class WizardArmorItem
extends ImbuableChestplateArmorItem
implements IDisableJacket {
    private static final String descIdHat = "item.irons_spellbooks.wizard_helmet.hat";
    private static final String descIdHood = "item.irons_spellbooks.wizard_helmet.hood";

    public WizardArmorItem(ArmorItem.Type type, Item.Properties settings) {
        super((Holder<ArmorMaterial>)ArmorMaterialRegistry.SCHOOL, type, settings, WizardArmorItem.withManaAndSpellPowerAttribute(125, 0.05));
    }

    @NotNull
    public String getDescriptionId(ItemStack stack) {
        ArmorItem armorItem;
        Item item;
        if (stack == null || !((item = stack.getItem()) instanceof ArmorItem) || (armorItem = (ArmorItem)item).getType() != ArmorItem.Type.HELMET) {
            return super.getDescriptionId(stack);
        }
        return ((String)stack.getOrDefault(ComponentRegistry.CLOTHING_VARIANT, (Object)"")).equals("hat") ? descIdHat : descIdHood;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new DyeableArmorRenderer(new GenericArmorModel("wizard").variants(Map.of("hat", IronsSpellbooks.id("geo/wizard_armor_hat.geo.json"))));
    }
}

