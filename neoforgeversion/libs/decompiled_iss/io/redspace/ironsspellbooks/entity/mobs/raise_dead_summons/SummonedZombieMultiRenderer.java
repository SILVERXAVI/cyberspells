/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.ZombieRenderer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.monster.Zombie
 */
package io.redspace.ironsspellbooks.entity.mobs.raise_dead_summons;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.entity.mobs.HumanoidRenderer;
import io.redspace.ironsspellbooks.entity.mobs.SummonedZombie;
import io.redspace.ironsspellbooks.entity.mobs.raise_dead_summons.SummonedZombieModel;
import io.redspace.ironsspellbooks.render.SpellTargetingLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;

public class SummonedZombieMultiRenderer
extends HumanoidRenderer<SummonedZombie> {
    ZombieRenderer vanillaRenderer;

    public SummonedZombieMultiRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SummonedZombieModel());
        this.vanillaRenderer = new ZombieRenderer(this, pContext){

            public ResourceLocation getTextureLocation(Zombie pEntity) {
                return SummonedZombieModel.TEXTURE;
            }
        };
        this.vanillaRenderer.addLayer(new SpellTargetingLayer.Vanilla(this.vanillaRenderer));
    }

    @Override
    public void render(SummonedZombie entity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (entity.isAnimatingRise()) {
            super.render(entity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        } else {
            this.vanillaRenderer.render((LivingEntity)entity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        }
    }
}

