/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.common.util.INBTSerializable
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerLoggedInEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerLoggedOutEvent
 *  net.neoforged.neoforge.event.server.ServerStoppingEvent
 *  net.neoforged.neoforge.event.tick.ServerTickEvent$Post
 *  org.apache.commons.lang3.stream.Streams
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.capabilities.magic;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerRecasts;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.capabilities.magic.SummonedEntitiesCastData;
import io.redspace.ironsspellbooks.data.IronsDataStorage;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.apache.commons.lang3.stream.Streams;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber
public class SummonManager
implements INBTSerializable<CompoundTag> {
    public static final SummonManager INSTANCE = new SummonManager();
    private final HashMap<UUID, List<CompoundTag>> offlineSummonersToSavedEntities = new HashMap();
    private final HashMap<UUID, UUID> summonToOwner = new HashMap();
    private final HashMap<UUID, Set<UUID>> ownerToSummons = new HashMap();
    private final PriorityQueue<ExpirationInstance> summonExpirations = new PriorityQueue<ExpirationInstance>(Comparator.comparingInt(ExpirationInstance::expirationServerTick));

    @Nullable
    public static Entity getOwner(@NotNull Entity summon) {
        Level level = summon.level;
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            if (SummonManager.INSTANCE.summonToOwner.containsKey(summon.getUUID())) {
                return serverLevel.getEntity(SummonManager.INSTANCE.summonToOwner.get(summon.getUUID()));
            }
        } else {
            IronsSpellbooks.LOGGER.warn("Summon {} attempting to lookup owner from client!", (Object)summon);
        }
        return null;
    }

    public static Set<UUID> getSummons(Entity owner) {
        return Set.copyOf(SummonManager.INSTANCE.ownerToSummons.getOrDefault(owner.getUUID(), Set.of()));
    }

    public static void setOwner(@NotNull Entity summon, @NotNull Entity owner) {
        SummonManager.removeSummon(summon);
        SummonManager.INSTANCE.summonToOwner.put(summon.getUUID(), owner.getUUID());
        SummonManager.startTrackingSummon(owner, summon);
        IronsDataStorage.INSTANCE.setDirty();
    }

    public static void initSummon(Entity owner, Entity summon, int duration, SummonedEntitiesCastData summonedEntitiesCastData) {
        SummonManager.setOwner(summon, owner);
        SummonManager.setDuration(summon, duration);
        summonedEntitiesCastData.add(summon);
    }

    public static void setDuration(Entity summon, int duration) {
        Level level = summon.level;
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel serverLevel = (ServerLevel)level;
        SummonManager.INSTANCE.summonExpirations.add(new ExpirationInstance(summon.getUUID(), serverLevel.getServer().getTickCount(), serverLevel.getServer().getTickCount() + duration));
    }

    public static void removeSummon(Entity summon) {
        Level level;
        UUID owner = SummonManager.INSTANCE.summonToOwner.remove(summon.getUUID());
        if (owner == null) {
            return;
        }
        IronsDataStorage.INSTANCE.setDirty();
        Set<UUID> summons = SummonManager.INSTANCE.ownerToSummons.get(owner);
        if (summons == null) {
            return;
        }
        UUID summonUuid = summon.getUUID();
        summons.remove(summonUuid);
        IronsDataStorage.INSTANCE.setDirty();
        if (summons.isEmpty()) {
            SummonManager.INSTANCE.ownerToSummons.remove(owner);
        }
        if ((level = summon.level) instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            SummonManager.removeFromRecastData(serverLevel, owner, summonUuid);
        }
    }

    public void handlePlayerDisconnect(ServerPlayer serverPlayer) {
        Set<UUID> summons = this.ownerToSummons.get(serverPlayer.getUUID());
        if (summons == null) {
            return;
        }
        ServerLevel serverLevel = serverPlayer.serverLevel();
        ArrayList<CompoundTag> savedSummons = new ArrayList<CompoundTag>();
        for (UUID uuid : summons) {
            Entity entity = serverLevel.getEntity(uuid);
            if (entity == null) continue;
            CompoundTag saveData = new CompoundTag();
            entity.save(saveData);
            int durationRemaining = INSTANCE.getExpirationTick(entity.getUUID()) - serverLevel.getServer().getTickCount();
            saveData.putInt("summon_duration_remaining", durationRemaining);
            entity.setRemoved(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
            savedSummons.add(saveData);
        }
        IronsDataStorage.INSTANCE.setDirty();
        SummonManager.INSTANCE.offlineSummonersToSavedEntities.put(serverPlayer.getUUID(), savedSummons);
        INSTANCE.stopTrackingSummonerAndSummons((Entity)serverPlayer);
    }

    public static boolean recastFinishedHelper(ServerPlayer serverPlayer, RecastInstance recastInstance, RecastResult recastResult, ICastDataSerializable castDataSerializable) {
        if (recastResult == RecastResult.COUNTERSPELL) {
            MagicData.getPlayerMagicData((LivingEntity)serverPlayer).getPlayerRecasts().forceAddRecast(recastInstance);
        } else if (recastResult != RecastResult.TIMEOUT) {
            if (castDataSerializable instanceof SummonedEntitiesCastData) {
                SummonedEntitiesCastData summonedEntitiesCastData = (SummonedEntitiesCastData)castDataSerializable;
                ServerLevel serverLevel = serverPlayer.serverLevel();
                summonedEntitiesCastData.getSummons().forEach(uuid -> {
                    Entity toRemove = serverLevel.getEntity(uuid);
                    if (toRemove instanceof IMagicSummon) {
                        IMagicSummon summon = (IMagicSummon)toRemove;
                        summon.onUnSummon();
                    } else if (toRemove != null) {
                        toRemove.discard();
                    }
                });
            }
        } else if (ItemRegistry.GREATER_CONJURERS_TALISMAN.get().isEquippedBy((LivingEntity)serverPlayer)) {
            return false;
        }
        return true;
    }

    private static void removeFromRecastData(ServerLevel level, UUID ownerUuid, UUID summonUuid) {
        Entity entity = level.getEntity(ownerUuid);
        if (!(entity instanceof Player)) {
            return;
        }
        Player player = (Player)entity;
        MagicData playerMagicData = MagicData.getPlayerMagicData((LivingEntity)player);
        PlayerRecasts recasts = playerMagicData.getPlayerRecasts();
        for (RecastInstance recastInstance : recasts.getActiveRecasts()) {
            SummonedEntitiesCastData summonData;
            ICastDataSerializable iCastDataSerializable = recastInstance.getCastData();
            if (!(iCastDataSerializable instanceof SummonedEntitiesCastData) || !(summonData = (SummonedEntitiesCastData)iCastDataSerializable).getSummons().contains(summonUuid)) continue;
            summonData.handleRemove(summonUuid, playerMagicData, recastInstance);
            break;
        }
    }

    private Optional<ExpirationInstance> getExpirationInstance(UUID uuid) {
        for (ExpirationInstance inst : this.summonExpirations) {
            if (!inst.uuid.equals(uuid)) continue;
            return Optional.of(inst);
        }
        return Optional.empty();
    }

    private int getExpirationTick(UUID uuid) {
        return this.getExpirationInstance(uuid).map(ExpirationInstance::expirationServerTick).orElse(0);
    }

    private static void startTrackingSummon(Entity owner, Entity summon) {
        Set summons = SummonManager.INSTANCE.ownerToSummons.computeIfAbsent(owner.getUUID(), uuid -> new HashSet());
        summons.add(summon.getUUID());
        IronsDataStorage.INSTANCE.setDirty();
    }

    private void stopTrackingSummonerAndSummons(Entity summoner) {
        Set<UUID> summons = this.ownerToSummons.remove(summoner.getUUID());
        if (summons != null) {
            IronsDataStorage.INSTANCE.setDirty();
            summons.forEach(summonUUID -> {
                if (this.summonToOwner.remove(summonUUID) != null) {
                    this.getExpirationInstance((UUID)summonUUID).ifPresent(this.summonExpirations::remove);
                }
            });
        }
    }

    public static void stopTrackingExpiration(Entity summon) {
        INSTANCE.getExpirationInstance(summon.getUUID()).ifPresent(SummonManager.INSTANCE.summonExpirations::remove);
    }

    public CompoundTag serializeNBT(HolderLookup.Provider pRegistries) {
        CompoundTag manager = new CompoundTag();
        ListTag offlineSummonsInstances = new ListTag();
        for (Map.Entry<UUID, List<CompoundTag>> entry : this.offlineSummonersToSavedEntities.entrySet()) {
            CompoundTag tag = new CompoundTag();
            tag.putUUID("summoner", entry.getKey());
            ListTag summons = new ListTag();
            summons.addAll((Collection)entry.getValue());
            tag.put("summons", (Tag)summons);
            offlineSummonsInstances.add((Object)tag);
        }
        manager.put("OfflineSummons", (Tag)offlineSummonsInstances);
        return manager;
    }

    public void deserializeNBT(HolderLookup.Provider pRegistries, CompoundTag compoundTag) {
        ListTag offline = compoundTag.getList("OfflineSummons", 10);
        for (Tag tag : offline) {
            CompoundTag entry = (CompoundTag)tag;
            UUID uuid = entry.getUUID("summoner");
            ListTag summons = entry.getList("summons", 10);
            ArrayList summonsList = new ArrayList();
            summons.forEach(t -> summonsList.add((CompoundTag)t));
            this.offlineSummonersToSavedEntities.put(uuid, summonsList);
        }
    }

    @SubscribeEvent
    public static void levelTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        int tick = server.getTickCount();
        if (!SummonManager.INSTANCE.summonExpirations.isEmpty() && tick % 20 == 0) {
            ExpirationInstance nextDespawn = SummonManager.INSTANCE.summonExpirations.peek();
            while (nextDespawn.expirationServerTick < tick) {
                SummonManager.INSTANCE.summonExpirations.remove();
                UUID uuid = nextDespawn.uuid;
                Entity toRemove = Streams.of((Iterable)server.getAllLevels()).map(level -> level.getEntity(uuid)).filter(Objects::nonNull).findFirst().orElse(null);
                if (toRemove instanceof IMagicSummon) {
                    IMagicSummon summon = (IMagicSummon)toRemove;
                    summon.onUnSummon();
                } else if (toRemove != null) {
                    toRemove.discard();
                }
                if (SummonManager.INSTANCE.summonExpirations.isEmpty()) break;
                nextDespawn = SummonManager.INSTANCE.summonExpirations.peek();
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            INSTANCE.handlePlayerDisconnect(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        event.getServer().getPlayerList().getPlayers().forEach(INSTANCE::handlePlayerDisconnect);
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        Level level = player.level;
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            IronsDataStorage.INSTANCE.setDirty();
            List<CompoundTag> savedSummons = SummonManager.INSTANCE.offlineSummonersToSavedEntities.remove(player.getUUID());
            MinecraftServer server = serverLevel.getServer();
            if (savedSummons != null) {
                HashSet<UUID> summonsSet = new HashSet<UUID>();
                UUID ownerUUID = player.getUUID();
                for (CompoundTag summon : savedSummons) {
                    Entity summonedEntity = EntityType.create((CompoundTag)summon, (Level)serverLevel).orElse(null);
                    if (summonedEntity != null) {
                        serverLevel.addFreshEntityWithPassengers(summonedEntity);
                        UUID summonUUID = summonedEntity.getUUID();
                        summonsSet.add(summonUUID);
                        SummonManager.INSTANCE.summonToOwner.put(summonUUID, ownerUUID);
                        SummonManager.INSTANCE.summonExpirations.add(new ExpirationInstance(summonUUID, server.getTickCount(), server.getTickCount() + summon.getInt("summon_duration_remaining")));
                    }
                    SummonManager.INSTANCE.ownerToSummons.put(ownerUUID, summonsSet);
                }
            }
        }
    }

    record ExpirationInstance(UUID uuid, int summonedServerTick, int expirationServerTick) {
    }
}

