/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.level.GameRules
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.event.entity.EntityJoinLevelEvent
 */
package com.maxwell.cyber_ware_port.common.entity;

import com.maxwell.cyber_ware_port.api.json.MobDataManager;
import com.maxwell.cyber_ware_port.common.block.radio.RadioKitBlock;
import com.maxwell.cyber_ware_port.common.block.radio.tower.RadioTowerCoreBlock;
import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid="cyber_ware_port", bus=EventBusSubscriber.Bus.GAME)
public class MobSpawnerEvents {
    private static final double RADIO_KIT_BOOST = 0.3;
    private static final double RADIO_TOWER_BOOST = 0.15;
    private static final long ACTIVE_TIMEOUT = 420L;
    private static final double PLAYER_BEACON_BOOST = 0.25;

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        ServerLevel level;
        block7: {
            block6: {
                Level level2 = event.getLevel();
                if (!(level2 instanceof ServerLevel)) break block6;
                level = (ServerLevel)level2;
                if (!event.loadedFromDisk()) break block7;
            }
            return;
        }
        if (!level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            return;
        }
        Entity entity = event.getEntity();
        if (!(entity instanceof Mob)) {
            return;
        }
        Mob vanillaMob = (Mob)entity;
        double bonusChance = MobSpawnerEvents.calculateBonusChance(level, vanillaMob.blockPosition());
        EntityType type = entity.getType();
        MobDataManager.MobData mobData = MobDataManager.MOB_DATA.get(type);
        if (mobData != null && mobData.replaceWith != null) {
            MobSpawnerEvents.tryReplaceMob(event, level, vanillaMob, mobData.replaceWith, mobData.chance + bonusChance);
        }
    }

    private static double calculateBonusChance(ServerLevel level, BlockPos spawnPos) {
        long currentTime;
        double bonus = 0.0;
        ResourceKey dimKey = level.dimension();
        if (MobSpawnerEvents.isActive(RadioKitBlock.LAST_ACTIVE_TIME, (ResourceKey<Level>)dimKey, currentTime = level.getGameTime())) {
            bonus += 0.3;
        }
        if (MobSpawnerEvents.isActive(RadioTowerCoreBlock.LAST_TOWER_ACTIVE_TIME, (ResourceKey<Level>)dimKey, currentTime)) {
            bonus += 0.15;
        }
        List players = level.players();
        for (Player player : players) {
            if (!(player.distanceToSqr((double)spawnPos.getX(), (double)spawnPos.getY(), (double)spawnPos.getZ()) < 2304.0) || !MobSpawnerEvents.isCranialBroadcasterActive(player)) continue;
            bonus += 0.25;
            break;
        }
        return bonus;
    }

    private static boolean isCranialBroadcasterActive(Player player) {
        return ((CyberwareUserData)player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get())).isCyberwareInstalled((Item)ModItems.CRANIAL_BROADCASTER.get());
    }

    private static boolean isActive(Map<ResourceKey<Level>, Long> timeMap, ResourceKey<Level> dimKey, long currentTime) {
        Long lastActive = timeMap.get(dimKey);
        if (lastActive == null) {
            return false;
        }
        return Math.abs(currentTime - lastActive) < 420L;
    }

    private static void tryReplaceMob(EntityJoinLevelEvent event, ServerLevel level, Mob original, EntityType<?> newType, double chance) {
        Mob customMob;
        if ((double)level.getRandom().nextFloat() < chance && (customMob = original.convertTo(newType, true)) != null) {
            customMob.finalizeSpawn((ServerLevelAccessor)level, level.getCurrentDifficultyAt(original.blockPosition()), MobSpawnType.CONVERSION, null);
            event.setCanceled(true);
        }
    }
}

