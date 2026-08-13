/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.util.concurrent.AtomicDouble
 *  com.mojang.serialization.Codec
 *  java.lang.MatchException
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.util.StringRepresentable
 */
package io.redspace.ironsspellbooks.api.spells;

import com.google.common.util.concurrent.AtomicDouble;
import com.mojang.serialization.Codec;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringRepresentable;

public enum SpellRarity implements StringRepresentable
{
    COMMON(0),
    UNCOMMON(1),
    RARE(2),
    EPIC(3),
    LEGENDARY(4);

    private final int value;
    public static final Codec<SpellRarity> CODEC;
    private static List<Double> rawRarityConfig;
    private static List<Double> rarityConfig;
    private final MutableComponent[] DISPLAYS = new MutableComponent[]{Component.translatable((String)"rarity.irons_spellbooks.common").withStyle(ChatFormatting.GRAY), Component.translatable((String)"rarity.irons_spellbooks.uncommon").withStyle(ChatFormatting.GREEN), Component.translatable((String)"rarity.irons_spellbooks.rare").withStyle(ChatFormatting.AQUA), Component.translatable((String)"rarity.irons_spellbooks.epic").withStyle(ChatFormatting.LIGHT_PURPLE), Component.translatable((String)"rarity.irons_spellbooks.legendary").withStyle(ChatFormatting.GOLD), Component.translatable((String)"rarity.irons_spellbooks.mythic").withStyle(ChatFormatting.GOLD), Component.translatable((String)"rarity.irons_spellbooks.ancient").withStyle(ChatFormatting.GOLD)};

    private SpellRarity(int newValue) {
        this.value = newValue;
    }

    public int getValue() {
        return this.value;
    }

    public MutableComponent getDisplayName() {
        return this.DISPLAYS[this.getValue()];
    }

    public static List<Double> getRawRarityConfig() {
        if (rarityConfig == null) {
            rawRarityConfig = SpellRarity.getRawRarityConfigInternal();
        }
        return rawRarityConfig;
    }

    private static List<Double> getRawRarityConfigInternal() {
        List fromConfig = (List)ServerConfigs.RARITY_CONFIG.get();
        if (fromConfig.size() != 5) {
            List configDefault = (List)ServerConfigs.RARITY_CONFIG.getDefault();
            IronsSpellbooks.LOGGER.info("INVALID RARITY CONFIG FOUND (Size != 5): {} FALLING BACK TO DEFAULT: {}", (Object)fromConfig, (Object)configDefault);
            return configDefault;
        }
        if (fromConfig.stream().mapToDouble(a -> a).sum() != 1.0) {
            List configDefault = (List)ServerConfigs.RARITY_CONFIG.getDefault();
            IronsSpellbooks.LOGGER.info("INVALID RARITY CONFIG FOUND (Values must add up to 1): {} FALLING BACK TO DEFAULT: {}", (Object)fromConfig, (Object)configDefault);
            return configDefault;
        }
        return fromConfig;
    }

    public static List<Double> getRarityConfig() {
        if (rarityConfig == null) {
            AtomicDouble counter = new AtomicDouble();
            rarityConfig = new ArrayList<Double>();
            SpellRarity.getRawRarityConfig().forEach(item -> rarityConfig.add(counter.addAndGet(item.doubleValue())));
        }
        return rarityConfig;
    }

    public int compareRarity(SpellRarity other) {
        return Integer.compare(this.getValue(), other.getValue());
    }

    public static void rarityTest() {
        StringBuilder sb = new StringBuilder();
        SpellRegistry.REGISTRY.forEach(s -> {
            int i;
            sb.append(String.format("\nSpellType:%s\n", s));
            sb.append(String.format("\tMinRarity:%s, MaxRarity:%s\n", s.getMinRarity(), s.getMaxRarity()));
            sb.append(String.format("\tMinLevel:%s, MaxLevel:%s\n", s.getMinLevel(), s.getMaxLevel()));
            sb.append(String.format("\tRawRarityConfig:%s\n", SpellRarity.getRawRarityConfig().stream().map(Object::toString).collect(Collectors.joining(","))));
            sb.append(String.format("\tRarityConfig:%s\n", SpellRarity.getRarityConfig().stream().map(Object::toString).collect(Collectors.joining(","))));
            for (i = s.getMinLevel(); i <= s.getMaxLevel(); ++i) {
                List<Double> rarityConfig = SpellRarity.getRawRarityConfig();
                double d = (double)i / (double)s.getMaxLevel();
                int start = s.getMinRarity();
                int end = s.getMaxRarity();
                List<Double> modifiedRarityBrackets = rarityConfig.subList(start, end + 1);
                double total = modifiedRarityBrackets.stream().mapToDouble(a -> a).sum();
                double current = 0.0;
                SpellRarity rarity = null;
                for (int j = 0; j < modifiedRarityBrackets.size(); ++j) {
                    if (!(d <= (current += modifiedRarityBrackets.get(j) / total))) continue;
                    rarity = SpellRarity.values()[j + s.getMinRarity()];
                    break;
                }
                if (rarity == null) {
                    throw new RuntimeException();
                }
                sb.append(String.format("\t\tLevel %s -> %s\n", new Object[]{i, s.getRarity(i)}));
                sb.append(String.format("\t\tTESTL %s -> %s\n", new Object[]{i, rarity}));
                sb.append(String.format("\t\tEQUAL:%s\n", rarity == s.getRarity(i)));
            }
            sb.append("\n");
            for (i = s.getMinRarity(); i <= s.getMaxRarity(); ++i) {
                sb.append(String.format("\t\t%s -> Level %s\n", new Object[]{SpellRarity.values()[i], s.getMinLevelForRarity(SpellRarity.values()[i])}));
            }
        });
        IronsSpellbooks.LOGGER.debug(sb.toString());
    }

    public ChatFormatting getChatFormatting() {
        return switch (this.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> ChatFormatting.GRAY;
            case 1 -> ChatFormatting.GREEN;
            case 2 -> ChatFormatting.AQUA;
            case 3 -> ChatFormatting.LIGHT_PURPLE;
            case 4 -> ChatFormatting.GOLD;
        };
    }

    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    static {
        CODEC = StringRepresentable.fromEnum(SpellRarity::values);
        rarityConfig = null;
    }
}

