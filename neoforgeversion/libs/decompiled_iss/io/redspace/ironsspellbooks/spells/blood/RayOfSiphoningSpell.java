/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.network.PacketDistributor
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.spells.blood;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.network.particles.BloodSiphonParticlesPacket;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.spells.CastingMobAimingData;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class RayOfSiphoningSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"ray_of_siphoning");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.BLOOD_RESOURCE).setMaxLevel(10).setCooldownSeconds(15.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.damage", (Object[])new Object[]{Utils.stringTruncation(this.getTickDamage(spellLevel, caster), 2)}), Component.translatable((String)"ui.irons_spellbooks.distance", (Object[])new Object[]{Utils.stringTruncation(RayOfSiphoningSpell.getRange(spellLevel), 1)}));
    }

    public RayOfSiphoningSpell() {
        this.manaCostPerLevel = 1;
        this.baseSpellPower = 4;
        this.spellPowerPerLevel = 1;
        this.castTime = 100;
        this.baseManaCost = 8;
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
        return Optional.empty();
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of((SoundEvent)SoundRegistry.RAY_OF_SIPHONING.get());
    }

    @Override
    public ICastDataSerializable getEmptyCastData() {
        return new CastingMobAimingData();
    }

    @Override
    public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        super.onServerCastTick(level, spellLevel, entity, playerMagicData);
        ICastData iCastData = playerMagicData.getAdditionalCastData();
        if (iCastData instanceof CastingMobAimingData) {
            Mob mob;
            LivingEntity target;
            CastingMobAimingData aimData = (CastingMobAimingData)iCastData;
            if (entity instanceof Mob && (target = (mob = (Mob)entity).getTarget()) != null) {
                aimData.updateAim((Entity)target, 0.15f);
            }
        }
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        Entity target;
        HitResult hitResult;
        Vec3 forward = entity.getForward();
        ICastData iCastData = playerMagicData.getAdditionalCastData();
        if (iCastData instanceof CastingMobAimingData) {
            CastingMobAimingData aimData = (CastingMobAimingData)iCastData;
            if (entity instanceof Mob) {
                Mob mob = (Mob)entity;
                forward = aimData.getForward((Entity)entity);
            }
        }
        if ((hitResult = Utils.raycastForEntity(level, (Entity)entity, entity.getEyePosition(), entity.getEyePosition().add(forward.scale((double)RayOfSiphoningSpell.getRange(spellLevel))), true, 0.15f, Utils::canHitWithRaycast)).getType() == HitResult.Type.ENTITY && (target = ((EntityHitResult)hitResult).getEntity()).canBeHitByProjectile() && DamageSources.applyDamage(target, this.getTickDamage(spellLevel, entity), this.getDamageSource((Entity)entity))) {
            PacketDistributor.sendToPlayersTrackingEntityAndSelf((Entity)entity, (CustomPacketPayload)new BloodSiphonParticlesPacket(target.position().add(0.0, (double)(target.getBbHeight() / 2.0f), 0.0), entity.position().add(0.0, (double)(entity.getBbHeight() / 2.0f), 0.0)), (CustomPacketPayload[])new CustomPacketPayload[0]);
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public SpellDamageSource getDamageSource(@Nullable Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker).setLifestealPercent(1.0f);
    }

    public static float getRange(int level) {
        return 12.0f;
    }

    private float getTickDamage(int spellLevel, LivingEntity caster) {
        return this.getSpellPower(spellLevel, (Entity)caster) * 0.25f;
    }

    @Override
    public boolean shouldAIStopCasting(int spellLevel, Mob mob, LivingEntity target) {
        return mob.distanceToSqr((Entity)target) > (double)(RayOfSiphoningSpell.getRange(spellLevel) * RayOfSiphoningSpell.getRange(spellLevel)) * 1.2;
    }
}

