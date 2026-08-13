/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.Message
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.IntegerArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.brigadier.exceptions.SimpleCommandExceptionType
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
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
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.command.SpellArgument;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class CreateScrollCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)Component.translatable((String)"commands.irons_spellbooks.create_scroll.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
        pDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal((String)"createScroll").requires(p_138819_ -> p_138819_.hasPermission(2))).then(Commands.argument((String)"spell", (ArgumentType)SpellArgument.spellArgument()).then(Commands.argument((String)"level", (ArgumentType)IntegerArgumentType.integer((int)1)).executes(commandContext -> CreateScrollCommand.createScroll((CommandSourceStack)commandContext.getSource(), (String)commandContext.getArgument("spell", String.class), IntegerArgumentType.getInteger((CommandContext)commandContext, (String)"level"))))));
    }

    private static int createScroll(CommandSourceStack source, String spell, int spellLevel) throws CommandSyntaxException {
        AbstractSpell abstractSpell;
        if (!((String)spell).contains(":")) {
            spell = "irons_spellbooks:" + (String)spell;
        }
        if ((abstractSpell = (AbstractSpell)SpellRegistry.REGISTRY.get(ResourceLocation.parse((String)spell))) == null || abstractSpell == SpellRegistry.none()) {
            throw ERROR_FAILED.create();
        }
        if (spellLevel > abstractSpell.getMaxLevel()) {
            throw new SimpleCommandExceptionType((Message)Component.translatable((String)"commands.irons_spellbooks.create_spell.failed_max_level", (Object[])new Object[]{abstractSpell.getSpellName(), abstractSpell.getMaxLevel()})).create();
        }
        ServerPlayer serverPlayer = source.getPlayer();
        if (serverPlayer != null) {
            ItemStack itemStack = new ItemStack((ItemLike)ItemRegistry.SCROLL.get());
            ISpellContainer.createScrollContainer(abstractSpell, spellLevel, itemStack);
            if (serverPlayer.getInventory().add(itemStack)) {
                return 1;
            }
        }
        throw ERROR_FAILED.create();
    }
}

