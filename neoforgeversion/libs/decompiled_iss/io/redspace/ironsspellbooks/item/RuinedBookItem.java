/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.Style
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 */
package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.item.ILecternPlaceable;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RuinedBookItem
extends Item
implements ILecternPlaceable {
    public static Component PAGE = Component.literal((String)String.valueOf(new char[]{'I', 'n', ' ', 't', 'h', 'e', ' ', 'b', 'e', 'g', 'i', 'n', 'n', 'i', 'n', 'g', ',', ' ', 'o', 'n', 'l', 'y', ' ', 'd', 'a', 'r', 'k', 'n', 'e', 's', 's', '.', '\n', 'W', 'h', 'y', ' ', 'd', 'i', 'd', ' ', 's', 'h', 'e', ' ', 'l', 'e', 'a', 'v', 'e', '?', '\n', 'D', 'a', 'u', 'g', 'h', 't', 'e', 'r', ' ', 'o', 'f', ' ', 'D', 'a', 'r', 'k', 'n', 'e', 's', 's', ',', ' ', 'B', 'r', 'i', 'n', 'g', 'e', 'r', ' ', 'o', 'f', ' ', 'L', 'i', 'g', 'h', 't', '.', '\n', 'O', 'h', ' ', 'h', 'o', 'w', ' ', 't', 'h', 'e', ' ', 'V', 'o', 'i', 'd', ' ', 'm', 'u', 's', 't', ' ', 'h', 'a', 't', 'e', ' ', 'y', 'o', 'u', ',', '\n', 'A', 'r', 'a', 't', 'h', 'y', 'l', 'l', ',', ' ', 'A', 'r', 'a', 't', 'h', 'y', 'l', 'l', ',', ' ', 'A', 'r', 'a', 't', 'h', 'y', 'l', 'l', '.', '.', '.'})).withStyle(Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace((String)"alt")));
    public static Component PAGE2 = PAGE.copy().withStyle(ChatFormatting.OBFUSCATED);
    public static Component DARKNESS = Component.literal((String)"Darkness").withStyle(Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace((String)"alt")));
    public static Component DARKNESS2 = DARKNESS.copy().withStyle(ChatFormatting.OBFUSCATED);
    public static ResourceLocation LECTERN_LOCATION = IronsSpellbooks.id("textures/entity/lectern/ruined_book.png");

    public RuinedBookItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public List<Component> getPages(ItemStack stack) {
        Player player = MinecraftInstanceHelper.getPlayer();
        if (player == null || !player.hasEffect(MobEffectRegistry.PLANAR_SIGHT)) {
            return List.of(PAGE2, DARKNESS2, DARKNESS2, DARKNESS2, DARKNESS2, DARKNESS2, DARKNESS2, DARKNESS2, DARKNESS2, DARKNESS2);
        }
        return List.of(PAGE, DARKNESS, DARKNESS, DARKNESS, DARKNESS, DARKNESS, DARKNESS, DARKNESS, DARKNESS, DARKNESS);
    }

    @Override
    public Optional<ResourceLocation> simpleTextureOverride(ItemStack stack) {
        return Optional.of(LECTERN_LOCATION);
    }
}

