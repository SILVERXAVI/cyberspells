/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.Container
 *  net.minecraft.world.Containers
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.SimpleContainer
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.ContainerData
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.neoforged.bus.api.Event
 *  net.neoforged.neoforge.common.NeoForge
 *  net.neoforged.neoforge.items.IItemHandlerModifiable
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.maxwell.cyber_ware_port.common.block.scanner;

import com.maxwell.cyber_ware_port.api.event.CyberwareEvents;
import com.maxwell.cyber_ware_port.api.json.CyberwareAPI;
import com.maxwell.cyber_ware_port.common.container.ScannerMenu;
import com.maxwell.cyber_ware_port.common.item.BlueprintItem;
import com.maxwell.cyber_ware_port.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScannerBlockEntity
extends BlockEntity
implements MenuProvider {
    public static final int SLOT_PAPER = 0;
    public static final int SLOT_INPUT = 1;
    public static final int SLOT_OUTPUT = 2;
    public static final int MAX_PROGRESS = 2400;
    private static final int SLOT_COUNT = 3;
    protected final ContainerData data;
    private final ItemStackHandler itemHandler = new ItemStackHandler(3){

        protected void onContentsChanged(int slot) {
            ScannerBlockEntity.this.setChanged();
        }

        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0 -> stack.is(Items.PAPER);
                case 1 -> {
                    if (CyberwareAPI.getCyberware(stack) != null) {
                        yield true;
                    }
                    yield false;
                }
                case 2 -> false;
                default -> super.isItemValid(slot, stack);
            };
        }
    };
    private final IItemHandlerModifiable exposedHandler = new IItemHandlerModifiable(){

        public void setStackInSlot(int slot, @NotNull ItemStack stack) {
            ScannerBlockEntity.this.itemHandler.setStackInSlot(slot, stack);
        }

        public int getSlots() {
            return 3;
        }

        @NotNull
        public ItemStack getStackInSlot(int slot) {
            return ScannerBlockEntity.this.itemHandler.getStackInSlot(slot);
        }

        @NotNull
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (stack.isEmpty()) {
                return stack;
            }
            if (slot == 0 && stack.is(Items.PAPER)) {
                return ScannerBlockEntity.this.itemHandler.insertItem(0, stack, simulate);
            }
            if (slot == 1 && CyberwareAPI.getCyberware(stack) != null) {
                return ScannerBlockEntity.this.itemHandler.insertItem(1, stack, simulate);
            }
            return stack;
        }

        @NotNull
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return slot == 2 ? ScannerBlockEntity.this.itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }

        public int getSlotLimit(int slot) {
            return ScannerBlockEntity.this.itemHandler.getSlotLimit(slot);
        }

        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return ScannerBlockEntity.this.itemHandler.isItemValid(slot, stack);
        }
    };
    private int progress = 0;
    private boolean isWorking = false;

    public ScannerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super((BlockEntityType)ModBlockEntities.SCANNER.get(), pPos, pBlockState);
        this.data = new ContainerData(){

            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> ScannerBlockEntity.this.progress;
                    case 1 -> 2400;
                    default -> 0;
                };
            }

            public void set(int pIndex, int pValue) {
                if (pIndex == 0) {
                    ScannerBlockEntity.this.progress = pValue;
                }
            }

            public int getCount() {
                return 2;
            }
        };
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, ScannerBlockEntity pEntity) {
        if (pLevel.isClientSide()) {
            if (pEntity.isWorking) {
                ++pEntity.progress;
                if (pEntity.progress >= 2400) {
                    pEntity.progress = 0;
                }
            } else {
                pEntity.progress = 0;
            }
            return;
        }
        if (pEntity.hasRecipe()) {
            ++pEntity.progress;
            if (!pEntity.isWorking) {
                pEntity.isWorking = true;
                pEntity.syncToClient();
            }
            if (pLevel.getGameTime() % 20L == 0L) {
                pLevel.playSound(null, pPos, SoundEvents.CONDUIT_AMBIENT, SoundSource.BLOCKS, 0.8f, 1.2f);
                pLevel.playSound(null, pPos, SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 0.5f, 1.5f);
            }
            ScannerBlockEntity.setChanged((Level)pLevel, (BlockPos)pPos, (BlockState)pState);
            if (pEntity.progress >= 2400) {
                pEntity.craftItem();
                pLevel.playSound(null, pPos, (SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), SoundSource.BLOCKS, 1.0f, 1.2f);
                pEntity.progress = 0;
                if (!pEntity.hasRecipe()) {
                    pEntity.isWorking = false;
                    pEntity.syncToClient();
                }
            }
        } else {
            pEntity.progress = 0;
            if (pEntity.isWorking) {
                pEntity.isWorking = false;
                pEntity.syncToClient();
            }
        }
    }

    private void syncToClient() {
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public boolean isWorking() {
        return this.isWorking;
    }

    public float getProgress() {
        return this.progress;
    }

    private boolean hasRecipe() {
        ItemStack paperStack = this.itemHandler.getStackInSlot(0);
        ItemStack inputStack = this.itemHandler.getStackInSlot(1);
        ItemStack outputStack = this.itemHandler.getStackInSlot(2);
        return paperStack.is(Items.PAPER) && CyberwareAPI.getCyberware(inputStack) != null && outputStack.isEmpty();
    }

    private void craftItem() {
        if (!this.hasRecipe()) {
            return;
        }
        ItemStack inputStack = this.itemHandler.getStackInSlot(1);
        CyberwareEvents.Scan.Complete event = new CyberwareEvents.Scan.Complete(this, inputStack, 0.5f);
        if (((CyberwareEvents.Scan.Complete)NeoForge.EVENT_BUS.post((Event)event)).isCanceled()) {
            return;
        }
        if (this.level.random.nextFloat() < event.getChance()) {
            ItemStack blueprint = BlueprintItem.createBlueprintFor(inputStack.getItem());
            this.itemHandler.setStackInSlot(2, blueprint);
        }
        if (event.shouldConsumeItem()) {
            this.itemHandler.extractItem(0, 1, false);
            this.itemHandler.extractItem(1, 1, false);
        }
    }

    @NotNull
    public Component getDisplayName() {
        return Component.translatable((String)"block.cyber_ware_port.scanner");
    }

    @Nullable
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ScannerMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.put("inventory", (Tag)this.itemHandler.serializeNBT(pRegistries));
        pTag.putInt("scanner.progress", this.progress);
        pTag.putBoolean("scanner.isWorking", this.isWorking);
    }

    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        if (pTag.contains("inventory")) {
            this.itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
        }
        this.progress = pTag.getInt("scanner.progress");
        this.isWorking = pTag.getBoolean("scanner.isWorking");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for (int i = 0; i < this.itemHandler.getSlots(); ++i) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }
        Containers.dropContents((Level)this.level, (BlockPos)this.worldPosition, (Container)inventory);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create((BlockEntity)this);
    }

    @NotNull
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return this.saveWithoutMetadata(pRegistries);
    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    public IItemHandlerModifiable getExposedHandler() {
        return this.exposedHandler;
    }
}

