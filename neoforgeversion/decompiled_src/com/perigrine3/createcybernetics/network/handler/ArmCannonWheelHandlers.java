/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.util.Mth
 *  net.minecraft.world.Container
 *  net.minecraft.world.SimpleContainer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.network.PacketDistributor
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package com.perigrine3.createcybernetics.network.handler;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.item.cyberware.ArmCannonItem;
import com.perigrine3.createcybernetics.network.payload.ArmCannonWheelPayloads;
import java.util.Map;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class ArmCannonWheelHandlers {
    private ArmCannonWheelHandlers() {
    }

    public static void handleOpen(ArmCannonWheelPayloads.RequestOpenArmCannonWheelPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
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
            ItemStack cannonStack = ArmCannonWheelHandlers.findInstalledArmCannonStack(data);
            if (cannonStack.isEmpty()) {
                return;
            }
            int segments = 4;
            int selected = data.getArmCannonSelected();
            PacketDistributor.sendToPlayer((ServerPlayer)sp, (CustomPacketPayload)new ArmCannonWheelPayloads.OpenArmCannonWheelClientPayload(segments, selected), (CustomPacketPayload[])new CustomPacketPayload[0]);
        });
    }

    public static void handleSelect(ArmCannonWheelPayloads.SelectArmCannonAmmoSlotPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
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
            ItemStack cannonStack = ArmCannonWheelHandlers.findInstalledArmCannonStack(data);
            if (cannonStack.isEmpty()) {
                return;
            }
            int idx = Mth.clamp((int)payload.slotIndex(), (int)0, (int)3);
            SimpleContainer tmp = new SimpleContainer(4);
            ArmCannonItem.loadFromInstalledStack(cannonStack, (HolderLookup.Provider)sp.level().registryAccess(), (Container)tmp);
            ItemStack chosen = tmp.getItem(idx);
            if (chosen == null || chosen.isEmpty() || !ArmCannonItem.isValidStoredItem(chosen)) {
                return;
            }
            data.setArmCannonSelected(idx);
            data.setDirty();
            sp.syncData(ModAttachments.CYBERWARE);
        });
    }

    private static ItemStack findInstalledArmCannonStack(PlayerCyberwareData data) {
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            CyberwareSlot slot = entry.getKey();
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (int i = 0; i < arr.length; ++i) {
                ItemStack st;
                InstalledCyberware cw = arr[i];
                if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || st.getItem() != ModItems.ARMUPGRADES_ARMCANNON.get()) continue;
                return st;
            }
        }
        return ItemStack.EMPTY;
    }
}

