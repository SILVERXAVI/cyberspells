/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.monster.Enemy
 */
package io.redspace.ironsspellbooks.entity.mobs.goals;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Enemy;

public class GustDefenseGoal
extends Goal {
    protected final PathfinderMob mob;
    protected final IMagicEntity spellCastingMob;
    protected int attackCooldown = 0;

    public GustDefenseGoal(IMagicEntity abstractSpellCastingMob) {
        PathfinderMob m;
        this.spellCastingMob = abstractSpellCastingMob;
        if (!(abstractSpellCastingMob instanceof PathfinderMob)) {
            throw new IllegalStateException("Unable to add " + ((Object)((Object)this)).getClass().getSimpleName() + "to entity, must extend PathfinderMob.");
        }
        this.mob = m = (PathfinderMob)abstractSpellCastingMob;
    }

    public boolean canUse() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null && --this.attackCooldown <= 0 && livingentity.isAlive() && this.shouldAreaAttack(livingentity)) {
            return false;
        }
        return false;
    }

    public boolean shouldAreaAttack(LivingEntity livingEntity) {
        boolean inRange;
        if (this.spellCastingMob.isCasting()) {
            return false;
        }
        double d = livingEntity.distanceToSqr((Entity)this.mob);
        boolean bl = inRange = d < 25.0;
        if (!inRange) {
            return false;
        }
        if (livingEntity.getType() == EntityType.VINDICATOR) {
            this.start();
            return false;
        }
        if (this.mob.getHealth() / this.mob.getMaxHealth() < 0.25f && this.mob.level.getEntities((Entity)this.mob, this.mob.getBoundingBox().inflate(3.0), entity -> entity instanceof Enemy).size() > 1) {
            this.start();
            return false;
        }
        int mobCount = livingEntity.level.getEntities((Entity)livingEntity, livingEntity.getBoundingBox().inflate(6.0), entity -> entity instanceof Enemy).size();
        if (mobCount >= 2) {
            this.start();
        }
        return false;
    }

    public void start() {
        this.attackCooldown = 40 + this.mob.getRandom().nextInt(30);
        int spellLevel = (int)((float)SpellRegistry.GUST_SPELL.get().getMaxLevel() * 0.5f);
        AbstractSpell spellType = SpellRegistry.GUST_SPELL.get();
        this.spellCastingMob.initiateCastSpell(spellType, spellLevel);
    }
}

