/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Holder
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.NonNullList
 *  net.minecraft.core.Vec3i
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.Connection
 *  net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.tags.TagKey
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.Container
 *  net.minecraft.world.ContainerHelper
 *  net.minecraft.world.Containers
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.ItemInteractionResult
 *  net.minecraft.world.SimpleContainer
 *  net.minecraft.world.WorldlyContainer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.ItemUtils
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.alchemy.PotionContents
 *  net.minecraft.world.item.alchemy.Potions
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.RecipeHolder
 *  net.minecraft.world.item.crafting.RecipeInput
 *  net.minecraft.world.item.crafting.RecipeManager
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.item.crafting.SingleRecipeInput
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.material.Fluid
 *  net.minecraft.world.level.material.Fluids
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.common.Tags$Fluids
 *  net.neoforged.neoforge.fluids.FluidStack
 *  net.neoforged.neoforge.fluids.IFluidTank
 *  net.neoforged.neoforge.fluids.capability.IFluidHandler
 *  net.neoforged.neoforge.fluids.capability.IFluidHandler$FluidAction
 *  net.neoforged.neoforge.fluids.capability.templates.FluidTank
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.block.alchemist_cauldron;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.fluids.PotionFluid;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.recipe_types.alchemist_cauldron.BrewAlchemistCauldronRecipe;
import io.redspace.ironsspellbooks.recipe_types.alchemist_cauldron.EmptyAlchemistCauldronRecipe;
import io.redspace.ironsspellbooks.recipe_types.alchemist_cauldron.FillAlchemistCauldronRecipe;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.RecipeRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

