/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.AttributeInstance
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.EntityMountEvent
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Expired
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Remove
 */
package com.perigrine3.createcybernetics.effect;

import java.util.Map;
import java.util.UUID;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

public class SpursEffect
extends MobEffect {
    public static final ResourceLocation SPURS_EFFECT_ID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"spurs_effect");
    public static final ResourceLocation MOUNT_SPEED_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"spurs_mount_speed");
    private static final String NBT_LAST_MOUNT_UUID = "cc_spurs_lastMountUuid";
    private static final double SPEED_MULT_PER_LEVEL = 0.75;

    public SpursEffect() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class Events {
        @SubscribeEvent
        public static void onMount(EntityMountEvent event) {
            Entity entity = event.getEntityMounting();
            if (!(entity instanceof Player)) {
                return;
            }
            Player player = (Player)entity;
            if (player.level().isClientSide) {
                return;
            }
            Entity beingMounted = event.getEntityBeingMounted();
            if (event.isMounting()) {
                if (!(beingMounted instanceof LivingEntity)) {
                    return;
                }
                LivingEntity mount = (LivingEntity)beingMounted;
                MobEffectInstance spurs = Events.getEffectById((LivingEntity)player, SPURS_EFFECT_ID);
                if (spurs == null) {
                    return;
                }
                Events.applyToMount(mount, spurs.getAmplifier());
                player.getPersistentData().putUUID(SpursEffect.NBT_LAST_MOUNT_UUID, mount.getUUID());
                return;
            }
            if (event.isDismounting()) {
                if (beingMounted instanceof LivingEntity) {
                    LivingEntity mount = (LivingEntity)beingMounted;
                    Events.removeFromMount(mount);
                } else {
                    Events.cleanupLastMount(player);
                }
                player.getPersistentData().remove(SpursEffect.NBT_LAST_MOUNT_UUID);
            }
        }

        @SubscribeEvent
        public static void onEffectRemoved(MobEffectEvent.Remove event) {
            Events.cleanupIfSpurs(event.getEffectInstance(), event.getEntity());
        }

        @SubscribeEvent
        public static void onEffectExpired(MobEffectEvent.Expired event) {
            Events.cleanupIfSpurs(event.getEffectInstance(), event.getEntity());
        }

        private static void cleanupIfSpurs(MobEffectInstance inst, LivingEntity entity) {
            if (!(entity instanceof Player)) {
                return;
            }
            Player player = (Player)entity;
            if (player.level().isClientSide) {
                return;
            }
            if (inst == null) {
                return;
            }
            ResourceLocation key = BuiltInRegistries.MOB_EFFECT.getKey((Object)((MobEffect)inst.getEffect().value()));
            if (!SPURS_EFFECT_ID.equals((Object)key)) {
                return;
            }
            Entity vehicle = player.getVehicle();
            if (vehicle instanceof LivingEntity) {
                LivingEntity mount = (LivingEntity)vehicle;
                Events.removeFromMount(mount);
            }
            Events.cleanupLastMount(player);
            player.getPersistentData().remove(SpursEffect.NBT_LAST_MOUNT_UUID);
        }

        private static void cleanupLastMount(Player player) {
            ServerLevel serverLevel;
            Entity e;
            CompoundTag tag = player.getPersistentData();
            if (!tag.hasUUID(SpursEffect.NBT_LAST_MOUNT_UUID)) {
                return;
            }
            UUID uuid = tag.getUUID(SpursEffect.NBT_LAST_MOUNT_UUID);
            Level level = player.level();
            if (level instanceof ServerLevel && (e = (serverLevel = (ServerLevel)level).getEntity(uuid)) instanceof LivingEntity) {
                LivingEntity mount = (LivingEntity)e;
                Events.removeFromMount(mount);
            }
        }

        private static MobEffectInstance getEffectById(LivingEntity entity, ResourceLocation effectId) {
            for (Map.Entry entry : entity.getActiveEffectsMap().entrySet()) {
                ResourceLocation key;
                MobEffectInstance inst = (MobEffectInstance)entry.getValue();
                if (inst == null || !effectId.equals((Object)(key = BuiltInRegistries.MOB_EFFECT.getKey((Object)((MobEffect)inst.getEffect().value()))))) continue;
                return inst;
            }
            return null;
        }

        private static void applyToMount(LivingEntity mount, int amplifier) {
            AttributeInstance speed = mount.getAttribute(Attributes.MOVEMENT_SPEED);
            if (speed == null) {
                return;
            }
            speed.removeModifier(MOUNT_SPEED_MODIFIER_ID);
            double amount = 0.75 * (double)(amplifier + 1);
            AttributeModifier modifier = new AttributeModifier(MOUNT_SPEED_MODIFIER_ID, amount, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
            speed.addTransientModifier(modifier);
        }

        private static void removeFromMount(LivingEntity mount) {
            AttributeInstance speed = mount.getAttribute(Attributes.MOVEMENT_SPEED);
            if (speed == null) {
                return;
            }
            speed.removeModifier(MOUNT_SPEED_MODIFIER_ID);
        }
    }
}

