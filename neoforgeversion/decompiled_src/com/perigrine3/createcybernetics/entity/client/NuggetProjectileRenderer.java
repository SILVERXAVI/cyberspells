/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.ThrownItemRenderer
 */
package com.perigrine3.createcybernetics.entity.client;

import com.perigrine3.createcybernetics.entity.projectile.NuggetProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class NuggetProjectileRenderer
extends ThrownItemRenderer<NuggetProjectile> {
    public NuggetProjectileRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, 0.5f, false);
    }
}

