/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.levelgen.structure.BoundingBox
 */
package io.redspace.ironsspellbooks.worldgen;

import net.minecraft.world.level.levelgen.structure.BoundingBox;

public interface IExtendedNoiseChunk {
    public AquifierNuke irons_spellbooks$getAquifierStatus();

    public void irons_spellbooks$setAquifierStatus(AquifierNuke var1);

    public record AquifierNuke(BoundingBox[] boundingBoxes) {
    }
}

