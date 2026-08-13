/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.Item
 */
package io.redspace.ironsspellbooks.api.spells;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import java.util.Objects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class SpellData
implements Comparable<SpellData> {
    public static final String SPELL_ID = "id";
    public static final String SPELL_LEVEL = "level";
    public static final String SPELL_LOCKED = "locked";
    public static final Codec<SpellData> CODEC = RecordCodecBuilder.create(builder -> builder.group((App)ResourceLocation.CODEC.fieldOf(SPELL_ID).forGetter(data -> data.spell.getSpellResource()), (App)Codec.INT.fieldOf(SPELL_LEVEL).forGetter(SpellData::getLevel), (App)Codec.BOOL.optionalFieldOf(SPELL_LOCKED, (Object)false).forGetter(SpellData::isLocked)).apply((Applicative)builder, SpellData::new));
    public static final SpellData EMPTY = new SpellData(SpellRegistry.none(), 0, false);
    protected final AbstractSpell spell;
    protected final int spellLevel;
    protected final boolean locked;

    private SpellData() throws Exception {
        throw new Exception("Cannot create empty spell slots.");
    }

    public SpellData(AbstractSpell spell, int level, boolean locked) {
        this.spell = Objects.requireNonNull(spell);
        this.spellLevel = level;
        this.locked = locked;
    }

    public SpellData(AbstractSpell spell, int level) {
        this(spell, level, false);
    }

    public SpellData(ResourceLocation spellId, int level, boolean locked) {
        this(SpellRegistry.getSpell(spellId), level, locked);
    }

    public static void writeToBuffer(FriendlyByteBuf buf, SpellData data) {
        buf.writeResourceLocation(data.spell.getSpellResource());
        buf.writeInt(data.spellLevel);
        buf.writeBoolean(data.locked);
    }

    public static SpellData readFromBuffer(FriendlyByteBuf buf) {
        return new SpellData(buf.readResourceLocation(), buf.readInt(), buf.readBoolean());
    }

    public AbstractSpell getSpell() {
        return this.spell == null ? SpellRegistry.none() : this.spell;
    }

    public int getLevel() {
        return this.spellLevel;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public boolean canRemove() {
        return !this.locked;
    }

    public SpellRarity getRarity() {
        return this.getSpell().getRarity(this.getLevel());
    }

    public Component getDisplayName() {
        return this.getSpell().getDisplayName(MinecraftInstanceHelper.instance.player()).append(" ").append((Component)Component.translatable((String)((Item)ItemRegistry.SCROLL.get()).getDescriptionId()));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SpellData) {
            SpellData other = (SpellData)obj;
            return this.spell.equals(other.spell) && this.spellLevel == other.spellLevel;
        }
        return false;
    }

    public int hashCode() {
        return 31 * this.spell.hashCode() + this.spellLevel;
    }

    @Override
    public int compareTo(SpellData other) {
        int i = this.spell.getSpellId().compareTo(other.spell.getSpellId());
        if (i == 0) {
            i = Integer.compare(this.spellLevel, other.spellLevel);
        }
        return i;
    }
}

