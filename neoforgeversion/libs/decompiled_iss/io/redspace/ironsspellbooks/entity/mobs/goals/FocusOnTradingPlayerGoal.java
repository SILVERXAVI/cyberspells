/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.trading.Merchant
 */
package io.redspace.ironsspellbooks.entity.mobs.goals;

import io.redspace.ironsspellbooks.entity.mobs.wizards.IMerchantWizard;
import java.util.EnumSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.Merchant;

public class FocusOnTradingPlayerGoal<T extends PathfinderMob>
extends Goal {
    final T mob;
    int hurtTracker;

    public FocusOnTradingPlayerGoal(T mob) {
        this.setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
        this.mob = mob;
    }

    public boolean canUse() {
        return ((Merchant)this.mob).getTradingPlayer() != null;
    }

    public void start() {
        super.start();
        this.mob.getNavigation().stop();
        this.hurtTracker = this.mob.getLastHurtByMobTimestamp();
    }

    public void tick() {
        Player player = ((Merchant)this.mob).getTradingPlayer();
        if (player == null || player.isDeadOrDying() || player.isRemoved()) {
            this.stop();
            return;
        }
        this.mob.getLookControl().setLookAt((Entity)player);
        if (this.mob.getLastHurtByMobTimestamp() != this.hurtTracker) {
            ((IMerchantWizard)this.mob).stopTrading();
            this.stop();
        }
    }
}

