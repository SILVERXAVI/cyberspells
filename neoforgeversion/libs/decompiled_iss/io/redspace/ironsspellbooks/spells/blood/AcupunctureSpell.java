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
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.spells.blood;

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
import io.redspace.ironsspellbooks.entity.spells.blood_needle.BloodNeedle;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AcupunctureSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"acupuncture");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.RARE).setSchoolResource(SchoolRegistry.BLOOD_RESOURCE).setMaxLevel(10).setCooldownSeconds(20.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.damage", (Object[])new Object[]{Utils.stringTruncation(this.getDamage(spellLevel, caster), 2)}), Component.translatable((String)"ui.irons_spellbooks.projectile_count", (Object[])new Object[]{this.getCount(spellLevel, caster)}));
    }

    public AcupunctureSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 0;
        this.castTime = 0;
        this.baseManaCost = 25;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 32, 0.15f);
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        TargetEntityCastData targetData;
        LivingEntity targetEntity;
        ICastData iCastData = playerMagicData.getAdditionalCastData();
        if (iCastData instanceof TargetEntityCastData && (targetEntity = (targetData = (TargetEntityCastData)iCastData).getTarget((ServerLevel)world)) != null) {
            int count = this.getCount(spellLevel, entity);
            float damage = this.getDamage(spellLevel, entity);
            Vec3 center = targetEntity.position().add(0.0, (double)(targetEntity.getEyeHeight() / 2.0f), 0.0);
            float degreesPerNeedle = 360.0f / (float)count;
            for (int i = 0; i < count; ++i) {
                Vec3 offset = new Vec3(0.0, Math.random(), 0.55).normalize().scale((double)(targetEntity.getBbWidth() + 2.75f)).yRot(degreesPerNeedle * (float)i * ((float)Math.PI / 180));
                Vec3 spawn = center.add(offset);
                Vec3 motion = center.subtract(spawn).normalize();
                BloodNeedle needle = new BloodNeedle(world, entity);
                needle.moveTo(spawn);
                needle.shoot(motion.scale((double)0.35f));
                needle.setDamage(damage);
                needle.setScale(0.4f);
                world.addFreshEntity((Entity)needle);
            }
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private int getCount(int spellLevel, LivingEntity caster) {
        return (int)((float)(4 + spellLevel) * this.getSpellPower(spellLevel, (Entity)caster));
    }

    private float getDamage(int spellLevel, LivingEntity caster) {
        return 1.0f + this.getSpellPower(spellLevel, (Entity)caster);
    }
}

