/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.Message
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.IntegerArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.builder.RequiredArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.brigadier.exceptions.SimpleCommandExceptionType
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 */
package io.redspace.ironsspellbooks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainerMutable;
import io.redspace.ironsspellbooks.loot.SpellFilter;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class CreateSpellBookCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)Component.translatable((String)"commands.irons_spellbooks.create_spell_book.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
        pDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal((String)"createSpellBook").requires(p_138819_ -> p_138819_.hasPermission(2))).then(((RequiredArgumentBuilder)Commands.argument((String)"slots", (ArgumentType)IntegerArgumentType.integer((int)1, (int)20)).executes(commandContext -> CreateSpellBookCommand.crateSpellBook((CommandSourceStack)commandContext.getSource(), IntegerArgumentType.getInteger((CommandContext)commandContext, (String)"slots")))).then(Commands.literal((String)"randomize").executes(commandContext -> CreateSpellBookCommand.crateRandomSpellBook((CommandSourceStack)commandContext.getSource(), IntegerArgumentType.getInteger((CommandContext)commandContext, (String)"slots"))))));
    }

    private static int crateSpellBook(CommandSourceStack source, int slots) throws CommandSyntaxException {
        ServerPlayer serverPlayer = source.getPlayer();
        if (serverPlayer != null) {
            ItemStack itemstack = new ItemStack((ItemLike)ItemRegistry.WIMPY_SPELL_BOOK.get());
            ISpellContainer spellContainer = ISpellContainer.create(slots, true, true);
            ISpellContainer.set(itemstack, spellContainer);
            if (serverPlayer.getInventory().add(itemstack)) {
                return 1;
            }
        }
        throw ERROR_FAILED.create();
    }

    private static int crateRandomSpellBook(CommandSourceStack source, int slots) throws CommandSyntaxException {
        ServerPlayer serverPlayer = source.getPlayer();
        if (serverPlayer != null) {
            ItemStack itemstack = new ItemStack((ItemLike)ItemRegistry.WIMPY_SPELL_BOOK.get());
            ISpellContainerMutable spellContainer = ISpellContainer.create(slots, true, true).mutableCopy();
            for (int i = 0; i < slots; ++i) {
                AbstractSpell spell;
                while (!spellContainer.addSpell(spell = new SpellFilter().getRandomSpell(source.getLevel().random), source.getLevel().random.nextIntBetweenInclusive(1, spell.getMaxLevel()), false)) {
                }
            }
            ISpellContainer.set(itemstack, spellContainer.toImmutable());
            if (serverPlayer.getInventory().add(itemstack)) {
                return 1;
            }
        }
        throw ERROR_FAILED.create();
    }
}

