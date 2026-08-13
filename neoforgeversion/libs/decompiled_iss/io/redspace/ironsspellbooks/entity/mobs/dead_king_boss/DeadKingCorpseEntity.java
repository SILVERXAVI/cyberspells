/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.util.Mth
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.phys.Vec3
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animation.AnimatableManager$ControllerRegistrar
 *  software.bernie.geckolib.animation.AnimationController
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.animation.PlayState
 *  software.bernie.geckolib.animation.RawAnimation
 */
package io.redspace.ironsspellbooks.entity.mobs.dead_king_boss;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingAmbienceSoundManager;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingBoss;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public class DeadKingCorpseEntity
extends AbstractSpellCastingMob {
    DeadKingAmbienceSoundManager ambienceSoundManager;
    private static final EntityDataAccessor<Boolean> TRIGGERED = SynchedEntityData.defineId(DeadKingCorpseEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private int currentAnimTime;
    private final int animLength = 300;
    private final RawAnimation idle = RawAnimation.begin().thenLoop("dead_king_rest");
    private final RawAnimation rise = RawAnimation.begin().thenPlay("dead_king_rise");

    public DeadKingCorpseEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPersistenceRequired();
    }

    public boolean isPickable() {
        return true;
    }

    public boolean isPushable() {
        return false;
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    public boolean isPersistenceRequired() {
        return true;
    }

    public void tick() {
        super.tick();
        if (this.triggered()) {
            ++this.currentAnimTime;
            if (!this.level().isClientSide) {
                if (this.currentAnimTime > 300) {
                    DeadKingBoss boss = new DeadKingBoss(this.level());
                    boss.moveTo(this.position().add(0.0, 1.0, 0.0));
                    boss.finalizeSpawn((ServerLevelAccessor)((ServerLevel)this.level()), this.level().getCurrentDifficultyAt(boss.getOnPos()), MobSpawnType.TRIGGERED, null);
                    int playerCount = Math.max(this.level().getEntitiesOfClass(Player.class, boss.getBoundingBox().inflate(32.0)).size(), 1);
                    ResourceLocation attributeId = IronsSpellbooks.id("gank_bonus");
                    boss.getAttributes().getInstance(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(attributeId, (double)(playerCount - 1) * 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                    boss.setHealth(boss.getMaxHealth());
                    boss.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier(attributeId, (double)(playerCount - 1) * 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                    boss.getAttributes().getInstance(AttributeRegistry.SPELL_RESIST).addPermanentModifier(new AttributeModifier(attributeId, (double)(playerCount - 1) * 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                    boss.setPersistenceRequired();
                    this.level.addFreshEntity((Entity)boss);
                    MagicManager.spawnParticles(this.level(), (ParticleOptions)ParticleTypes.SCULK_SOUL, this.position().x, this.position().y + 2.5, this.position().z, 80, 0.2, 0.2, 0.2, 0.25, true);
                    this.level.playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)SoundRegistry.DEAD_KING_SPAWN.get(), SoundSource.MASTER, 20.0f, 1.0f);
                    this.discard();
                }
            } else {
                this.resurrectParticles();
            }
        } else if (this.level.isClientSide && this.tickCount % 40 == 0) {
            MinecraftInstanceHelper.ifPlayerPresent(player -> {
                float yRot = this.getYRot();
                Vec3 musicCenter = this.position().add((double)(-15.0f * Mth.sin((float)(yRot * ((float)Math.PI / 180)))), 0.0, (double)(15.0f * Mth.cos((float)(yRot * ((float)Math.PI / 180)))));
                if (musicCenter.distanceToSqr(player.position()) < 400.0) {
                    if (this.ambienceSoundManager == null) {
                        this.ambienceSoundManager = new DeadKingAmbienceSoundManager(musicCenter);
                    }
                    this.ambienceSoundManager.trigger();
                }
            });
        }
    }

    private void resurrectParticles() {
        float f = (float)this.currentAnimTime / 300.0f;
        float rot = (float)(this.currentAnimTime * 12) + (1.0f + f * 15.0f);
        float height = f * 4.0f + 0.4f * Mth.sin((float)((float)(this.currentAnimTime * 30) * ((float)Math.PI / 180))) * f * f;
        float distance = Mth.clamp((float)Utils.smoothstep(0.0f, 1.15f, f * 3.0f), (float)0.0f, (float)1.15f);
        Vec3 pos = new Vec3(0.0, 0.0, (double)distance).yRot(rot * ((float)Math.PI / 180)).add(0.0, (double)height, 0.0).add(this.position());
        this.level.addParticle((ParticleOptions)ParticleTypes.SCULK_SOUL, pos.x, pos.y, pos.z, 0.0, 0.0, 0.0);
        float radius = 4.0f;
        if (this.random.nextFloat() < f * 1.5f) {
            Vec3 random = this.position().add(new Vec3((double)((this.random.nextFloat() * 2.0f - 1.0f) * radius), 3.5 + (double)((this.random.nextFloat() * 2.0f - 1.0f) * radius), (double)((this.random.nextFloat() * 2.0f - 1.0f) * radius)));
            Vec3 motion = this.position().subtract(random).scale((double)0.04f);
            this.level.addParticle((ParticleOptions)ParticleTypes.SCULK_SOUL, random.x, random.y, random.z, motion.x, motion.y, motion.z);
        }
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            this.discard();
            return true;
        }
        Player player = this.level.getNearestPlayer((Entity)this, 8.0);
        if (player != null) {
            this.trigger();
        }
        return false;
    }

    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (!this.triggered()) {
            this.trigger();
            return InteractionResult.sidedSuccess((boolean)this.level().isClientSide);
        }
        return super.mobInteract(pPlayer, pHand);
    }

    private void trigger() {
        if (!this.triggered()) {
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)SoundRegistry.DEAD_KING_RESURRECT.get(), SoundSource.AMBIENT, 2.0f, 1.0f);
            this.entityData.set(TRIGGERED, (Object)true);
            if (this.ambienceSoundManager != null) {
                this.ambienceSoundManager.triggerStop();
            }
        }
    }

    public boolean triggered() {
        return (Boolean)this.entityData.get(TRIGGERED);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(TRIGGERED, (Object)false);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController((GeoAnimatable)this, "idle", 0, this::idlePredicate));
    }

    private PlayState idlePredicate(AnimationState event) {
        if (this.triggered()) {
            event.getController().setAnimation(this.rise);
        } else {
            event.getController().setAnimation(this.idle);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public boolean shouldBeExtraAnimated() {
        return false;
    }

    @Override
    public boolean shouldAlwaysAnimateHead() {
        return false;
    }
}

