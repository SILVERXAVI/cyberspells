/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  net.minecraft.ChatFormatting
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.commands.arguments.EntityArgument
 *  net.minecraft.network.chat.ClickEvent
 *  net.minecraft.network.chat.ClickEvent$Action
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.neoforged.neoforge.server.command.EnumArgument
 */
package io.redspace.ironsspellbooks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.capabilities.magic.PocketDimensionManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.command.CreateRecipeCompatGenerator;
import io.redspace.ironsspellbooks.item.ChronicleItem;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.server.command.EnumArgument;

public class IronsDebugCommand {
    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
        pDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal((String)"ironsDebug").requires(p_138819_ -> p_138819_.hasPermission(2))).then(Commands.argument((String)"dataType", (ArgumentType)EnumArgument.enumArgument(IronsDebugCommandTypes.class)).executes(commandContext -> IronsDebugCommand.getDataForType((CommandSourceStack)commandContext.getSource(), (IronsDebugCommandTypes)((Object)((Object)commandContext.getArgument("dataType", IronsDebugCommandTypes.class))))))).then(Commands.literal((String)"spellCount").executes(commandContext -> {
            int i = SpellRegistry.getEnabledSpells().size();
            ((CommandSourceStack)commandContext.getSource()).sendSuccess(() -> Component.literal((String)String.valueOf(i)), true);
            return i;
        }))).then(Commands.literal((String)"items").executes(commandContext -> {
            ServerPlayer patt0$temp = ((CommandSourceStack)commandContext.getSource()).getPlayer();
            if (patt0$temp instanceof ServerPlayer) {
                ServerPlayer player = patt0$temp;
                player.getInventory().add(new ItemStack((ItemLike)ItemRegistry.DEV_CROWN.get()));
                player.getInventory().add(new ItemStack((ItemLike)ItemRegistry.NETHERITE_SPELL_BOOK.get()));
                player.getInventory().add(new ItemStack((ItemLike)ItemRegistry.INSCRIPTION_TABLE_BLOCK_ITEM.get()));
            }
            return 1;
        }))).then(Commands.literal((String)"pocketDimension").then(Commands.literal((String)"clearId").executes(commandContext -> {
            ServerPlayer patt0$temp = ((CommandSourceStack)commandContext.getSource()).getPlayer();
            if (patt0$temp instanceof ServerPlayer) {
                ServerPlayer player = patt0$temp;
                PocketDimensionManager.INSTANCE.remove(player.getUUID());
            }
            return 1;
        })))).then(Commands.literal((String)"rarityTest").executes(commandContext -> {
            SpellRarity.rarityTest();
            return 1;
        }))).then(Commands.literal((String)"claimSummon").then(Commands.argument((String)"target", (ArgumentType)EntityArgument.entity()).executes(commandContext -> {
            SummonManager.setOwner(EntityArgument.getEntity((CommandContext)commandContext, (String)"target"), ((CommandSourceStack)commandContext.getSource()).getEntityOrException());
            return 1;
        })))).then(Commands.literal((String)"generateCreateRecipeCompat").executes(CreateRecipeCompatGenerator::run))).then(Commands.literal((String)"clear_chronicle_cache").executes(cmd -> {
            ((ChronicleItem)ItemRegistry.THE_CHRONICLE.get()).clearCache();
            return 1;
        })));
    }

    public static int getDataForType(CommandSourceStack source, IronsDebugCommandTypes ironsDebugCommandTypes) {
        switch (ironsDebugCommandTypes.ordinal()) {
            case 0: {
                IronsDebugCommand.getReacstingData(source);
            }
        }
        return 1;
    }

    public static void getReacstingData(CommandSourceStack source) {
        ServerPlayer serverPlayer = source.getPlayer();
        MagicData magicData = MagicData.getPlayerMagicData((LivingEntity)serverPlayer);
        IronsDebugCommand.writeResults(source, magicData.getPlayerRecasts().toString());
    }

    private static void writeResults(CommandSourceStack source, String results) {
        try {
            File file = new File("irons_debug.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(results);
            writer.close();
            MutableComponent component = Component.literal((String)file.getName()).withStyle(ChatFormatting.UNDERLINE).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file.getAbsolutePath())));
            source.sendSuccess(() -> IronsDebugCommand.lambda$writeResults$10((Component)component), true);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static /* synthetic */ Component lambda$writeResults$10(Component component) {
        return Component.translatable((String)"commands.irons_spellbooks.irons_debug_command.success", (Object[])new Object[]{component});
    }

    public static enum IronsDebugCommandTypes {
        RECASTING;

    }
}

