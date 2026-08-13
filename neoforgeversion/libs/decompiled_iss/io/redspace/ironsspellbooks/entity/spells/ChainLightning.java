/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.particle.ZapParticleOption;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ChainLightning
extends AbstractMagicProjectile {
    List<Entity> allVictims = new ArrayList<Entity>();
    List<Entity> lastVictims = new ArrayList<Entity>();
    Entity initialVictim;
    public int maxConnections = 4;
    public int maxConnectionsPerWave = 3;
    public float range = 3.0f;
    private static final Supplier<AbstractSpell> SPELL = SpellRegistry.CHAIN_LIGHTNING_SPELL;
    int hits;

    public ChainLightning(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
        this.noPhysics = true;
    }

    public ChainLightning(Level level, Entity owner, Entity initialVictim) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.CHAIN_LIGHTNING.get()), level);
        this.setOwner(owner);
        this.setPos(initialVictim.position());
        this.initialVictim = initialVictim;
    }

    @Override
    public void tick() {
        super.tick();
        int f = this.tickCount - 1;
        if (!this.level.isClientSide && f % 6 == 0) {
            if (f == 0 && !this.allVictims.contains(this.initialVictim)) {
                this.doHurt(this.initialVictim);
                if (this.getOwner() != null) {
                    Vec3 start = this.getOwner().position().add(0.0, (double)(this.getOwner().getBbHeight() / 2.0f), 0.0);
                    Vec3 dest = this.initialVictim.position().add(0.0, (double)(this.initialVictim.getBbHeight() / 2.0f), 0.0);
                    ((ServerLevel)this.level).sendParticles((ParticleOptions)new ZapParticleOption(dest), start.x, start.y, start.z, 1, 0.0, 0.0, 0.0, 0.0);
                }
            } else {
                int j = this.lastVictims.size();
                AtomicInteger zapsThisWave = new AtomicInteger();
                for (int i = 0; i < j; ++i) {
                    Entity entity = this.lastVictims.get(i);
                    List entities = this.level.getEntities(entity, entity.getBoundingBox().inflate((double)this.range), this::canHitEntity);
                    entities.sort(Comparator.comparingDouble(o -> o.distanceToSqr(entity)));
                    entities.forEach(victim -> {
                        if (zapsThisWave.get() < this.maxConnectionsPerWave && this.hits < this.maxConnections && victim.distanceToSqr(entity) < (double)(this.range * this.range) && Utils.hasLineOfSight(this.level, entity.getEyePosition(), victim.getEyePosition(), true)) {
                            this.doHurt((Entity)victim);
                            victim.playSound((SoundEvent)SoundRegistry.CHAIN_LIGHTNING_CHAIN.get(), 2.0f, 1.0f);
                            zapsThisWave.getAndIncrement();
                            Vec3 start = new Vec3(entity.xOld, entity.yOld, entity.zOld).add(0.0, (double)(entity.getBbHeight() / 2.0f), 0.0);
                            Vec3 dest = victim.position().add(0.0, (double)(victim.getBbHeight() / 2.0f), 0.0);
                            ((ServerLevel)this.level).sendParticles((ParticleOptions)new ZapParticleOption(dest), start.x, start.y, start.z, 1, 0.0, 0.0, 0.0, 0.0);
                        }
                    });
                }
                this.lastVictims.removeAll(this.allVictims);
                if (this.lastVictims.isEmpty()) {
                    this.discard();
                }
            }
            this.allVictims.addAll(this.lastVictims);
        }
    }

    public void doHurt(Entity victim) {
        ++this.hits;
        DamageSources.applyDamage(victim, this.damage, SPELL.get().getDamageSource((Entity)this, this.getOwner()));
        MagicManager.spawnParticles(this.level, ParticleHelper.ELECTRICITY, victim.getX(), victim.getY() + (double)(victim.getBbHeight() / 2.0f), victim.getZ(), 10, victim.getBbWidth() / 3.0f, victim.getBbHeight() / 3.0f, victim.getBbWidth() / 3.0f, 0.1, false);
        this.lastVictims.add(victim);
    }

    public boolean hasAlreadyZapped(Entity entity) {
        return this.allVictims.contains(entity) || this.lastVictims.contains(entity);
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return target instanceof LivingEntity && !DamageSources.isFriendlyFireBetween(target, this.getOwner()) && target != this.getOwner() && !this.hasAlreadyZapped(target) && super.canHitEntity(target);
    }

    @Override
    public void trailParticles() {
    }

    @Override
    public void impactParticles(double x, double y, double z) {
    }

    @Override
    public float getSpeed() {
        return 0.0f;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }
}