public class AlchemistCauldronTile
extends BlockEntity
implements WorldlyContainer {
    public static int INPUT_SIZE = 4;
    public final NonNullList<ItemStack> inputItems = NonNullList.withSize((int)INPUT_SIZE, (Object)ItemStack.EMPTY);
    private final int[] cooktimes = new int[INPUT_SIZE];
    boolean capDirty = false;
    public IFluidHandler fluidCapability;
    public AlchemistCauldronFluidHandler fluidInventory = new AlchemistCauldronFluidHandler();

    public void refreshCapabilities() {
        this.fluidCapability = this.fluidInventory;
        this.invalidateCapabilities();
        this.capDirty = false;
    }

    public AlchemistCauldronTile(BlockPos pWorldPosition, BlockState pBlockState) {
        super((BlockEntityType)BlockRegistry.ALCHEMIST_CAULDRON_TILE.get(), pWorldPosition, pBlockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState blockState, AlchemistCauldronTile cauldronTile) {
        if (cauldronTile.capDirty) {
            cauldronTile.refreshCapabilities();
        }
        for (int i = 0; i < cauldronTile.inputItems.size(); ++i) {
            ItemStack itemStack = (ItemStack)cauldronTile.inputItems.get(i);
            if (itemStack.isEmpty() || !cauldronTile.isBoiling(blockState)) {
                cauldronTile.cooktimes[i] = 0;
            } else {
                int n = i;
                cauldronTile.cooktimes[n] = cauldronTile.cooktimes[n] + 1;
            }
            if (cauldronTile.cooktimes[i] <= 100) continue;
            cauldronTile.tryMeltInput(itemStack);
            cauldronTile.cooktimes[i] = 0;
        }
        RandomSource random = Utils.random;
        if (cauldronTile.isBoiling(blockState)) {
            float waterLevel = Mth.lerp((float)((float)cauldronTile.getFluidAmount() / 1000.0f), (float)0.25f, (float)0.9f);
            MagicManager.spawnParticles(level, (ParticleOptions)ParticleTypes.BUBBLE_POP, (float)pos.getX() + Mth.randomBetween((RandomSource)random, (float)0.2f, (float)0.8f), (float)pos.getY() + waterLevel, (float)pos.getZ() + Mth.randomBetween((RandomSource)random, (float)0.2f, (float)0.8f), 1, 0.0, 0.0, 0.0, 0.0, false);
        }
    }

    public ItemStack tryExecuteRecipeInteractions(Level level, ItemStack itemStack) {
        ItemStack potionStack;
        SingleRecipeInput fillRecipeInput = new SingleRecipeInput(itemStack);
        RecipeManager recipeManager = level.getRecipeManager();
        Optional<FillAlchemistCauldronRecipe> fillRecipe = recipeManager.getRecipeFor((RecipeType)RecipeRegistry.ALCHEMIST_CAULDRON_FILL_TYPE.get(), (RecipeInput)fillRecipeInput, level).map(RecipeHolder::value);
        if (fillRecipe.isEmpty() && itemStack.has(DataComponents.POTION_CONTENTS)) {
            FluidStack fluid = ((PotionContents)itemStack.get(DataComponents.POTION_CONTENTS)).is(Potions.WATER) ? new FluidStack((Fluid)Fluids.WATER, 250) : PotionFluid.from(itemStack);
            fillRecipe = Optional.of(new FillAlchemistCauldronRecipe(Ingredient.of((ItemStack[])new ItemStack[]{itemStack}), new ItemStack((ItemLike)Items.GLASS_BOTTLE), fluid, true, (Holder<SoundEvent>)BuiltInRegistries.SOUND_EVENT.wrapAsHolder((Object)SoundEvents.BOTTLE_EMPTY)));
        }
        if (fillRecipe.isPresent()) {
            FillAlchemistCauldronRecipe recipe = fillRecipe.get();
            int amountThatCanFit = this.fluidInventory.fill(recipe.result(), IFluidHandler.FluidAction.SIMULATE);
            if (!(recipe.mustFitAll() && amountThatCanFit != recipe.result().getAmount() || amountThatCanFit == 0)) {
                this.fluidInventory.fill(recipe.result(), IFluidHandler.FluidAction.EXECUTE);
                this.setChanged();
                level.playSound(null, this.getBlockPos(), (SoundEvent)recipe.fillSound().value(), SoundSource.BLOCKS);
                return recipe.assemble(fillRecipeInput, (HolderLookup.Provider)level.registryAccess());
            }
        }
        FluidStack topFluid = this.fluidInventory.drain(1000, IFluidHandler.FluidAction.SIMULATE);
        EmptyAlchemistCauldronRecipe.Input emptyRecipeInput = new EmptyAlchemistCauldronRecipe.Input(itemStack, topFluid);
        Optional<EmptyAlchemistCauldronRecipe> emptyRecipe = recipeManager.getRecipeFor((RecipeType)RecipeRegistry.ALCHEMIST_CAULDRON_EMPTY_TYPE.get(), (RecipeInput)emptyRecipeInput, level).map(RecipeHolder::value);
        if (emptyRecipe.isEmpty() && itemStack.is(Items.GLASS_BOTTLE) && !(potionStack = PotionFluid.from(topFluid)).isEmpty()) {
            emptyRecipe = Optional.of(new EmptyAlchemistCauldronRecipe(Ingredient.EMPTY, potionStack, topFluid.copyWithAmount(250), (Holder<SoundEvent>)BuiltInRegistries.SOUND_EVENT.wrapAsHolder((Object)SoundEvents.BOTTLE_FILL)));
        }
        if (emptyRecipe.isPresent()) {
            EmptyAlchemistCauldronRecipe recipe = emptyRecipe.get();
            this.fluidInventory.drain(recipe.fluid(), IFluidHandler.FluidAction.EXECUTE);
            level.playSound(null, this.getBlockPos(), (SoundEvent)recipe.emptySound().value(), SoundSource.BLOCKS);
            this.setChanged();
            return recipe.assemble(emptyRecipeInput, (HolderLookup.Provider)level.registryAccess());
        }
        return ItemStack.EMPTY;
    }

    public ItemInteractionResult handleUse(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        ItemStack recipeResult = this.tryExecuteRecipeInteractions(level, itemStack);
        if (!recipeResult.isEmpty()) {
            player.setItemInHand(hand, ItemUtils.createFilledResult((ItemStack)player.getItemInHand(hand), (Player)player, (ItemStack)recipeResult));
            return ItemInteractionResult.SUCCESS;
        }
        if (this.isValidInput(itemStack)) {
            if (!level.isClientSide) {
                for (int i = 0; i < this.inputItems.size(); ++i) {
                    ItemStack stack = (ItemStack)this.inputItems.get(i);
                    if (!stack.isEmpty()) continue;
                    ItemStack input = player.getAbilities().instabuild ? itemStack.copy() : itemStack.split(1);
                    input.setCount(1);
                    this.inputItems.set(i, (Object)input);
                    player.setItemInHand(hand, itemStack);
                    this.setChanged();
                    break;
                }
            }
            return ItemInteractionResult.SUCCESS;
        }
        if ((itemStack.isEmpty() || player.isCrouching()) && hand.equals((Object)InteractionHand.MAIN_HAND)) {
            for (ItemStack item : this.inputItems) {
                if (item.isEmpty()) continue;
                if (!level.isClientSide) {
                    ItemStack take = item.split(1);
                    if (player.getItemInHand(hand).isEmpty()) {
                        player.setItemInHand(hand, take);
                    } else if (!player.getInventory().add(take)) {
                        player.drop(take, false);
                    }
                    this.setChanged();
                }
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.CONSUME;
    }

    public void tryMeltInput(ItemStack itemStack) {
        BrewAlchemistCauldronRecipe.Input input;
        Level level;
        if (this.level == null || !((level = this.level) instanceof ServerLevel)) {
            return;
        }
        ServerLevel serverLevel = (ServerLevel)level;
        boolean shouldMelt = false;
        boolean success = true;
        Optional<Object> byproduct = Optional.empty();
        if (itemStack.is((Item)ItemRegistry.SCROLL.get()) && this.fluidInventory.contains((TagKey<Fluid>)Tags.Fluids.WATER, 250)) {
            if ((double)Utils.random.nextFloat() < (Double)ServerConfigs.SCROLL_RECYCLE_CHANCE.get()) {
                this.fluidInventory.drain(new FluidStack((Fluid)Fluids.WATER, 250), IFluidHandler.FluidAction.EXECUTE);
                this.fluidInventory.fill(new FluidStack(AlchemistCauldronTile.getInkFromScroll(itemStack).fluid(), 250), IFluidHandler.FluidAction.EXECUTE);
            } else {
                success = false;
            }
            shouldMelt = true;
        }
        if (!shouldMelt) {
            for (FluidStack fluid : this.fluidInventory.fluids()) {
                BrewAlchemistCauldronRecipe recipe;
                int totalNewFluid;
                input = new BrewAlchemistCauldronRecipe.Input(fluid, itemStack);
                Optional<BrewAlchemistCauldronRecipe> brewRecipeOpt = serverLevel.getRecipeManager().getRecipeFor((RecipeType)RecipeRegistry.ALCHEMIST_CAULDRON_BREW_TYPE.get(), (RecipeInput)input, (Level)serverLevel).map(RecipeHolder::value);
                if (!brewRecipeOpt.isPresent() || !this.fluidInventory.canFit((totalNewFluid = (recipe = brewRecipeOpt.get()).results().stream().mapToInt(FluidStack::getAmount).sum()) - recipe.fluidIn().getAmount()) || !this.fluidInventory.contains(recipe.fluidIn(), recipe.fluidIn().getAmount())) continue;
                shouldMelt = true;
                this.fluidInventory.drain(recipe.fluidIn(), IFluidHandler.FluidAction.EXECUTE);
                recipe.results().forEach(result -> this.fluidInventory.fill((FluidStack)result, IFluidHandler.FluidAction.EXECUTE));
                byproduct = recipe.byproduct();
            }
        }
        if (!shouldMelt && this.isBrewable(itemStack)) {
            for (FluidStack fluid : this.fluidInventory.fluids()) {
                ItemStack potionGhostStack = PotionFluid.from(fluid);
                if (potionGhostStack.isEmpty() || !serverLevel.potionBrewing().hasPotionMix(potionGhostStack, itemStack) && !this.level.potionBrewing().hasContainerMix(potionGhostStack, itemStack)) continue;
                ItemStack potionResult = serverLevel.potionBrewing().mix(itemStack, potionGhostStack);
                FluidStack fluidResult = PotionFluid.from(potionResult).copyWithAmount(fluid.getAmount());
                this.fluidInventory.drain(fluid, IFluidHandler.FluidAction.EXECUTE);
                this.fluidInventory.fill(fluidResult, IFluidHandler.FluidAction.EXECUTE);
                shouldMelt = true;
            }
        }
        if (shouldMelt) {
            itemStack.shrink(1);
            if (byproduct.isPresent()) {
                for (int i = 0; i < this.inputItems.size(); ++i) {
                    ItemStack stack = (ItemStack)this.inputItems.get(i);
                    if (!stack.isEmpty()) continue;
                    input = ((ItemStack)byproduct.get()).split(1);
                    this.inputItems.set(i, (Object)input);
                    break;
                }
                Vec3 pos = Vec3.upFromBottomCenterOf((Vec3i)this.getBlockPos(), (double)1.0);
                Containers.dropItemStack((Level)this.level, (double)pos.x, (double)pos.y, (double)pos.z, (ItemStack)((ItemStack)byproduct.get()).split(1));
            }
            this.setChanged();
            if (success) {
                this.level.playSound(null, this.getBlockPos(), SoundEvents.BREWING_STAND_BREW, SoundSource.MASTER, 1.0f, 1.0f);
                this.level.markAndNotifyBlock(this.getBlockPos(), this.level.getChunkAt(this.getBlockPos()), this.getBlockState(), this.getBlockState(), 1, 1);
            } else {
                this.level.playSound(null, this.getBlockPos(), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.MASTER, 1.0f, 1.0f);
            }
        }
    }

    public boolean isValidInput(ItemStack itemStack) {
        return itemStack.is((Item)ItemRegistry.SCROLL.get()) || this.isBrewable(itemStack) || this.level != null && this.level.getRecipeManager().getAllRecipesFor((RecipeType)RecipeRegistry.ALCHEMIST_CAULDRON_BREW_TYPE.get()).stream().anyMatch(holder -> ((BrewAlchemistCauldronRecipe)holder.value()).reagent().test(itemStack));
    }

    public boolean isBrewable(ItemStack itemStack) {
        return (Boolean)ServerConfigs.ALLOW_CAULDRON_BREWING.get() != false && this.level != null && this.level.potionBrewing().isIngredient(itemStack);
    }

    public static InkItem getInkFromScroll(ItemStack scrollStack) {
        ISpellContainer spellContainer = ISpellContainer.get(scrollStack);
        SpellData spellData = spellContainer.getSpellAtIndex(0);
        SpellRarity rarity = spellData.getSpell().getRarity(spellData.getLevel());
        return InkItem.getInkForRarity(rarity);
    }

    public void setChanged() {
        super.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 2);
        }
    }

    public boolean stillValid(Player pPlayer) {
        return false;
    }

    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registryAccess) {
        Utils.loadAllItems(tag, this.inputItems, "Items", registryAccess);
        this.fluidInventory.load("Results", tag, registryAccess);
        super.loadAdditional(tag, registryAccess);
    }

    protected void saveAdditional(@Nonnull CompoundTag tag, HolderLookup.Provider registryAccess) {
        Utils.saveAllItems(tag, this.inputItems, "Items", registryAccess);
        this.fluidInventory.save("Results", tag, registryAccess);
        super.saveAdditional(tag, registryAccess);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        ClientboundBlockEntityDataPacket packet = ClientboundBlockEntityDataPacket.create((BlockEntity)this);
        return packet;
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        this.handleUpdateTag(pkt.getTag(), lookupProvider);
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag, pRegistries);
        return tag;
    }

    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        this.inputItems.clear();
        this.fluidInventory.clear();
        if (tag != null) {
            this.loadAdditional(tag, lookupProvider);
        }
    }

    public void drops() {
        SimpleContainer simpleContainer = new SimpleContainer(this.inputItems.size());
        for (int i = 0; i < this.inputItems.size(); ++i) {
            simpleContainer.setItem(i, (ItemStack)this.inputItems.get(i));
        }
        if (this.level != null) {
            Containers.dropContents((Level)this.level, (BlockPos)this.worldPosition, (Container)simpleContainer);
        }
    }

    public int[] getSlotsForFace(Direction pSide) {
        return new int[]{0, 1, 2, 3};
    }

    public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
        return pDirection != Direction.DOWN && this.isValidInput(pItemStack) && this.getItem(pIndex).isEmpty();
    }

    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
        return pDirection == Direction.DOWN;
    }

    public void clearContent() {
        this.inputItems.clear();
        this.fluidInventory.clear();
    }

    public int getContainerSize() {
        return INPUT_SIZE;
    }

    public boolean isEmpty() {
        return this.inputItems.stream().allMatch(ItemStack::isEmpty);
    }

    public ItemStack getItem(int pSlot) {
        return pSlot >= 0 && pSlot <= this.inputItems.size() ? (ItemStack)this.inputItems.get(pSlot) : ItemStack.EMPTY;
    }

    public ItemStack removeItem(int pSlot, int pAmount) {
        return ContainerHelper.removeItem(this.inputItems, (int)pSlot, (int)pAmount);
    }

    public ItemStack removeItemNoUpdate(int pSlot) {
        return pSlot >= 0 && pSlot <= this.inputItems.size() ? (ItemStack)this.inputItems.remove(pSlot) : ItemStack.EMPTY;
    }

    public void setItem(int pSlot, ItemStack pStack) {
        if (pSlot >= 0 && pSlot <= this.inputItems.size()) {
            this.inputItems.set(pSlot, (Object)pStack);
        }
    }

    public boolean isBoiling(BlockState blockState) {
        return this.getFluidAmount() >= 1;
    }

    public int getFluidAmount() {
        return this.fluidInventory.fluidAmount();
    }

    public class AlchemistCauldronFluidHandler
    implements IFluidHandler {
        IFluidTank[] tanks = new IFluidTank[]{new CallbackFluidTank(1000), new CallbackFluidTank(1000), new CallbackFluidTank(1000), new CallbackFluidTank(1000)};

        public int getTanks() {
            return this.tanks.length;
        }

        public FluidStack getFluidInTank(int tank) {
            return tank < 0 || tank > this.tanks.length ? FluidStack.EMPTY : (this.tanks[tank].getFluidAmount() == 0 ? FluidStack.EMPTY : this.tanks[tank].getFluid());
        }

        public int getTankCapacity(int tank) {
            return 1000;
        }

        public int fluidAmount() {
            return this.fluids().stream().mapToInt(FluidStack::getAmount).sum();
        }

        public boolean canFit(int fluidAmount) {
            return fluidAmount + this.fluidAmount() <= 1000;
        }

        public boolean isFluidValid(int tank, FluidStack stack) {
            return tank >= 0 && tank <= this.tanks.length && this.tanks[tank].isFluidValid(stack);
        }

        public boolean isTankCompatible(IFluidTank tank, FluidStack stack) {
            return tank.isFluidValid(stack) && FluidStack.isSameFluidSameComponents((FluidStack)tank.getFluid(), (FluidStack)stack);
        }

        public void onContentsChanged() {
            AlchemistCauldronTile.this.setChanged();
        }

        public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
            if (resource.is(ModTags.CAULDRON_FLUID_DISALLOW)) {
                return 0;
            }
            int resourceLocation = -1;
            int emptyLocation = -1;
            int remainingCapacity = 1000 - this.fluidAmount();
            if (remainingCapacity == 0) {
                return 0;
            }
            for (int i = 0; i < this.tanks.length; ++i) {
                if (this.isTankCompatible(this.tanks[i], resource)) {
                    resourceLocation = i;
                    break;
                }
                if (emptyLocation != -1 || !this.tanks[i].getFluid().isEmpty()) continue;
                emptyLocation = i;
            }
            FluidStack copy = resource.copyWithAmount(Math.min(remainingCapacity, resource.getAmount()));
            if (resourceLocation >= 0) {
                return this.tanks[resourceLocation].fill(copy, action);
            }
            if (emptyLocation >= 0) {
                return this.tanks[emptyLocation].fill(copy, action);
            }
            return 0;
        }

        public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
            for (int i = 0; i < this.tanks.length; ++i) {
                IFluidTank tank = this.tanks[i];
                if (!this.isTankCompatible(tank, resource)) continue;
                FluidStack result = tank.drain(resource, action);
                for (int j = i; j < this.tanks.length - 1; ++j) {
                    for (int k = j + 1; k < this.tanks.length && this.tanks[j].getFluid().isEmpty() && !this.tanks[k].getFluid().isEmpty(); ++k) {
                        IFluidTank tmp = this.tanks[j];
                        this.tanks[j] = this.tanks[k];
                        this.tanks[k] = tmp;
                    }
                }
                return result;
            }
            return FluidStack.EMPTY;
        }

        public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
            for (int i = this.tanks.length - 1; i >= 0; --i) {
                IFluidTank tank = this.tanks[i];
                if (tank.getFluid().isEmpty()) continue;
                return tank.drain(maxDrain, action);
            }
            return FluidStack.EMPTY;
        }

        public boolean contains(FluidStack stack, int minAmount) {
            for (IFluidTank tank : this.tanks) {
                if (!this.isTankCompatible(tank, stack)) continue;
                return tank.getFluidAmount() >= minAmount;
            }
            return false;
        }

        public boolean contains(Holder<Fluid> fluid, int minAmount) {
            for (IFluidTank tank : this.tanks) {
                if (!tank.getFluid().is(fluid)) continue;
                return tank.getFluidAmount() >= minAmount;
            }
            return false;
        }

        public boolean contains(TagKey<Fluid> fluid, int minAmount) {
            for (IFluidTank tank : this.tanks) {
                if (!tank.getFluid().is(fluid)) continue;
                return tank.getFluidAmount() >= minAmount;
            }
            return false;
        }

        public List<FluidStack> fluids() {
            return Arrays.stream(this.tanks).map(IFluidTank::getFluid).filter(f -> !f.isEmpty()).toList();
        }

        public void clear() {
            for (IFluidTank tank : this.tanks) {
                tank.drain(tank.getCapacity(), IFluidHandler.FluidAction.EXECUTE);
            }
        }

        public void save(String name, CompoundTag tag, HolderLookup.Provider access) {
            ListTag fluids = new ListTag();
            for (IFluidTank tank : this.tanks) {
                if (tank.getFluid().isEmpty()) continue;
                fluids.add((Object)tank.getFluid().save(access));
            }
            tag.put(name, (Tag)fluids);
        }

        public void load(String name, CompoundTag tag, HolderLookup.Provider access) {
            if (tag.contains(name, 9)) {
                ListTag fluids = tag.getList(name, 10);
                int i = 0;
                try {
                    for (Tag l : fluids) {
                        FluidStack stack = FluidStack.parseOptional((HolderLookup.Provider)access, (CompoundTag)((CompoundTag)l));
                        this.tanks[i++].fill(stack, IFluidHandler.FluidAction.EXECUTE);
                    }
                }
                catch (Exception e) {
                    IronsSpellbooks.LOGGER.error("Alchemist Cauldron Handler Failed to load fluid, skipping: {}", (Object)e.getMessage());
                }
            }
        }

        public class CallbackFluidTank
        extends FluidTank {
            public CallbackFluidTank(int capacity) {
                super(capacity);
            }

            protected void onContentsChanged() {
                super.onContentsChanged();
                AlchemistCauldronFluidHandler.this.onContentsChanged();
            }
        }
    }
}

