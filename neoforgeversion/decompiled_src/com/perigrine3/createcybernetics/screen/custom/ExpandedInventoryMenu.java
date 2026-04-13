/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.Holder$Reference
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.HolderLookup$RegistryLookup
 *  net.minecraft.core.NonNullList
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.Container
 *  net.minecraft.world.ContainerHelper
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.EquipmentSlot$Type
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.player.StackedContents
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.ContainerLevelAccess
 *  net.minecraft.world.inventory.CraftingContainer
 *  net.minecraft.world.inventory.InventoryMenu
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.inventory.ResultContainer
 *  net.minecraft.world.inventory.ResultSlot
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.CraftingInput
 *  net.minecraft.world.item.crafting.CraftingRecipe
 *  net.minecraft.world.item.crafting.RecipeHolder
 *  net.minecraft.world.item.crafting.RecipeInput
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.item.enchantment.EnchantmentHelper
 *  net.minecraft.world.item.enchantment.Enchantments
 *  net.minecraft.world.level.Level
 */
package com.perigrine3.createcybernetics.screen.custom;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.screen.ModMenuTypes;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class ExpandedInventoryMenu
extends AbstractContainerMenu {
    public static final int SLOT_RESULT = 0;
    private static final int SLOT_CRAFT_START = 1;
    private static final int SLOT_CRAFT_COUNT = 9;
    private static final int SLOT_ARMOR_START = 10;
    private static final int SLOT_ARMOR_COUNT = 4;
    private static final int SLOT_INV_START = 14;
    private static final int SLOT_INV_COUNT = 27;
    private static final int SLOT_HOTBAR_START = 41;
    private static final int SLOT_HOTBAR_COUNT = 9;
    private static final int SLOT_OFFHAND = 50;
    private final Player player;
    private final ContainerLevelAccess access;
    private final CraftingContainer craftMatrix;
    private final ResultContainer craftResult = new ResultContainer();
    private static final int DATA_SHARD_SLOT_COUNT = 2;
    public static final int DATA_SHARD_X = 77;
    public static final int DATA_SHARD_Y = 8;
    public static final int DATA_SHARD_SPACING = 18;
    private final boolean hasChipwareSlots;
    private final Container dataShardInv;
    private int dataShardStart = -1;
    private int dataShardEnd = -1;

    public ExpandedInventoryMenu(int containerId, Inventory playerInv, FriendlyByteBuf buf) {
        this(containerId, playerInv, ContainerLevelAccess.NULL);
    }

    public ExpandedInventoryMenu(int containerId, Inventory playerInv) {
        this(containerId, playerInv, ContainerLevelAccess.NULL);
    }

    public ExpandedInventoryMenu(int containerId, Inventory playerInv, ContainerLevelAccess access) {
        super((MenuType)ModMenuTypes.EXPANDED_INVENTORY_MENU.get(), containerId);
        this.player = playerInv.player;
        this.access = access;
        this.craftMatrix = new SimpleCraftingContainer(this, 3, 3);
        this.hasChipwareSlots = ExpandedInventoryMenu.hasChipwareInstalled(this.player);
        this.dataShardInv = this.hasChipwareSlots ? new ChipwareContainer(this.player, this) : null;
        this.addSlot((Slot)new ResultSlot(playerInv.player, this.craftMatrix, (Container)this.craftResult, 0, 154, 28));
        int craftX = 98;
        int craftY = 18;
        int idx = 0;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                this.addSlot(new Slot((Container)this.craftMatrix, idx++, craftX + col * 18, craftY + row * 18));
            }
        }
        this.addArmorSlots(playerInv);
        this.addPlayerInventory(playerInv);
        this.addPlayerHotbar(playerInv);
        this.addOffhandSlot(playerInv);
        this.addDataShardSlotsIfPresent();
        Player col = this.player;
        if (col instanceof ServerPlayer) {
            ServerPlayer sp = (ServerPlayer)col;
            this.updateCraftingResult(sp, sp.serverLevel());
            if (this.hasDataShardSlots()) {
                for (int i = 0; i < 2; ++i) {
                    ItemStack st = this.dataShardInv.getItem(i);
                    sp.connection.send((Packet)new ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId(), this.dataShardStart + i, st));
                }
            }
        }
    }

    public boolean hasDataShardSlots() {
        return this.hasChipwareSlots && this.dataShardStart >= 0 && this.dataShardEnd > this.dataShardStart;
    }

    private void addDataShardSlotsIfPresent() {
        if (!this.hasChipwareSlots || this.dataShardInv == null) {
            return;
        }
        this.dataShardStart = this.slots.size();
        this.addSlot(new DataShardSlot(this.dataShardInv, 0, 77, 8));
        this.addSlot(new DataShardSlot(this.dataShardInv, 1, 77, 26));
        this.dataShardEnd = this.slots.size();
    }

    public void slotsChanged(Container changed) {
        super.slotsChanged(changed);
        if (changed != this.craftMatrix) {
            return;
        }
        Player player = this.player;
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer sp = (ServerPlayer)player;
        this.access.execute((level, pos) -> {
            if (level instanceof ServerLevel) {
                ServerLevel sl = (ServerLevel)level;
                this.updateCraftingResult(sp, sl);
            }
        });
    }

    private void updateCraftingResult(ServerPlayer sp, ServerLevel level) {
        CraftingInput input = this.craftMatrix.asCraftInput();
        ItemStack result = ItemStack.EMPTY;
        Optional match = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, (RecipeInput)input, (Level)level);
        if (match.isPresent()) {
            RecipeHolder holder = (RecipeHolder)match.get();
            if (this.craftResult.setRecipeUsed((Level)level, sp, holder)) {
                result = ((CraftingRecipe)holder.value()).assemble((RecipeInput)input, (HolderLookup.Provider)level.registryAccess());
            }
        } else {
            this.craftResult.setRecipeUsed(null);
        }
        this.craftResult.setItem(0, result);
        sp.connection.send((Packet)new ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId(), 0, result));
    }

    private void addArmorSlots(Inventory playerInv) {
        int x = 8;
        int y = 8;
        this.addSlot(new EquipmentSlotSlot((Container)playerInv, 39, x, y + 0, this.player, EquipmentSlot.HEAD, InventoryMenu.EMPTY_ARMOR_SLOT_HELMET));
        this.addSlot(new EquipmentSlotSlot((Container)playerInv, 38, x, y + 18, this.player, EquipmentSlot.CHEST, InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE));
        this.addSlot(new EquipmentSlotSlot((Container)playerInv, 37, x, y + 36, this.player, EquipmentSlot.LEGS, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS));
        this.addSlot(new EquipmentSlotSlot((Container)playerInv, 36, x, y + 54, this.player, EquipmentSlot.FEET, InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS));
    }

    private void addOffhandSlot(Inventory playerInv) {
        Slot s = new Slot((Container)playerInv, 40, 77, 62);
        s.setBackground(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
        this.addSlot(s);
    }

    private void addPlayerInventory(Inventory inv) {
        int invX = 8;
        int invY = 84;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot((Container)inv, col + row * 9 + 9, invX + col * 18, invY + row * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory inv) {
        int invX = 8;
        int hotbarY = 142;
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot((Container)inv, col, invX + col * 18, hotbarY));
        }
    }

    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide) {
            this.clearContainer(player, (Container)this.craftMatrix);
            this.craftResult.clearContent();
        }
    }

    public boolean stillValid(Player player) {
        return true;
    }

    public ItemStack quickMoveStack(Player player, int index) {
        int allEnd;
        Slot slot = (Slot)this.slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();
        int craftEnd = 10;
        int armorEnd = 14;
        int invEnd = 41;
        int hotbarEnd = 50;
        int n = allEnd = this.hasDataShardSlots() ? this.dataShardEnd : 51;
        if (index == 0) {
            if (!this.moveItemStackTo(stack, 14, allEnd, true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(stack, copy);
        } else if (index >= 1 && index < craftEnd) {
            if (!this.moveItemStackTo(stack, 14, allEnd, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index >= 10 && index < armorEnd) {
            if (!this.moveItemStackTo(stack, 14, allEnd, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index == 50) {
            if (!this.moveItemStackTo(stack, 14, hotbarEnd, false)) {
                return ItemStack.EMPTY;
            }
        } else if (this.hasDataShardSlots() && index >= this.dataShardStart && index < this.dataShardEnd) {
            if (!this.moveItemStackTo(stack, 14, hotbarEnd, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index >= 14 && index < hotbarEnd) {
            int target;
            EquipmentSlot eq;
            if (this.hasDataShardSlots() && stack.is(ModTags.Items.DATA_SHARDS)) {
                this.moveItemStackTo(stack, this.dataShardStart, this.dataShardEnd, false);
            }
            if ((eq = player.getEquipmentSlotForItem(stack)).getType() == EquipmentSlot.Type.HUMANOID_ARMOR ? ((target = ExpandedInventoryMenu.armorTargetIndex(eq)) == -1 || !this.moveItemStackTo(stack, target, target + 1, false)) && (index < invEnd ? !this.moveItemStackTo(stack, 41, hotbarEnd, false) : !this.moveItemStackTo(stack, 14, invEnd, false)) : (eq == EquipmentSlot.OFFHAND ? !this.moveItemStackTo(stack, 50, 51, false) && (index < invEnd ? !this.moveItemStackTo(stack, 41, hotbarEnd, false) : !this.moveItemStackTo(stack, 14, invEnd, false)) : (index < invEnd ? !this.moveItemStackTo(stack, 41, hotbarEnd, false) : !this.moveItemStackTo(stack, 14, invEnd, false)))) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (stack.getCount() == copy.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, stack);
        return copy;
    }

    private static int armorTargetIndex(EquipmentSlot eq) {
        return switch (eq) {
            case EquipmentSlot.HEAD -> 10;
            case EquipmentSlot.CHEST -> 11;
            case EquipmentSlot.LEGS -> 12;
            case EquipmentSlot.FEET -> 13;
            default -> -1;
        };
    }

    public Container getCraftMatrix() {
        return this.craftMatrix;
    }

    public Container getCraftResult() {
        return this.craftResult;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ContainerLevelAccess getAccess() {
        return this.access;
    }

    private static boolean hasChipwareInstalled(Player player) {
        if (player == null) {
            return false;
        }
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return false;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return false;
        }
        return data.hasSpecificItem((Item)ModItems.BRAINUPGRADES_CHIPWARESLOTS.get(), CyberwareSlot.BRAIN);
    }

    private static final class SimpleCraftingContainer
    implements CraftingContainer {
        private final AbstractContainerMenu menu;
        private final int w;
        private final int h;
        private final NonNullList<ItemStack> items;

        private SimpleCraftingContainer(AbstractContainerMenu menu, int w, int h) {
            this.menu = menu;
            this.w = w;
            this.h = h;
            this.items = NonNullList.withSize((int)(w * h), (Object)ItemStack.EMPTY);
        }

        public void fillStackedContents(StackedContents stackedContents) {
        }

        public void clearContent() {
            for (int i = 0; i < this.items.size(); ++i) {
                this.items.set(i, (Object)ItemStack.EMPTY);
            }
            this.setChanged();
        }

        public int getContainerSize() {
            return this.items.size();
        }

        public boolean isEmpty() {
            for (ItemStack s : this.items) {
                if (s.isEmpty()) continue;
                return false;
            }
            return true;
        }

        public ItemStack getItem(int i) {
            return i >= 0 && i < this.items.size() ? (ItemStack)this.items.get(i) : ItemStack.EMPTY;
        }

        public ItemStack removeItem(int i, int count) {
            ItemStack out = ContainerHelper.removeItem(this.items, (int)i, (int)count);
            if (!out.isEmpty()) {
                this.setChanged();
            }
            return out;
        }

        public ItemStack removeItemNoUpdate(int i) {
            ItemStack out = ContainerHelper.takeItem(this.items, (int)i);
            if (!out.isEmpty()) {
                this.setChanged();
            }
            return out;
        }

        public void setItem(int i, ItemStack stack) {
            if (i >= 0 && i < this.items.size()) {
                this.items.set(i, (Object)stack);
                this.setChanged();
            }
        }

        public void setChanged() {
            this.menu.slotsChanged((Container)this);
        }

        public boolean stillValid(Player player) {
            return true;
        }

        public int getWidth() {
            return this.w;
        }

        public int getHeight() {
            return this.h;
        }

        public List<ItemStack> getItems() {
            return this.items;
        }
    }

    private static final class ChipwareContainer
    implements Container {
        private final Player player;
        private final AbstractContainerMenu menu;

        private ChipwareContainer(Player player, AbstractContainerMenu menu) {
            this.player = player;
            this.menu = menu;
        }

        private PlayerCyberwareData data() {
            if (this.player == null) {
                return null;
            }
            if (!this.player.hasData(ModAttachments.CYBERWARE)) {
                return null;
            }
            return (PlayerCyberwareData)this.player.getData(ModAttachments.CYBERWARE);
        }

        public int getContainerSize() {
            return 2;
        }

        public boolean isEmpty() {
            for (int i = 0; i < this.getContainerSize(); ++i) {
                if (this.getItem(i).isEmpty()) continue;
                return false;
            }
            return true;
        }

        public ItemStack getItem(int slot) {
            PlayerCyberwareData d = this.data();
            if (d == null) {
                return ItemStack.EMPTY;
            }
            return d.getChipwareStack(slot);
        }

        public ItemStack removeItem(int slot, int count) {
            if (count <= 0) {
                return ItemStack.EMPTY;
            }
            ItemStack cur = this.getItem(slot);
            if (cur.isEmpty()) {
                return ItemStack.EMPTY;
            }
            this.setItem(slot, ItemStack.EMPTY);
            return cur;
        }

        public ItemStack removeItemNoUpdate(int slot) {
            ItemStack cur = this.getItem(slot);
            if (cur.isEmpty()) {
                return ItemStack.EMPTY;
            }
            PlayerCyberwareData d = this.data();
            if (d != null) {
                d.setChipwareStack(slot, ItemStack.EMPTY);
            }
            return cur;
        }

        public void setItem(int slot, ItemStack stack) {
            PlayerCyberwareData d = this.data();
            if (d == null) {
                return;
            }
            d.setChipwareStack(slot, stack);
            this.setChanged();
        }

        public int getMaxStackSize() {
            return 1;
        }

        public void setChanged() {
            this.menu.slotsChanged((Container)this);
            this.menu.broadcastChanges();
        }

        public boolean stillValid(Player player) {
            return true;
        }

        public void clearContent() {
            for (int i = 0; i < this.getContainerSize(); ++i) {
                this.setItem(i, ItemStack.EMPTY);
            }
        }
    }

    private static final class DataShardSlot
    extends Slot {
        private DataShardSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        public boolean mayPlace(ItemStack stack) {
            return !stack.isEmpty() && stack.is(ModTags.Items.DATA_SHARDS);
        }

        public int getMaxStackSize() {
            return 1;
        }
    }

    private static final class EquipmentSlotSlot
    extends Slot {
        private final Player owner;
        private final EquipmentSlot equip;

        private EquipmentSlotSlot(Container container, int index, int x, int y, Player owner, EquipmentSlot equip, ResourceLocation emptySprite) {
            super(container, index, x, y);
            this.owner = owner;
            this.equip = equip;
            this.setBackground(InventoryMenu.BLOCK_ATLAS, emptySprite);
        }

        public int getMaxStackSize() {
            return 1;
        }

        public boolean mayPlace(ItemStack stack) {
            return this.owner.getEquipmentSlotForItem(stack) == this.equip;
        }

        public boolean mayPickup(Player player) {
            ItemStack stack = this.getItem();
            if (stack.isEmpty()) {
                return true;
            }
            if (player.isCreative()) {
                return true;
            }
            HolderLookup.RegistryLookup enchReg = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            Holder.Reference binding = enchReg.getOrThrow(Enchantments.BINDING_CURSE);
            return EnchantmentHelper.getItemEnchantmentLevel((Holder)binding, (ItemStack)stack) <= 0;
        }
    }
}

