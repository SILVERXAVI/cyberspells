package cyberspells.logic;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import cyberspells.items.RuneHolder;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.compat.Curios;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

public class RuneSpellEventHandler {

    private static final Map<UUID, String> LAST_SPELL_SUMMARY = new HashMap<>();

    @SubscribeEvent
    public static void onSpellSelection(SpellSelectionManager.SpellSelectionEvent event) {
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

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player == null || player.level().isClientSide)
            return;

        if (player.tickCount % 10 == 0) {
            String currentSummary = getCyberwareSpellSummary(player);
            String lastSummary = LAST_SPELL_SUMMARY.get(player.getUUID());

            if (lastSummary == null || !lastSummary.equals(currentSummary)) {
                LAST_SPELL_SUMMARY.put(player.getUUID(), currentSummary);

                if (player instanceof ServerPlayer serverPlayer) {
                    SpellSelectionManager manager = new SpellSelectionManager(serverPlayer);
                    MagicData magicData = MagicData.getPlayerMagicData(serverPlayer);
                    if (magicData != null && magicData.getSyncedData() != null) {
                        magicData.getSyncedData().setSpellSelection(manager.getCurrentSelection());
                        magicData.getSyncedData().syncToPlayer(serverPlayer);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() != null) {
            LAST_SPELL_SUMMARY.remove(event.getEntity().getUUID());
        }
    }

    public static String getCyberwareSpellSummary(Player player) {
        StringBuilder sb = new StringBuilder();

        if (ModList.get().isLoaded("createcybernetics")) {
            try {
                com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData data = player.getData(com.perigrine3.createcybernetics.common.capabilities.ModAttachments.CYBERWARE);
                if (data != null) {
                    for (com.perigrine3.createcybernetics.api.CyberwareSlot slot : com.perigrine3.createcybernetics.api.CyberwareSlot.values()) {
                        com.perigrine3.createcybernetics.api.InstalledCyberware installed = data.get(slot, 0);
                        if (installed != null) {
                            ItemStack stack = installed.getItem();
                            if (stack != null && !stack.isEmpty() && stack.getItem() instanceof RuneHolder holder) {
                                appendStackSpells(stack, holder, sb);
                            }
                        }
                    }
                }
            } catch (Throwable ignored) {}
        }

        if (ModList.get().isLoaded("cyber_ware_port")) {
            try {
                com.maxwell.cyber_ware_port.common.capability.CyberwareUserData data = player.getData(com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider.CYBERWARE_DATA);
                if (data != null && data.getInstalledCyberware() != null) {
                    var handler = data.getInstalledCyberware();
                    for (int i = 0; i < handler.getSlots(); i++) {
                        ItemStack stack = handler.getStackInSlot(i);
                        if (stack != null && !stack.isEmpty() && stack.getItem() instanceof RuneHolder holder) {
                            appendStackSpells(stack, holder, sb);
                        }
                    }
                }
            } catch (Throwable ignored) {}
        }

        return sb.toString();
    }

    private static void appendStackSpells(ItemStack stack, RuneHolder holder, StringBuilder sb) {
        List<String> runes = holder.getRunes(stack);
        int maxSlots = holder.getMaxRuneSlots();
        for (int i = 0; i < runes.size() && i < maxSlots; i++) {
            String runeId = runes.get(i);
            if (runeId.startsWith("spell:")) {
                sb.append(runeId).append(";");
            }
        }
    }

    private static void addCCSpells(Player player, SpellSelectionManager.SpellSelectionEvent event) {
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

    private static void addCWSpells(Player player, SpellSelectionManager.SpellSelectionEvent event) {
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

    private static void processStackSpells(ItemStack stack, RuneHolder holder, String slotName, SpellSelectionManager.SpellSelectionEvent event) {
        List<String> runes = holder.getRunes(stack);
        int maxSlots = holder.getMaxRuneSlots();
        int localIndex = 0;
        String slotId = Curios.SPELLBOOK_SLOT + "_cyberware_" + slotName;
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
                    AbstractSpell spell = SpellRegistry.getSpell(spellId);
                    if (spell != null && !spell.equals(SpellRegistry.none())) {
                        SpellData spellData = new SpellData(spell, level);
                        event.addSelectionOption(spellData, slotId, localIndex++);
                    }
                }
            }
        }
    }
}
