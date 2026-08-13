/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package io.redspace.ironsspellbooks.registries;

import io.redspace.ironsspellbooks.worldgen.IndividualTerrainStructurePoolElement;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class StructureElementRegistry {
    public static final DeferredRegister<StructurePoolElementType<?>> STRUCTURE_POOL_ELEMENT_DEFERRED_REGISTER = DeferredRegister.create((ResourceKey)Registries.STRUCTURE_POOL_ELEMENT, (String)"irons_spellbooks");
    public static final Supplier<StructurePoolElementType<IndividualTerrainStructurePoolElement>> INDIVIDUAL_TERRAIN_ELEMENT = STRUCTURE_POOL_ELEMENT_DEFERRED_REGISTER.register("individual_terrain_element", () -> () -> IndividualTerrainStructurePoolElement.CODEC);

    public static void register(IEventBus eventBus) {
        STRUCTURE_POOL_ELEMENT_DEFERRED_REGISTER.register(eventBus);
    }
}

