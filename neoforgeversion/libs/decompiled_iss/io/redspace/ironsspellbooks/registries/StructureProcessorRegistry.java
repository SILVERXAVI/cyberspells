/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package io.redspace.ironsspellbooks.registries;

import io.redspace.ironsspellbooks.worldgen.ClearPortalFrameDataProcessor;
import io.redspace.ironsspellbooks.worldgen.DegradeSlabsStairsProcessor;
import io.redspace.ironsspellbooks.worldgen.HandleLitBlocksProcessor;
import io.redspace.ironsspellbooks.worldgen.StructureFoundationProcessor;
import io.redspace.ironsspellbooks.worldgen.WeatherCopperProcessor;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class StructureProcessorRegistry {
    public static final DeferredRegister<StructureProcessorType<?>> PROCESSORS = DeferredRegister.create((ResourceKey)Registries.STRUCTURE_PROCESSOR, (String)"irons_spellbooks");
    public static final Supplier<StructureProcessorType<WeatherCopperProcessor>> WEATHER_COPPER = PROCESSORS.register("weather_copper", () -> () -> WeatherCopperProcessor.CODEC);
    public static final Supplier<StructureProcessorType<DegradeSlabsStairsProcessor>> DEGRADE_SLABS_STAIRS = PROCESSORS.register("degrade_slabs_stairs", () -> () -> DegradeSlabsStairsProcessor.CODEC);
    public static final Supplier<StructureProcessorType<StructureFoundationProcessor>> STRUCTURE_FOUNDATION_PROCESSOR = PROCESSORS.register("foundation", () -> () -> StructureFoundationProcessor.CODEC);
    public static final Supplier<StructureProcessorType<HandleLitBlocksProcessor>> HANDLE_LIT_BLOCKS_PROCESSOR = PROCESSORS.register("handle_lit_blocks", () -> () -> HandleLitBlocksProcessor.CODEC);
    public static final Supplier<StructureProcessorType<ClearPortalFrameDataProcessor>> CLEAR_PORTAL_FRAME_DATA = PROCESSORS.register("clear_portal_frame_data", () -> () -> ClearPortalFrameDataProcessor.CODEC);

    public static void register(IEventBus eventBus) {
        PROCESSORS.register(eventBus);
    }
}

