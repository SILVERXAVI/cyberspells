/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.level.Level
 *  net.neoforged.neoforge.common.NeoForge
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.spells.ender;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.SpellSummonEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerRecasts;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonedEntitiesCastData;
import io.redspace.ironsspellbooks.entity.spells.summoned_weapons.SummonedClaymoreEntity;
import io.redspace.ironsspellbooks.entity.spells.summoned_weapons.SummonedRapierEntity;
import io.redspace.ironsspellbooks.entity.spells.summoned_weapons.SummonedSwordEntity;
import io.redspace.ironsspellbooks.entity.spells.summoned_weapons.SummonedWeaponEntity;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

public class SummonSwordsSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"summon_swords");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.RARE).setSchoolResource(SchoolRegistry.ENDER_RESOURCE).setMaxLevel(5).setCooldownSeconds(150.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.summon_count", (Object[])new Object[]{3}), Component.translatable((String)"ui.irons_spellbooks.percent_damage", (Object[])new Object[]{(int)(100.0 + this.getDamageBonus(spellLevel, caster) * 100.0)}), Component.translatable((String)"ui.irons_spellbooks.percent_health", (Object[])new Object[]{(int)(100.0 + this.getHealthBonus(spellLevel, caster) * 100.0)}));
    }

    public SummonSwordsSpell() {
        this.manaCostPerLevel = 15;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 2;
        this.castTime = 20;
        this.baseManaCost = 80;
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
        return Optional.of((SoundEvent)SoundRegistry.SUMMONED_SWORDS_CHARGE.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of((SoundEvent)SoundRegistry.SUMMONED_SWORDS_CAST.get());
    }

    public double getHealthBonus(int spellLevel, LivingEntity caster) {
        return (double)(this.getSpellPower(spellLevel, (Entity)caster) - 1.0f) * 0.1;
    }

    public double getDamageBonus(int spellLevel, LivingEntity caster) {
        return (double)(this.getSpellPower(spellLevel, (Entity)caster) - 1.0f) * 0.05;
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
    public ICastDataSerializable getEmptyCastData() {
        return new SummonedEntitiesCastData();
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        PlayerRecasts recasts = playerMagicData.getPlayerRecasts();
        if (!recasts.hasRecastForSpell(this)) {
            SummonedEntitiesCastData summonedEntitiesCastData = new SummonedEntitiesCastData();
            int summonTime = 12000;
            AttributeModifier healthModifier = new AttributeModifier(IronsSpellbooks.id("spell_power_health_bonus"), this.getHealthBonus(spellLevel, entity), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
            AttributeModifier damageModifier = new AttributeModifier(IronsSpellbooks.id("spell_power_damage_bonus"), this.getDamageBonus(spellLevel, entity), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
            SummonedClaymoreEntity claymore = new SummonedClaymoreEntity(world, entity);
            SummonedRapierEntity rapier = new SummonedRapierEntity(world, entity);
            SummonedSwordEntity sword = new SummonedSwordEntity(world, entity);
            List<SummonedSwordEntity> weapons = List.of(claymore, rapier, sword);
            weapons.forEach(weapon -> {
                weapon.moveTo(entity.position().add(0.0, 1.2, 0.0).add(Utils.getRandomVec3(1.0)));
                weapon.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(damageModifier);
                weapon.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(healthModifier);
                weapon.setHealth(weapon.getMaxHealth());
                SummonedWeaponEntity creature = (SummonedWeaponEntity)((SpellSummonEvent)NeoForge.EVENT_BUS.post(new SpellSummonEvent<SummonedWeaponEntity>(entity, (SummonedWeaponEntity)weapon, this.spellId, spellLevel))).getCreature();
                world.addFreshEntity((Entity)creature);
                SummonManager.initSummon((Entity)entity, (Entity)creature, summonTime, summonedEntitiesCastData);
            });
            RecastInstance recastInstance = new RecastInstance(this.getSpellId(), spellLevel, this.getRecastCount(spellLevel, entity), summonTime, castSource, summonedEntitiesCastData);
            recasts.addRecast(recastInstance, playerMagicData);
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }
}

