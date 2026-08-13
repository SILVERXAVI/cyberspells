/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.fml.ModList
 *  org.objectweb.asm.Type
 */
package io.redspace.ironsspellbooks.config;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import net.neoforged.fml.ModList;
import org.objectweb.asm.Type;

@Deprecated(forRemoval=true)
public final class SpellDiscovery {
    @Deprecated(forRemoval=true)
    public static List<AbstractSpell> getSpellsForConfig() {
        List allScanData = ModList.get().getAllScanData();
        HashSet spellClassNames = new HashSet();
        allScanData.forEach(scanData -> scanData.getAnnotations().forEach(annotationData -> {
            if (Objects.equals(annotationData.annotationType(), Type.getType(AutoSpellConfig.class))) {
                spellClassNames.add(annotationData.memberName());
            }
        }));
        ArrayList<AbstractSpell> spells = new ArrayList<AbstractSpell>();
        spellClassNames.forEach(spellName -> {
            try {
                Class<?> pluginClass = Class.forName(spellName);
                Class<AbstractSpell> pluginClassSubclass = pluginClass.asSubclass(AbstractSpell.class);
                Constructor<AbstractSpell> constructor = pluginClassSubclass.getDeclaredConstructor(new Class[0]);
                AbstractSpell instance = constructor.newInstance(new Object[0]);
                spells.add(instance);
            }
            catch (Exception e) {
                IronsSpellbooks.LOGGER.error("SpellDiscovery:  {}, {}", spellName, (Object)e);
            }
        });
        return spells;
    }
}

