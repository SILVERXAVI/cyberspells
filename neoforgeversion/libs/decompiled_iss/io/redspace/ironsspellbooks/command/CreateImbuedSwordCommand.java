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
 *  com.mojang.brigadier.suggestion.SuggestionProvider
 *  com.mojang.brigadier.suggestion.SuggestionsBuilder
 *  net.minecraft.commands.CommandBuildContext
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.commands.SharedSuggestionProvider
 *  net.minecraft.commands.arguments.item.ItemArgument
 *  net.minecraft.commands.arguments.item.ItemInput
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.SwordItem
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
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainerMutable;
import io.redspace.ironsspellbooks.command.SpellArgument;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.ItemLike;

public class CreateImbuedSwordCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)Component.translatable((String)"commands.irons_spellbooks.create_imbued_sword.failed"));
    private static final SuggestionProvider<CommandSourceStack> SWORD_SUGGESTIONS = (context, builder) -> {
        Set resources = BuiltInRegistries.ITEM.entrySet().stream().filter(e -> e.getValue() instanceof SwordItem).map(e -> ((ResourceKey)e.getKey()).location()).collect(Collectors.toSet());
        return SharedSuggestionProvider.suggestResource(resources, (SuggestionsBuilder)builder);
    };

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext context) {
        pDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal((String)"createImbuedSword").requires(commandSourceStack -> commandSourceStack.hasPermission(2))).then(Commands.argument((String)"item", (ArgumentType)ItemArgument.item((CommandBuildContext)context)).suggests(SWORD_SUGGESTIONS).then(Commands.argument((String)"spell", (ArgumentType)SpellArgument.spellArgument()).then(Commands.argument((String)"level", (ArgumentType)IntegerArgumentType.integer((int)1)).executes(ctx -> CreateImbuedSwordCommand.createImbuedSword((CommandSourceStack)ctx.getSource(), (ItemInput)ctx.getArgument("item", ItemInput.class), (String)ctx.getArgument("spell", String.class), IntegerArgumentType.getInteger((CommandContext)ctx, (String)"level")))))));
    }

    private static int createImbuedSword(CommandSourceStack source, ItemInput itemInput, String spell, int spellLevel) throws CommandSyntaxException {
        ItemStack itemstack;
        Item item;
        AbstractSpell abstractSpell;
        if (!((String)spell).contains(":")) {
            spell = "irons_spellbooks:" + (String)spell;
        }
        if (spellLevel > (abstractSpell = (AbstractSpell)SpellRegistry.REGISTRY.get(ResourceLocation.parse((String)spell))).getMaxLevel()) {
            throw new SimpleCommandExceptionType((Message)Component.translatable((String)"commands.irons_spellbooks.create_spell.failed_max_level", (Object[])new Object[]{abstractSpell.getSpellName(), abstractSpell.getMaxLevel()})).create();
        }
        ServerPlayer serverPlayer = source.getPlayer();
        if (serverPlayer != null && (item = (itemstack = new ItemStack((ItemLike)itemInput.getItem())).getItem()) instanceof SwordItem) {
            SwordItem swordItem = (SwordItem)item;
            ISpellContainerMutable spellContainer = ISpellContainer.create(1, true, false).mutableCopy();
            spellContainer.addSpell(abstractSpell, spellLevel, false);
            ISpellContainer.set(itemstack, spellContainer.toImmutable());
            if (serverPlayer.getInventory().add(itemstack)) {
                return 1;
            }
        }
        throw ERROR_FAILED.create();
    }
}

