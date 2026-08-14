/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.data.PackOutput
 *  net.minecraft.data.tags.ItemTagsProvider
 *  net.minecraft.data.tags.TagsProvider$TagLookup
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.block.Block
 *  net.neoforged.neoforge.common.data.ExistingFileHelper
 *  org.jetbrains.annotations.Nullable
 */
package com.maxwell.cyber_ware_port.datagen;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import com.maxwell.cyber_ware_port.common.util.ModTags;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModItemTagProvider
extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, "cyber_ware_port", existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider pProvider) {
        ModItems.ITEMS.getEntries().forEach(holder -> {
            Item item = (Item)holder.get();
            if (item instanceof ICyberware) {
                ICyberware cw = (ICyberware)item;
                this.tag(ModTags.Items.CYBERWARE).add((Object)item);
                int slot = cw.getSlot(ItemStack.EMPTY);
                int slotsPerPart = 9;
                if (slot >= RobosurgeonBlockEntity.SLOT_EYES && slot < RobosurgeonBlockEntity.SLOT_EYES + slotsPerPart) {
                    this.tag(ModTags.Items.CYBERWARE_EYES).add((Object)item);
                } else if (slot >= RobosurgeonBlockEntity.SLOT_BRAIN && slot < RobosurgeonBlockEntity.SLOT_BRAIN + slotsPerPart) {
                    this.tag(ModTags.Items.CYBERWARE_BRAIN).add((Object)item);
                } else if (slot >= RobosurgeonBlockEntity.SLOT_HEART && slot < RobosurgeonBlockEntity.SLOT_HEART + slotsPerPart) {
                    this.tag(ModTags.Items.CYBERWARE_HEART).add((Object)item);
                } else if (slot >= RobosurgeonBlockEntity.SLOT_LUNGS && slot < RobosurgeonBlockEntity.SLOT_LUNGS + slotsPerPart) {
                    this.tag(ModTags.Items.CYBERWARE_LUNGS).add((Object)item);
                } else if (slot >= RobosurgeonBlockEntity.SLOT_STOMACH && slot < RobosurgeonBlockEntity.SLOT_STOMACH + slotsPerPart) {
                    this.tag(ModTags.Items.CYBERWARE_STOMACH).add((Object)item);
                } else if (slot >= RobosurgeonBlockEntity.SLOT_SKIN && slot < RobosurgeonBlockEntity.SLOT_SKIN + slotsPerPart) {
                    this.tag(ModTags.Items.CYBERWARE_SKIN).add((Object)item);
                } else if (slot >= RobosurgeonBlockEntity.SLOT_MUSCLE && slot < RobosurgeonBlockEntity.SLOT_MUSCLE + slotsPerPart) {
                    this.tag(ModTags.Items.CYBERWARE_MUSCLE).add((Object)item);
                } else if (slot >= RobosurgeonBlockEntity.SLOT_BONES && slot < RobosurgeonBlockEntity.SLOT_BONES + slotsPerPart) {
                    this.tag(ModTags.Items.CYBERWARE_BONES).add((Object)item);
                } else if (slot >= RobosurgeonBlockEntity.SLOT_ARMS && slot < RobosurgeonBlockEntity.SLOT_ARMS + slotsPerPart) {
                    this.tag(ModTags.Items.CYBERWARE_ARMS).add((Object)item);
                } else if (slot >= RobosurgeonBlockEntity.SLOT_HANDS && slot < RobosurgeonBlockEntity.SLOT_HANDS + slotsPerPart) {
                    this.tag(ModTags.Items.CYBERWARE_HANDS).add((Object)item);
                } else if (slot >= RobosurgeonBlockEntity.SLOT_LEGS && slot < RobosurgeonBlockEntity.SLOT_LEGS + slotsPerPart) {
                    this.tag(ModTags.Items.CYBERWARE_LEGS).add((Object)item);
                } else if (slot >= RobosurgeonBlockEntity.SLOT_BOOTS && slot < RobosurgeonBlockEntity.SLOT_BOOTS + slotsPerPart) {
                    this.tag(ModTags.Items.CYBERWARE_BOOTS).add((Object)item);
                }
            }
        });
    }
}

