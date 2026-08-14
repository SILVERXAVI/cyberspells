/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.LivingEntity
 *  net.neoforged.bus.api.Event
 *  net.neoforged.bus.api.ICancellableEvent
 */
package com.maxwell.cyber_ware_port.api.event;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class CyberwareSurgeryEvent
extends Event {
    private final LivingEntity patient;
    private final RobosurgeonBlockEntity blockEntity;

    public CyberwareSurgeryEvent(LivingEntity patient, RobosurgeonBlockEntity blockEntity) {
        this.patient = patient;
        this.blockEntity = blockEntity;
    }

    public LivingEntity getPatient() {
        return this.patient;
    }

    public RobosurgeonBlockEntity getRobosurgeon() {
        return this.blockEntity;
    }

    public static class Post
    extends CyberwareSurgeryEvent {
        public Post(LivingEntity patient, RobosurgeonBlockEntity blockEntity) {
            super(patient, blockEntity);
        }
    }

    public static class Pre
    extends CyberwareSurgeryEvent
    implements ICancellableEvent {
        private Component denialReason;

        public Pre(LivingEntity patient, RobosurgeonBlockEntity blockEntity) {
            super(patient, blockEntity);
        }

        public Component getDenialReason() {
            return this.denialReason;
        }

        public void setDenialReason(Component reason) {
            this.denialReason = reason;
        }
    }
}

