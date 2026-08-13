/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  com.mojang.serialization.codecs.RecordCodecBuilder$Instance
 *  java.lang.MatchException
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.storage.loot.LootContext
 *  net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction
 *  net.minecraft.world.level.storage.loot.functions.LootItemFunctionType
 *  net.minecraft.world.level.storage.loot.predicates.LootItemCondition
 *  net.minecraft.world.level.storage.loot.providers.number.NumberProvider
 *  net.minecraft.world.level.storage.loot.providers.number.NumberProviders
 */
package io.redspace.ironsspellbooks.loot;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.loot.SpellFilter;
import io.redspace.ironsspellbooks.registries.LootRegistry;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

public class RandomizeSpellFunction
extends LootItemConditionalFunction {
    final NumberProvider qualityRange;
    final SpellFilter applicableSpells;
    public static final MapCodec<RandomizeSpellFunction> CODEC = RecordCodecBuilder.mapCodec(builder -> RandomizeSpellFunction.commonFields((RecordCodecBuilder.Instance)builder).and(builder.group((App)NumberProviders.CODEC.fieldOf("quality").forGetter(data -> data.qualityRange), (App)SpellFilter.CODEC.optionalFieldOf("spell_filter", (Object)new SpellFilter()).forGetter(data -> data.applicableSpells))).apply((Applicative)builder, RandomizeSpellFunction::new));

    protected RandomizeSpellFunction(List<LootItemCondition> lootConditions, NumberProvider qualityRange, SpellFilter spellFilter) {
        super(lootConditions);
        this.qualityRange = qualityRange;
        this.applicableSpells = spellFilter;
    }

    protected ItemStack run(ItemStack itemStack, LootContext lootContext) {
        if (itemStack.getItem() instanceof Scroll || Utils.canImbue(itemStack)) {
            ItemStack fallback = itemStack.getItem() instanceof Scroll ? ItemStack.EMPTY : itemStack;
            List<AbstractSpell> applicableSpells = this.applicableSpells.getApplicableSpells();
            if (applicableSpells.isEmpty()) {
                return fallback;
            }
            NavigableMap<Integer, AbstractSpell> spellList = this.getWeightedSpellList(applicableSpells);
            int total = spellList.floorKey(Integer.MAX_VALUE);
            AbstractSpell spell = spellList.higherEntry(lootContext.getRandom().nextInt(total)).getValue();
            if (spell.equals(SpellRegistry.none())) {
                return fallback;
            }
            int maxLevel = spell.getMaxLevel();
            float quality = this.qualityRange.getFloat(lootContext);
            int spellLevel = 1 + Math.round(quality * (float)(maxLevel - 1));
            if (itemStack.getItem() instanceof Scroll) {
                ISpellContainer.createScrollContainer(spell, spellLevel, itemStack);
            } else {
                ISpellContainer.createImbuedContainer(spell, spellLevel, itemStack);
            }
        }
        return itemStack;
    }

    private NavigableMap<Integer, AbstractSpell> getWeightedSpellList(List<AbstractSpell> entries) {
        int total = 0;
        TreeMap<Integer, AbstractSpell> weightedSpells = new TreeMap<Integer, AbstractSpell>();
        for (AbstractSpell entry : entries) {
            weightedSpells.put(total += this.getWeightFromRarity(SpellRarity.values()[entry.getMinRarity()]), entry);
        }
        return weightedSpells;
    }

    private int getWeightFromRarity(SpellRarity rarity) {
        return switch (rarity) {
            default -> throw new MatchException(null, null);
            case SpellRarity.COMMON -> 40;
            case SpellRarity.UNCOMMON -> 30;
            case SpellRarity.RARE -> 15;
            case SpellRarity.EPIC -> 8;
            case SpellRarity.LEGENDARY -> 4;
        };
    }

    public LootItemFunctionType getType() {
        return LootRegistry.RANDOMIZE_SPELL_FUNCTION.get();
    }
}

