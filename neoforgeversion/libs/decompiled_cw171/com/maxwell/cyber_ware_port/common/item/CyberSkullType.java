/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.block.SkullBlock$Type
 */
package com.maxwell.cyber_ware_port.common.item;

import java.util.Locale;
import net.minecraft.world.level.block.SkullBlock;

public enum CyberSkullType implements SkullBlock.Type
{
    CYBER_WITHER_SKELETON;


    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}

