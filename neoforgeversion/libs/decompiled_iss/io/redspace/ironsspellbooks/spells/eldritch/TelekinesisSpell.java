/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 *  org.joml.Vector3f
 */
package io.redspace.ironsspellbooks.spells.eldritch;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.capabilities.magic.TelekinesisData;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class TelekinesisSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"telekinesis");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.LEGENDARY).setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE).setMaxLevel(5).setCooldownSeconds(35.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.distance", (Object[])new Object[]{Utils.stringTruncation(this.getRange(spellLevel, caster), 1)}));
    }

    public TelekinesisSpell() {
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 8;
        this.spellPowerPerLevel = 4;
        this.castTime = 140;
        this.baseManaCost = 25;
    }

    @Override
    public int getCastTime(int spellLevel) {
        return this.castTime + 20 * (spellLevel - 1);
    }

    @Override
    public CastType getCastType() {
        return CastType.CONTINUOUS;
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
        return Optional.of((SoundEvent)SoundRegistry.TELEKINESIS_CAST.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of((SoundEvent)SoundRegistry.TELEKINESIS_LOOP.get());
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        if (Utils.preCastTargetHelper(level, entity, playerMagicData, this, this.getRange(spellLevel, entity), 0.15f)) {
            LivingEntity target = ((TargetEntityCastData)playerMagicData.getAdditionalCastData()).getTarget((ServerLevel)level);
            if (target == null) {
                return false;
            }
            playerMagicData.setAdditionalCastData(new TelekinesisData(entity.distanceTo((Entity)target), target, 6));
            return true;
        }
        return false;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        super.onServerCastTick(level, spellLevel, entity, playerMagicData);
        if (playerMagicData != null && playerMagicData.getCastDurationRemaining() % 2 == 0) {
            this.handleTelekinesis((ServerLevel)level, entity, playerMagicData, 0.6f);
        }
    }

    private void handleTelekinesis(ServerLevel world, LivingEntity entity, MagicData playerMagicData, float strength) {
        TelekinesisData targetData;
        LivingEntity targetEntity;
        ICastData iCastData = playerMagicData.getAdditionalCastData();
        if (iCastData instanceof TelekinesisData && (targetEntity = (targetData = (TelekinesisData)iCastData).getTarget(world)) != null) {
            if ((targetEntity.isRemoved() || targetEntity.isDeadOrDying()) && entity instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)entity;
                Utils.serverSideCancelCast(serverPlayer);
                return;
            }
            float resistance = Mth.clamp((float)(1.0f - (float)targetEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)), (float)0.2f, (float)1.0f);
            float lockedDistance = targetData.getDistance();
            float actualDistance = entity.distanceTo((Entity)targetEntity);
            float distance = Mth.lerp((float)(actualDistance > lockedDistance ? 0.25f : 0.1f), (float)lockedDistance, (float)actualDistance);
            targetData.setDistance(distance);
            Vec3 force = entity.getForward().normalize().scale((double)targetData.getDistance()).add(entity.position()).subtract(targetEntity.position()).scale((double)(resistance * strength));
            Vec3 travel = new Vec3(targetEntity.getX() - targetEntity.xOld, targetEntity.getY() - targetEntity.yOld, targetEntity.getZ() - targetEntity.zOld);
            if (force.y > 0.0) {
                targetEntity.resetFallDistance();
            }
            if (playerMagicData.getCastDurationRemaining() % 10 == 0) {
                int airborne = (int)(travel.x * travel.x + travel.z * travel.z) / 2;
                targetEntity.addEffect(new MobEffectInstance(MobEffectRegistry.AIRBORNE, 31, airborne));
                targetEntity.addEffect(new MobEffectInstance(MobEffectRegistry.ANTIGRAVITY, 11, 0));
            }
            Vec3 deltaMovement = targetEntity.getDeltaMovement();
            Vec3 newMotion = force.subtract(deltaMovement).scale(0.25).add(deltaMovement);
            Vec3 delta = newMotion.subtract(deltaMovement);
            Vec3 clampedMotion = new Vec3(Utils.signedMin(delta.x, force.x * 4.0), Utils.signedMin(delta.y, force.y * 4.0), Utils.signedMin(delta.z, force.z * 4.0));
            targetEntity.setDeltaMovement(deltaMovement.add(clampedMotion));
            targetEntity.hurtMarked = true;
        }
    }

    @Override
    public Vector3f getTargetingColor() {
        return new Vector3f(1.0f, 0.24f, 0.95f);
    }

    private int getRange(int spellLevel, LivingEntity caster) {
        return 12 + (spellLevel - 1) * 2;
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.SELF_CAST_ANIMATION;
    }
}

