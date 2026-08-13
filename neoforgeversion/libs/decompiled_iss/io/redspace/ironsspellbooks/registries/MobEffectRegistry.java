/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package io.redspace.ironsspellbooks.registries;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.effect.AbyssalShroudEffect;
import io.redspace.ironsspellbooks.effect.AirborneEffect;
import io.redspace.ironsspellbooks.effect.AngelWingsEffect;
import io.redspace.ironsspellbooks.effect.AscensionEffect;
import io.redspace.ironsspellbooks.effect.BlightEffect;
import io.redspace.ironsspellbooks.effect.BurningDashEffect;
import io.redspace.ironsspellbooks.effect.ChargeEffect;
import io.redspace.ironsspellbooks.effect.ChilledEffect;
import io.redspace.ironsspellbooks.effect.EchoingStrikesEffect;
import io.redspace.ironsspellbooks.effect.EvasionEffect;
import io.redspace.ironsspellbooks.effect.FallDamageImmunityEffect;
import io.redspace.ironsspellbooks.effect.FortifyEffect;
import io.redspace.ironsspellbooks.effect.FrostbiteEffect;
import io.redspace.ironsspellbooks.effect.GluttonyEffect;
import io.redspace.ironsspellbooks.effect.HeartstopEffect;
import io.redspace.ironsspellbooks.effect.ImmolateEffect;
import io.redspace.ironsspellbooks.effect.InstantManaEffect;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.effect.OakskinEffect;
import io.redspace.ironsspellbooks.effect.PlanarSightEffect;
import io.redspace.ironsspellbooks.effect.RendEffect;
import io.redspace.ironsspellbooks.effect.SpiderAspectEffect;
import io.redspace.ironsspellbooks.effect.SummonTimer;
import io.redspace.ironsspellbooks.effect.ThunderstormEffect;
import io.redspace.ironsspellbooks.effect.TrueInvisibilityEffect;
import io.redspace.ironsspellbooks.effect.VoltStrikeEffect;
import io.redspace.ironsspellbooks.effect.guiding_bolt.GuidingBoltEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MobEffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECT_DEFERRED_REGISTER = DeferredRegister.create((ResourceKey)Registries.MOB_EFFECT, (String)"irons_spellbooks");
    public static final DeferredHolder<MobEffect, MobEffect> ANGEL_WINGS = MOB_EFFECT_DEFERRED_REGISTER.register("angel_wings", () -> new AngelWingsEffect(MobEffectCategory.BENEFICIAL, 12495141));
    public static final DeferredHolder<MobEffect, MobEffect> EVASION = MOB_EFFECT_DEFERRED_REGISTER.register("evasion", () -> new EvasionEffect(MobEffectCategory.BENEFICIAL, 15825908));
    public static final DeferredHolder<MobEffect, MobEffect> HEARTSTOP = MOB_EFFECT_DEFERRED_REGISTER.register("heartstop", () -> new HeartstopEffect(MobEffectCategory.BENEFICIAL, 4393481));
    public static final DeferredHolder<MobEffect, MobEffect> ABYSSAL_SHROUD = MOB_EFFECT_DEFERRED_REGISTER.register("abyssal_shroud", () -> new AbyssalShroudEffect(MobEffectCategory.BENEFICIAL, 0));
    public static final DeferredHolder<MobEffect, MobEffect> ASCENSION = MOB_EFFECT_DEFERRED_REGISTER.register("ascension", () -> new AscensionEffect(MobEffectCategory.BENEFICIAL, 12495141).addAttributeModifier(Attributes.GRAVITY, IronsSpellbooks.id("mobeffect_ascension"), -0.85f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final DeferredHolder<MobEffect, MobEffect> CHARGED = MOB_EFFECT_DEFERRED_REGISTER.register("charged", () -> new ChargeEffect(MobEffectCategory.BENEFICIAL, 3311322).addAttributeModifier(Attributes.ATTACK_DAMAGE, IronsSpellbooks.id("mobeffect_charged"), 0.1f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL).addAttributeModifier(Attributes.MOVEMENT_SPEED, IronsSpellbooks.id("mobeffect_charged"), (double)0.2f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL).addAttributeModifier(AttributeRegistry.SPELL_POWER, IronsSpellbooks.id("mobeffect_charged"), (double)0.05f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final DeferredHolder<MobEffect, MobEffect> TRUE_INVISIBILITY = MOB_EFFECT_DEFERRED_REGISTER.register("true_invisibility", () -> new TrueInvisibilityEffect(MobEffectCategory.BENEFICIAL, 8356754));
    public static final DeferredHolder<MobEffect, MobEffect> FORTIFY = MOB_EFFECT_DEFERRED_REGISTER.register("fortify", () -> new FortifyEffect(MobEffectCategory.BENEFICIAL, 16239960).addAttributeModifier(Attributes.MAX_ABSORPTION, IronsSpellbooks.id("mobeffect_fortify"), 1.0, AttributeModifier.Operation.ADD_VALUE));
    public static final DeferredHolder<MobEffect, MobEffect> REND = MOB_EFFECT_DEFERRED_REGISTER.register("rend", () -> new RendEffect(MobEffectCategory.HARMFUL, 4800826).addAttributeModifier(Attributes.ARMOR, IronsSpellbooks.id("mobeffect_rend"), -0.05f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final DeferredHolder<MobEffect, MobEffect> SPIDER_ASPECT = MOB_EFFECT_DEFERRED_REGISTER.register("spider_aspect", () -> new SpiderAspectEffect(MobEffectCategory.BENEFICIAL, 4800826));
    public static final DeferredHolder<MobEffect, MobEffect> BLIGHT = MOB_EFFECT_DEFERRED_REGISTER.register("blight", () -> new BlightEffect(MobEffectCategory.HARMFUL, 14679851));
    public static final DeferredHolder<MobEffect, MobEffect> GUIDING_BOLT = MOB_EFFECT_DEFERRED_REGISTER.register("guided", () -> new GuidingBoltEffect(MobEffectCategory.HARMFUL, 16239960));
    public static final DeferredHolder<MobEffect, MobEffect> AIRBORNE = MOB_EFFECT_DEFERRED_REGISTER.register("airborne", () -> new AirborneEffect(MobEffectCategory.HARMFUL, 0xFFFFFF));
    public static final DeferredHolder<MobEffect, MobEffect> VIGOR = MOB_EFFECT_DEFERRED_REGISTER.register("vigor", () -> new MagicMobEffect(MobEffectCategory.BENEFICIAL, 8719629).addAttributeModifier(Attributes.MAX_HEALTH, IronsSpellbooks.id("mobeffect_vigor"), 2.0, AttributeModifier.Operation.ADD_VALUE));
    public static final DeferredHolder<MobEffect, MobEffect> INSTANT_MANA = MOB_EFFECT_DEFERRED_REGISTER.register("instant_mana", () -> new InstantManaEffect(MobEffectCategory.BENEFICIAL, 47084));
    public static final DeferredHolder<MobEffect, MobEffect> OAKSKIN = MOB_EFFECT_DEFERRED_REGISTER.register("oakskin", () -> new OakskinEffect(MobEffectCategory.BENEFICIAL, 16773013).addAttributeModifier(Attributes.MOVEMENT_SPEED, IronsSpellbooks.id("mobeffect_oakskin"), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, level -> (double)-0.2f));
    public static final DeferredHolder<MobEffect, MobEffect> PLANAR_SIGHT = MOB_EFFECT_DEFERRED_REGISTER.register("planar_sight", () -> new PlanarSightEffect(MobEffectCategory.BENEFICIAL, 7095029));
    public static final DeferredHolder<MobEffect, MobEffect> ANTIGRAVITY = MOB_EFFECT_DEFERRED_REGISTER.register("antigravity", () -> new MagicMobEffect(MobEffectCategory.NEUTRAL, 7095029).addAttributeModifier(Attributes.GRAVITY, IronsSpellbooks.id("mobeffect_antigravity"), -1.02, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    public static final DeferredHolder<MobEffect, MobEffect> HASTENED = MOB_EFFECT_DEFERRED_REGISTER.register("hastened", () -> new MagicMobEffect(MobEffectCategory.BENEFICIAL, 14270531).addAttributeModifier(Attributes.MOVEMENT_SPEED, IronsSpellbooks.id("mobeffect_haste"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL).addAttributeModifier(Attributes.ATTACK_SPEED, IronsSpellbooks.id("mobeffect_haste"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL).addAttributeModifier(AttributeRegistry.MANA_REGEN, IronsSpellbooks.id("mobeffect_haste"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL).addAttributeModifier(AttributeRegistry.CAST_TIME_REDUCTION, IronsSpellbooks.id("mobeffect_haste"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final DeferredHolder<MobEffect, MobEffect> SLOWED = MOB_EFFECT_DEFERRED_REGISTER.register("slowed", () -> new MagicMobEffect(MobEffectCategory.HARMFUL, 5926017).addAttributeModifier(Attributes.MOVEMENT_SPEED, IronsSpellbooks.id("mobeffect_slow"), -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL).addAttributeModifier(Attributes.ATTACK_SPEED, IronsSpellbooks.id("mobeffect_slow"), -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL).addAttributeModifier(AttributeRegistry.MANA_REGEN, IronsSpellbooks.id("mobeffect_slow"), -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL).addAttributeModifier(AttributeRegistry.CAST_TIME_REDUCTION, IronsSpellbooks.id("mobeffect_slow"), -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final DeferredHolder<MobEffect, MobEffect> CHILLED = MOB_EFFECT_DEFERRED_REGISTER.register("chilled", () -> new ChilledEffect(MobEffectCategory.HARMFUL, 13695487).addAttributeModifier(Attributes.MOVEMENT_SPEED, IronsSpellbooks.id("mobeffect_chilled"), -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final DeferredHolder<MobEffect, MobEffect> BURNING_DASH = MOB_EFFECT_DEFERRED_REGISTER.register("burning_dash", () -> new BurningDashEffect(MobEffectCategory.BENEFICIAL, 13695487));
    public static final DeferredHolder<MobEffect, MobEffect> VOLT_STRIKE = MOB_EFFECT_DEFERRED_REGISTER.register("volt_strike", () -> new VoltStrikeEffect(MobEffectCategory.BENEFICIAL, 218138879));
    public static final DeferredHolder<MobEffect, MobEffect> GLUTTONY = MOB_EFFECT_DEFERRED_REGISTER.register("gluttony", () -> new GluttonyEffect(MobEffectCategory.BENEFICIAL, 13695487));
    public static final DeferredHolder<MobEffect, MobEffect> ECHOING_STRIKES = MOB_EFFECT_DEFERRED_REGISTER.register("echoing_strikes", () -> new EchoingStrikesEffect(MobEffectCategory.BENEFICIAL, 10423267));
    public static final DeferredHolder<MobEffect, MobEffect> THUNDERSTORM = MOB_EFFECT_DEFERRED_REGISTER.register("thunderstorm", () -> new ThunderstormEffect(MobEffectCategory.BENEFICIAL, 10423267));
    public static final DeferredHolder<MobEffect, MobEffect> FROSTBITTEN_STRIKES = MOB_EFFECT_DEFERRED_REGISTER.register("frostbite", () -> new FrostbiteEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF));
    public static final DeferredHolder<MobEffect, MobEffect> IMMOLATE = MOB_EFFECT_DEFERRED_REGISTER.register("immolate", () -> new ImmolateEffect(MobEffectCategory.HARMFUL, 0xFFAA00));
    public static final DeferredHolder<MobEffect, MobEffect> FALL_DAMAGE_IMMUNITY = MOB_EFFECT_DEFERRED_REGISTER.register("fall_damage_immunity", () -> new FallDamageImmunityEffect(MobEffectCategory.BENEFICIAL, 0xDDDDFF));
    @Deprecated(forRemoval=true)
    public static final DeferredHolder<MobEffect, SummonTimer> VEX_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("vex_timer", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 12495141));
    @Deprecated(forRemoval=true)
    public static final DeferredHolder<MobEffect, SummonTimer> POLAR_BEAR_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("polar_bear_timer", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 12495141));
    @Deprecated(forRemoval=true)
    public static final DeferredHolder<MobEffect, SummonTimer> SUMMONED_SWORD_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("summon_swords_timer", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 12495141));
    @Deprecated(forRemoval=true)
    public static final DeferredHolder<MobEffect, SummonTimer> RAISE_DEAD_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("raise_dead_timer", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 12495141));
    @Deprecated(forRemoval=true)
    public static final DeferredHolder<MobEffect, SummonTimer> SUMMON_HORSE_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("summon_horse_timer", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 12495141));

    public static void register(IEventBus eventBus) {
        MOB_EFFECT_DEFERRED_REGISTER.register(eventBus);
    }
}

