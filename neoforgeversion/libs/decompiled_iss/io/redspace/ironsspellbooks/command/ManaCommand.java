/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.IntegerArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.tree.LiteralCommandNode
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.commands.arguments.EntityArgument
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.LivingEntity
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package io.redspace.ironsspellbooks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;

public class ManaCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode command = dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal((String)"mana").requires(p -> p.hasPermission(2))).then(Commands.literal((String)"set").then(Commands.argument((String)"targets", (ArgumentType)EntityArgument.players()).then(Commands.argument((String)"amount", (ArgumentType)IntegerArgumentType.integer()).executes(context -> ManaCommand.changeMana((CommandSourceStack)context.getSource(), EntityArgument.getPlayers((CommandContext)context, (String)"targets"), IntegerArgumentType.getInteger((CommandContext)context, (String)"amount"), true)))))).then(Commands.literal((String)"add").then(Commands.argument((String)"targets", (ArgumentType)EntityArgument.players()).then(Commands.argument((String)"amount", (ArgumentType)IntegerArgumentType.integer()).executes(context -> ManaCommand.changeMana((CommandSourceStack)context.getSource(), EntityArgument.getPlayers((CommandContext)context, (String)"targets"), IntegerArgumentType.getInteger((CommandContext)context, (String)"amount"), false)))))).then(Commands.literal((String)"get").then(Commands.argument((String)"targets", (ArgumentType)EntityArgument.player()).executes(context -> ManaCommand.getMana((CommandSourceStack)context.getSource(), EntityArgument.getPlayer((CommandContext)context, (String)"targets"))))));
    }

    private static int changeMana(CommandSourceStack source, Collection<ServerPlayer> targets, int amount, boolean set) {
        String s;
        targets.forEach(serverPlayer -> {
            MagicData pmg = MagicData.getPlayerMagicData((LivingEntity)serverPlayer);
            float base = set ? 0.0f : pmg.getMana();
            pmg.setMana((float)amount + base);
            PacketDistributor.sendToPlayer((ServerPlayer)serverPlayer, (CustomPacketPayload)new SyncManaPacket(pmg), (CustomPacketPayload[])new CustomPacketPayload[0]);
        });
        String string = s = set ? "set" : "add";
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable((String)("commands.mana." + s + ".success.single"), (Object[])new Object[]{amount, ((ServerPlayer)targets.iterator().next()).getDisplayName()}), true);
        } else {
            source.sendSuccess(() -> Component.translatable((String)("commands.mana." + s + ".success.multiple"), (Object[])new Object[]{amount, targets.size()}), true);
        }
        return targets.size();
    }

    private static int getMana(CommandSourceStack source, ServerPlayer serverPlayer) {
        MagicData pmg = MagicData.getPlayerMagicData((LivingEntity)serverPlayer);
        int mana = (int)pmg.getMana();
        source.sendSuccess(() -> Component.translatable((String)"commands.mana.get.success", (Object[])new Object[]{serverPlayer.getDisplayName(), mana}), true);
        return mana;
    }
}

