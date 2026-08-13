/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.spells.fire;

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
import io.redspace.ironsspellbooks.capabilities.magic.ImpulseCastData;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.player.SpinAttackType;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BurningDashSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"burning_dash");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.FIRE_RESOURCE).setMaxLevel(10).setCooldownSeconds(10.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.damage", (Object[])new Object[]{this.getDamage(spellLevel, caster)}));
    }

    public BurningDashSpell() {
        this.manaCostPerLevel = 2;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 20;
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
    public void onClientCast(Level level, int spellLevel, LivingEntity entity, ICastData castData) {
        if (castData instanceof ImpulseCastData) {
            ImpulseCastData bdcd = (ImpulseCastData)castData;
            entity.hasImpulse = bdcd.hasImpulse;
            entity.setDeltaMovement(entity.getDeltaMovement().add((double)bdcd.x, (double)bdcd.y, (double)bdcd.z));
        }
        super.onClientCast(level, spellLevel, entity, castData);
    }

    @Override
    public ICastDataSerializable getEmptyCastData() {
        return new ImpulseCastData();
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        entity.hasImpulse = true;
        float multiplier = (15.0f + this.getSpellPower(spellLevel, (Entity)entity)) / 12.0f;
        Vec3 forward = entity.getLookAngle();
        if (playerMagicData.getAdditionalCastData() instanceof BurningDashDirectionOverrideCastData) {
            forward = Utils.random.nextBoolean() ? forward.yRot(90.0f) : forward.yRot(-90.0f);
        }
        Vec3 vec = forward.multiply(3.0, 1.0, 3.0).normalize().add(0.0, 0.25, 0.0).scale((double)multiplier);
        if (entity.onGround()) {
            entity.setPos(entity.position().add(0.0, 1.5, 0.0));
            vec.add(0.0, 0.25, 0.0);
        }
        playerMagicData.setAdditionalCastData(new ImpulseCastData((float)vec.x, (float)vec.y, (float)vec.z, true));
        entity.setDeltaMovement(new Vec3(Mth.lerp((double)0.75, (double)entity.getDeltaMovement().x, (double)vec.x), Mth.lerp((double)0.75, (double)entity.getDeltaMovement().y, (double)vec.y), Mth.lerp((double)0.75, (double)entity.getDeltaMovement().z, (double)vec.z)));
        entity.addEffect(new MobEffectInstance(MobEffectRegistry.BURNING_DASH, 15, this.getDamage(spellLevel, entity), false, false, false));
        entity.invulnerableTime = 20;
        playerMagicData.getSyncedData().setSpinAttackType(SpinAttackType.FIRE);
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public SpellDamageSource getDamageSource(@Nullable Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker).setFireTicks(80);
    }

    private int getDamage(int spellLevel, LivingEntity caster) {
        return (int)(5.0f + this.getSpellPower(spellLevel, (Entity)caster));
    }

    public static void ambientParticles(ClientLevel level, LivingEntity entity) {
        Vec3 random;
        int i;
        for (i = 0; i < 2; ++i) {
            random = Utils.getRandomVec3(0.2);
            level.addParticle(ParticleHelper.FIRE, entity.getRandomX(0.75), entity.getY() + Utils.getRandomScaled(0.75), entity.getRandomZ(0.75), random.x, random.y, random.z);
        }
        for (i = 0; i < 6; ++i) {
            random = Utils.getRandomVec3(0.2);
            level.addParticle(ParticleHelper.EMBERS, entity.getRandomX(0.75), entity.getY() + Utils.getRandomScaled(0.75), entity.getRandomZ(0.75), random.x, random.y, random.z);
        }
    }

    public static class BurningDashDirectionOverrideCastData
    implements ICastData {
        @Override
        public void reset() {
        }
    }
}

