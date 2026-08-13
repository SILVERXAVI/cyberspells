/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.monster.Enemy
 *  net.minecraft.world.level.Level
 */
package io.redspace.ironsspellbooks.entity.mobs.debug_wizard;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.DebugTargetClosestEntityGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.DebugWizardAttackGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

public class DebugWizard
extends AbstractSpellCastingMob
implements Enemy {
    private AbstractSpell spell;
    private int spellLevel;
    private boolean targetsPlayer;
    private String spellInfo = "No Spell Found";
    private int cancelCastAfterTicks;
    private static final EntityDataAccessor<String> DEBUG_SPELL_INFO = SynchedEntityData.defineId(DebugWizard.class, (EntityDataSerializer)EntityDataSerializers.STRING);

    public DebugWizard(EntityType<? extends AbstractSpellCastingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public DebugWizard(EntityType<? extends AbstractSpellCastingMob> pEntityType, Level pLevel, AbstractSpell spell, int spellLevel, boolean targetsPlayer, int cancelCastAfterTicks) {
        super(pEntityType, pLevel);
        this.targetsPlayer = targetsPlayer;
        this.spellLevel = spellLevel;
        this.spell = spell;
        this.cancelCastAfterTicks = cancelCastAfterTicks;
        this.initGoals();
    }

    public String getSpellInfo() {
        return this.spellInfo;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DEBUG_SPELL_INFO, (Object)"DEFAULT");
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        super.onSyncedDataUpdated(pKey);
        if (!this.level().isClientSide) {
            return;
        }
        if (pKey.id() == DEBUG_SPELL_INFO.id()) {
            this.spellInfo = (String)this.entityData.get(DEBUG_SPELL_INFO);
        }
    }

    private void initGoals() {
        this.goalSelector.addGoal(1, (Goal)new DebugWizardAttackGoal(this, this.spell, this.spellLevel, this.cancelCastAfterTicks));
        if (this.targetsPlayer) {
            IronsSpellbooks.LOGGER.debug("DebugWizard: Adding DebugTargetClosestEntityGoal");
            this.targetSelector.addGoal(1, (Goal)new DebugTargetClosestEntityGoal((Mob)this));
        }
        this.entityData.set(DEBUG_SPELL_INFO, (Object)String.format("%s (L%s)", this.spell.getSpellName(), this.spellLevel));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putString("spellId", this.spell.getSpellId());
        pCompound.putInt("spellLevel", this.spellLevel);
        pCompound.putBoolean("targetsPlayer", this.targetsPlayer);
        pCompound.putInt("cancelCastAfterTicks", this.cancelCastAfterTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.spell = SpellRegistry.getSpell(pCompound.getString("spellId"));
        this.spellLevel = pCompound.getInt("spellLevel");
        this.targetsPlayer = pCompound.getBoolean("targetsPlayer");
        this.cancelCastAfterTicks = pCompound.getInt("cancelCastAfterTicks");
        this.initGoals();
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.MAX_HEALTH, 30.0).add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.MOVEMENT_SPEED, 0.4);
    }
}

