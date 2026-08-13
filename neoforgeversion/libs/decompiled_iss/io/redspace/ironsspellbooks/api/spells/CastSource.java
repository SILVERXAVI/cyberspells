/*
 * Decompiled with CFR 0.152.
 */
package io.redspace.ironsspellbooks.api.spells;

import io.redspace.ironsspellbooks.config.ServerConfigs;

public enum CastSource {
    SPELLBOOK,
    SCROLL,
    SWORD,
    MOB,
    COMMAND,
    NONE;


    public boolean consumesMana() {
        return this == SPELLBOOK || this == SWORD && (Boolean)ServerConfigs.SWORDS_CONSUME_MANA.get() != false;
    }

    public boolean respectsCooldown() {
        return this == SPELLBOOK || this == SWORD;
    }
}

