/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.bus.api.Event
 *  net.neoforged.bus.api.ICancellableEvent
 */
package com.maxwell.cyber_ware_port.api.event;

import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class CyberwareAbilityEvent
extends Event
implements ICancellableEvent {
    private final LivingEntity entity;
    private final ItemStack stack;
    private final ICyberware cyberware;

    public CyberwareAbilityEvent(LivingEntity entity, ItemStack stack, ICyberware cyberware) {
        this.entity = entity;
        this.stack = stack;
        this.cyberware = cyberware;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public ICyberware getCyberware() {
        return this.cyberware;
    }
}

