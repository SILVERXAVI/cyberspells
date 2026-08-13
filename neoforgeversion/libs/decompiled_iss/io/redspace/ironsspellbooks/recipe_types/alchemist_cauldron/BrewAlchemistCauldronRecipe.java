/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  net.minecraft.advancements.Criterion
 *  net.minecraft.core.Holder
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.data.recipes.RecipeBuilder
 *  net.minecraft.data.recipes.RecipeOutput
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.Recipe
 *  net.minecraft.world.item.crafting.RecipeInput
 *  net.minecraft.world.item.crafting.RecipeSerializer
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.material.Fluid
 *  net.neoforged.neoforge.fluids.FluidStack
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.recipe_types.alchemist_cauldron;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.ironsspellbooks.registries.RecipeRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public record BrewAlchemistCauldronRecipe(FluidStack fluidIn, Ingredient reagent, List<FluidStack> results, Optional<ItemStack> byproduct) implements Recipe<Input>
{
    private final FluidStack fluidIn;
    private final List<FluidStack> results;
    private final Optional<ItemStack> byproduct;

    public static Builder builder() {
        return new Builder();
    }

    public FluidStack fluidIn() {
        return this.fluidIn.copy();
    }

    public List<FluidStack> results() {
        return List.copyOf(this.results);
    }

    public Optional<ItemStack> byproduct() {
        return this.byproduct.map(ItemStack::copy);
    }

    public boolean matches(Input input, Level level) {
        return FluidStack.isSameFluidSameComponents((FluidStack)this.fluidIn, (FluidStack)input.fluidIn()) && this.reagent.test(input.reagent());
    }

    public ItemStack assemble(Input input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY.copy();
    }

    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY.copy();
    }

    public RecipeSerializer<?> getSerializer() {
        return (RecipeSerializer)RecipeRegistry.ALCHEMIST_CAULDRON_BREW_SERIALIZER.get();
    }

    public RecipeType<?> getType() {
        return (RecipeType)RecipeRegistry.ALCHEMIST_CAULDRON_BREW_TYPE.get();
    }

    public static class Builder
    implements RecipeBuilder {
        FluidStack input = null;
        Ingredient reagent = null;
        List<FluidStack> results = new ArrayList<FluidStack>();
        ItemStack byproduct = null;

        public Builder withInput(Holder<Fluid> fluid, int amount) {
            return this.withInput(new FluidStack(fluid, amount));
        }

        public Builder withInput(FluidStack fluidStack) {
            this.input = fluidStack;
            return this;
        }

        public Builder withReagent(Item item) {
            this.reagent = Ingredient.of((ItemLike[])new ItemLike[]{item});
            return this;
        }

        public Builder withReagent(ItemStack item) {
            this.reagent = Ingredient.of((ItemStack[])new ItemStack[]{item});
            return this;
        }

        public Builder withReagent(TagKey<Item> item) {
            this.reagent = Ingredient.of(item);
            return this;
        }

        public Builder withResult(FluidStack fluidStack) {
            this.results.add(fluidStack);
            return this;
        }

        public Builder withResult(Holder<Fluid> fluid, int amount) {
            return this.withResult(new FluidStack(fluid, amount));
        }

        public Builder withByproduct(ItemStack item) {
            this.byproduct = item;
            return this;
        }

        public Builder withByproduct(Holder<Item> item) {
            return this.withByproduct(new ItemStack(item));
        }

        public Builder withByproduct(Item item) {
            return this.withByproduct(new ItemStack((ItemLike)item));
        }

        public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
            return this;
        }

        public RecipeBuilder group(@Nullable String groupName) {
            return this;
        }

        public Item getResult() {
            return Items.AIR;
        }

        public void save(RecipeOutput recipeOutput, ResourceLocation id) {
            Objects.requireNonNull(this.input);
            Objects.requireNonNull(this.reagent);
            recipeOutput.accept(id, (Recipe)new BrewAlchemistCauldronRecipe(this.input, this.reagent, this.results, Optional.ofNullable(this.byproduct)), null);
        }

        public void save(RecipeOutput recipeOutput) {
            this.save(recipeOutput, BuiltInRegistries.FLUID.getKey((Object)((FluidStack)this.results.getFirst()).getFluid()).withPrefix("alchemist_cauldron/brew_"));
        }

        public void saveSoak(RecipeOutput recipeOutput) {
            this.save(recipeOutput, BuiltInRegistries.ITEM.getKey((Object)this.byproduct.getItem()).withPrefix("alchemist_cauldron/soak_"));
        }
    }

    public record Input(FluidStack fluidIn, ItemStack reagent) implements RecipeInput
    {
        public ItemStack getItem(int index) {
            return this.reagent;
        }

        public int size() {
            return 1;
        }
    }

    public static class Serializer
    implements RecipeSerializer<BrewAlchemistCauldronRecipe> {
        public static final MapCodec<BrewAlchemistCauldronRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group((App)FluidStack.CODEC.fieldOf("base_fluid").forGetter(BrewAlchemistCauldronRecipe::fluidIn), (App)Ingredient.CODEC.fieldOf("input").forGetter(BrewAlchemistCauldronRecipe::reagent), (App)Codec.list((Codec)FluidStack.CODEC).fieldOf("results").forGetter(BrewAlchemistCauldronRecipe::results), (App)ItemStack.SINGLE_ITEM_CODEC.optionalFieldOf("byproduct").forGetter(BrewAlchemistCauldronRecipe::byproduct)).apply((Applicative)builder, BrewAlchemistCauldronRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, BrewAlchemistCauldronRecipe> STREAM_CODEC = StreamCodec.composite((StreamCodec)FluidStack.STREAM_CODEC, BrewAlchemistCauldronRecipe::fluidIn, (StreamCodec)Ingredient.CONTENTS_STREAM_CODEC, BrewAlchemistCauldronRecipe::reagent, (StreamCodec)ByteBufCodecs.fromCodec((Codec)Codec.list((Codec)FluidStack.CODEC)), BrewAlchemistCauldronRecipe::results, (StreamCodec)ByteBufCodecs.optional((StreamCodec)ItemStack.STREAM_CODEC), BrewAlchemistCauldronRecipe::byproduct, BrewAlchemistCauldronRecipe::new);

        public MapCodec<BrewAlchemistCauldronRecipe> codec() {
            return CODEC;
        }

        public StreamCodec<RegistryFriendlyByteBuf, BrewAlchemistCauldronRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}

