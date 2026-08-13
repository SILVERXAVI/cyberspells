/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashMultimap
 *  com.google.common.collect.Multimap
 *  net.minecraft.core.Holder
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket
 *  net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket
 *  net.minecraft.server.level.ServerChunkCache
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.EquipmentSlotGroup
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.ItemAttributeModifiers
 *  net.minecraft.world.item.component.ItemAttributeModifiers$Entry
 *  net.minecraft.world.level.chunk.ChunkSource
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package io.redspace.ironsspellbooks.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.effect.IMobEffectEndCallback;
import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.chunk.ChunkSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={LivingEntity.class})
public abstract class LivingEntityMixin {
    @Unique
    private static final List<EquipmentSlot> handSlots = List.of(EquipmentSlot.OFFHAND, EquipmentSlot.MAINHAND);

    @Inject(method={"onEffectRemoved"}, at={@At(value="HEAD")})
    public void irons_spellbooks$onEffectRemoved(MobEffectInstance effectInstance, CallbackInfo ci) {
        LivingEntity self = (LivingEntity)this;
        if (!self.level.isClientSide) {
            Object object = effectInstance.getEffect().value();
            if (object instanceof IMobEffectEndCallback) {
                IMobEffectEndCallback mobEffect = (IMobEffectEndCallback)object;
                mobEffect.onEffectRemoved(self, effectInstance.getAmplifier());
            }
            if (effectInstance.getEffect().value() instanceof ISyncedMobEffect && (object = self.level.getChunkSource()) instanceof ServerChunkCache) {
                ServerChunkCache serverChunk = (ServerChunkCache)object;
                serverChunk.broadcast((Entity)self, (Packet)new ClientboundRemoveMobEffectPacket(self.getId(), effectInstance.getEffect()));
            }
        }
    }

    @Inject(method={"onEffectUpdated"}, at={@At(value="HEAD")})
    public void irons_spellbooks$onEffectUpdated(MobEffectInstance effectInstance, boolean forced, Entity entity, CallbackInfo ci) {
        ChunkSource chunkSource;
        LivingEntity self = (LivingEntity)this;
        if (!self.level.isClientSide && effectInstance.getEffect().value() instanceof ISyncedMobEffect && (chunkSource = self.level.getChunkSource()) instanceof ServerChunkCache) {
            ServerChunkCache serverChunk = (ServerChunkCache)chunkSource;
            serverChunk.broadcast((Entity)self, (Packet)new ClientboundUpdateMobEffectPacket(self.getId(), effectInstance, false));
        }
    }

    @Inject(method={"onEffectAdded"}, at={@At(value="HEAD")})
    public void irons_spellbooks$onEffectAdded(MobEffectInstance effectInstance, Entity entity, CallbackInfo ci) {
        ChunkSource chunkSource;
        LivingEntity self = (LivingEntity)this;
        if (!self.level.isClientSide && effectInstance.getEffect().value() instanceof ISyncedMobEffect && (chunkSource = self.level.getChunkSource()) instanceof ServerChunkCache) {
            ServerChunkCache serverChunk = (ServerChunkCache)chunkSource;
            serverChunk.broadcast((Entity)self, (Packet)new ClientboundUpdateMobEffectPacket(self.getId(), effectInstance, false));
        }
    }

    @Inject(method={"updateInvisibilityStatus"}, at={@At(value="TAIL")})
    public void irons_spellbooks$updateInvisibilityStatus(CallbackInfo ci) {
        LivingEntity self = (LivingEntity)this;
        if (self.hasEffect(MobEffectRegistry.TRUE_INVISIBILITY)) {
            self.setInvisible(true);
        }
    }

    @Inject(method={"isCurrentlyGlowing"}, at={@At(value="HEAD")}, cancellable=true)
    public void irons_spellbooks$isCurrentlyGlowing(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity)this;
        if (!self.level.isClientSide() && self.hasEffect(MobEffectRegistry.GUIDING_BOLT)) {
            cir.setReturnValue((Object)true);
        }
    }

    @Inject(method={"hurt"}, at={@At(value="RETURN")})
    public void irons_spellbooks$changeSummonHurtCredit(DamageSource damageSource, float amount, CallbackInfoReturnable<Boolean> cir) {
        IMagicSummon summon;
        IMagicSummon fromSummon;
        IMagicSummon summon2;
        Entity entity = damageSource.getDirectEntity();
        IMagicSummon iMagicSummon = entity instanceof IMagicSummon ? (summon2 = (IMagicSummon)entity) : (fromSummon = (entity = damageSource.getEntity()) instanceof IMagicSummon ? (summon = (IMagicSummon)entity) : null);
        if (fromSummon instanceof LivingEntity) {
            LivingEntity livingSummon = (LivingEntity)fromSummon;
            ((LivingEntity)this).setLastHurtByMob(livingSummon);
        }
    }

    @Shadow
    abstract ItemStack getLastHandItem(EquipmentSlot var1);

    @Inject(method={"collectEquipmentChanges"}, at={@At(value="RETURN")})
    public void handleEquipmentChanges(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> cir) {
        ItemStack offhandStack;
        Map changedEquipment = (Map)cir.getReturnValue();
        if (changedEquipment == null) {
            return;
        }
        LivingEntity self = (LivingEntity)this;
        ItemStack toStack = (ItemStack)changedEquipment.get(EquipmentSlot.MAINHAND);
        if (toStack == null) {
            return;
        }
        ItemStack fromStack = this.getLastHandItem(EquipmentSlot.MAINHAND);
        if (fromStack == (offhandStack = self.getOffhandItem())) {
            return;
        }
        if (!offhandStack.isEmpty() && offhandStack.has(ComponentRegistry.MULTIHAND_WEAPON)) {
            if (toStack.has(ComponentRegistry.MULTIHAND_WEAPON)) {
                if (!toStack.isEmpty()) {
                    self.getAttributes().removeAttributeModifiers(LivingEntityMixin.filterApplicableAttributes(offhandStack.getAttributeModifiers()));
                }
            } else if (fromStack.has(ComponentRegistry.MULTIHAND_WEAPON) && !offhandStack.isEmpty()) {
                self.getAttributes().addTransientAttributeModifiers(LivingEntityMixin.filterApplicableAttributes(offhandStack.getAttributeModifiers()));
            }
        }
    }

    @Unique
    private static Multimap<Holder<Attribute>, AttributeModifier> filterApplicableAttributes(ItemAttributeModifiers modifiers) {
        List<ItemAttributeModifiers.Entry> list = modifiers.modifiers().stream().filter(entry -> entry.slot() == EquipmentSlotGroup.MAINHAND).toList();
        HashMultimap map = HashMultimap.create();
        for (ItemAttributeModifiers.Entry entry2 : list) {
            Predicate<Holder<Attribute>> predicate = (Boolean)ServerConfigs.APPLY_ALL_MULTIHAND_ATTRIBUTES.get() != false ? Utils.NON_BASE_ATTRIBUTES : Utils.ONLY_MAGIC_ATTRIBUTES;
            if (!predicate.test((Holder<Attribute>)entry2.attribute())) continue;
            map.put((Object)entry2.attribute(), (Object)entry2.modifier());
        }
        return map;
    }
}

