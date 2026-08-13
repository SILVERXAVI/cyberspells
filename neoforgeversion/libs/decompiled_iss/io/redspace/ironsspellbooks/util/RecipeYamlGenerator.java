/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 */
package io.redspace.ironsspellbooks.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;

public class RecipeYamlGenerator {
    private static final String RECIPE_DATA_TEMPLATE = "- name: \"%s\"\n  path: \"%s\"\n  item0: \"%s\"\n  item0Path: \"%s\"\n  item1: \"%s\"\n  item1Path: \"%s\"\n  item2: \"%s\"\n  item2Path: \"%s\"\n  item3: \"%s\"\n  item3Path: \"%s\"\n  item4: \"%s\"\n  item4Path: \"%s\"\n  item5: \"%s\"\n  item5Path: \"%s\"\n  item6: \"%s\"\n  item6Path: \"%s\"\n  item7: \"%s\"\n  item7Path: \"%s\"\n  item8: \"%s\"\n  item8Path: \"%s\"\n  item9: \"%s\"\n  item9Path: \"%s\"\n\n";

    public static void main(String[] args) {
        String baseDir = System.getProperty("user.dir");
        String recipesDir = "src/main/resources/data/irons_spellbooks/recipes/";
        StringBuilder sb = new StringBuilder();
        Gson g = new Gson();
        try {
            Files.walk(Path.of(baseDir, recipesDir), new FileVisitOption[0]).forEach(path -> {
                System.out.println(path);
                if (path.toFile().isFile()) {
                    try {
                        JsonObject jsonObject = JsonParser.parseReader((Reader)new FileReader(path.toFile())).getAsJsonObject();
                        String type = jsonObject.get("type").getAsString();
                        if (type.equals("minecraft:crafting_shaped")) {
                            int i;
                            String result = jsonObject.get("result").getAsJsonObject().get("item").getAsString();
                            System.out.println(result);
                            ItemInfo[] itemInfoArray = new ItemInfo[9];
                            JsonArray pattern = jsonObject.get("pattern").getAsJsonArray();
                            Map keyMap = jsonObject.get("key").getAsJsonObject().asMap();
                            int count = 0;
                            for (i = 0; i < pattern.size(); ++i) {
                                String slots = pattern.get(i).getAsString();
                                for (int j = 0; j < slots.length(); ++j) {
                                    char key = slots.charAt(j);
                                    if (key != ' ') {
                                        JsonElement item = ((JsonElement)keyMap.get(String.valueOf(key))).getAsJsonObject().get("item");
                                        if (item == null) {
                                            item = ((JsonElement)keyMap.get(String.valueOf(key))).getAsJsonObject().get("tag");
                                        }
                                        itemInfoArray[count] = new ItemInfo(item.getAsString(), item.getAsString());
                                    }
                                    ++count;
                                }
                            }
                            for (i = 0; i < itemInfoArray.length; ++i) {
                                System.out.println(String.format("%d: %s", i, itemInfoArray[i]));
                            }
                        }
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                        Arrays.stream(e.getStackTrace()).forEach(System.out::println);
                    }
                }
            });
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private record ItemInfo(String name, String path) {
    }
}

