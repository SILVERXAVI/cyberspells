/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Position
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LightningBolt
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.monster.Creeper
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.spells.lightning;

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
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AscensionSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"ascension");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.RARE).setSchoolResource(SchoolRegistry.LIGHTNING_RESOURCE).setMaxLevel(10).setCooldownSeconds(15.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.damage", (Object[])new Object[]{Utils.stringTruncation(this.getSpellPower(spellLevel, (Entity)caster), 1)}));
    }

    public AscensionSpell() {
        this.manaCostPerLevel = 1;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 50;
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
    public ICastDataSerializable getEmptyCastData() {
        return new ImpulseCastData();
    }

    @Override
    public void onClientCast(Level level, int spellLevel, LivingEntity entity, ICastData castData) {
        if (castData instanceof ImpulseCastData) {
            ImpulseCastData data = (ImpulseCastData)castData;
            entity.hasImpulse = data.hasImpulse;
            double y = Math.max(entity.getDeltaMovement().y, (double)data.y);
            entity.setDeltaMovement((double)data.x, y, (double)data.z);
        }
        super.onClientCast(level, spellLevel, entity, castData);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        entity.addEffect(new MobEffectInstance(MobEffectRegistry.ASCENSION, 80, 0, false, false, true));
        Vec3 vec = entity.position();
        for (int i = 0; i < 32 && level.getBlockState(BlockPos.containing((Position)vec).below()).isAir(); ++i) {
            vec = vec.subtract(0.0, 1.0, 0.0);
        }
        Vec3 strikePos = vec;
        LightningBolt lightningBolt = (LightningBolt)EntityType.LIGHTNING_BOLT.create(level);
        lightningBolt.setVisualOnly(true);
        lightningBolt.setDamage(0.0f);
        lightningBolt.setPos(strikePos);
        level.addFreshEntity((Entity)lightningBolt);
        float radius = 5.0f;
        level.getEntities((Entity)entity, entity.getBoundingBox().inflate((double)radius)).forEach(target -> {
            double distance = target.distanceToSqr(strikePos);
            if (distance < (double)(radius * radius)) {
                float finalDamage = (float)((double)this.getDamage(spellLevel, entity) * (1.0 - distance / (double)(radius * radius)));
                DamageSources.applyDamage(target, finalDamage, this.getDamageSource((Entity)lightningBolt, (Entity)entity));
                if (target instanceof Creeper) {
                    Creeper creeper = (Creeper)target;
                    creeper.thunderHit((ServerLevel)level, lightningBolt);
                }
                if (target instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity)target;
                    livingEntity.knockback((double)(0.25f + finalDamage / 10.0f), entity.getX() - livingEntity.getX(), entity.getZ() - livingEntity.getZ());
                }
            }
        });
        Vec3 motion = entity.getLookAngle().multiply(1.0, 0.0, 1.0).normalize().add(0.0, 5.0, 0.0).scale(0.125);
        playerMagicData.setAdditionalCastData(new ImpulseCastData((float)motion.x, (float)motion.y, (float)motion.z, true));
        entity.setDeltaMovement(entity.getDeltaMovement().add(motion));
        entity.hasImpulse = true;
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private int getDamage(int spellLevel, LivingEntity caster) {
        return (int)this.getSpellPower(spellLevel, (Entity)caster);
    }
}

