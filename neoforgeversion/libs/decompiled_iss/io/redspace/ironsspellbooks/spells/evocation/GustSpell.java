/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 */
package io.redspace.ironsspellbooks.spells.evocation;

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
import io.redspace.ironsspellbooks.effect.AirborneEffect;
import io.redspace.ironsspellbooks.entity.spells.gust.GustCollider;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;

public class GustSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"gust");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE).setMaxLevel(10).setCooldownSeconds(12.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.strength", (Object[])new Object[]{String.format("%s%%", (int)(this.getStrength(spellLevel, caster) * 100.0f / this.getStrength(1, null)))}), Component.translatable((String)"ui.irons_spellbooks.impact_damage", (Object[])new Object[]{Utils.stringTruncation(AirborneEffect.getDamageFromLevel(spellLevel), 1)}));
    }

    public GustSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 1;
        this.castTime = 15;
        this.baseManaCost = 30;
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
        return Optional.of((SoundEvent)SoundRegistry.GUST_CHARGE.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of((SoundEvent)SoundRegistry.GUST_CAST.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        float range = this.getRange(spellLevel, entity);
        float strength = this.getStrength(spellLevel, entity);
        GustCollider gust = new GustCollider(level, entity);
        gust.setPos(entity.position().add(0.0, (double)entity.getEyeHeight() * 0.7, 0.0).add(entity.getForward().normalize().scale(2.0)));
        gust.range = range;
        gust.strength = strength;
        gust.amplifier = spellLevel - 1;
        level.addFreshEntity((Entity)gust);
        gust.setDealDamageActive();
        gust.tick();
        float kickback = (float)entity.getBoundingBox().getCenter().distanceToSqr(Utils.getTargetBlock(level, entity, ClipContext.Fluid.NONE, 3.5).getLocation());
        kickback = Mth.clamp((float)(1.0f / (kickback + 1.0f) - 0.11f), (float)0.0f, (float)0.95f);
        if (kickback > 0.0f) {
            entity.setDeltaMovement(entity.getDeltaMovement().subtract(entity.getLookAngle().scale((double)(kickback * (float)spellLevel * 0.25f))));
            entity.resetFallDistance();
            entity.hurtMarked = true;
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    public float getRange(int spellLevel, LivingEntity caster) {
        return 8.0f;
    }

    public float getStrength(int spellLevel, LivingEntity caster) {
        return this.getSpellPower(spellLevel, (Entity)caster) * 0.2f;
    }

    public float getDamage(int spellLevel, LivingEntity caster) {
        return this.getSpellPower(spellLevel, (Entity)caster);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_WAVY_ANIMATION;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.ANIMATION_LONG_CAST_FINISH;
    }

    @Override
    public boolean shouldAIStopCasting(int spellLevel, Mob mob, LivingEntity target) {
        return target.distanceToSqr((Entity)mob) > (double)(this.getRange(spellLevel, (LivingEntity)mob) * this.getRange(spellLevel, (LivingEntity)mob)) * 1.25;
    }
}

