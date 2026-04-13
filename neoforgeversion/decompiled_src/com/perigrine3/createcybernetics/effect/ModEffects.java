/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.Registry
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package com.perigrine3.createcybernetics.effect;

import com.perigrine3.createcybernetics.effect.AerostasisGyrobladderEffect;
import com.perigrine3.createcybernetics.effect.BreathlessEffect;
import com.perigrine3.createcybernetics.effect.CyberwareRejectionEffect;
import com.perigrine3.createcybernetics.effect.EmpEffect;
import com.perigrine3.createcybernetics.effect.GuardianEyeEffect;
import com.perigrine3.createcybernetics.effect.InkedEffect;
import com.perigrine3.createcybernetics.effect.NeuralContextualizerEffect;
import com.perigrine3.createcybernetics.effect.NeuropozyneEffect;
import com.perigrine3.createcybernetics.effect.PneumaticCalvesEffect;
import com.perigrine3.createcybernetics.effect.ProjectileDodgeEffect;
import com.perigrine3.createcybernetics.effect.SandevistanEffect;
import com.perigrine3.createcybernetics.effect.SculkLungsEffect;
import com.perigrine3.createcybernetics.effect.SpiderEyesEffect;
import com.perigrine3.createcybernetics.effect.SpursEffect;
import com.perigrine3.createcybernetics.effect.SubdermalSpikesEffect;
import com.perigrine3.createcybernetics.effect.SyntheticSetulesEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create((Registry)BuiltInRegistries.MOB_EFFECT, (String)"createcybernetics");
    public static final Holder<MobEffect> CYBERWARE_REJECTION = MOB_EFFECTS.register("cyberware_rejection", () -> new CyberwareRejectionEffect(MobEffectCategory.NEUTRAL, 10559237).addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware_rejection"), -0.17f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> NEUROPOZYNE = MOB_EFFECTS.register("neuropozyne", () -> new NeuropozyneEffect(MobEffectCategory.BENEFICIAL, 12900645).addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"neuropozyne_speed"), 0.001, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> EMP = MOB_EFFECTS.register("emp_effect", () -> new EmpEffect().addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"emp_speed"), 0.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> SYNTHETIC_SETULES_EFFECT = MOB_EFFECTS.register("synthetic_setules_effect", () -> new SyntheticSetulesEffect(MobEffectCategory.NEUTRAL, 0).addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"synthetic_setules_effect"), 0.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> AEROSTASIS_GYROBLADDER_EFFECT = MOB_EFFECTS.register("aerostasis_gyrobladder_effect", () -> new AerostasisGyrobladderEffect(MobEffectCategory.NEUTRAL, 0).addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"aerostasis_gyrobladder_effect"), 0.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> PNEUMATIC_CALVES_EFFECT = MOB_EFFECTS.register("pneumatic_calves_effect", () -> new PneumaticCalvesEffect(MobEffectCategory.NEUTRAL, 0).addAttributeModifier(Attributes.JUMP_STRENGTH, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"pneumatic_calves_effect"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> SPURS_EFFECT = MOB_EFFECTS.register("spurs_effect", () -> new SpursEffect().addAttributeModifier(Attributes.JUMP_STRENGTH, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"spurs_effect"), 0.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> NEURAL_CONTEXTUALIZER_EFFECT = MOB_EFFECTS.register("neural_contextualizer_effect", () -> new NeuralContextualizerEffect(MobEffectCategory.NEUTRAL, 0).addAttributeModifier(Attributes.JUMP_STRENGTH, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"neural_contextualizer_effect"), 0.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> SUBDERMAL_SPIKES_EFFECT = MOB_EFFECTS.register("subdermal_spikes_effect", () -> new SubdermalSpikesEffect().addAttributeModifier(Attributes.JUMP_STRENGTH, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"subdermal_spikes_effect"), 0.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> GUARDIAN_EYE_EFFECT = MOB_EFFECTS.register("guardian_eye_effect", () -> new GuardianEyeEffect(MobEffectCategory.NEUTRAL, 0).addAttributeModifier(Attributes.JUMP_STRENGTH, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"guardian_eye_effect"), 0.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> PROJECTILE_DODGE_EFFECT = MOB_EFFECTS.register("projectile_dodge_effect", () -> new ProjectileDodgeEffect().addAttributeModifier(Attributes.JUMP_STRENGTH, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"projectile_dodge_effect"), 0.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> SCULK_LUNGS_EFFECT = MOB_EFFECTS.register("sculk_lungs_effect", () -> new SculkLungsEffect(MobEffectCategory.NEUTRAL, 0).addAttributeModifier(Attributes.JUMP_STRENGTH, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"sculk_lungs_effect"), 0.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> INKED_EFFECT = MOB_EFFECTS.register("inked_effect", () -> new InkedEffect().addAttributeModifier(Attributes.JUMP_STRENGTH, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"inked_effect"), 0.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> BREATHLESS_EFFECT = MOB_EFFECTS.register("breathless_effect", () -> new BreathlessEffect().addAttributeModifier(Attributes.JUMP_STRENGTH, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"breathless_effect"), 0.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> SANDEVISTAN_EFFECT = MOB_EFFECTS.register("sandevistan_effect", SandevistanEffect::new);
    public static final Holder<MobEffect> SPIDER_EYES_EFFECT = MOB_EFFECTS.register("spider_eyes_effect", SpiderEyesEffect::new);

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}

