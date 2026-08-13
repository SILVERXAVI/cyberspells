/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.datafixers.util.Either
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  net.minecraft.core.Holder
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.levelgen.structure.TerrainAdjustment
 *  net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement
 *  net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType
 *  net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool$Projection
 *  net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.worldgen;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.ironsspellbooks.registries.StructureElementRegistry;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

public class IndividualTerrainStructurePoolElement
extends SinglePoolElement {
    public static final MapCodec<IndividualTerrainStructurePoolElement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group((App)IndividualTerrainStructurePoolElement.templateCodec(), (App)IndividualTerrainStructurePoolElement.processorsCodec(), (App)IndividualTerrainStructurePoolElement.projectionCodec(), (App)IndividualTerrainStructurePoolElement.overrideLiquidSettingsCodec(), (App)TerrainAdjustment.CODEC.optionalFieldOf("terrain_adjustment").forGetter(element -> Optional.ofNullable(element.terrainAdjustment))).apply((Applicative)instance, (either, processorListHolder, projection, liquidSettings, terrainAdjustment) -> new IndividualTerrainStructurePoolElement((Either<ResourceLocation, StructureTemplate>)either, (Holder<StructureProcessorList>)processorListHolder, (StructureTemplatePool.Projection)projection, (Optional<LiquidSettings>)liquidSettings, terrainAdjustment.orElse(null))));
    @Nullable
    private final TerrainAdjustment terrainAdjustment;

    public IndividualTerrainStructurePoolElement(Either<ResourceLocation, StructureTemplate> resourceLocation, Holder<StructureProcessorList> processors, StructureTemplatePool.Projection projection, Optional<LiquidSettings> liquidSettings, @Nullable TerrainAdjustment terrainAdjustment) {
        super(resourceLocation, processors, projection, liquidSettings);
        this.terrainAdjustment = terrainAdjustment;
    }

    public TerrainAdjustment getTerrainAdjustment() {
        return this.terrainAdjustment != null ? this.terrainAdjustment : TerrainAdjustment.NONE;
    }

    public StructurePoolElementType<?> getType() {
        return StructureElementRegistry.INDIVIDUAL_TERRAIN_ELEMENT.get();
    }
}

