/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.block.SkullBlock$Type
 *  net.minecraft.world.level.block.WallSkullBlock
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 */
package com.maxwell.cyber_ware_port.common.block.cyberskull;

import com.maxwell.cyber_ware_port.common.block.cyberskull.CyberSkullBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class CyberWallSkullBlock
extends WallSkullBlock {
    public CyberWallSkullBlock(SkullBlock.Type type, BlockBehaviour.Properties properties) {
        super(type, properties);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CyberSkullBlockEntity(pos, state);
    }
}

