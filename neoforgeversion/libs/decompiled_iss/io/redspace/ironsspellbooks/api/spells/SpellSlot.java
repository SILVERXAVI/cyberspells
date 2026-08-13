/*
 * Decompiled with CFR 0.152.
 */
package io.redspace.ironsspellbooks.api.spells;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SpellData;

public record SpellSlot(SpellData spellData, int index) {
    public AbstractSpell getSpell() {
        return this.spellData.getSpell();
    }

    public int getLevel() {
        return this.spellData.getLevel();
    }

    public boolean isLocked() {
        return this.spellData.isLocked();
    }

    public static SpellSlot of(SpellData data, int index) {
        return new SpellSlot(data, index);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof SpellSlot)) return false;
        SpellSlot o = (SpellSlot)obj;
        if (!o.spellData.equals(this.spellData)) return false;
        if (o.index != this.index) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return this.spellData.hashCode() * 31 + this.index;
    }
}

