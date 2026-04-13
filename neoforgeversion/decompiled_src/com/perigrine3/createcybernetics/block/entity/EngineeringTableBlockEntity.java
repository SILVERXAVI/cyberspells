/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.Container
 *  net.minecraft.world.Containers
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.SimpleContainer
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.neoforged.neoforge.items.ItemStackHandler
 */
package com.perigrine3.createcybernetics.block.entity;

import com.perigrine3.createcybernetics.block.entity.ModBlockEntities;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.screen.custom.EngineeringTableMenu;
import com.perigrine3.createcybernetics.sound.ModSounds;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.ArrayList;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public class EngineeringTableBlockEntity
extends BlockEntity
implements MenuProvider {
    public static final int GRID_SIZE = 25;
    public static final int DECONSTRUCT_INPUT_SIZE = 1;
    public static final int DECONSTRUCT_OUTPUT_SIZE = 6;
    private final ItemStackHandler crafting = new ItemStackHandler(25){

        protected void onContentsChanged(int slot) {
            EngineeringTableBlockEntity.this.setChanged();
            if (EngineeringTableBlockEntity.this.level != null && !EngineeringTableBlockEntity.this.level.isClientSide()) {
                EngineeringTableBlockEntity.this.level.sendBlockUpdated(EngineeringTableBlockEntity.this.getBlockPos(), EngineeringTableBlockEntity.this.getBlockState(), EngineeringTableBlockEntity.this.getBlockState(), 3);
            }
        }
    };
    private boolean deconProcessing = false;
    private final ItemStackHandler deconstructInput = new ItemStackHandler(1){

        protected void onContentsChanged(int slot) {
            EngineeringTableBlockEntity.this.setChanged();
            if (EngineeringTableBlockEntity.this.level != null && !EngineeringTableBlockEntity.this.level.isClientSide()) {
                Level level;
                if (!EngineeringTableBlockEntity.this.deconProcessing && (level = EngineeringTableBlockEntity.this.level) instanceof ServerLevel) {
                    ServerLevel sl = (ServerLevel)level;
                    EngineeringTableBlockEntity.this.tryInstantDeconstruct(sl);
                }
                EngineeringTableBlockEntity.this.level.sendBlockUpdated(EngineeringTableBlockEntity.this.getBlockPos(), EngineeringTableBlockEntity.this.getBlockState(), EngineeringTableBlockEntity.this.getBlockState(), 3);
            }
        }

        public int getSlotLimit(int slot) {
            return 1;
        }
    };
    private final ItemStackHandler deconstructOutputs = new ItemStackHandler(6){

        protected void onContentsChanged(int slot) {
            EngineeringTableBlockEntity.this.setChanged();
            if (EngineeringTableBlockEntity.this.level != null && !EngineeringTableBlockEntity.this.level.isClientSide()) {
                EngineeringTableBlockEntity.this.level.sendBlockUpdated(EngineeringTableBlockEntity.this.getBlockPos(), EngineeringTableBlockEntity.this.getBlockState(), EngineeringTableBlockEntity.this.getBlockState(), 3);
            }
        }
    };

    public EngineeringTableBlockEntity(BlockPos pos, BlockState state) {
        super((BlockEntityType)ModBlockEntities.ENGINEERING_TABLE_BLOCKENTITY.get(), pos, state);
    }

    public ItemStackHandler getCrafting() {
        return this.crafting;
    }

    public ItemStackHandler getDeconstructInput() {
        return this.deconstructInput;
    }

    public ItemStackHandler getDeconstructOutputs() {
        return this.deconstructOutputs;
    }

    public boolean outputsAreEmpty() {
        for (int i = 0; i < 6; ++i) {
            if (this.deconstructOutputs.getStackInSlot(i).isEmpty()) continue;
            return false;
        }
        return true;
    }

    public static boolean isDeconstructable(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        return stack.is(ModTags.Items.SCAVENGED_CYBERWARE) || stack.is(ModTags.Items.CYBERWARE_ITEM);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void tryInstantDeconstruct(ServerLevel level) {
        ItemStack in = this.deconstructInput.getStackInSlot(0);
        if (in.isEmpty()) {
            return;
        }
        if (!EngineeringTableBlockEntity.isDeconstructable(in)) {
            return;
        }
        if (!this.outputsAreEmpty()) {
            return;
        }
        this.deconProcessing = true;
        try {
            this.deconstructInput.setStackInSlot(0, ItemStack.EMPTY);
            for (int i = 0; i < 6; ++i) {
                ItemStack rolled = this.rollFromPool(level.random, in);
                this.deconstructOutputs.setStackInSlot(i, rolled);
            }
            level.playSound(null, this.worldPosition, ModSounds.METAL_CRUSHING.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
            this.setChanged();
            level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
        finally {
            this.deconProcessing = false;
        }
    }

    private ItemStack rollFromPool(RandomSource r, ItemStack originalInput) {
        boolean scavenged = originalInput.is(ModTags.Items.SCAVENGED_CYBERWARE);
        ArrayList<PoolEntry> pool = new ArrayList<PoolEntry>();
        if (scavenged) {
            pool.add(new PoolEntry((Item)ModItems.COMPONENT_ACTUATOR.get(), 0, 2, 3));
            pool.add(new PoolEntry((Item)ModItems.COMPONENT_FIBEROPTICS.get(), 0, 2, 3));
            pool.add(new PoolEntry((Item)ModItems.COMPONENT_WIRING.get(), 0, 3, 5));
            pool.add(new PoolEntry((Item)ModItems.COMPONENT_DIODES.get(), 0, 2, 2));
            pool.add(new PoolEntry((Item)ModItems.COMPONENT_PLATING.get(), 0, 2, 5));
            pool.add(new PoolEntry((Item)ModItems.COMPONENT_GRAPHICSCARD.get(), 0, 2, 2));
            pool.add(new PoolEntry((Item)ModItems.COMPONENT_SSD.get(), 0, 2, 3));
            pool.add(new PoolEntry((Item)ModItems.COMPONENT_STORAGE.get(), 0, 2, 4));
            pool.add(new PoolEntry((Item)ModItems.COMPONENT_SYNTHNERVES.get(), 0, 2, 1));
            pool.add(new PoolEntry((Item)ModItems.COMPONENT_MESH.get(), 0, 2, 2));
            if (ModItems.COMPONENT_LED != null) {
                pool.add(new PoolEntry((Item)ModItems.COMPONENT_LED.get(), 0, 2, 3));
            }
            if (ModItems.COMPONENT_TITANIUMROD != null) {
                pool.add(new PoolEntry((Item)ModItems.COMPONENT_TITANIUMROD.get(), 0, 2, 2));
            }
        } else {
            pool.add(new PoolEntry((Item)ModItems.COMPONENT_ACTUATOR.get(), 0, 5, 4));
            pool.add(new PoolEntry((Item)ModItems.COMPONENT_FIBEROPTICS.get(), 0, 5, 4));
            pool.add(new PoolEntry((Item)ModItems.COMPONENT_WIRING.get(), 1, 5, 5));
            pool.add(new PoolEntry((Item)ModItems.COMPONENT_DIODES.get(), 0, 5, 4));
            pool.add(new PoolEntry((Item)ModItems.COMPONENT_PLATING.get(), 0, 5, 5));
            pool.add(new PoolEntry((Item)ModItems.COMPONENT_GRAPHICSCARD.get(), 0, 5, 4));
            pool.add(new PoolEntry((Item)ModItems.COMPONENT_SSD.get(), 0, 5, 4));
            pool.add(new PoolEntry((Item)ModItems.COMPONENT_STORAGE.get(), 0, 5, 4));
            pool.add(new PoolEntry((Item)ModItems.COMPONENT_SYNTHNERVES.get(), 0, 5, 3));
            pool.add(new PoolEntry((Item)ModItems.COMPONENT_MESH.get(), 0, 5, 2));
            if (ModItems.COMPONENT_LED != null) {
                pool.add(new PoolEntry((Item)ModItems.COMPONENT_LED.get(), 0, 5, 3));
            }
            if (ModItems.COMPONENT_TITANIUMROD != null) {
                pool.add(new PoolEntry((Item)ModItems.COMPONENT_TITANIUMROD.get(), 0, 5, 2));
            }
        }
        if (pool.isEmpty()) {
            return ItemStack.EMPTY;
        }
        int total = 0;
        for (PoolEntry e : pool) {
            total += e.weight;
        }
        int roll = r.nextInt(total);
        for (PoolEntry e : pool) {
            if ((roll -= e.weight) >= 0) continue;
            return e.create(r);
        }
        return ((PoolEntry)pool.get(0)).create(r);
    }

    public void drops() {
        int i;
        SimpleContainer inv = new SimpleContainer(32);
        for (i = 0; i < 25; ++i) {
            inv.setItem(i, this.crafting.getStackInSlot(i));
        }
        inv.setItem(25, this.deconstructInput.getStackInSlot(0));
        for (i = 0; i < 6; ++i) {
            inv.setItem(26 + i, this.deconstructOutputs.getStackInSlot(i));
        }
        Containers.dropContents((Level)this.level, (BlockPos)this.worldPosition, (Container)inv);
    }

    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("crafting", (Tag)this.crafting.serializeNBT(registries));
        tag.put("deconstructInput", (Tag)this.deconstructInput.serializeNBT(registries));
        tag.put("deconstructOutputs", (Tag)this.deconstructOutputs.serializeNBT(registries));
    }

    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.crafting.deserializeNBT(registries, tag.getCompound("crafting"));
        this.deconstructInput.deserializeNBT(registries, tag.getCompound("deconstructInput"));
        this.deconstructOutputs.deserializeNBT(registries, tag.getCompound("deconstructOutputs"));
    }

    public Component getDisplayName() {
        return Component.translatable((String)"container.createcybernetics.engineering_table");
    }

    @Nullable
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new EngineeringTableMenu(id, inv, this.getBlockPos());
    }

    private static final class PoolEntry {
        final Item item;
        final int min;
        final int max;
        final int weight;

        PoolEntry(Item item, int min, int max, int weight) {
            this.item = item;
            this.min = min;
            this.max = max;
            this.weight = weight;
        }

        ItemStack create(RandomSource r) {
            int count = this.min == this.max ? this.min : Mth.nextInt((RandomSource)r, (int)this.min, (int)this.max);
            return new ItemStack((ItemLike)this.item, count);
        }
    }
}

