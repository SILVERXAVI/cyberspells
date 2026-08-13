/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 */
package io.redspace.ironsspellbooks.block.chiseled_bookshelf;

import io.redspace.ironsspellbooks.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class WisewoodChiseledBookShelfBlockEntity
extends ChiseledBookShelfBlockEntity {
    public WisewoodChiseledBookShelfBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public BlockEntityType<?> getType() {
        return (BlockEntityType)BlockRegistry.WISEWOOD_CHISELED_BOOKSHELF_ENTITY.get();
    }
}

