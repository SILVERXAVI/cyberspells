/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.LivingEntity
 */
package io.redspace.ironsspellbooks.entity.mobs.ice_spider;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.ice_spider.IceSpiderEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.GenericAnimatedWarlockAttackGoal;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;

public class IceSpiderAttackGoal
extends GenericAnimatedWarlockAttackGoal<IceSpiderEntity> {
    public IceSpiderAttackGoal(IceSpiderEntity abstractSpellCastingMob, double pSpeedModifier, int minAttackInterval, int maxAttackInterval) {
        super(abstractSpellCastingMob, pSpeedModifier, minAttackInterval, maxAttackInterval);
    }

    @Override
    public void tick() {
        this.wantsToMelee = !((IceSpiderEntity)this.mob).wantsToCastSpells;
        super.tick();
    }

    @Override
    public void handleAttackLogic(double distanceSquared) {
        if (((IceSpiderEntity)this.mob).getGrappleTargetUUID() != null) {
            return;
        }
        super.handleAttackLogic(distanceSquared);
    }

    @Override
    public void playSwingSound() {
        if (this.currentAttack != null && this.currentAttack.animationId.contains("bite")) {
            ((IceSpiderEntity)this.mob).playSound((SoundEvent)SoundRegistry.ICE_SPIDER_BITE.get());
        }
        ((IceSpiderEntity)this.mob).playSound((SoundEvent)SoundRegistry.ICE_SPIDER_SWING.get(), 1.0f, (float)Utils.random.nextIntBetweenInclusive(9, 11) * 0.1f);
    }

    @Override
    public void playImpactSound() {
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    @Override
    public void doMovement(double distanceSquared) {
        super.doMovement(distanceSquared);
    }
}

