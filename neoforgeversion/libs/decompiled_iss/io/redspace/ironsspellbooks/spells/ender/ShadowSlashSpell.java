/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.item.enchantment.EnchantmentHelper
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Vector3f
 */
package io.redspace.ironsspellbooks.spells.ender;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
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
import io.redspace.ironsspellbooks.particle.EnderSlashParticleOptions;
import io.redspace.ironsspellbooks.particle.TraceParticleOptions;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

@AutoSpellConfig
public class ShadowSlashSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"shadow_slash");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.ENDER_RESOURCE).setMaxLevel(5).setCooldownSeconds(15.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.damage", (Object[])new Object[]{this.getDamageText(spellLevel, caster)}));
    }

    public ShadowSlashSpell() {
        this.manaCostPerLevel = 15;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 30;
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
    public void onClientCast(Level level, int spellLevel, LivingEntity entity, ICastData castData) {
        super.onClientCast(level, spellLevel, entity, castData);
        entity.setYBodyRot(entity.getYRot());
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of((SoundEvent)SoundRegistry.SHADOW_SLASH.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        float distance = 12.0f;
        Vec3 forward = entity.getForward();
        Vec3 end = Utils.raycastForBlock(level, entity.getEyePosition(), entity.getEyePosition().add(forward.scale((double)distance)), ClipContext.Fluid.NONE).getLocation();
        AABB hitbox = entity.getHitbox().expandTowards(end.subtract(entity.getEyePosition())).inflate(2.0);
        List targetableEntities = level.getEntities((Entity)entity, hitbox, e -> !e.isSpectator() && (e instanceof LivingEntity || e instanceof Projectile) && e.getBoundingBox().getCenter().subtract(entity.getBoundingBox().getCenter()).normalize().dot(entity.getForward()) >= 0.85);
        targetableEntities.sort(Comparator.comparingDouble(e -> e.distanceToSqr((Entity)entity)));
        if (!targetableEntities.isEmpty() && ((Entity)targetableEntities.get(0)).distanceToSqr((Entity)entity) < (double)(distance * distance)) {
            Entity closestEntity = (Entity)targetableEntities.get(0);
            float radius = 2.5f;
            AABB damageBox = AABB.ofSize((Vec3)closestEntity.getBoundingBox().getCenter(), (double)radius, (double)(radius + 1.0f), (double)radius).move(forward.scale((double)(radius / 2.0f)));
            end = damageBox.getCenter().add(end).scale(0.5);
            List damageEntities = level.getEntities((Entity)entity, damageBox);
            SpellDamageSource damageSource = this.getDamageSource((Entity)entity);
            boolean projectileEffects = false;
            for (Entity targetEntity : damageEntities) {
                if (targetEntity instanceof Projectile) {
                    Projectile projectile = (Projectile)targetEntity;
                    if (!projectile.noPhysics && !projectile.getType().is(ModTags.CANT_PARRY)) {
                        projectileEffects = true;
                        projectile.setOwner((Entity)entity);
                        projectile.shoot(forward.x, forward.y, forward.z, (float)projectile.getDeltaMovement().length(), 0.0f);
                        continue;
                    }
                }
                if (!targetEntity.isAlive() || !entity.isPickable() || !Utils.hasLineOfSight(level, entity.getEyePosition(), targetEntity.getBoundingBox().getCenter(), true) || !DamageSources.applyDamage(targetEntity, this.getDamage(spellLevel, entity), damageSource)) continue;
                MagicManager.spawnParticles(level, ParticleHelper.ENDER_SPARKS, targetEntity.getX(), targetEntity.getY() + (double)(targetEntity.getBbHeight() * 0.5f), targetEntity.getZ(), 15, targetEntity.getBbWidth() * 0.5f, targetEntity.getBbHeight() * 0.5f, targetEntity.getBbWidth() * 0.5f, 0.25, false);
                EnchantmentHelper.doPostAttackEffects((ServerLevel)((ServerLevel)level), (Entity)targetEntity, (DamageSource)damageSource);
                Vec3 knockback = targetEntity.position().subtract(entity.position()).normalize().add(0.0, 0.5, 0.0).normalize();
                knockback.scale((double)((float)Utils.random.nextIntBetweenInclusive(70, 100) / 100.0f * Utils.clampedKnockbackResistanceFactor(targetEntity, 0.2f, 1.0f) * 0.1f));
                targetEntity.setDeltaMovement(targetEntity.getDeltaMovement().add(knockback));
                targetEntity.hurtMarked = true;
            }
            if (projectileEffects) {
                level.playSound(null, closestEntity.getX(), closestEntity.getY(), closestEntity.getZ(), (SoundEvent)SoundRegistry.FIRE_DAGGER_PARRY.get(), entity.getSoundSource());
                MagicManager.spawnParticles(level, ParticleHelper.ENDER_SPARKS, closestEntity.getX(), closestEntity.getY() + (double)(closestEntity.getBbHeight() * 0.5f), closestEntity.getZ(), 25, 0.0, 0.0, 0.0, 0.4, false);
            }
        }
        Vec3 rayVector = end.subtract(entity.getEyePosition());
        Vec3 impulse = rayVector.scale(0.1666666716337204).add(0.0, 0.1, 0.0);
        entity.setDeltaMovement(entity.getDeltaMovement().scale(0.2).add(impulse));
        entity.hurtMarked = true;
        entity.addEffect(new MobEffectInstance(MobEffectRegistry.FALL_DAMAGE_IMMUNITY, 20, 0, false, false, true));
        forward = impulse.normalize();
        Vec3 up = new Vec3(0.0, 1.0, 0.0);
        if (forward.dot(up) > 0.999) {
            up = new Vec3(1.0, 0.0, 0.0);
        }
        Vec3 right = up.cross(forward);
        Vec3 particlePos = end.subtract(forward.scale(3.0)).add(right.scale(-0.3));
        MagicManager.spawnParticles(level, new EnderSlashParticleOptions((float)forward.x, (float)forward.y, (float)forward.z, (float)right.x, (float)right.y, (float)right.z, 1.0f), particlePos.x, particlePos.y + 0.3, particlePos.z, 1, 0.0, 0.0, 0.0, 0.0, true);
        int trailParticles = 15;
        double speed = rayVector.length() / 12.0 * 0.75;
        for (int i = 0; i < trailParticles; ++i) {
            Vec3 particleStart = entity.getBoundingBox().getCenter().add(Utils.getRandomVec3(1.0f + entity.getBbWidth()));
            Vec3 particleEnd = particleStart.add(rayVector);
            MagicManager.spawnParticles(level, new TraceParticleOptions(Utils.v3f(particleEnd), new Vector3f(1.0f, 0.333f, 1.0f)), particleStart.x, particleStart.y, particleStart.z, 1, 0.0, 0.0, 0.0, speed, false);
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, (Entity)entity) + Utils.getWeaponDamage(entity);
    }

    private String getDamageText(int spellLevel, LivingEntity entity) {
        if (entity != null) {
            float weaponDamage = Utils.getWeaponDamage(entity);
            String plus = "";
            if (weaponDamage > 0.0f) {
                plus = String.format(" (+%s)", Utils.stringTruncation(weaponDamage, 1));
            }
            String damage = Utils.stringTruncation(this.getDamage(spellLevel, entity), 1);
            return damage + plus;
        }
        return "" + this.getSpellPower(spellLevel, (Entity)entity);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.ONE_HANDED_VERTICAL_UPSWING_ANIMATION;
    }
}

