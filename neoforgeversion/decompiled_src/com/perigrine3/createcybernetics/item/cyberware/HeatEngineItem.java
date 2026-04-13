/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.Level
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class HeatEngineItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;

    public HeatEngineItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.organsupgrades_heatengine.energy").withStyle(ChatFormatting.DARK_GREEN));
        }
    }

    @Override
    public int getEnergyGeneratedPerTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        return data.isHeatEngineActive() ? 50 : 0;
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.ORGANS);
    }

    @Override
    public boolean replacesOrgan() {
        return false;
    }

    @Override
    public Set<CyberwareSlot> getReplacedOrgans() {
        return Set.of();
    }

    @Override
    public void onInstalled(Player player) {
        if (player instanceof ServerPlayer) {
            ServerPlayer sp = (ServerPlayer)player;
            sp.syncData(ModAttachments.CYBERWARE);
        }
    }

    @Override
    public void onRemoved(Player player) {
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer sp = (ServerPlayer)player;
        PlayerCyberwareData data = (PlayerCyberwareData)sp.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        data.setHeatEngineBurnTime(0);
        data.setHeatEngineBurnTimeTotal(0);
        data.setHeatEngineCookTime(0);
        data.setHeatEngineCookTimeTotal(200);
        data.setDirty();
        sp.syncData(ModAttachments.CYBERWARE);
    }

    @Override
    public void onTick(Player player) {
        boolean isBurning;
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer sp = (ServerPlayer)player;
        PlayerCyberwareData data = (PlayerCyberwareData)sp.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        boolean wasBurning = data.getHeatEngineBurnTime() > 0;
        data.tickHeatEngine(sp);
        boolean bl = isBurning = data.getHeatEngineBurnTime() > 0;
        if (wasBurning != isBurning) {
            sp.syncData(ModAttachments.CYBERWARE);
        } else if (isBurning && sp.tickCount % 10 == 0) {
            sp.syncData(ModAttachments.CYBERWARE);
        }
        if (isBurning) {
            HeatEngineItem.spawnHeatEngineParticles(sp);
        }
    }

    private static void spawnHeatEngineParticles(ServerPlayer sp) {
        Level level = sp.level();
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel level2 = (ServerLevel)level;
        if (sp.tickCount % Math.max(1, HeatEngineParticleTuning.SPAWN_EVERY_TICKS) != 0) {
            return;
        }
        Vec3Pos belly = HeatEngineItem.anchoredToBody(sp, HeatEngineParticleTuning.BELLY_UP, HeatEngineParticleTuning.BELLY_FORWARD, HeatEngineParticleTuning.BELLY_RIGHT);
        Vec3Pos back = HeatEngineItem.anchoredToBody(sp, HeatEngineParticleTuning.BACK_UP, HeatEngineParticleTuning.BACK_FORWARD, HeatEngineParticleTuning.BACK_RIGHT);
        HeatEngineItem.spawnJittered(level2, (ParticleOptions)ParticleTypes.FLAME, belly, HeatEngineParticleTuning.FIRE_COUNT, HeatEngineParticleTuning.FIRE_SPEED);
        HeatEngineItem.spawnJittered(level2, (ParticleOptions)ParticleTypes.CAMPFIRE_COSY_SMOKE, back, HeatEngineParticleTuning.SMOKE_COUNT, HeatEngineParticleTuning.SMOKE_SPEED);
    }

    private static Vec3Pos anchoredToBody(ServerPlayer sp, float up, float forward, float right) {
        float yawDeg = sp.yBodyRot;
        float yawRad = yawDeg * ((float)Math.PI / 180);
        float sin = Mth.sin((float)yawRad);
        float cos = Mth.cos((float)yawRad);
        double px = sp.getX();
        double py = sp.getY();
        double pz = sp.getZ();
        double x = px + (double)(-sin * forward) + (double)(cos * right);
        double y = py + (double)up;
        double z = pz + (double)(cos * forward) + (double)(sin * right);
        return new Vec3Pos(x, y, z);
    }

    private static void spawnJittered(ServerLevel level, ParticleOptions particle, Vec3Pos pos, int count, double speed) {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        double jh = HeatEngineParticleTuning.JITTER_H;
        double jv = HeatEngineParticleTuning.JITTER_V;
        for (int i = 0; i < Math.max(0, count); ++i) {
            double jx = (r.nextDouble() - 0.5) * (jh * 2.0);
            double jy = (r.nextDouble() - 0.5) * (jv * 2.0);
            double jz = (r.nextDouble() - 0.5) * (jh * 2.0);
            level.sendParticles(particle, pos.x + jx, pos.y + jy, pos.z + jz, 1, 0.0, 0.0, 0.0, speed);
        }
    }

    private static final class HeatEngineParticleTuning {
        static float BELLY_UP = 0.95f;
        static float BELLY_FORWARD = 0.15f;
        static float BELLY_RIGHT = 0.0f;
        static float BACK_UP = 1.05f;
        static float BACK_FORWARD = -0.28f;
        static float BACK_RIGHT = 0.0f;
        static float JITTER_H = 0.06f;
        static float JITTER_V = 0.04f;
        static int FIRE_COUNT = 2;
        static int SMOKE_COUNT = 1;
        static double FIRE_SPEED = 0.01;
        static double SMOKE_SPEED = 0.02;
        static int SPAWN_EVERY_TICKS = 20;

        private HeatEngineParticleTuning() {
        }
    }

    private record Vec3Pos(double x, double y, double z) {
    }
}

