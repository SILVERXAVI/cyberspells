/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.resources.ResourceLocation
 *  org.apache.commons.lang3.ArrayUtils
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.capabilities.magic;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainerMutable;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.spells.SpellSlot;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

public class SpellContainer
implements ISpellContainer {
    public static final String SPELL_SLOT_CONTAINER = "ISB_Spells";
    public static final String SPELL_DATA = "data";
    public static final String MAX_SLOTS = "maxSpells";
    public static final String MUST_EQUIP = "mustEquip";
    public static final String IMPROVED = "improved";
    public static final String SPELL_WHEEL = "spellWheel";
    public static final String SLOT_INDEX = "index";
    public static final String SPELL_ID = "id";
    public static final String SPELL_LEVEL = "level";
    public static final String SPELL_LOCKED = "locked";
    SpellSlot[] slots;
    int maxSpells = 0;
    int activeSlots = 0;
    boolean spellWheel = false;
    boolean mustEquip = true;
    boolean improved = false;
    public static final Codec<SpellSlot> SPELL_SLOT_CODEC = RecordCodecBuilder.create(builder -> builder.group((App)ResourceLocation.CODEC.fieldOf(SPELL_ID).forGetter(data -> data.getSpell().getSpellResource()), (App)Codec.INT.fieldOf(SLOT_INDEX).forGetter(SpellSlot::index), (App)Codec.INT.fieldOf(SPELL_LEVEL).forGetter(SpellSlot::getLevel), (App)Codec.BOOL.optionalFieldOf(SPELL_LOCKED, (Object)false).forGetter(SpellSlot::isLocked)).apply((Applicative)builder, (id, index, lvl, lock) -> SpellSlot.of(new SpellData((ResourceLocation)id, (int)lvl, (boolean)lock), index)));
    public static final Codec<ISpellContainer> CODEC = RecordCodecBuilder.create(builder -> builder.group((App)Codec.INT.fieldOf(MAX_SLOTS).forGetter(ISpellContainer::getMaxSpellCount), (App)Codec.BOOL.fieldOf(SPELL_WHEEL).forGetter(ISpellContainer::isSpellWheel), (App)Codec.BOOL.fieldOf(MUST_EQUIP).forGetter(ISpellContainer::mustEquip), (App)Codec.BOOL.optionalFieldOf(IMPROVED, (Object)false).forGetter(ISpellContainer::isImproved), (App)Codec.list(SPELL_SLOT_CODEC).fieldOf(SPELL_DATA).forGetter(ISpellContainer::getActiveSpells)).apply((Applicative)builder, (count, wheel, equip, improved, spells) -> {
        SpellContainer container = new SpellContainer((int)count, (boolean)wheel, (boolean)equip, (boolean)improved);
        spells.forEach(slot -> {
            container.slots[slot.index()] = slot;
        });
        container.activeSlots = spells.size();
        return container;
    }));
    public static final StreamCodec<FriendlyByteBuf, ISpellContainer> STREAM_CODEC = StreamCodec.of((buf, container) -> {
        buf.writeInt(container.getMaxSpellCount());
        buf.writeBoolean(container.isSpellWheel());
        buf.writeBoolean(container.mustEquip());
        buf.writeBoolean(container.isImproved());
        List<SpellSlot> spells = container.getActiveSpells();
        int i = spells.size();
        buf.writeInt(i);
        for (int j = 0; j < i; ++j) {
            SpellSlot spell = spells.get(j);
            SpellData.writeToBuffer(buf, spell.spellData());
            buf.writeInt(spell.index());
        }
    }, buf -> {
        int count = buf.readInt();
        boolean wheel = buf.readBoolean();
        boolean equip = buf.readBoolean();
        boolean improved = buf.readBoolean();
        int i = buf.readInt();
        SpellContainer container = new SpellContainer(count, wheel, equip, improved);
        for (int j = 0; j < i; ++j) {
            SpellSlot spell;
            container.slots[spell.index()] = spell = new SpellSlot(SpellData.readFromBuffer(buf), buf.readInt());
        }
        container.activeSlots = i;
        return container;
    });

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof SpellContainer)) return false;
        SpellContainer o = (SpellContainer)obj;
        if (!Arrays.equals(o.slots, this.slots)) return false;
        if (this.maxSpells != o.maxSpells) return false;
        if (this.activeSlots != o.activeSlots) return false;
        if (this.spellWheel != o.spellWheel) return false;
        if (this.mustEquip != o.mustEquip) return false;
        if (this.improved != o.improved) return false;
        return true;
    }

    public int hashCode() {
        int hash = Arrays.hashCode(this.slots);
        hash = (hash * 31 + this.maxSpells) * 31 + this.activeSlots;
        hash *= 1000;
        hash += this.spellWheel ? 100 : 0;
        hash += this.mustEquip ? 10 : 0;
        return hash += this.improved ? 1 : 0;
    }

    public SpellContainer() {
    }

    public SpellContainer(int maxSpells, boolean spellWheel, boolean mustEquip) {
        this(maxSpells, spellWheel, mustEquip, false);
    }

    public SpellContainer(int maxSpells, boolean spellWheel, boolean mustEquip, boolean improved) {
        this.maxSpells = maxSpells;
        this.slots = new SpellSlot[this.maxSpells];
        this.spellWheel = spellWheel;
        this.mustEquip = mustEquip;
        this.improved = improved;
    }

    public SpellContainer(int maxSpells, boolean spellWheel, boolean mustEquip, boolean improved, SpellSlot[] slots) {
        this.maxSpells = maxSpells;
        this.slots = slots;
        this.spellWheel = spellWheel;
        this.mustEquip = mustEquip;
        this.improved = improved;
        this.activeSlots = Arrays.stream(slots).filter(Objects::nonNull).toList().size();
    }

    @Override
    public int getMaxSpellCount() {
        return this.maxSpells;
    }

    @Override
    public int getActiveSpellCount() {
        return this.activeSlots;
    }

    @Override
    public boolean isEmpty() {
        return this.activeSlots == 0;
    }

    @Override
    public SpellSlot[] getAllSpells() {
        SpellSlot[] result = new SpellSlot[this.maxSpells];
        if (this.maxSpells > 0) {
            System.arraycopy(this.slots, 0, result, 0, this.slots.length);
        }
        return result;
    }

    @Override
    @NotNull
    public List<SpellSlot> getActiveSpells() {
        return Arrays.stream(this.slots).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public int getNextAvailableIndex() {
        return ArrayUtils.indexOf((Object[])this.slots, null);
    }

    @Override
    public boolean mustEquip() {
        return this.mustEquip;
    }

    @Override
    public boolean isImproved() {
        return this.improved;
    }

    @Override
    public boolean isSpellWheel() {
        return this.spellWheel;
    }

    @Override
    @NotNull
    public SpellData getSpellAtIndex(int index) {
        SpellSlot result;
        if (index >= 0 && index < this.maxSpells && (result = this.slots[index]) != null) {
            return this.slots[index].spellData();
        }
        return SpellData.EMPTY;
    }

    @Override
    public int getIndexForSpell(AbstractSpell spell) {
        for (int i = 0; i < this.maxSpells; ++i) {
            SpellSlot s = this.slots[i];
            if (s == null || !s.getSpell().equals(spell)) continue;
            return i;
        }
        return -1;
    }

    @Override
    public ISpellContainerMutable mutableCopy() {
        return new Mutable(this, this);
    }

    public class Mutable
    extends SpellContainer
    implements ISpellContainerMutable {
        public Mutable(SpellContainer this$0, SpellContainer spellContainer) {
            this.maxSpells = spellContainer.maxSpells;
            this.activeSlots = spellContainer.activeSlots;
            this.spellWheel = spellContainer.spellWheel;
            this.mustEquip = spellContainer.mustEquip;
            this.improved = spellContainer.improved;
            this.slots = Arrays.copyOf(spellContainer.slots, spellContainer.slots.length);
        }

        @Override
        public void setMaxSpellCount(int maxSpells) {
            this.maxSpells = maxSpells;
            this.slots = Arrays.copyOf(this.slots, maxSpells);
        }

        @Override
        public void setImproved(boolean improved) {
            this.improved = improved;
        }

        @Override
        public boolean addSpellAtIndex(AbstractSpell spell, int level, int index, boolean locked) {
            if (index > -1 && index < this.maxSpells && this.slots[index] == null && Arrays.stream(this.slots).noneMatch(s -> s != null && s.getSpell().equals(spell))) {
                this.slots[index] = SpellSlot.of(new SpellData(spell, level, locked), index);
                ++this.activeSlots;
                return true;
            }
            return false;
        }

        @Override
        public boolean addSpell(AbstractSpell spell, int level, boolean locked) {
            return this.addSpellAtIndex(spell, level, this.getNextAvailableIndex(), locked);
        }

        @Override
        public boolean removeSpellAtIndex(int index) {
            if (index > -1 && index < this.maxSpells && this.slots[index] != null) {
                this.slots[index] = null;
                --this.activeSlots;
                return true;
            }
            return false;
        }

        @Override
        public boolean removeSpell(AbstractSpell spell) {
            SpellSlot spellData;
            if (spell == null) {
                return false;
            }
            int i = 0;
            if (i < this.maxSpells && (spellData = this.slots[i]) != null && spell.equals(spellData.getSpell())) {
                return this.removeSpellAtIndex(i);
            }
            return false;
        }

        @Override
        public ISpellContainer toImmutable() {
            return new SpellContainer(this.maxSpells, this.spellWheel, this.mustEquip, this.improved, this.slots);
        }
    }
}

