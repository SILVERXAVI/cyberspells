/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  net.neoforged.bus.api.Event
 *  net.neoforged.neoforge.common.NeoForge
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.api.magic;

import io.redspace.ironsspellbooks.api.events.ChangeManaEvent;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerCooldowns;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerRecasts;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.registries.DataAttachmentRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

public class MagicData {
    private boolean isMob = false;
    private ServerPlayer serverPlayer = null;
    public static final String MANA = "mana";
    public static final String COOLDOWNS = "cooldowns";
    public static final String RECASTS = "recasts";
    private float mana;
    private SyncedSpellData syncedSpellData;
    private int castingSpellLevel = 0;
    private int castDuration = 0;
    private int castDurationRemaining = 0;
    private CastSource castSource;
    private CastType castType;
    @Nullable
    private ICastData additionalCastData;
    private int poisonedTimestamp;
    private ItemStack castingItemStack = ItemStack.EMPTY;
    private final PlayerCooldowns playerCooldowns = new PlayerCooldowns();
    private PlayerRecasts playerRecasts = new PlayerRecasts();

    public MagicData(boolean isMob) {
        this.isMob = isMob;
    }

    public MagicData() {
        this(false);
    }

    public MagicData(ServerPlayer serverPlayer) {
        this(false);
        this.serverPlayer = serverPlayer;
        this.playerRecasts = new PlayerRecasts(serverPlayer);
    }

    public void setServerPlayer(ServerPlayer serverPlayer) {
        if (this.serverPlayer == null && serverPlayer != null) {
            this.serverPlayer = serverPlayer;
            this.playerRecasts = new PlayerRecasts(serverPlayer);
        }
    }

    public float getMana() {
        return this.mana;
    }

    public void setMana(float mana) {
        float maxMana;
        ChangeManaEvent e = new ChangeManaEvent((Player)this.serverPlayer, this, this.mana, mana);
        if (this.serverPlayer == null || !((ChangeManaEvent)NeoForge.EVENT_BUS.post((Event)e)).isCanceled()) {
            this.mana = e.getNewMana();
        }
        if (this.serverPlayer != null && this.mana > (maxMana = (float)this.serverPlayer.getAttributeValue(AttributeRegistry.MAX_MANA))) {
            this.mana = maxMana;
        }
    }

    public void addMana(float mana) {
        this.setMana(this.mana + mana);
    }

    public SyncedSpellData getSyncedData() {
        if (this.syncedSpellData == null) {
            this.syncedSpellData = new SyncedSpellData((LivingEntity)this.serverPlayer);
        }
        return this.syncedSpellData;
    }

    public void setSyncedData(SyncedSpellData syncedSpellData) {
        this.syncedSpellData = syncedSpellData;
    }

    public void resetCastingState() {
        this.castingSpellLevel = 0;
        this.castDuration = 0;
        this.castDurationRemaining = 0;
        this.castSource = CastSource.NONE;
        this.castType = CastType.NONE;
        this.getSyncedData().setIsCasting(false, "", 0, this.getCastingEquipmentSlot());
        this.resetAdditionalCastData();
        if (this.serverPlayer != null) {
            this.serverPlayer.stopUsingItem();
        }
    }

    public void initiateCast(AbstractSpell spell, int spellLevel, int castDuration, CastSource castSource, String castingEquipmentSlot) {
        this.castingSpellLevel = spellLevel;
        this.castDuration = castDuration;
        this.castDurationRemaining = castDuration;
        this.castSource = castSource;
        this.castType = spell.getCastType();
        this.syncedSpellData.setIsCasting(true, spell.getSpellId(), spellLevel, castingEquipmentSlot);
    }

    public ICastData getAdditionalCastData() {
        return this.additionalCastData;
    }

    public void setAdditionalCastData(ICastData newCastData) {
        this.additionalCastData = newCastData;
    }

    public void resetAdditionalCastData() {
        if (this.additionalCastData != null) {
            this.additionalCastData.reset();
            this.additionalCastData = null;
        }
    }

