/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.Container
 *  net.minecraft.world.Containers
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.SimpleContainer
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.maxwell.cyber_ware_port.common.block.blueprintchest;

import com.maxwell.cyber_ware_port.common.container.BlueprintChestMenu;
import com.maxwell.cyber_ware_port.common.item.BlueprintItem;
import com.maxwell.cyber_ware_port.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlueprintChestBlockEntity
extends BlockEntity
implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(18){

        protected void onContentsChanged(int slot) {
            BlueprintChestBlockEntity.this.setChanged();
        }

        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem() instanceof BlueprintItem;
        }
    };

    public BlueprintChestBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super((BlockEntityType)ModBlockEntities.BLUEPRINT_CHEST.get(), pPos, pBlockState);
    }

    @NotNull
    public Component getDisplayName() {
        return Component.translatable((String)"block.cyber_ware_port.blueprint_chest");
    }

    @Nullable
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new BlueprintChestMenu(pContainerId, pPlayerInventory, this);
    }

    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.put("inventory", (Tag)this.itemHandler.serializeNBT(pRegistries));
    }

    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        if (pTag.contains("inventory")) {
            this.itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
        }
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for (int i = 0; i < this.itemHandler.getSlots(); ++i) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }
        if (this.level != null) {
            Containers.dropContents((Level)this.level, (BlockPos)this.worldPosition, (Container)inventory);
        }
    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }
}

