/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  org.joml.Vector3f
 */
package io.redspace.ironsspellbooks.spells.evocation;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.spells.TargetedTargetAreaCastData;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public class SlowSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"slow");
    private static final int MAX_TARGETS = 5;
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.EPIC).setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE).setMaxLevel(4).setCooldownSeconds(80.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.slowed", (Object[])new Object[]{Utils.stringTruncation((float)(1 + this.getAmplifier(spellLevel, caster)) * 0.1f * 100.0f, 1)}), Component.translatable((String)"ui.irons_spellbooks.effect_length", (Object[])new Object[]{Utils.timeFromTicks(this.getDuration(spellLevel, caster), 1)}), Component.translatable((String)"ui.irons_spellbooks.max_victims", (Object[])new Object[]{5}));
    }

    public SlowSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 20;
        this.spellPowerPerLevel = 4;
        this.castTime = 30;
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
        return Optional.of(SoundEvents.EVOKER_PREPARE_SUMMON);
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        if (Utils.preCastTargetHelper(level, entity, playerMagicData, this, 32, 0.35f)) {
            float radius = 3.0f;
            LivingEntity target = ((TargetEntityCastData)playerMagicData.getAdditionalCastData()).getTarget((ServerLevel)level);
            TargetedAreaEntity area = TargetedAreaEntity.createTargetAreaEntity(level, target.position(), radius, ((MobEffect)MobEffectRegistry.SLOWED.get()).getColor(), (Entity)target);
            playerMagicData.setAdditionalCastData(new TargetedTargetAreaCastData(target, area));
            return true;
        }
        return false;
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        TargetedTargetAreaCastData targetData;
        LivingEntity targetEntity;
        ICastData iCastData = playerMagicData.getAdditionalCastData();
        if (iCastData instanceof TargetedTargetAreaCastData && (targetEntity = (targetData = (TargetedTargetAreaCastData)iCastData).getTarget((ServerLevel)world)) != null) {
            float radius = 3.0f;
            AtomicInteger targets = new AtomicInteger(0);
            targetEntity.level.getEntitiesOfClass(LivingEntity.class, targetEntity.getBoundingBox().inflate((double)radius)).forEach(victim -> {
                if (targets.get() < 5 && victim != entity && victim.distanceToSqr((Entity)targetEntity) < (double)(radius * radius) && !DamageSources.isFriendlyFireBetween((Entity)entity, (Entity)victim)) {
                    victim.addEffect(new MobEffectInstance(MobEffectRegistry.SLOWED, this.getDuration(spellLevel, entity), this.getAmplifier(spellLevel, entity)));
                    targets.incrementAndGet();
                }
            });
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    public int getAmplifier(int spellLevel, LivingEntity caster) {
        return spellLevel - 1;
    }

    public int getDuration(int spellLevel, LivingEntity caster) {
        return (int)(this.getSpellPower(spellLevel, (Entity)caster) * 20.0f);
    }

    @Override
    public Vector3f getTargetingColor() {
        return Utils.deconstructRGB(((MobEffect)MobEffectRegistry.SLOWED.get()).getColor());
    }
}

