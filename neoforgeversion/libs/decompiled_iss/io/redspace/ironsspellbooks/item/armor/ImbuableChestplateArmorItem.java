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
 */
package io.redspace.ironsspellbooks.item.armor;

import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.item.armor.ExtendedArmorItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class ImbuableChestplateArmorItem
extends ExtendedArmorItem
implements IPresetSpellContainer {
    public ImbuableChestplateArmorItem(Holder<ArmorMaterial> pMaterial, ArmorItem.Type pType, Item.Properties pProperties, AttributeContainer ... attributes) {
        super(pMaterial, pType, pProperties, attributes);
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        ArmorItem armorItem;
        if (itemStack == null) {
            return;
        }
        Item item = itemStack.getItem();
        if (item instanceof ArmorItem && (armorItem = (ArmorItem)item).getType() == ArmorItem.Type.CHESTPLATE && !ISpellContainer.isSpellContainer(itemStack)) {
            ISpellContainer spellContainer = ISpellContainer.create(1, true, true);
            ISpellContainer.set(itemStack, spellContainer);
        }
    }
}

