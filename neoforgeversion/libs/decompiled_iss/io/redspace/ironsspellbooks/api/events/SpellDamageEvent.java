/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.neoforged.bus.api.ICancellableEvent
 *  net.neoforged.neoforge.event.entity.living.LivingEvent
 */
package io.redspace.ironsspellbooks.api.events;

import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class SpellDamageEvent
extends LivingEvent
implements ICancellableEvent {
    private final SpellDamageSource spellDamageSource;
    private final float baseAmount;
    private float amount;

    public SpellDamageEvent(LivingEntity livingEntity, float amount, SpellDamageSource spellDamageSource) {
        super(livingEntity);
        this.spellDamageSource = spellDamageSource;
        this.amount = this.baseAmount = amount;
    }

    public float getOriginalAmount() {
        return this.baseAmount;
    }

    public float getAmount() {
        return this.amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public SpellDamageSource getSpellDamageSource() {
        return this.spellDamageSource;
    }
}

