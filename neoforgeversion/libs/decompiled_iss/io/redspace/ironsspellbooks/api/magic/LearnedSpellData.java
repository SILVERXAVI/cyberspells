/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.StringTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.resources.ResourceLocation
 */
package io.redspace.ironsspellbooks.api.magic;

import io.redspace.ironsspellbooks.api.network.ISerializable;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class LearnedSpellData
implements ISerializable {
    public static final String LEARNED_SPELLS = "learnedSpells";
    public final Set<ResourceLocation> learnedSpells = new HashSet<ResourceLocation>();

    public void saveToNBT(CompoundTag compound) {
        if (!this.learnedSpells.isEmpty()) {
            ListTag listTag = new ListTag();
            for (ResourceLocation resourceLocation : this.learnedSpells) {
                listTag.add((Object)StringTag.valueOf((String)resourceLocation.toString()));
            }
            compound.put(LEARNED_SPELLS, (Tag)listTag);
        }
    }

    public void loadFromNBT(CompoundTag compound) {
        ListTag learnedTag = (ListTag)compound.get(LEARNED_SPELLS);
        if (learnedTag != null && !learnedTag.isEmpty()) {
            for (Tag tag : learnedTag) {
                StringTag stringTag;
                ResourceLocation resourceLocation;
                if (!(tag instanceof StringTag) || SpellRegistry.getSpell(resourceLocation = ResourceLocation.parse((String)(stringTag = (StringTag)tag).getAsString())) == null) continue;
                this.learnedSpells.add(resourceLocation);
            }
        }
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buf) {
        buf.writeInt(this.learnedSpells.size());
        for (ResourceLocation resourceLocation : this.learnedSpells) {
            buf.writeResourceLocation(resourceLocation);
        }
    }

    @Override
    public void readFromBuffer(FriendlyByteBuf buf) {
        int i = buf.readInt();
        if (i > 0) {
            for (int j = 0; j < i; ++j) {
                ResourceLocation resourceLocation = buf.readResourceLocation();
                if (SpellRegistry.REGISTRY.get(resourceLocation) == null) continue;
                this.learnedSpells.add(resourceLocation);
            }
        }
    }
}

