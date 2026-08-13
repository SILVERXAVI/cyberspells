/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.bus.api.Event
 *  net.neoforged.neoforge.common.NeoForge
 */
package io.redspace.ironsspellbooks.spells.ender;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.events.CounterSpellEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;

public class CounterspellSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"counterspell");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.RARE).setSchoolResource(SchoolRegistry.ENDER_RESOURCE).setMaxLevel(1).setCooldownSeconds(10.0).build();

    public CounterspellSpell() {
        this.manaCostPerLevel = 1;
        this.baseSpellPower = 1;
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
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        EntityHitResult entityHitResult;
        Entity hitEntity;
        Vec3 start = entity.getEyePosition();
        Vec3 end = start.add(entity.getForward().normalize().scale(80.0));
        HitResult hitResult = Utils.raycastForEntity(entity.level, (Entity)entity, start, end, true, 0.35f, Utils::validAntiMagicTarget);
        Vec3 forward = entity.getForward().normalize();
        if (hitResult instanceof EntityHitResult && !((CounterSpellEvent)NeoForge.EVENT_BUS.post((Event)new CounterSpellEvent((Entity)entity, hitEntity = (entityHitResult = (EntityHitResult)hitResult).getEntity()))).isCanceled()) {
            if (hitEntity instanceof AntiMagicSusceptible) {
                AntiMagicSusceptible antiMagicSusceptible = (AntiMagicSusceptible)hitEntity;
                if (antiMagicSusceptible instanceof IMagicSummon) {
                    IMagicSummon summon = (IMagicSummon)antiMagicSusceptible;
                    if (summon.getSummoner() == entity) {
                        Mob mob;
                        if (summon instanceof Mob && (mob = (Mob)summon).getTarget() == null) {
                            antiMagicSusceptible.onAntiMagic(playerMagicData);
                        }
                    } else {
                        antiMagicSusceptible.onAntiMagic(playerMagicData);
                    }
                } else {
                    antiMagicSusceptible.onAntiMagic(playerMagicData);
                }
            } else if (hitEntity instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)hitEntity;
                Utils.serverSideCancelCast(serverPlayer, true);
                MagicData.getPlayerMagicData((LivingEntity)serverPlayer).getPlayerRecasts().removeAll(RecastResult.COUNTERSPELL);
            } else if (hitEntity instanceof IMagicEntity) {
                IMagicEntity abstractSpellCastingMob = (IMagicEntity)hitEntity;
                abstractSpellCastingMob.cancelCast();
            }
            if (hitEntity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)hitEntity;
                for (Holder mobEffect : livingEntity.getActiveEffectsMap().keySet().stream().toList()) {
                    if (!(mobEffect.value() instanceof MagicMobEffect)) continue;
                    livingEntity.removeEffect(mobEffect);
                }
            }
        }
        double distance = entity.position().distanceTo(hitResult.getLocation());
        float i = 1.0f;
        while ((double)i < distance) {
            Vec3 pos = entity.getEyePosition().add(forward.scale((double)i));
            MagicManager.spawnParticles(world, (ParticleOptions)ParticleTypes.ENCHANT, pos.x, pos.y, pos.z, 1, 0.0, 0.0, 0.0, 0.0, false);
            i += 0.5f;
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }
}

