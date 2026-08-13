/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.Message
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.brigadier.exceptions.SimpleCommandExceptionType
 *  net.minecraft.commands.CommandBuildContext
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.commands.arguments.blocks.BlockPredicateArgument
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.state.pattern.BlockInWorld
 */
package io.redspace.ironsspellbooks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import java.util.function.Predicate;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class LocateBlock {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)Component.translatable((String)"commands.irons_spellbooks.create_spell_book.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext pContext) {
        pDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal((String)"locateBlock").requires(p_138819_ -> p_138819_.hasPermission(2))).then(Commands.argument((String)"block", (ArgumentType)BlockPredicateArgument.blockPredicate((CommandBuildContext)pContext)).executes(commandContext -> LocateBlock.locateBlock((CommandSourceStack)commandContext.getSource(), BlockPredicateArgument.getBlockPredicate((CommandContext)commandContext, (String)"block")))));
    }

    private static int locateBlock(CommandSourceStack source, Predicate<BlockInWorld> predicate) throws CommandSyntaxException {
        BlockPos startingPosition = source.getPlayer().blockPosition();
        ServerLevel level = source.getLevel();
        int xFrom = startingPosition.getX() - 200;
        int xTo = startingPosition.getX() + 200;
        int yFrom = -64;
        int yTo = startingPosition.getY();
        int zFrom = startingPosition.getZ() - 200;
        int zTo = startingPosition.getZ() + 200;
        IronsSpellbooks.LOGGER.debug("Starting locateBlock from: {}, xFrom:{} xTo:{} yFrom:{} yTo:{} zFrom:{} zTo:{}", new Object[]{startingPosition, xFrom, xTo, yFrom, yTo, zFrom, zTo});
        for (int i = yFrom; i < yTo; ++i) {
            for (int j = xFrom; j < xTo; ++j) {
                for (int k = zFrom; k < zTo; ++k) {
                    BlockPos blockPos = new BlockPos(j, i, k);
                    if (!predicate.test(new BlockInWorld((LevelReader)level, blockPos, true))) continue;
                    IronsSpellbooks.LOGGER.debug("Located x:{} y:{} z:{}", new Object[]{j, i, k});
                }
            }
        }
        IronsSpellbooks.LOGGER.debug("Finished locateBlock");
        return 1;
    }
}

