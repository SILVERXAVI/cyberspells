/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.tree.LiteralCommandNode
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.commands.arguments.EntityArgument
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.LivingEntity
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class ClearCooldownCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode command = dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal((String)"clearCooldowns").requires(p -> p.hasPermission(2))).executes(context -> ClearCooldownCommand.clearCooldowns((CommandSourceStack)context.getSource(), null))).then(Commands.literal((String)"all").executes(context -> ClearCooldownCommand.clearCooldowns((CommandSourceStack)context.getSource(), null)))).then(Commands.literal((String)"player").then(Commands.argument((String)"targets", (ArgumentType)EntityArgument.players()).executes(context -> ClearCooldownCommand.clearCooldowns((CommandSourceStack)context.getSource(), EntityArgument.getPlayers((CommandContext)context, (String)"targets"))))));
    }

    private static int clearCooldowns(CommandSourceStack source, @Nullable Collection<ServerPlayer> targets) {
        if (targets != null && !targets.isEmpty()) {
            targets.forEach(serverPlayer -> {
                MagicData magicData = MagicData.getPlayerMagicData((LivingEntity)serverPlayer);
                magicData.getPlayerCooldowns().clearCooldowns();
                magicData.getPlayerCooldowns().syncToPlayer((ServerPlayer)serverPlayer);
            });
            if (!targets.isEmpty()) {
                source.sendSuccess(() -> Component.translatable((String)"commands.clearCooldown.success"), true);
            }
            return targets.size();
        }
        source.getServer().getAllLevels().forEach(level -> level.getPlayers(player -> true).forEach(serverPlayer -> {
            MagicData magicData = MagicData.getPlayerMagicData((LivingEntity)serverPlayer);
            magicData.getPlayerCooldowns().clearCooldowns();
            magicData.getPlayerCooldowns().syncToPlayer((ServerPlayer)serverPlayer);
        }));
        source.sendSuccess(() -> Component.translatable((String)"commands.clearCooldown.success"), true);
        return 1;
    }
}

