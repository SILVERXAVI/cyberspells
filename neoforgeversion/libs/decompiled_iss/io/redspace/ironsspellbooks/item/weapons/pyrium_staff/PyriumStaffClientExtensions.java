/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
 */
package io.redspace.ironsspellbooks.item.weapons.pyrium_staff;

import io.redspace.ironsspellbooks.item.weapons.pyrium_staff.PyriumStaffRenderer;
import io.redspace.ironsspellbooks.render.ClientStaffItemExtensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

public class PyriumStaffClientExtensions
extends ClientStaffItemExtensions {
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return new PyriumStaffRenderer(Minecraft.getInstance().getItemRenderer(), Minecraft.getInstance().getEntityModels());
    }
}

