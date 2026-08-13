/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.util.Mth
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.ThrownPotion
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.alchemy.Potion
 *  net.minecraft.world.item.alchemy.PotionContents
 *  net.minecraft.world.item.alchemy.Potions
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.mobs.goals;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.phys.Vec3;

public class AlchemistAttackGoal
extends WizardAttackGoal {
    protected float throwRangeSqr;
    protected float throwRange;
    protected float potionBias;
    public static final List<Holder<MobEffect>> ATTACK_POTIONS = List.of(MobEffects.WEAKNESS, MobEffects.BLINDNESS, MobEffects.LEVITATION, MobEffects.MOVEMENT_SLOWDOWN, MobEffects.DIG_SLOWDOWN);

    public AlchemistAttackGoal(IMagicEntity abstractSpellCastingMob, double pSpeedModifier, int minAttackInterval, int maxAttackInterval, float throwRange, float potionBias) {
        super(abstractSpellCastingMob, pSpeedModifier, minAttackInterval, maxAttackInterval);
        this.throwRange = throwRange;
        this.throwRangeSqr = throwRange * throwRange;
        this.spellcastingRange = throwRange - 2.0f;
        this.spellcastingRangeSqr = this.spellcastingRange * this.spellcastingRange;
        this.potionBias = potionBias;
    }

    @Override
    public AlchemistAttackGoal setSpells(List<AbstractSpell> attackSpells, List<AbstractSpell> defenseSpells, List<AbstractSpell> movementSpells, List<AbstractSpell> supportSpells) {
        return (AlchemistAttackGoal)super.setSpells(attackSpells, defenseSpells, movementSpells, supportSpells);
    }

    @Override
    public AlchemistAttackGoal setSpellQuality(float minSpellQuality, float maxSpellQuality) {
        return (AlchemistAttackGoal)super.setSpellQuality(minSpellQuality, maxSpellQuality);
    }

    @Override
    public AlchemistAttackGoal setSingleUseSpell(AbstractSpell spellType, int minDelay, int maxDelay, int minLevel, int maxLevel) {
        return (AlchemistAttackGoal)super.setSingleUseSpell(spellType, minDelay, maxDelay, minLevel, maxLevel);
    }

    @Override
    public AlchemistAttackGoal setIsFlying() {
        return (AlchemistAttackGoal)super.setIsFlying();
    }

    @Override
    protected void doSpellAction() {
        if (this.mob.distanceToSqr((Entity)this.target) < (double)this.throwRangeSqr && this.mob.getRandom().nextFloat() < this.potionBias) {
            int attackWeight = this.getAttackWeight();
            int supportWeight = this.getSupportWeight();
            ItemStack potion = new ItemStack((ItemLike)Items.SPLASH_POTION);
            LivingEntity targetedEntity = this.target;
            if (this.hasLineOfSight && this.mob.getRandom().nextFloat() * (float)(attackWeight + supportWeight) > (float)supportWeight) {
                Holder<MobEffect> effect;
                int amplifier = (this.mob.getRandom().nextFloat() < 0.75f ? 0 : 1) + (this.target.getMaxHealth() > 30.0f ? (this.mob.getRandom().nextFloat() < 0.5f ? 0 : 1) : 0);
                Object object = effect = this.target.isInvertedHealAndHarm() ? MobEffects.HEAL : MobEffects.HARM;
                if (this.mob.getRandom().nextFloat() < 0.45f) {
                    for (int i = 0; i < ATTACK_POTIONS.size(); ++i) {
                        int p = this.mob.getRandom().nextInt(ATTACK_POTIONS.size());
                        if (this.target.hasEffect(ATTACK_POTIONS.get(p))) continue;
                        effect = ATTACK_POTIONS.get(p);
                        break;
                    }
                }
                potion.set(DataComponents.POTION_CONTENTS, (Object)new PotionContents(Potions.WATER).withEffectAdded(new MobEffectInstance((Holder)effect, ((MobEffect)effect.value()).isInstantenous() ? 0 : 200, amplifier)));
            } else {
                Utils.setPotion(potion, (Holder<Potion>)Potions.STRONG_HEALING);
                targetedEntity = this.mob;
            }
            ThrownPotion thrownpotion = new ThrownPotion(this.mob.level, (LivingEntity)this.mob);
            thrownpotion.setItem(potion);
            thrownpotion.setXRot(thrownpotion.getXRot() - -20.0f);
            Vec3 vec3 = targetedEntity.getDeltaMovement();
            double d0 = targetedEntity.getX() + vec3.x - this.mob.getX();
            double d1 = targetedEntity.getEyeY() - (double)1.1f - this.mob.getEyeY();
            double d2 = targetedEntity.getZ() + vec3.z - this.mob.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            thrownpotion.shoot(d0, d1 + d3 * 0.2, d2, (float)Mth.clampedLerp((double)0.5, (double)1.25, (double)(this.mob.distanceToSqr((Entity)targetedEntity) / (double)this.throwRangeSqr)), 8.0f);
            if (!this.mob.isSilent()) {
                this.mob.level.playSound(null, this.mob.getX(), this.mob.getY(), this.mob.getZ(), SoundEvents.WITCH_THROW, this.mob.getSoundSource(), 1.0f, 0.8f + this.mob.getRandom().nextFloat() * 0.4f);
            }
            this.mob.level.addFreshEntity((Entity)thrownpotion);
            this.mob.swing(InteractionHand.MAIN_HAND, true);
        } else {
            super.doSpellAction();
        }
    }
}

