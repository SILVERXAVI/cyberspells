/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.material.FluidState
 *  net.neoforged.neoforge.fluids.BaseFlowingFluid
 *  net.neoforged.neoforge.fluids.BaseFlowingFluid$Properties
 */
package io.redspace.ironsspellbooks.fluids;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class NoopFluid
extends BaseFlowingFluid {
    public NoopFluid(BaseFlowingFluid.Properties properties) {
        super(properties);
    }

    public Item getBucket() {
        return Items.AIR;
    }

    protected BlockState createLegacyBlock(FluidState state) {
        return Blocks.AIR.defaultBlockState();
    }

    public boolean isSource(FluidState p_207193_1_) {
        return true;
    }

    public int getAmount(FluidState p_207192_1_) {
        return 0;
    }
}

