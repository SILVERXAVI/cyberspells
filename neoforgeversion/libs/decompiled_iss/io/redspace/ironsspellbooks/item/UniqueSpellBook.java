/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.ItemStack
 */
package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainerMutable;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.UniqueItem;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import java.util.Arrays;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class UniqueSpellBook
extends SpellBook
implements UniqueItem {
    List<SpellData> spellData = null;
    SpellDataRegistryHolder[] spellDataRegistryHolders;

    public UniqueSpellBook(SpellDataRegistryHolder[] spellDataRegistryHolders) {
        super(spellDataRegistryHolders.length);
        this.spellDataRegistryHolders = spellDataRegistryHolders;
    }

    public UniqueSpellBook(SpellDataRegistryHolder[] spellDataRegistryHolders, int additionalSlots) {
        super(spellDataRegistryHolders.length + additionalSlots);
        this.spellDataRegistryHolders = spellDataRegistryHolders;
    }

    public List<SpellData> getSpells() {
        if (this.spellData == null) {
            this.spellData = Arrays.stream(this.spellDataRegistryHolders).map(SpellDataRegistryHolder::getSpellData).toList();
            this.spellDataRegistryHolders = null;
        }
        return this.spellData;
    }

    public Component getName(ItemStack stack) {
        return stack.has(ComponentRegistry.SPELL_CONTAINER) && ((ISpellContainer)stack.get(ComponentRegistry.SPELL_CONTAINER)).isImproved() ? Component.translatable((String)"tooltip.irons_spellbooks.improved_format", (Object[])new Object[]{super.getName(stack)}) : super.getName(stack);
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }
        if (!ISpellContainer.isSpellContainer(itemStack)) {
            ISpellContainerMutable spellContainer = ISpellContainer.create(this.getMaxSpellSlots(), true, true).mutableCopy();
            this.getSpells().forEach(spellSlot -> spellContainer.addSpell(spellSlot.getSpell(), spellSlot.getLevel(), true));
            ISpellContainer.set(itemStack, spellContainer.toImmutable());
        }
    }
}

