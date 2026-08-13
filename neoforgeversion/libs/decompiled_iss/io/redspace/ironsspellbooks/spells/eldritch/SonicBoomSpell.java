/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.spells.eldritch;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SonicBoomSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"sonic_boom");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.LEGENDARY).setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE).setMaxLevel(3).setCooldownSeconds(25.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.damage", (Object[])new Object[]{Utils.stringTruncation(this.getDamage(spellLevel, caster), 2)}), Component.translatable((String)"ui.irons_spellbooks.distance", (Object[])new Object[]{Utils.stringTruncation(SonicBoomSpell.getRange(spellLevel, caster), 1)}));
    }

    public SonicBoomSpell() {
        this.manaCostPerLevel = 50;
        this.baseSpellPower = 20;
        this.spellPowerPerLevel = 8;
        this.castTime = 30;
        this.baseManaCost = 110;
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
        return Optional.of(SoundEvents.WARDEN_SONIC_CHARGE);
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of((SoundEvent)SoundRegistry.SONIC_BOOM.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        CameraShakeManager.addCameraShake(new CameraShakeData(level, 20, entity.position(), 20.0f));
        float range = SonicBoomSpell.getRange(spellLevel, entity);
        Vec3 start = entity.getEyePosition();
        Vec3 end = start.add(entity.getForward().scale((double)range));
        AABB boundingBox = entity.getBoundingBox().expandTowards(end.subtract(start));
        List entities = level.getEntities((Entity)entity, boundingBox);
        for (Entity target : entities) {
            HitResult hit = Utils.checkEntityIntersecting(target, start, end, 0.4f);
            if (hit.getType() == HitResult.Type.MISS) continue;
            DamageSources.applyDamage(target, this.getDamage(spellLevel, entity), this.getDamageSource((Entity)entity));
        }
        Vec3 vec3 = entity.getLookAngle().normalize();
        int i = 0;
        while ((float)i < range) {
            Vec3 vec32 = vec3.scale((double)i).add(entity.getEyePosition());
            MagicManager.spawnParticles(level, (ParticleOptions)ParticleTypes.SONIC_BOOM, vec32.x, vec32.y, vec32.z, 1, 0.0, 0.0, 0.0, 0.0, false);
            ++i;
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public void playSound(Optional<SoundEvent> sound, Entity entity) {
        if (sound == this.getCastFinishSound()) {
            entity.playSound(sound.get(), 3.5f, 0.9f + entity.level.random.nextFloat() * 0.2f);
        } else {
            super.playSound(sound, entity);
        }
    }

    public static float getRange(int level, LivingEntity caster) {
        return 15 + 5 * level;
    }

    private float getDamage(int spellLevel, LivingEntity caster) {
        return this.getSpellPower(spellLevel, (Entity)caster);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_SPIT_ANIMATION;
    }
}

