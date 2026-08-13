/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.Message
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.brigadier.exceptions.SimpleCommandExceptionType
 *  net.minecraft.ChatFormatting
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.network.chat.ClickEvent
 *  net.minecraft.network.chat.ClickEvent$Action
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.neoforged.fml.ModList
 */
package io.redspace.ironsspellbooks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.fml.ModList;

public class GenerateModList {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)Component.translatable((String)"commands.irons_spellbooks.generate_mod_list.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
        if (pDispatcher.getRoot().getChild("modlist") == null) {
            pDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal((String)"modlist").requires(p_138819_ -> p_138819_.hasPermission(2))).executes(commandContext -> GenerateModList.generateModList((CommandSourceStack)commandContext.getSource())));
        } else {
            IronsSpellbooks.LOGGER.debug("modlist already loaded.. skipping");
        }
    }

    private static int generateModList(CommandSourceStack source) throws CommandSyntaxException {
        StringBuilder sb = new StringBuilder();
        sb.append("mod_id");
        sb.append(",");
        sb.append("mod_name");
        sb.append(",");
        sb.append("mod_version");
        sb.append(",");
        sb.append("mod_file");
        sb.append(",");
        sb.append("mod_url");
        sb.append(",");
        sb.append("display_url");
        sb.append(",");
        sb.append("issue_tracker_url");
        sb.append("\n");
        ModList.get().getMods().forEach(iModInfo -> {
            sb.append(iModInfo.getModId());
            sb.append(",");
            sb.append(iModInfo.getDisplayName());
            sb.append(",");
            sb.append(iModInfo.getVersion());
            sb.append(",");
            sb.append(iModInfo.getOwningFile().getFile().getFileName());
            sb.append(",");
            iModInfo.getModURL().ifPresent(sb::append);
            sb.append(",");
            iModInfo.getConfig().getConfigElement(new String[]{"displayURL"}).ifPresent(sb::append);
            sb.append(",");
            iModInfo.getOwningFile().getConfig().getConfigElement(new String[]{"issueTrackerURL"}).ifPresent(sb::append);
            sb.append("\n");
        });
        try {
            File file = new File("modlist.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(sb.toString());
            writer.close();
            MutableComponent component = Component.literal((String)file.getName()).withStyle(ChatFormatting.UNDERLINE).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file.getAbsolutePath())));
            source.sendSuccess(() -> GenerateModList.lambda$generateModList$4((Component)component), true);
        }
        catch (Exception e) {
            IronsSpellbooks.LOGGER.info(e.getMessage());
            throw ERROR_FAILED.create();
        }
        return 1;
    }

    private static /* synthetic */ Component lambda$generateModList$4(Component component) {
        return Component.translatable((String)"commands.irons_spellbooks.generate_mod_list.success", (Object[])new Object[]{component});
    }
}

