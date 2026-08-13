/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.entity.ai.village.poi.PoiType
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockState
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package io.redspace.ironsspellbooks.registries;

import com.google.common.collect.ImmutableSet;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import java.util.Collection;
import java.util.Set;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PoiTypeRegistry {
    private static final DeferredRegister<PoiType> POIS = DeferredRegister.create((ResourceKey)Registries.POINT_OF_INTEREST_TYPE, (String)"irons_spellbooks");
    public static final DeferredHolder<PoiType, PoiType> CINDEROUS_KEYSTONE_POI = POIS.register("cinderous_soul_rune", () -> new PoiType(PoiTypeRegistry.getBlockStates((Block)BlockRegistry.CINDEROUS_KEYSTONE.get()), 1, 1));

    public static void register(IEventBus eventBus) {
        POIS.register(eventBus);
    }

    private static Set<BlockState> getBlockStates(Block pBlock) {
        return ImmutableSet.copyOf((Collection)pBlock.getStateDefinition().getPossibleStates());
    }
}

