/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.ComponentContents
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.contents.TranslatableContents
 */
package io.redspace.ironsspellbooks.command;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;

public class SpellBalanceDebugger {
    Map<String, Integer> trackedProperties;
    List<Info> spellInfo = new ArrayList<Info>();

    public SpellBalanceDebugger() {
        this.trackedProperties = new HashMap<String, Integer>();
    }

    private void trackOccurance(String property) {
        int occurrences = this.trackedProperties.getOrDefault(property, 0);
        this.trackedProperties.put(property, ++occurrences);
    }

    private Map<String, String> getValuesFromSpell(AbstractSpell spell) {
        List<MutableComponent> info = spell.getUniqueInfo(spell.getMaxLevel(), null);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("Name", Component.translatable((String)spell.getComponentId()).getString());
        map.put("Mana Cost", String.valueOf(spell.getManaCost(spell.getMaxLevel())));
        map.put("Cooldown", Utils.timeFromTicks(spell.getSpellCooldown(), 0));
        map.put("Cast Type", spell.getCastType().toString());
        HashSet<String> tracked = new HashSet<String>();
        for (Component component : info) {
            ComponentContents componentContents = component.getContents();
            if (!(componentContents instanceof TranslatableContents)) continue;
            TranslatableContents translatableContents = (TranslatableContents)componentContents;
            String fullLine = component.getString();
            String translate = Component.translatable((String)translatableContents.getKey()).getString();
            int valueBeginIndex = translate.indexOf("%s");
            if (valueBeginIndex < 0) {
                IronsSpellbooks.LOGGER.info("skipping property {}", (Object)translate);
                continue;
            }
            int valueEndIndex = fullLine.indexOf(" ", valueBeginIndex);
            if (valueEndIndex < 0) {
                valueEndIndex = fullLine.length();
            }
            String value = fullLine.substring(valueBeginIndex, valueEndIndex);
            String property = translate.replace("%s", "");
            map.put(property, value);
            if (tracked.contains(property)) continue;
            this.trackOccurance(property);
            tracked.add(property);
        }
        return map;
    }

    private List<String> generateCSV() {
        ArrayList propertiesToExport = new ArrayList();
        for (Map.Entry<String, Integer> entry : this.trackedProperties.entrySet()) {
            if (entry.getValue() < 3) continue;
            propertiesToExport.add(entry.getKey());
        }
        propertiesToExport.addFirst("Cast Type");
        propertiesToExport.addFirst("Cooldown");
        propertiesToExport.addFirst("Mana Cost");
        propertiesToExport.addFirst("Name");
        String header = String.join((CharSequence)",", propertiesToExport);
        ArrayList<String> contents = new ArrayList<String>();
        contents.add(header);
        for (Info spellInfo : this.spellInfo) {
            StringBuilder line = new StringBuilder();
            for (String property : propertiesToExport) {
                line.append(spellInfo.values.getOrDefault(property, "")).append(",");
            }
            contents.add(line.toString());
        }
        return contents;
    }

    private void exportCSV(String fileName, List<String> contents) throws IOException {
        if (!((String)fileName).endsWith(".csv")) {
            fileName = (String)fileName + ".csv";
        }
        Path dirPath = Path.of("screenshots/irons_jewelry", new String[0]);
        Path filePath = dirPath.resolve((String)fileName);
        if (Files.notExists(dirPath, new LinkOption[0])) {
            Files.createDirectories(dirPath, new FileAttribute[0]);
        }
        if (Files.notExists(filePath, new LinkOption[0])) {
            Files.createFile(filePath, new FileAttribute[0]);
        }
        Files.write(filePath, contents, new OpenOption[0]);
    }

    public void run() {
        for (AbstractSpell spell : SpellRegistry.REGISTRY) {
            this.spellInfo.add(new Info(spell, this.getValuesFromSpell(spell)));
        }
        try {
            this.exportCSV("spell_info.csv", this.generateCSV());
        }
        catch (IOException e) {
            IronsSpellbooks.LOGGER.error("Failed to generate csv: {}", (Object)e.getMessage());
        }
    }

    record Info(AbstractSpell spell, Map<String, String> values) {
    }
}

