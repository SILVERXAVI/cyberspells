/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.projectile.Projectile
 */
package io.redspace.ironsspellbooks.entity.spells.comet;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.fireball.FireballRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;

public class CometRenderer
extends FireballRenderer {
    private static final ResourceLocation BASE_TEXTURE = IronsSpellbooks.id("textures/entity/comet/comet.png");
    private static final ResourceLocation[] FIRE_TEXTURES = new ResourceLocation[]{IronsSpellbooks.id("textures/entity/comet/fire_1.png"), IronsSpellbooks.id("textures/entity/comet/fire_2.png"), IronsSpellbooks.id("textures/entity/comet/fire_3.png"), IronsSpellbooks.id("textures/entity/comet/fire_4.png")};

    public CometRenderer(EntityRendererProvider.Context context, float scale) {
        super(context, scale);
    }

    @Override
    public ResourceLocation getTextureLocation(Projectile entity) {
        return BASE_TEXTURE;
    }

    @Override
    public ResourceLocation getFireTextureLocation(Projectile entity) {
        int frame = entity.tickCount / 2 % FIRE_TEXTURES.length;
        return FIRE_TEXTURES[frame];
    }
}

