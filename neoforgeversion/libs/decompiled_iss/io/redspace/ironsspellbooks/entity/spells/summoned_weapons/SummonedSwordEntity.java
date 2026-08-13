/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.level.Level
 */
package io.redspace.ironsspellbooks.entity.spells.summoned_weapons;

import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackAnimationData;
import io.redspace.ironsspellbooks.entity.mobs.wizards.GenericAnimatedWarlockAttackGoal;
import io.redspace.ironsspellbooks.entity.spells.summoned_weapons.SummonedWeaponEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import java.util.List;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class SummonedSwordEntity
extends SummonedWeaponEntity {
    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_KNOCKBACK, 1.0).add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.MAX_HEALTH, 25.0).add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.FLYING_SPEED, 1.5).add(Attributes.ENTITY_INTERACTION_RANGE, 4.0).add(Attributes.MOVEMENT_SPEED, 0.5);
    }

    public SummonedSwordEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SummonedSwordEntity(Level level, LivingEntity owner) {
        this((EntityType<? extends PathfinderMob>)((EntityType)EntityRegistry.SUMMONED_SWORD.get()), level);
        this.setSummoner(owner);
    }

    public GenericAnimatedWarlockAttackGoal<SummonedSwordEntity> makeAttackGoal() {
        return new GenericAnimatedWarlockAttackGoal<SummonedSwordEntity>(this, 1.5, 0, 20).setMoveset(List.of(new AttackAnimationData(36, "summoned_sword_basic_swing", 20), new AttackAnimationData(52, "summoned_sword_basic_dual_swing", 20, 35)));
    }
}

