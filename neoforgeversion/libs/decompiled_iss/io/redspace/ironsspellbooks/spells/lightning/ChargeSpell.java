/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 */
package io.redspace.ironsspellbooks.spells.lightning;

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
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ChargeSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"charge");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.RARE).setSchoolResource(SchoolRegistry.LIGHTNING_RESOURCE).setMaxLevel(3).setCooldownSeconds(40.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.effect_length", (Object[])new Object[]{Utils.timeFromTicks(this.getSpellPower(spellLevel, (Entity)caster) * 20.0f, 1)}), Component.translatable((String)"attribute.modifier.plus.1", (Object[])new Object[]{Utils.stringTruncation(this.getPercentSpeed(spellLevel, caster), 0), Component.translatable((String)"attribute.name.generic.movement_speed")}), Component.translatable((String)"attribute.modifier.plus.1", (Object[])new Object[]{Utils.stringTruncation(this.getPercentAttackDamage(spellLevel, caster), 0), Component.translatable((String)"attribute.name.generic.attack_damage")}), Component.translatable((String)"attribute.modifier.plus.1", (Object[])new Object[]{Utils.stringTruncation(this.getPercentSpellPower(spellLevel, caster), 0), Component.translatable((String)"attribute.irons_spellbooks.spell_power")}));
    }

    public ChargeSpell() {
        this.manaCostPerLevel = 25;
        this.baseSpellPower = 30;
        this.spellPowerPerLevel = 8;
        this.castTime = 0;
        this.baseManaCost = 50;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
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
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        entity.addEffect(new MobEffectInstance(MobEffectRegistry.CHARGED, (int)(this.getSpellPower(spellLevel, (Entity)entity) * 20.0f), spellLevel - 1, false, false, true));
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getPercentAttackDamage(int spellLevel, LivingEntity entity) {
        return (float)spellLevel * 0.1f * 100.0f;
    }

    private float getPercentSpeed(int spellLevel, LivingEntity entity) {
        return (float)spellLevel * 0.2f * 100.0f;
    }

    private float getPercentSpellPower(int spellLevel, LivingEntity entity) {
        return (float)spellLevel * 0.05f * 100.0f;
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.SELF_CAST_ANIMATION;
    }
}

