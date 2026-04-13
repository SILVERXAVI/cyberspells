/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.HumanoidArm
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeInstance
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.common.util.FakePlayer
 *  net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent$Start
 *  net.neoforged.neoforge.event.entity.player.AttackEntityEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$BreakSpeed
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$RightClickBlock
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$RightClickItem
 *  net.neoforged.neoforge.event.level.BlockEvent$BreakEvent
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 */
package com.perigrine3.createcybernetics.common.organs;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.common.damage.ModDamageSources;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.util.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class MissingOrganController {
    private static final String HOLO_SNAPSHOT_FLAG = "cc_holo_snapshot";
    private static final ResourceLocation NO_BONE_SPEED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"missing_bone_speed");
    private static final ResourceLocation NO_BONE_JUMP = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"missing_bone_jump");
    private static final ResourceLocation NO_LEFT_LEG_SPEED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"missing_left_leg_speed");
    private static final ResourceLocation NO_RIGHT_LEG_SPEED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"missing_right_leg_speed");
    private static final ResourceLocation NO_BOTH_LEGS_JUMP = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"missing_both_legs_jump");
    private static final String NO_LUNGS_AIR = "cc_no_lungs_air";
    private static final String FORCED_PRONE = "cc_forced_prone";
    private static final ResourceLocation NO_MUSCLE_SPEED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"missing_muscle_speed");
    private static final ResourceLocation NO_MUSCLE_ATTACK = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"missing_muscle_attack");
    private static final ResourceLocation NO_MUSCLE_JUMP = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"missing_muscle_jump");
    private static final String DEFAULTS_PATCHED = "cc_defaults_patched";

    private MissingOrganController() {
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        boolean inWater;
        boolean hasLegs;
        Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }
        if (player.getPersistentData().getBoolean(HOLO_SNAPSHOT_FLAG)) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        boolean hasBrain = data.hasAnyTagged(ModTags.Items.BRAIN_ITEMS, CyberwareSlot.BRAIN) || data.hasAnyInSlots(CyberwareSlot.BRAIN);
        boolean hasEyes = data.hasAnyTagged(ModTags.Items.EYE_ITEMS, CyberwareSlot.EYES);
        boolean hasHeart = data.hasAnyTagged(ModTags.Items.HEART_ITEMS, CyberwareSlot.HEART);
        boolean hasLungs = data.hasAnyTagged(ModTags.Items.LUNGS_ITEMS, CyberwareSlot.LUNGS);
        boolean hasLiver = data.hasAnyTagged(ModTags.Items.LIVER_ITEMS, CyberwareSlot.ORGANS);
        boolean hasIntestines = data.hasAnyTagged(ModTags.Items.INTESTINES_ITEMS, CyberwareSlot.ORGANS);
        boolean hasBone = data.hasAnyTagged(ModTags.Items.BONE_ITEMS, CyberwareSlot.BONE);
        boolean hasMuscle = data.hasAnyTagged(ModTags.Items.MUSCLE_ITEMS, CyberwareSlot.MUSCLE);
        boolean hasSkin = data.hasAnyTagged(ModTags.Items.SKIN_ITEMS, CyberwareSlot.SKIN);
        boolean hasLeftArm = data.hasAnyTagged(ModTags.Items.LEFTARM_ITEMS, CyberwareSlot.LARM);
        boolean hasRightArm = data.hasAnyTagged(ModTags.Items.RIGHTARM_ITEMS, CyberwareSlot.RARM);
        boolean hasLeftLeg = data.hasAnyTagged(ModTags.Items.LEFTLEG_ITEMS, CyberwareSlot.LLEG);
        boolean hasRightLeg = data.hasAnyTagged(ModTags.Items.RIGHTLEG_ITEMS, CyberwareSlot.RLEG);
        boolean hasArms = hasLeftArm || hasRightArm;
        boolean bl = hasLegs = hasLeftLeg || hasRightLeg;
        if (!player.getPersistentData().getBoolean(DEFAULTS_PATCHED)) {
            boolean hasBrainNow = MissingOrganController.hasAnyTagged(data, ModTags.Items.BRAIN_ITEMS, CyberwareSlot.BRAIN);
            if (!hasBrainNow) {
                data.resetToDefaultOrgans();
                data.setDirty();
                player.syncData(ModAttachments.CYBERWARE);
            }
            player.getPersistentData().putBoolean(DEFAULTS_PATCHED, true);
        }
        if (!hasBrain) {
            player.hurt(ModDamageSources.brainDamage(player.level(), (Entity)player, null), 500000.0f);
            return;
        }
        if (!hasEyes) {
            MissingOrganController.refreshEffect(player, (Holder<MobEffect>)MobEffects.BLINDNESS, 220, 1);
            MissingOrganController.refreshEffect(player, (Holder<MobEffect>)MobEffects.DARKNESS, 220, 0);
        } else {
            player.removeEffect(MobEffects.BLINDNESS);
            player.removeEffect(MobEffects.DARKNESS);
        }
        if (!hasHeart && player.tickCount % 20 == 0) {
            player.hurt(ModDamageSources.heartAttack(player.level(), (Entity)player, null), 4.0f);
        }
        boolean hasGills = data.hasSpecificItem((Item)ModItems.WETWARE_WATERBREATHINGLUNGS.get(), CyberwareSlot.LUNGS);
        boolean bl2 = inWater = player.isUnderWater() || player.isInWaterOrRain();
        if (!hasLungs) {
            boolean canBreatheHere;
            boolean bl3 = canBreatheHere = hasGills && inWater;
            if (canBreatheHere) {
                player.getPersistentData().remove(NO_LUNGS_AIR);
                player.setAirSupply(player.getMaxAirSupply());
            } else {
                int air;
                CompoundTag pd = player.getPersistentData();
                int n = air = pd.contains(NO_LUNGS_AIR, 3) ? pd.getInt(NO_LUNGS_AIR) : player.getAirSupply();
                if (--air <= -20) {
                    player.hurt(ModDamageSources.missingLungs(player.level(), (Entity)player, null), 2.0f);
                    air = 0;
                }
                pd.putInt(NO_LUNGS_AIR, air);
                player.setAirSupply(air);
            }
        } else {
            player.getPersistentData().remove(NO_LUNGS_AIR);
        }
        if (!hasLiver) {
            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 60, 1, true, false));
            player.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 0, true, false));
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0, true, false));
            player.hurt(ModDamageSources.liverFailure(player.level(), (Entity)player, null), 6.0f);
        }
        if (!hasBone) {
            MissingOrganController.applyOrRemoveModifier(player, (Holder<Attribute>)Attributes.MOVEMENT_SPEED, NO_BONE_SPEED, true, -0.85);
            MissingOrganController.applyOrRemoveModifier(player, (Holder<Attribute>)Attributes.JUMP_STRENGTH, NO_BONE_JUMP, true, -0.7);
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 40, 4, true, false));
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 1, true, false));
            player.hurt(ModDamageSources.boneless(player.level(), (Entity)player, null), 8.0f);
            MissingOrganController.forceProneLike(player);
        } else {
            MissingOrganController.applyOrRemoveModifier(player, (Holder<Attribute>)Attributes.MOVEMENT_SPEED, NO_BONE_SPEED, false, 0.0);
            MissingOrganController.applyOrRemoveModifier(player, (Holder<Attribute>)Attributes.JUMP_STRENGTH, NO_BONE_JUMP, false, 0.0);
            MissingOrganController.clearProneLike(player);
        }
        if (!hasMuscle) {
            MissingOrganController.applyOrRemoveModifier(player, (Holder<Attribute>)Attributes.MOVEMENT_SPEED, NO_MUSCLE_SPEED, true, -1.0);
            MissingOrganController.applyOrRemoveModifier(player, (Holder<Attribute>)Attributes.ATTACK_SPEED, NO_MUSCLE_ATTACK, true, -1.0);
            MissingOrganController.applyOrRemoveModifier(player, (Holder<Attribute>)Attributes.JUMP_STRENGTH, NO_MUSCLE_JUMP, true, -1.0);
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 40, 4, true, false));
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 1, true, false));
            player.hurt(ModDamageSources.missingMuscle(player.level(), (Entity)player, null), 11.0f);
            MissingOrganController.forceProneLike(player);
        } else {
            MissingOrganController.applyOrRemoveModifier(player, (Holder<Attribute>)Attributes.MOVEMENT_SPEED, NO_MUSCLE_SPEED, false, 0.0);
            MissingOrganController.applyOrRemoveModifier(player, (Holder<Attribute>)Attributes.ATTACK_SPEED, NO_MUSCLE_ATTACK, false, 0.0);
            MissingOrganController.applyOrRemoveModifier(player, (Holder<Attribute>)Attributes.JUMP_STRENGTH, NO_MUSCLE_JUMP, false, 0.0);
            MissingOrganController.clearProneLike(player);
        }
        if (!hasSkin && (player.horizontalCollision || player.verticalCollision || player.tickCount % 100 == 0)) {
            player.hurt(ModDamageSources.missingSkin(player.level(), (Entity)player, null), 2.0f);
        }
        if (!hasLeftArm) {
            MissingOrganController.enforceOffhandEmpty(player);
        }
        MissingOrganController.applyOrRemoveModifier(player, (Holder<Attribute>)Attributes.MOVEMENT_SPEED, NO_LEFT_LEG_SPEED, !hasLeftLeg, -1.0);
        MissingOrganController.applyOrRemoveModifier(player, (Holder<Attribute>)Attributes.MOVEMENT_SPEED, NO_RIGHT_LEG_SPEED, !hasRightLeg, -1.0);
        MissingOrganController.applyOrRemoveModifier(player, (Holder<Attribute>)Attributes.JUMP_STRENGTH, NO_BOTH_LEGS_JUMP, !hasLeftLeg && !hasRightLeg, -0.35);
        if (!hasLegs) {
            player.setSprinting(false);
        }
    }

    private static void forceProneLike(Player player) {
        CompoundTag pd = player.getPersistentData();
        pd.putBoolean(FORCED_PRONE, true);
        player.setSprinting(false);
        player.setSwimming(true);
        try {
            player.setPose(Pose.SWIMMING);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void refreshEffect(Player player, Holder<MobEffect> effect, int durationTicks, int amplifier) {
        MobEffectInstance cur = player.getEffect(effect);
        if (cur == null || cur.getDuration() < 40 || cur.getAmplifier() != amplifier) {
            player.addEffect(new MobEffectInstance(effect, durationTicks, amplifier, true, false, false));
        }
    }

    private static void clearProneLike(Player player) {
        CompoundTag pd = player.getPersistentData();
        if (!pd.getBoolean(FORCED_PRONE)) {
            return;
        }
        pd.remove(FORCED_PRONE);
        player.setSwimming(false);
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        boolean hasMuscle = data.hasAnyTagged(ModTags.Items.MUSCLE_ITEMS, CyberwareSlot.MUSCLE);
        if (!hasMuscle) {
            event.setNewSpeed(0.0f);
        }
    }

    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player == null) {
            return;
        }
        if (player.level().isClientSide) {
            return;
        }
        if (player instanceof FakePlayer) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        boolean hasMuscle = data.hasAnyTagged(ModTags.Items.MUSCLE_ITEMS, CyberwareSlot.MUSCLE);
        if (!hasMuscle) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onItemUse(LivingEntityUseItemEvent.Start event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof Player)) {
            return;
        }
        Player player = (Player)livingEntity;
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        if (!data.hasAnyTagged(ModTags.Items.INTESTINES_ITEMS, CyberwareSlot.ORGANS) && event.getItem().getFoodProperties((LivingEntity)player) != null) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        boolean mainHandWorks;
        Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        boolean hasMuscle = data.hasAnyTagged(ModTags.Items.MUSCLE_ITEMS, CyberwareSlot.MUSCLE);
        if (!hasMuscle) {
            event.setCanceled(true);
            return;
        }
        boolean hasLeftArm = data.hasAnyTagged(ModTags.Items.LEFTARM_ITEMS, CyberwareSlot.LARM);
        boolean hasRightArm = data.hasAnyTagged(ModTags.Items.RIGHTARM_ITEMS, CyberwareSlot.RARM);
        HumanoidArm mainArm = player.getMainArm();
        boolean bl = mainHandWorks = mainArm == HumanoidArm.RIGHT && hasRightArm || mainArm == HumanoidArm.LEFT && hasLeftArm;
        if (!mainHandWorks) {
            event.setCanceled(true);
        }
    }

    private static boolean hasAny(PlayerCyberwareData data, CyberwareSlot ... slots) {
        for (CyberwareSlot slot : slots) {
            InstalledCyberware[] arr = data.getAll().get((Object)slot);
            if (arr == null) continue;
            for (InstalledCyberware installed : arr) {
                if (installed == null || installed.getItem().isEmpty()) continue;
                return true;
            }
        }
        return false;
    }

    private static boolean hasAnyTagged(PlayerCyberwareData data, TagKey<Item> tag, CyberwareSlot ... slots) {
        for (CyberwareSlot slot : slots) {
            InstalledCyberware[] arr = data.getAll().get((Object)slot);
            if (arr == null) continue;
            for (InstalledCyberware installed : arr) {
                if (installed == null || installed.getItem().isEmpty() || !installed.getItem().is(tag)) continue;
                return true;
            }
        }
        return false;
    }

    private static void enforceOffhandEmpty(Player player) {
        ItemStack off = player.getOffhandItem();
        if (off.isEmpty()) {
            return;
        }
        ItemStack removed = off.copy();
        player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        if (!player.getInventory().add(removed)) {
            player.drop(removed, false);
        }
        player.inventoryMenu.broadcastChanges();
    }

    private static void applyOrRemoveModifier(Player player, Holder<Attribute> attribute, ResourceLocation id, boolean apply, double amount) {
        AttributeInstance attr = player.getAttribute(attribute);
        if (attr == null) {
            return;
        }
        if (apply) {
            if (attr.getModifier(id) == null) {
                attr.addTransientModifier(new AttributeModifier(id, amount, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            }
        } else {
            attr.removeModifier(id);
        }
    }

    private static boolean handWorks(Player player, InteractionHand hand, boolean hasLeftArm, boolean hasRightArm) {
        HumanoidArm mainArm = player.getMainArm();
        boolean usingRightArm = hand == InteractionHand.MAIN_HAND && mainArm == HumanoidArm.RIGHT || hand == InteractionHand.OFF_HAND && mainArm == HumanoidArm.LEFT;
        return usingRightArm ? hasRightArm : hasLeftArm;
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        boolean hasMuscle = data.hasAnyTagged(ModTags.Items.MUSCLE_ITEMS, CyberwareSlot.MUSCLE);
        if (!hasMuscle) {
            event.setCanceled(true);
            return;
        }
        boolean hasLeftArm = data.hasAnyTagged(ModTags.Items.LEFTARM_ITEMS, CyberwareSlot.LARM);
        boolean hasRightArm = data.hasAnyTagged(ModTags.Items.RIGHTARM_ITEMS, CyberwareSlot.RARM);
        if (!MissingOrganController.handWorks(player, event.getHand(), hasLeftArm, hasRightArm)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        boolean hasMuscle = data.hasAnyTagged(ModTags.Items.MUSCLE_ITEMS, CyberwareSlot.MUSCLE);
        if (!hasMuscle) {
            event.setCanceled(true);
            return;
        }
        boolean hasLeftArm = data.hasAnyTagged(ModTags.Items.LEFTARM_ITEMS, CyberwareSlot.LARM);
        boolean hasRightArm = data.hasAnyTagged(ModTags.Items.RIGHTARM_ITEMS, CyberwareSlot.RARM);
        if (!MissingOrganController.handWorks(player, event.getHand(), hasLeftArm, hasRightArm)) {
            event.setCanceled(true);
        }
    }
}

