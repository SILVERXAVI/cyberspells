/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientboundOpenBookPacket
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.WrittenBookItem
 *  net.minecraft.world.item.component.WrittenBookContent
 *  net.minecraft.world.level.Level
 */
package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.item.ILecternPlaceable;
import java.util.List;
import java.util.Optional;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundOpenBookPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.level.Level;

public class ReadableLoreItem
extends Item
implements ILecternPlaceable {
    private final ResourceLocation lecternLocation;

    public ReadableLoreItem(ResourceLocation lecternLocation, Item.Properties pProperties) {
        super(pProperties);
        this.lecternLocation = lecternLocation;
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (pPlayer instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)pPlayer;
            if (WrittenBookItem.resolveBookComponents((ItemStack)itemstack, (CommandSourceStack)serverPlayer.createCommandSourceStack(), (Player)serverPlayer)) {
                serverPlayer.containerMenu.broadcastChanges();
            }
            serverPlayer.connection.send((Packet)new ClientboundOpenBookPacket(pHand));
        }
        return InteractionResultHolder.sidedSuccess((Object)itemstack, (boolean)pLevel.isClientSide());
    }

    @Override
    public List<Component> getPages(ItemStack stack) {
        WrittenBookContent writtenbookcontent = (WrittenBookContent)stack.get(DataComponents.WRITTEN_BOOK_CONTENT);
        if (writtenbookcontent != null) {
            return writtenbookcontent.getPages(false);
        }
        return List.of();
    }

    @Override
    public Optional<ResourceLocation> simpleTextureOverride(ItemStack stack) {
        return Optional.of(this.lecternLocation);
    }
}

