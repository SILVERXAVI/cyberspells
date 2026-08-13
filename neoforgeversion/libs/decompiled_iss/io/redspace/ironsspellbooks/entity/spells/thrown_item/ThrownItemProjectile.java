/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.enchantment.EnchantmentHelper
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.entity.spells.thrown_item;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public class ThrownItemProjectile
extends AbstractMagicProjectile {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(ThrownItemProjectile.class, (EntityDataSerializer)EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Float> DATA_SCALE = SynchedEntityData.defineId(ThrownItemProjectile.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);

    public float getScale() {
        return ((Float)this.entityData.get(DATA_SCALE)).floatValue();
    }

    public void setScale(float scale) {
        this.entityData.set(DATA_SCALE, (Object)Float.valueOf(scale));
    }

    public ThrownItemProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ThrownItemProjectile(Level level, ItemStack itemStack) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.THROWN_ITEM.get()), level);
        this.setThrownItem(itemStack);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_ITEM, (Object)ItemStack.EMPTY);
        pBuilder.define(DATA_SCALE, (Object)Float.valueOf(1.0f));
    }

    public ItemStack getThrownItem() {
        return (ItemStack)this.entityData.get(DATA_ITEM);
    }

    public void setThrownItem(ItemStack stack) {
        this.entityData.set(DATA_ITEM, (Object)stack);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        ItemStack item = this.getThrownItem();
        if (!item.isEmpty()) {
            tag.put("item", item.save((HolderLookup.Provider)this.level.registryAccess()));
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("item")) {
            this.setThrownItem(ItemStack.parseOptional((HolderLookup.Provider)this.level.registryAccess(), (CompoundTag)tag.getCompound("item")));
        }
    }

    @Override
    public void trailParticles() {
    }

    @Nullable
    public ItemStack getWeaponItem() {
        return this.getThrownItem();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        Level level;
        super.onHitEntity(pResult);
        ItemStack item = this.getThrownItem();
        double damage = this.getDamage();
        Entity target = pResult.getEntity();
        SpellDamageSource damageSource = SpellRegistry.THROW_SPELL.get().getDamageSource((Entity)this, this.getOwner());
        if (DamageSources.applyDamage(target, (float)damage, damageSource) && !item.isEmpty() && (level = this.level) instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            EnchantmentHelper.doPostAttackEffectsWithItemSource((ServerLevel)serverLevel, (Entity)target, (DamageSource)damageSource, (ItemStack)item);
        }
        this.discard();
    }

    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.discard();
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.level, (ParticleOptions)ParticleTypes.CRIT, x, y, z, 25, 0.1, 0.1, 0.1, 0.5, true);
    }

    @Override
    public float getSpeed() {
        return 1.5f;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.of(BuiltInRegistries.SOUND_EVENT.wrapAsHolder((Object)SoundEvents.TRIDENT_HIT_GROUND));
    }
}

