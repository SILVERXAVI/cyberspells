/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.ArmorItem
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 *  top.theillusivec4.curios.api.type.capability.ICurioItem
 */
package io.redspace.ironsspellbooks.api.spells;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainerMutable;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.spells.SpellSlot;
import io.redspace.ironsspellbooks.capabilities.magic.SpellContainer;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import java.util.List;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public interface ISpellContainer {
    @NotNull
    public SpellSlot[] getAllSpells();

    @NotNull
    public List<SpellSlot> getActiveSpells();

    public int getMaxSpellCount();

    public int getActiveSpellCount();

    public int getNextAvailableIndex();

    public boolean mustEquip();

    public boolean isImproved();

    public boolean isSpellWheel();

    @NotNull
    public SpellData getSpellAtIndex(int var1);

    public int getIndexForSpell(AbstractSpell var1);

    public boolean isEmpty();

    public ISpellContainerMutable mutableCopy();

    public static boolean isSpellContainer(ItemStack itemStack) {
        return itemStack != null && !itemStack.isEmpty() && itemStack.has(ComponentRegistry.SPELL_CONTAINER);
    }

    public static ISpellContainer create(int maxSpells, boolean addsToSpellWheel, boolean mustBeEquipped) {
        return new SpellContainer(maxSpells, addsToSpellWheel, mustBeEquipped);
    }

    public static ISpellContainer createScrollContainer(AbstractSpell spell, int spellLevel, ItemStack itemStack) {
        ISpellContainerMutable spellContainer = ISpellContainer.create(1, false, false).mutableCopy();
        spellContainer.addSpellAtIndex(spell, spellLevel, 0, true);
        ISpellContainer i = spellContainer.toImmutable();
        ISpellContainer.set(itemStack, i);
        return i;
    }

    public static ISpellContainer createImbuedContainer(AbstractSpell spell, int spellLevel, ItemStack itemStack) {
        ISpellContainerMutable spellContainer = ISpellContainer.create(1, true, itemStack.getItem() instanceof ArmorItem || itemStack.getItem() instanceof ICurioItem).mutableCopy();
        spellContainer.addSpellAtIndex(spell, spellLevel, 0, true);
        ISpellContainer i = spellContainer.toImmutable();
        ISpellContainer.set(itemStack, i);
        return i;
    }

    public static ISpellContainer get(ItemStack itemStack) {
        return (ISpellContainer)itemStack.get(ComponentRegistry.SPELL_CONTAINER);
    }

    public static ISpellContainer getOrCreate(ItemStack itemStack) {
        return (ISpellContainer)itemStack.getOrDefault(ComponentRegistry.SPELL_CONTAINER, (Object)new SpellContainer(1, true, false));
    }

    public static void set(ItemStack stack, ISpellContainer container) {
        stack.set(ComponentRegistry.SPELL_CONTAINER, (Object)container);
    }

    public static void remove(ItemStack stack) {
        stack.remove(ComponentRegistry.SPELL_CONTAINER);
    }
}

