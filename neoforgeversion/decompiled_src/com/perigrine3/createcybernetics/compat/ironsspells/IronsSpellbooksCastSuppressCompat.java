/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.ModList
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.EntityJoinLevelEvent
 */
package com.perigrine3.createcybernetics.compat.ironsspells;

import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class IronsSpellbooksCastSuppressCompat {
    public static final String MODID = "irons_spellbooks";
    public static final String SUPPRESSED_UNTIL_TAG = "cc_spelljammer_suppressed_until";
    private static final Set<String> SPELL_ENTITY_PATHS = Set.of("acid_orb", "ball_lightning", "black_hole", "blood_needle", "blood_slash", "comet", "cone_of_cold", "creeper_head", "devour_jaw", "dragon_breath", "eldritch_blast", "electrocute", "fiery_dagger", "fire_arrow", "fire_breath", "fireball", "firebolt", "firefly_swarm", "guiding_bolt", "gust", "ice_block", "ice_spike", "ice_tomb", "icicle", "lightning_lance", "magic_arrow", "magic_missile", "magma_ball", "poison_arrow", "poison_breath", "poison_cloud", "portal", "ray_of_frost", "root", "shield", "skull_projectile", "small_magic_arrow", "snowball", "spectral_hammer", "summoned_weapons", "sunbeam", "target_area", "thrown_item", "thrown_spear", "thunderstep", "void_tentacle", "wall_of_fire", "wisp", "aoe_entity", "arrow_volley", "chain_lightning", "cone_part", "earthquake_aoe", "echoing_strike", "extended_evoker_fang", "extended_firework_rocket", "extended_lightning_bolt", "fire_eruption_aoe", "healing_aoe", "lightning_strike", "shield_part", "stomp_aoe", "wither_skull_projectile");

    private IronsSpellbooksCastSuppressCompat() {
    }

    public static boolean isLoaded() {
        return ModList.get().isLoaded(MODID);
    }

    public static boolean isSuppressed(LivingEntity e) {
        if (e == null) {
            return false;
        }
        long until = e.getPersistentData().getLong(SUPPRESSED_UNTIL_TAG);
        return until > e.level().getGameTime();
    }

    private static boolean isTargetedSpellEntity(Entity e) {
        ResourceLocation typeId = e.getType().builtInRegistryHolder().key().location();
        if (!MODID.equals(typeId.getNamespace())) {
            return false;
        }
        return SPELL_ENTITY_PATHS.contains(typeId.getPath());
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        boolean nearSuppressed;
        LivingEntity caster;
        Projectile proj;
        Entity owner;
        if (!IronsSpellbooksCastSuppressCompat.isLoaded()) {
            return;
        }
        Level level = event.getLevel();
        if (level.isClientSide) {
            return;
        }
        Entity spawned = event.getEntity();
        if (!IronsSpellbooksCastSuppressCompat.isTargetedSpellEntity(spawned)) {
            return;
        }
        if (spawned instanceof Projectile && (owner = (proj = (Projectile)spawned).getOwner()) instanceof LivingEntity && IronsSpellbooksCastSuppressCompat.isSuppressed(caster = (LivingEntity)owner)) {
            event.setCanceled(true);
            return;
        }
        double suppressionRadius = 30.0;
        boolean bl = nearSuppressed = !level.getEntitiesOfClass(LivingEntity.class, spawned.getBoundingBox().inflate(30.0), IronsSpellbooksCastSuppressCompat::isSuppressed).isEmpty();
        if (nearSuppressed) {
            event.setCanceled(true);
        }
    }
}

