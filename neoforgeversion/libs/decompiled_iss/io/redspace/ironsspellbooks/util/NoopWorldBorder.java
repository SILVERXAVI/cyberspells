/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.border.WorldBorder
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.border.WorldBorder;
import org.jetbrains.annotations.NotNull;

public class NoopWorldBorder
extends WorldBorder {
    public boolean isWithinBounds(double x, double z, double offset) {
        return true;
    }

    @NotNull
    public BlockPos clampToBounds(double x, double y, double z) {
        return BlockPos.containing((double)x, (double)y, (double)z);
    }

    public double getDistanceToBorder(double x, double z) {
        return 5.9999968E7;
    }

    public void setSize(double size) {
    }

    public void setCenter(double x, double z) {
    }
}

