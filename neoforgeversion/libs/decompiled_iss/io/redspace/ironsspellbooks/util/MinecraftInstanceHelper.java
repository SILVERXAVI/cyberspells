/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.util;

import io.redspace.ironsspellbooks.util.IMinecraftInstanceHelper;
import java.util.function.Consumer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class MinecraftInstanceHelper
implements IMinecraftInstanceHelper {
    public static IMinecraftInstanceHelper instance = () -> null;

    @Override
    @Nullable
    public Player player() {
        return instance.player();
    }

    @Nullable
    public static Player getPlayer() {
        return instance.player();
    }

    public static void ifPlayerPresent(Consumer<Player> consumer) {
        Player player = MinecraftInstanceHelper.getPlayer();
        if (player != null) {
            consumer.accept(player);
        }
    }
}

