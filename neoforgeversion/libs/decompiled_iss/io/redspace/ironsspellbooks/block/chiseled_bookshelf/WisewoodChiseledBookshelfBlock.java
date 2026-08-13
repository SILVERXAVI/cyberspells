/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.block.ChiseledBookShelfBlock
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.block.chiseled_bookshelf;

import io.redspace.ironsspellbooks.block.chiseled_bookshelf.WisewoodChiseledBookShelfBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class WisewoodChiseledBookshelfBlock
extends ChiseledBookShelfBlock {
    public WisewoodChiseledBookshelfBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WisewoodChiseledBookShelfBlockEntity(pos, state);
    }
}

