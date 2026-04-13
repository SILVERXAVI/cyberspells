/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.longs.LongOpenHashSet
 *  it.unimi.dsi.fastutil.longs.LongSet
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.level.saveddata.SavedData
 *  net.minecraft.world.level.saveddata.SavedData$Factory
 */
package com.perigrine3.createcybernetics.world;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class MansionBossSavedData
extends SavedData {
    private static final String NAME = "createcybernetics_mansion_bosses";
    private final LongSet rolledFail = new LongOpenHashSet();
    private final LongSet pending = new LongOpenHashSet();
    private final LongSet spawned = new LongOpenHashSet();

    public static MansionBossSavedData get(ServerLevel level) {
        return (MansionBossSavedData)level.getDataStorage().computeIfAbsent(new SavedData.Factory(MansionBossSavedData::new, MansionBossSavedData::load), NAME);
    }

    public boolean isRolledFail(long key) {
        return this.rolledFail.contains(key);
    }

    public boolean isPending(long key) {
        return this.pending.contains(key);
    }

    public boolean isSpawned(long key) {
        return this.spawned.contains(key);
    }

    public void markRolledFail(long key) {
        boolean changed = this.rolledFail.add(key) | this.pending.remove(key);
        if (changed) {
            this.setDirty();
        }
    }

    public void markPending(long key) {
        if (this.spawned.contains(key) || this.rolledFail.contains(key)) {
            return;
        }
        if (this.pending.add(key)) {
            this.setDirty();
        }
    }

    public void markSpawned(long key) {
        boolean changed = this.spawned.add(key) | this.pending.remove(key);
        if (changed) {
            this.setDirty();
        }
    }

    public static MansionBossSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
        MansionBossSavedData data = new MansionBossSavedData();
        if (tag.contains("RolledFail")) {
            for (long v : tag.getLongArray("RolledFail")) {
                data.rolledFail.add(v);
            }
        }
        if (tag.contains("Pending")) {
            for (long v : tag.getLongArray("Pending")) {
                data.pending.add(v);
            }
        }
        if (tag.contains("Spawned")) {
            for (long v : tag.getLongArray("Spawned")) {
                data.spawned.add(v);
            }
        }
        if (!tag.contains("RolledFail") && tag.contains("Attempted")) {
            for (long v : tag.getLongArray("Attempted")) {
                data.rolledFail.add(v);
            }
        }
        return data;
    }

    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putLongArray("RolledFail", this.rolledFail.toLongArray());
        tag.putLongArray("Pending", this.pending.toLongArray());
        tag.putLongArray("Spawned", this.spawned.toLongArray());
        return tag;
    }
}

