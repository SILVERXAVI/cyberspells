/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.nbt.CompoundTag
 */
package io.redspace.ironsspellbooks.entity.mobs.goals;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public interface HomeOwner {
    @Nullable
    public BlockPos getHome();

    public void setHome(BlockPos var1);

    default public void serializeHome(HomeOwner self, CompoundTag tag) {
        if (self.getHome() != null) {
            tag.putIntArray("HomePos", new int[]{this.getHome().getX(), this.getHome().getY(), this.getHome().getZ()});
        }
    }

    default public void deserializeHome(HomeOwner self, CompoundTag tag) {
        if (tag.contains("HomePos")) {
            int[] home = tag.getIntArray("HomePos");
            self.setHome(new BlockPos(home[0], home[1], home[2]));
        }
    }
}

