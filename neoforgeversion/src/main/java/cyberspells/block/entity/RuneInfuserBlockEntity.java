package cyberspells.block.entity;

import cyberspells.menu.RuneInfuserMenu;
import cyberspells.registration.ModBlockEntities;
import cyberspells.items.RuneHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RuneInfuserBlockEntity extends BlockEntity implements MenuProvider {
    @Nullable
    public Player interactingPlayer = null;

    private final ItemStackHandler itemHandler = new ItemStackHandler(7) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (slot == 0) {
                ejectInvalidRunes();
            }
            if (slot >= 0 && slot <= 5) {
                tryInfuse();
            }
        }

        private void ejectInvalidRunes() {
            if (level == null || level.isClientSide)
                return;

            ItemStack cyberware = itemHandler.getStackInSlot(0);
            int maxSlots = 0;
            if (!cyberware.isEmpty() && cyberware.getItem() instanceof RuneHolder runeItem) {
                maxSlots = runeItem.getMaxRuneSlots();
            }

            for (int i = 1; i <= 5; i++) {
                if (i > maxSlots) {
                    ItemStack stack = itemHandler.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        if (interactingPlayer != null) {
                            if (!interactingPlayer.getInventory().add(stack)) {
                                interactingPlayer.drop(stack, false);
                            }
                        } else {
                            net.minecraft.world.Containers.dropItemStack(level, worldPosition.getX() + 0.5,
                                    worldPosition.getY() + 0.5,
                                    worldPosition.getZ() + 0.5, stack);
                        }
                        itemHandler.setStackInSlot(i, ItemStack.EMPTY);
                    }
                }
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            return (slot >= 1 && slot <= 5) ? 1 : 64;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == 6)
                return false;
            return super.isItemValid(slot, stack);
        }
    };

    public void clearIngredients() {
        ItemStack cyberware = itemHandler.getStackInSlot(0);
        int maxSlots = 0;
        if (!cyberware.isEmpty() && cyberware.getItem() instanceof RuneHolder runeItem) {
            maxSlots = runeItem.getMaxRuneSlots();
        }

        itemHandler.getStackInSlot(0).shrink(1);
        for (int i = 1; i <= maxSlots; i++) {
            itemHandler.getStackInSlot(i).shrink(1);
        }
    }

    public RuneInfuserBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RUNE_INFUSER_BE.get(), pos, state);
    }

    private void tryInfuse() {
        ItemStack cyberware = itemHandler.getStackInSlot(0);
        if (cyberware.isEmpty() || !(cyberware.getItem() instanceof RuneHolder runeItem)) {
            itemHandler.setStackInSlot(6, ItemStack.EMPTY);
            return;
        }

        java.util.List<String> runes = new java.util.ArrayList<>();
        int maxSlots = runeItem.getMaxRuneSlots();

        net.minecraft.world.item.component.ItemAttributeModifiers.Builder modifierBuilder = net.minecraft.world.item.component.ItemAttributeModifiers
                .builder();

        for (int i = 1; i <= 5; i++) {
            ItemStack runeStack = itemHandler.getStackInSlot(i);
            if (!runeStack.isEmpty()) {
                if (runes.size() < maxSlots) {
                    net.minecraft.resources.ResourceLocation runeLoc = net.minecraft.core.registries.BuiltInRegistries.ITEM
                            .getKey(runeStack.getItem());
                    String runeId = runeLoc.toString();
                    runes.add(runeId);
                }
            }
        }

        // Add attributes for all collected runes after the list is populated
        for (int i = 0; i < runes.size(); i++) {
            String runeId = runes.get(i);
            cyberspells.logic.RuneAttributeManager.addAttributes(modifierBuilder, runeId, String.valueOf(i));
        }

        if (!runes.isEmpty()) {
            ItemStack result = cyberware.copy();
            result.set(cyberspells.registration.ModDataComponents.RUNES.get(), runes);
            result.set(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS, modifierBuilder.build());
            itemHandler.setStackInSlot(6, result);
        } else {
            itemHandler.setStackInSlot(6, ItemStack.EMPTY);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        nbt.put("inventory", itemHandler.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        if (nbt.contains("inventory")) {
            itemHandler.deserializeNBT(registries, nbt.getCompound("inventory"));
        }
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
