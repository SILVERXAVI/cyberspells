/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 */
package io.redspace.ironsspellbooks.item;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.item.ReadableLoreItem;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Stack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ChronicleItem
extends ReadableLoreItem {
    private List<Component> chronicleCache;
    private LocalDate lastCachedDate;

    public ChronicleItem(Item.Properties pProperties) {
        super(IronsSpellbooks.id("textures/entity/lectern/archevoker_logbook.png"), pProperties);
    }

    @Override
    public Optional<ResourceLocation> simpleTextureOverride(ItemStack stack) {
        return Optional.empty();
    }

    @Override
    public List<Component> getPages(ItemStack stack) {
        if (this.chronicleCache == null || this.lastCachedDate != null && this.lastCachedDate.isBefore(LocalDate.now().minusDays(1L))) {
            this.chronicleCache = new ArrayList<Component>();
            ArrayList<MutableComponent> lostSouls = new ArrayList<MutableComponent>();
            ArrayList<MutableComponent> faithfulSouls = new ArrayList<MutableComponent>();
            ArrayList<MutableComponent> loyalSouls = new ArrayList<MutableComponent>();
            boolean success = this.resolveChronicleData(lostSouls, faithfulSouls, loyalSouls);
            if (!success) {
                this.chronicleCache.add((Component)Component.literal((String)"Failed to fetch Patreon Data :(").withStyle(ChatFormatting.RED));
                return this.chronicleCache;
            }
            Stack<MutableComponent> pages = new Stack<MutableComponent>();
            MutableComponent loyalPage = Component.translatable((String)"item.irons_spellbooks.chronicle.chapter", (Object[])new Object[]{1}).withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE).withBold(Boolean.valueOf(true)).withUnderlined(Boolean.valueOf(false))).append((Component)Component.translatable((String)"item.irons_spellbooks.chronicle.chapter_1").withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE).withBold(Boolean.valueOf(true)).withUnderlined(Boolean.valueOf(true))));
            loyalPage.append("\n\n");
            pages.push(loyalPage);
            this.createChapterPages(pages, loyalSouls);
            MutableComponent chroniclersPage = Component.translatable((String)"item.irons_spellbooks.chronicle.chapter", (Object[])new Object[]{2}).withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE).withBold(Boolean.valueOf(true)).withUnderlined(Boolean.valueOf(false))).append((Component)Component.translatable((String)"item.irons_spellbooks.chronicle.chapter_2").withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE).withBold(Boolean.valueOf(true)).withUnderlined(Boolean.valueOf(true))));
            chroniclersPage.append("\n\n");
            pages.push(chroniclersPage);
            this.createChapterPages(pages, faithfulSouls);
            MutableComponent lostPage = Component.translatable((String)"item.irons_spellbooks.chronicle.chapter", (Object[])new Object[]{3}).withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE).withBold(Boolean.valueOf(true)).withUnderlined(Boolean.valueOf(false))).append((Component)Component.translatable((String)"item.irons_spellbooks.chronicle.chapter_3").withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE).withBold(Boolean.valueOf(true)).withUnderlined(Boolean.valueOf(true))));
            lostPage.append("\n\n");
            pages.push(lostPage);
            this.createChapterPages(pages, lostSouls);
            this.chronicleCache.addAll(pages);
        }
        return this.chronicleCache;
    }

    public void clearCache() {
        this.chronicleCache = null;
    }

    private boolean resolveChronicleData(List<MutableComponent> lostSouls, List<MutableComponent> faithfulSouls, List<MutableComponent> loyalSouls) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URI("https://code.redspace.io/data/chronicle_data.json").toURL().openStream()));){
            JsonObject json = (JsonObject)new Gson().fromJson((Reader)reader, JsonObject.class);
            int format = json.get("format").getAsInt();
            if (format != 1) {
                throw new IllegalStateException("Unsupported data format: " + format);
            }
            this.lastCachedDate = LocalDate.now();
            int entry = 0;
            JsonArray entries = json.getAsJsonArray("values");
            for (JsonElement e : entries) {
                try {
                    ++entry;
                    JsonObject object = e.getAsJsonObject();
                    int bookCategory = object.get("category").getAsInt();
                    int activeTier = object.get("type").getAsInt();
                    String name = object.get("name").getAsString();
                    Style style = switch (activeTier) {
                        case 2 -> Style.EMPTY.withColor(14645504).withBold(Boolean.valueOf(true)).withUnderlined(Boolean.valueOf(false));
                        case 3 -> Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE).withBold(Boolean.valueOf(true)).withUnderlined(Boolean.valueOf(false));
                        default -> Style.EMPTY.withColor(10376448).withBold(Boolean.valueOf(false)).withUnderlined(Boolean.valueOf(false));
                    };
                    MutableComponent component = Component.literal((String)name).withStyle(style);
                    switch (bookCategory) {
                        case 0: {
                            lostSouls.add(component);
                            break;
                        }
                        case 1: {
                            faithfulSouls.add(component);
                            break;
                        }
                        case 2: {
                            loyalSouls.add(component);
                        }
                    }
                }
                catch (Exception exception) {
                    IronsSpellbooks.LOGGER.error("Failed to handle chronicle member entry {}: {}", (Object)entry, (Object)exception.getMessage());
                }
            }
            reader.close();
        }
        catch (Exception ex) {
            IronsSpellbooks.LOGGER.error("Failed to handle Chronicle Data: {}", (Object)ex.toString());
            return false;
        }
        Comparator<MutableComponent> comparator = Comparator.comparing(c -> c.getString().toLowerCase(Locale.ROOT));
        lostSouls.sort(comparator);
        faithfulSouls.sort(comparator);
        loyalSouls.sort(comparator);
        return true;
    }

    private void createChapterPages(Stack<MutableComponent> pages, List<MutableComponent> entries) {
        int linecount = 3;
        int charWidth = 6;
        int bookLimit = 114;
        for (Component component : entries) {
            int estLines = component.getString().length() * charWidth / bookLimit + 1;
            if ((linecount += estLines) > 13) {
                MutableComponent nextPage = Component.empty();
                pages.push(nextPage);
                linecount = estLines;
            }
            pages.peek().append(component).append("\n");
        }
    }
}

