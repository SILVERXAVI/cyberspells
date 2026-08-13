/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 */
package io.redspace.ironsspellbooks.spells.lightning;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.entity.spells.ChainLightning;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ChainLightningSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"chain_lightning");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.LIGHTNING_RESOURCE).setMaxLevel(10).setCooldownSeconds(20.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.damage", (Object[])new Object[]{Utils.stringTruncation(this.getDamage(spellLevel, caster), 2)}), Component.translatable((String)"ui.irons_spellbooks.max_victims", (Object[])new Object[]{this.getMaxConnections(spellLevel, caster)}), Component.translatable((String)"ui.irons_spellbooks.distance", (Object[])new Object[]{Utils.stringTruncation(this.getRange(spellLevel, caster), 1)}));
    }

    public ChainLightningSpell() {
        this.manaCostPerLevel = 7;
        this.baseSpellPower = 6;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 25;
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
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 32, 0.35f);
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        TargetEntityCastData targetData;
        LivingEntity targetEntity;
        ICastData iCastData = playerMagicData.getAdditionalCastData();
        if (iCastData instanceof TargetEntityCastData && (targetEntity = (targetData = (TargetEntityCastData)iCastData).getTarget((ServerLevel)world)) != null) {
            ChainLightning chainLightning = new ChainLightning(world, (Entity)entity, (Entity)targetEntity);
            chainLightning.setDamage(this.getDamage(spellLevel, entity));
            chainLightning.range = this.getRange(spellLevel, entity);
            chainLightning.maxConnections = this.getMaxConnections(spellLevel, entity);
            world.addFreshEntity((Entity)chainLightning);
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    public float getDamage(int spellLevel, LivingEntity caster) {
        return this.getSpellPower(spellLevel, (Entity)caster);
    }

    public int getMaxConnections(int spellLevel, LivingEntity caster) {
        return 3 + spellLevel;
    }

    public float getRange(int spellLevel, LivingEntity caster) {
        return 1.0f + this.getSpellPower(spellLevel, (Entity)caster) * 0.5f;
    }
}

