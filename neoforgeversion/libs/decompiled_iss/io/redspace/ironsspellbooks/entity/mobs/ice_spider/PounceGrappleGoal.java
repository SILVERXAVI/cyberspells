/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.mobs.ice_spider;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.ice_spider.IceSpiderEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.AnimatedActionGoal;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class PounceGrappleGoal
extends AnimatedActionGoal<IceSpiderEntity> {
    private static final AttributeModifier TELEGRAPH_SPEED_MODIFIER = new AttributeModifier(IronsSpellbooks.id("pouncing"), -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static final int DAMAGER_START = 30;
    private static final int DAMAGER_END = 35;

    public PounceGrappleGoal(IceSpiderEntity mob) {
        super(mob);
    }

    @Override
    protected boolean canStartAction() {
        return !((IceSpiderEntity)this.mob).isCrouching() && ((IceSpiderEntity)this.mob).isAggressive() && ((IceSpiderEntity)this.mob).getTarget() != null && ((double)Utils.random.nextFloat() < 0.05 || ((IceSpiderEntity)this.mob).distanceToSqr((Entity)((IceSpiderEntity)this.mob).getTarget()) > 25.0);
    }

    @Override
    protected int getActionTimestamp() {
        return 20;
    }

    @Override
    protected int getActionDuration() {
        return 40;
    }

    @Override
    protected int getCooldown() {
        return ((IceSpiderEntity)this.mob).getRandom().nextIntBetweenInclusive(3, 8) * 20;
    }

    @Override
    protected String getAnimationId() {
        return "attack_grapple_pounce";
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = ((IceSpiderEntity)this.mob).getTarget();
        if (target == null) {
            return;
        }
        ((IceSpiderEntity)this.mob).attackGoal.setTarget(((IceSpiderEntity)this.mob).getTarget());
        ((IceSpiderEntity)this.mob).attackGoal.doMovement(((IceSpiderEntity)this.mob).distanceToSqr((Entity)((IceSpiderEntity)this.mob).getTarget()));
        if (this.abilityTimer == 30) {
            ((IceSpiderEntity)this.mob).playSound((SoundEvent)SoundRegistry.ICE_SPIDER_BITE.get());
        }
        if (this.abilityTimer >= 30 && this.abilityTimer <= 35) {
            double meleeRange = ((IceSpiderEntity)this.mob).getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE) * (double)((IceSpiderEntity)this.mob).getScale();
            if (target.distanceToSqr((Entity)this.mob) <= meleeRange * meleeRange && Utils.hasLineOfSight(((IceSpiderEntity)this.mob).level, (Entity)this.mob, (Entity)target, true)) {
                if (((IceSpiderEntity)this.mob).doHurtTarget((Entity)target)) {
                    if (target.isBlocking() && target instanceof Player) {
                        Player player = (Player)target;
                        player.disableShield();
                    } else {
                        ((IceSpiderEntity)this.mob).startGrapple((Entity)target);
                        ((IceSpiderEntity)this.mob).playSound((SoundEvent)SoundRegistry.ICE_SPIDER_GRAPPLE_LATCH.get());
                    }
                }
                this.stop();
            }
        }
    }

    public boolean isInterruptable() {
        return false;
    }

    @Override
    protected void doAction() {
        Vec3 leapVector = new Vec3(0.0, 0.5, 1.5);
        LivingEntity target = ((IceSpiderEntity)this.mob).getTarget();
        if (target == null) {
            return;
        }
        Vec3 power = Utils.lerp(Math.clamp((float)(((IceSpiderEntity)this.mob).distanceTo((Entity)target) / 18.0f), (float)0.0f, (float)1.0f), new Vec3(0.125, 0.25, 0.125), new Vec3(3.0, 1.2, 3.0));
        Vec3 lunge = leapVector.multiply(power.x, power.y, power.z).yRot(-Utils.getAngle(((IceSpiderEntity)this.mob).getX(), ((IceSpiderEntity)this.mob).getZ(), target.getX(), target.getZ()) - 1.5707964f);
        ((IceSpiderEntity)this.mob).push(lunge);
        ((IceSpiderEntity)this.mob).getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(TELEGRAPH_SPEED_MODIFIER);
        ((IceSpiderEntity)this.mob).playSound((SoundEvent)SoundRegistry.ICE_SPIDER_SWING.get(), 3.0f, (float)Utils.random.nextIntBetweenInclusive(13, 16) * 0.1f);
        ((IceSpiderEntity)this.mob).playSound((SoundEvent)SoundRegistry.ICE_SPIDER_AMBIENT.get(), 3.0f, (float)Utils.random.nextIntBetweenInclusive(14, 20) * 0.1f);
    }

    @Override
    public void start() {
        super.start();
        ((IceSpiderEntity)this.mob).getAttribute(Attributes.MOVEMENT_SPEED).addOrUpdateTransientModifier(TELEGRAPH_SPEED_MODIFIER);
    }
}

