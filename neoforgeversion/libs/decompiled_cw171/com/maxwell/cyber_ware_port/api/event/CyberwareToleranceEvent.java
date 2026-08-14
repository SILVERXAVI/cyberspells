/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.neoforged.bus.api.Event
 */
package com.maxwell.cyber_ware_port.api.event;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;

public class CyberwareToleranceEvent
extends Event {
    private final LivingEntity entity;
    private final int originalTolerance;
    private int newTolerance;

    public CyberwareToleranceEvent(LivingEntity entity, int originalTolerance) {
        this.entity = entity;
        this.originalTolerance = originalTolerance;
        this.newTolerance = originalTolerance;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public int getOriginalTolerance() {
        return this.originalTolerance;
    }

    public int getNewTolerance() {
        return this.newTolerance;
    }

    public void setNewTolerance(int newTolerance) {
        this.newTolerance = newTolerance;
    }
}

