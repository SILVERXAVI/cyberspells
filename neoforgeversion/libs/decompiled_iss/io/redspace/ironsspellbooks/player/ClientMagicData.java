/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.player;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.capabilities.magic.ClientSpellTargetingData;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerCooldowns;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerRecasts;
import io.redspace.ironsspellbooks.capabilities.magic.SummonedEntitiesCastData;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class ClientMagicData {
    private static final MagicData playerMagicData = new MagicData();
    private static final Set<UUID> activeSummons = new HashSet<UUID>();
    private static final HashMap<Integer, SyncedSpellData> playerSyncedDataLookup = new HashMap();
    private static final SyncedSpellData emptySyncedData = new SyncedSpellData(-999);
    static SpellSelectionManager spellSelectionManager;
    private static ClientSpellTargetingData spellTargetingData;

    public static SpellSelectionManager getSpellSelectionManager() {
        Player player;
        if (spellSelectionManager == null && (player = MinecraftInstanceHelper.getPlayer()) != null) {
            spellSelectionManager = new SpellSelectionManager(player);
        }
        return spellSelectionManager;
    }

    public static void updateSpellSelectionManager(@NotNull ServerPlayer player) {
        spellSelectionManager = new SpellSelectionManager((Player)player);
    }

    public static void updateSpellSelectionManager() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            spellSelectionManager = new SpellSelectionManager((Player)Minecraft.getInstance().player);
        }
    }

    public static void setTargetingData(ClientSpellTargetingData spellTargetingData) {
        ClientMagicData.spellTargetingData = spellTargetingData;
    }

    public static ClientSpellTargetingData getTargetingData() {
        if (spellTargetingData == null) {
            ClientMagicData.setTargetingData(new ClientSpellTargetingData());
        }
        return spellTargetingData;
    }

    public static void resetTargetingData() {
        spellTargetingData = null;
    }

    public static PlayerCooldowns getCooldowns() {
        return playerMagicData.getPlayerCooldowns();
    }

    public static PlayerRecasts getRecasts() {
        return playerMagicData.getPlayerRecasts();
    }

    public static void cacheClientSummons() {
        PlayerRecasts recasts = ClientMagicData.getRecasts();
        activeSummons.clear();
        recasts.getActiveRecasts().forEach(instance -> {
            ICastDataSerializable patt0$temp = instance.getCastData();
            if (patt0$temp instanceof SummonedEntitiesCastData) {
                SummonedEntitiesCastData summonedEntitiesCastData = (SummonedEntitiesCastData)patt0$temp;
                activeSummons.addAll(summonedEntitiesCastData.getSummons());
            }
        });
    }

    public static void setRecasts(PlayerRecasts playerRecasts) {
        playerMagicData.setPlayerRecasts(playerRecasts);
        ClientMagicData.cacheClientSummons();
    }

    public static Set<UUID> getActiveSummons() {
        return activeSummons;
    }

    public static float getCooldownPercent(AbstractSpell spell) {
        return playerMagicData.getPlayerCooldowns().getCooldownPercent(spell);
    }

    public static int getPlayerMana() {
        return (int)playerMagicData.getMana();
    }

    public static void setMana(int playerMana) {
        playerMagicData.setMana(playerMana);
    }

    public static CastType getCastType() {
        return playerMagicData.getCastType();
    }

    public static String getCastingSpellId() {
        return playerMagicData.getCastingSpellId();
    }

    public static int getCastingSpellLevel() {
        return playerMagicData.getCastingSpellLevel();
    }

    public static int getCastDurationRemaining() {
        return playerMagicData.getCastDurationRemaining();
    }

    public static int getCastDuration() {
        return playerMagicData.getCastDuration();
    }

    public static boolean isCasting() {
        return playerMagicData.isCasting();
    }

    public static void handleCastDuration() {
        playerMagicData.handleCastDuration();
    }

    public static float getCastCompletionPercent() {
        return playerMagicData.getCastCompletionPercent();
    }

    public static void setClientCastState(String spellId, int spellLevel, int castDuration, CastSource castSource, String castingEquipmentSlot) {
        playerMagicData.initiateCast(SpellRegistry.getSpell(spellId), spellLevel, castDuration, castSource, castingEquipmentSlot);
    }

    public static void resetClientCastState(UUID playerUUID) {
        if (Minecraft.getInstance().player.getUUID().equals(playerUUID)) {
            playerMagicData.resetCastingState();
            ClientMagicData.resetTargetingData();
        }
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isUsingItem() && Minecraft.getInstance().player.getUUID().equals(playerUUID)) {
            Minecraft.getInstance().player.stopUsingItem();
        }
    }

    public static SyncedSpellData getSyncedSpellData(LivingEntity livingEntity) {
        if (livingEntity instanceof Player) {
            return playerSyncedDataLookup.getOrDefault(livingEntity.getId(), emptySyncedData);
        }
        if (livingEntity instanceof IMagicEntity) {
            IMagicEntity abstractSpellCastingMob = (IMagicEntity)livingEntity;
            return abstractSpellCastingMob.getMagicData().getSyncedData();
        }
        return new SyncedSpellData(null);
    }

    public static void handlePlayerSyncedData(SyncedSpellData playerSyncedData) {
        playerSyncedDataLookup.put(playerSyncedData.getServerPlayerId(), playerSyncedData);
    }

    public static void handleAbstractCastingMobSyncedData(int entityId, SyncedSpellData syncedSpellData) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        Entity entity = level.getEntity(entityId);
        if (entity instanceof IMagicEntity) {
            IMagicEntity abstractSpellCastingMob = (IMagicEntity)entity;
            abstractSpellCastingMob.setSyncedSpellData(syncedSpellData);
        }
    }
}

