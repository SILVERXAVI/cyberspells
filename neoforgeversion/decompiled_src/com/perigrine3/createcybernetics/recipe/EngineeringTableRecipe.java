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
 *  net.minecraft.world.item.ItemStack
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
import com.perigrine3.createcybernetics.recipe.EngineeringTableRecipeInput;
import com.perigrine3.createcybernetics.recipe.ModRecipes;
import java.util.List;
import java.util.Map;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record EngineeringTableRecipe(int width, int height, NonNullList<Ingredient> ingredients, ItemStack result, boolean accept_mirrored) implements Recipe<EngineeringTableRecipeInput>
{
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public boolean matches(EngineeringTableRecipeInput input, Level level) {
        if (this.width <= 0 || this.height <= 0) {
            return false;
        }
        int maxX = 5 - this.width;
        int maxY = 5 - this.height;
        for (int offY = 0; offY <= maxY; ++offY) {
            for (int offX = 0; offX <= maxX; ++offX) {
                if (this.matchesAt(input, offX, offY, false)) {
                    return true;
                }
                if (!this.accept_mirrored || !this.matchesAt(input, offX, offY, true)) continue;
                return true;
            }
        }
        return false;
    }

    private boolean matchesAt(EngineeringTableRecipeInput input, int offX, int offY, boolean mirror) {
        for (int y = 0; y < 5; ++y) {
            for (int x = 0; x < 5; ++x) {
                int relY;
                int gridIndex = x + y * 5;
                boolean inside = x >= offX && x < offX + this.width && y >= offY && y < offY + this.height;
                ItemStack stack = input.getItem(gridIndex);
                if (!inside) {
                    if (stack.isEmpty()) continue;
                    return false;
                }
                int relX = x - offX;
                int patX = mirror ? this.width - 1 - relX : relX;
                int patIndex = patX + (relY = y - offY) * this.width;
                Ingredient ing = (Ingredient)this.ingredients.get(patIndex);
                if (ing.test(stack)) continue;
                return false;
            }
        }
        return true;
    }

    public ItemStack assemble(EngineeringTableRecipeInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }

    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result;
    }

    public RecipeSerializer<?> getSerializer() {
        return (RecipeSerializer)ModRecipes.ENGINEERING_TABLE_SERIALIZER.get();
    }

    public RecipeType<?> getType() {
        return (RecipeType)ModRecipes.ENGINEERING_TABLE_TYPE.get();
    }

    public static final class Serializer
    implements RecipeSerializer<EngineeringTableRecipe> {
        private static final Codec<Map<String, Ingredient>> KEY_CODEC = Codec.unboundedMap((Codec)Codec.STRING, (Codec)Ingredient.CODEC_NONEMPTY).flatXmap(Serializer::validateKeyMap, Serializer::validateKeyMap);
        private static final Codec<List<String>> PATTERN_CODEC = Codec.list((Codec)Codec.STRING).flatXmap(Serializer::validatePattern, Serializer::validatePattern);
        public static final MapCodec<EngineeringTableRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group((App)PATTERN_CODEC.fieldOf("pattern").forGetter(r -> {
            throw new UnsupportedOperationException("Encoding to JSON not supported for EngineeringTableRecipe");
        }), (App)KEY_CODEC.fieldOf("key").forGetter(r -> {
            throw new UnsupportedOperationException("Encoding to JSON not supported for EngineeringTableRecipe");
        }), (App)ItemStack.CODEC.fieldOf("result").forGetter(EngineeringTableRecipe::result), (App)Codec.BOOL.optionalFieldOf("accept_mirrored", (Object)true).forGetter(EngineeringTableRecipe::accept_mirrored)).apply((Applicative)inst, (pattern, key, result, accept_mirrored) -> Serializer.fromJson(pattern, key, result, accept_mirrored)));
        public static final StreamCodec<RegistryFriendlyByteBuf, EngineeringTableRecipe> STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, EngineeringTableRecipe>(){

            public EngineeringTableRecipe decode(RegistryFriendlyByteBuf buf) {
                int w = buf.readVarInt();
                int h = buf.readVarInt();
                boolean accept_mirrored = buf.readBoolean();
                int count = buf.readVarInt();
                NonNullList ings = NonNullList.withSize((int)count, (Object)Ingredient.EMPTY);
                for (int i = 0; i < count; ++i) {
                    ings.set(i, (Object)((Ingredient)Ingredient.CONTENTS_STREAM_CODEC.decode((Object)buf)));
                }
                ItemStack result = (ItemStack)ItemStack.STREAM_CODEC.decode((Object)buf);
                return new EngineeringTableRecipe(w, h, (NonNullList<Ingredient>)ings, result, accept_mirrored);
            }

            public void encode(RegistryFriendlyByteBuf buf, EngineeringTableRecipe recipe) {
                buf.writeVarInt(recipe.width);
                buf.writeVarInt(recipe.height);
                buf.writeBoolean(recipe.accept_mirrored);
                buf.writeVarInt(recipe.ingredients.size());
                for (int i = 0; i < recipe.ingredients.size(); ++i) {
                    Ingredient.CONTENTS_STREAM_CODEC.encode((Object)buf, (Object)((Ingredient)recipe.ingredients.get(i)));
                }
                ItemStack.STREAM_CODEC.encode((Object)buf, (Object)recipe.result);
            }
        };

        private static DataResult<Map<String, Ingredient>> validateKeyMap(Map<String, Ingredient> map) {
            for (String k : map.keySet()) {
                if (k.length() != 1) {
                    return DataResult.error(() -> "Key entries must be single-character strings. Got: \"" + k + "\"");
                }
                if (!" ".equals(k)) continue;
                return DataResult.error(() -> "Key entry \" \" (space) is reserved for empty and cannot be defined.");
            }
            return DataResult.success(map);
        }

        private static DataResult<List<String>> validatePattern(List<String> pattern) {
            if (pattern.isEmpty()) {
                return DataResult.error(() -> "Pattern must have at least 1 row.");
            }
            if (pattern.size() > 5) {
                return DataResult.error(() -> "Pattern height must be <= 5.");
            }
            int w = pattern.get(0).length();
            if (w == 0) {
                return DataResult.error(() -> "Pattern rows must not be empty.");
            }
            if (w > 5) {
                return DataResult.error(() -> "Pattern width must be <= 5.");
            }
            for (String row : pattern) {
                if (row.length() == w) continue;
                return DataResult.error(() -> "All pattern rows must be the same width.");
            }
            return DataResult.success(pattern);
        }

        public MapCodec<EngineeringTableRecipe> codec() {
            return CODEC;
        }

        public StreamCodec<RegistryFriendlyByteBuf, EngineeringTableRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static EngineeringTableRecipe fromJson(List<String> pattern, Map<String, Ingredient> key, ItemStack result, boolean accept_mirrored) {
            int h = pattern.size();
            int w = pattern.get(0).length();
            NonNullList ings = NonNullList.withSize((int)(w * h), (Object)Ingredient.EMPTY);
            for (int y = 0; y < h; ++y) {
                String row = pattern.get(y);
                for (int x = 0; x < w; ++x) {
                    char c = row.charAt(x);
                    if (c == ' ') {
                        ings.set(x + y * w, (Object)Ingredient.EMPTY);
                        continue;
                    }
                    Ingredient ing = key.get(String.valueOf(c));
                    if (ing == null) {
                        throw new IllegalArgumentException("Pattern uses symbol '" + c + "' but it is not defined in key.");
                    }
                    ings.set(x + y * w, (Object)ing);
                }
            }
            return new EngineeringTableRecipe(w, h, (NonNullList<Ingredient>)ings, result, accept_mirrored);
        }
    }
}

