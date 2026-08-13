/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.bus.api.ICancellableEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent
 */
package io.redspace.ironsspellbooks.api.events;

import io.redspace.ironsspellbooks.api.spells.SpellData;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class InscribeSpellEvent
extends PlayerEvent
implements ICancellableEvent {
    private final SpellData spellData;

    public InscribeSpellEvent(Player player, SpellData spellData) {
        super(player);
        this.spellData = spellData;
    }

    public SpellData getSpellData() {
        return this.spellData;
    }
}

