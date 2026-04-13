/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.BoolArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.suggestion.SuggestionProvider
 *  com.mojang.brigadier.suggestion.SuggestionsBuilder
 *  net.minecraft.commands.CommandBuildContext
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.commands.SharedSuggestionProvider
 *  net.minecraft.commands.arguments.EntityArgument
 *  net.minecraft.commands.arguments.item.ItemArgument
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 */
package com.perigrine3.createcybernetics.command.custom;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.perigrine3.createcybernetics.ConfigValues;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public final class CyberneticsCommand {
    private static final String KEY_KEEP_QUERY = "command.createcybernetics.keep_cyberware.query";
    private static final String KEY_KEEP_SET = "command.createcybernetics.keep_cyberware.set";
    private static final String KEY_WRONG_ITEM = "commands.createcybernetics.implants.wrong_item";
    private static final String KEY_NO_CYBERWARE = "commands.createcybernetics.implants.no_cyberware";
    private static final String KEY_INSTALL_FAIL = "commands.createcybernetics.implants.install_fail";
    private static final String KEY_INSTALL_OK = "commands.createcybernetics.implants.install_success";
    private static final String KEY_REMOVE_FAIL = "commands.createcybernetics.implants.remove_fail";
    private static final String KEY_REMOVE_OK = "commands.createcybernetics.implants.remove_success";
    private static final String KEY_CLEAR_OK = "commands.createcybernetics.implants.clear_success";
    private static final String PKEY_ENERGY_DEBUG = "cc_energy_debug_enabled";
    private static final String KEY_ENERGY_DEBUG_SET = "commands.createcybernetics.energy_debug.set";
    private static final SuggestionProvider<CommandSourceStack> CYBERWARE_ITEM_SUGGESTIONS = (context, builder) -> SharedSuggestionProvider.suggestResource(BuiltInRegistries.ITEM.entrySet().stream().filter(e -> "createcybernetics".equals(((ResourceKey)e.getKey()).location().getNamespace())).filter(e -> e.getValue() instanceof ICyberwareItem).map(e -> ((ResourceKey)e.getKey()).location()).sorted(), (SuggestionsBuilder)builder);

    private CyberneticsCommand() {
    }

    private static boolean isCreateCyberneticsCyberwareItem(Item item) {
        if (!(item instanceof ICyberwareItem)) {
            return false;
        }
        ResourceLocation key = BuiltInRegistries.ITEM.getKey((Object)item);
        return key != null && "createcybernetics".equals(key.getNamespace());
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext ctx) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal((String)"cybernetics").requires(src -> src.hasPermission(2))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal((String)"implants").then(Commands.literal((String)"install").then(Commands.argument((String)"player", (ArgumentType)EntityArgument.player()).then(Commands.argument((String)"item", (ArgumentType)ItemArgument.item((CommandBuildContext)ctx)).suggests(CYBERWARE_ITEM_SUGGESTIONS).executes(c -> {
            ServerPlayer target = EntityArgument.getPlayer((CommandContext)c, (String)"player");
            Item item = ItemArgument.getItem((CommandContext)c, (String)"item").getItem();
            return CyberneticsCommand.install((CommandSourceStack)c.getSource(), target, item);
        }))))).then(Commands.literal((String)"remove").then(Commands.argument((String)"player", (ArgumentType)EntityArgument.player()).then(Commands.argument((String)"item", (ArgumentType)ItemArgument.item((CommandBuildContext)ctx)).suggests(CYBERWARE_ITEM_SUGGESTIONS).executes(c -> {
            ServerPlayer target = EntityArgument.getPlayer((CommandContext)c, (String)"player");
            Item item = ItemArgument.getItem((CommandContext)c, (String)"item").getItem();
            return CyberneticsCommand.remove((CommandSourceStack)c.getSource(), target, item);
        }))))).then(Commands.literal((String)"list").then(Commands.argument((String)"player", (ArgumentType)EntityArgument.player()).executes(c -> {
            ServerPlayer target = EntityArgument.getPlayer((CommandContext)c, (String)"player");
            return CyberneticsCommand.list((CommandSourceStack)c.getSource(), target);
        })))).then(Commands.literal((String)"clear").then(Commands.argument((String)"player", (ArgumentType)EntityArgument.player()).executes(c -> {
            ServerPlayer target = EntityArgument.getPlayer((CommandContext)c, (String)"player");
            return CyberneticsCommand.clear((CommandSourceStack)c.getSource(), target);
        }))))).then(Commands.literal((String)"keepCyberware").then(Commands.argument((String)"value", (ArgumentType)BoolArgumentType.bool()).executes(c -> {
            boolean value;
            ConfigValues.KEEP_CYBERWARE = value = BoolArgumentType.getBool((CommandContext)c, (String)"value");
            MutableComponent state = Component.translatable((String)(value ? "options.on" : "options.off"));
            ((CommandSourceStack)c.getSource()).sendSuccess(() -> CyberneticsCommand.lambda$register$9((Component)state), true);
            return 1;
        })))).then(Commands.literal((String)"energyDebug").then(Commands.argument((String)"value", (ArgumentType)BoolArgumentType.bool()).executes(c -> {
            ServerPlayer target = ((CommandSourceStack)c.getSource()).getPlayerOrException();
            boolean value = BoolArgumentType.getBool((CommandContext)c, (String)"value");
            CyberneticsCommand.setEnergyDebug(target, value);
            MutableComponent state = Component.translatable((String)(value ? "options.on" : "options.off"));
            ((CommandSourceStack)c.getSource()).sendSuccess(() -> CyberneticsCommand.lambda$register$11((Component)state), false);
            return 1;
        }))));
    }

    private static int install(CommandSourceStack src, ServerPlayer target, Item item) {
        if (!CyberneticsCommand.isCreateCyberneticsCyberwareItem(item)) {
            src.sendFailure((Component)Component.translatable((String)KEY_WRONG_ITEM));
            return 0;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)target.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            src.sendFailure((Component)Component.translatable((String)KEY_NO_CYBERWARE));
            return 0;
        }
        ItemStack stack = new ItemStack((ItemLike)item);
        boolean ok = data.commandInstall((Player)target, stack);
        if (!ok) {
            src.sendFailure((Component)Component.translatable((String)KEY_INSTALL_FAIL, (Object[])new Object[]{stack.getHoverName()}));
            return 0;
        }
        target.syncData(ModAttachments.CYBERWARE);
        src.sendSuccess(() -> Component.translatable((String)KEY_INSTALL_OK, (Object[])new Object[]{stack.getHoverName(), target.getDisplayName()}), false);
        return 1;
    }

    private static int remove(CommandSourceStack src, ServerPlayer target, Item item) {
        if (!CyberneticsCommand.isCreateCyberneticsCyberwareItem(item)) {
            src.sendFailure((Component)Component.translatable((String)KEY_WRONG_ITEM));
            return 0;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)target.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            src.sendFailure((Component)Component.translatable((String)KEY_NO_CYBERWARE));
            return 0;
        }
        boolean ok = data.commandRemove((Player)target, item);
        if (!ok) {
            src.sendFailure((Component)Component.translatable((String)KEY_REMOVE_FAIL, (Object[])new Object[]{target.getDisplayName()}));
            return 0;
        }
        target.syncData(ModAttachments.CYBERWARE);
        Component itemName = item.getDescription();
        src.sendSuccess(() -> Component.translatable((String)KEY_REMOVE_OK, (Object[])new Object[]{itemName, target.getDisplayName()}), false);
        return 1;
    }

    private static int list(CommandSourceStack src, ServerPlayer target) {
        PlayerCyberwareData data = (PlayerCyberwareData)target.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            src.sendFailure((Component)Component.translatable((String)KEY_NO_CYBERWARE));
            return 0;
        }
        Component out = data.commandListComponent();
        src.sendSuccess(() -> out, false);
        return 1;
    }

    private static int clear(CommandSourceStack src, ServerPlayer target) {
        PlayerCyberwareData data = (PlayerCyberwareData)target.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            src.sendFailure((Component)Component.translatable((String)KEY_NO_CYBERWARE));
            return 0;
        }
        data.clear();
        data.resetToDefaultOrgans();
        target.syncData(ModAttachments.CYBERWARE);
        src.sendSuccess(() -> Component.translatable((String)KEY_CLEAR_OK, (Object[])new Object[]{target.getDisplayName()}), false);
        return 1;
    }

    private static void setEnergyDebug(ServerPlayer player, boolean value) {
        player.getPersistentData().putBoolean(PKEY_ENERGY_DEBUG, value);
    }

    private static /* synthetic */ Component lambda$register$11(Component state) {
        return Component.translatable((String)KEY_ENERGY_DEBUG_SET, (Object[])new Object[]{state});
    }

    private static /* synthetic */ Component lambda$register$9(Component state) {
        return Component.translatable((String)KEY_KEEP_SET, (Object[])new Object[]{state});
    }
}

