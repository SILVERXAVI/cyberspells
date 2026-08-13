/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.level.Level
 */
package io.redspace.ironsspellbooks.spells.nature;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.acid_orb.AcidOrb;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class AcidOrbSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"acid_orb");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.NATURE_RESOURCE).setMaxLevel(8).setCooldownSeconds(15.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.radius", (Object[])new Object[]{Utils.stringTruncation(this.getRadius(spellLevel, caster), 1)}), Component.translatable((String)"ui.irons_spellbooks.rend", (Object[])new Object[]{Utils.stringTruncation((this.getRendAmplifier(spellLevel, caster) + 1) * 5, 1)}), Component.translatable((String)"ui.irons_spellbooks.effect_length", (Object[])new Object[]{Utils.timeFromTicks(this.getRendDuration(spellLevel, caster), 1)}));
    }

    public AcidOrbSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 0;
        this.castTime = 15;
        this.baseManaCost = 40;
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
        return Optional.of((SoundEvent)SoundRegistry.ACID_ORB_CHARGE.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of((SoundEvent)SoundRegistry.ACID_ORB_CAST.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        AcidOrb orb = new AcidOrb(level, entity);
        orb.setPos(entity.position().add(0.0, (double)entity.getEyeHeight() - orb.getBoundingBox().getYsize() * 0.5, 0.0).add(entity.getForward()));
        orb.shoot(entity.getLookAngle());
        orb.setDeltaMovement(orb.getDeltaMovement().add(0.0, 0.2, 0.0));
        orb.setExplosionRadius(this.getRadius(spellLevel, entity));
        orb.setRendLevel(this.getRendAmplifier(spellLevel, entity));
        orb.setRendDuration(this.getRendDuration(spellLevel, entity));
        level.addFreshEntity((Entity)orb);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    public float getRadius(int spellLevel, LivingEntity caster) {
        return this.getSpellPower(spellLevel, (Entity)caster) * 3.0f;
    }

    public int getRendAmplifier(int spellLevel, LivingEntity caster) {
        return spellLevel + 2;
    }

    public int getRendDuration(int spellLevel, LivingEntity caster) {
        return (int)(this.getSpellPower(spellLevel, (Entity)caster) * 20.0f * 20.0f);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_SPIT_ANIMATION;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.SPIT_FINISH_ANIMATION;
    }

    @Override
    public boolean shouldAIStopCasting(int spellLevel, Mob mob, LivingEntity target) {
        return target.getAttributeValue(Attributes.ARMOR) < 4.0;
    }
}

