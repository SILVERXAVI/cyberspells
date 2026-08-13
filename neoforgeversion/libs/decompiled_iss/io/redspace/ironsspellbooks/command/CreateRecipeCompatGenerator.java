/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.serialization.DynamicOps
 *  com.mojang.serialization.JsonOps
 *  net.minecraft.client.Minecraft
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.RecipeManager
 *  net.minecraft.world.item.crafting.RecipeType
 */
package io.redspace.ironsspellbooks.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.recipe_types.alchemist_cauldron.EmptyAlchemistCauldronRecipe;
import io.redspace.ironsspellbooks.recipe_types.alchemist_cauldron.FillAlchemistCauldronRecipe;
import io.redspace.ironsspellbooks.registries.RecipeRegistry;
import java.io.File;
import java.io.FileWriter;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

public class CreateRecipeCompatGenerator {
    private static final String EMPTY_FORMAT = "{\n  \"neoforge:conditions\": [\n    {\n      \"type\": \"neoforge:mod_loaded\",\n      \"modid\": \"create\"\n    }\n  ],\n  \"type\": \"create:emptying\",\n  \"ingredients\": [\n    %s\n  ],\n  \"results\": [\n    {\n      \"id\": \"%s\"\n    },\n    {\n      \"id\": \"%s\",\n      \"amount\": %s\n    }\n  ]\n}\n";
    private static final String FILL_FORMAT = "{\n  \"neoforge:conditions\": [\n    {\n      \"type\": \"neoforge:mod_loaded\",\n      \"modid\": \"create\"\n    }\n  ],\n  \"type\": \"create:filling\",\n  \"ingredients\": [\n    %s,\n    {\n      \"type\": \"fluid_stack\",\n      \"fluid\": \"%s\",\n      \"amount\": %s\n    }\n  ],\n  \"results\": [\n    {\n      \"id\": \"%s\"\n    }\n  ]\n}\n";

    public static int run(CommandContext<CommandSourceStack> context) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        new File("create_compat").mkdir();
        recipeManager.getAllRecipesFor((RecipeType)RecipeRegistry.ALCHEMIST_CAULDRON_EMPTY_TYPE.get()).stream().forEach(recipeHolder -> {
            EmptyAlchemistCauldronRecipe recipe = (EmptyAlchemistCauldronRecipe)recipeHolder.value();
            ResourceLocation resultId = recipe.result().getItemHolder().getKey().location();
            if (resultId.getNamespace().equals("irons_spellbooks")) {
                String stringJson = String.format(FILL_FORMAT, ((JsonElement)Ingredient.CODEC.encodeStart((DynamicOps)JsonOps.INSTANCE, (Object)recipe.input()).getOrThrow()).toString(), recipe.fluid().getFluidHolder().getKey().location().toString(), recipe.fluid().getAmount(), recipe.result().getItemHolder().getKey().location().toString());
                String outputFilepath = String.format("create_compat/create_fill_%s.json", recipeHolder.id().getPath().split("/", 2)[1].split("_", 2)[1]);
                File file = new File(outputFilepath);
                try (FileWriter writer = new FileWriter(file);){
                    writer.write(stringJson);
                }
                catch (Exception e) {
                    IronsSpellbooks.LOGGER.debug("Failed to generate recipe \"{}\": {}", (Object)outputFilepath, (Object)e.getMessage());
                }
            }
        });
        recipeManager.getAllRecipesFor((RecipeType)RecipeRegistry.ALCHEMIST_CAULDRON_FILL_TYPE.get()).stream().forEach(recipeHolder -> {
            FillAlchemistCauldronRecipe recipe = (FillAlchemistCauldronRecipe)recipeHolder.value();
            ResourceLocation inputId = recipe.result().getFluidHolder().getKey().location();
            if (inputId.getNamespace().equals("irons_spellbooks")) {
                String stringJson = String.format(EMPTY_FORMAT, ((JsonElement)Ingredient.CODEC.encodeStart((DynamicOps)JsonOps.INSTANCE, (Object)recipe.input()).getOrThrow()).toString(), recipe.returned().getItemHolder().getKey().location().toString(), recipe.result().getFluidHolder().getKey().location().toString(), recipe.result().getAmount());
                new File("create_compat").mkdir();
                String outputFilepath = String.format("create_compat/create_empty_%s.json", recipeHolder.id().getPath().split("/", 2)[1].split("_", 2)[1]);
                File file = new File(outputFilepath);
                try (FileWriter writer = new FileWriter(file);){
                    writer.write(stringJson);
                }
                catch (Exception e) {
                    IronsSpellbooks.LOGGER.debug("Failed to generate recipe \"{}\": {}", (Object)outputFilepath, (Object)e.getMessage());
                }
            }
        });
        return 1;
    }
}

