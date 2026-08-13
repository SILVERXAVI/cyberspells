/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.util.Mth
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.AbstractArrow
 *  net.minecraft.world.entity.projectile.AbstractArrow$Pickup
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.enchantment.Enchantment
 *  net.minecraft.world.item.enchantment.EnchantmentHelper
 *  net.minecraft.world.item.enchantment.Enchantments
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells.thrown_spear;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import javax.annotation.Nullable;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownSpear
extends AbstractArrow {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(ThrownSpear.class, (EntityDataSerializer)EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownSpear.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ID_CHANNELED = SynchedEntityData.defineId(ThrownSpear.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<ItemStack> ID_ITEM = SynchedEntityData.defineId(ThrownSpear.class, (EntityDataSerializer)EntityDataSerializers.ITEM_STACK);
    private boolean dealtDamage;
    public int clientSideReturnTridentTickCount;

    public ThrownSpear(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownSpear(Level level, ItemStack spearitem, double damage) {
        this((EntityType<? extends AbstractArrow>)((EntityType)EntityRegistry.THROWN_SPEAR.get()), level);
        this.setBaseDamage(damage);
        this.setWeaponItem(spearitem);
    }

    public boolean isChanneled() {
        return (Boolean)this.entityData.get(ID_CHANNELED);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_LOYALTY, (Object)0);
        builder.define(ID_FOIL, (Object)false);
        builder.define(ID_ITEM, (Object)ItemStack.EMPTY);
        builder.define(ID_CHANNELED, (Object)false);
    }

    protected void setPickupItemStack(ItemStack pickupItemStack) {
        if (!pickupItemStack.isEmpty()) {
            this.setWeaponItem(pickupItemStack);
        } else {
            super.setPickupItemStack(pickupItemStack);
        }
    }

    public void setWeaponItem(ItemStack itemStack) {
        this.entityData.set(ID_ITEM, (Object)itemStack);
        this.entityData.set(ID_LOYALTY, (Object)this.getLoyaltyFromItem(itemStack));
        this.entityData.set(ID_FOIL, (Object)itemStack.hasFoil());
        this.entityData.set(ID_CHANNELED, (Object)(Utils.getEnchantmentLevel(this.level, itemStack, (ResourceKey<Enchantment>)Enchantments.CHANNELING) > 0 ? 1 : 0));
    }

    protected ItemStack getPickupItem() {
        return this.getWeaponItem();
    }

    public ItemStack getPickupItemStackOrigin() {
        return this.getWeaponItem();
    }

    public ItemStack getWeaponItem() {
        return (ItemStack)this.entityData.get(ID_ITEM);
    }

    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }
        Entity entity = this.getOwner();
        byte loyalty = (Byte)this.entityData.get(ID_LOYALTY);
        if (loyalty > 0 && (this.dealtDamage || this.isNoPhysics()) && entity != null) {
            Player player;
            this.setNoPhysics(true);
            Vec3 vec3 = entity.getEyePosition().subtract(this.position());
            this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015 * (double)loyalty, this.getZ());
            if (this.level().isClientSide) {
                this.yOld = this.getY();
            }
            double d0 = 0.07 * (double)loyalty;
            this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(vec3.normalize().scale(d0)));
            if (this.clientSideReturnTridentTickCount == 0) {
                this.setDeltaMovement(Vec3.ZERO);
                this.playSound(SoundEvents.TRIDENT_RETURN, 10.0f, 1.0f);
            }
            if ((player = this.level.getPlayerByUUID(entity.getUUID())) != null && player.distanceToSqr((Entity)this) < Math.clamp((double)(this.getDeltaMovement().lengthSqr() * 3.0), (double)4.0, (double)25.0)) {
                this.playerTouch(player);
            }
            ++this.clientSideReturnTridentTickCount;
        }
        super.tick();
    }

    public boolean isFoil() {
        return (Boolean)this.entityData.get(ID_FOIL);
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }

    protected void onHit(HitResult result) {
        if (this.isChanneled() && !this.level.isClientSide && !this.dealtDamage) {
            this.playSound((SoundEvent)SoundRegistry.SPEAR_CHANNELING_STRIKE.get(), 6.0f, 0.9f + (float)Utils.random.nextInt(20) * 0.01f);
            MagicManager.spawnParticles(this.level, ParticleHelper.ELECTRICITY, this.getX(), this.getY(), this.getZ(), 75, 0.1, 0.1, 0.1, 2.0, true);
            MagicManager.spawnParticles(this.level, ParticleHelper.ELECTRICITY, this.getX(), this.getY(), this.getZ(), 75, 0.1, 0.1, 0.1, 0.5, false);
        }
        super.onHit(result);
        this.setSoundEvent(SoundEvents.TRIDENT_HIT_GROUND);
    }

    protected void onHitEntity(EntityHitResult result) {
        Entity victim = result.getEntity();
        float f = (float)this.getBaseDamage();
        Entity owner = this.getOwner();
        boolean channeled = this.isChanneled();
        DamageSource damagesource = channeled ? this.damageSources().source(ISSDamageTypes.LIGHTNING_MAGIC, (Entity)this, (Entity)(owner == null ? this : owner)) : this.damageSources().trident((Entity)this, (Entity)(owner == null ? this : owner));
        Level level = this.level();
        if (level instanceof ServerLevel) {
            ServerLevel serverlevel = (ServerLevel)level;
            f = EnchantmentHelper.modifyDamage((ServerLevel)serverlevel, (ItemStack)this.getWeaponItem(), (Entity)victim, (DamageSource)damagesource, (float)f);
        }
        if (channeled && owner instanceof LivingEntity) {
            LivingEntity livingOwner = (LivingEntity)owner;
            f *= (float)livingOwner.getAttributeValue(AttributeRegistry.LIGHTNING_SPELL_POWER);
        }
        this.dealtDamage = true;
        if (victim.hurt(damagesource, f)) {
            if (victim.getType() == EntityType.ENDERMAN) {
                return;
            }
            level = this.level();
            if (level instanceof ServerLevel) {
                ServerLevel serverlevel1 = (ServerLevel)level;
                EnchantmentHelper.doPostAttackEffectsWithItemSource((ServerLevel)serverlevel1, (Entity)victim, (DamageSource)damagesource, (ItemStack)this.getWeaponItem());
            }
            if (victim instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)victim;
                this.doKnockback(livingentity, damagesource);
                this.doPostHurtEffects(livingentity);
            }
        }
        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.03, -0.1, -0.03));
        this.playSound(SoundEvents.TRIDENT_HIT, 1.0f, 0.7f);
    }

    protected void hitBlockEnchantmentEffects(ServerLevel level, BlockHitResult hitResult, ItemStack stack) {
        LivingEntity livingentity;
        Vec3 vec3 = hitResult.getBlockPos().clampLocationWithin(hitResult.getLocation());
        Entity entity = this.getOwner();
        EnchantmentHelper.onHitBlock((ServerLevel)level, (ItemStack)stack, (LivingEntity)(entity instanceof LivingEntity ? (livingentity = (LivingEntity)entity) : null), (Entity)this, null, (Vec3)vec3, (BlockState)level.getBlockState(hitResult.getBlockPos()), p_348680_ -> this.kill());
    }

    protected boolean tryPickup(Player player) {
        if (!this.isRemoved() && this.getOwner() != null && this.ownedBy((Entity)player)) {
            byte loyalty = (Byte)this.entityData.get(ID_LOYALTY);
            if (player.hasInfiniteMaterials() && this.pickup == AbstractArrow.Pickup.CREATIVE_ONLY || !player.hasInfiniteMaterials() && this.pickup == AbstractArrow.Pickup.ALLOWED || this.pickup != AbstractArrow.Pickup.DISALLOWED && loyalty > 0) {
                player.getCooldowns().removeCooldown(this.getPickupItem().getItem());
                if (loyalty > 0) {
                    this.playSound((SoundEvent)SoundRegistry.SPEAR_RETURN.get());
                }
                return true;
            }
        }
        return false;
    }

    protected ItemStack getDefaultPickupItem() {
        return new ItemStack((ItemLike)Items.TRIDENT);
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    public void playerTouch(Player entity) {
        if (this.ownedBy((Entity)entity) || this.getOwner() == null) {
            super.playerTouch(entity);
        }
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.dealtDamage = compound.getBoolean("DealtDamage");
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("DealtDamage", this.dealtDamage);
        compound.put("item", this.getWeaponItem().save((HolderLookup.Provider)this.registryAccess()));
        compound.remove("weapon");
    }

    private byte getLoyaltyFromItem(ItemStack stack) {
        byte by;
        Level level = this.level();
        if (level instanceof ServerLevel) {
            ServerLevel serverlevel = (ServerLevel)level;
            by = (byte)Mth.clamp((int)EnchantmentHelper.getTridentReturnToOwnerAcceleration((ServerLevel)serverlevel, (ItemStack)stack, (Entity)this), (int)0, (int)127);
        } else {
            by = 0;
        }
        return by;
    }

    public void tickDespawn() {
        byte i = (Byte)this.entityData.get(ID_LOYALTY);
        if (this.pickup != AbstractArrow.Pickup.ALLOWED || i <= 0) {
            super.tickDespawn();
        }
    }

    protected float getWaterInertia() {
        return 0.99f;
    }

    public boolean shouldRender(double x, double y, double z) {
        return true;
    }
}

