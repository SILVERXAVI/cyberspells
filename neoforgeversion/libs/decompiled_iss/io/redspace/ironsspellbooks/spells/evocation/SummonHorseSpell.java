/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.animal.horse.AbstractHorse
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.common.NeoForge
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.spells.evocation;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.SpellSummonEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerRecasts;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonedEntitiesCastData;
import io.redspace.ironsspellbooks.entity.mobs.SummonedHorse;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

public class SummonHorseSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"summon_horse");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE).setMaxLevel(5).setCooldownSeconds(20.0).build();

    public SummonHorseSpell() {
        this.manaCostPerLevel = 2;
        this.baseSpellPower = 85;
        this.spellPowerPerLevel = 15;
        this.castTime = 20;
        this.baseManaCost = 50;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundEvents.ILLUSIONER_PREPARE_MIRROR);
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.ILLUSIONER_MIRROR_MOVE);
    }

    @Override
    public ICastDataSerializable getEmptyCastData() {
        return new SummonedEntitiesCastData();
    }

    @Override
    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        return 2;
    }

    @Override
    public void onRecastFinished(ServerPlayer serverPlayer, RecastInstance recastInstance, RecastResult recastResult, ICastDataSerializable castDataSerializable) {
        if (SummonManager.recastFinishedHelper(serverPlayer, recastInstance, recastResult, castDataSerializable)) {
            super.onRecastFinished(serverPlayer, recastInstance, recastResult, castDataSerializable);
        }
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        PlayerRecasts recasts = playerMagicData.getPlayerRecasts();
        if (!recasts.hasRecastForSpell(this)) {
            SummonedEntitiesCastData summonedEntitiesCastData = new SummonedEntitiesCastData();
            int summonTime = 12000;
            Vec3 spawn = entity.position();
            Vec3 forward = entity.getForward().normalize().scale(1.5);
            spawn.add(forward.x, (double)0.15f, forward.z);
            SummonedHorse horse = new SummonedHorse(world, entity);
            horse.setPos(spawn);
            this.setAttributes(horse, this.getSpellPower(spellLevel, (Entity)entity) / 100.0f);
            SummonedHorse creature = (SummonedHorse)((SpellSummonEvent)NeoForge.EVENT_BUS.post(new SpellSummonEvent<SummonedHorse>(entity, horse, this.spellId, spellLevel))).getCreature();
            world.addFreshEntity((Entity)creature);
            SummonManager.initSummon((Entity)entity, (Entity)creature, summonTime, summonedEntitiesCastData);
            RecastInstance recastInstance = new RecastInstance(this.getSpellId(), spellLevel, this.getRecastCount(spellLevel, entity), summonTime, castSource, summonedEntitiesCastData);
            recasts.addRecast(recastInstance, playerMagicData);
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private void setAttributes(AbstractHorse horse, float powerMultiplier) {
        float speed = 0.22f * powerMultiplier;
        float jump = 0.4f * powerMultiplier;
        float health = 15.0f * powerMultiplier;
        int safeFall = 6 + (int)((jump - 0.2f) * 3.0f);
        horse.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)speed);
        horse.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue((double)jump);
        horse.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)health);
        horse.getAttribute(Attributes.SAFE_FALL_DISTANCE).setBaseValue((double)safeFall);
        horse.setHealth(health);
    }
}

