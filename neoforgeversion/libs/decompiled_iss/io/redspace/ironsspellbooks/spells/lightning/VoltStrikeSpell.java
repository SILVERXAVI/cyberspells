/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.util.Mth
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.MoverType
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.spells.lightning;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.player.SpinAttackType;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class VoltStrikeSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"volt_strike");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.LIGHTNING_RESOURCE).setMaxLevel(10).setCooldownSeconds(10.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.damage", (Object[])new Object[]{this.getDamage(spellLevel, caster)}));
    }

    public VoltStrikeSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 30;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        entity.hasImpulse = true;
        float multiplier = (15.0f + this.getSpellPower(spellLevel, (Entity)entity)) / 20.0f;
        Vec3 forward = entity.getLookAngle();
        double upwardness = forward.dot(new Vec3(0.0, 1.0, 0.0));
        double remap = 1.0 - Math.max(0.0, upwardness) * (double)0.6f;
        Vec3 impulse = forward.scale((double)(3.0f * multiplier)).multiply(1.0, remap, 1.0);
        if (entity.onGround()) {
            if (entity instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)entity;
                serverPlayer.connection.teleport(serverPlayer.getX(), serverPlayer.getY() + 1.0, serverPlayer.getZ(), serverPlayer.getYRot(), serverPlayer.getXRot());
            } else {
                entity.move(MoverType.SELF, new Vec3(0.0, 1.1999999284744263, 0.0));
            }
            impulse.add(0.0, 0.5, 0.0);
        } else {
            impulse.add(0.0, 0.25, 0.0);
        }
        entity.setDeltaMovement(new Vec3(Mth.lerp((double)0.75, (double)entity.getDeltaMovement().x, (double)impulse.x), Mth.lerp((double)0.75, (double)entity.getDeltaMovement().y, (double)impulse.y), Mth.lerp((double)0.75, (double)entity.getDeltaMovement().z, (double)impulse.z)));
        entity.hurtMarked = true;
        entity.addEffect(new MobEffectInstance(MobEffectRegistry.VOLT_STRIKE, 10, this.getDamage(spellLevel, entity), false, false, false));
        entity.invulnerableTime = 20;
        playerMagicData.getSyncedData().setSpinAttackType(SpinAttackType.LIGHTNING);
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private int getDamage(int spellLevel, LivingEntity caster) {
        return (int)(5.0f + this.getSpellPower(spellLevel, (Entity)caster));
    }
}

