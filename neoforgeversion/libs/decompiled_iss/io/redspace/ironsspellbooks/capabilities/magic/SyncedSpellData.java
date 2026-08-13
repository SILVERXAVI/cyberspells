/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.neoforged.neoforge.network.PacketDistributor
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.capabilities.magic;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.LearnedSpellData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.gui.overlays.SpellSelection;
import io.redspace.ironsspellbooks.network.casting.SyncEntityDataPacket;
import io.redspace.ironsspellbooks.network.casting.SyncPlayerDataPacket;
import io.redspace.ironsspellbooks.player.SpinAttackType;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class SyncedSpellData {
    private final int serverPlayerId;
    @Nullable
    private LivingEntity livingEntity = null;
    private boolean isCasting;
    private String castingSpellId;
    private int castingSpellLevel;
    private float heartStopAccumulatedDamage;
    private int evasionHitsRemaining;
    private SpinAttackType spinAttackType;
    private LearnedSpellData learnedSpellData;
    private SpellSelection spellSelection;
    private String castingEquipmentSlot;

    public SyncedSpellData(int serverPlayerId) {
        this.serverPlayerId = serverPlayerId;
        this.isCasting = false;
        this.castingSpellId = "";
        this.castingEquipmentSlot = "";
        this.castingSpellLevel = 0;
        this.heartStopAccumulatedDamage = 0.0f;
        this.evasionHitsRemaining = 0;
        this.spinAttackType = SpinAttackType.RIPTIDE;
        this.learnedSpellData = new LearnedSpellData();
        this.spellSelection = new SpellSelection();
    }

    public static void write(FriendlyByteBuf buffer, SyncedSpellData data) {
        buffer.writeInt(data.serverPlayerId);
        buffer.writeBoolean(data.isCasting);
        buffer.writeUtf(data.castingSpellId);
        buffer.writeInt(data.castingSpellLevel);
        buffer.writeFloat(data.heartStopAccumulatedDamage);
        buffer.writeInt(data.evasionHitsRemaining);
        buffer.writeResourceLocation(data.spinAttackType.textureId());
        buffer.writeBoolean(data.spinAttackType.fullbright());
        buffer.writeUtf(data.castingEquipmentSlot);
        data.learnedSpellData.writeToBuffer(buffer);
        data.spellSelection.writeToBuffer(buffer);
    }

    public static SyncedSpellData read(FriendlyByteBuf buffer) {
        SyncedSpellData data = new SyncedSpellData(buffer.readInt());
        data.isCasting = buffer.readBoolean();
        data.castingSpellId = buffer.readUtf();
        data.castingSpellLevel = buffer.readInt();
        data.heartStopAccumulatedDamage = buffer.readFloat();
        data.evasionHitsRemaining = buffer.readInt();
        data.spinAttackType = new SpinAttackType(buffer.readResourceLocation(), buffer.readBoolean());
        data.castingEquipmentSlot = buffer.readUtf();
        data.learnedSpellData.readFromBuffer(buffer);
        data.spellSelection.readFromBuffer(buffer);
        return data;
    }

    public SyncedSpellData(LivingEntity livingEntity) {
        this(livingEntity == null ? -1 : livingEntity.getId());
        this.livingEntity = livingEntity;
    }

    public void saveNBTData(CompoundTag compound, HolderLookup.Provider provider) {
        compound.putBoolean("isCasting", this.isCasting);
        compound.putString("castingSpellId", this.castingSpellId);
        compound.putString("castingEquipmentSlot", this.castingEquipmentSlot);
        compound.putInt("castingSpellLevel", this.castingSpellLevel);
        compound.putFloat("heartStopAccumulatedDamage", this.heartStopAccumulatedDamage);
        compound.putFloat("evasionHitsRemaining", (float)this.evasionHitsRemaining);
        this.learnedSpellData.saveToNBT(compound);
        compound.put("spellSelection", (Tag)this.spellSelection.serializeNBT(provider));
    }

    public void loadNBTData(CompoundTag compound, HolderLookup.Provider provider) {
        this.isCasting = compound.getBoolean("isCasting");
        this.castingSpellId = compound.getString("castingSpellId");
        this.castingEquipmentSlot = compound.getString("castingEquipmentSlot");
        this.castingSpellLevel = compound.getInt("castingSpellLevel");
        this.heartStopAccumulatedDamage = compound.getFloat("heartStopAccumulatedDamage");
        this.evasionHitsRemaining = compound.getInt("evasionHitsRemaining");
        this.learnedSpellData.loadFromNBT(compound);
        this.spellSelection.deserializeNBT(provider, compound.getCompound("spellSelection"));
    }

    public int getServerPlayerId() {
        return this.serverPlayerId;
    }

    public String getCastingEquipmentSlot() {
        return this.castingEquipmentSlot;
    }

    public float getHeartstopAccumulatedDamage() {
        return this.heartStopAccumulatedDamage;
    }

    public void setHeartstopAccumulatedDamage(float damage) {
        this.heartStopAccumulatedDamage = damage;
        this.doSync();
    }

    public SpellSelection getSpellSelection() {
        return this.spellSelection;
    }

    public void setSpellSelection(SpellSelection spellSelection) {
        this.spellSelection = spellSelection;
        this.doSync();
    }

    public void learnSpell(AbstractSpell spell) {
        this.learnSpell(spell, true);
    }

    public void learnSpell(AbstractSpell spell, boolean sync) {
        this.learnedSpellData.learnedSpells.add(spell.getSpellResource());
        if (sync) {
            this.doSync();
        }
    }

    public void forgetAllSpells() {
        this.learnedSpellData.learnedSpells.clear();
        this.doSync();
    }

    public boolean isSpellLearned(AbstractSpell spell) {
        return !spell.requiresLearning() || this.learnedSpellData.learnedSpells.contains(spell.getSpellResource());
    }

    public SpinAttackType getSpinAttackType() {
        return this.spinAttackType;
    }

    public void setSpinAttackType(SpinAttackType spinAttackType) {
        this.spinAttackType = spinAttackType;
        this.doSync();
    }

    public int getEvasionHitsRemaining() {
        return this.evasionHitsRemaining;
    }

    public void subtractEvasionHit() {
        --this.evasionHitsRemaining;
        this.doSync();
    }

    public void setEvasionHitsRemaining(int hitsRemaining) {
        this.evasionHitsRemaining = hitsRemaining;
        this.doSync();
    }

    public void addHeartstopDamage(float damage) {
        this.heartStopAccumulatedDamage += damage;
        this.doSync();
    }

    public void doSync() {
        LivingEntity livingEntity = this.livingEntity;
        if (livingEntity instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)livingEntity;
            PacketDistributor.sendToPlayersTrackingEntityAndSelf((Entity)serverPlayer, (CustomPacketPayload)new SyncPlayerDataPacket(this), (CustomPacketPayload[])new CustomPacketPayload[0]);
        } else {
            livingEntity = this.livingEntity;
            if (livingEntity instanceof IMagicEntity) {
                IMagicEntity abstractSpellCastingMob = (IMagicEntity)livingEntity;
                PacketDistributor.sendToPlayersTrackingEntity((Entity)this.livingEntity, (CustomPacketPayload)new SyncEntityDataPacket(this, abstractSpellCastingMob), (CustomPacketPayload[])new CustomPacketPayload[0]);
            }
        }
    }

    public void syncToPlayer(ServerPlayer serverPlayer) {
        PacketDistributor.sendToPlayer((ServerPlayer)serverPlayer, (CustomPacketPayload)new SyncPlayerDataPacket(this), (CustomPacketPayload[])new CustomPacketPayload[0]);
    }

    public void setIsCasting(boolean isCasting, String castingSpellId, int castingSpellLevel, String castingEquipmentSlot) {
        this.isCasting = isCasting;
        this.castingSpellId = castingSpellId;
        this.castingSpellLevel = castingSpellLevel;
        this.castingEquipmentSlot = castingEquipmentSlot;
        this.doSync();
    }

    public boolean isCasting() {
        return this.isCasting;
    }

    public String getCastingSpellId() {
        return this.castingSpellId;
    }

    public int getCastingSpellLevel() {
        return this.castingSpellLevel;
    }

    protected SyncedSpellData clone() {
        return new SyncedSpellData(this.livingEntity);
    }

    public String toString() {
        return String.format("isCasting:%s, spellID:%s, spellLevel:%d", this.isCasting, this.castingSpellId, this.castingSpellLevel);
    }

    public SyncedSpellData getPersistentData(ServerPlayer serverPlayer) {
        SyncedSpellData persistentData = new SyncedSpellData(this.livingEntity);
        persistentData.livingEntity = serverPlayer;
        persistentData.learnedSpellData.learnedSpells.addAll(this.learnedSpellData.learnedSpells);
        persistentData.spellSelection = this.spellSelection;
        return persistentData;
    }
}

