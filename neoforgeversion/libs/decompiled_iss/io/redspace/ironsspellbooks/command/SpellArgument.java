/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.StringReader
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.brigadier.suggestion.Suggestions
 *  com.mojang.brigadier.suggestion.SuggestionsBuilder
 *  net.minecraft.commands.SharedSuggestionProvider
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 */
package io.redspace.ironsspellbooks.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class SpellArgument
implements ArgumentType<String> {
    private static final List<String> EXAMPLES = Arrays.asList("angel_wings", "electrocute", "spell_addon_mod:spell_name");

    public static SpellArgument spellArgument() {
        return new SpellArgument();
    }

    public String parse(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();
        while (reader.canRead() && ResourceLocation.isAllowedInResourceLocation((char)reader.peek())) {
            reader.skip();
        }
        return reader.getString().substring(i, reader.getCursor());
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        List<String> registeredSpells = SpellRegistry.REGISTRY.entrySet().stream().map(entry -> {
            if (((ResourceKey)entry.getKey()).location().getNamespace().equals("irons_spellbooks")) {
                return ((ResourceKey)entry.getKey()).location().getPath();
            }
            return ((ResourceKey)entry.getKey()).location().toString();
        }).sorted((s1, s2) -> {
            if (s1.contains(":") && s2.contains(":")) {
                return s1.compareTo((String)s2);
            }
            if (s1.contains(":")) {
                return 1;
            }
            if (s2.contains(":")) {
                return -1;
            }
            return s1.compareTo((String)s2);
        }).toList();
        return SharedSuggestionProvider.suggest(registeredSpells, (SuggestionsBuilder)builder);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}

