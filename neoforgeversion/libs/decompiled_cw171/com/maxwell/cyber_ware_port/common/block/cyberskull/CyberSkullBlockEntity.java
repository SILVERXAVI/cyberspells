/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.entity.SkullBlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 */
package com.maxwell.cyber_ware_port.common.block.cyberskull;

import com.maxwell.cyber_ware_port.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CyberSkullBlockEntity
extends SkullBlockEntity {
    public CyberSkullBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public BlockEntityType<?> getType() {
        return (BlockEntityType)ModBlockEntities.CYBER_SKULL.get();
    }
}

