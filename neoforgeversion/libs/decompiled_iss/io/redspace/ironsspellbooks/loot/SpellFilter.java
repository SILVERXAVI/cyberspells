/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  net.minecraft.util.RandomSource
 */
package io.redspace.ironsspellbooks.loot;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.util.RandomSource;

public class SpellFilter {
    SchoolType schoolType = null;
    List<AbstractSpell> spells = new ArrayList<AbstractSpell>();
    final boolean force;
    static final Map<SchoolType, List<AbstractSpell>> SPELLS_FOR_SCHOOL = new HashMap<SchoolType, List<AbstractSpell>>();
    static final Map<SchoolType, List<AbstractSpell>> SPELLS_FOR_SCHOOL_FORCED = new HashMap<SchoolType, List<AbstractSpell>>();
    private static final Codec<SpellFilter> SCHOOL_CODEC = RecordCodecBuilder.create(builder -> builder.group((App)Codec.BOOL.optionalFieldOf("force", (Object)false).forGetter(f -> f.force), (App)SchoolRegistry.REGISTRY.byNameCodec().fieldOf("school").forGetter(f -> f.schoolType)).apply((Applicative)builder, SpellFilter::new));
    private static final Codec<SpellFilter> SPELLS_CODEC = RecordCodecBuilder.create(builder -> builder.group((App)Codec.BOOL.optionalFieldOf("force", (Object)false).forGetter(f -> f.force), (App)Codec.list((Codec)SpellRegistry.REGISTRY.byNameCodec()).fieldOf("spells").forGetter(f -> f.spells)).apply((Applicative)builder, SpellFilter::new));
    private static final Codec<SpellFilter> NO_FILTER_CODEC = Codec.unit((Object)new SpellFilter());
    public static final Codec<SpellFilter> CODEC = Codec.withAlternative(SCHOOL_CODEC, SPELLS_CODEC);

    public SpellFilter(boolean force, SchoolType schoolType) {
        this.force = force;
        this.schoolType = schoolType;
    }

    public SpellFilter(SchoolType type) {
        this(false, type);
    }

    public SpellFilter(boolean force, List<AbstractSpell> spells) {
        this.force = force;
        this.spells = spells;
    }

    public SpellFilter(List<AbstractSpell> spells) {
        this(false, spells);
    }

    public SpellFilter() {
        this.force = false;
    }

    private boolean isSpellAllowed(AbstractSpell spell) {
        return spell.isEnabled() && (this.force || spell.allowLooting());
    }

    public List<AbstractSpell> getApplicableSpells() {
        if (!this.spells.isEmpty()) {
            return this.spells.stream().filter(AbstractSpell::isEnabled).toList();
        }
        if (this.schoolType != null) {
            if (this.force) {
                return SPELLS_FOR_SCHOOL_FORCED.computeIfAbsent(this.schoolType, school -> SpellRegistry.getSpellsForSchool(school).stream().filter(AbstractSpell::isEnabled).toList());
            }
            return SPELLS_FOR_SCHOOL.computeIfAbsent(this.schoolType, school -> SpellRegistry.getSpellsForSchool(school).stream().filter(this::isSpellAllowed).toList());
        }
        return SpellRegistry.getEnabledSpells().stream().filter(this::isSpellAllowed).toList();
    }

    public AbstractSpell getRandomSpell(RandomSource random, Predicate<AbstractSpell> filter) {
        List<AbstractSpell> spells = this.getApplicableSpells().stream().filter(filter).toList();
        if (spells.isEmpty()) {
            return SpellRegistry.none();
        }
        return spells.get(random.nextInt(spells.size()));
    }

    public AbstractSpell getRandomSpell(RandomSource randomSource) {
        return this.getRandomSpell(randomSource, spell -> spell.isEnabled() && spell != SpellRegistry.none() && spell.allowLooting());
    }
}

