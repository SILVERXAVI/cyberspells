/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.network.PacketDistributor
 *  net.neoforged.neoforge.network.registration.PayloadRegistrar
 */
package com.perigrine3.createcybernetics.network;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.client.render.SandevistanMirageTrail;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.effect.AerostasisGyrobladderEffect;
import com.perigrine3.createcybernetics.effect.GuardianEyeEffect;
import com.perigrine3.createcybernetics.effect.NeuralContextualizerEffect;
import com.perigrine3.createcybernetics.effect.SculkLungsEffect;
import com.perigrine3.createcybernetics.event.custom.SandevistanSnapshotRelay;
import com.perigrine3.createcybernetics.network.handler.ArmCannonFireHandler;
import com.perigrine3.createcybernetics.network.handler.ArmCannonWheelClientHandlers;
import com.perigrine3.createcybernetics.network.handler.ArmCannonWheelHandlers;
import com.perigrine3.createcybernetics.network.handler.InfologSaveChipwareHandler;
import com.perigrine3.createcybernetics.network.handler.OpenArmCannonHandler;
import com.perigrine3.createcybernetics.network.handler.OpenChipwareMiniHandler;
import com.perigrine3.createcybernetics.network.handler.OpenHeatEnginePayloadHandler;
import com.perigrine3.createcybernetics.network.handler.OpenSpinalInjectorHandler;
import com.perigrine3.createcybernetics.network.payload.ArmCannonFirePayload;
import com.perigrine3.createcybernetics.network.payload.ArmCannonWheelPayloads;
import com.perigrine3.createcybernetics.network.payload.CerebralShutdownStatePayload;
import com.perigrine3.createcybernetics.network.payload.CopernicusOxygenSyncPayload;
import com.perigrine3.createcybernetics.network.payload.CyberwareEnabledStatePayload;
import com.perigrine3.createcybernetics.network.payload.CyberwareTogglePayloads;
import com.perigrine3.createcybernetics.network.payload.EnergyHudSnapshotPayload;
import com.perigrine3.createcybernetics.network.payload.EnergyHudSyncPayload;
import com.perigrine3.createcybernetics.network.payload.InfologSaveChipwarePayload;
import com.perigrine3.createcybernetics.network.payload.OpenArmCannonPayload;
import com.perigrine3.createcybernetics.network.payload.OpenChipwareMiniPayload;
import com.perigrine3.createcybernetics.network.payload.OpenExpandedInventoryPayload;
import com.perigrine3.createcybernetics.network.payload.OpenHeatEnginePayload;
import com.perigrine3.createcybernetics.network.payload.OpenSpinalInjectorPayload;
import com.perigrine3.createcybernetics.network.payload.SandevistanSnapshotC2SPayload;
import com.perigrine3.createcybernetics.network.payload.SandevistanSnapshotPayload;
import com.perigrine3.createcybernetics.network.payload.SetChipwareShardPayload;
import com.perigrine3.createcybernetics.network.payload.TargetingHighlightPayload;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.Map;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class ModPayloads {
    private ModPayloads() {
    }

    public static void register(PayloadRegistrar r) {
        r.playToServer(SculkLungsEffect.SonicUseHeldPayload.TYPE, SculkLungsEffect.SonicUseHeldPayload.STREAM_CODEC, (payload, ctx) -> ctx.enqueueWork(() -> {
            Player patt0$temp = ctx.player();
            if (patt0$temp instanceof ServerPlayer) {
                ServerPlayer sp = (ServerPlayer)patt0$temp;
                SculkLungsEffect.setUseHeld(sp, payload.held());
            }
        }));
        r.playToServer(GuardianEyeEffect.GuardianEyeUseHeldPayload.TYPE, GuardianEyeEffect.GuardianEyeUseHeldPayload.STREAM_CODEC, (payload, ctx) -> ctx.enqueueWork(() -> {
            Player patt0$temp = ctx.player();
            if (patt0$temp instanceof ServerPlayer) {
                ServerPlayer sp = (ServerPlayer)patt0$temp;
                GuardianEyeEffect.setUseHeld(sp, payload.held());
            }
        }));
        r.playToServer(AerostasisGyrobladderEffect.GyroJumpHeldPayload.TYPE, AerostasisGyrobladderEffect.GyroJumpHeldPayload.STREAM_CODEC, (payload, ctx) -> ctx.enqueueWork(() -> {
            Player patt0$temp = ctx.player();
            if (patt0$temp instanceof ServerPlayer) {
                ServerPlayer sp = (ServerPlayer)patt0$temp;
                AerostasisGyrobladderEffect.handleJumpHeldPayload(sp, payload.held());
            }
        }));
        r.playToServer(NeuralContextualizerEffect.SwapHotbarPayload.TYPE, NeuralContextualizerEffect.SwapHotbarPayload.STREAM_CODEC, (payload, ctx) -> ctx.enqueueWork(() -> {
            Player patt0$temp = ctx.player();
            if (patt0$temp instanceof ServerPlayer) {
                ServerPlayer sp = (ServerPlayer)patt0$temp;
                NeuralContextualizerEffect.handleSwapHotbarPayload(sp, payload.slot());
            }
        }));
        r.playToClient(TargetingHighlightPayload.TYPE, TargetingHighlightPayload.STREAM_CODEC, TargetingHighlightPayload::handle);
        r.playToServer(OpenExpandedInventoryPayload.TYPE, OpenExpandedInventoryPayload.STREAM_CODEC, (payload, ctx) -> ctx.enqueueWork(() -> OpenExpandedInventoryPayload.handle(payload, ctx)));
        r.playToServer(OpenSpinalInjectorPayload.TYPE, OpenSpinalInjectorPayload.STREAM_CODEC, OpenSpinalInjectorHandler::handle);
        r.playToServer(OpenArmCannonPayload.TYPE, OpenArmCannonPayload.STREAM_CODEC, OpenArmCannonHandler::handle);
        r.playToClient(EnergyHudSnapshotPayload.TYPE, EnergyHudSnapshotPayload.STREAM_CODEC, EnergyHudSnapshotPayload::handle);
        r.playToServer(ArmCannonWheelPayloads.RequestOpenArmCannonWheelPayload.TYPE, ArmCannonWheelPayloads.RequestOpenArmCannonWheelPayload.STREAM_CODEC, ArmCannonWheelHandlers::handleOpen);
        r.playToClient(ArmCannonWheelPayloads.OpenArmCannonWheelClientPayload.TYPE, ArmCannonWheelPayloads.OpenArmCannonWheelClientPayload.STREAM_CODEC, (payload, ctx) -> ctx.enqueueWork(() -> ArmCannonWheelClientHandlers.handleOpen(payload, ctx)));
        r.playToServer(ArmCannonWheelPayloads.SelectArmCannonAmmoSlotPayload.TYPE, ArmCannonWheelPayloads.SelectArmCannonAmmoSlotPayload.STREAM_CODEC, ArmCannonWheelHandlers::handleSelect);
        r.playToServer(ArmCannonFirePayload.TYPE, ArmCannonFirePayload.STREAM_CODEC, ArmCannonFireHandler::handle);
        r.playToServer(SetChipwareShardPayload.TYPE, SetChipwareShardPayload.STREAM_CODEC, (payload, ctx) -> ctx.enqueueWork(() -> {
            Player patt0$temp = ctx.player();
            if (!(patt0$temp instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer sp = (ServerPlayer)patt0$temp;
            PlayerCyberwareData data = (PlayerCyberwareData)sp.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            int slot = payload.slot();
            ItemStack stack = payload.stack();
            if (slot < 0 || slot >= 2) {
                return;
            }
            data.setChipwareStack(slot, stack);
            data.setDirty();
            sp.syncData(ModAttachments.CYBERWARE);
        }));
        r.playToServer(OpenChipwareMiniPayload.TYPE, OpenChipwareMiniPayload.STREAM_CODEC, OpenChipwareMiniHandler::handle);
        r.playToClient(CerebralShutdownStatePayload.TYPE, CerebralShutdownStatePayload.STREAM_CODEC, CerebralShutdownStatePayload::handle);
        r.playToClient(CopernicusOxygenSyncPayload.TYPE, CopernicusOxygenSyncPayload.STREAM_CODEC, CopernicusOxygenSyncPayload::handle);
        r.playToServer(OpenHeatEnginePayload.TYPE, OpenHeatEnginePayload.STREAM_CODEC, (payload, ctx) -> ctx.enqueueWork(() -> {
            Player patt0$temp = ctx.player();
            if (!(patt0$temp instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer sp = (ServerPlayer)patt0$temp;
            OpenHeatEnginePayloadHandler.handle(payload, sp);
        }));
        r.playToClient(SandevistanSnapshotPayload.TYPE, SandevistanSnapshotPayload.STREAM_CODEC, (payload, ctx) -> ctx.enqueueWork(() -> SandevistanMirageTrail.acceptNetworkSnapshot(payload)));
        r.playToServer(SandevistanSnapshotC2SPayload.TYPE, SandevistanSnapshotC2SPayload.STREAM_CODEC, (payload, ctx) -> ctx.enqueueWork(() -> {
            Player patt0$temp = ctx.player();
            if (patt0$temp instanceof ServerPlayer) {
                ServerPlayer sp = (ServerPlayer)patt0$temp;
                SandevistanSnapshotRelay.handle(sp, payload);
            }
        }));
        r.playToServer(InfologSaveChipwarePayload.TYPE, InfologSaveChipwarePayload.STREAM_CODEC, (payload, ctx) -> ctx.enqueueWork(() -> {
            Player patt0$temp = ctx.player();
            if (patt0$temp instanceof ServerPlayer) {
                ServerPlayer sp = (ServerPlayer)patt0$temp;
                InfologSaveChipwareHandler.handle(payload, sp);
            }
        }));
        r.playToClient(EnergyHudSyncPayload.TYPE, EnergyHudSyncPayload.STREAM_CODEC, EnergyHudSyncPayload::handle);
        r.playToServer(CyberwareTogglePayloads.RequestToggleStatesPayload.TYPE, CyberwareTogglePayloads.RequestToggleStatesPayload.STREAM_CODEC, (payload, ctx) -> ctx.enqueueWork(() -> {
            Player patt0$temp = ctx.player();
            if (!(patt0$temp instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer sp = (ServerPlayer)patt0$temp;
            if (!sp.hasData(ModAttachments.CYBERWARE)) {
                return;
            }
            PlayerCyberwareData data = (PlayerCyberwareData)sp.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
                CyberwareSlot slot = entry.getKey();
                InstalledCyberware[] arr = entry.getValue();
                if (arr == null) continue;
                for (int i = 0; i < arr.length; ++i) {
                    ItemStack stack;
                    InstalledCyberware inst = arr[i];
                    if (inst == null || (stack = inst.getItem()) == null || stack.isEmpty() || !stack.is(ModTags.Items.TOGGLEABLE_CYBERWARE)) continue;
                    boolean enabled = data.isEnabled(slot, i);
                    PacketDistributor.sendToPlayer((ServerPlayer)sp, (CustomPacketPayload)new CyberwareEnabledStatePayload(slot.name(), i, enabled), (CustomPacketPayload[])new CustomPacketPayload[0]);
                }
            }
        }));
        r.playToServer(CyberwareTogglePayloads.ToggleCyberwarePayload.TYPE, CyberwareTogglePayloads.ToggleCyberwarePayload.STREAM_CODEC, (payload, ctx) -> ctx.enqueueWork(() -> {
            CyberwareSlot slot;
            Player patt0$temp = ctx.player();
            if (!(patt0$temp instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer sp = (ServerPlayer)patt0$temp;
            if (!sp.hasData(ModAttachments.CYBERWARE)) {
                return;
            }
            PlayerCyberwareData data = (PlayerCyberwareData)sp.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            try {
                slot = CyberwareSlot.valueOf(payload.slotName());
            }
            catch (IllegalArgumentException ex) {
                return;
            }
            int index = payload.index();
            InstalledCyberware inst = data.get(slot, index);
            if (inst == null) {
                return;
            }
            ItemStack stack = inst.getItem();
            if (stack == null || stack.isEmpty()) {
                return;
            }
            if (!stack.is(ModTags.Items.TOGGLEABLE_CYBERWARE)) {
                return;
            }
            boolean nowEnabled = data.toggleEnabled(slot, index);
            PacketDistributor.sendToPlayer((ServerPlayer)sp, (CustomPacketPayload)new CyberwareEnabledStatePayload(slot.name(), index, nowEnabled), (CustomPacketPayload[])new CustomPacketPayload[0]);
        }));
        r.playToClient(CyberwareEnabledStatePayload.TYPE, CyberwareEnabledStatePayload.STREAM_CODEC, (payload, ctx) -> ctx.enqueueWork(() -> CyberwareEnabledStatePayload.handle(payload, ctx)));
    }
}

