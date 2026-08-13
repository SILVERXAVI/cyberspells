/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Holder
 *  net.minecraft.core.Position
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.entity.projectile.ProjectileUtil
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.event.EventHooks
 */
package io.redspace.ironsspellbooks.entity.spells.magic_arrow;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

public class MagicArrowProjectile
extends AbstractMagicProjectile {
    private final List<Entity> victims = new ArrayList<Entity>();
    private int hitsPerTick;
    BlockPos lastHitBlock;

    public MagicArrowProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
        this.setPierceLevel(-1);
    }

    public MagicArrowProjectile(Level levelIn, LivingEntity shooter) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.MAGIC_ARROW_PROJECTILE.get()), levelIn);
        this.setOwner((Entity)shooter);
    }

    @Override
    public void trailParticles() {
        Vec3 vec = this.getDeltaMovement();
        double length = vec.length();
        int count = (int)Math.min(20L, Math.round(length) * 2L) + 1;
        float f = (float)length / (float)count;
        for (int i = 0; i < count; ++i) {
            Vec3 random = Utils.getRandomVec3(0.025);
            Vec3 p = vec.scale((double)(f * (float)i));
            this.level.addParticle(ParticleHelper.UNSTABLE_ENDER, this.getX() + random.x + p.x, this.getY() + random.y + p.y, this.getZ() + random.z + p.z, random.x, random.y, random.z);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.level, ParticleHelper.UNSTABLE_ENDER, x, y, z, 10, 0.1, 0.1, 0.1, 0.4, false);
    }

    @Override
    public float getSpeed() {
        return 2.7f;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }

    protected void onHitBlock(BlockHitResult pResult) {
    }

    @Override
    public void tick() {
        super.tick();
        this.hitsPerTick = 0;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        if (!this.victims.contains(entity)) {
            DamageSources.applyDamage(entity, this.damage, SpellRegistry.MAGIC_ARROW_SPELL.get().getDamageSource((Entity)this, this.getOwner()));
            this.victims.add(entity);
        }
        if (this.getPierceLevel() != 0) {
            HitResult hitresult;
            if (this.hitsPerTick++ < 5 && (hitresult = ProjectileUtil.getHitResultOnMoveVector((Entity)this, x$0 -> this.canHitEntity((Entity)x$0))).getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact((Projectile)this, (HitResult)hitresult)) {
                this.onHit(hitresult);
            }
            this.pierceOrDiscard();
        } else {
            this.discard();
        }
    }

    @Override
    protected void onHit(HitResult result) {
        if (!this.level.isClientSide) {
            BlockPos blockPos = BlockPos.containing((Position)result.getLocation());
            if (result.getType() == HitResult.Type.BLOCK && !blockPos.equals((Object)this.lastHitBlock)) {
                this.lastHitBlock = blockPos;
            } else if (result.getType() == HitResult.Type.ENTITY) {
                this.level.playSound(null, BlockPos.containing((Position)this.position()), (SoundEvent)SoundRegistry.FORCE_IMPACT.get(), SoundSource.NEUTRAL, 2.0f, 0.65f);
            }
        }
        super.onHit(result);
    }

    @Override
    protected boolean shouldPierceShields() {
        return true;
    }
}

