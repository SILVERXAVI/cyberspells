/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  net.minecraft.resources.ResourceLocation
 */
package io.redspace.ironsspellbooks.api.config;

import com.mojang.serialization.Codec;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;

public record SpellConfigParameter<T>(ResourceLocation key, Codec<T> datatype, Supplier<T> defaultValue) {
    public static final SpellConfigParameter<SchoolType> SCHOOL = new SpellConfigParameter<SchoolType>(IronsSpellbooks.id("school"), SchoolRegistry.REGISTRY.byNameCodec(), SchoolRegistry.EVOCATION);
    public static final SpellConfigParameter<SpellRarity> MIN_RARITY = new SpellConfigParameter<SpellRarity>(IronsSpellbooks.id("min_rarity"), SpellRarity.CODEC, SpellRarity.COMMON);
    public static final SpellConfigParameter<Integer> MAX_LEVEL = new SpellConfigParameter<Integer>(IronsSpellbooks.id("max_level"), (Codec<Integer>)Codec.INT, 1);
    public static final SpellConfigParameter<Boolean> ENABLED = new SpellConfigParameter<Boolean>(IronsSpellbooks.id("enabled"), (Codec<Boolean>)Codec.BOOL, true);
    public static final SpellConfigParameter<Double> COOLDOWN_IN_SECONDS = new SpellConfigParameter<Double>(IronsSpellbooks.id("cooldown_in_seconds"), (Codec<Double>)Codec.DOUBLE, 10.0);
    public static final SpellConfigParameter<Boolean> ALLOW_CRAFTING = new SpellConfigParameter<Boolean>(IronsSpellbooks.id("allow_crafting"), (Codec<Boolean>)Codec.BOOL, true);
    public static final SpellConfigParameter<Double> POWER_MULTIPLIER = new SpellConfigParameter<Double>(IronsSpellbooks.id("power_multiplier"), (Codec<Double>)Codec.DOUBLE, 1.0);
    public static final SpellConfigParameter<Double> MANA_MULTIPLIER = new SpellConfigParameter<Double>(IronsSpellbooks.id("mana_cost_multiplier"), (Codec<Double>)Codec.DOUBLE, 1.0);

    public SpellConfigParameter(ResourceLocation key, Codec<T> datatype, T defaultValue) {
        this(key, datatype, () -> defaultValue);
    }
}

