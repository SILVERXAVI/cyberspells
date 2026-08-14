/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.level.ItemLike
 *  net.neoforged.neoforge.capabilities.Capabilities$ItemHandler
 *  net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  org.jetbrains.annotations.NotNull
 */
package com.maxwell.cyber_ware_port.common.item.componentbox;

import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.common.item.componentbox.ComponentBoxItem;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.stream.Stream;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class ComponentBoxCapability {
    public static void register(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.ItemHandler.ITEM, (stack, context) -> {
            HolderLookup.Provider provider = HolderLookup.Provider.create(Stream.of(BuiltInRegistries.ITEM.asLookup()));
            ItemStackHandler handler = new ItemStackHandler(18, (ItemStack)stack, provider){
                final /* synthetic */ ItemStack val$stack;
                final /* synthetic */ HolderLookup.Provider val$provider;
                {
                    this.val$stack = itemStack;
                    this.val$provider = provider;
                    super(arg0);
                }

                public boolean isItemValid(int slot, @NotNull ItemStack s) {
                    Item item = s.getItem();
                    if (item instanceof CyberwareItem || item instanceof ComponentBoxItem) {
                        return false;
                    }
                    ResourceLocation id = BuiltInRegistries.ITEM.getKey((Object)item);
                    return id.getPath().contains("component_");
                }

                protected void onContentsChanged(int slot) {
                    this.val$stack.set(DataComponents.CUSTOM_DATA, (Object)CustomData.of((CompoundTag)this.serializeNBT(this.val$provider)));
                }
            };
            CustomData data = (CustomData)stack.get(DataComponents.CUSTOM_DATA);
            if (data != null) {
                handler.deserializeNBT(provider, data.copyTag());
            }
            return handler;
        }, new ItemLike[]{(ItemLike)ModItems.COMPONENT_BOX.get()});
    }
}

