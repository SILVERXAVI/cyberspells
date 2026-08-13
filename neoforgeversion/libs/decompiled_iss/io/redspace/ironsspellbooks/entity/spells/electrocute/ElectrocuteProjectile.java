/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells.electrocute;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractConeProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ElectrocuteProjectile
extends AbstractConeProjectile {
    private List<Vec3> beamVectors;

    public ElectrocuteProjectile(EntityType<? extends AbstractConeProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public ElectrocuteProjectile(Level level, LivingEntity entity) {
        super((EntityType<? extends AbstractConeProjectile>)((EntityType)EntityRegistry.ELECTROCUTE_PROJECTILE.get()), level, entity);
    }

    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return super.shouldRenderAtSqrDistance(pDistance);
    }

    public boolean shouldRender(double pX, double pY, double pZ) {
        return super.shouldRender(pX, pY, pZ);
    }

    public void generateLightningBeams() {
        Random random = new Random();
        this.beamVectors = new ArrayList<Vec3>();
        Vec3 coreStart = new Vec3(0.0, 0.0, 0.0);
        int coreLength = random.nextInt(3) + 7;
        for (int core = 0; core < coreLength; ++core) {
            float width = Mth.lerp((float)((float)core / (float)coreLength), (float)2.0f, (float)4.0f);
            Vec3 coreEnd = coreStart.add(0.0, 0.0, 1.0).add(ElectrocuteProjectile.randomVector(0.3f).multiply((double)width, 1.0, (double)width));
            this.beamVectors.add(coreStart);
            this.beamVectors.add(coreEnd);
            coreStart = coreEnd;
            int branchSegments = random.nextInt(3) + 1;
            this.beamVectors.addAll(ElectrocuteProjectile.generateBranch(coreEnd, branchSegments, 0.5f, 1));
        }
    }

    public static List<Vec3> generateBranch(Vec3 origin, int maxLength, float splitChance, int recursionCount) {
        ArrayList<Vec3> branchSegements = new ArrayList<Vec3>();
        Random random = new Random();
        int branches = random.nextInt(maxLength + 1);
        Vec3 branchStart = origin;
        int dir = random.nextBoolean() ? 1 : -1;
        float branchLength = 1.75f / (float)(recursionCount + 1);
        for (int i = 0; i < branches; ++i) {
            Vec3 branchEnd = branchStart.add((double)((float)dir * branchLength), 0.0, (double)branchLength).add(ElectrocuteProjectile.randomVector(0.4f));
            branchSegements.add(branchStart);
            branchSegements.add(branchEnd);
            if (random.nextFloat() <= splitChance) {
                branchSegements.addAll(ElectrocuteProjectile.generateBranch(branchEnd, maxLength - 1, splitChance * 1.2f, recursionCount + 1));
            }
            branchStart = branchEnd;
        }
        return branchSegements;
    }

    public int getAge() {
        return this.age;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            this.generateLightningBeams();
        }
    }

    public static Vec3 randomVector(float radius) {
        double x = Math.random() * 2.0 * (double)radius - (double)radius;
        double y = Math.random() * 2.0 * (double)radius - (double)radius;
        double z = Math.random() * 2.0 * (double)radius - (double)radius;
        return new Vec3(x, y, z);
    }

    public List<Vec3> getBeamCache() {
        if (this.beamVectors == null) {
            this.generateLightningBeams();
        }
        return this.beamVectors;
    }

    @Override
    public void spawnParticles() {
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        DamageSources.applyDamage(entity, this.damage, SpellRegistry.ELECTROCUTE_SPELL.get().getDamageSource((Entity)this, this.getOwner()));
        MagicManager.spawnParticles(this.level(), ParticleHelper.ELECTRICITY, entity.getX(), entity.getY() + (double)(entity.getBbHeight() / 2.0f), entity.getZ(), 10, entity.getBbWidth() / 3.0f, entity.getBbHeight() / 3.0f, entity.getBbWidth() / 3.0f, 0.1, false);
    }
}

