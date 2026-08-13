/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.neoforge.common.util.INBTSerializable
 *  net.neoforged.neoforge.entity.PartEntity
 */
package io.redspace.ironsspellbooks.capabilities.magic;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerRecasts;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.data.IronsDataStorage;
import io.redspace.ironsspellbooks.entity.spells.portal.PortalData;
import io.redspace.ironsspellbooks.entity.spells.portal.PortalEntity;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.entity.PartEntity;

public class PortalManager
implements INBTSerializable<CompoundTag> {
    public static final PortalManager INSTANCE = new PortalManager();
    public final HashMap<UUID, HashMap<UUID, AtomicInteger>> cooldownLookup = new HashMap();
    private final HashMap<UUID, PortalData> portalLookup = new HashMap();
    private static final int cooldownTicks = 10;

    public PortalData getPortalData(PortalEntity portalEntity) {
        return this.portalLookup.get(portalEntity.getUUID());
    }

    public PortalData getPortalData(UUID portalId) {
        return this.portalLookup.get(portalId);
    }

    public void addPortalData(UUID portalEntityUUID, PortalData portalData) {
        this.portalLookup.put(portalEntityUUID, portalData);
        IronsDataStorage.INSTANCE.setDirty();
    }

    public void addPortalCooldown(Entity entity, UUID portalId) {
        PortalData portalData = this.portalLookup.get(portalId);
        if (portalData == null) {
            return;
        }
        this.addDirectPortalCooldown(entity, portalData.getConnectedPortalUUID(portalId));
    }

    public void addDirectPortalCooldown(Entity entity, UUID portalId) {
        HashMap playerMap = this.cooldownLookup.computeIfAbsent(portalId, k -> new HashMap());
        playerMap.put(entity.getUUID(), new AtomicInteger(10));
    }

    public boolean isEntityOnCooldown(Entity entity, UUID portalId) {
        HashMap<UUID, AtomicInteger> playerMap = this.cooldownLookup.get(portalId);
        return playerMap != null && playerMap.containsKey(entity.getUUID());
    }

    public boolean isPortalConnected(UUID portalID) {
        UUID connectedPortal;
        PortalData portalData = this.portalLookup.get(portalID);
        return portalData != null && (connectedPortal = portalData.getConnectedPortalUUID(portalID)) != null;
    }

    public boolean canUsePortal(UUID portalId, Entity entityToTeleport) {
        PortalData portalData = this.portalLookup.get(portalId);
        return !entityToTeleport.isPassenger() && !(entityToTeleport instanceof PartEntity) && portalData != null && portalData.portalEntityId1 != null && portalData.portalEntityId2 != null && !this.isEntityOnCooldown(entityToTeleport, portalId);
    }

    public boolean canUsePortal(PortalEntity portalEntity, Entity entity) {
        if (portalEntity == null || entity == null) {
            return false;
        }
        return this.canUsePortal(portalEntity.getUUID(), entity);
    }

    public void processCooldownTick(UUID portalUUID, int delta) {
        HashMap<UUID, AtomicInteger> playerCooldownsForPortal = this.cooldownLookup.get(portalUUID);
        if (playerCooldownsForPortal != null) {
            playerCooldownsForPortal.entrySet().stream().filter(item -> ((AtomicInteger)item.getValue()).addAndGet(delta) <= 0).toList().forEach(itemToRemove -> playerCooldownsForPortal.remove(itemToRemove.getKey()));
        }
    }

    public void processDelayCooldown(UUID portalUUID, UUID playerUUID, int delta) {
        AtomicInteger cooldown;
        HashMap<UUID, AtomicInteger> playerCooldownsForPortal = this.cooldownLookup.get(portalUUID);
        if (playerCooldownsForPortal != null && (cooldown = playerCooldownsForPortal.get(playerUUID)) != null) {
            cooldown.addAndGet(delta);
        }
    }

    public void removePortalData(UUID portalUUID) {
        this.portalLookup.remove(portalUUID);
        this.cooldownLookup.remove(portalUUID);
        IronsDataStorage.INSTANCE.setDirty();
    }

    public void killPortal(UUID portalUUID, UUID ownerUUID) {
        PortalData removedPortalData = this.portalLookup.remove(portalUUID);
        if (removedPortalData != null) {
            if (removedPortalData.portalEntityId2 == null || removedPortalData.globalPos2 == null) {
                this.tryCancelRecast(portalUUID, ownerUUID);
            } else {
                UUID connectedPortalUUID = removedPortalData.getConnectedPortalUUID(portalUUID);
                if (connectedPortalUUID != null) {
                    removedPortalData = this.portalLookup.remove(connectedPortalUUID);
                    removedPortalData.getConnectedPortalPos(portalUUID).ifPresent(globalPos -> {
                        Entity connectedPortalToRemove;
                        ServerLevel level = IronsSpellbooks.MCS.getLevel(globalPos.dimension());
                        if (level != null && (connectedPortalToRemove = level.getEntity(connectedPortalUUID)) != null) {
                            connectedPortalToRemove.discard();
                        }
                    });
                    this.cooldownLookup.remove(connectedPortalUUID);
                }
            }
        }
        this.cooldownLookup.remove(portalUUID);
        IronsDataStorage.INSTANCE.setDirty();
    }

    private void tryCancelRecast(UUID portalUUID, UUID ownerUUID) {
        IronsSpellbooks.MCS.getAllLevels().forEach(level -> {
            ICastDataSerializable patt0$temp;
            String spellId;
            MagicData magicData;
            PlayerRecasts playerRecasts;
            RecastInstance recastInstance;
            Player player = level.getPlayerByUUID(ownerUUID);
            if (player != null && (recastInstance = (playerRecasts = (magicData = MagicData.getPlayerMagicData((LivingEntity)player)).getPlayerRecasts()).getRecastInstance(spellId = SpellRegistry.PORTAL_SPELL.get().getSpellId())) != null && (patt0$temp = recastInstance.castData) instanceof PortalData) {
                PortalData portalData = (PortalData)patt0$temp;
                if (portalData.portalEntityId1 == portalUUID) {
                    playerRecasts.removeRecast(recastInstance, RecastResult.COUNTERSPELL);
                    return;
                }
            }
        });
    }

    public CompoundTag serializeNBT(HolderLookup.Provider pRegistries) {
        CompoundTag tag = new CompoundTag();
        ListTag portalLookupTag = new ListTag();
        if (!this.portalLookup.isEmpty()) {
            portalLookupTag.addAll(this.portalLookup.entrySet().stream().map(entry -> {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putUUID("key", (UUID)entry.getKey());
                itemTag.put("value", (Tag)((PortalData)entry.getValue()).serializeNBT(pRegistries));
                return itemTag;
            }).toList());
        }
        tag.put("portalLookup", (Tag)portalLookupTag);
        return tag;
    }

    public void deserializeNBT(HolderLookup.Provider pRegistries, CompoundTag compoundTag) {
        ListTag portalLookupTag;
        if (compoundTag.contains("portalLookup") && (portalLookupTag = (ListTag)compoundTag.get("portalLookup")) != null) {
            portalLookupTag.forEach(tag -> {
                CompoundTag portalLookupItem = (CompoundTag)tag;
                PortalData portalData = new PortalData();
                portalData.deserializeNBT(pRegistries, portalLookupItem.getCompound("value"));
                this.portalLookup.put(portalLookupItem.getUUID("key"), portalData);
            });
        }
    }
}

