/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  net.minecraft.commands.CommandBuildContext
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.SimpleMenuProvider
 *  net.minecraft.world.inventory.ContainerLevelAccess
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.loading.FMLLoader
 *  net.neoforged.neoforge.event.RegisterCommandsEvent
 */
package io.redspace.ironsspellbooks.registries;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.redspace.ironsspellbooks.command.CastCommand;
import io.redspace.ironsspellbooks.command.ClearCooldownCommand;
import io.redspace.ironsspellbooks.command.ClearRecastsCommand;
import io.redspace.ironsspellbooks.command.ClearSpellSelectionCommand;
import io.redspace.ironsspellbooks.command.CreateDebugWizardCommand;
import io.redspace.ironsspellbooks.command.CreateImbuedSwordCommand;
import io.redspace.ironsspellbooks.command.CreateScrollCommand;
import io.redspace.ironsspellbooks.command.CreateSpellBookCommand;
import io.redspace.ironsspellbooks.command.GenerateModList;
import io.redspace.ironsspellbooks.command.GenerateSiteData;
import io.redspace.ironsspellbooks.command.IronsDebugCommand;
import io.redspace.ironsspellbooks.command.IronsSpellbooksCommand;
import io.redspace.ironsspellbooks.command.LearnCommand;
import io.redspace.ironsspellbooks.command.ManaCommand;
import io.redspace.ironsspellbooks.gui.inscription_table.InscriptionTableMenu;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber
public class CommandRegistry {
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        CommandDispatcher commandDispatcher = event.getDispatcher();
        CommandBuildContext commandBuildContext = event.getBuildContext();
        CreateScrollCommand.register((CommandDispatcher<CommandSourceStack>)commandDispatcher);
        CreateSpellBookCommand.register((CommandDispatcher<CommandSourceStack>)commandDispatcher);
        CreateImbuedSwordCommand.register((CommandDispatcher<CommandSourceStack>)commandDispatcher, commandBuildContext);
        CreateDebugWizardCommand.register((CommandDispatcher<CommandSourceStack>)commandDispatcher);
        CastCommand.register((CommandDispatcher<CommandSourceStack>)commandDispatcher);
        ManaCommand.register((CommandDispatcher<CommandSourceStack>)commandDispatcher);
        GenerateModList.register((CommandDispatcher<CommandSourceStack>)commandDispatcher);
        LearnCommand.register((CommandDispatcher<CommandSourceStack>)commandDispatcher);
        ClearCooldownCommand.register((CommandDispatcher<CommandSourceStack>)commandDispatcher);
        ClearRecastsCommand.register((CommandDispatcher<CommandSourceStack>)commandDispatcher);
        IronsSpellbooksCommand.register((CommandDispatcher<CommandSourceStack>)commandDispatcher);
        if (!FMLLoader.isProduction()) {
            ClearSpellSelectionCommand.register((CommandDispatcher<CommandSourceStack>)commandDispatcher);
            IronsDebugCommand.register((CommandDispatcher<CommandSourceStack>)commandDispatcher);
            GenerateSiteData.register((CommandDispatcher<CommandSourceStack>)commandDispatcher);
            commandDispatcher.register((LiteralArgumentBuilder)LiteralArgumentBuilder.literal((String)"it").executes(source -> ((CommandSourceStack)source.getSource()).getPlayer().openMenu((MenuProvider)new SimpleMenuProvider((i, inventory, player) -> new InscriptionTableMenu(i, inventory, ContainerLevelAccess.NULL), (Component)Component.translatable((String)"block.irons_spellbooks.inscription_table"))).orElse(0)));
        }
    }
}

