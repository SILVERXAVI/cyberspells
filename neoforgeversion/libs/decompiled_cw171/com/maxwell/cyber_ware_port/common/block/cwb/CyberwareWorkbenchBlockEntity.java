/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.Connection
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.Container
 *  net.minecraft.world.Containers
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.SimpleContainer
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.crafting.RecipeHolder
 *  net.minecraft.world.item.crafting.RecipeInput
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.item.crafting.SingleRecipeInput
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.neoforged.bus.api.Event
 *  net.neoforged.neoforge.common.NeoForge
 *  net.neoforged.neoforge.items.IItemHandler
 *  net.neoforged.neoforge.items.IItemHandlerModifiable
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.maxwell.cyber_ware_port.common.block.cwb;

import com.maxwell.cyber_ware_port.api.event.CyberwareEvents;
import com.maxwell.cyber_ware_port.api.json.CyberwareAPI;
import com.maxwell.cyber_ware_port.common.block.cwb.CyberwareWorkbenchBlock;
import com.maxwell.cyber_ware_port.common.block.cwb.recipe.AssemblyRecipe;
import com.maxwell.cyber_ware_port.common.block.cwb.recipe.EngineeringRecipe;
import com.maxwell.cyber_ware_port.common.container.CyberwareWorkbenchMenu;
import com.maxwell.cyber_ware_port.common.item.BlueprintItem;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import com.maxwell.cyber_ware_port.config.CyberwareConfig;
import com.maxwell.cyber_ware_port.init.ModBlockEntities;
import com.maxwell.cyber_ware_port.init.ModRecipes;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CyberwareWorkbenchBlockEntity
extends BlockEntity
implements MenuProvider {
    public static final int INPUT_SLOT = 0;
    public static final int PAPER_SLOT = 1;
    public static final int BLUEPRINT_SLOT = 2;
    public static final int OUTPUT_SLOT_START = 3;
    public static final int OUTPUT_SLOT_END = 8;
    public static final int SPECIAL_OUTPUT_SLOT = 9;
    private static final int INVENTORY_SIZE = 10;
    public float animationProgress = 0.0f;
    public float prevAnimationProgress = 0.0f;
    private AssemblyRecipe cachedRecipe = null;
    private final ItemStackHandler itemHandler = new ItemStackHandler(10){

        protected void onContentsChanged(int slot) {
            CyberwareWorkbenchBlockEntity.this.setChanged();
            if (slot == 2) {
                CyberwareWorkbenchBlockEntity.this.cachedRecipe = null;
            }
        }

        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot >= 3) {
                return true;
            }
            return switch (slot) {
                case 0 -> CyberwareAPI.isCyberware(stack);
                case 1 -> stack.is(Items.PAPER);
                case 2 -> stack.getItem() instanceof BlueprintItem;
                default -> false;
            };
        }
    };
    private final IItemHandlerModifiable sideInputHandler = new SidedProxyHandler(this.itemHandler, true, false, 0, 1, 2);
    private final IItemHandlerModifiable sideIngredientHandler = new SidedProxyHandler(this.itemHandler, true, false, 3, 4, 5, 6, 7, 8);
    private final IItemHandlerModifiable sideOutputHandler = new SidedProxyHandler(this.itemHandler, false, true, 3, 4, 5, 6, 7, 8, 9);
    private final IItemHandlerModifiable sideFrontHandler = new SidedProxyHandler(this.itemHandler, true, false, 2);
    private int progress = 0;
    private boolean isCrafting = false;
    private int cooldown = 0;

    public CyberwareWorkbenchBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super((BlockEntityType)ModBlockEntities.CYBERWARE_WORKBENCH.get(), pPos, pBlockState);
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, CyberwareWorkbenchBlockEntity pBlockEntity) {
        pBlockEntity.prevAnimationProgress = pBlockEntity.animationProgress;
        if (pBlockEntity.cooldown > 0) {
            --pBlockEntity.cooldown;
        }
        float target = pBlockEntity.isCrafting ? 1.0f : 0.0f;
        float speed = 0.1f;
        if (pBlockEntity.animationProgress < target) {
            pBlockEntity.animationProgress = Math.min(pBlockEntity.animationProgress + speed, target);
        } else if (pBlockEntity.animationProgress > target) {
            pBlockEntity.animationProgress = Math.max(pBlockEntity.animationProgress - speed, target);
        }
        if (!pLevel.isClientSide) {
            if (pLevel.hasNeighborSignal(pPos)) {
                pBlockEntity.startCrafting();
            }
            if (pBlockEntity.cooldown == 0 && pBlockEntity.isCrafting) {
                pLevel.playSound(null, pPos, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.5f, 1.2f);
                pBlockEntity.cooldown = 3;
                pBlockEntity.craftItem();
            }
            if (pBlockEntity.isCrafting && pLevel.getGameTime() % 40L == 0L) {
                pLevel.playSound(null, pPos, SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 0.2f, 1.2f);
                pLevel.playSound(null, pPos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 0.3f, 1.5f);
            }
            if (pBlockEntity.isCrafting && pBlockEntity.animationProgress >= 1.0f) {
                pLevel.playSound(null, pPos, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.BLOCKS, 0.5f, 1.2f);
                pBlockEntity.resetCrafting();
            }
        }
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for (int i = 0; i < this.itemHandler.getSlots(); ++i) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }
        Containers.dropContents((Level)this.level, (BlockPos)this.worldPosition, (Container)inventory);
    }

    public void startCrafting() {
        if (!this.isCrafting && this.canCraft()) {
            this.isCrafting = true;
            this.animationProgress = 0.0f;
            this.progress = 0;
            this.setChanged();
            this.notifyClient();
        }
    }

    private void resetCrafting() {
        this.isCrafting = false;
        this.progress = 0;
        this.cooldown = 1;
        this.setChanged();
        this.notifyClient();
    }

    private void notifyClient() {
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    private boolean checkOrConsumeIngredients(AssemblyRecipe recipe, boolean consume) {
        for (AssemblyRecipe.SizedIngredient req : recipe.getInputs()) {
            int needed = req.count();
            int found = 0;
            for (int i = 3; i < 9; ++i) {
                ItemStack stack = this.itemHandler.getStackInSlot(i);
                if (!req.ingredient().test(stack)) continue;
                int take = Math.min(stack.getCount(), needed - found);
                if (consume) {
                    this.itemHandler.extractItem(i, take, false);
                }
                if ((found += take) >= needed) break;
            }
            if (found >= needed) continue;
            return false;
        }
        return true;
    }

    private boolean canCraft() {
        AssemblyRecipe recipe = this.getActiveAssemblyRecipe();
        if (recipe != null) {
            if (this.checkOrConsumeIngredients(recipe, false)) {
                ItemStack result = recipe.getResultItem((HolderLookup.Provider)this.level.registryAccess());
                ItemStack currentOutput = this.itemHandler.getStackInSlot(9);
                if (currentOutput.isEmpty()) {
                    return true;
                }
                return ItemStack.isSameItem((ItemStack)currentOutput, (ItemStack)result) && currentOutput.getCount() + result.getCount() <= currentOutput.getMaxStackSize();
            }
            return false;
        }
        ItemStack inputStack = this.itemHandler.getStackInSlot(0);
        if (inputStack.isEmpty()) {
            return false;
        }
        Optional recipeOpt = this.level.getRecipeManager().getRecipeFor((RecipeType)ModRecipes.ENGINEERING_TYPE.get(), (RecipeInput)new SingleRecipeInput(inputStack), this.level);
        if (recipeOpt.isPresent()) {
            for (int i = 3; i < 9; ++i) {
                if (!this.itemHandler.getStackInSlot(i).isEmpty()) continue;
                return true;
            }
        }
        return false;
    }

    private ItemStack mergeIntoOutput(ItemStack stack) {
        ItemStack remainder = stack.copy();
        for (int i = 3; i < 9; ++i) {
            if (!(remainder = this.itemHandler.insertItem(i, remainder, false)).isEmpty()) continue;
            return ItemStack.EMPTY;
        }
        return remainder;
    }

    public float getRenderProgress(float pPartialTick) {
        return Mth.lerp((float)pPartialTick, (float)this.prevAnimationProgress, (float)this.animationProgress);
    }

    private void craftItem() {
        AssemblyRecipe recipe = this.getActiveAssemblyRecipe();
        if (recipe != null) {
            if (this.checkOrConsumeIngredients(recipe, true)) {
                ItemStack result = recipe.getResultItem((HolderLookup.Provider)this.level.registryAccess()).copy();
                ICyberware cw = CyberwareAPI.getCyberware(result);
                if (cw != null) {
                    cw.setPristine(result, true);
                }
                this.itemHandler.insertItem(9, result, false);
                if (((Boolean)CyberwareConfig.CONSUME_BLUEPRINT.get()).booleanValue()) {
                    this.itemHandler.extractItem(2, 1, false);
                }
            }
            return;
        }
        ItemStack inputStack = this.itemHandler.getStackInSlot(0);
        if (inputStack.isEmpty()) {
            return;
        }
        Optional recipeOpt = this.level.getRecipeManager().getRecipeFor((RecipeType)ModRecipes.ENGINEERING_TYPE.get(), (RecipeInput)new SingleRecipeInput(inputStack), this.level);
        if (recipeOpt.isPresent()) {
            ItemStack blueprint;
            EngineeringRecipe engRecipe = (EngineeringRecipe)((RecipeHolder)recipeOpt.get()).value();
            float baseChance = engRecipe.getBlueprintChance();
            CyberwareEvents.Salvage.Pre preEvent = new CyberwareEvents.Salvage.Pre(this, inputStack, baseChance);
            if (((CyberwareEvents.Salvage.Pre)NeoForge.EVENT_BUS.post((Event)preEvent)).isCanceled()) {
                return;
            }
            List<ItemStack> results = engRecipe.rollOutputs(this.level.random);
            CyberwareEvents.Salvage.Post postEvent = new CyberwareEvents.Salvage.Post(this, inputStack, results);
            NeoForge.EVENT_BUS.post((Event)postEvent);
            this.itemHandler.extractItem(0, 1, false);
            for (ItemStack result : postEvent.getOutputs()) {
                ItemStack remainder = this.mergeIntoOutput(result);
                if (remainder.isEmpty()) continue;
                Block.popResource((Level)this.level, (BlockPos)this.worldPosition.above(), (ItemStack)remainder);
            }
            ItemStack paperStack = this.itemHandler.getStackInSlot(1);
            if (paperStack.is(Items.PAPER) && this.level.random.nextFloat() < preEvent.getBlueprintChance() && this.mergeIntoOutput(blueprint = BlueprintItem.createBlueprintFor(inputStack.getItem())).isEmpty()) {
                this.itemHandler.extractItem(1, 1, false);
            }
        }
    }

    @Nullable
    private AssemblyRecipe getActiveAssemblyRecipe() {
        if (this.cachedRecipe != null) {
            return this.cachedRecipe;
        }
        if (this.level == null) {
            return null;
        }
        ItemStack blueprintStack = this.itemHandler.getStackInSlot(2);
        if (blueprintStack.isEmpty() || !(blueprintStack.getItem() instanceof BlueprintItem)) {
            return null;
        }
        Item targetItem = BlueprintItem.getTargetItem(blueprintStack);
        if (targetItem == null) {
            return null;
        }
        for (RecipeHolder holder : this.level.getRecipeManager().getAllRecipesFor((RecipeType)ModRecipes.ASSEMBLY_TYPE.get())) {
            if (((AssemblyRecipe)holder.value()).getResultItem((HolderLookup.Provider)this.level.registryAccess()).getItem() != targetItem) continue;
            this.cachedRecipe = (AssemblyRecipe)holder.value();
            return this.cachedRecipe;
        }
        return null;
    }

    private boolean isItemNeededForRecipe(AssemblyRecipe recipe, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        for (AssemblyRecipe.SizedIngredient input : recipe.getInputs()) {
            if (!input.ingredient().test(stack)) continue;
            return true;
        }
        return false;
    }

    @NotNull
    public Component getDisplayName() {
        return Component.translatable((String)"block.cyber_ware_port.cyberware_workbench");
    }

    @Nullable
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CyberwareWorkbenchMenu(pContainerId, pPlayerInventory, this);
    }

    public IItemHandler getItemHandler(@Nullable Direction side) {
        if (side == null) {
            return this.itemHandler;
        }
        Direction facing = (Direction)this.getBlockState().getValue((Property)CyberwareWorkbenchBlock.FACING);
        if (side == facing) {
            return this.sideFrontHandler;
        }
        if (side == facing.getClockWise()) {
            return this.sideInputHandler;
        }
        if (side == facing.getCounterClockWise()) {
            return this.sideOutputHandler;
        }
        if (side == Direction.DOWN || side == facing.getOpposite() || side == Direction.UP) {
            return this.sideIngredientHandler;
        }
        return this.sideIngredientHandler;
    }

    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.put("inventory", (Tag)this.itemHandler.serializeNBT(pRegistries));
        pTag.putInt("workbench.progress", this.progress);
        pTag.putBoolean("workbench.isCrafting", this.isCrafting);
        pTag.putInt("workbench.cooldown", this.cooldown);
    }

    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        if (pTag.contains("inventory")) {
            this.itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
        }
        this.progress = pTag.getInt("workbench.progress");
        this.isCrafting = pTag.getBoolean("workbench.isCrafting");
        this.cooldown = pTag.getInt("workbench.cooldown");
        this.cachedRecipe = null;
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);
        CompoundTag tag = pkt.getTag();
        if (tag != null) {
            this.loadAdditional(tag, lookupProvider);
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create((BlockEntity)this);
    }

    @NotNull
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag, pRegistries);
        return tag;
    }

    private class SidedProxyHandler
    implements IItemHandlerModifiable {
        private final ItemStackHandler internal;
        private final boolean canInsert;
        private final boolean canExtract;
        private final int[] allowedSlots;

        public SidedProxyHandler(ItemStackHandler internal, boolean canInsert, boolean canExtract, int ... slots) {
            this.internal = internal;
            this.canInsert = canInsert;
            this.canExtract = canExtract;
            this.allowedSlots = slots;
        }

        private boolean isSlotAllowed(int slot) {
            for (int s : this.allowedSlots) {
                if (s != slot) continue;
                return true;
            }
            return false;
        }

        public int getSlots() {
            return this.internal.getSlots();
        }

        @NotNull
        public ItemStack getStackInSlot(int slot) {
            return this.internal.getStackInSlot(slot);
        }

        public int getSlotLimit(int slot) {
            return this.internal.getSlotLimit(slot);
        }

        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return this.internal.isItemValid(slot, stack);
        }

        public void setStackInSlot(int slot, @NotNull ItemStack stack) {
            this.internal.setStackInSlot(slot, stack);
        }

        @NotNull
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            AssemblyRecipe recipe;
            if (!this.canInsert || !this.isSlotAllowed(slot)) {
                return stack;
            }
            if (slot == 0 && !CyberwareAPI.isCyberware(stack)) {
                return stack;
            }
            if (slot == 1 && !stack.is(Items.PAPER)) {
                return stack;
            }
            if (slot == 2 && !(stack.getItem() instanceof BlueprintItem)) {
                return stack;
            }
            if (!(slot < 3 || slot >= 9 || (recipe = CyberwareWorkbenchBlockEntity.this.getActiveAssemblyRecipe()) != null && CyberwareWorkbenchBlockEntity.this.isItemNeededForRecipe(recipe, stack))) {
                return stack;
            }
            return this.internal.insertItem(slot, stack, simulate);
        }

        @NotNull
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            ItemStack stackInSlot;
            AssemblyRecipe recipe;
            if (!this.canExtract || !this.isSlotAllowed(slot)) {
                return ItemStack.EMPTY;
            }
            if (slot >= 3 && slot <= 8 && (recipe = CyberwareWorkbenchBlockEntity.this.getActiveAssemblyRecipe()) != null && CyberwareWorkbenchBlockEntity.this.isItemNeededForRecipe(recipe, stackInSlot = this.internal.getStackInSlot(slot))) {
                return ItemStack.EMPTY;
            }
            return this.internal.extractItem(slot, amount, simulate);
        }
    }
}

