/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.item.ArmorItem$Type
 *  net.minecraft.world.item.ArmorMaterial
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  software.bernie.geckolib.renderer.GeoArmorRenderer
 */
package io.redspace.ironsspellbooks.item.armor;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.entity.armor.TarnishedCrownModel;
import io.redspace.ironsspellbooks.item.armor.ExtendedArmorItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.registries.ArmorMaterialRegistry;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class TarnishedCrownArmorItem
extends ExtendedArmorItem
implements IPresetSpellContainer {
    public TarnishedCrownArmorItem(ArmorItem.Type slot, Item.Properties settings) {
        super((Holder<ArmorMaterial>)ArmorMaterialRegistry.TARNISHED, slot, settings, new AttributeContainer((Holder<Attribute>)AttributeRegistry.MAX_MANA, 150.0, AttributeModifier.Operation.ADD_VALUE), new AttributeContainer((Holder<Attribute>)AttributeRegistry.MANA_REGEN, 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), new AttributeContainer((Holder<Attribute>)Attributes.ATTACK_DAMAGE, -0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer<TarnishedCrownArmorItem>(new TarnishedCrownModel());
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }
        if (!ISpellContainer.isSpellContainer(itemStack)) {
            ISpellContainer spellContainer = ISpellContainer.create(1, true, true);
            ISpellContainer.set(itemStack, spellContainer);
        }
    }
}

