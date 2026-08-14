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
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.Recipe
 *  net.minecraft.world.item.crafting.RecipeInput
 *  net.minecraft.world.item.crafting.RecipeSerializer
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.level.Level
 */
package com.maxwell.cyber_ware_port.common.block.cwb.recipe;

import com.maxwell.cyber_ware_port.init.ModRecipes;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class AssemblyRecipe
implements Recipe<RecipeInput> {
    private final List<SizedIngredient> inputs;
    private final ItemStack output;

    public AssemblyRecipe(List<SizedIngredient> inputs, ItemStack output) {
        this.inputs = inputs;
        this.output = output;
    }

    public List<SizedIngredient> getInputs() {
        return this.inputs;
    }

    public boolean matches(RecipeInput pInput, Level pLevel) {
        return true;
    }

    public ItemStack assemble(RecipeInput pInput, HolderLookup.Provider pRegistries) {
        return this.output.copy();
    }

    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return this.output;
    }

    public RecipeSerializer<?> getSerializer() {
        return (RecipeSerializer)ModRecipes.ASSEMBLY_SERIALIZER.get();
    }

    public RecipeType<?> getType() {
        return (RecipeType)ModRecipes.ASSEMBLY_TYPE.get();
    }

    public static class Serializer
    implements RecipeSerializer<AssemblyRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        private static final MapCodec<AssemblyRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group((App)SizedIngredient.CODEC.codec().listOf().fieldOf("inputs").forGetter(r -> r.inputs), (App)ItemStack.CODEC.fieldOf("output").forGetter(r -> r.output)).apply((Applicative)inst, AssemblyRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, AssemblyRecipe> STREAM_CODEC = StreamCodec.composite((StreamCodec)SizedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.inputs, (StreamCodec)ItemStack.STREAM_CODEC, r -> r.output, AssemblyRecipe::new);

        public MapCodec<AssemblyRecipe> codec() {
            return CODEC;
        }

        public StreamCodec<RegistryFriendlyByteBuf, AssemblyRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public record SizedIngredient(Ingredient ingredient, int count) {
        public static final MapCodec<SizedIngredient> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group((App)Ingredient.CODEC.fieldOf("ingredient").forGetter(SizedIngredient::ingredient), (App)Codec.INT.optionalFieldOf("count", (Object)1).forGetter(SizedIngredient::count)).apply((Applicative)inst, SizedIngredient::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, SizedIngredient> STREAM_CODEC = StreamCodec.composite((StreamCodec)Ingredient.CONTENTS_STREAM_CODEC, SizedIngredient::ingredient, (StreamCodec)ByteBufCodecs.VAR_INT, SizedIngredient::count, SizedIngredient::new);
    }
}

