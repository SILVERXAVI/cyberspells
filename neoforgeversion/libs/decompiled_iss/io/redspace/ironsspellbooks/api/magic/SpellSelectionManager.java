/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  net.neoforged.bus.api.Event
 *  net.neoforged.neoforge.common.NeoForge
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent
 *  net.neoforged.neoforge.network.PacketDistributor
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  top.theillusivec4.curios.api.CuriosApi
 *  top.theillusivec4.curios.api.SlotResult
 */
package io.redspace.ironsspellbooks.api.magic;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.spells.SpellSlot;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.gui.overlays.SpellSelection;
import io.redspace.ironsspellbooks.network.gui.SelectSpellPacket;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

public class SpellSelectionManager {
    public static final String MAINHAND = EquipmentSlot.MAINHAND.getName();
    public static final String OFFHAND = EquipmentSlot.OFFHAND.getName();
    private final List<SelectionOption> selectionOptionList = new ArrayList<SelectionOption>();
    private SpellSelection spellSelection = null;
    private int selectionIndex = -1;
    private boolean selectionValid = false;
    private final Player player;

    public SpellSelectionManager(@NotNull Player player) {
        this.player = player;
        this.init(player);
    }

    private void init(Player player) {
        if (player == null) {
            return;
        }
        this.spellSelection = player.level.isClientSide ? ClientMagicData.getSyncedSpellData((LivingEntity)player).getSpellSelection() : MagicData.getPlayerMagicData((LivingEntity)player).getSyncedData().getSpellSelection();
        this.initCurioItems(player);
        this.initItem(player.getItemBySlot(EquipmentSlot.HEAD), EquipmentSlot.HEAD.getName());
        this.initItem(player.getItemBySlot(EquipmentSlot.CHEST), EquipmentSlot.CHEST.getName());
        this.initItem(player.getItemBySlot(EquipmentSlot.LEGS), EquipmentSlot.LEGS.getName());
        this.initItem(player.getItemBySlot(EquipmentSlot.FEET), EquipmentSlot.FEET.getName());
        this.initItem(player.getItemBySlot(EquipmentSlot.MAINHAND), MAINHAND);
        this.initItem(player.getItemBySlot(EquipmentSlot.OFFHAND), OFFHAND);
        NeoForge.EVENT_BUS.post((Event)new SpellSelectionEvent(this.player, this));
        if (!this.selectionValid && !this.selectionOptionList.isEmpty()) {
            this.tryLastSelectionOrDefault();
        }
        if (this.selectionIndex == -1 && !this.selectionOptionList.isEmpty()) {
            this.selectionIndex = 0;
        }
    }

    private void initCurioItems(Player player) {
        CuriosApi.getCuriosInventory((LivingEntity)player).ifPresent(inv -> {
            ItemStack spellbook = Utils.getPlayerSpellbookStack(player);
            if (spellbook != null) {
                this.initItem(spellbook, Curios.SPELLBOOK_SLOT);
            }
            inv.findCurios(ISpellContainer::isSpellContainer).stream().filter(slot -> !slot.slotContext().identifier().equals(Curios.SPELLBOOK_SLOT)).forEach(slotResult -> this.initItem(slotResult.stack(), String.format("%s_%s", slotResult.slotContext().identifier(), slotResult.slotContext().index())));
        });
    }

    private int sortSpellbookSlot(SlotResult s1, SlotResult s2) {
        if (s1.slotContext().identifier().equals(Curios.SPELLBOOK_SLOT)) {
            return -1;
        }
        if (s2.slotContext().identifier().equals(Curios.SPELLBOOK_SLOT)) {
            return 1;
        }
        return s1.slotContext().identifier().compareTo(s2.slotContext().identifier());
    }

    private void initItem(@Nullable ItemStack itemStack, String equipmentSlot) {
        ISpellContainer spellContainer;
        if (ISpellContainer.isSpellContainer(itemStack) && (spellContainer = ISpellContainer.get(itemStack)).isSpellWheel() && (!spellContainer.mustEquip() || !equipmentSlot.equals(MAINHAND) && !equipmentSlot.equals(OFFHAND))) {
            List<SpellSlot> activeSpells = spellContainer.getActiveSpells();
            for (int i = 0; i < activeSpells.size(); ++i) {
                SpellSlot spellSlot = activeSpells.get(i);
                this.addOrMergeSelectionOption(new SelectionOption(spellSlot.spellData(), equipmentSlot, i, this.selectionOptionList.size()));
                if (this.spellSelection.index != i || !this.spellSelection.equipmentSlot.equals(equipmentSlot)) continue;
                this.selectionIndex = this.selectionOptionList.size() - 1;
                this.selectionValid = true;
            }
        }
    }

    private void addOrMergeSelectionOption(SelectionOption option) {
        SelectionOption existing = this.findExistingSpell(option.spellData.getSpell());
        if (existing != null) {
            if (option.spellData.getLevel() > existing.spellData.getLevel()) {
                option.globalIndex = existing.globalIndex;
                this.selectionOptionList.set(existing.globalIndex, option);
            }
        } else {
            this.selectionOptionList.add(option);
        }
    }

    @Nullable
    private SelectionOption findExistingSpell(AbstractSpell spell) {
        for (SelectionOption selectionOption : this.selectionOptionList) {
            if (!selectionOption.spellData.getSpell().equals(spell)) continue;
            return selectionOption;
        }
        return null;
    }

