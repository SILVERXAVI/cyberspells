/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Position
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.spells.ice;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.entity.spells.ice_spike.IceSpikeEntity;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class IceSpikesSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"ice_spikes");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.ICE_RESOURCE).setMaxLevel(10).setCooldownSeconds(15.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.damage", (Object[])new Object[]{Utils.stringTruncation(this.getDamage(spellLevel, caster), 2)}), Component.translatable((String)"ui.irons_spellbooks.spike_count", (Object[])new Object[]{this.getCount(spellLevel, caster)}));
    }

    public IceSpikesSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 12;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 30;
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
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        Utils.preCastTargetHelper(level, entity, playerMagicData, this, (int)((float)this.getCount(spellLevel, entity) * 1.25f), 0.15f, false);
        return true;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        TargetEntityCastData castTargetingData;
        LivingEntity target;
        Vec3 forward = entity.getForward().multiply(1.0, 0.0, 1.0).normalize();
        Vec3 start = entity.getEyePosition().add(forward.scale(1.5));
        float damage = this.getDamage(spellLevel, entity);
        float minScale = 1.0f;
        float maxScale = 3.0f;
        int count = this.getCount(spellLevel, entity);
        start = Utils.moveToRelativeGroundLevel(level, start, 1, 3).add(0.0, 0.1, 0.0);
        double distance = count;
        ICastData iCastData = playerMagicData.getAdditionalCastData();
        if (iCastData instanceof TargetEntityCastData && (target = (castTargetingData = (TargetEntityCastData)iCastData).getTarget((ServerLevel)level)) != null) {
            distance = start.subtract(target.position()).horizontalDistance();
            Vec3 targetPos = target.position().add(target.getDeltaMovement().multiply(distance, 0.0, distance));
            distance = targetPos.subtract(start).horizontalDistance();
        }
        float distanceCovered = 0.0f;
        for (int i = 0; i < count; ++i) {
            boolean isFinalSpike;
            float f = (float)Math.max((double)((float)i / (float)count), (double)(distanceCovered + 1.0f) / distance);
            float scale = Mth.lerp((float)f, (float)minScale, (float)maxScale);
            Vec3 spawn = start.add(forward.scale((double)i));
            Vec3 ground = Utils.moveToRelativeGroundLevel(level, spawn, 8);
            spawn = ground.subtract(spawn).scale((double)Mth.clamp((float)((float)i / 3.0f), (float)0.0f, (float)1.0f)).add(spawn);
            boolean bl = isFinalSpike = i == count - 1 || (double)(distanceCovered + 1.0f) > distance;
            if (isFinalSpike) {
                scale = maxScale * 1.5f;
            }
            distanceCovered += (float)forward.horizontalDistance();
            int delay = i;
            if (level.getBlockState(BlockPos.containing((Position)spawn).below()).isFaceSturdy((BlockGetter)level, BlockPos.containing((Position)spawn).below(), Direction.UP)) {
                IceSpikeEntity spike = new IceSpikeEntity(level, entity);
                if (i % 2 == count % 2) {
                    spike.setSilent(true);
                }
                spike.setSpikeSize(scale);
                spike.moveTo(spawn.add(0.0, 0.0, 0.0));
                spike.setWaitTime(delay);
                spike.setDamage(damage * (isFinalSpike ? 1.0f : 0.5f));
                spike.setYRot(entity.getYRot() - 45.0f + (float)Utils.random.nextIntBetweenInclusive(-20, 20));
                spike.setXRot(Utils.random.nextIntBetweenInclusive(-15, 15));
                level.addFreshEntity((Entity)spike);
            }
            if (isFinalSpike) break;
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public boolean shouldAIStopCasting(int spellLevel, Mob mob, LivingEntity target) {
        float f = (float)this.getCount(spellLevel, (LivingEntity)mob) * 1.5f;
        return mob.distanceToSqr((Entity)target) > (double)(f * f);
    }

    private int getCount(int spellLevel, LivingEntity entity) {
        return 7 + 3 * spellLevel / 2;
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, (Entity)entity);
    }
}

