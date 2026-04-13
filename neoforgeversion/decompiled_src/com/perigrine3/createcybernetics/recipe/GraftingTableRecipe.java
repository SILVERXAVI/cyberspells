/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.DataResult
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.NonNullList
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.Recipe
 *  net.minecraft.world.item.crafting.RecipeSerializer
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.level.Level
 */
package com.perigrine3.createcybernetics.recipe;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.recipe.GraftingTableRecipeInput;
import com.perigrine3.createcybernetics.recipe.ModRecipes;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record GraftingTableRecipe(NonNullList<Ingredient> wetware, ItemStack result) implements Recipe<GraftingTableRecipeInput>
{
    public static final int WETWARE_SLOTS = 4;
    public static final int INPUT_COUNT = 7;
    public static final int SLOT_MESH = 4;
    public static final int SLOT_STRING = 5;
    public static final int SLOT_TEAR = 6;

    public NonNullList<Ingredient> getIngredients() {
        return this.wetware;
    }

    public boolean matches(GraftingTableRecipeInput input, Level level) {
        if (input.size() < 7) {
            return false;
        }
        if (!input.getItem(4).is((Item)ModItems.COMPONENT_MESH.get())) {
            return false;
        }
        if (!input.getItem(5).is(Items.STRING)) {
            return false;
        }
        if (!input.getItem(6).is(Items.GHAST_TEAR)) {
            return false;
        }
        ArrayList<ItemStack> stacks = new ArrayList<ItemStack>(4);
        for (int i = 0; i < 4; ++i) {
            ItemStack s = input.getItem(i);
            if (s.isEmpty()) continue;
            stacks.add(s);
        }
        if (stacks.size() != this.wetware.size()) {
            return false;
        }
        boolean[] used = new boolean[stacks.size()];
        for (Ingredient ing : this.wetware) {
            boolean matched = false;
            for (int i = 0; i < stacks.size(); ++i) {
                if (used[i] || !ing.test((ItemStack)stacks.get(i))) continue;
                used[i] = true;
                matched = true;
                break;
            }
            if (matched) continue;
            return false;
        }
        return true;
    }

    public ItemStack assemble(GraftingTableRecipeInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }

    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result.copy();
    }

    public RecipeSerializer<?> getSerializer() {
        return (RecipeSerializer)ModRecipes.GRAFTING_TABLE_SERIALIZER.get();
    }

    public RecipeType<?> getType() {
        return (RecipeType)ModRecipes.GRAFTING_TABLE_TYPE.get();
    }

    public NonNullList<ItemStack> getRemainingItems(GraftingTableRecipeInput input) {
        NonNullList remains = NonNullList.withSize((int)7, (Object)ItemStack.EMPTY);
        for (int i = 0; i < 7; ++i) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty() || !stack.hasCraftingRemainingItem()) continue;
            remains.set(i, (Object)stack.getCraftingRemainingItem());
        }
        return remains;
    }

    public static final class Serializer
    implements RecipeSerializer<GraftingTableRecipe> {
        private static final Codec<List<Ingredient>> WETWARE_LIST_CODEC = Ingredient.CODEC_NONEMPTY.listOf().flatXmap(Serializer::validateWetwareSize, Serializer::validateWetwareSize);
        public static final MapCodec<GraftingTableRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group((App)WETWARE_LIST_CODEC.fieldOf("wetware").forGetter(r -> r.wetware), (App)ItemStack.CODEC.fieldOf("result").forGetter(GraftingTableRecipe::result)).apply((Applicative)inst, (wetware, result) -> new GraftingTableRecipe(Serializer.toNNL(wetware), (ItemStack)result)));
        public static final StreamCodec<RegistryFriendlyByteBuf, GraftingTableRecipe> STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, GraftingTableRecipe>(){

            public GraftingTableRecipe decode(RegistryFriendlyByteBuf buf) {
                int n = buf.readVarInt();
                NonNullList wetware = NonNullList.withSize((int)n, (Object)Ingredient.EMPTY);
                for (int i = 0; i < n; ++i) {
                    wetware.set(i, (Object)((Ingredient)Ingredient.CONTENTS_STREAM_CODEC.decode((Object)buf)));
                }
                ItemStack res = (ItemStack)ItemStack.STREAM_CODEC.decode((Object)buf);
                return new GraftingTableRecipe((NonNullList<Ingredient>)wetware, res);
            }

            public void encode(RegistryFriendlyByteBuf buf, GraftingTableRecipe recipe) {
                buf.writeVarInt(recipe.wetware.size());
                for (Ingredient ing : recipe.wetware) {
                    Ingredient.CONTENTS_STREAM_CODEC.encode((Object)buf, (Object)ing);
                }
                ItemStack.STREAM_CODEC.encode((Object)buf, (Object)recipe.result);
            }
        };

        private static DataResult<List<Ingredient>> validateWetwareSize(List<Ingredient> list) {
            if (list.isEmpty()) {
                return DataResult.error(() -> "wetware must have at least 1 ingredient");
            }
            if (list.size() > 4) {
                return DataResult.error(() -> "wetware must have 1..4 ingredients, got " + list.size());
            }
            return DataResult.success(list);
        }

        public MapCodec<GraftingTableRecipe> codec() {
            return CODEC;
        }

        public StreamCodec<RegistryFriendlyByteBuf, GraftingTableRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static NonNullList<Ingredient> toNNL(List<Ingredient> list) {
            NonNullList nn = NonNullList.withSize((int)list.size(), (Object)Ingredient.EMPTY);
            for (int i = 0; i < list.size(); ++i) {
                nn.set(i, (Object)list.get(i));
            }
            return nn;
        }
    }
}

