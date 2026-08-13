/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.api.spells;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.spells.SpellSlot;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface ISpellContainerMutable {
    public void setMaxSpellCount(int var1);

    public void setImproved(boolean var1);

    public boolean addSpellAtIndex(AbstractSpell var1, int var2, int var3, boolean var4);

    public boolean addSpell(AbstractSpell var1, int var2, boolean var3);

    public boolean removeSpellAtIndex(int var1);

    public boolean removeSpell(AbstractSpell var1);

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

    public ISpellContainer toImmutable();
}

