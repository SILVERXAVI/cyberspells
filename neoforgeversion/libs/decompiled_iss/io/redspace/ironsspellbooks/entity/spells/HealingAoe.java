/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.bus.api.Event
 *  net.neoforged.neoforge.common.NeoForge
 */
package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.events.SpellHealEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.player.ClientSpellCastHelper;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;

public class HealingAoe
extends AoeEntity
implements AntiMagicSusceptible {
    public HealingAoe(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public HealingAoe(Level level) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.HEALING_AOE.get()), level);
    }

    @Override
    public void applyEffect(LivingEntity target) {
        LivingEntity owner;
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity && Utils.shouldHealEntity(owner = (LivingEntity)entity, target)) {
            float healAmount = this.getDamage();
            NeoForge.EVENT_BUS.post((Event)new SpellHealEvent((LivingEntity)this.getOwner(), target, healAmount, SchoolRegistry.HOLY.get()));
            target.heal(healAmount);
        }
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        return !pTarget.isSpectator() && pTarget.isAlive() && pTarget.isPickable();
    }

    @Override
    public float getParticleCount() {
        return 0.35f;
    }

    @Override
    protected float getParticleSpeedModifier() {
        return 0.0f;
    }

    @Override
    protected Vec3 getInflation() {
        return new Vec3(0.0, 1.0, 0.0);
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.of(ClientSpellCastHelper.coloredMobEffect(((MobEffect)MobEffects.HEAL.value()).getColor()));
    }

    @Override
    public void onAntiMagic(MagicData magicData) {
        this.discard();
    }
}

