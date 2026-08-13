/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.Message
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.BoolArgumentType
 *  com.mojang.brigadier.arguments.IntegerArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.brigadier.exceptions.SimpleCommandExceptionType
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 */
package io.redspace.ironsspellbooks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.command.SpellArgument;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.debug_wizard.DebugWizard;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class CreateDebugWizardCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)Component.translatable((String)"commands.irons_spellbooks.create_debug_wizard.failed"));
    private static final SimpleCommandExceptionType ERROR_FAILED_MAX_LEVEL = new SimpleCommandExceptionType((Message)Component.translatable((String)"commands.irons_spellbooks.create_debug_wizard.failed_max_level"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
        pDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal((String)"createDebugWizard").requires(commandSourceStack -> commandSourceStack.hasPermission(2))).then(Commands.argument((String)"spell", (ArgumentType)SpellArgument.spellArgument()).then(Commands.argument((String)"spellLevel", (ArgumentType)IntegerArgumentType.integer((int)1)).then(Commands.argument((String)"targetsPlayer", (ArgumentType)BoolArgumentType.bool()).then(Commands.argument((String)"cancelAfterTicks", (ArgumentType)IntegerArgumentType.integer((int)0)).executes(ctx -> CreateDebugWizardCommand.createDebugWizard((CommandSourceStack)ctx.getSource(), (String)ctx.getArgument("spell", String.class), IntegerArgumentType.getInteger((CommandContext)ctx, (String)"spellLevel"), BoolArgumentType.getBool((CommandContext)ctx, (String)"targetsPlayer"), IntegerArgumentType.getInteger((CommandContext)ctx, (String)"cancelAfterTicks"))))))));
    }

    private static int createDebugWizard(CommandSourceStack source, String spellId, int spellLevel, boolean targetsPlayer, int cancelAfterTicks) throws CommandSyntaxException {
        AbstractSpell spell;
        if (!((String)spellId).contains(":")) {
            spellId = "irons_spellbooks:" + (String)spellId;
        }
        if (spellLevel > (spell = SpellRegistry.getSpell((String)spellId)).getMaxLevel()) {
            throw new SimpleCommandExceptionType((Message)Component.translatable((String)"commands.irons_spellbooks.create_spell.failed_max_level", (Object[])new Object[]{spell.getSpellName(), spell.getMaxLevel()})).create();
        }
        ServerPlayer serverPlayer = source.getPlayer();
        if (serverPlayer != null) {
            DebugWizard debugWizard = new DebugWizard((EntityType<? extends AbstractSpellCastingMob>)((EntityType)EntityRegistry.DEBUG_WIZARD.get()), serverPlayer.level, spell, spellLevel, targetsPlayer, cancelAfterTicks);
            debugWizard.setPos(serverPlayer.position());
            if (serverPlayer.level.addFreshEntity((Entity)debugWizard)) {
                return 1;
            }
        }
        throw ERROR_FAILED.create();
    }
}

