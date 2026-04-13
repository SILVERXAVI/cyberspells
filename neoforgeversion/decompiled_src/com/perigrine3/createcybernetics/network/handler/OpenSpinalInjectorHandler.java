/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 */
package com.perigrine3.createcybernetics.network.handler;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.network.payload.OpenSpinalInjectorPayload;
import com.perigrine3.createcybernetics.screen.custom.SpinalInjectorMenu;
import java.util.Map;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class OpenSpinalInjectorHandler {
    private OpenSpinalInjectorHandler() {
    }

    public static void handle(OpenSpinalInjectorPayload payload, IPayloadContext ctx) {
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
            final InjectorRef ref = OpenSpinalInjectorHandler.findInstalledInjector(data);
            if (ref == null) {
                return;
            }
            sp.openMenu(new MenuProvider(){

                public Component getDisplayName() {
                    return Component.translatable((String)"gui.spinalinjector.title");
                }

                public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
                    return new SpinalInjectorMenu(id, inv, ref.slot(), ref.index());
                }
            }, buf -> {
                buf.writeUtf(ref.slot().name());
                buf.writeVarInt(ref.index());
            });
        });
    }

    private static InjectorRef findInstalledInjector(PlayerCyberwareData data) {
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            CyberwareSlot slot = entry.getKey();
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (int i = 0; i < arr.length; ++i) {
                ItemStack st;
                InstalledCyberware cw = arr[i];
                if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || st.getItem() != ModItems.BONEUPGRADES_SPINALINJECTOR.get()) continue;
                return new InjectorRef(slot, i);
            }
        }
        return null;
    }

    private record InjectorRef(CyberwareSlot slot, int index) {
    }
}

