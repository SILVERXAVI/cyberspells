package cyberspells.logic;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import cyberspells.items.RuneHolder;
import java.util.List;

public class RuneSpellEventHandler {

    @SubscribeEvent
    public static void onSpellSelection(io.redspace.ironsspellbooks.api.magic.SpellSelectionManager.SpellSelectionEvent event) {
        Player player = event.getEntity();
        if (player == null)
            return;

        if (ModList.get().isLoaded("createcybernetics")) {
            addCCSpells(player, event);
        }

        if (ModList.get().isLoaded("cyber_ware_port")) {
            addCWSpells(player, event);
        }
    }

    private static void addCCSpells(Player player, io.redspace.ironsspellbooks.api.magic.SpellSelectionManager.SpellSelectionEvent event) {
        try {
            com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData data = player.getData(com.perigrine3.createcybernetics.common.capabilities.ModAttachments.CYBERWARE);
            if (data != null) {
                for (com.perigrine3.createcybernetics.api.CyberwareSlot slot : com.perigrine3.createcybernetics.api.CyberwareSlot.values()) {
                    com.perigrine3.createcybernetics.api.InstalledCyberware installed = data.get(slot, 0);
                    if (installed != null) {
                        ItemStack stack = installed.getItem();
                        if (stack != null && !stack.isEmpty() && stack.getItem() instanceof RuneHolder holder) {
                            processStackSpells(stack, holder, slot.name().toLowerCase(), event);
                        }
                    }
                }
            }
        } catch (Throwable ignored) {}
    }

    private static void addCWSpells(Player player, io.redspace.ironsspellbooks.api.magic.SpellSelectionManager.SpellSelectionEvent event) {
        try {
            com.maxwell.cyber_ware_port.common.capability.CyberwareUserData data = player.getData(com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider.CYBERWARE_DATA);
            if (data != null && data.getInstalledCyberware() != null) {
                var handler = data.getInstalledCyberware();
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack stack = handler.getStackInSlot(i);
                    if (stack != null && !stack.isEmpty() && stack.getItem() instanceof RuneHolder holder) {
                        processStackSpells(stack, holder, "cw_" + i, event);
                    }
                }
            }
        } catch (Throwable ignored) {}
    }

    private static void processStackSpells(ItemStack stack, RuneHolder holder, String slotName, io.redspace.ironsspellbooks.api.magic.SpellSelectionManager.SpellSelectionEvent event) {
        List<String> runes = holder.getRunes(stack);
        int maxSlots = holder.getMaxRuneSlots();
        int localIndex = 0;
        for (int i = 0; i < runes.size() && i < maxSlots; i++) {
            String runeId = runes.get(i);
            if (runeId.startsWith("spell:")) {
                String[] parts = runeId.split(":");
                if (parts.length >= 3) {
                    String spellId = parts[1] + (parts.length >= 4 ? ":" + parts[2] : "");
                    int level = 1;
                    try {
                        level = Integer.parseInt(parts[parts.length - 1]);
                    } catch (Exception ignored) {}
                    io.redspace.ironsspellbooks.api.spells.AbstractSpell spell = io.redspace.ironsspellbooks.api.registry.SpellRegistry.getSpell(spellId);
                    if (spell != null && !spell.equals(io.redspace.ironsspellbooks.api.registry.SpellRegistry.none())) {
                        io.redspace.ironsspellbooks.api.spells.SpellData spellData = new io.redspace.ironsspellbooks.api.spells.SpellData(spell, level);
                        event.addSelectionOption(spellData, "cyberware_" + slotName, localIndex++);
                    }
                }
            }
        }
    }
}
