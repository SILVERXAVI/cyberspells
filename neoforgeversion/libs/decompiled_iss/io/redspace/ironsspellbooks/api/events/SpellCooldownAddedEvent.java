/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.bus.api.Event
 *  net.neoforged.bus.api.ICancellableEvent
 */
package io.redspace.ironsspellbooks.api.events;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public abstract class SpellCooldownAddedEvent
extends Event {
    private final AbstractSpell spell;
    private int effectiveCooldown;
    private final Player entity;
    private CastSource castSource;

    public SpellCooldownAddedEvent(int effectiveCooldown, AbstractSpell spell, Player entity, CastSource castSource) {
        this.spell = spell;
        this.effectiveCooldown = effectiveCooldown;
        this.entity = entity;
        this.castSource = castSource;
    }

    public AbstractSpell getSpell() {
        return this.spell;
    }

    public int getEffectiveCooldown() {
        return this.effectiveCooldown;
    }

    private void setEffectiveCooldown(int effectiveCooldown) {
        this.effectiveCooldown = effectiveCooldown;
    }

    public CastSource getCastSource() {
        return this.castSource;
    }

    public Player getEntity() {
        return this.entity;
    }

    public static class Post
    extends SpellCooldownAddedEvent {
        public Post(int effectiveCooldown, AbstractSpell spell, Player entity, CastSource castSource) {
            super(effectiveCooldown, spell, entity, castSource);
        }
    }

    public static class Pre
    extends SpellCooldownAddedEvent
    implements ICancellableEvent {
        public Pre(int effectiveCooldown, AbstractSpell spell, Player entity, CastSource castSource) {
            super(effectiveCooldown, spell, entity, castSource);
        }

        @Override
        public void setEffectiveCooldown(int newCooldown) {
            super.setEffectiveCooldown(newCooldown);
        }
    }
}

