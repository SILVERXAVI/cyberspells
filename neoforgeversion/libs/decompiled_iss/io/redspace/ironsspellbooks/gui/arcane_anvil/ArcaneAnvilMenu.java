/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.Holder$Reference
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.ContainerLevelAccess
 *  net.minecraft.world.inventory.ItemCombinerMenu
 *  net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockState
 */
package io.redspace.ironsspellbooks.gui.arcane_anvil;

import io.redspace.ironsspellbooks.api.item.UpgradeData;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainerMutable;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.SpellSlotUpgradeItem;
import io.redspace.ironsspellbooks.item.UniqueItem;
import io.redspace.ironsspellbooks.item.armor.UpgradeOrbType;
import io.redspace.ironsspellbooks.item.curios.AffinityRing;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.MenuRegistry;
import io.redspace.ironsspellbooks.util.UpgradeUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ArcaneAnvilMenu
extends ItemCombinerMenu {
    private final List<ItemStack> additionalDrops = new ArrayList<ItemStack>();

    public ArcaneAnvilMenu(int pContainerId, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        super(MenuRegistry.ARCANE_ANVIL_MENU.get(), pContainerId, inventory, containerLevelAccess);
    }

    public ArcaneAnvilMenu(int pContainerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(pContainerId, inventory, ContainerLevelAccess.NULL);
    }

    protected boolean mayPickup(Player pPlayer, boolean pHasStack) {
        return true;
    }

    protected void onTake(Player p_150601_, ItemStack p_150602_) {
        this.inputSlots.getItem(0).shrink(1);
        this.inputSlots.getItem(1).shrink(1);
        this.access.execute((level, pos) -> {
            level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 0.8f, 1.1f);
            level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
            this.additionalDrops.forEach(stack -> {
                if (!stack.isEmpty()) {
                    level.addFreshEntity((Entity)new ItemEntity(level, (double)pos.getX() + 0.5, (double)(pos.getY() + 1), (double)pos.getZ() + 0.5, stack));
                }
            });
            this.additionalDrops.clear();
        });
        this.createResult();
    }

    protected boolean isValidBlock(BlockState pState) {
        return pState.is((Block)BlockRegistry.ARCANE_ANVIL_BLOCK.get());
    }

    public void createResult() {
        ItemStack result = ItemStack.EMPTY;
        this.additionalDrops.clear();
        ItemStack baseItemStack = this.inputSlots.getItem(0);
        ItemStack modifierItemStack = this.inputSlots.getItem(1);
        if (!baseItemStack.isEmpty() && !modifierItemStack.isEmpty()) {
            Object scrollSlot;
            Object spell1;
            Item item;
            if (((Boolean)ServerConfigs.SCROLL_MERGING.get()).booleanValue() && baseItemStack.getItem() instanceof Scroll && (item = modifierItemStack.getItem()) instanceof InkItem) {
                InkItem inkItem = (InkItem)item;
                spell1 = ISpellContainer.get(baseItemStack).getSpellAtIndex(0);
                if (((SpellData)spell1).getLevel() < ((SpellData)spell1).getSpell().getMaxLevel()) {
                    SpellRarity baseRarity = ((SpellData)spell1).getRarity();
                    SpellRarity nextRarity = ((SpellData)spell1).getSpell().getRarity(((SpellData)spell1).getLevel() + 1);
                    if (nextRarity.equals((Object)inkItem.getRarity())) {
                        result = baseItemStack.copy();
                        result.setCount(1);
                        ISpellContainer.createScrollContainer(((SpellData)spell1).getSpell(), ((SpellData)spell1).getLevel() + 1, result);
                    }
                }
            } else if (baseItemStack.getItem() instanceof UniqueItem && (spell1 = modifierItemStack.getItem()) instanceof Scroll) {
                SpellData spellData;
                ISpellContainer spellContainer;
                int matchIndex;
                Scroll scroll = (Scroll)spell1;
                scrollSlot = ISpellContainer.get(modifierItemStack).getSpellAtIndex(0);
                if (ISpellContainer.isSpellContainer(baseItemStack) && (matchIndex = (spellContainer = ISpellContainer.get(baseItemStack)).getIndexForSpell(((SpellData)scrollSlot).getSpell())) >= 0 && (spellData = spellContainer.getSpellAtIndex(matchIndex)).getLevel() < ((SpellData)scrollSlot).getLevel() && spellData.isLocked()) {
                    result = baseItemStack.copy();
                    ISpellContainerMutable newContainer = spellContainer.mutableCopy();
                    newContainer.removeSpellAtIndex(matchIndex);
                    newContainer.addSpellAtIndex(((SpellData)scrollSlot).getSpell(), ((SpellData)scrollSlot).getLevel(), matchIndex, true);
                    newContainer.setImproved(true);
                    ISpellContainer.set(result, newContainer.toImmutable());
                }
            } else if (Utils.canImbue(baseItemStack) && (scrollSlot = modifierItemStack.getItem()) instanceof Scroll) {
                Scroll scroll = (Scroll)scrollSlot;
                result = baseItemStack.copy();
                ISpellContainerMutable spellContainer = ISpellContainer.getOrCreate(result).mutableCopy();
                SpellData scrollSlot2 = ISpellContainer.get(modifierItemStack).getSpellAtIndex(0);
                int nextSlotIndex = spellContainer.getNextAvailableIndex();
                if (nextSlotIndex == -1) {
                    nextSlotIndex = 0;
                }
                spellContainer.removeSpellAtIndex(nextSlotIndex);
                spellContainer.addSpellAtIndex(scrollSlot2.getSpell(), scrollSlot2.getLevel(), nextSlotIndex, false);
                ISpellContainer.set(result, spellContainer.toImmutable());
            } else if (Utils.canBeUpgraded(baseItemStack) && UpgradeData.getUpgradeData(baseItemStack).getTotalUpgrades() < (Integer)ServerConfigs.MAX_UPGRADES.get() && modifierItemStack.has(ComponentRegistry.UPGRADE_ORB_TYPE)) {
                ResourceKey upgradeKey = (ResourceKey)modifierItemStack.get(ComponentRegistry.UPGRADE_ORB_TYPE);
                Optional holderopt = this.player.registryAccess().holder(upgradeKey);
                if (holderopt.isPresent()) {
                    Holder.Reference upgradeOrb = (Holder.Reference)holderopt.get();
                    result = baseItemStack.copy();
                    String slot = UpgradeUtils.getRelevantEquipmentSlot(result);
                    UpgradeData.getUpgradeData(result).addUpgrade(result, (Holder<UpgradeOrbType>)upgradeOrb, slot);
                }
            } else if (modifierItemStack.is((Item)ItemRegistry.SHRIVING_STONE.get())) {
                result = Utils.handleShriving(baseItemStack);
                upgradeData = UpgradeData.getUpgradeData(baseItemStack);
                ((UpgradeData)upgradeData).upgrades().forEach((upgrade, count) -> ((UpgradeOrbType)upgrade.value()).containerItem().map(stack -> {
                    stack.setCount(count.intValue());
                    return stack;
                }).ifPresent(this.additionalDrops::add));
            } else {
                upgradeData = modifierItemStack.getItem();
                if (upgradeData instanceof SpellSlotUpgradeItem) {
                    SpellSlotUpgradeItem spellSlotUpgradeItem = (SpellSlotUpgradeItem)((Object)upgradeData);
                    if (baseItemStack.getItem() instanceof SpellBook) {
                        spellBookContainer = ISpellContainer.get(baseItemStack);
                        int max = spellSlotUpgradeItem.maxSlots();
                        if (spellBookContainer.getMaxSpellCount() < max) {
                            result = baseItemStack.copy();
                            ISpellContainerMutable upgradedContainer = ISpellContainer.get(result).mutableCopy();
                            upgradedContainer.setMaxSpellCount(upgradedContainer.getMaxSpellCount() + 1);
                            ISpellContainer.set(result, upgradedContainer.toImmutable());
                        }
                    }
                } else {
                    spellBookContainer = baseItemStack.getItem();
                    if (spellBookContainer instanceof AffinityRing) {
                        AffinityRing affinityRing = (AffinityRing)((Object)spellBookContainer);
                        spellBookContainer = modifierItemStack.getItem();
                        if (spellBookContainer instanceof Scroll) {
                            Scroll scroll = (Scroll)spellBookContainer;
                            result = baseItemStack.copy();
                            scrollSlot = ISpellContainer.get(modifierItemStack).getSpellAtIndex(0);
                            AffinityData newData = new AffinityData(((SpellData)scrollSlot).getSpell());
                            AffinityData.set(result, newData);
                        }
                    }
                }
            }
        }
        this.resultSlots.setItem(0, result);
    }

    protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create().withSlot(0, 27, 47, p_266635_ -> true).withSlot(1, 76, 47, p_266634_ -> true).withResultSlot(2, 134, 47).build();
    }

    public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
        return pSlot.container != this.resultSlots && super.canTakeItemForPickAll(pStack, pSlot);
    }
}

