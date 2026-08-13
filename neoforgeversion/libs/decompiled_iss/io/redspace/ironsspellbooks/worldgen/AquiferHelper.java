/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
 *  net.minecraft.core.Registry
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.StructureManager
 *  net.minecraft.world.level.levelgen.structure.Structure
 */
package io.redspace.ironsspellbooks.worldgen;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Set;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.Structure;

public class AquiferHelper {
    static final Set<ResourceLocation> structuresToTrack;
    private static final Set<Structure> structuresInterruptingAquifers;
    private static boolean cached;

    public static void registerTrackedStructure(ResourceLocation resourceLocation) {
        structuresToTrack.add(resourceLocation);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Set<Structure> getOrCacheStructures(StructureManager registryAccess) {
        if (!cached) {
            Set<Structure> set = structuresInterruptingAquifers;
            synchronized (set) {
                Registry registry = registryAccess.registryAccess().registryOrThrow(Registries.STRUCTURE);
                for (ResourceLocation r : structuresToTrack) {
                    Structure str = (Structure)registry.get(r);
                    if (str == null) continue;
                    structuresInterruptingAquifers.add(str);
                }
                cached = true;
            }
        }
        return structuresInterruptingAquifers;
    }

    static {
        cached = false;
        structuresToTrack = new ObjectOpenHashSet();
        structuresInterruptingAquifers = new ObjectOpenHashSet();
        structuresToTrack.add(IronsSpellbooks.id("ice_spider_den"));
    }
}

