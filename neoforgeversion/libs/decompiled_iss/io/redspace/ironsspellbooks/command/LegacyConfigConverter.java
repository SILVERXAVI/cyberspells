/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.electronwill.nightconfig.core.Config
 *  com.electronwill.nightconfig.core.file.FileNotFoundAction
 *  com.electronwill.nightconfig.toml.TomlParser
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.world.level.storage.LevelResource
 *  net.neoforged.fml.loading.FMLPaths
 */
package io.redspace.ironsspellbooks.command;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileNotFoundAction;
import com.electronwill.nightconfig.toml.TomlParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.config.SpellConfigManager;
import io.redspace.ironsspellbooks.api.config.SpellConfigParameter;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.fml.loading.FMLPaths;

public class LegacyConfigConverter {
    public static int runCommand(CommandContext<CommandSourceStack> commandSourceStackCommandContext) {
        String path;
        try {
            path = LegacyConfigConverter.run(commandSourceStackCommandContext);
        }
        catch (RuntimeException e) {
            ((CommandSourceStack)commandSourceStackCommandContext.getSource()).sendFailure((Component)Component.literal((String)("Failed to execute conversion, aborting: " + e.getMessage() + ". See log for full details.")));
            IronsSpellbooks.LOGGER.error("[Config Converter] Failed to execute: {}", (Object)e.toString());
            return 0;
        }
        ((CommandSourceStack)commandSourceStackCommandContext.getSource()).sendSuccess(() -> Component.literal((String)("Saved to " + path)), false);
        return 1;
    }

    private static String run(CommandContext<CommandSourceStack> commandSourceStackCommandContext) throws RuntimeException {
        CommandSourceStack commandSourceStack = (CommandSourceStack)commandSourceStackCommandContext.getSource();
        MinecraftServer server = commandSourceStack.getServer();
        String filename = "irons_spellbooks-server-1.toml.bak";
        File worldConfigFile = server.getWorldPath(LevelResource.ROOT).resolve("serverconfig").resolve(filename).toFile();
        File configDir = worldConfigFile.exists() ? worldConfigFile.getParentFile() : FMLPaths.CONFIGDIR.get().toFile();
        if (!configDir.exists()) {
            throw new RuntimeException("Failed to find server config directory");
        }
        File spellbooksConfig = configDir.toPath().resolve(filename).toFile();
        if (!spellbooksConfig.exists()) {
            throw new RuntimeException("No existing config to convert");
        }
        TomlParser parser = new TomlParser();
        Config toml = parser.parse(spellbooksConfig, FileNotFoundAction.THROW_ERROR);
        Config spellToml = (Config)toml.get("Spells");
        Map<String, SpellConfigParameter<Boolean>> conversionMap = Map.of("Enabled", SpellConfigParameter.ENABLED, "School", SpellConfigParameter.SCHOOL, "MaxLevel", SpellConfigParameter.MAX_LEVEL, "MinRarity", SpellConfigParameter.MIN_RARITY, "ManaCostMultiplier", SpellConfigParameter.MANA_MULTIPLIER, "SpellPowerMultiplier", SpellConfigParameter.POWER_MULTIPLIER, "CooldownInSeconds", SpellConfigParameter.COOLDOWN_IN_SECONDS, "AllowCrafting", SpellConfigParameter.ALLOW_CRAFTING);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        HashMap configOutput = new HashMap();
        for (Object entry : spellToml.entrySet()) {
            ResourceLocation resourceLocation = ResourceLocation.parse((String)entry.getKey());
            if (entry.isNull() || !(entry.getRawValue() instanceof Config)) continue;
            Config config = (Config)entry.getValue();
            HashMap<String, Object> jsonEntry = new HashMap<String, Object>();
            if (SpellRegistry.getSpell(resourceLocation) == SpellRegistry.none()) {
                IronsSpellbooks.LOGGER.info("[Config Converter] Skipping spell {}, not a valid spell", (Object)resourceLocation);
                continue;
            }
            for (Map.Entry<String, SpellConfigParameter<Boolean>> conversion : conversionMap.entrySet()) {
                Object configValue = config.get(conversion.getKey());
                SpellConfigParameter<Boolean> param = conversion.getValue();
                if (configValue == null) continue;
                if (configValue instanceof String) {
                    String string = (String)configValue;
                    configValue = string.toLowerCase(Locale.ROOT);
                }
                if (LegacyConfigConverter.checkIsDefaultValue(resourceLocation, configValue, param)) continue;
                jsonEntry.put(param.key().toString(), configValue);
            }
            if (jsonEntry.size() > 0) {
                configOutput.put(resourceLocation, jsonEntry);
                continue;
            }
            IronsSpellbooks.LOGGER.info("[Config Converter] Skipping config entry {}, all values are default", (Object)resourceLocation);
        }
        File outdir = configDir.toPath().resolve("irons_spellbooks_spell_config").toFile();
        for (Map.Entry entry : configOutput.entrySet()) {
            File modDir = outdir.toPath().resolve(((ResourceLocation)entry.getKey()).getNamespace()).toFile();
            if (!modDir.exists()) {
                modDir.mkdir();
            }
            File fileout = modDir.toPath().resolve(((ResourceLocation)entry.getKey()).getPath() + ".json").toFile();
            try (FileWriter writer = new FileWriter(fileout);){
                gson.toJson(entry.getValue(), (Appendable)writer);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        String path = outdir.toPath().toString();
        IronsSpellbooks.LOGGER.info("[Config Converter] Saved {} entries to {}", (Object)configOutput.size(), (Object)path);
        return path;
    }

    private static boolean checkIsDefaultValue(ResourceLocation spellId, Object value, SpellConfigParameter<?> param) {
        Object toCompare = value;
        if (param.equals(SpellConfigParameter.SCHOOL)) {
            try {
                toCompare = SchoolRegistry.getSchool(ResourceLocation.parse((String)((String)toCompare)));
            }
            catch (Exception e) {
                throw new RuntimeException("Failed to read school entry for spell " + spellId.toString());
            }
        }
        if (param.equals(SpellConfigParameter.MIN_RARITY)) {
            try {
                toCompare = SpellRarity.valueOf(((String)toCompare).toUpperCase(Locale.ROOT));
            }
            catch (Exception e) {
                throw new RuntimeException("Failed to read rarity entry for spell " + spellId.toString());
            }
        }
        return toCompare.equals(SpellConfigManager.getSpellDefaultConfigValue(SpellRegistry.getSpell(spellId), param));
    }
}

