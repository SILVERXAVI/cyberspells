package cyberspells.menu;

import cyberspells.items.CyberRuneItem;
import cyberspells.registration.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import cyberspells.block.entity.RuneInfuserBlockEntity;
import org.jetbrains.annotations.NotNull;

public class RuneInfuserMenu extends AbstractContainerMenu {
    private final IItemHandler itemHandler;
    private final Player player;

    // Client-side constructor
    public RuneInfuserMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, new ItemStackHandler(7) {
            @Override
            public int getSlotLimit(int slot) {
                if (slot >= 1 && slot <= 5)
                    return 1;
                return super.getSlotLimit(slot);
            }
        });
    }

    // Server-side constructor (via BlockEntity)
    public RuneInfuserMenu(int containerId, Inventory playerInventory, RuneInfuserBlockEntity blockEntity) {
        this(containerId, playerInventory, blockEntity.getItemHandler());
    }

    // Internal constructor
    public RuneInfuserMenu(int containerId, Inventory playerInventory, IItemHandler handler) {
        super(ModMenus.RUNE_INFUSER_MENU.get(), containerId);
        this.itemHandler = handler;
        this.player = playerInventory.player;

        // Slot 0: Cyberware Item (Input)
        this.addSlot(new SlotItemHandler(itemHandler, 0, 80, 20) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() instanceof CyberRuneItem;
            }

            @Override
            public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
                ejectAllRunes(player);
                super.onTake(player, stack);
            }
        });

        // Slot 1-5: Rune Items (Fixed spread)
        for (int i = 0; i < 5; i++) {
            final int slotIndex = i;
            this.addSlot(new SlotItemHandler(itemHandler, 1 + i, 40 + (i * 20), 50) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    if (!stack.getDescriptionId().contains("rune"))
                        return false;
                    ItemStack input = itemHandler.getStackInSlot(0);
                    if (input.getItem() instanceof CyberRuneItem runeItem) {
                        return slotIndex < runeItem.getMaxSlots();
                    }
                    return false;
                }

                @Override
                public boolean isActive() {
                    ItemStack input = itemHandler.getStackInSlot(0);
                    if (input.getItem() instanceof CyberRuneItem runeItem) {
                        return slotIndex < runeItem.getMaxSlots();
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
                int activeSlots = 0;
                ItemStack input = itemHandler.getStackInSlot(0);
                if (input.getItem() instanceof CyberRuneItem runeItem) {
                    activeSlots = runeItem.getMaxSlots();
                }

                itemHandler.extractItem(0, 1, false);
                for (int i = 1; i <= 5; i++) {
                    if (i <= activeSlots) {
                        itemHandler.extractItem(i, 1, false);
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

    private void ejectAllRunes(Player player) {
        for (int i = 1; i <= 5; i++) {
            ItemStack runeStack = itemHandler.getStackInSlot(i);
            if (!runeStack.isEmpty()) {
                player.getInventory().placeItemBackInInventory(runeStack);
                itemHandler.extractItem(i, 64, false);
            }
        }
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        // Dynamic ejection: If a slot becomes inactive, throw it back to player
        ItemStack input = itemHandler.getStackInSlot(0);
        int maxSlots = 0;
        if (!input.isEmpty() && input.getItem() instanceof CyberRuneItem runeItem) {
            maxSlots = runeItem.getMaxSlots();
        }

        for (int i = 0; i < 5; i++) {
            if (i >= maxSlots) {
                ItemStack slotStack = itemHandler.getStackInSlot(1 + i);
                if (!slotStack.isEmpty()) {
                    player.getInventory().placeItemBackInInventory(slotStack);
                    itemHandler.extractItem(1 + i, 64, false);
                }
            }
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 7) {
                if (!this.moveItemStackTo(itemstack1, 7, 43, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else {
                if (!this.moveItemStackTo(itemstack1, 0, 6, false)) {
                    return ItemStack.EMPTY;
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
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}
