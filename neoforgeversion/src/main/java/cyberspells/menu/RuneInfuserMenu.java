package cyberspells.menu;

import cyberspells.items.RuneHolder;
import cyberspells.registration.ModMenus;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import cyberspells.block.entity.RuneInfuserBlockEntity;
import org.jetbrains.annotations.NotNull;

public class RuneInfuserMenu extends AbstractContainerMenu {
    private final IItemHandler itemHandler;
    private final Player player;
    private final RuneInfuserBlockEntity blockEntity;

    // Client-side constructor
    public RuneInfuserMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf extraData) {
        this(containerId, playerInventory, new ItemStackHandler(7), null);
    }

    // Server-side constructor
    public RuneInfuserMenu(int containerId, Inventory playerInventory, RuneInfuserBlockEntity blockEntity) {
        this(containerId, playerInventory, blockEntity.getItemHandler(), blockEntity);
    }

    // Internal constructor
    public RuneInfuserMenu(int containerId, Inventory playerInventory, IItemHandler handler,
            RuneInfuserBlockEntity blockEntity) {
        super(ModMenus.RUNE_INFUSER_MENU.get(), containerId);
        this.itemHandler = handler;
        this.player = playerInventory.player;
        this.blockEntity = blockEntity;

        if (this.blockEntity != null) {
            this.blockEntity.interactingPlayer = this.player;
        }

        // Slot 0: Cyberware (Input)
        this.addSlot(new SlotItemHandler(itemHandler, 0, 80, 20) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() instanceof RuneHolder;
            }
        });

        // Slots 1-5: Runes
        for (int i = 0; i < 5; i++) {
            final int slotIndex = i;
            this.addSlot(new SlotItemHandler(itemHandler, 1 + i, 40 + (i * 20), 50) {
                @Override
                public boolean isActive() {
                    ItemStack cyberware = itemHandler.getStackInSlot(0);
                    if (!cyberware.isEmpty() && cyberware.getItem() instanceof RuneHolder holder) {
                        return slotIndex < holder.getMaxRuneSlots();
                    }
                    return false;
                }

                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    ItemStack cyberware = itemHandler.getStackInSlot(0);
                    if (!cyberware.isEmpty() && cyberware.getItem() instanceof RuneHolder holder) {
                        return slotIndex < holder.getMaxRuneSlots();
                    }
                    return false;
                }
            });
        }

        // Slot 6: Output
        this.addSlot(new SlotItemHandler(itemHandler, 6, 145, 35) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
                if (handler instanceof ItemStackHandler) {
                    // Logic for consumption: Shrink ingredients
                    // We need a reference to the BE or a way to call clearIngredients
                    if (blockEntity != null) {
                        blockEntity.clearIngredients();
                    }
                }
                super.onTake(player, stack);
            }
        });

        // Player Inventory
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Hotbar
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 7) { // From machine to inventory
                if (!this.moveItemStackTo(itemstack1, 7, 43, true)) {
                    return ItemStack.EMPTY;
                }
            } else { // From inventory to machine
                if (itemstack1.getItem() instanceof RuneHolder) {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    // Try to move to rune slots (1-5)
                    if (!this.moveItemStackTo(itemstack1, 1, 6, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (this.blockEntity != null) {
            this.blockEntity.interactingPlayer = null;
        }
    }
}
