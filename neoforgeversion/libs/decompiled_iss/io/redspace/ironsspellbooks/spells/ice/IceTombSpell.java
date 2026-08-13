/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 */
package io.redspace.ironsspellbooks.spells.ice;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.ice_tomb.IceTombEntity;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class IceTombSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"ice_tomb");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.ICE_RESOURCE).setMaxLevel(8).setCooldownSeconds(30.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.healing", (Object[])new Object[]{Utils.stringTruncation(this.getHealing(spellLevel, caster), 1)}), Component.translatable((String)"ui.irons_spellbooks.duration", (Object[])new Object[]{Utils.timeFromTicks(this.getDuration(spellLevel, caster), 1)}));
    }

    public IceTombSpell() {
        this.manaCostPerLevel = 15;
        this.baseSpellPower = 5;
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
        IceTombEntity iceTombEntity = new IceTombEntity(world, (Entity)entity);
        iceTombEntity.moveTo(entity.position());
        iceTombEntity.setDeltaMovement(entity.getDeltaMovement());
        iceTombEntity.setHealing(this.getHealing(spellLevel, entity));
        iceTombEntity.setLifetime((int)this.getDuration(spellLevel, entity));
        world.addFreshEntity((Entity)iceTombEntity);
        entity.startRiding((Entity)iceTombEntity, true);
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    public float getDuration(int spellLevel, LivingEntity caster) {
        return 80.0f + (float)(spellLevel * 20) * Mth.sqrt((float)this.getEntityPowerMultiplier(caster));
    }

    public float getHealing(int spellLevel, LivingEntity caster) {
        return 1.0f * Mth.sqrt((float)this.getEntityPowerMultiplier(caster));
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.SELF_CAST_TWO_HANDS;
    }
}

