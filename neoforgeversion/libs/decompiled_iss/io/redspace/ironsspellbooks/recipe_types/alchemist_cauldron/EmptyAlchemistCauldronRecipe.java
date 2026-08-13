/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
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
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public record EmptyAlchemistCauldronRecipe(Ingredient input, ItemStack result, FluidStack fluid, Holder<SoundEvent> emptySound) implements Recipe<Input>
{
    private final ItemStack result;
    private final FluidStack fluid;

    public ItemStack result() {
        return this.result.copy();
    }

    public FluidStack fluid() {
        return this.fluid.copy();
    }

    public boolean matches(Input input, Level level) {
        return this.input.test(input.item()) && input.fluid.getAmount() >= this.fluid.getAmount() && FluidStack.isSameFluidSameComponents((FluidStack)this.fluid, (FluidStack)input.fluid);
    }

    public ItemStack assemble(Input input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    public boolean isSpecial() {
        return true;
    }

    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result.copy();
    }

    public RecipeSerializer<?> getSerializer() {
        return (RecipeSerializer)RecipeRegistry.ALCHEMIST_CAULDRON_EMPTY_SERIALIZER.get();
    }

    public RecipeType<?> getType() {
        return (RecipeType)RecipeRegistry.ALCHEMIST_CAULDRON_EMPTY_TYPE.get();
    }

    public record Input(ItemStack item, FluidStack fluid) implements RecipeInput
    {
        public ItemStack getItem(int index) {
            return this.item;
        }

        public int size() {
            return 1;
        }
    }

    public static class Builder
    implements RecipeBuilder {
        SoundEvent soundEvent = SoundEvents.BOTTLE_FILL;
        Ingredient input = null;
        ItemStack returned = null;
        FluidStack fluid = null;

        public Builder withInput(Item input) {
            this.input = Ingredient.of((ItemLike[])new ItemLike[]{input});
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
            recipeOutput.accept(IronsSpellbooks.id("alchemist_cauldron/empty_" + BuiltInRegistries.ITEM.getKey((Object)this.returned.getItem()).getPath()), (Recipe)new EmptyAlchemistCauldronRecipe(this.input, this.returned, this.fluid, (Holder<SoundEvent>)BuiltInRegistries.SOUND_EVENT.wrapAsHolder((Object)this.soundEvent)), null);
        }

        public void save(RecipeOutput recipeOutput, ResourceLocation id) {
            recipeOutput.accept(id, (Recipe)new EmptyAlchemistCauldronRecipe(this.input, this.returned, this.fluid, (Holder<SoundEvent>)BuiltInRegistries.SOUND_EVENT.wrapAsHolder((Object)this.soundEvent)), null);
        }
    }

    public static class Serializer
    implements RecipeSerializer<EmptyAlchemistCauldronRecipe> {
        public static final MapCodec<EmptyAlchemistCauldronRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group((App)Ingredient.CODEC.fieldOf("input").forGetter(EmptyAlchemistCauldronRecipe::input), (App)ItemStack.CODEC.fieldOf("result").forGetter(EmptyAlchemistCauldronRecipe::result), (App)FluidStack.CODEC.fieldOf("fluid").forGetter(EmptyAlchemistCauldronRecipe::fluid), (App)BuiltInRegistries.SOUND_EVENT.holderByNameCodec().optionalFieldOf("sound", (Object)BuiltInRegistries.SOUND_EVENT.wrapAsHolder((Object)SoundEvents.BOTTLE_FILL)).forGetter(EmptyAlchemistCauldronRecipe::emptySound)).apply((Applicative)builder, EmptyAlchemistCauldronRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, EmptyAlchemistCauldronRecipe> STREAM_CODEC = StreamCodec.composite((StreamCodec)Ingredient.CONTENTS_STREAM_CODEC, EmptyAlchemistCauldronRecipe::input, (StreamCodec)ItemStack.STREAM_CODEC, EmptyAlchemistCauldronRecipe::result, (StreamCodec)FluidStack.STREAM_CODEC, EmptyAlchemistCauldronRecipe::fluid, (StreamCodec)ByteBufCodecs.holderRegistry((ResourceKey)Registries.SOUND_EVENT), EmptyAlchemistCauldronRecipe::emptySound, EmptyAlchemistCauldronRecipe::new);

        public MapCodec<EmptyAlchemistCauldronRecipe> codec() {
            return CODEC;
        }

        public StreamCodec<RegistryFriendlyByteBuf, EmptyAlchemistCauldronRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}

