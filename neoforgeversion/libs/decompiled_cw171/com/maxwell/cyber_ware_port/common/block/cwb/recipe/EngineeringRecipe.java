/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.NonNullList
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.Recipe
 *  net.minecraft.world.item.crafting.RecipeSerializer
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.item.crafting.SingleRecipeInput
 *  net.minecraft.world.level.Level
 */
package com.maxwell.cyber_ware_port.common.block.cwb.recipe;

import com.maxwell.cyber_ware_port.init.ModRecipes;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class EngineeringRecipe
implements Recipe<SingleRecipeInput> {
    private final Ingredient input;
    private final List<OutputEntry> outputs;
    private final float blueprintChance;

    public EngineeringRecipe(Ingredient input, List<OutputEntry> outputs, float blueprintChance) {
        this.input = input;
        this.outputs = outputs;
        this.blueprintChance = blueprintChance;
    }

    public Ingredient input() {
        return this.input;
    }

    public List<OutputEntry> outputs() {
        return this.outputs;
    }

    public float blueprintChance() {
        return this.blueprintChance;
    }

    public NonNullList<Ingredient> getIngredients() {
        NonNullList list = NonNullList.create();
        list.add((Object)this.input);
        return list;
    }

    public float getBlueprintChance() {
        return this.blueprintChance;
    }

    public boolean matches(SingleRecipeInput pInput, Level pLevel) {
        return this.input.test(pInput.item());
    }

    public List<ItemStack> rollOutputs(RandomSource random) {
        ArrayList<ItemStack> results = new ArrayList<ItemStack>();
        for (OutputEntry entry : this.outputs) {
            if (!(random.nextFloat() < entry.chance())) continue;
            results.add(entry.stack().copy());
        }
        return results;
    }

    public ItemStack assemble(SingleRecipeInput pInput, HolderLookup.Provider pRegistries) {
        return ItemStack.EMPTY;
    }

    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        if (!this.outputs.isEmpty()) {
            return this.outputs.get(0).stack();
        }
        return ItemStack.EMPTY;
    }

    public RecipeSerializer<?> getSerializer() {
        return (RecipeSerializer)ModRecipes.ENGINEERING_SERIALIZER.get();
    }

    public RecipeType<?> getType() {
        return (RecipeType)ModRecipes.ENGINEERING_TYPE.get();
    }

    public record OutputEntry(ItemStack stack, float chance) {
        public static final MapCodec<OutputEntry> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group((App)ItemStack.CODEC.fieldOf("stack").forGetter(OutputEntry::stack), (App)Codec.FLOAT.fieldOf("chance").forGetter(OutputEntry::chance)).apply((Applicative)inst, OutputEntry::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, OutputEntry> STREAM_CODEC = StreamCodec.composite((StreamCodec)ItemStack.STREAM_CODEC, OutputEntry::stack, (StreamCodec)ByteBufCodecs.FLOAT, OutputEntry::chance, OutputEntry::new);
    }

    public static class Serializer
    implements RecipeSerializer<EngineeringRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        private static final MapCodec<EngineeringRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group((App)Ingredient.CODEC.fieldOf("input").forGetter(EngineeringRecipe::input), (App)OutputEntry.CODEC.codec().listOf().fieldOf("outputs").forGetter(EngineeringRecipe::outputs), (App)Codec.FLOAT.optionalFieldOf("blueprint_chance", (Object)Float.valueOf(0.5f)).forGetter(EngineeringRecipe::blueprintChance)).apply((Applicative)inst, EngineeringRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, EngineeringRecipe> STREAM_CODEC = StreamCodec.composite((StreamCodec)Ingredient.CONTENTS_STREAM_CODEC, EngineeringRecipe::input, (StreamCodec)OutputEntry.STREAM_CODEC.apply(ByteBufCodecs.list()), EngineeringRecipe::outputs, (StreamCodec)ByteBufCodecs.FLOAT, EngineeringRecipe::blueprintChance, EngineeringRecipe::new);

        public MapCodec<EngineeringRecipe> codec() {
            return CODEC;
        }

        public StreamCodec<RegistryFriendlyByteBuf, EngineeringRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}

