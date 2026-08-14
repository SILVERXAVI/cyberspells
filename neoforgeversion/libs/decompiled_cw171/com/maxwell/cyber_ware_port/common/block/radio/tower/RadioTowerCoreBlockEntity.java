/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 */
package com.maxwell.cyber_ware_port.common.block.radio.tower;

import com.maxwell.cyber_ware_port.common.block.radio.tower.RadioTowerCoreBlock;
import com.maxwell.cyber_ware_port.common.block.radio.tower.RadioTowerFenceBlock;
import com.maxwell.cyber_ware_port.init.ModBlockEntities;
import com.maxwell.cyber_ware_port.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class RadioTowerCoreBlockEntity
extends BlockEntity {
    private static final int BASE_HEIGHT = 4;
    private static final int SHAFT_HEIGHT = 6;
    private static final int TOTAL_HEIGHT = 10;

    public RadioTowerCoreBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super((BlockEntityType)ModBlockEntities.RADIO_TOWER_CORE.get(), pPos, pBlockState);
    }

    public void deformFencesOnly() {
        if (this.level == null || this.level.isClientSide()) {
            return;
        }
        this.setStructureState(false, false);
    }

    public void tryToFormStructure() {
        if (this.level == null || this.level.isClientSide()) {
            return;
        }
        if (this.checkStructure()) {
            this.setStructureState(true);
        }
    }

    public void deformStructure() {
        if (this.level == null || this.level.isClientSide()) {
            return;
        }
        this.setStructureState(false);
    }

    private void setStructureState(boolean formed) {
        this.setStructureState(formed, true);
    }

    private void setStructureState(boolean formed, boolean includeCore) {
        int yOffset;
        BlockState coreState;
        if (this.level == null) {
            return;
        }
        BlockPos corePos = this.worldPosition;
        if (includeCore && (coreState = this.getBlockState()).hasProperty((Property)RadioTowerCoreBlock.FORMED)) {
            this.level.setBlock(corePos, (BlockState)coreState.setValue((Property)RadioTowerCoreBlock.FORMED, (Comparable)Boolean.valueOf(formed)), 3);
        }
        for (yOffset = 1; yOffset <= 6; ++yOffset) {
            this.updateFenceState(this.level, corePos.below(yOffset), formed);
        }
        for (yOffset = 7; yOffset <= 10; ++yOffset) {
            BlockPos layerCenter = corePos.below(yOffset);
            for (int x = -1; x <= 1; ++x) {
                for (int z = -1; z <= 1; ++z) {
                    this.updateFenceState(this.level, layerCenter.offset(x, 0, z), formed);
                }
            }
        }
    }

    private void updateFenceState(Level level, BlockPos pos, boolean formed) {
        BlockState state = level.getBlockState(pos);
        if (state.is((Block)ModBlocks.RADIO_TOWER_COMPONENT.get()) && state.hasProperty((Property)RadioTowerFenceBlock.FORMED) && (Boolean)state.getValue((Property)RadioTowerFenceBlock.FORMED) != formed) {
            level.setBlock(pos, (BlockState)state.setValue((Property)RadioTowerFenceBlock.FORMED, (Comparable)Boolean.valueOf(formed)), 3);
        }
    }

    private boolean checkStructure() {
        int z;
        int x;
        BlockPos layerCenter;
        int yOffset;
        if (this.level == null) {
            return false;
        }
        BlockPos corePos = this.worldPosition;
        for (yOffset = 1; yOffset <= 6; ++yOffset) {
            layerCenter = corePos.below(yOffset);
            for (x = -1; x <= 1; ++x) {
                for (z = -1; z <= 1; ++z) {
                    BlockPos targetPos = layerCenter.offset(x, 0, z);
                    BlockState state = this.level.getBlockState(targetPos);
                    if (!(x == 0 && z == 0 ? !state.is((Block)ModBlocks.RADIO_TOWER_COMPONENT.get()) : !state.isAir())) continue;
                    return false;
                }
            }
        }
        for (yOffset = 7; yOffset <= 10; ++yOffset) {
            layerCenter = corePos.below(yOffset);
            for (x = -1; x <= 1; ++x) {
                for (z = -1; z <= 1; ++z) {
                    BlockState state = this.level.getBlockState(layerCenter.offset(x, 0, z));
                    if (state.is((Block)ModBlocks.RADIO_TOWER_COMPONENT.get())) continue;
                    return false;
                }
            }
        }
        return true;
    }
}

