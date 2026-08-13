/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.FireworkRocketEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ExtendedFireworkRocket
extends FireworkRocketEntity
implements AntiMagicSusceptible {
    private final float damage;

    public ExtendedFireworkRocket(Level pLevel, ItemStack pStack, Entity pShooter, double pX, double pY, double pZ, boolean pShotAtAngle, float damage) {
        super(pLevel, pStack, pShooter, pX, pY, pZ, pShotAtAngle);
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }

    public void tick() {
    }

    public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
        this.explode();
    }

    private void explode() {
        this.level.broadcastEntityEvent((Entity)this, (byte)17);
        this.gameEvent((Holder)GameEvent.EXPLODE, this.getOwner());
        this.dealExplosionDamage();
        this.discard();
    }

    private void dealExplosionDamage() {
        Vec3 hitPos = this.position();
        double explosionRadius = 2.0;
        for (LivingEntity livingentity : this.level.getEntitiesOfClass(LivingEntity.class, new AABB(hitPos.subtract(explosionRadius, explosionRadius, explosionRadius), hitPos.add(explosionRadius, explosionRadius, explosionRadius)))) {
            if (!livingentity.isAlive() || !livingentity.isPickable() || !Utils.hasLineOfSight(this.level, hitPos, livingentity.getBoundingBox().getCenter(), true)) continue;
            DamageSources.applyDamage((Entity)livingentity, this.getDamage(), SpellRegistry.FIRECRACKER_SPELL.get().getDamageSource((Entity)this, this.getOwner()));
        }
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        this.discard();
    }
}