    public boolean isCasting() {
        return this.getSyncedData().isCasting();
    }

    public String getCastingEquipmentSlot() {
        return this.getSyncedData().getCastingEquipmentSlot();
    }

    public String getCastingSpellId() {
        return this.getSyncedData().getCastingSpellId();
    }

    public SpellData getCastingSpell() {
        return new SpellData(SpellRegistry.getSpell(this.getSyncedData().getCastingSpellId()), this.castingSpellLevel);
    }

    public int getCastingSpellLevel() {
        return this.castingSpellLevel;
    }

    public CastSource getCastSource() {
        if (this.castSource == null) {
            return CastSource.NONE;
        }
        return this.castSource;
    }

    public CastType getCastType() {
        return this.castType;
    }

    public float getCastCompletionPercent() {
        if (this.castDuration == 0) {
            return 1.0f;
        }
        return 1.0f - (float)this.castDurationRemaining / (float)this.castDuration;
    }

    public int getCastDurationRemaining() {
        return this.castDurationRemaining;
    }

    public int getCastDuration() {
        return this.castDuration;
    }

    public void handleCastDuration() {
        --this.castDurationRemaining;
        if (this.castDurationRemaining <= 0) {
            this.castDurationRemaining = 0;
        }
    }

    public void setPlayerCastingItem(ItemStack itemStack) {
        this.castingItemStack = itemStack;
    }

    public ItemStack getPlayerCastingItem() {
        return this.castingItemStack;
    }

    public void markPoisoned() {
        if (this.serverPlayer != null) {
            this.poisonedTimestamp = this.serverPlayer.tickCount;
        }
    }

    public boolean popMarkedPoison() {
        if (this.serverPlayer != null) {
            boolean poisoned = this.serverPlayer.tickCount - this.poisonedTimestamp <= 1;
            this.poisonedTimestamp = 0;
            return poisoned;
        }
        return false;
    }

    public PlayerCooldowns getPlayerCooldowns() {
        return this.playerCooldowns;
    }

    public PlayerRecasts getPlayerRecasts() {
        return this.isMob ? new PlayerRecasts() : this.playerRecasts;
    }

    @OnlyIn(value=Dist.CLIENT)
    public void setPlayerRecasts(PlayerRecasts playerRecasts) {
        this.playerRecasts = playerRecasts;
    }

    public static MagicData getPlayerMagicData(LivingEntity livingEntity) {
        return (MagicData)livingEntity.getData(DataAttachmentRegistry.MAGIC_DATA);
    }

    public void saveNBTData(CompoundTag compound, HolderLookup.Provider provider) {
        compound.putInt(MANA, (int)this.mana);
        if (this.playerCooldowns.hasCooldownsActive()) {
            compound.put(COOLDOWNS, (Tag)this.playerCooldowns.saveNBTData());
        }
        if (this.playerRecasts.hasRecastsActive()) {
            compound.put(RECASTS, (Tag)this.playerRecasts.saveNBTData(provider));
        }
        this.getSyncedData().saveNBTData(compound, provider);
    }

    public void loadNBTData(CompoundTag compound, HolderLookup.Provider provider) {
        this.mana = compound.getInt(MANA);
        ListTag listTag = (ListTag)compound.get(COOLDOWNS);
        if (listTag != null && !listTag.isEmpty()) {
            this.playerCooldowns.loadNBTData(listTag);
        }
        if ((listTag = (ListTag)compound.get(RECASTS)) != null && !listTag.isEmpty()) {
            this.playerRecasts.loadNBTData(listTag, provider);
        }
        this.getSyncedData().loadNBTData(compound, provider);
    }

    public String toString() {
        return String.format("isCasting:%s, spellID:%s], spellLevel:%s, duration:%s, durationRemaining:%s, source:%s, type:%s", new Object[]{this.getSyncedData().isCasting(), this.getSyncedData().getCastingSpellId(), this.castingSpellLevel, this.castDuration, this.castDurationRemaining, this.castSource, this.castType});
    }
}

