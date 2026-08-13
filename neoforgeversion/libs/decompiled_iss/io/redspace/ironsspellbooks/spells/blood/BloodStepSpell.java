/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.commands.arguments.EntityAnchorArgument$Anchor
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Position
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.spells.blood;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.spells.ender.TeleportSpell;
import java.util.List;
import java.util.Optional;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BloodStepSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"blood_step");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.BLOOD_RESOURCE).setMaxLevel(5).setCooldownSeconds(12.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.distance", (Object[])new Object[]{Utils.stringTruncation(this.getDistance(spellLevel, caster), 1)}));
    }

    public BloodStepSpell() {
        this.baseSpellPower = 12;
        this.spellPowerPerLevel = 4;
        this.baseManaCost = 30;
        this.manaCostPerLevel = 10;
        this.castTime = 0;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.empty();
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of((SoundEvent)SoundRegistry.BLOOD_STEP.get());
    }

    @Override
    public void onClientPreCast(Level level, int spellLevel, LivingEntity entity, InteractionHand hand, @Nullable MagicData playerMagicData) {
        super.onClientPreCast(level, spellLevel, entity, hand, playerMagicData);
        Vec3 forward = entity.getForward().normalize();
        for (int i = 0; i < 35; ++i) {
            Vec3 motion = forward.scale(Utils.random.nextDouble() * 0.25);
            level.addParticle((ParticleOptions)ParticleTypes.CAMPFIRE_COSY_SMOKE, entity.getRandomX((double)0.4f), entity.getRandomY(), entity.getRandomZ((double)0.4f), motion.x, motion.y, motion.z);
        }
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        Vec3 dest = null;
        TeleportSpell.TeleportData teleportData = (TeleportSpell.TeleportData)playerMagicData.getAdditionalCastData();
        if (teleportData != null) {
            Vec3 potentialTarget = teleportData.getTeleportTargetPosition();
            if (potentialTarget != null) {
                dest = potentialTarget;
                Utils.handleSpellTeleport(this, (Entity)entity, dest);
            }
        } else {
            Entity entity2;
            HitResult hitResult = Utils.raycastForEntity(level, (Entity)entity, this.getDistance(spellLevel, entity), true);
            if (entity.isPassenger()) {
                entity.stopRiding();
            }
            if (hitResult.getType() == HitResult.Type.ENTITY && (entity2 = ((EntityHitResult)hitResult).getEntity()) instanceof LivingEntity) {
                LivingEntity target = (LivingEntity)entity2;
                for (int i = 0; i < 8 && !level.getBlockState(BlockPos.containing((Position)(dest = target.position().subtract(new Vec3(0.0, 0.0, 1.5).yRot(-(target.getYRot() + (float)(i * 45)) * ((float)Math.PI / 180))))).above()).isAir(); ++i) {
                }
                Utils.handleSpellTeleport(this, (Entity)entity, dest.add(0.0, 1.0, 0.0));
                entity.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition().subtract(0.0, 0.15, 0.0));
            } else {
                dest = TeleportSpell.findTeleportLocation(level, entity, this.getDistance(spellLevel, entity));
                Utils.handleSpellTeleport(this, (Entity)entity, dest);
            }
        }
        entity.resetFallDistance();
        level.playSound(null, dest.x, dest.y, dest.z, this.getCastFinishSound().get(), SoundSource.NEUTRAL, 1.0f, 1.0f);
        entity.setInvisible(true);
        entity.addEffect(new MobEffectInstance(MobEffectRegistry.TRUE_INVISIBILITY, 100, 0, false, false, true));
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDistance(int spellLevel, LivingEntity sourceEntity) {
        return (float)(Utils.softCapFormula(this.getEntityPowerMultiplier(sourceEntity)) * (double)this.getSpellPower(spellLevel, null));
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return AnimationHolder.none();
    }
}

