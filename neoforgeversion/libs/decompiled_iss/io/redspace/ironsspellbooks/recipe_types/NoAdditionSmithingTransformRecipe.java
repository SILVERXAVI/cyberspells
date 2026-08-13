/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.RecipeSerializer
 *  net.minecraft.world.item.crafting.SmithingRecipe
 *  net.minecraft.world.item.crafting.SmithingRecipeInput
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 */
package io.redspace.ironsspellbooks.recipe_types;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.ironsspellbooks.registries.RecipeRegistry;
import java.util.stream.Stream;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class NoAdditionSmithingTransformRecipe
implements SmithingRecipe {
    final Ingredient template;
    final Ingredient base;
    final ItemStack result;

    public NoAdditionSmithingTransformRecipe(Ingredient template, Ingredient base, ItemStack result) {
        this.template = template;
        this.base = base;
        this.result = result;
    }

    public boolean matches(SmithingRecipeInput input, Level level) {
        return this.template.test(input.template()) && this.base.test(input.base()) && input.addition().isEmpty();
    }

    public ItemStack assemble(SmithingRecipeInput input, HolderLookup.Provider registries) {
        ItemStack itemstack = input.base().transmuteCopy((ItemLike)this.result.getItem(), this.result.getCount());
        itemstack.applyComponents(this.result.getComponentsPatch());
        return itemstack;
    }

    public Ingredient getTemplate() {
        return this.template;
    }

    public Ingredient getBase() {
        return this.base;
    }

    public Ingredient getResult() {
        return Ingredient.of((ItemStack[])new ItemStack[]{this.result});
    }

    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result;
    }

    public boolean isTemplateIngredient(ItemStack stack) {
        return this.template.test(stack);
    }

    public boolean isBaseIngredient(ItemStack stack) {
        return this.base.test(stack);
    }

    public boolean isAdditionIngredient(ItemStack stack) {
        return false;
    }

    public RecipeSerializer<?> getSerializer() {
        return (RecipeSerializer)RecipeRegistry.SMITHING_TRANSFORM_NO_ADDITION_SERIALIZER.get();
    }

    public boolean isIncomplete() {
        return Stream.of(this.template, this.base).anyMatch(Ingredient::hasNoItems);
    }

    public static class Serializer
    implements RecipeSerializer<NoAdditionSmithingTransformRecipe> {
        private static final MapCodec<NoAdditionSmithingTransformRecipe> CODEC = RecordCodecBuilder.mapCodec(p_340782_ -> p_340782_.group((App)Ingredient.CODEC.fieldOf("template").forGetter(p_301310_ -> p_301310_.template), (App)Ingredient.CODEC.fieldOf("base").forGetter(p_300938_ -> p_300938_.base), (App)ItemStack.STRICT_CODEC.fieldOf("result").forGetter(p_300935_ -> p_300935_.result)).apply((Applicative)p_340782_, NoAdditionSmithingTransformRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, NoAdditionSmithingTransformRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        public MapCodec<NoAdditionSmithingTransformRecipe> codec() {
            return CODEC;
        }

        public StreamCodec<RegistryFriendlyByteBuf, NoAdditionSmithingTransformRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static NoAdditionSmithingTransformRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            Ingredient ingredient = (Ingredient)Ingredient.CONTENTS_STREAM_CODEC.decode((Object)buffer);
            Ingredient ingredient1 = (Ingredient)Ingredient.CONTENTS_STREAM_CODEC.decode((Object)buffer);
            ItemStack itemstack = (ItemStack)ItemStack.STREAM_CODEC.decode((Object)buffer);
            return new NoAdditionSmithingTransformRecipe(ingredient, ingredient1, itemstack);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, NoAdditionSmithingTransformRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode((Object)buffer, (Object)recipe.template);
            Ingredient.CONTENTS_STREAM_CODEC.encode((Object)buffer, (Object)recipe.base);
            ItemStack.STREAM_CODEC.encode((Object)buffer, (Object)recipe.result);
        }
    }
}

