/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.enchantment.EnchantmentHelper
 *  net.minecraft.world.item.enchantment.Enchantments
 *  net.minecraft.world.level.GameRules
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.Level$ExplosionInteraction
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.event.entity.living.LivingDropsEvent
 *  net.neoforged.neoforge.event.level.ExplosionEvent$Start
 *  net.neoforged.neoforge.registries.DeferredHolder
 */
package com.maxwell.cyber_ware_port.common.entity;

import com.maxwell.cyber_ware_port.api.json.MobDataManager;
import com.maxwell.cyber_ware_port.common.entity.ICyberwareMob;
import com.maxwell.cyber_ware_port.common.entity.monster.cybercreeper.CyberCreeperEntity;
import com.maxwell.cyber_ware_port.common.entity.monster.cyberwither.CyberWitherBoss;
import com.maxwell.cyber_ware_port.common.entity.monster.cyberwitherskeleton.CyberWitherSkeletonEntity;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

@EventBusSubscriber(modid="cyber_ware_port")
public class EntitiesItemDropEvents {
    private static List<Item> CACHED_COMMON_POOL = null;
    private static List<Item> CACHED_HIGH_TIER_POOL = null;

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        int looting;
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof ICyberwareMob)) {
            return;
        }
        ICyberwareMob cyberMob = (ICyberwareMob)entity;
        if (CACHED_COMMON_POOL == null) {
            EntitiesItemDropEvents.initDropPools();
        }
        RandomSource random = entity.getRandom();
        LivingEntity attacker = null;
        Entity entity2 = event.getSource().getEntity();
        if (entity2 instanceof LivingEntity) {
            LivingEntity livingAttacker;
            attacker = livingAttacker = (LivingEntity)entity2;
        }
        int n = looting = attacker != null ? EnchantmentHelper.getEnchantmentLevel((Holder)attacker.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOOTING), (LivingEntity)attacker) : 0;
        if (entity instanceof CyberWitherBoss) {
            EntitiesItemDropEvents.addScavengedDrop(event, (Item)ModItems.INTERNAL_DEFIBRILLATOR.get());
            List<Item> witherPool = EntitiesItemDropEvents.generateMobDropPool(cyberMob);
            witherPool.remove(ModItems.INTERNAL_DEFIBRILLATOR.get());
            if (!witherPool.isEmpty()) {
                int dropCount = 5 + random.nextInt(4);
                for (int i = 0; i < dropCount; ++i) {
                    Item randomItem = witherPool.get(random.nextInt(witherPool.size()));
                    EntitiesItemDropEvents.addScavengedDrop(event, randomItem);
                }
            }
            return;
        }
        List<Item> pool = EntitiesItemDropEvents.generateMobDropPool(cyberMob);
        if (pool.isEmpty()) {
            return;
        }
        float dropChance = 0.25f + (float)looting * 0.05f;
        if (random.nextFloat() < dropChance) {
            Item selectedItem = pool.get(random.nextInt(pool.size()));
            EntitiesItemDropEvents.addScavengedDrop(event, selectedItem);
        }
        if (attacker != null && attacker.getMainHandItem().is((Item)ModItems.KATANA.get())) {
            float katanaChance = 0.5f + (float)looting * 0.1f;
            if (random.nextFloat() < katanaChance) {
                Item katanaDrop = pool.get(random.nextInt(pool.size()));
                EntitiesItemDropEvents.addScavengedDrop(event, katanaDrop);
            }
        }
        if (entity instanceof CyberWitherSkeletonEntity && random.nextFloat() < 0.05f + (float)looting * 0.02f) {
            EntitiesItemDropEvents.addScavengedDrop(event, (Item)ModItems.CYBER_WITHER_SKELETON_SKULL_ITEM.get());
        }
    }

    private static List<Item> generateMobDropPool(ICyberwareMob cyberMob) {
        ArrayList<Item> pool = new ArrayList<Item>();
        pool.addAll(CACHED_COMMON_POOL);
        LivingEntity entity = (LivingEntity)cyberMob;
        MobDataManager.MobData mobData = MobDataManager.MOB_DATA.get(entity.getType());
        boolean isHighTier = cyberMob.isHighTierMob();
        ArrayList<Item> specialDrops = new ArrayList<Item>(cyberMob.getSpecialDrops());
        ArrayList<Item> forbiddenDrops = new ArrayList<Item>(cyberMob.getForbiddenDrops());
        if (mobData != null) {
            isHighTier |= mobData.isHighTier;
            if (mobData.specialDrops != null) {
                pool.addAll(mobData.specialDrops);
            }
            if (mobData.forbiddenDrops != null) {
                forbiddenDrops.addAll(mobData.forbiddenDrops);
            }
        }
        if (isHighTier) {
            pool.addAll(CACHED_HIGH_TIER_POOL);
        }
        if (!specialDrops.isEmpty()) {
            pool.addAll(specialDrops);
            pool.addAll(specialDrops);
        }
        if (!forbiddenDrops.isEmpty()) {
            pool.removeAll(forbiddenDrops);
        }
        return pool;
    }

    private static void addScavengedDrop(LivingDropsEvent event, Item item) {
        ItemStack stack = new ItemStack((ItemLike)item);
        if (item instanceof CyberwareItem) {
            CyberwareItem cw = (CyberwareItem)item;
            cw.setPristine(stack, false);
        }
        event.getDrops().add(new ItemEntity(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), stack));
    }

    private static void initDropPools() {
        CACHED_COMMON_POOL = new ArrayList<Item>();
        CACHED_HIGH_TIER_POOL = new ArrayList<Item>();
        List<Item> highTierItems = List.of((Item)ModItems.INTERNAL_DEFIBRILLATOR.get(), (Item)ModItems.RAPID_FIRE_FLYWHEEL.get(), (Item)ModItems.LINEAR_ACTUATORS.get(), (Item)ModItems.CONSCIOUSNESS_TRANSMITTER.get(), (Item)ModItems.STEM_CELL_SYNTHESIZER.get());
        for (DeferredHolder entry : ModItems.ITEMS.getEntries()) {
            ResourceLocation id;
            Item item = (Item)entry.get();
            if (!(item instanceof CyberwareItem) || (id = BuiltInRegistries.ITEM.getKey((Object)item)).getPath().contains("body_part") || item == ModItems.CREATIVE_BATTERY.get()) continue;
            if (highTierItems.contains(item)) {
                CACHED_HIGH_TIER_POOL.add(item);
                continue;
            }
            CACHED_COMMON_POOL.add(item);
        }
    }

    @SubscribeEvent
    public static void onExplosionStart(ExplosionEvent.Start event) {
        Entity entity = event.getExplosion().getDirectSourceEntity();
        if (entity instanceof CyberCreeperEntity) {
            CyberCreeperEntity creeper = (CyberCreeperEntity)entity;
            if (creeper.isCausingCustomExplosion()) {
                return;
            }
            event.setCanceled(true);
            Level level = event.getLevel();
            if (level.isClientSide) {
                return;
            }
            boolean mobGriefing = level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
            Level.ExplosionInteraction interaction = mobGriefing ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.NONE;
            creeper.setCausingCustomExplosion(true);
            float baseRadius = creeper.isPowered() ? 6.0f : 3.0f;
            float finalRadius = baseRadius + 1.0f;
            long time = level.getDayTime() % 24000L;
            if (time >= 0L && time < 13000L) {
                finalRadius *= 1.5f;
            }
            level.explode((Entity)creeper, null, null, creeper.getX(), creeper.getY(), creeper.getZ(), finalRadius, false, interaction);
            creeper.discard();
        }
    }
}