    private void tryLastSelectionOrDefault() {
        List<SelectionOption> spellsForSlot;
        if (this.spellSelection.lastEquipmentSlot.isEmpty()) {
            Optional select = this.selectionOptionList.stream().findFirst();
            select.ifPresent(selection -> this.makeLocalSelection(selection.slot, selection.slotIndex, selection.globalIndex, false));
        } else if (this.spellSelection.lastIndex != -1 && !(spellsForSlot = this.getSpellsForSlot(this.spellSelection.lastEquipmentSlot)).isEmpty()) {
            if (this.spellSelection.lastIndex < spellsForSlot.size()) {
                this.makeLocalSelection(this.spellSelection.lastEquipmentSlot, this.spellSelection.lastIndex, spellsForSlot.get((int)this.spellSelection.lastIndex).globalIndex, false);
            } else {
                this.makeLocalSelection(this.spellSelection.lastEquipmentSlot, 0, spellsForSlot.get((int)0).globalIndex, false);
            }
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public void makeSelection(int index) {
        if (index != this.selectionIndex && index >= 0 && index < this.selectionOptionList.size()) {
            SelectionOption item = this.selectionOptionList.get(index);
            this.makeLocalSelection(item.slot, item.slotIndex, index, true);
        }
    }

    private void makeLocalSelection(String slot, int slotIndex, int globalIndex, boolean doSync) {
        this.selectionIndex = globalIndex;
        this.selectionValid = true;
        if (doSync && this.player.level.isClientSide) {
            this.spellSelection.makeSelection(slot, slotIndex);
            PacketDistributor.sendToServer((CustomPacketPayload)new SelectSpellPacket(this.spellSelection), (CustomPacketPayload[])new CustomPacketPayload[0]);
        }
    }

    private void setSpellSelection(SpellSelection spellSelection) {
        this.spellSelection = spellSelection;
        if (this.player.level.isClientSide) {
            PacketDistributor.sendToServer((CustomPacketPayload)new SelectSpellPacket(spellSelection), (CustomPacketPayload[])new CustomPacketPayload[0]);
        } else {
            MagicData.getPlayerMagicData((LivingEntity)this.player).getSyncedData().setSpellSelection(spellSelection);
        }
    }

    public SpellSelection getCurrentSelection() {
        return this.spellSelection;
    }

    @Nullable
    public SelectionOption getSpellSlot(int index) {
        if (index >= 0 && index < this.selectionOptionList.size()) {
            return this.selectionOptionList.get(index);
        }
        return null;
    }

    public SpellData getSpellData(int index) {
        if (index >= 0 && index < this.selectionOptionList.size()) {
            return this.selectionOptionList.get((int)index).spellData;
        }
        return SpellData.EMPTY;
    }

    public int getSelectionIndex() {
        return this.selectionIndex;
    }

    public int getGlobalSelectionIndex() {
        SelectionOption selection = this.getSelection();
        if (selection == null) {
            return -1;
        }
        return this.getSelection().globalIndex;
    }

    @Nullable
    public SelectionOption getSelection() {
        if (this.selectionIndex >= 0 && this.selectionIndex < this.selectionOptionList.size()) {
            return this.selectionOptionList.get(this.selectionIndex);
        }
        if (!this.selectionOptionList.isEmpty()) {
            return this.selectionOptionList.get(0);
        }
        return null;
    }

    public SpellData getSelectedSpellData() {
        return this.selectionIndex >= 0 && this.selectionIndex < this.selectionOptionList.size() ? this.selectionOptionList.get((int)this.selectionIndex).spellData : SpellData.EMPTY;
    }

    public List<SelectionOption> getSpellsForSlot(String slot) {
        return this.selectionOptionList.stream().filter(selectionOption -> selectionOption.slot.equals(slot)).toList();
    }

    public List<SelectionOption> getAllSpells() {
        return this.selectionOptionList;
    }

    public SpellData getSpellForSlot(String slot, int index) {
        List<SelectionOption> spells = this.getSpellsForSlot(slot);
        if (index >= 0 && index < spells.size()) {
            return spells.get((int)index).spellData;
        }
        return SpellData.EMPTY;
    }

    public int getSpellCount() {
        return this.selectionOptionList.size();
    }

    public static class SpellSelectionEvent
    extends PlayerEvent {
        SpellSelectionManager manager;

        public SpellSelectionEvent(Player player, SpellSelectionManager manager) {
            super(player);
            this.manager = manager;
        }

        public void addSelectionOption(SpellData spellData, String slotId, int localSlotIndex) {
            this.addSelectionOption(spellData, slotId, localSlotIndex, this.manager.selectionOptionList.size());
        }

        public void addSelectionOption(SpellData spellData, String slotId, int localSlotIndex, int globalIndex) {
            globalIndex = this.manager.selectionOptionList.size();
            if (globalIndex >= 0 && globalIndex <= this.manager.selectionOptionList.size()) {
                this.manager.selectionOptionList.add(globalIndex, new SelectionOption(spellData, slotId, localSlotIndex, globalIndex));
                if (this.manager.spellSelection.index == localSlotIndex && this.manager.spellSelection.equipmentSlot.equals(slotId)) {
                    this.manager.selectionIndex = this.manager.selectionOptionList.size() - 1;
                    this.manager.selectionValid = true;
                }
            }
        }

        public SpellSelectionManager getManager() {
            return this.manager;
        }
    }

    public static class SelectionOption {
        public SpellData spellData;
        public String slot;
        public int slotIndex;
        public int globalIndex;

        public SelectionOption(SpellData spellData, String slot, int slotIndex, int globalIndex) {
            this.spellData = spellData;
            this.slot = slot;
            this.slotIndex = slotIndex;
            this.globalIndex = globalIndex;
        }

        public CastSource getCastSource() {
            return this.slot.startsWith(Curios.SPELLBOOK_SLOT) ? CastSource.SPELLBOOK : CastSource.SWORD;
        }
    }
}

