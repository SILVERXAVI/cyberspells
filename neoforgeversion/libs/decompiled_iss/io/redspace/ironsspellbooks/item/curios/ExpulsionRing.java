/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.level.Level$ExplosionInteraction
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
 */
package io.redspace.ironsspellbooks.item.curios;

import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.item.curios.PassiveAbilityCurio;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class ExpulsionRing
extends PassiveAbilityCurio {
    public static final int COOLDOWN_IN_TICKS = 40;
    public static final int RADIUS = 4;
    public static final int RADIUS_SQR = 16;

    public ExpulsionRing() {
        super(new Item.Properties().stacksTo(1), Curios.RING_SLOT);
    }

    @Override
    protected int getCooldownTicks() {
        return 40;
    }

    @SubscribeEvent
    public static void handleAbility(LivingIncomingDamageEvent event) {
        ExpulsionRing RING = (ExpulsionRing)ItemRegistry.EXPULSION_RING.get();
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)livingEntity;
            if (event.getSource().getEntity() != null && RING.isEquippedBy((LivingEntity)serverPlayer) && RING.tryProcCooldown((Player)serverPlayer)) {
                Vec3 vec = serverPlayer.getBoundingBox().getCenter();
                serverPlayer.level.explode(null, null, null, vec.x, vec.y, vec.z, 0.0f, false, Level.ExplosionInteraction.NONE, (ParticleOptions)ParticleTypes.GUST_EMITTER_SMALL, (ParticleOptions)ParticleTypes.GUST_EMITTER_LARGE, (Holder)SoundEvents.WIND_CHARGE_BURST);
                serverPlayer.level.getEntities((Entity)serverPlayer, serverPlayer.getBoundingBox().inflate(3.0)).forEach(entity -> {
                    double d = Math.max(entity.distanceToSqr((Entity)serverPlayer), 0.2);
                    if (d < 16.0 && !DamageSources.isFriendlyFireBetween((Entity)serverPlayer, entity)) {
                        double d2;
                        double d3 = 1.0 - d / 16.0;
                        if (entity instanceof LivingEntity) {
                            LivingEntity living = (LivingEntity)entity;
                            d2 = 1.0 - living.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
                        } else {
                            d2 = 1.0;
                        }
                        double f = d3 + (double)0.6f * d2;
                        Vec3 impulse = entity.getBoundingBox().getCenter().subtract(serverPlayer.getBoundingBox().getCenter()).normalize().add(0.0, 0.1, 0.0).scale(f);
                        entity.setDeltaMovement(entity.getDeltaMovement().add(impulse));
                        entity.hurtMarked = true;
                    }
                });
            }
        }
    }
}

