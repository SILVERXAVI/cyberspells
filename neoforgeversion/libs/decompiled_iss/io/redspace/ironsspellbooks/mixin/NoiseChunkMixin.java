/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.levelgen.Aquifer$FluidPicker
 *  net.minecraft.world.level.levelgen.DensityFunctions$BeardifierOrMarker
 *  net.minecraft.world.level.levelgen.NoiseChunk
 *  net.minecraft.world.level.levelgen.NoiseGeneratorSettings
 *  net.minecraft.world.level.levelgen.NoiseSettings
 *  net.minecraft.world.level.levelgen.RandomState
 *  net.minecraft.world.level.levelgen.blending.Blender
 *  net.minecraft.world.level.levelgen.structure.BoundingBox
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.worldgen.IExtendedNoiseChunk;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={NoiseChunk.class})
public class NoiseChunkMixin
implements IExtendedNoiseChunk {
    @Unique
    IExtendedNoiseChunk.AquifierNuke irons_spellbooks$aquifierNuke = null;
    @Unique
    BlockState irons_spellbooks$defaultBlockState;

    @Inject(method={"<init>"}, at={@At(value="RETURN")})
    private void irons_spellbooks$captureDefaultBlockstate(int cellCountXZ, RandomState random, int firstNoiseX, int firstNoiseZ, NoiseSettings noiseSettings, DensityFunctions.BeardifierOrMarker beardifier, NoiseGeneratorSettings noiseGeneratorSettings, Aquifer.FluidPicker fluidPicker, Blender blendifier, CallbackInfo ci) {
        this.irons_spellbooks$defaultBlockState = noiseGeneratorSettings.defaultBlock();
    }

    @Inject(method={"getInterpolatedState"}, at={@At(value="RETURN")}, cancellable=true)
    private void irons_spellbooks$cancelAquifierGeneration(CallbackInfoReturnable<BlockState> cir) {
        BlockState state = (BlockState)cir.getReturnValue();
        if (state == null) {
            return;
        }
        IExtendedNoiseChunk.AquifierNuke nuke = this.irons_spellbooks$getAquifierStatus();
        if (nuke == null) {
            return;
        }
        if (state.is(Blocks.WATER) || state.is(Blocks.LAVA)) {
            NoiseChunk chunk = (NoiseChunk)this;
            int x = chunk.blockX();
            int y = chunk.blockY();
            int z = chunk.blockZ();
            for (BoundingBox box : nuke.boundingBoxes()) {
                int dx = 0;
                if (x < box.minX()) {
                    dx = box.minX() - x;
                } else if (x > box.maxX()) {
                    dx = x - box.maxX();
                }
                int dy = 0;
                if (y < box.minY()) {
                    dy = box.minY() - y;
                } else if (y > box.maxY()) {
                    dy = y - box.maxY();
                }
                int dz = 0;
                if (z < box.minZ()) {
                    dz = box.minZ() - z;
                } else if (z > box.maxZ()) {
                    dz = z - box.maxZ();
                }
                int manhattanDistance = dx + dy + dz;
                if (manhattanDistance > 5) continue;
                if (manhattanDistance <= 3) {
                    cir.setReturnValue((Object)Blocks.CAVE_AIR.defaultBlockState());
                } else {
                    cir.setReturnValue((Object)this.irons_spellbooks$defaultBlockState);
                }
                return;
            }
        }
    }

    @Override
    public IExtendedNoiseChunk.AquifierNuke irons_spellbooks$getAquifierStatus() {
        return this.irons_spellbooks$aquifierNuke;
    }

    @Override
    public void irons_spellbooks$setAquifierStatus(IExtendedNoiseChunk.AquifierNuke nuke) {
        this.irons_spellbooks$aquifierNuke = nuke;
    }
}

