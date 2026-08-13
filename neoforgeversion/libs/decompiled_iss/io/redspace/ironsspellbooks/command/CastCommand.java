/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.IntegerArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.builder.RequiredArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.tree.LiteralCommandNode
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.commands.arguments.EntityArgument
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 */
package io.redspace.ironsspellbooks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.command.SpellArgument;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CastCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode command = dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal((String)"cast").requires(p -> p.hasPermission(2))).then(Commands.argument((String)"casters", (ArgumentType)EntityArgument.entities()).then(((RequiredArgumentBuilder)Commands.argument((String)"spell", (ArgumentType)SpellArgument.spellArgument()).executes(context -> CastCommand.castSpell((CommandSourceStack)context.getSource(), EntityArgument.getEntities((CommandContext)context, (String)"casters"), (String)context.getArgument("spell", String.class)))).then(Commands.argument((String)"level", (ArgumentType)IntegerArgumentType.integer((int)1)).executes(context -> CastCommand.castSpell((CommandSourceStack)context.getSource(), EntityArgument.getEntities((CommandContext)context, (String)"casters"), (String)context.getArgument("spell", String.class), IntegerArgumentType.getInteger((CommandContext)context, (String)"level")))))));
    }

    private static int castSpell(CommandSourceStack source, Collection<? extends Entity> targets, String spellId) {
        return CastCommand.castSpell(source, targets, spellId, 1);
    }

    private static int castSpell(CommandSourceStack source, Collection<? extends Entity> targets, String spellId, int spellLevel) {
        if (!((String)spellId).contains(":")) {
            spellId = "irons_spellbooks:" + (String)spellId;
        }
        AbstractSpell spell = SpellRegistry.getSpell((String)spellId);
        for (Entity entity : targets) {
            if (entity instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)entity;
                spell.attemptInitiateCast(ItemStack.EMPTY, spellLevel, (Level)source.getLevel(), (Player)serverPlayer, CastSource.COMMAND, false, "command");
                continue;
            }
            if (entity instanceof IMagicEntity) {
                IMagicEntity castingMob = (IMagicEntity)entity;
                castingMob.initiateCastSpell(spell, spellLevel);
                continue;
            }
            if (!(entity instanceof LivingEntity)) continue;
            LivingEntity livingEntity = (LivingEntity)entity;
            MagicData magicData = MagicData.getPlayerMagicData(livingEntity);
            if (!spell.checkPreCastConditions((Level)source.getLevel(), spellLevel, livingEntity, magicData)) {
                return 0;
            }
            spell.onCast((Level)source.getLevel(), spellLevel, livingEntity, CastSource.COMMAND, magicData);
            spell.onServerCastComplete((Level)source.getLevel(), spellLevel, livingEntity, magicData, false);
        }
        return 1;
    }
}

