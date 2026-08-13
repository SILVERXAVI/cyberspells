/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.world.level.saveddata.SavedData
 *  net.minecraft.world.level.saveddata.SavedData$Factory
 *  net.minecraft.world.level.storage.DimensionDataStorage
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.data;

import io.redspace.ironsspellbooks.capabilities.magic.PocketDimensionManager;
import io.redspace.ironsspellbooks.capabilities.magic.PortalManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.effect.guiding_bolt.GuidingBoltManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

public class IronsDataStorage
extends SavedData {
    public static IronsDataStorage INSTANCE;

    public static void init(DimensionDataStorage dimensionDataStorage) {
        if (dimensionDataStorage != null) {
            INSTANCE = (IronsDataStorage)dimensionDataStorage.computeIfAbsent(new SavedData.Factory(IronsDataStorage::new, IronsDataStorage::load), "irons_spellbooks_data");
        }
    }

    @NotNull
    public CompoundTag save(@NotNull CompoundTag pCompoundTag, HolderLookup.Provider pRegistries) {
        CompoundTag tag = new CompoundTag();
        tag.put("GuidingBoltManager", (Tag)GuidingBoltManager.INSTANCE.serializeNBT(pRegistries));
        tag.put("PortalManager", (Tag)PortalManager.INSTANCE.serializeNBT(pRegistries));
        tag.put("PocketDimensionIdManager", (Tag)PocketDimensionManager.INSTANCE.serializeNBT(pRegistries));
        tag.put("SummonManager", (Tag)SummonManager.INSTANCE.serializeNBT(pRegistries));
        return tag;
    }

    public static IronsDataStorage load(CompoundTag tag, HolderLookup.Provider pRegistries) {
        if (tag.contains("GuidingBoltManager", 10)) {
            GuidingBoltManager.INSTANCE.deserializeNBT(pRegistries, tag.getCompound("GuidingBoltManager"));
        }
        if (tag.contains("PortalManager", 10)) {
            PortalManager.INSTANCE.deserializeNBT(pRegistries, tag.getCompound("PortalManager"));
        }
        if (tag.contains("PocketDimensionIdManager", 10)) {
            PocketDimensionManager.INSTANCE.deserializeNBT(pRegistries, tag.getCompound("PocketDimensionIdManager"));
        }
        if (tag.contains("SummonManager", 10)) {
            SummonManager.INSTANCE.deserializeNBT(pRegistries, tag.getCompound("SummonManager"));
        }
        return new IronsDataStorage();
    }
}

