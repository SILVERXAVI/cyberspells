/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Tier
 */
package io.redspace.ironsspellbooks.api.item.weapons;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainerMutable;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import java.util.Arrays;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class MagicSwordItem
extends ExtendedSwordItem
implements IPresetSpellContainer {
    List<SpellData> spellData = null;
    SpellDataRegistryHolder[] spellDataRegistryHolders;

    public MagicSwordItem(Tier pTier, Item.Properties pProperties, SpellDataRegistryHolder[] spellDataRegistryHolders) {
        super(pTier, pProperties);
        this.spellDataRegistryHolders = spellDataRegistryHolders;
    }

    public List<SpellData> getSpells() {
        if (this.spellData == null) {
            this.spellData = Arrays.stream(this.spellDataRegistryHolders).map(SpellDataRegistryHolder::getSpellData).toList();
            this.spellDataRegistryHolders = null;
        }
        return this.spellData;
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }
        if (!ISpellContainer.isSpellContainer(itemStack)) {
            List<SpellData> spells = this.getSpells();
            ISpellContainerMutable spellContainer = ISpellContainer.create(spells.size(), true, false).mutableCopy();
            spells.forEach(spellData -> spellContainer.addSpell(spellData.getSpell(), spellData.getLevel(), true));
            ISpellContainer.set(itemStack, spellContainer.toImmutable());
        }
    }
}

