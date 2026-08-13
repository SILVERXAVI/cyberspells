/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 */
package io.redspace.ironsspellbooks.entity.spells.summoned_weapons;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackAnimationData;
import io.redspace.ironsspellbooks.entity.mobs.wizards.GenericAnimatedWarlockAttackGoal;
import io.redspace.ironsspellbooks.entity.spells.summoned_weapons.SummonedWeaponEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SummonedRapierEntity
extends SummonedWeaponEntity {
    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_KNOCKBACK, 1.0).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.MAX_HEALTH, 15.0).add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.FLYING_SPEED, 2.2).add(Attributes.ENTITY_INTERACTION_RANGE, 4.0).add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    public GenericAnimatedWarlockAttackGoal<? extends SummonedWeaponEntity> makeAttackGoal() {
        return new GenericAnimatedWarlockAttackGoal<SummonedRapierEntity>(this, 1.5, 0, 20).setMoveset(List.of(new AttackAnimationData(40, "summoned_sword_multistab", 20, 26, 32)));
    }

    public SummonedRapierEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SummonedRapierEntity(Level level, LivingEntity owner) {
        this((EntityType<? extends PathfinderMob>)((EntityType)EntityRegistry.SUMMONED_RAPIER.get()), level);
        this.setSummoner(owner);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!this.level.isClientSide && pSource.getEntity() != null && !pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            if (this.shouldIgnoreDamage(pSource)) {
                return false;
            }
            if (this.random.nextFloat() < 0.4f) {
                this.performSidestep(pSource.getEntity());
                return false;
            }
        }
        return super.hurt(pSource, pAmount);
    }

    public void performSidestep(Entity damager) {
        EntityDimensions dimensions;
        boolean direction = this.random.nextBoolean();
        Vec3 delta = this.position().subtract(damager.position());
        Vec3 targetPos = delta.lengthSqr() < 49.0 ? damager.position().add(delta.yRot(direction ? -1.5707964f : 1.5707964f)) : this.position().add(new Vec3(direction ? -2.0 : 2.0, 0.0, -0.25).yRot(this.getYRot()));
        Vec3 vec3 = targetPos.add(0.0, (double)(dimensions = this.getDimensions(this.getPose())).height() / 2.0, 0.0);
        VoxelShape voxelshape = Shapes.create((AABB)AABB.ofSize((Vec3)vec3, (double)(dimensions.width() + 0.2f), (double)(dimensions.height() + 0.2f), (double)(dimensions.width() + 0.2f)));
        Optional optional = this.level.findFreePosition(null, voxelshape, vec3, (double)dimensions.width(), (double)dimensions.height(), (double)dimensions.width());
        if (optional.isPresent()) {
            targetPos = ((Vec3)optional.get()).add(0.0, (double)(-dimensions.height() / 2.0f) + 1.0E-6, 0.0);
        }
        if (this.level.collidesWithSuffocatingBlock(null, AABB.ofSize((Vec3)targetPos.add(0.0, (double)(dimensions.height() / 2.0f), 0.0), (double)dimensions.width(), (double)dimensions.height(), (double)dimensions.width()))) {
            targetPos = this.position();
        }
        if (this.isPassenger()) {
            this.stopRiding();
        }
        MagicManager.spawnParticles(this.level, ParticleHelper.UNSTABLE_ENDER, this.getX(), this.getY(), this.getZ(), 25, 0.1, 0.1, 0.1, 0.1, false);
        this.teleportTo(targetPos.x, targetPos.y, targetPos.z);
        MagicManager.spawnParticles(this.level, ParticleHelper.UNSTABLE_ENDER, this.getX(), this.getY(), this.getZ(), 25, 0.1, 0.1, 0.1, 0.1, false);
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ILLUSIONER_MIRROR_MOVE, SoundSource.AMBIENT, 1.0f, 0.9f + this.random.nextFloat() * 0.2f);
    }
}

