/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.tree.LiteralCommandNode
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.LivingEntity
 */
package io.redspace.ironsspellbooks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.gui.overlays.SpellSelection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public class ClearSpellSelectionCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode command = dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal((String)"clearSpellSelection").requires(p -> p.hasPermission(2))).executes(context -> ClearSpellSelectionCommand.clearCooldowns((CommandSourceStack)context.getSource())));
    }

    private static int clearCooldowns(CommandSourceStack source) {
        MagicData.getPlayerMagicData((LivingEntity)source.getPlayer()).getSyncedData().setSpellSelection(new SpellSelection());
        source.sendSuccess(() -> Component.literal((String)String.format("Spell selection cleared for %s", source.getPlayer().toString())), true);
        return 1;
    }
}

