/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.bus.api.ICancellableEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent
 */
package io.redspace.ironsspellbooks.api.events;

import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class SpellPreCastEvent
extends PlayerEvent
implements ICancellableEvent {
    private final String spellId;
    private final SchoolType schoolType;
    private final CastSource castSource;
    private final int spellLevel;

    public SpellPreCastEvent(Player player, String spellId, int spellLevel, SchoolType schoolType, CastSource castSource) {
        super(player);
        this.spellId = spellId;
        this.spellLevel = spellLevel;
        this.schoolType = schoolType;
        this.castSource = castSource;
    }

    public String getSpellId() {
        return this.spellId;
    }

    public SchoolType getSchoolType() {
        return this.schoolType;
    }

    public int getSpellLevel() {
        return this.spellLevel;
    }

    public CastSource getCastSource() {
        return this.castSource;
    }
}

