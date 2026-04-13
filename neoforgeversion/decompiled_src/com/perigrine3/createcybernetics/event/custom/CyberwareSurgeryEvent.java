/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.bus.api.Event
 */
package com.perigrine3.createcybernetics.event.custom;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;

public final class CyberwareSurgeryEvent
extends Event {
    private final ServerPlayer player;
    private final int installs;
    private final int removals;
    private final List<Change> installed;
    private final List<Change> removed;

    public CyberwareSurgeryEvent(ServerPlayer player, int installs, int removals, List<Change> installed, List<Change> removed) {
        this.player = player;
        this.installs = installs;
        this.removals = removals;
        this.installed = installed;
        this.removed = removed;
    }

    public ServerPlayer getPlayer() {
        return this.player;
    }

    public int getInstalls() {
        return this.installs;
    }

    public int getRemovals() {
        return this.removals;
    }

    public List<Change> getInstalled() {
        return this.installed;
    }

    public List<Change> getRemoved() {
        return this.removed;
    }

    public record Change(CyberwareSlot slot, int index, ItemStack stack) {
    }
}

