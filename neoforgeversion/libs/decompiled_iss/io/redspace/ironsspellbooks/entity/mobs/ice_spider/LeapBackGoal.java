/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.mobs.ice_spider;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.ice_spider.IceSpiderEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.AnimatedActionGoal;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;

public class LeapBackGoal
extends AnimatedActionGoal<IceSpiderEntity> {
    public LeapBackGoal(IceSpiderEntity mob) {
        super(mob);
    }

    @Override
    protected boolean canStartAction() {
        return ((IceSpiderEntity)this.mob).wantsToLeapBack;
    }

    @Override
    protected int getActionTimestamp() {
        return 0;
    }

    @Override
    protected int getActionDuration() {
        return 10;
    }

    @Override
    protected int getCooldown() {
        return 0;
    }

    @Override
    protected String getAnimationId() {
        return "leap_back";
    }

    @Override
    protected void doAction() {
        ((IceSpiderEntity)this.mob).playSound((SoundEvent)SoundRegistry.ICE_SPIDER_SWING.get(), 3.0f, (float)Utils.random.nextIntBetweenInclusive(13, 16) * 0.1f);
        Vec3 leapVector = new Vec3(0.0, 0.5, -2.2);
        ((IceSpiderEntity)this.mob).push(((IceSpiderEntity)this.mob).rotateWithBody(leapVector));
        ((IceSpiderEntity)this.mob).wantsToLeapBack = false;
    }
}

