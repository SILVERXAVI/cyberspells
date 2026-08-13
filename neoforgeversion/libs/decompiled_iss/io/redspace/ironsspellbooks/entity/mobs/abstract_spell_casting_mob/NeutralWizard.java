/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Object2IntArrayMap
 *  it.unimi.dsi.fastutil.objects.Object2IntMap$Entry
 *  it.unimi.dsi.fastutil.objects.ObjectIterator
 *  javax.annotation.Nullable
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.TimeUtil
 *  net.minecraft.util.valueproviders.UniformInt
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.NeutralMob
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.GameRules
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class NeutralWizard
extends AbstractSpellCastingMob
implements NeutralMob {
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds((int)20, (int)39);
    private int remainingPersistentAngerTime;
    @Nullable
    private UUID persistentAngerTarget;
    private int lastAngerLevelUpdate;
    private final Object2IntArrayMap<UUID> angerLevels = new Object2IntArrayMap();

    protected NeutralWizard(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    public void setRemainingPersistentAngerTime(int pTime) {
        this.remainingPersistentAngerTime = pTime;
    }

    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    public void setPersistentAngerTarget(@Nullable UUID pTarget) {
        this.persistentAngerTarget = pTarget;
    }

    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        this.addPersistentAngerSaveData(pCompound);
        if (!this.angerLevels.isEmpty()) {
            ListTag levels = new ListTag();
            for (Map.Entry entry : this.angerLevels.object2IntEntrySet()) {
                CompoundTag tag = new CompoundTag();
                tag.putUUID("player", (UUID)entry.getKey());
                tag.putInt("anger", ((Integer)entry.getValue()).intValue());
                levels.add((Object)tag);
            }
            pCompound.put("angerLevels", (Tag)levels);
        }
        super.addAdditionalSaveData(pCompound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        this.readPersistentAngerSaveData(this.level, pCompound);
        if (pCompound.contains("angerLevels")) {
            ListTag entries = pCompound.getList("angerLevels", 10);
            for (Tag tag : entries) {
                try {
                    this.angerLevels.put((Object)((CompoundTag)tag).getUUID("player"), ((CompoundTag)tag).getInt("anger"));
                }
                catch (Exception exception) {}
            }
        }
        super.readAdditionalSaveData(pCompound);
    }

    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level, true);
        }
        if (!this.angerLevels.isEmpty() && this.lastAngerLevelUpdate + 400 < this.tickCount) {
            ObjectIterator it = this.angerLevels.object2IntEntrySet().iterator();
            while (it.hasNext()) {
                Object2IntMap.Entry entry = (Object2IntMap.Entry)it.next();
                int newLevel = entry.getIntValue() - 1;
                if (newLevel == 0) {
                    it.remove();
                    continue;
                }
                this.angerLevels.put((Object)((UUID)entry.getKey()), newLevel);
            }
            this.lastAngerLevelUpdate = this.tickCount;
        }
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        Player player;
        Entity entity = pSource.getEntity();
        if (entity instanceof Player && !(player = (Player)entity).isCreative()) {
            this.increaseAngerLevel(player, (int)Math.ceil(pAmount), !this.isHostileTowards((LivingEntity)player));
        }
        return super.hurt(pSource, pAmount);
    }

    public void increaseAngerLevel(Player angryAt, int levels, boolean showParticles) {
        if (this.level.isClientSide) {
            return;
        }
        int anger = Math.min(this.angerLevels.getOrDefault((Object)angryAt.getUUID(), 0) + levels, 10);
        this.angerLevels.put((Object)angryAt.getUUID(), anger);
        this.lastAngerLevelUpdate = this.tickCount;
        if (anger < this.getAngerThreshold() && showParticles) {
            MagicManager.spawnParticles(this.level, (ParticleOptions)ParticleTypes.ANGRY_VILLAGER, this.getX(), this.getY() + 1.25, this.getZ(), 15, 0.3, 0.2, 0.3, 0.0, false);
            this.getAngerSound().ifPresent(sound -> this.playSound((SoundEvent)sound, this.getSoundVolume(), this.getVoicePitch()));
        }
        if (anger >= this.getAngerThreshold()) {
            this.setPersistentAngerTarget(angryAt.getUUID());
        }
    }

    @Deprecated
    public void increaseAngerLevel(int levels, boolean showParticles) {
        IronsSpellbooks.LOGGER.warn("Warning! Use of deprecated NeutralWizard#increaseAngerLevel");
        for (Object2IntMap.Entry entry : this.angerLevels.object2IntEntrySet()) {
            int newLevel = entry.getIntValue() + 1;
            entry.setValue(newLevel);
        }
    }

    public Optional<SoundEvent> getAngerSound() {
        return Optional.empty();
    }

    public int getAngerThreshold() {
        return 2;
    }

    public boolean isHostileTowards(LivingEntity entity) {
        return this.isAngryAt(entity) && (entity.getType() != EntityType.PLAYER || this.angerLevels.getOrDefault((Object)entity.getUUID(), 0) >= this.getAngerThreshold());
    }

    public boolean isAngryAt(LivingEntity pTarget) {
        return pTarget.getType() == EntityType.PLAYER && this.angerLevels.containsKey((Object)pTarget.getUUID()) || super.isAngryAt(pTarget);
    }

    public boolean guardsBlocks() {
        return true;
    }

    public void readPersistentAngerSaveData(Level level, CompoundTag tag) {
        this.setRemainingPersistentAngerTime(tag.getInt("AngerTime"));
        if (level instanceof ServerLevel) {
            if (!tag.hasUUID("AngryAt")) {
                this.setPersistentAngerTarget(null);
            } else {
                UUID uuid = tag.getUUID("AngryAt");
                this.setPersistentAngerTarget(uuid);
            }
        }
    }

    public void playerDied(@NotNull Player player) {
        super.playerDied(player);
        if (player.level().getGameRules().getBoolean(GameRules.RULE_FORGIVE_DEAD_PLAYERS)) {
            this.angerLevels.removeInt((Object)player.getUUID());
        }
    }
}

