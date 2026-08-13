/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent$LoggingOut
 *  net.neoforged.neoforge.event.entity.EntityJoinLevelEvent
 *  net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent
 */
package io.redspace.ironsspellbooks.particle;

import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.entity.spells.AbstractShieldEntity;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;

@EventBusSubscriber(value={Dist.CLIENT})
public class ClientShieldHelper {
    private static final ArrayList<AbstractShieldEntity> trackedEntities = new ArrayList();

    @SubscribeEvent
    public static synchronized void trackShieldCreated(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof AbstractShieldEntity) {
            AbstractShieldEntity ase = (AbstractShieldEntity)entity;
            trackedEntities.add(ase);
        }
    }

    @SubscribeEvent
    public static synchronized void trackShieldRemoved(EntityLeaveLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof AbstractShieldEntity) {
            AbstractShieldEntity ase = (AbstractShieldEntity)entity;
            trackedEntities.remove(ase);
        }
    }

    @SubscribeEvent
    public static synchronized void onPlayerLogOut(ClientPlayerNetworkEvent.LoggingOut event) {
        trackedEntities.clear();
    }

    public static synchronized List<VoxelShape> getShieldsFor(AABB boundingBox) {
        if (trackedEntities.isEmpty() || !((Boolean)ClientConfigs.SHIELD_PARTICLE_COLLISIONS.get()).booleanValue()) {
            return List.of();
        }
        ArrayList<VoxelShape> shieldCollisions = new ArrayList<VoxelShape>();
        for (AbstractShieldEntity s : trackedEntities) {
            if (!boundingBox.intersects(s.getBoundingBox().inflate(1.0))) continue;
            shieldCollisions.addAll(s.getVoxels());
        }
        return shieldCollisions;
    }
}

