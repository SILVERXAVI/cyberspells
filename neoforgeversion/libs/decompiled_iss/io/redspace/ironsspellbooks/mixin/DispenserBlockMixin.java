/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.dispenser.BlockSource
 *  net.minecraft.core.dispenser.DefaultDispenseItemBehavior
 *  net.minecraft.core.dispenser.DispenseItemBehavior
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.DirectionalBlock
 *  net.minecraft.world.level.block.DispenserBlock
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.block.alchemist_cauldron.AlchemistCauldronTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={DispenserBlock.class})
public class DispenserBlockMixin {
    @Unique
    @Nullable
    private static BlockState irons_spellbooks$blockStateCapture;
    @Unique
    @Nullable
    private static BlockPos irons_spellbooks$blockPosCapture;

    @Inject(method={"dispenseFrom"}, at={@At(value="HEAD")})
    private void irons_spellbooks$captureParameters(ServerLevel level, BlockState state, BlockPos pos, CallbackInfo ci) {
        irons_spellbooks$blockStateCapture = state;
        irons_spellbooks$blockPosCapture = pos;
    }

    @Inject(method={"getDispenseMethod"}, at={@At(value="HEAD")}, cancellable=true)
    private void irons_spellbooks$injectCauldronInteractions(Level level, ItemStack item, CallbackInfoReturnable<DispenseItemBehavior> cir) {
        AlchemistCauldronTile alchemistCauldronTile;
        ItemStack cauldronResult;
        BlockEntity blockEntity;
        if (irons_spellbooks$blockStateCapture != null && irons_spellbooks$blockPosCapture != null && (blockEntity = level.getBlockEntity(irons_spellbooks$blockPosCapture.mutable().relative((Direction)irons_spellbooks$blockStateCapture.getValue((Property)DirectionalBlock.FACING)))) instanceof AlchemistCauldronTile && !(cauldronResult = (alchemistCauldronTile = (AlchemistCauldronTile)blockEntity).tryExecuteRecipeInteractions(level, item)).isEmpty()) {
            cir.setReturnValue((Object)new DefaultDispenseItemBehavior(this){

                protected ItemStack execute(BlockSource blockSource, ItemStack dispensingStack) {
                    return this.consumeWithRemainder(blockSource, dispensingStack, cauldronResult);
                }
            });
        }
    }
}

