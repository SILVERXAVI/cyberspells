/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.NbtUtils
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.common.util.INBTSerializable
 *  net.neoforged.neoforge.event.entity.EntityJoinLevelEvent
 *  net.neoforged.neoforge.event.tick.EntityTickEvent$Pre
 *  net.neoforged.neoforge.event.tick.LevelTickEvent$Post
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package io.redspace.ironsspellbooks.effect.guiding_bolt;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.data.IronsDataStorage;
import io.redspace.ironsspellbooks.network.spells.GuidingBoltManagerStartTrackingPacket;
import io.redspace.ironsspellbooks.network.spells.GuidingBoltManagerStopTrackingPacket;
import io.redspace.ironsspellbooks.util.ModTags;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber
public class GuidingBoltManager
implements INBTSerializable<CompoundTag> {
    public static final GuidingBoltManager INSTANCE = new GuidingBoltManager();
    public static final GuidingBoltManager CLIENT_INSTANCE = new GuidingBoltManager();
    private final HashMap<UUID, HashSet<Projectile>> trackedEntities = new HashMap();
    private final HashMap<ResourceKey<Level>, List<Projectile>> dirtyProjectiles = new HashMap();
    private final int tickDelay = 1;

    public void startTracking(LivingEntity entity) {
        if (!entity.level.isClientSide && !this.trackedEntities.containsKey(entity.getUUID())) {
            this.trackedEntities.put(entity.getUUID(), new HashSet());
            IronsDataStorage.INSTANCE.setDirty();
        }
    }

    public void stopTracking(LivingEntity entity) {
        if (!entity.level.isClientSide) {
            this.trackedEntities.remove(entity.getUUID());
            IronsDataStorage.INSTANCE.setDirty();
            PacketDistributor.sendToPlayersTrackingEntity((Entity)entity, (CustomPacketPayload)new GuidingBoltManagerStopTrackingPacket((Entity)entity), (CustomPacketPayload[])new CustomPacketPayload[0]);
        }
    }

    public CompoundTag serializeNBT(HolderLookup.Provider pRegistries) {
        CompoundTag tag = new CompoundTag();
        ListTag uuids = new ListTag();
        for (UUID key : this.trackedEntities.keySet()) {
            uuids.add((Object)NbtUtils.createUUID((UUID)key));
        }
        tag.put("TrackedEntities", (Tag)uuids);
        return tag;
    }

    public void deserializeNBT(HolderLookup.Provider pRegistries, CompoundTag compoundTag) {
        ListTag list = compoundTag.getList("TrackedEntities", 11);
        for (Tag uuidTag : list) {
            try {
                UUID uuid = NbtUtils.loadUUID((Tag)uuidTag);
                this.trackedEntities.put(uuid, new HashSet());
            }
            catch (Exception ignored) {}
        }
    }

    @SubscribeEvent
    public static void onProjectileShot(EntityJoinLevelEvent event) {
        Level level = event.getLevel();
        if (level instanceof ServerLevel) {
            Projectile projectile;
            Entity entity;
            ServerLevel serverLevel = (ServerLevel)level;
            if (!GuidingBoltManager.INSTANCE.trackedEntities.isEmpty() && (entity = event.getEntity()) instanceof Projectile && !(projectile = (Projectile)entity).getType().is(ModTags.GUIDING_BOLT_IMMUNE)) {
                GuidingBoltManager.INSTANCE.dirtyProjectiles.computeIfAbsent((ResourceKey<Level>)serverLevel.dimension(), key -> new ArrayList()).add(projectile);
            }
        }
    }

    @SubscribeEvent
    public static void serverTick(LevelTickEvent.Post event) {
        if (GuidingBoltManager.INSTANCE.dirtyProjectiles.isEmpty()) {
            return;
        }
        Level level = event.getLevel();
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            HashMap<Entity, List> toSync = new HashMap<Entity, List>();
            List dirtyProjectiles = GuidingBoltManager.INSTANCE.dirtyProjectiles.getOrDefault(serverLevel.dimension(), List.of());
            for (int i = dirtyProjectiles.size() - 1; i >= 0; --i) {
                Projectile projectile = (Projectile)dirtyProjectiles.get(i);
                if (projectile.isRemoved()) {
                    dirtyProjectiles.remove(i);
                    continue;
                }
                if (!projectile.isAddedToLevel()) continue;
                Vec3 start = projectile.position();
                int searchRange = 48;
                Vec3 end = Utils.raycastForBlock((Level)serverLevel, start, projectile.getDeltaMovement().normalize().scale((double)searchRange).add(start), ClipContext.Fluid.NONE).getLocation();
                for (Map.Entry<UUID, HashSet<Projectile>> entityToTrackedProjectiles : GuidingBoltManager.INSTANCE.trackedEntities.entrySet()) {
                    Entity entity = serverLevel.getEntity(entityToTrackedProjectiles.getKey());
                    if (entity == null || Math.abs(entity.getX() - projectile.getX()) > (double)searchRange || Math.abs(entity.getY() - projectile.getY()) > (double)searchRange || Math.abs(entity.getZ() - projectile.getZ()) > (double)searchRange) continue;
                    float homeRadius = 3.5f + Math.min(entity.getBbWidth() * 0.5f, 2.0f);
                    if (!entity.getBoundingBox().inflate((double)homeRadius).contains(start) && Utils.checkEntityIntersecting(entity, start, end, homeRadius).getType() != HitResult.Type.ENTITY) continue;
                    GuidingBoltManager.updateTrackedProjectiles((Set<Projectile>)entityToTrackedProjectiles.getValue(), projectile);
                    toSync.computeIfAbsent(entity, key -> new ArrayList()).add(projectile);
                    break;
                }
                dirtyProjectiles.remove(i);
            }
            for (Map.Entry entry : toSync.entrySet()) {
                Entity entity = (Entity)entry.getKey();
                PacketDistributor.sendToPlayersTrackingEntity((Entity)entity, (CustomPacketPayload)new GuidingBoltManagerStartTrackingPacket(entity, (List)entry.getValue()), (CustomPacketPayload[])new CustomPacketPayload[0]);
            }
        }
    }

    private static void updateTrackedProjectiles(Set<Projectile> tracked, Projectile toTrack) {
        GuidingBoltManager.updateTrackedProjectiles(tracked, Set.of(toTrack));
    }

    private static void updateTrackedProjectiles(Set<Projectile> tracked, Set<Projectile> toTrack) {
        tracked.removeIf(Entity::isRemoved);
        tracked.addAll(toTrack);
    }

    @SubscribeEvent
    public static void livingTick(EntityTickEvent.Pre event) {
        GuidingBoltManager manager;
        GuidingBoltManager guidingBoltManager = manager = event.getEntity().level instanceof ServerLevel ? INSTANCE : CLIENT_INSTANCE;
        if (manager.trackedEntities.isEmpty()) {
            return;
        }
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity) {
            HashSet<Projectile> projectiles;
            LivingEntity livingEntity = (LivingEntity)entity;
            if (livingEntity.tickCount % manager.tickDelay == 0 && (projectiles = manager.trackedEntities.get(event.getEntity().getUUID())) != null) {
                if (livingEntity.isRemoved() || livingEntity.isDeadOrDying()) {
                    manager.stopTracking(livingEntity);
                    return;
                }
                ArrayList<Projectile> projectilesToRemove = new ArrayList<Projectile>();
                for (Projectile projectile : projectiles) {
                    Vec3 motion = projectile.getDeltaMovement();
                    float speed = (float)motion.length();
                    Vec3 vec3 = livingEntity.getBoundingBox().getCenter().subtract(projectile.position()).normalize();
                    Objects.requireNonNull(manager);
                    Vec3 home = vec3.scale((double)(speed * 0.55f * 1.0f / 3.0f));
                    if (projectile.isRemoved() || home.dot(motion) < -0.75) {
                        projectilesToRemove.add(projectile);
                        continue;
                    }
                    Vec3 newMotion = motion.add(home).normalize().scale((double)speed);
                    projectile.setDeltaMovement(newMotion);
                }
                projectiles.removeAll(projectilesToRemove);
            }
        }
    }

    public static void handleClientboundStartTracking(UUID uuid, List<Integer> projectileIds) {
        ClientLevel level = Minecraft.getInstance().level;
        HashSet<Projectile> projectiles = new HashSet<Projectile>();
        for (Integer i : projectileIds) {
            Entity entity = level.getEntity(i.intValue());
            if (!(entity instanceof Projectile)) continue;
            Projectile projectile = (Projectile)entity;
            GuidingBoltManager.updateTrackedProjectiles(projectiles, projectile);
        }
        GuidingBoltManager.CLIENT_INSTANCE.trackedEntities.computeIfAbsent(uuid, key -> new HashSet()).addAll(projectiles);
    }

    public static void handleClientboundStopTracking(UUID uuid) {
        GuidingBoltManager.CLIENT_INSTANCE.trackedEntities.remove(uuid);
    }

    public static void handleClientLogout() {
        GuidingBoltManager.CLIENT_INSTANCE.trackedEntities.clear();
    }
}

