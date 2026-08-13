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
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.spells.fire;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.spells.magma_ball.FireField;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.spells.TargetAreaCastData;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ScorchSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"scorch");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.FIRE_RESOURCE).setMaxLevel(10).setCooldownSeconds(12.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.damage", (Object[])new Object[]{Utils.stringTruncation(this.getDamage(spellLevel, caster), 2)}), Component.translatable((String)"ui.irons_spellbooks.radius", (Object[])new Object[]{Utils.stringTruncation(this.getRadius(caster), 1)}));
    }

    public ScorchSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 8;
        this.spellPowerPerLevel = 1;
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
        return Optional.of((SoundEvent)SoundRegistry.SCORCH_PREPARE.get());
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        float radius = this.getRadius(entity);
        HitResult hitResult = Utils.raycastForEntity(level, (Entity)entity, 32.0f, true, 0.2f);
        Vec3 location = Utils.moveToRelativeGroundLevel(level, hitResult.getLocation(), 3, 6);
        TargetedAreaEntity area = TargetedAreaEntity.createTargetAreaEntity(level, location, radius, Utils.packRGB(this.getTargetingColor()));
        playerMagicData.setAdditionalCastData(new TargetAreaCastData(location, area));
        return true;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        ICastData iCastData = playerMagicData.getAdditionalCastData();
        if (iCastData instanceof TargetAreaCastData) {
            TargetAreaCastData castData = (TargetAreaCastData)iCastData;
            Vec3 targetArea = castData.getCenter();
            MagicManager.spawnParticles(level, (ParticleOptions)ParticleTypes.LAVA, targetArea.x, targetArea.y, targetArea.z, 25, 1.0, 1.0, 1.0, 1.0, true);
            MagicManager.spawnParticles(level, (ParticleOptions)ParticleTypes.LAVA, targetArea.x, targetArea.y + 1.0, targetArea.z, 25, 0.25, 1.5, 0.25, 1.0, false);
            level.playSound(null, targetArea.x, targetArea.y, targetArea.z, (SoundEvent)SoundRegistry.FIERY_EXPLOSION.get(), SoundSource.PLAYERS, 2.0f, (float)Utils.random.nextIntBetweenInclusive(8, 12) * 0.1f);
            float radius = castData.getCastingEntity().getRadius();
            float radiusSqr = radius * radius;
            float damage = this.getDamage(spellLevel, entity);
            SpellDamageSource source = this.getDamageSource((Entity)entity);
            level.getEntitiesOfClass(LivingEntity.class, new AABB(targetArea.subtract((double)radius, (double)radius, (double)radius), targetArea.add((double)radius, (double)radius, (double)radius)), livingEntity -> livingEntity != entity && this.horizontalDistanceSqr((LivingEntity)livingEntity, targetArea) < radiusSqr && livingEntity.isPickable() && !DamageSources.isFriendlyFireBetween((Entity)livingEntity, (Entity)entity) && Utils.hasLineOfSight(level, targetArea.add(0.0, 1.5, 0.0), livingEntity.getBoundingBox().getCenter(), true)).forEach(livingEntity -> {
                DamageSources.applyDamage((Entity)livingEntity, damage, source);
                DamageSources.ignoreNextKnockback(livingEntity);
            });
            FireField fire = new FireField(level);
            fire.setOwner((Entity)entity);
            fire.setDuration(200);
            fire.setDamage(damage * 0.1f);
            fire.setRadius(radius);
            fire.setCircular();
            fire.moveTo(targetArea);
            level.addFreshEntity((Entity)fire);
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float horizontalDistanceSqr(LivingEntity livingEntity, Vec3 vec3) {
        double dx = livingEntity.getX() - vec3.x;
        double dz = livingEntity.getZ() - vec3.z;
        return (float)(dx * dx + dz * dz);
    }

    @Override
    public SpellDamageSource getDamageSource(@Nullable Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker).setFireTicks(60);
    }

    private float getDamage(int spellLevel, LivingEntity caster) {
        return this.getSpellPower(spellLevel, (Entity)caster);
    }

    private float getRadius(LivingEntity caster) {
        return 2.5f;
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_RAISED_HAND;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.ANIMATION_INSTANT_CAST;
    }
}

