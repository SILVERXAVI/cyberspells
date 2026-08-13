/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageType
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.scores.PlayerTeam
 *  net.minecraft.world.scores.Team
 *  net.neoforged.bus.api.Event
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.common.NeoForge
 *  net.neoforged.neoforge.event.entity.living.LivingDamageEvent$Post
 *  net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
 *  net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.damage;

import io.redspace.ironsspellbooks.api.entity.NoKnockbackProjectile;
import io.redspace.ironsspellbooks.api.events.SpellDamageEvent;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber
public class DamageSources {
    private static final HashMap<UUID, Integer> knockbackImmunes = new HashMap();

    public static DamageSource get(Level level, ResourceKey<DamageType> damageType) {
        return level.damageSources().source(damageType);
    }

    public static Holder<DamageType> getHolderFromResource(Entity entity, ResourceKey<DamageType> damageTypeResourceKey) {
        Optional option = entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolder(damageTypeResourceKey);
        if (option.isPresent()) {
            return (Holder)option.get();
        }
        return entity.level().damageSources().genericKill().typeHolder();
    }

    public static boolean applyDamage(Entity target, float baseAmount, DamageSource damageSource) {
        if (target instanceof LivingEntity) {
            LivingEntity livingTarget = (LivingEntity)target;
            if (damageSource instanceof SpellDamageSource) {
                Entity entity;
                SpellDamageSource spellDamageSource = (SpellDamageSource)damageSource;
                SpellDamageEvent e = new SpellDamageEvent(livingTarget, baseAmount, spellDamageSource);
                if (((SpellDamageEvent)NeoForge.EVENT_BUS.post((Event)e)).isCanceled()) {
                    return false;
                }
                baseAmount = e.getAmount();
                float adjustedDamage = baseAmount * DamageSources.getResist(livingTarget, spellDamageSource.spell.getSchoolType());
                if (damageSource.getDirectEntity() instanceof NoKnockbackProjectile) {
                    DamageSources.ignoreNextKnockback(livingTarget);
                }
                if ((entity = damageSource.getEntity()) instanceof LivingEntity) {
                    LivingEntity livingAttacker = (LivingEntity)entity;
                    if (DamageSources.isFriendlyFireBetween((Entity)livingAttacker, (Entity)livingTarget)) {
                        return false;
                    }
                    livingAttacker.setLastHurtMob(target);
                }
                return livingTarget.hurt(damageSource, adjustedDamage);
            }
        }
        return target.hurt(damageSource, baseAmount);
    }

    public static void ignoreNextKnockback(LivingEntity livingEntity) {
        if (livingEntity.getServer() != null) {
            int tickCount = livingEntity.getServer().getTickCount();
            knockbackImmunes.entrySet().removeIf(entry -> tickCount - (Integer)entry.getValue() >= 10);
            knockbackImmunes.put(livingEntity.getUUID(), tickCount);
        }
    }

    @SubscribeEvent
    public static void cancelKnockback(LivingKnockBackEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getServer() != null && knockbackImmunes.containsKey(event.getEntity().getUUID())) {
            if (entity.getServer().getTickCount() - knockbackImmunes.get(entity.getUUID()) <= 1) {
                event.setCanceled(true);
            }
            knockbackImmunes.remove(entity.getUUID());
        }
    }

    @SubscribeEvent
    public static void postHitEffects(LivingDamageEvent.Post event) {
        SpellDamageSource spellDamageSource;
        DamageSource damageSource = event.getSource();
        if (damageSource instanceof SpellDamageSource && (spellDamageSource = (SpellDamageSource)damageSource).hasPostHitEffects()) {
            float actualDamage = event.getNewDamage();
            LivingEntity target = event.getEntity();
            Entity attacker = event.getSource().getEntity();
            if (attacker instanceof LivingEntity) {
                LivingEntity livingAttacker = (LivingEntity)attacker;
                if (spellDamageSource.getLifestealPercent() > 0.0f) {
                    livingAttacker.heal(spellDamageSource.getLifestealPercent() * actualDamage);
                }
            }
            if (spellDamageSource.getFreezeTicks() > 0 && target.canFreeze()) {
                target.setTicksFrozen(target.getTicksFrozen() + spellDamageSource.getFreezeTicks() * 2);
            }
            if (spellDamageSource.getFireTime() > 0 && target instanceof LivingEntity) {
                target.igniteForTicks(spellDamageSource.getFireTime());
            }
        }
    }

    @SubscribeEvent
    public static void preHitEffects(LivingIncomingDamageEvent event) {
        IMagicSummon summon;
        IMagicSummon fromSummon;
        IMagicSummon summon2;
        Entity entity;
        SpellDamageSource spellDamageSource;
        DamageSource damageSource = event.getSource();
        if (damageSource instanceof SpellDamageSource && (spellDamageSource = (SpellDamageSource)damageSource).getIFrames() >= 0) {
            event.getContainer().setPostAttackInvulnerabilityTicks(spellDamageSource.getIFrames());
        }
        IMagicSummon iMagicSummon = (entity = damageSource.getDirectEntity()) instanceof IMagicSummon ? (summon2 = (IMagicSummon)entity) : (fromSummon = (entity = damageSource.getEntity()) instanceof IMagicSummon ? (summon = (IMagicSummon)entity) : null);
        if (fromSummon != null) {
            Entity summoner = fromSummon.getSummoner();
            if (summoner != null && summoner.getUUID().equals(event.getEntity().getUUID())) {
                event.setCanceled(true);
                return;
            }
            if (summoner instanceof LivingEntity) {
                LivingEntity livingSummoner = (LivingEntity)summoner;
                event.setAmount(event.getAmount() * (float)livingSummoner.getAttributeValue(AttributeRegistry.SUMMON_DAMAGE));
            }
        }
    }

    public static boolean isFriendlyFireBetween(@Nullable Entity attacker, @Nullable Entity target) {
        PlayerTeam team;
        IMagicSummon summon;
        Entity tmp;
        if (attacker == null || target == null) {
            return false;
        }
        if (attacker instanceof IMagicSummon && (tmp = (summon = (IMagicSummon)attacker).getSummoner()) != null) {
            attacker = tmp;
        }
        if (target instanceof IMagicSummon && (tmp = (summon = (IMagicSummon)target).getSummoner()) != null) {
            target = tmp;
        }
        if (attacker.isPassengerOfSameVehicle(target)) {
            return true;
        }
        if (attacker instanceof Player) {
            Player playertarget;
            Player playerAttacker = (Player)attacker;
            if (target instanceof Player && !playerAttacker.canHarmPlayer(playertarget = (Player)target)) {
                return true;
            }
        }
        if ((team = attacker.getTeam()) != null) {
            return team.isAlliedTo((Team)target.getTeam()) && !team.isAllowFriendlyFire();
        }
        return attacker.isAlliedTo(target);
    }

    public static float getResist(LivingEntity entity, SchoolType damageSchool) {
        double baseResist = entity.getAttributeValue(AttributeRegistry.SPELL_RESIST);
        if (damageSchool == null) {
            return 2.0f - (float)Utils.softCapFormula(baseResist);
        }
        return 2.0f - (float)Utils.softCapFormula(damageSchool.getResistanceFor(entity) * baseResist);
    }
}

