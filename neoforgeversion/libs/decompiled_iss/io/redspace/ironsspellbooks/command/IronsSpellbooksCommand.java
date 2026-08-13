/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.DoubleArgumentType
 *  com.mojang.brigadier.arguments.IntegerArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.builder.RequiredArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.datafixers.util.Pair
 *  net.minecraft.ChatFormatting
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.commands.arguments.EntityArgument
 *  net.minecraft.commands.arguments.ResourceKeyArgument
 *  net.minecraft.commands.arguments.coordinates.Vec3Argument
 *  net.minecraft.core.Holder
 *  net.minecraft.network.chat.ClickEvent
 *  net.minecraft.network.chat.ClickEvent$Action
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.Style
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.SimpleMenuProvider
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.inventory.ContainerLevelAccess
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import io.redspace.ironsspellbooks.api.config.SpellConfigManager;
import io.redspace.ironsspellbooks.api.item.UpgradeData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.command.LegacyConfigConverter;
import io.redspace.ironsspellbooks.command.SpellArgument;
import io.redspace.ironsspellbooks.gui.inscription_table.InscriptionTableMenu;
import io.redspace.ironsspellbooks.item.armor.UpgradeOrbType;
import io.redspace.ironsspellbooks.registries.UpgradeOrbTypeRegistry;
import io.redspace.ironsspellbooks.util.UpgradeUtils;
import java.io.File;
import java.util.Collection;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class IronsSpellbooksCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder command = (LiteralArgumentBuilder)Commands.literal((String)"ironsSpellbooks").requires(p -> p.hasPermission(3));
        IronsSpellbooksCommand.registerSummonCommandChain((LiteralArgumentBuilder<CommandSourceStack>)command);
        IronsSpellbooksCommand.registerUpgradeChain((LiteralArgumentBuilder<CommandSourceStack>)command);
        IronsSpellbooksCommand.registerInscriptionTableCommand((LiteralArgumentBuilder<CommandSourceStack>)command);
        IronsSpellbooksCommand.registerCameraShakeCommand((LiteralArgumentBuilder<CommandSourceStack>)command);
        IronsSpellbooksCommand.registerConfigCommands((LiteralArgumentBuilder<CommandSourceStack>)command);
        dispatcher.register(command);
    }

    public static void registerSummonCommandChain(LiteralArgumentBuilder<CommandSourceStack> command) {
        command.then(Commands.literal((String)"summons").then(Commands.argument((String)"target", (ArgumentType)EntityArgument.entities()).then(Commands.literal((String)"setOwner").then(Commands.argument((String)"owner", (ArgumentType)EntityArgument.entity()).executes(IronsSpellbooksCommand::summonSetOwner)))));
    }

    public static void registerUpgradeChain(LiteralArgumentBuilder<CommandSourceStack> command) {
        command.then(Commands.literal((String)"upgrade").then(((RequiredArgumentBuilder)Commands.argument((String)"type", (ArgumentType)ResourceKeyArgument.key(UpgradeOrbTypeRegistry.UPGRADE_ORB_REGISTRY_KEY)).executes(IronsSpellbooksCommand::upgradeHeldItem)).then(Commands.argument((String)"amount", (ArgumentType)IntegerArgumentType.integer((int)1)).executes(IronsSpellbooksCommand::upgradeHeldItem))));
    }

    public static void registerInscriptionTableCommand(LiteralArgumentBuilder<CommandSourceStack> command) {
        command.then(Commands.literal((String)"it").executes(source -> ((CommandSourceStack)source.getSource()).getPlayer().openMenu((MenuProvider)new SimpleMenuProvider((i, inventory, player) -> new InscriptionTableMenu(i, inventory, ContainerLevelAccess.NULL), (Component)Component.translatable((String)"block.irons_spellbooks.inscription_table"))).orElse(0)));
    }

    public static void registerCameraShakeCommand(LiteralArgumentBuilder<CommandSourceStack> command) {
        command.then(Commands.literal((String)"camera_shake").then(Commands.argument((String)"pos", (ArgumentType)Vec3Argument.vec3()).then(Commands.argument((String)"radius", (ArgumentType)DoubleArgumentType.doubleArg((double)0.0)).then(Commands.argument((String)"ticks", (ArgumentType)IntegerArgumentType.integer((int)0)).executes(IronsSpellbooksCommand::createCameraShake)))));
    }

    private static int upgradeHeldItem(CommandContext<CommandSourceStack> commandSourceStackCommandContext) {
        int amount = 1;
        try {
            amount = IntegerArgumentType.getInteger(commandSourceStackCommandContext, (String)"amount");
        }
        catch (Exception exception) {
            // empty catch block
        }
        ItemStack stack = ((CommandSourceStack)commandSourceStackCommandContext.getSource()).getPlayer().getMainHandItem();
        if (stack.isEmpty()) {
            throw new RuntimeException("empty item");
        }
        ResourceKey resourcekey = (ResourceKey)commandSourceStackCommandContext.getArgument("type", ResourceKey.class);
        String slot = UpgradeUtils.getRelevantEquipmentSlot(stack);
        for (int i = 0; i < amount; ++i) {
            UpgradeData.set(stack, UpgradeData.getUpgradeData(stack).addUpgrade(stack, (Holder<UpgradeOrbType>)((Holder)UpgradeOrbTypeRegistry.upgradeTypeRegistry(((CommandSourceStack)commandSourceStackCommandContext.getSource()).registryAccess()).getHolder(resourcekey).get()), slot));
        }
        return amount;
    }

    private static int summonSetOwner(CommandContext<CommandSourceStack> source) throws CommandSyntaxException {
        Entity owner = EntityArgument.getEntity(source, (String)"owner");
        Collection targets = EntityArgument.getEntities(source, (String)"target");
        for (Entity entity : targets) {
            SummonManager.setOwner(entity, owner);
        }
        ((CommandSourceStack)source.getSource()).sendSuccess(() -> Component.literal((String)String.format("Set %s as owner for %s entities", owner.getName().getString(), targets.size())), true);
        return targets.size();
    }

    private static int createCameraShake(CommandContext<CommandSourceStack> source) throws CommandSyntaxException {
        Vec3 pos = Vec3Argument.getVec3(source, (String)"pos");
        double radius = DoubleArgumentType.getDouble(source, (String)"radius");
        int ticks = IntegerArgumentType.getInteger(source, (String)"ticks");
        CameraShakeManager.addCameraShake(new CameraShakeData((Level)((CommandSourceStack)source.getSource()).getLevel(), ticks, pos, (float)radius));
        return ticks;
    }

    public static void registerConfigCommands(LiteralArgumentBuilder<CommandSourceStack> command) {
        command.then(Commands.literal((String)"convert_legacy_config").executes(LegacyConfigConverter::runCommand));
        command.then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal((String)"config").then(Commands.literal((String)"regenerate_example").executes(IronsSpellbooksCommand::regenerateExampleSpellConfigFile))).then(Commands.literal((String)"generate_file").then(((RequiredArgumentBuilder)Commands.argument((String)"spell", (ArgumentType)SpellArgument.spellArgument()).then(((LiteralArgumentBuilder)Commands.literal((String)"full").executes(c -> IronsSpellbooksCommand.generateSpellConfigFile((CommandContext<CommandSourceStack>)c, true, false))).then(Commands.literal((String)"override").executes(c -> IronsSpellbooksCommand.generateSpellConfigFile((CommandContext<CommandSourceStack>)c, true, true))))).then(((LiteralArgumentBuilder)Commands.literal((String)"skeleton").executes(c -> IronsSpellbooksCommand.generateSpellConfigFile((CommandContext<CommandSourceStack>)c, false, false))).then(Commands.literal((String)"override").executes(c -> IronsSpellbooksCommand.generateSpellConfigFile((CommandContext<CommandSourceStack>)c, false, true))))))).then(Commands.literal((String)"list").executes(c -> {
            SpellConfigManager.ALL_TYPES.forEach(param -> ((CommandSourceStack)c.getSource()).sendSystemMessage((Component)Component.literal((String)param.key().toString())));
            return 1;
        })));
    }

    private static int regenerateExampleSpellConfigFile(CommandContext<CommandSourceStack> context) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Pair<Boolean, File> result = SpellConfigManager.createExampleConfig(gson, SpellConfigManager.getSpellConfigDir().toPath().resolve("irons_spellbooks").resolve("example.txt").toFile());
        if (((Boolean)result.getFirst()).booleanValue()) {
            ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.translatable((String)"commands.irons_spellbooks.generic.create_file", (Object[])new Object[]{Component.literal((String)((File)result.getSecond()).getName()).withStyle(Style.EMPTY.withUnderlined(Boolean.valueOf(true)).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, ((File)result.getSecond()).getPath())))}), true);
            return 1;
        }
        ((CommandSourceStack)context.getSource()).sendFailure((Component)Component.translatable((String)"command.failed"));
        return 0;
    }

    private static int generateSpellConfigFile(CommandContext<CommandSourceStack> context, boolean full, boolean override) {
        Object spellid = (String)context.getArgument("spell", String.class);
        if (!((String)spellid).contains(":")) {
            spellid = "irons_spellbooks:" + (String)spellid;
        }
        CommandSourceStack source = (CommandSourceStack)context.getSource();
        AbstractSpell spell = SpellRegistry.getSpell((String)spellid);
        if (spell == SpellRegistry.none()) {
            source.sendFailure((Component)Component.translatable((String)"commands.irons_spellbooks.generic.unknown_spell", (Object[])new Object[]{spellid}));
            return 0;
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Pair<Boolean, File> result = SpellConfigManager.generateSpellConfigFile(gson, spell, full, override);
        if (((Boolean)result.getFirst()).booleanValue()) {
            source.sendSuccess(() -> Component.translatable((String)"commands.irons_spellbooks.generic.create_file", (Object[])new Object[]{Component.literal((String)((File)result.getSecond()).getName()).withStyle(Style.EMPTY.withUnderlined(Boolean.valueOf(true)).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, ((File)result.getSecond()).getPath())))}), true);
            return 1;
        }
        if (result.getSecond() != null) {
            source.sendFailure((Component)Component.translatable((String)"commands.irons_spellbooks.config.cant_override", (Object[])new Object[]{Component.literal((String)((File)result.getSecond()).getName()).withStyle(ChatFormatting.UNDERLINE)}));
            return 0;
        }
        source.sendFailure((Component)Component.translatable((String)"command.failed"));
        return 0;
    }
}

