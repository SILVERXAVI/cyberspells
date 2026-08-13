/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.Connection
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
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
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.block.scroll_forge;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.gui.scroll_forge.ScrollForgeMenu;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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
import org.jetbrains.annotations.Nullable;

public class ScrollForgeTile
extends BlockEntity
implements MenuProvider {
    private ScrollForgeMenu menu;
    private final ItemStackHandler itemHandler = new ItemStackHandler(4){

        protected void onContentsChanged(int slot) {
            ScrollForgeTile.this.updateMenuSlots(slot);
            ScrollForgeTile.this.setChanged();
        }
    };

    public ScrollForgeTile(BlockPos pWorldPosition, BlockState pBlockState) {
        super((BlockEntityType)BlockRegistry.SCROLL_FORGE_TILE.get(), pWorldPosition, pBlockState);
    }

    private void updateMenuSlots(int slot) {
        if (this.menu != null) {
            this.menu.onSlotsChanged(slot);
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    public ItemStack getStackInSlot(int slot) {
        return this.itemHandler.getStackInSlot(slot);
    }

    public MutableComponent getDisplayName() {
        return Component.translatable((String)"ui.irons_spellbooks.scroll_forge_title");
    }

    @Nullable
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        this.menu = new ScrollForgeMenu(containerId, inventory, this);
        return this.menu;
    }

    public void setRecipeSpell(String spellId) {
        this.menu.setRecipeSpell(SpellRegistry.getSpell(spellId));
    }

    public void drops() {
        SimpleContainer simpleContainer = new SimpleContainer(this.itemHandler.getSlots());
        for (int i = 0; i < this.itemHandler.getSlots() - 1; ++i) {
            simpleContainer.setItem(i, this.itemHandler.getStackInSlot(i));
        }
        Containers.dropContents((Level)this.level, (BlockPos)this.worldPosition, (Container)simpleContainer);
    }

    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        if (pTag.contains("inventory")) {
            this.itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
        }
    }

    protected void saveAdditional(@Nonnull CompoundTag tag, HolderLookup.Provider registryAccess) {
        tag.put("inventory", (Tag)this.itemHandler.serializeNBT(registryAccess));
    }

    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag tag = new CompoundTag();
        tag.put("inventory", (Tag)this.itemHandler.serializeNBT(pRegistries));
        return tag;
    }

    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        this.loadAdditional(tag, lookupProvider);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        ClientboundBlockEntityDataPacket packet = ClientboundBlockEntityDataPacket.create((BlockEntity)this);
        return packet;
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        this.handleUpdateTag(pkt.getTag(), lookupProvider);
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
    }
}

