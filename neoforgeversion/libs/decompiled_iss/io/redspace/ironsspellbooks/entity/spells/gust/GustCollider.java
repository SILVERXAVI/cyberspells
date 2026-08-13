/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.protocol.game.ClientboundAddEntityPacket
 *  net.minecraft.server.level.ServerEntity
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.entity.spells.gust;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractConeProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class GustCollider
extends AbstractConeProjectile {
    public float strength;
    public float range;
    public int amplifier;

    public GustCollider(Level level, LivingEntity owner) {
        this((EntityType<GustCollider>)((EntityType)EntityRegistry.GUST_COLLIDER.get()), level);
        this.setOwner((Entity)owner);
        IronsSpellbooks.LOGGER.debug("GustCollider<init>: {} {}", (Object)Float.valueOf(owner.getYRot()), (Object)Float.valueOf(owner.getXRot()));
        this.setRot(owner.getYRot(), owner.getXRot());
    }

    public GustCollider(EntityType<GustCollider> gustColliderEntityType, Level level) {
        super(gustColliderEntityType, level);
    }

    @Override
    public void spawnParticles() {
        if (!this.level.isClientSide || this.tickCount > 2) {
            return;
        }
        Vec3 rotation = this.getLookAngle().normalize();
        Vec3 pos = this.position().add(rotation.scale(1.6));
        double x = pos.x;
        double y = pos.y;
        double z = pos.z;
        double speed = this.random.nextDouble() * 0.4 + 0.45;
        for (int i = 0; i < 5; ++i) {
            double offset = 0.25;
            double ox = Math.random() * 2.0 * offset - offset;
            double oy = Math.random() * 2.0 * offset - offset;
            double oz = Math.random() * 2.0 * offset - offset;
            double angularness = 0.8;
            Vec3 randomVec = new Vec3(Math.random() * 2.0 * angularness - angularness, Math.random() * 2.0 * angularness - angularness, Math.random() * 2.0 * angularness - angularness).normalize();
            Vec3 result = rotation.scale(3.0).add(randomVec).normalize().scale(speed);
            this.level.addParticle((ParticleOptions)ParticleTypes.POOF, x + ox, y + oy, z + oz, result.x, result.y, result.z);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        LivingEntity target;
        Entity entity = this.getOwner();
        Entity resultEntity = entityHitResult.getEntity();
        if (entity != null && resultEntity instanceof LivingEntity && (target = (LivingEntity)resultEntity).distanceToSqr(entity) < (double)(this.range * this.range) && !DamageSources.isFriendlyFireBetween(entity, (Entity)target)) {
            Vec3 knockback = new Vec3(entity.getX() - target.getX(), entity.getY() - target.getY(), entity.getZ() - target.getZ()).normalize().scale((double)(-this.strength));
            target.setDeltaMovement(target.getDeltaMovement().add(knockback));
            target.hurtMarked = true;
            target.addEffect(new MobEffectInstance(MobEffectRegistry.AIRBORNE, 60, this.amplifier));
        }
    }

    @Override
    public void tick() {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        if (this.tickCount > 8) {
            this.discard();
        } else {
            super.tick();
        }
        this.setPosRaw(x, y, z);
    }

    @Nullable
    public Entity getOwner() {
        if (this.tickCount >= 1) {
            return null;
        }
        return super.getOwner();
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity pEntity) {
        return super.getAddEntityPacket(pEntity);
    }

    public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
        super.recreateFromPacket(pPacket);
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
    }
}

