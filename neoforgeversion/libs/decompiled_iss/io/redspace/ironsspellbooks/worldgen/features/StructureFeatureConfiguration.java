/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  net.minecraft.core.BlockPos
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
 */
package io.redspace.ironsspellbooks.worldgen.features;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record StructureFeatureConfiguration(ResourceLocation structureTemplateLocation, int xsize, int ysize, int zsize, BlockPos offset) implements FeatureConfiguration
{
    public static final Codec<StructureFeatureConfiguration> CODEC = RecordCodecBuilder.create(builder -> builder.group((App)ResourceLocation.CODEC.fieldOf("structure_piece").forGetter(StructureFeatureConfiguration::structureTemplateLocation), (App)Codec.intRange((int)1, (int)16).fieldOf("x_size").forGetter(StructureFeatureConfiguration::xsize), (App)Codec.intRange((int)1, (int)16).fieldOf("y_size").forGetter(StructureFeatureConfiguration::ysize), (App)Codec.intRange((int)1, (int)16).fieldOf("z_size").forGetter(StructureFeatureConfiguration::zsize), (App)BlockPos.CODEC.fieldOf("offset").forGetter(StructureFeatureConfiguration::offset)).apply((Applicative)builder, StructureFeatureConfiguration::new));
}

