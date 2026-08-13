/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.world.level.ChunkPos
 *  net.minecraft.world.level.levelgen.Beardifier
 *  net.minecraft.world.level.levelgen.Beardifier$Rigid
 *  net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece
 *  net.minecraft.world.level.levelgen.structure.StructurePiece
 *  net.minecraft.world.level.levelgen.structure.StructureStart
 *  net.minecraft.world.level.levelgen.structure.pools.JigsawJunction
 *  net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement
 *  net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool$Projection
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.worldgen.IndividualTerrainStructurePoolElement;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Beardifier.class})
public class BeardifierMixin {
    @Inject(method={"lambda$forStructuresInChunk$2", "m_223930_"}, remap=false, at={@At(value="HEAD")}, cancellable=true)
    private static void irons_spellbooks$injectCustomTerrainAdaptation(ChunkPos pChunkPos, ObjectList<Beardifier.Rigid> list, int i, int j, ObjectList<JigsawJunction> junctions, StructureStart p_223936_, CallbackInfo ci) {
        for (StructurePiece structurepiece : p_223936_.getPieces()) {
            PoolElementStructurePiece poolelementstructurepiece;
            StructurePoolElement structurePoolElement;
            if (!(structurepiece instanceof PoolElementStructurePiece) || !((structurePoolElement = (poolelementstructurepiece = (PoolElementStructurePiece)structurepiece).getElement()) instanceof IndividualTerrainStructurePoolElement)) continue;
            IndividualTerrainStructurePoolElement ironElement = (IndividualTerrainStructurePoolElement)structurePoolElement;
            if (!structurepiece.isCloseToChunk(pChunkPos, 12)) continue;
            StructureTemplatePool.Projection structuretemplatepool$projection = ironElement.getProjection();
            if (structuretemplatepool$projection == StructureTemplatePool.Projection.RIGID) {
                list.add((Object)new Beardifier.Rigid(poolelementstructurepiece.getBoundingBox(), ironElement.getTerrainAdjustment(), ironElement.getGroundLevelDelta()));
            }
            ci.cancel();
        }
    }
}

