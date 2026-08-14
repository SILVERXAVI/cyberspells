/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.commands.arguments.EntityArgument
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.neoforged.neoforge.attachment.AttachmentType
 */
package com.maxwell.cyber_ware_port.common.command;

import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.AttachmentType;

public class CyberwareCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal((String)"cyberware").requires(source -> source.hasPermission(2))).then(Commands.literal((String)"clear").then(Commands.argument((String)"target", (ArgumentType)EntityArgument.player()).executes(ctx -> CyberwareCommands.clearCyberware((CommandContext<CommandSourceStack>)ctx, EntityArgument.getPlayer((CommandContext)ctx, (String)"target")))))).then(Commands.literal((String)"heal").then(Commands.argument((String)"target", (ArgumentType)EntityArgument.player()).executes(ctx -> CyberwareCommands.healCyberware((CommandContext<CommandSourceStack>)ctx, EntityArgument.getPlayer((CommandContext)ctx, (String)"target")))))).then(Commands.literal((String)"admin").then(Commands.literal((String)"reset_human").then(Commands.argument((String)"target", (ArgumentType)EntityArgument.player()).executes(ctx -> CyberwareCommands.resetToHuman((CommandContext<CommandSourceStack>)ctx, EntityArgument.getPlayer((CommandContext)ctx, (String)"target")))))));
    }

    private static int resetToHuman(CommandContext<CommandSourceStack> ctx, ServerPlayer player) {
        CyberwareUserData data = (CyberwareUserData)player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
        if (data != null) {
            data.resetToHuman();
            data.recalculateCapacity(player);
            data.syncToClient(player);
            ((CommandSourceStack)ctx.getSource()).sendSuccess(() -> Component.literal((String)("Successfully reset " + player.getName().getString() + " to human state.")), true);
            player.sendSystemMessage((Component)Component.literal((String)"Your cyberware has been reset by an administrator."));
        }
        return 1;
    }

    private static int clearCyberware(CommandContext<CommandSourceStack> ctx, ServerPlayer player) {
        CyberwareUserData data = (CyberwareUserData)player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
        if (data != null) {
            data.resetToHuman();
            data.recalculateCapacity(player);
            data.syncToClient(player);
            ((CommandSourceStack)ctx.getSource()).sendSuccess(() -> Component.literal((String)("Reset cyberware for " + player.getName().getString())), true);
        }
        return 1;
    }

    private static int healCyberware(CommandContext<CommandSourceStack> ctx, ServerPlayer player) {
        CyberwareUserData data = (CyberwareUserData)player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
        if (data != null) {
            data.receiveEnergy(data.getMaxEnergyStored(), false);
            data.applyImmunity(24000);
            data.syncToClient(player);
            ((CommandSourceStack)ctx.getSource()).sendSuccess(() -> Component.literal((String)("Cyberware energy/status restored for " + player.getName().getString())), true);
        }
        return 1;
    }
}

