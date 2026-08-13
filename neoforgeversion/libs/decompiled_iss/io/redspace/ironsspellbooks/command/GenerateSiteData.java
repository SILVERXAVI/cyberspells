/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.Message
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.exceptions.SimpleCommandExceptionType
 *  javax.annotation.Nullable
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ArmorItem
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.CreativeModeTabs
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.ProjectileWeaponItem
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.TooltipFlag$Default
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.Recipe
 *  net.minecraft.world.item.crafting.RecipeHolder
 *  net.minecraft.world.item.crafting.ShapedRecipe
 *  net.minecraft.world.item.crafting.ShapelessRecipe
 *  net.minecraft.world.item.crafting.SmithingTransformRecipe
 *  net.minecraft.world.level.ItemLike
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.item.CastingItem;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.UniqueItem;
import io.redspace.ironsspellbooks.item.UpgradeOrbItem;
import io.redspace.ironsspellbooks.item.consumables.SimpleElixir;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.player.ClientInputEvents;
import io.redspace.ironsspellbooks.recipe_types.NoAdditionSmithingTransformRecipe;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public class GenerateSiteData {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)Component.translatable((String)"commands.irons_spellbooks.generate_recipe_data.failed"));
    private static final String RECIPE_DATA_TEMPLATE = "- id: \"%s\"\n  name: \"%s\"\n  path: \"%s\"\n  group: \"%s\"\n  sortOverride: \"%s\"\n  craftingType: \"%s\"\n  item0Id: \"%s\"\n  item0: \"%s\"\n  item0Path: \"%s\"\n  item1Id: \"%s\"\n  item1: \"%s\"\n  item1Path: \"%s\"\n  item2Id: \"%s\"\n  item2: \"%s\"\n  item2Path: \"%s\"\n  item3Id: \"%s\"\n  item3: \"%s\"\n  item3Path: \"%s\"\n  item4Id: \"%s\"\n  item4: \"%s\"\n  item4Path: \"%s\"\n  item5Id: \"%s\"\n  item5: \"%s\"\n  item5Path: \"%s\"\n  item6Id: \"%s\"\n  item6: \"%s\"\n  item6Path: \"%s\"\n  item7Id: \"%s\"\n  item7: \"%s\"\n  item7Path: \"%s\"\n  item8Id: \"%s\"\n  item8: \"%s\"\n  item8Path: \"%s\"\n  tooltip: \"%s\"\n  description: \"\"\n\n";
    private static final String SPELL_DATA_TEMPLATE = "- name: \"%s\"\n  school: \"%s\"\n  icon: \"%s\"\n  level: \"%d to %d\"\n  mana: \"%d to %d\"\n  cooldown: \"%ds\"\n  cast_type: \"%s\"\n  rarity: \"%s to %s\"\n  description: \"%s\"\n  u1: \"%s\"\n  u2: \"%s\"\n  u3: \"%s\"\n  u4: \"%s\"\n\n";
    static ServerLevel level;

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
        pDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal((String)"generateSiteData").requires(p_138819_ -> p_138819_.hasPermission(2))).executes(commandContext -> GenerateSiteData.generateSiteData((CommandSourceStack)commandContext.getSource())));
    }

    private static int generateSiteData(CommandSourceStack source) {
        GenerateSiteData.generateRecipeData(source);
        GenerateSiteData.generateSpellData();
        return 1;
    }

    private static void generateRecipeData(CommandSourceStack source) {
        try {
            StringBuilder itemBuilder = new StringBuilder();
            StringBuilder armorBuilder = new StringBuilder();
            StringBuilder spellbookBuilder = new StringBuilder();
            StringBuilder curioBuilder = new StringBuilder();
            StringBuilder blockBuilder = new StringBuilder();
            level = source.getLevel();
            HashSet<Item> itemsTracked = new HashSet<Item>();
            ClientInputEvents.isShiftKeyDown = true;
            GenerateSiteData.handleAffinityRingEntry(curioBuilder, itemsTracked, source);
            GenerateSiteData.getVisibleItems().stream().sorted(Comparator.comparing(Item::getDescriptionId)).forEach(item -> {
                ResourceLocation itemResource = BuiltInRegistries.ITEM.getKey(item);
                String tooltip = GenerateSiteData.getTooltip(source.getPlayer(), new ItemStack((ItemLike)item));
                if (itemResource.getNamespace().equals("irons_spellbooks") && !itemsTracked.contains(item)) {
                    Recipe recipe = GenerateSiteData.getRecipeFor(source, item);
                    String name = item.getName(ItemStack.EMPTY).getString();
                    if (!(item.getDescriptionId().contains("patchouli") || item.getDescriptionId().contains("spawn_egg") || item.getDescriptionId().equals("item.irons_spellbooks.scroll"))) {
                        if (item instanceof ArmorItem) {
                            ArmorItem armorItem = (ArmorItem)item;
                            Class<?> armortype = armorItem.getClass();
                            boolean hasGroup = ItemRegistry.getIronsItems().stream().filter(holder -> armortype.isAssignableFrom(((Item)holder.value()).getClass())).toList().size() > 1;
                            int sort = 0;
                            Object group = "All Armor";
                            if (hasGroup) {
                                String[] words = name.split(" ");
                                group = Arrays.stream(words).limit(words.length - 1).collect(Collectors.joining(" ")) + " Armor";
                                sort = 3 - armorItem.getEquipmentSlot().getIndex();
                            }
                            if (recipe != null) {
                                GenerateSiteData.appendRecipeSorted(armorBuilder, recipe, GenerateSiteData.getRecipeData(recipe), (String)group, tooltip, sort);
                            } else {
                                GenerateSiteData.appendSimpleGroupedSorted(armorBuilder, name, itemResource, (String)group, tooltip, sort);
                            }
                        } else if (item instanceof SpellBook || item instanceof ExtendedSwordItem || item instanceof CastingItem || item instanceof ProjectileWeaponItem || item instanceof UniqueItem) {
                            String group;
                            String string = item instanceof SpellBook ? "Spellbooks" : (group = item instanceof CastingItem ? "Staves" : "Weapons");
                            if (recipe != null) {
                                GenerateSiteData.appendRecipe(spellbookBuilder, recipe, GenerateSiteData.getRecipeData(recipe), group, tooltip);
                            } else {
                                GenerateSiteData.appendSimpleGrouped(spellbookBuilder, name, itemResource, group, tooltip);
                            }
                        } else if (item instanceof CurioBaseItem) {
                            if (recipe != null) {
                                GenerateSiteData.appendRecipe(curioBuilder, recipe, GenerateSiteData.getRecipeData(recipe), "", tooltip);
                            } else {
                                GenerateSiteData.appendSimple(curioBuilder, name, itemResource, tooltip);
                            }
                        } else if (item instanceof BlockItem) {
                            if (recipe != null) {
                                GenerateSiteData.appendRecipe(blockBuilder, recipe, GenerateSiteData.getRecipeData(recipe), "", tooltip);
                            } else {
                                GenerateSiteData.appendSimple(blockBuilder, name, itemResource, tooltip);
                            }
                        } else if (recipe != null) {
                            GenerateSiteData.appendRecipe(itemBuilder, recipe, GenerateSiteData.getRecipeData(recipe), GenerateSiteData.handleGenericItemGrouping(item), tooltip);
                        } else {
                            GenerateSiteData.appendSimpleGrouped(itemBuilder, name, itemResource, GenerateSiteData.handleGenericItemGrouping(item), tooltip);
                        }
                    }
                    itemsTracked.add((Item)item);
                }
            });
            ClientInputEvents.isShiftKeyDown = false;
            BufferedWriter file = new BufferedWriter(new FileWriter("item_data.yml"));
            file.write(GenerateSiteData.postProcess(itemBuilder));
            file.close();
            file = new BufferedWriter(new FileWriter("armor_data.yml"));
            file.write(GenerateSiteData.postProcess(armorBuilder));
            file.close();
            file = new BufferedWriter(new FileWriter("curio_data.yml"));
            file.write(GenerateSiteData.postProcess(curioBuilder));
            file.close();
            file = new BufferedWriter(new FileWriter("spellbook_data.yml"));
            file.write(GenerateSiteData.postProcess(spellbookBuilder));
            file.close();
            file = new BufferedWriter(new FileWriter("block_data.yml"));
            file.write(GenerateSiteData.postProcess(blockBuilder));
            file.close();
        }
        catch (Exception e) {
            IronsSpellbooks.LOGGER.debug(e.getMessage());
        }
    }

    private static void handleAffinityRingEntry(StringBuilder curioBuilder, Set<Item> itemsTracked, CommandSourceStack source) {
        CurioBaseItem item = ItemRegistry.AFFINITY_RING.get();
        itemsTracked.add(item);
        ResourceLocation itemResource = BuiltInRegistries.ITEM.getKey((Object)item);
        String name = item.getName(ItemStack.EMPTY).getString();
        GenerateSiteData.appendSimple(curioBuilder, name, itemResource, "Affinity Rings are randomly generated as loot, and will boost the level of a select spell by one. This effect can stack. Spell can be set in the Arcane Anvil using a scroll.");
    }

    private static String handleGenericItemGrouping(Item item) {
        if (item instanceof InkItem) {
            return "Ink";
        }
        if (item.components().has(DataComponents.JUKEBOX_PLAYABLE)) {
            return "Music Discs";
        }
        if (item.getDescriptionId().contains("rune")) {
            return "Runes";
        }
        if (item instanceof UpgradeOrbItem || item == ItemRegistry.UPGRADE_ORB.get()) {
            return "Upgrade Orbs";
        }
        if (item instanceof SimpleElixir) {
            return "Elixirs";
        }
        return "All";
    }

    @NotNull
    private static ArrayList<RecipeIngredientData> getRecipeData(Recipe<?> recipe) {
        ResourceLocation resultItemResourceLocation = BuiltInRegistries.ITEM.getKey((Object)recipe.getResultItem((HolderLookup.Provider)level.registryAccess()).getItem());
        ArrayList<RecipeIngredientData> recipeData = new ArrayList<RecipeIngredientData>(10);
        recipeData.add(new RecipeIngredientData(resultItemResourceLocation.toString(), recipe.getResultItem((HolderLookup.Provider)level.registryAccess()).getItem().getName(ItemStack.EMPTY).getString(), String.format("/img/items/%s.png", resultItemResourceLocation.getPath()), recipe.getResultItem((HolderLookup.Provider)level.registryAccess()).getItem()));
        if (recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe) {
            recipe.getIngredients().forEach(ingredient -> GenerateSiteData.handleIngredient(ingredient, recipeData, recipe));
        } else if (recipe instanceof SmithingTransformRecipe) {
            SmithingTransformRecipe smithingRecipe = (SmithingTransformRecipe)recipe;
            ItemStack[] itemStackArray = new ItemStack[1];
            itemStackArray[0] = BuiltInRegistries.ITEM.stream().map(Item::getDefaultInstance).filter(arg_0 -> ((SmithingTransformRecipe)smithingRecipe).isTemplateIngredient(arg_0)).findFirst().orElse(ItemStack.EMPTY);
            GenerateSiteData.handleIngredient(Ingredient.of((ItemStack[])itemStackArray), recipeData, recipe);
            ItemStack[] itemStackArray2 = new ItemStack[1];
            itemStackArray2[0] = BuiltInRegistries.ITEM.stream().map(Item::getDefaultInstance).filter(arg_0 -> ((SmithingTransformRecipe)smithingRecipe).isBaseIngredient(arg_0)).findFirst().orElse(ItemStack.EMPTY);
            GenerateSiteData.handleIngredient(Ingredient.of((ItemStack[])itemStackArray2), recipeData, recipe);
            ItemStack[] itemStackArray3 = new ItemStack[1];
            itemStackArray3[0] = BuiltInRegistries.ITEM.stream().map(Item::getDefaultInstance).filter(arg_0 -> ((SmithingTransformRecipe)smithingRecipe).isAdditionIngredient(arg_0)).findFirst().orElse(ItemStack.EMPTY);
            GenerateSiteData.handleIngredient(Ingredient.of((ItemStack[])itemStackArray3), recipeData, recipe);
        } else if (recipe instanceof NoAdditionSmithingTransformRecipe) {
            NoAdditionSmithingTransformRecipe smithingRecipe = (NoAdditionSmithingTransformRecipe)recipe;
            ItemStack[] itemStackArray = new ItemStack[1];
            itemStackArray[0] = BuiltInRegistries.ITEM.stream().map(Item::getDefaultInstance).filter(smithingRecipe::isTemplateIngredient).findFirst().orElse(ItemStack.EMPTY);
            GenerateSiteData.handleIngredient(Ingredient.of((ItemStack[])itemStackArray), recipeData, recipe);
            ItemStack[] itemStackArray4 = new ItemStack[1];
            itemStackArray4[0] = BuiltInRegistries.ITEM.stream().map(Item::getDefaultInstance).filter(smithingRecipe::isBaseIngredient).findFirst().orElse(ItemStack.EMPTY);
            GenerateSiteData.handleIngredient(Ingredient.of((ItemStack[])itemStackArray4), recipeData, recipe);
        }
        return recipeData;
    }

    @Nullable
    private static Recipe getRecipeFor(CommandSourceStack sourceStack, Item item) {
        return sourceStack.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.value().getResultItem((HolderLookup.Provider)level.registryAccess()).is(item)).max(Comparator.comparing(recipeHolder -> BuiltInRegistries.RECIPE_TYPE.getKey((Object)recipeHolder.value().getType()).toString())).map(RecipeHolder::value).orElse(null);
    }

    private static String postProcess(StringBuilder sb) {
        return sb.toString().replace("netherite_spell_book.png", "netherite_spell_book.gif").replace("ruined_book.png", "ruined_book.gif").replace("lightning_bottle.png", "lightning_bottle.gif").replace("cinder_essence.png", "cinder_essence.gif").replace("nature_upgrade_orb.png", "nature_upgrade_orb.gif").replace("evasion_elixir.png", "evasion_elixir.gif").replace("/upgrade_orb.png", "/upgrade_orb.gif").replace("fire_upgrade_orb.png", "fire_upgrade_orb.gif").replace("holy_upgrade_orb.png", "holy_upgrade_orb.gif").replace("lightning_upgrade_orb.png", "lightning_upgrade_orb.gif").replace("ender_upgrade_orb.png", "ender_upgrade_orb.gif").replace("mana_upgrade_orb.png", "upgrade_orb_mana.gif").replace("protection_upgrade_orb.png", "upgrade_orb_protection.gif").replace("ice_upgrade_orb.png", "upgrade_orb_ice.gif").replace("evocation_upgrade_orb.png", "upgrade_orb_evocation.gif").replace("cooldown_upgrade_orb.png", "upgrade_orb_cooldown.gif").replace("blood_upgrade_orb.png", "upgrade_orb_blood.gif").replace("wayward_compass.png", "wayward_compass.gif").replace("affinity_ring.png", "affinity_rings.gif").replace("energized_core.png", "energized_core.gif").replace("Deepslate Mithril Ore", "Mithril Ore (Deepslate)");
    }

    private static String getSpells(ItemStack itemStack) {
        if (itemStack.getItem() instanceof SpellBook) {
            ISpellContainer spellList = ISpellContainer.get(itemStack);
            return spellList.getActiveSpells().stream().map(spell -> spell.getSpell().getDisplayName(null).getString() + " (" + spell.getLevel() + ")").collect(Collectors.joining(", "));
        }
        return "";
    }

    private static String getTooltip(ServerPlayer player, ItemStack itemStack) {
        return Arrays.stream(itemStack.getTooltipLines(Item.TooltipContext.EMPTY, (Player)player, (TooltipFlag)TooltipFlag.Default.NORMAL).stream().skip(1L).map(Component::getString).filter(x -> x.trim().length() > 0).collect(Collectors.joining(", ")).replace(":,", ": ").replace("  ", " ").split(",")).filter(item -> !item.contains("Slot")).collect(Collectors.joining(",")).trim().replace(":", ":<br>");
    }

    private static void appendRecipeSorted(StringBuilder sb, Recipe recipe, List<RecipeIngredientData> recipeIngredientData, String group, String tooltip, int sort) {
        sb.append(String.format(RECIPE_DATA_TEMPLATE, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)0).id, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)0).name, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)0).path, group, sort, recipe.getType(), GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)1).id, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)1).name, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)1).path, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)2).id, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)2).name, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)2).path, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)3).id, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)3).name, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)3).path, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)4).id, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)4).name, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)4).path, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)5).id, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)5).name, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)5).path, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)6).id, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)6).name, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)6).path, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)7).id, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)7).name, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)7).path, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)8).id, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)8).name, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)8).path, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)9).id, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)9).name, GenerateSiteData.getRecipeDataAtIndex(recipeIngredientData, (int)9).path, tooltip));
    }

    private static void appendRecipe(StringBuilder sb, Recipe recipe, List<RecipeIngredientData> recipeIngredientData, String group, String tooltip) {
        GenerateSiteData.appendRecipeSorted(sb, recipe, recipeIngredientData, group, tooltip, 0);
    }

    private static void appendSimple(StringBuilder sb, String name, ResourceLocation itemResource, String tooltip) {
        sb.append(String.format(RECIPE_DATA_TEMPLATE, itemResource.toString(), name, String.format("/img/items/%s.png", itemResource.getPath()), "", "0", "none", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", tooltip));
    }

    private static void appendSimpleGrouped(StringBuilder sb, String name, ResourceLocation itemResource, String group, String tooltip) {
        sb.append(String.format(RECIPE_DATA_TEMPLATE, itemResource.toString(), name, String.format("/img/items/%s.png", itemResource.getPath()), group, "0", "none", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", tooltip));
    }

    private static void appendSimpleGroupedSorted(StringBuilder sb, String name, ResourceLocation itemResource, String group, String tooltip, int sort) {
        sb.append(String.format(RECIPE_DATA_TEMPLATE, itemResource.toString(), name, String.format("/img/items/%s.png", itemResource.getPath()), group, sort, "none", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", tooltip));
    }

    private static void handleIngredient(Ingredient ingredient, ArrayList<RecipeIngredientData> recipeData, Recipe recipe) {
        Arrays.stream(ingredient.getItems()).findFirst().ifPresentOrElse(itemStack -> {
            ResourceLocation itemResource = BuiltInRegistries.ITEM.getKey((Object)itemStack.getItem());
            String path = "";
            path = itemResource.toString().contains("irons_spellbooks") ? String.format("/img/items/%s.png", itemResource.getPath()) : String.format("/img/items/minecraft/%s.png", itemResource.getPath());
            if (itemStack.is(ModTags.INSCRIBED_RUNES) && recipe.getResultItem((HolderLookup.Provider)level.registryAccess()).is((Item)ItemRegistry.BLANK_RUNE.get())) {
                path = "/img/items/all_runes.gif";
            }
            recipeData.add(new RecipeIngredientData(itemResource.toString(), itemStack.getItem().getName(ItemStack.EMPTY).getString(), path, recipe.getResultItem((HolderLookup.Provider)level.registryAccess()).getItem()));
        }, () -> recipeData.add(RecipeIngredientData.EMPTY));
    }

    private static RecipeIngredientData getRecipeDataAtIndex(List<RecipeIngredientData> recipeIngredientData, int index) {
        if (index < recipeIngredientData.size()) {
            return recipeIngredientData.get(index);
        }
        return RecipeIngredientData.EMPTY;
    }

    private static List<Item> getVisibleItems() {
        return BuiltInRegistries.ITEM.stream().filter(item -> CreativeModeTabs.allTabs().stream().anyMatch(tab -> tab.contains(new ItemStack((ItemLike)item)))).toList();
    }

    private static void generateSpellData() {
        try {
            StringBuilder sb = new StringBuilder();
            SpellRegistry.REGISTRY.stream().filter(st -> st.isEnabled() && st != SpellRegistry.none()).forEach(spellType -> {
                int spellMin = spellType.getMinLevel();
                int spellMax = spellType.getMaxLevel();
                List<String> uniqueInfo = GenerateSiteData.processUniqueInfo(spellType);
                String u1 = uniqueInfo.size() >= 1 ? uniqueInfo.get(0) : "";
                String u2 = uniqueInfo.size() >= 2 ? uniqueInfo.get(1) : "";
                String u3 = uniqueInfo.size() >= 3 ? uniqueInfo.get(2) : "";
                String u4 = uniqueInfo.size() >= 4 ? uniqueInfo.get(3) : "";
                sb.append(String.format(SPELL_DATA_TEMPLATE, GenerateSiteData.handleCapitalization(spellType.getSpellName()), GenerateSiteData.handleCapitalization(spellType.getSchoolType().getDisplayName().getString()), String.format("/img/spells/%s.png", spellType.getSpellName()), spellType.getMinLevel(), spellType.getMaxLevel(), spellType.getManaCost(spellMin), spellType.getManaCost(spellMax), spellType.getSpellCooldown(), GenerateSiteData.handleCapitalization(spellType.getCastType().name()), GenerateSiteData.handleCapitalization(spellType.getRarity(spellMin).name()), GenerateSiteData.handleCapitalization(spellType.getRarity(spellMax).name()), Component.translatable((String)String.format("%s.guide", spellType.getComponentId())).getString(), u1, u2, u3, u4));
            });
            BufferedWriter file = new BufferedWriter(new FileWriter("spell_data.yml"));
            file.write(sb.toString());
            file.close();
        }
        catch (Exception e) {
            IronsSpellbooks.LOGGER.debug(e.getMessage());
        }
    }

    private static List<String> processUniqueInfo(AbstractSpell spell) {
        ArrayList<String> text = new ArrayList<String>();
        List<MutableComponent> uniqueInfoMin = spell.getUniqueInfo(spell.getMinLevel(), null);
        List<MutableComponent> uniqueInfoMax = spell.getUniqueInfo(spell.getMaxLevel(), null);
        for (int i = 0; i < uniqueInfoMax.size(); ++i) {
            String[] lineMinLevel = uniqueInfoMin.get(i).getString().split(" ");
            String[] lineMaxLevel = uniqueInfoMax.get(i).getString().split(" ");
            int k = -1;
            for (int j = 0; j < lineMinLevel.length; ++j) {
                if (!lineMinLevel[j].matches("([+\\-])?\\d\\.?\\d*(s|m|%)*")) continue;
                k = j;
                break;
            }
            if (k >= 0 && !lineMinLevel[k].equals(lineMaxLevel[k])) {
                StringBuilder builder = new StringBuilder();
                for (int j = 0; j < lineMinLevel.length; ++j) {
                    if (j == k) {
                        builder.append(String.format("%s-%s", lineMinLevel[k], lineMaxLevel[k])).append(" ");
                        continue;
                    }
                    builder.append(lineMinLevel[j]).append(" ");
                }
                text.add(builder.toString().strip());
                continue;
            }
            text.add(uniqueInfoMin.get(i).getString());
        }
        return text;
    }

    public static String handleCapitalization(String input) {
        return Arrays.stream(input.toLowerCase().split("[ |_]")).map(word -> {
            if (word.equals("spell")) {
                return "";
            }
            String first = word.substring(0, 1);
            String rest = word.substring(1);
            return first.toUpperCase() + rest;
        }).collect(Collectors.joining(" ")).trim();
    }

    private record RecipeIngredientData(String id, String name, String path, Item item) {
        public static RecipeIngredientData EMPTY = new RecipeIngredientData("", "", "", null);
    }

    private static enum CraftingType {
        CRAFTING_TABLE,
        SMITHING_TABLE,
        NOT_CRAFTABLE;

    }
}

