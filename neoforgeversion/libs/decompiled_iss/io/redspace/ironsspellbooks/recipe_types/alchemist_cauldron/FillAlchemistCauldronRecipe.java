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
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.data.recipes.RecipeBuilder
 *  net.minecraft.data.recipes.RecipeOutput
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.Recipe
 *  net.minecraft.world.item.crafting.RecipeSerializer
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.item.crafting.SingleRecipeInput
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
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.registries.RecipeRegistry;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public record FillAlchemistCauldronRecipe(Ingredient input, ItemStack returned, FluidStack result, boolean mustFitAll, Holder<SoundEvent> fillSound) implements Recipe<SingleRecipeInput>
{
    private final ItemStack returned;
    private final FluidStack result;

    public FluidStack result() {
        return this.result.copy();
    }

    public ItemStack returned() {
        return this.returned.copy();
    }

    public boolean matches(SingleRecipeInput input, Level level) {
        return this.input.test(input.item());
    }

    public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
        return this.returned.copy();
    }

    public boolean isSpecial() {
        return true;
    }

    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.returned.copy();
    }

    public RecipeSerializer<?> getSerializer() {
        return (RecipeSerializer)RecipeRegistry.ALCHEMIST_CAULDRON_FILL_SERIALIZER.get();
    }

    public RecipeType<?> getType() {
        return (RecipeType)RecipeRegistry.ALCHEMIST_CAULDRON_FILL_TYPE.get();
    }

    public static class Builder
    implements RecipeBuilder {
        SoundEvent soundEvent = SoundEvents.BOTTLE_EMPTY;
        Ingredient input = null;
        ItemStack returned = null;
        FluidStack fluid = null;
        boolean mustFitAll = true;

        public Builder withInput(Item input) {
            this.input = Ingredient.of((ItemLike[])new ItemLike[]{input});
            return this;
        }

        public Builder withInput(Ingredient input) {
            this.input = input;
            return this;
        }

        public Builder withReturnItem(Item returned) {
            this.returned = new ItemStack((ItemLike)returned);
            return this;
        }

        public Builder withFluid(Holder<Fluid> fluid, int amount) {
            return this.withFluid(new FluidStack(fluid, amount));
        }

        public Builder withSound(SoundEvent soundEvent) {
            this.soundEvent = soundEvent;
            return this;
        }

        public Builder withFluid(FluidStack fluidStack) {
            this.fluid = fluidStack;
            return this;
        }

        public Builder mustFitAll(boolean mustFitAll) {
            this.mustFitAll = mustFitAll;
            return this;
        }

        public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
            return this;
        }

        public RecipeBuilder group(@Nullable String groupName) {
            return this;
        }

        public Item getResult() {
            return this.returned.getItem();
        }

        public void save(RecipeOutput recipeOutput) {
            this.save(recipeOutput, IronsSpellbooks.id("alchemist_cauldron/fill_" + BuiltInRegistries.ITEM.getKey((Object)this.input.getItems()[0].getItem()).getPath()));
        }

        public void save(RecipeOutput recipeOutput, ResourceLocation id) {
            recipeOutput.accept(id, (Recipe)new FillAlchemistCauldronRecipe(this.input, this.returned, this.fluid, this.mustFitAll, (Holder<SoundEvent>)BuiltInRegistries.SOUND_EVENT.wrapAsHolder((Object)this.soundEvent)), null);
        }
    }

    public static class Serializer
    implements RecipeSerializer<FillAlchemistCauldronRecipe> {
        public static final MapCodec<FillAlchemistCauldronRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group((App)Ingredient.CODEC.fieldOf("input").forGetter(FillAlchemistCauldronRecipe::input), (App)ItemStack.CODEC.fieldOf("result").forGetter(FillAlchemistCauldronRecipe::returned), (App)FluidStack.CODEC.fieldOf("fluid").forGetter(FillAlchemistCauldronRecipe::result), (App)Codec.BOOL.optionalFieldOf("mustFitAll", (Object)true).forGetter(FillAlchemistCauldronRecipe::mustFitAll), (App)BuiltInRegistries.SOUND_EVENT.holderByNameCodec().optionalFieldOf("sound", (Object)BuiltInRegistries.SOUND_EVENT.wrapAsHolder((Object)SoundEvents.BOTTLE_EMPTY)).forGetter(FillAlchemistCauldronRecipe::fillSound)).apply((Applicative)builder, FillAlchemistCauldronRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, FillAlchemistCauldronRecipe> STREAM_CODEC = StreamCodec.composite((StreamCodec)Ingredient.CONTENTS_STREAM_CODEC, FillAlchemistCauldronRecipe::input, (StreamCodec)ItemStack.STREAM_CODEC, FillAlchemistCauldronRecipe::returned, (StreamCodec)FluidStack.STREAM_CODEC, FillAlchemistCauldronRecipe::result, (StreamCodec)ByteBufCodecs.BOOL, FillAlchemistCauldronRecipe::mustFitAll, (StreamCodec)ByteBufCodecs.holderRegistry((ResourceKey)Registries.SOUND_EVENT), FillAlchemistCauldronRecipe::fillSound, FillAlchemistCauldronRecipe::new);

        public MapCodec<FillAlchemistCauldronRecipe> codec() {
            return CODEC;
        }

        public StreamCodec<RegistryFriendlyByteBuf, FillAlchemistCauldronRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}

