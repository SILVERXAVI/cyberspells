/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.Component$Serializer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.maxwell.cyber_ware_port.common.block.component_box;

import com.maxwell.cyber_ware_port.common.container.ComponentBoxMenu;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.common.item.componentbox.ComponentBoxItem;
import com.maxwell.cyber_ware_port.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ComponentBoxBlockEntity
extends BlockEntity
implements MenuProvider {
    public final ItemStackHandler itemHandler = new ItemStackHandler(18){

        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return ComponentBoxBlockEntity.this.isComponent(stack);
        }

        protected void onContentsChanged(int slot) {
            ComponentBoxBlockEntity.this.setChanged();
        }
    };
    private Component customName;

    public ComponentBoxBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super((BlockEntityType)ModBlockEntities.COMPONENT_BOX.get(), pPos, pBlockState);
    }

    private boolean isComponent(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof CyberwareItem || item instanceof ComponentBoxItem) {
            return false;
        }
        ResourceLocation id = BuiltInRegistries.ITEM.getKey((Object)item);
        return id.getPath().contains("component_");
    }

    @NotNull
    public Component getDisplayName() {
        return this.customName != null ? this.customName : Component.translatable((String)"item.cyber_ware_port.component_box");
    }

    public void setCustomName(Component name) {
        this.customName = name;
    }

    public boolean hasCustomName() {
        return this.customName != null;
    }

    @Nullable
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new ComponentBoxMenu(pContainerId, pPlayerInventory, this);
    }

    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        if (pTag.contains("Inventory")) {
            this.itemHandler.deserializeNBT(pRegistries, pTag.getCompound("Inventory"));
        }
        if (pTag.contains("CustomName")) {
            this.customName = Component.Serializer.fromJson((String)pTag.getString("CustomName"), (HolderLookup.Provider)pRegistries);
        }
    }

    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.put("Inventory", (Tag)this.itemHandler.serializeNBT(pRegistries));
        if (this.customName != null) {
            pTag.putString("CustomName", Component.Serializer.toJson((Component)this.customName, (HolderLookup.Provider)pRegistries));
        }
    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }
}

