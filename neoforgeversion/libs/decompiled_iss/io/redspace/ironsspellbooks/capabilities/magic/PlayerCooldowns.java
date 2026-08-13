/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.level.ServerPlayer
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package io.redspace.ironsspellbooks.capabilities.magic;

import com.google.common.collect.Maps;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.CooldownInstance;
import io.redspace.ironsspellbooks.network.casting.SyncCooldownsPacket;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class PlayerCooldowns {
    public static final String SPELL_ID = "id";
    public static final String SPELL_COOLDOWN = "scd";
    public static final String COOLDOWN_REMAINING = "cdr";
    private final Map<String, CooldownInstance> spellCooldowns = Maps.newHashMap();
    private int tickBuffer = 0;

    public void setTickBuffer(int tickBuffer) {
        this.tickBuffer = tickBuffer;
    }

    public void tick(int actualTicks) {
        List<Map.Entry> spells = this.spellCooldowns.entrySet().stream().filter(x -> this.decrementCooldown((CooldownInstance)x.getValue(), actualTicks)).toList();
        spells.forEach(spell -> this.spellCooldowns.remove(spell.getKey()));
    }

    public boolean hasCooldownsActive() {
        return !this.spellCooldowns.isEmpty();
    }

    public Map<String, CooldownInstance> getSpellCooldowns() {
        return this.spellCooldowns;
    }

    public boolean removeCooldown(String spellId) {
        return this.spellCooldowns.remove(spellId) != null;
    }

    public void clearCooldowns() {
        this.spellCooldowns.clear();
    }

    public void addCooldown(AbstractSpell spell, int durationTicks) {
        this.spellCooldowns.put(spell.getSpellId(), new CooldownInstance(durationTicks));
    }

    public void addCooldown(AbstractSpell spell, int durationTicks, int remaining) {
        this.spellCooldowns.put(spell.getSpellId(), new CooldownInstance(durationTicks, remaining));
    }

    public void addCooldown(String spellID, int durationTicks) {
        this.spellCooldowns.put(spellID, new CooldownInstance(durationTicks));
    }

    public void addCooldown(String spellID, int durationTicks, int remaining) {
        this.spellCooldowns.put(spellID, new CooldownInstance(durationTicks, remaining));
    }

    public boolean isOnCooldown(AbstractSpell spell) {
        return this.spellCooldowns.containsKey(spell.getSpellId());
    }

    public float getCooldownPercent(AbstractSpell spell) {
        return this.spellCooldowns.getOrDefault(spell.getSpellId(), new CooldownInstance(0)).getCooldownPercent();
    }

    public boolean decrementCooldown(CooldownInstance c, int amount) {
        c.decrementBy(amount);
        return c.getCooldownRemaining() <= this.tickBuffer;
    }

    public void syncToPlayer(ServerPlayer serverPlayer) {
        PacketDistributor.sendToPlayer((ServerPlayer)serverPlayer, (CustomPacketPayload)new SyncCooldownsPacket(this.spellCooldowns), (CustomPacketPayload[])new CustomPacketPayload[0]);
    }

    public ListTag saveNBTData() {
        ListTag listTag = new ListTag();
        this.spellCooldowns.forEach((spellId, cooldown) -> {
            if (cooldown.getCooldownRemaining() > 0) {
                CompoundTag ct = new CompoundTag();
                ct.putString(SPELL_ID, spellId);
                ct.putInt(SPELL_COOLDOWN, cooldown.getSpellCooldown());
                ct.putInt(COOLDOWN_REMAINING, cooldown.getCooldownRemaining());
                listTag.add((Object)ct);
            }
        });
        return listTag;
    }

    public void loadNBTData(ListTag listTag) {
        if (listTag != null) {
            listTag.forEach(tag -> {
                CompoundTag t = (CompoundTag)tag;
                String spellId = t.getString(SPELL_ID);
                int spellCooldown = t.getInt(SPELL_COOLDOWN);
                int cooldownRemaining = t.getInt(COOLDOWN_REMAINING);
                this.spellCooldowns.put(spellId, new CooldownInstance(spellCooldown, cooldownRemaining));
            });
        }
    }
}

