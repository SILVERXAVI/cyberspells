/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
 */
package io.redspace.ironsspellbooks.item.curios;

import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.render.AffinityRingRenderer;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class AffinityRing
extends CurioBaseItem {
    public AffinityRing(Item.Properties properties) {
        super(properties);
    }

    public void appendHoverText(ItemStack pStack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        AffinityData affinity = AffinityData.getAffinityData(pStack);
        if (affinity != AffinityData.NONE && !affinity.affinityData().isEmpty()) {
            tooltip.add((Component)Component.empty());
            tooltip.add((Component)Component.translatable((String)"curios.modifiers.ring").withStyle(ChatFormatting.GOLD));
            tooltip.addAll(affinity.getDescriptionComponent());
        } else {
            tooltip.add((Component)Component.translatable((String)"tooltip.irons_spellbooks.empty_affinity_ring").withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC}));
        }
    }

    public Component getName(ItemStack pStack) {
        return Component.translatable((String)this.getDescriptionId(pStack), (Object[])new Object[]{AffinityData.getAffinityData(pStack).getNameForItem()});
    }

    public static class ClientExtension
    implements IClientItemExtensions {
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return new AffinityRingRenderer(Minecraft.getInstance().getItemRenderer(), Minecraft.getInstance().getEntityModels());
        }
    }
}

