package cyberspells.block.entity;

import cyberspells.menu.RuneInfuserMenu;
import cyberspells.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RuneInfuserBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(7) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (slot >= 0 && slot <= 5) {
                tryInfuse();
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            if (slot >= 1 && slot <= 5) {
                return 1;
            }
            return super.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == 6)
                return false; // Output slot
            return super.isItemValid(slot, stack);
        }
    };

    private void tryInfuse() {
        ItemStack cyberware = itemHandler.getStackInSlot(0);
        if (cyberware.isEmpty()) {
            itemHandler.setStackInSlot(6, ItemStack.EMPTY);
            return;
        }

        ItemStack result = cyberware.copy();
        boolean changed = false;

        // Check if there are any new runes to apply
        boolean hasNewRunes = false;
        for (int i = 1; i <= 5; i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                hasNewRunes = true;
                break;
            }
        }

        // If new runes are present, clear existing runes on the result item (Overwrite
        // Mode)
        if (hasNewRunes) {
            if (result.getItem() instanceof cyberspells.items.CyberRuneArmItem arm) {
                arm.clearRunes(result);
            } else if (result.getItem() instanceof cyberspells.items.CyberRuneLegItem leg) {
                leg.clearRunes(result);
            } else if (result.getItem() instanceof cyberspells.items.CyberRuneHeartItem heart) {
                heart.clearRunes(result);
            }
            // Note: CyberRuneItem base class usage check might be needed if other items
            // exist,
            // but currently specific classes are checked.
        }

        // Iterate over rune slots
        for (int i = 1; i <= 5; i++) {
            ItemStack rune = itemHandler.getStackInSlot(i);
            if (!rune.isEmpty()) {
                String runeId = net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(rune.getItem()).toString();
                if (result.getItem() instanceof cyberspells.items.CyberRuneItem runeItem) {
                    if (runeItem.getRunes(result).size() < runeItem.getMaxSlots()) {
                        runeItem.addRune(result, runeId);
                        changed = true;
                    }
                }
            }
        }

        if (changed) {
            // Only update if different to prevent loop/spam
            if (!ItemStack.matches(itemHandler.getStackInSlot(6), result)) {
                itemHandler.setStackInSlot(6, result);
            }
        } else {
            itemHandler.setStackInSlot(6, ItemStack.EMPTY);
        }
    }

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public RuneInfuserBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RUNE_INFUSER_BE.get(), pos, state);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        CompoundTag invTag = nbt.getCompound("inventory");
        if (invTag.contains("Size", net.minecraft.nbt.Tag.TAG_INT) && invTag.getInt("Size") < 7) {
            invTag.putInt("Size", 7);
        }
        itemHandler.deserializeNBT(invTag);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.cyberspells.rune_infuser");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new RuneInfuserMenu(containerId, playerInventory, this);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }
}
