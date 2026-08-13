/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.tree.LiteralCommandNode
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 */
package io.redspace.ironsspellbooks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.command.SpellArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class LearnCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode command = dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal((String)"learnSpell").requires(p -> p.hasPermission(2))).then(Commands.literal((String)"forget_all").executes(context -> LearnCommand.forgetAll((CommandSourceStack)context.getSource())))).then(Commands.literal((String)"learn_all").executes(context -> LearnCommand.learnAll((CommandSourceStack)context.getSource())))).then(Commands.literal((String)"learn").then(Commands.argument((String)"spell", (ArgumentType)SpellArgument.spellArgument()).executes(commandContext -> LearnCommand.learn((CommandSourceStack)commandContext.getSource(), (String)commandContext.getArgument("spell", String.class))))));
    }

    private static int forgetAll(CommandSourceStack source) {
        MagicData.getPlayerMagicData((LivingEntity)source.getPlayer()).getSyncedData().forgetAllSpells();
        return 1;
    }

    private static int learnAll(CommandSourceStack source) {
        int i = 0;
        for (AbstractSpell spell : SpellRegistry.getEnabledSpells()) {
            if (!spell.requiresLearning() || spell.isLearned((Player)source.getPlayer())) continue;
            MagicData.getPlayerMagicData((LivingEntity)source.getPlayer()).getSyncedData().learnSpell(spell, false);
        }
        MagicData.getPlayerMagicData((LivingEntity)source.getPlayer()).getSyncedData().doSync();
        return i;
    }

    private static int learn(CommandSourceStack source, String spellId) {
        if (!((String)spellId).contains(":")) {
            spellId = "irons_spellbooks:" + (String)spellId;
        }
        AbstractSpell spell = SpellRegistry.getSpell((String)spellId);
        MagicData.getPlayerMagicData((LivingEntity)source.getPlayer()).getSyncedData().learnSpell(spell);
        return 1;
    }
}

