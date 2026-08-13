/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.bus.api.Event
 */
package io.redspace.ironsspellbooks.api.config;

import io.redspace.ironsspellbooks.api.config.SpellConfigHolder;
import io.redspace.ironsspellbooks.api.config.SpellConfigParameter;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.neoforged.bus.api.Event;

public class ModifyDefaultConfigValuesEvent
extends Event {
    private final AbstractSpell spell;
    private final SpellConfigHolder config;

    public ModifyDefaultConfigValuesEvent(AbstractSpell spell, SpellConfigHolder spellConfigHolder) {
        this.spell = spell;
        this.config = spellConfigHolder;
    }

    public <T> void setDefaultValue(SpellConfigParameter<T> type, T value) {
        this.config.setDefaultValue(type, value);
    }

    public AbstractSpell getSpell() {
        return this.spell;
    }
}

