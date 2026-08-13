/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.StructureManager
 *  net.minecraft.world.level.chunk.ChunkAccess
 *  net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator
 *  net.minecraft.world.level.levelgen.NoiseChunk
 *  net.minecraft.world.level.levelgen.RandomState
 *  net.minecraft.world.level.levelgen.blending.Blender
 *  net.minecraft.world.level.levelgen.structure.BoundingBox
 *  net.minecraft.world.level.levelgen.structure.StructurePiece
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.worldgen.AquiferHelper;
import io.redspace.ironsspellbooks.worldgen.IExtendedNoiseChunk;
import java.util.List;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={NoiseBasedChunkGenerator.class})
public class NoiseChunkGeneratorMixin {
    @Inject(method={"createNoiseChunk"}, at={@At(value="RETURN")})
    void irons_spellbooks$detectAquifers(ChunkAccess chunk, StructureManager structureManager, Blender blender, RandomState random, CallbackInfoReturnable<NoiseChunk> cir) {
        if (ServerConfigs.SPEC.isLoaded() && !((Boolean)ServerConfigs.AQUIFER_DETECTION.get()).booleanValue()) {
            return;
        }
        IExtendedNoiseChunk noisechunk = (IExtendedNoiseChunk)cir.getReturnValue();
        List starts = structureManager.startsForStructure(chunk.getPos(), structure -> AquiferHelper.getOrCacheStructures(structureManager).contains(structure));
        if (!starts.isEmpty()) {
            BoundingBox[] boundingBoxes = (BoundingBox[])starts.stream().flatMap(s -> s.getPieces().stream()).map(StructurePiece::getBoundingBox).toArray(BoundingBox[]::new);
            noisechunk.irons_spellbooks$setAquifierStatus(new IExtendedNoiseChunk.AquifierNuke(boundingBoxes));
        }
    }
}

