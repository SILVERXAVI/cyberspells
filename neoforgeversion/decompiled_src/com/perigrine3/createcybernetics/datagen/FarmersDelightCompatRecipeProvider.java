/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  net.minecraft.data.CachedOutput
 *  net.minecraft.data.DataProvider
 *  net.minecraft.data.PackOutput
 *  net.minecraft.data.PackOutput$PathProvider
 *  net.minecraft.data.PackOutput$Target
 *  net.minecraft.resources.ResourceLocation
 */
package com.perigrine3.createcybernetics.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

public final class FarmersDelightCompatRecipeProvider
implements DataProvider {
    private final PackOutput output;
    private final PackOutput.PathProvider recipePath;

    public FarmersDelightCompatRecipeProvider(PackOutput output) {
        this.output = output;
        this.recipePath = output.createPathProvider(PackOutput.Target.DATA_PACK, "recipe");
    }

    public CompletableFuture<?> run(CachedOutput cache) {
        LinkedHashMap<ResourceLocation, JsonObject> toWrite = new LinkedHashMap<ResourceLocation, JsonObject>();
        this.build(toWrite);
        return CompletableFuture.allOf((CompletableFuture[])toWrite.entrySet().stream().map(e -> this.save(cache, (ResourceLocation)e.getKey(), (JsonObject)e.getValue())).toArray(CompletableFuture[]::new));
    }

    public String getName() {
        return "Cybernetic's Farmer's Delight Compat Recipes";
    }

    private CompletableFuture<?> save(CachedOutput cache, ResourceLocation id, JsonObject json) {
        Path path = this.recipePath.json(id);
        return DataProvider.saveStable((CachedOutput)cache, (JsonElement)json, (Path)path);
    }

    private void build(Map<ResourceLocation, JsonObject> out) {
        ResourceLocation id = FarmersDelightCompatRecipeProvider.cc("compat/farmersdelight/brain_stew");
        JsonArray ingredients = new JsonArray();
        ingredients.add((JsonElement)FarmersDelightCompatRecipeProvider.item("createcybernetics:cooked_brain"));
        ingredients.add((JsonElement)FarmersDelightCompatRecipeProvider.item("minecraft:potato"));
        ingredients.add((JsonElement)FarmersDelightCompatRecipeProvider.item("farmersdelight:onion"));
        JsonObject recipe = FarmersDelightCompatRecipeProvider.cookingPot("meals", ingredients, FarmersDelightCompatRecipeProvider.stack("createcybernetics:brain_stew", 1), FarmersDelightCompatRecipeProvider.stack("minecraft:bowl", 1), 200, 0.35);
        out.put(id, FarmersDelightCompatRecipeProvider.withModLoaded(recipe, "farmersdelight"));
        id = FarmersDelightCompatRecipeProvider.cc("compat/farmersdelight/ground_offal_cuttingboard");
        ingredients = new JsonArray();
        ingredients.add((JsonElement)FarmersDelightCompatRecipeProvider.tag("createcybernetics:offal"));
        JsonObject tool = FarmersDelightCompatRecipeProvider.tag("c:tools/knives");
        JsonArray results = new JsonArray();
        results.add((JsonElement)FarmersDelightCompatRecipeProvider.result("createcybernetics:ground_offal", 1, 1.0));
        JsonObject recipe2 = FarmersDelightCompatRecipeProvider.cuttingBoard(ingredients, tool, results, "farmersdelight:block.cutting_board.knife");
        out.put(id, FarmersDelightCompatRecipeProvider.withModLoaded(recipe2, "farmersdelight"));
    }

    private static JsonObject withModLoaded(JsonObject root, String modid) {
        JsonArray conds = new JsonArray();
        conds.add((JsonElement)FarmersDelightCompatRecipeProvider.modLoaded(modid));
        root.add("neoforge:conditions", (JsonElement)conds);
        return root;
    }

    private static JsonObject modLoaded(String modid) {
        JsonObject c = new JsonObject();
        c.addProperty("type", "neoforge:mod_loaded");
        c.addProperty("modid", modid);
        return c;
    }

    private static JsonObject registered(String registry, String value) {
        JsonObject c = new JsonObject();
        c.addProperty("type", "neoforge:registered");
        c.addProperty("registry", registry);
        c.addProperty("value", value);
        return c;
    }

    private static JsonObject cookingPot(String recipeBookTab, JsonArray ingredients, JsonObject result, JsonObject container, int cookingTime, double experience) {
        JsonObject root = new JsonObject();
        root.addProperty("type", "farmersdelight:cooking");
        root.addProperty("recipe_book_tab", recipeBookTab);
        root.add("ingredients", (JsonElement)ingredients);
        root.add("result", (JsonElement)result);
        root.add("container", (JsonElement)container);
        root.addProperty("cookingtime", (Number)cookingTime);
        root.addProperty("experience", (Number)experience);
        return root;
    }

    private static JsonObject cuttingBoard(JsonArray ingredients, JsonObject tool, JsonArray results, String sound) {
        JsonObject root = new JsonObject();
        root.addProperty("type", "farmersdelight:cutting");
        root.add("ingredients", (JsonElement)ingredients);
        root.add("tool", (JsonElement)tool);
        root.add("result", (JsonElement)results);
        root.addProperty("sound", sound);
        return root;
    }

    private static JsonObject item(String id) {
        JsonObject o = new JsonObject();
        o.addProperty("item", id);
        return o;
    }

    private static JsonObject tag(String tagId) {
        JsonObject o = new JsonObject();
        o.addProperty("tag", tagId);
        return o;
    }

    private static JsonObject stack(String id, int count) {
        JsonObject o = new JsonObject();
        o.addProperty("id", id);
        o.addProperty("count", (Number)count);
        return o;
    }

    private static JsonObject result(String id, int count, double chance) {
        JsonObject o = new JsonObject();
        o.addProperty("item", id);
        o.addProperty("count", (Number)count);
        if (chance < 1.0) {
            o.addProperty("chance", (Number)chance);
        }
        return o;
    }

    private static ResourceLocation cc(String path) {
        return ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)path);
    }
}

