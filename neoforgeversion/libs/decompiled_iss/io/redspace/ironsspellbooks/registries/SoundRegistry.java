/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package io.redspace.ironsspellbooks.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SoundRegistry {
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create((ResourceKey)Registries.SOUND_EVENT, (String)"irons_spellbooks");
    public static DeferredHolder<SoundEvent, SoundEvent> FORCE_IMPACT = SoundRegistry.registerSoundEvent("force_impact");
    public static DeferredHolder<SoundEvent, SoundEvent> ICE_IMPACT = SoundRegistry.registerSoundEvent("ice_impact");
    public static DeferredHolder<SoundEvent, SoundEvent> FIRE_IMPACT = SoundRegistry.registerSoundEvent("entity.generic.fire_impact");
    public static DeferredHolder<SoundEvent, SoundEvent> MAGIC_SPELL_REVERSE_3 = SoundRegistry.registerSoundEvent("magic_spell_reverse_3");
    public static DeferredHolder<SoundEvent, SoundEvent> ARIAL_SUMMONING_5_CUSTOM_1 = SoundRegistry.registerSoundEvent("arial_summoning_5_custom_1");
    public static DeferredHolder<SoundEvent, SoundEvent> DARK_MAGIC_BUFF_03_CUSTOM_1 = SoundRegistry.registerSoundEvent("dark_magic_buff_03_custom_1");
    public static DeferredHolder<SoundEvent, SoundEvent> DARK_SPELL_02 = SoundRegistry.registerSoundEvent("dark_spell_02");
    public static DeferredHolder<SoundEvent, SoundEvent> LIGHTNING_WOOSH_01 = SoundRegistry.registerSoundEvent("lightning_woosh_01");
    public static DeferredHolder<SoundEvent, SoundEvent> HEARTSTOP_CAST = SoundRegistry.registerSoundEvent("heartstop_cast");
    public static DeferredHolder<SoundEvent, SoundEvent> LIGHTNING_LANCE_CAST = SoundRegistry.registerSoundEvent("lightning_lance_cast");
    public static DeferredHolder<SoundEvent, SoundEvent> MAGIC_ARROW_RELEASE = SoundRegistry.registerSoundEvent("magic_arrow_release");
    public static DeferredHolder<SoundEvent, SoundEvent> MAGIC_ARROW_CHARGE = SoundRegistry.registerSoundEvent("magic_arrow_charge");
    public static DeferredHolder<SoundEvent, SoundEvent> FROST_STEP = SoundRegistry.registerSoundEvent("frost_step");
    public static DeferredHolder<SoundEvent, SoundEvent> ABYSSAL_TELEPORT = SoundRegistry.registerSoundEvent("abyssal_teleport");
    public static DeferredHolder<SoundEvent, SoundEvent> ABYSSAL_SHROUD = SoundRegistry.registerSoundEvent("cast.abyssal_shroud");
    public static DeferredHolder<SoundEvent, SoundEvent> BLOOD_STEP = SoundRegistry.registerSoundEvent("cast.blood_step");
    public static DeferredHolder<SoundEvent, SoundEvent> FIRE_BREATH_LOOP = SoundRegistry.registerSoundEvent("loop.fire_breath");
    public static DeferredHolder<SoundEvent, SoundEvent> ELECTROCUTE_LOOP = SoundRegistry.registerSoundEvent("loop.electrocute");
    public static DeferredHolder<SoundEvent, SoundEvent> CONE_OF_COLD_LOOP = SoundRegistry.registerSoundEvent("loop.cone_of_cold");
    public static DeferredHolder<SoundEvent, SoundEvent> CLOUD_OF_REGEN_LOOP = SoundRegistry.registerSoundEvent("loop.cloud_of_regen");
    public static DeferredHolder<SoundEvent, SoundEvent> RAISE_DEAD_START = SoundRegistry.registerSoundEvent("cast.raise_dead.start");
    public static DeferredHolder<SoundEvent, SoundEvent> RAISE_DEAD_FINISH = SoundRegistry.registerSoundEvent("cast.raise_dead.finish");
    public static DeferredHolder<SoundEvent, SoundEvent> VOID_TENTACLES_START = SoundRegistry.registerSoundEvent("cast.void_tentacles.start");
    public static DeferredHolder<SoundEvent, SoundEvent> VOID_TENTACLES_FINISH = SoundRegistry.registerSoundEvent("cast.void_tentacles.finish");
    public static DeferredHolder<SoundEvent, SoundEvent> VOID_TENTACLES_LEAVE = SoundRegistry.registerSoundEvent("entity.void_tentacles.retreat");
    public static DeferredHolder<SoundEvent, SoundEvent> VOID_TENTACLES_AMBIENT = SoundRegistry.registerSoundEvent("entity.void_tentacles.ambient");
    public static DeferredHolder<SoundEvent, SoundEvent> ICE_BLOCK_CAST = SoundRegistry.registerSoundEvent("cast.ice_block");
    public static DeferredHolder<SoundEvent, SoundEvent> ICE_BLOCK_IMPACT = SoundRegistry.registerSoundEvent("entity.ice_block.impact");
    public static DeferredHolder<SoundEvent, SoundEvent> RAY_OF_SIPHONING = SoundRegistry.registerSoundEvent("loop.ray_of_siphoning");
    public static DeferredHolder<SoundEvent, SoundEvent> FIREBALL_START = SoundRegistry.registerSoundEvent("cast.fireball");
    public static DeferredHolder<SoundEvent, SoundEvent> ACID_ORB_CHARGE = SoundRegistry.registerSoundEvent("spell.acid_orb.charge");
    public static DeferredHolder<SoundEvent, SoundEvent> ACID_ORB_CAST = SoundRegistry.registerSoundEvent("spell.acid_orb.cast");
    public static DeferredHolder<SoundEvent, SoundEvent> ACID_ORB_IMPACT = SoundRegistry.registerSoundEvent("entity.acid_orb.impact");
    public static DeferredHolder<SoundEvent, SoundEvent> POISON_ARROW_CHARGE = SoundRegistry.registerSoundEvent("spell.poison_arrow.charge");
    public static DeferredHolder<SoundEvent, SoundEvent> POISON_ARROW_CAST = SoundRegistry.registerSoundEvent("spell.poison_arrow.cast");
    public static DeferredHolder<SoundEvent, SoundEvent> POISON_BREATH_LOOP = SoundRegistry.registerSoundEvent("spell.poison_breath.loop");
    public static DeferredHolder<SoundEvent, SoundEvent> ROOT_EMERGE = SoundRegistry.registerSoundEvent("entity.root.emerge");
    public static DeferredHolder<SoundEvent, SoundEvent> BLACK_HOLE_CHARGE = SoundRegistry.registerSoundEvent("spell.black_hole.charge");
    public static DeferredHolder<SoundEvent, SoundEvent> BLACK_HOLE_CAST = SoundRegistry.registerSoundEvent("spell.black_hole.cast");
    public static DeferredHolder<SoundEvent, SoundEvent> BLACK_HOLE_LOOP = SoundRegistry.registerSoundEvent("entity.black_hole.loop");
    public static DeferredHolder<SoundEvent, SoundEvent> POISON_SPLASH_BEGIN = SoundRegistry.registerSoundEvent("spell.poison_splash.begin");
    public static DeferredHolder<SoundEvent, SoundEvent> BLIGHT_BEGIN = SoundRegistry.registerSoundEvent("spell.blight.begin");
    public static DeferredHolder<SoundEvent, SoundEvent> SPIDER_ASPECT_CAST = SoundRegistry.registerSoundEvent("spell.spider_aspect.cast");
    public static DeferredHolder<SoundEvent, SoundEvent> BLOOD_NEEDLE_IMPACT = SoundRegistry.registerSoundEvent("entity.blood_needle.impact");
    public static DeferredHolder<SoundEvent, SoundEvent> FIRE_BOMB_CHARGE = SoundRegistry.registerSoundEvent("spell.fire_bomb.charge");
    public static DeferredHolder<SoundEvent, SoundEvent> FIRE_BOMB_CAST = SoundRegistry.registerSoundEvent("spell.fire_bomb.cast");
    public static DeferredHolder<SoundEvent, SoundEvent> GUST_CHARGE = SoundRegistry.registerSoundEvent("spell.gust.charge");
    public static DeferredHolder<SoundEvent, SoundEvent> GUST_CAST = SoundRegistry.registerSoundEvent("spell.gust.cast");
    public static DeferredHolder<SoundEvent, SoundEvent> GUIDING_BOLT_IMPACT = SoundRegistry.registerSoundEvent("entity.guiding_bolt.impact");
    public static DeferredHolder<SoundEvent, SoundEvent> GUIDING_BOLT_CAST = SoundRegistry.registerSoundEvent("spell.guiding_bolt.cast");
    public static DeferredHolder<SoundEvent, SoundEvent> CHAIN_LIGHTNING_CHAIN = SoundRegistry.registerSoundEvent("entity.chain_lightning.lightning_chain");
    public static DeferredHolder<SoundEvent, SoundEvent> DEVOUR_BITE = SoundRegistry.registerSoundEvent("entity.devour_jaw.bite");
    public static DeferredHolder<SoundEvent, SoundEvent> KEEPER_SWING = SoundRegistry.registerSoundEvent("entity.citadel_keeper.swing");
    public static DeferredHolder<SoundEvent, SoundEvent> KEEPER_STEP = SoundRegistry.registerSoundEvent("entity.citadel_keeper.step");
    public static DeferredHolder<SoundEvent, SoundEvent> KEEPER_DEATH = SoundRegistry.registerSoundEvent("entity.citadel_keeper.death");
    public static DeferredHolder<SoundEvent, SoundEvent> KEEPER_HURT = SoundRegistry.registerSoundEvent("entity.citadel_keeper.hurt");
    public static DeferredHolder<SoundEvent, SoundEvent> KEEPER_SWORD_IMPACT = SoundRegistry.registerSoundEvent("entity.citadel_keeper.sword_impact");
    public static DeferredHolder<SoundEvent, SoundEvent> KEEPER_IDLE = SoundRegistry.registerSoundEvent("entity.citadel_keeper.idle");
    public static DeferredHolder<SoundEvent, SoundEvent> OAKSKIN_CAST = SoundRegistry.registerSoundEvent("spell.oakskin.cast");
    public static DeferredHolder<SoundEvent, SoundEvent> TOUCH_DIG_CAST = SoundRegistry.registerSoundEvent("spell.touch_dig.cast");
    public static DeferredHolder<SoundEvent, SoundEvent> EARTHQUAKE_LOOP = SoundRegistry.registerSoundEvent("entity.earthquake_aoe.loop");
    public static DeferredHolder<SoundEvent, SoundEvent> EARTHQUAKE_IMPACT = SoundRegistry.registerSoundEvent("entity.earthquake_aoe.impact");
    public static DeferredHolder<SoundEvent, SoundEvent> EARTHQUAKE_CAST = SoundRegistry.registerSoundEvent("spell.earthquake.cast");
    public static DeferredHolder<SoundEvent, SoundEvent> FIREFLY_SWARM_IDLE = SoundRegistry.registerSoundEvent("entity.firefly_swarm.idle");
    public static DeferredHolder<SoundEvent, SoundEvent> FIREFLY_SWARM_ATTACK = SoundRegistry.registerSoundEvent("entity.firefly_swarm.attack");
    public static DeferredHolder<SoundEvent, SoundEvent> FIREFLY_SPELL_PREPARE = SoundRegistry.registerSoundEvent("spell.firefly_swarm.prepare");
    public static DeferredHolder<SoundEvent, SoundEvent> RAY_OF_FROST = SoundRegistry.registerSoundEvent("spell.ray_of_frost.cast");
    public static DeferredHolder<SoundEvent, SoundEvent> SONIC_BOOM = SoundRegistry.registerSoundEvent("spell.sonic_boom.cast");
    public static DeferredHolder<SoundEvent, SoundEvent> DIVINE_SMITE_WINDUP = SoundRegistry.registerSoundEvent("spell.divine_smite.windup");
    public static DeferredHolder<SoundEvent, SoundEvent> DIVINE_SMITE_CAST = SoundRegistry.registerSoundEvent("spell.divine_smite.cast");
    public static DeferredHolder<SoundEvent, SoundEvent> TELEKINESIS_CAST = SoundRegistry.registerSoundEvent("spell.telekinesis.cast");
    public static DeferredHolder<SoundEvent, SoundEvent> TELEKINESIS_LOOP = SoundRegistry.registerSoundEvent("spell.telekinesis.loop");
    public static DeferredHolder<SoundEvent, SoundEvent> PLANAR_SIGHT_CAST = SoundRegistry.registerSoundEvent("spell.planar_sight.cast");
    public static DeferredHolder<SoundEvent, SoundEvent> HEAT_SURGE_PREPARE = SoundRegistry.registerSoundEvent("spell.heat_surge.prepare");
    public static DeferredHolder<SoundEvent, SoundEvent> FROSTWAVE_PREPARE = SoundRegistry.registerSoundEvent("spell.frostwave.prepare");
    public static DeferredHolder<SoundEvent, SoundEvent> ARROW_VOLLEY_PREPARE = SoundRegistry.registerSoundEvent("spell.arrow_volley.prepare");
    public static DeferredHolder<SoundEvent, SoundEvent> BOW_SHOOT = SoundRegistry.registerSoundEvent("bow_shoot");
    public static DeferredHolder<SoundEvent, SoundEvent> RECALL_PREPARE = SoundRegistry.registerSoundEvent("spell.recall.prepare");
    public static DeferredHolder<SoundEvent, SoundEvent> ELDRITCH_BLAST = SoundRegistry.registerSoundEvent("spell.eldritch_blast.cast");
    public static DeferredHolder<SoundEvent, SoundEvent> FLAMING_STRIKE_UPSWING = SoundRegistry.registerSoundEvent("spell.flaming_strike.begin");
    public static DeferredHolder<SoundEvent, SoundEvent> FLAMING_STRIKE_SWING = SoundRegistry.registerSoundEvent("spell.flaming_strike.cast");
    public static DeferredHolder<SoundEvent, SoundEvent> SHOCKWAVE_CAST = SoundRegistry.registerSoundEvent("spell.shockwave.cast");
    public static DeferredHolder<SoundEvent, SoundEvent> SHOCKWAVE_PREPARE = SoundRegistry.registerSoundEvent("spell.shockwave.prepare");
    public static DeferredHolder<SoundEvent, SoundEvent> TRADER_YES = SoundRegistry.registerSoundEvent("entity.generic.trader.yes");
    public static DeferredHolder<SoundEvent, SoundEvent> TRADER_NO = SoundRegistry.registerSoundEvent("entity.generic.trader.no");
    public static DeferredHolder<SoundEvent, SoundEvent> SCORCH_PREPARE = SoundRegistry.registerSoundEvent("spell.scorch.prepare");
    public static DeferredHolder<SoundEvent, SoundEvent> FIERY_EXPLOSION = SoundRegistry.registerSoundEvent("entity.generic.fiery_explosion");
    public static DeferredHolder<SoundEvent, SoundEvent> ECHOING_STRIKE = SoundRegistry.registerSoundEvent("entity.echoing_strike.echoing_strike");
    public static DeferredHolder<SoundEvent, SoundEvent> SMALL_LIGHTNING_STRIKE = SoundRegistry.registerSoundEvent("entity.lightning_strike.strike");
    public static DeferredHolder<SoundEvent, SoundEvent> THUNDERSTORM_PREPARE = SoundRegistry.registerSoundEvent("spell.thunderstorm.prepare");
    public static DeferredHolder<SoundEvent, SoundEvent> BLOOD_EXPLOSION = SoundRegistry.registerSoundEvent("spell.sacrifice.blood_explosion");
    public static DeferredHolder<SoundEvent, SoundEvent> SUNBEAM_WINDUP = SoundRegistry.registerSoundEvent("entity.sunbeam.windup");
    public static DeferredHolder<SoundEvent, SoundEvent> SUNBEAM_IMPACT = SoundRegistry.registerSoundEvent("entity.sunbeam.impact");
    public static DeferredHolder<SoundEvent, SoundEvent> CLEANSE_CAST = SoundRegistry.registerSoundEvent("spell.cleanse.cast");
    public static DeferredHolder<SoundEvent, SoundEvent> ICE_SPIKE_EMERGE = SoundRegistry.registerSoundEvent("entity.ice_spike.emerge");
    public static DeferredHolder<SoundEvent, SoundEvent> FIRE_ARROW_CHARGE = SoundRegistry.registerSoundEvent("spell.fire_arrow.charge");
    public static DeferredHolder<SoundEvent, SoundEvent> FIRE_ARROW_CAST = SoundRegistry.registerSoundEvent("spell.fire_arrow.cast");
    public static DeferredHolder<SoundEvent, SoundEvent> RAISE_HELL_PREPARE = SoundRegistry.registerSoundEvent("spell.raise_hell.prepare");
    public static DeferredHolder<SoundEvent, SoundEvent> FIERY_DAGGER_THROW = SoundRegistry.registerSoundEvent("entity.fiery_dagger.throw");
    public static DeferredHolder<SoundEvent, SoundEvent> FIRE_DAGGER_PARRY = SoundRegistry.registerSoundEvent("entity.fiery_dagger.parry");
    public static DeferredHolder<SoundEvent, SoundEvent> SOULCALLER_TOLL_SUCCESS = SoundRegistry.registerSoundEvent("item.cinderous_soulcaller.toll.success");
    public static DeferredHolder<SoundEvent, SoundEvent> SOULCALLER_TOLL_FAILURE = SoundRegistry.registerSoundEvent("item.cinderous_soulcaller.toll.failure");
    public static DeferredHolder<SoundEvent, SoundEvent> GENERIC_BLADE_SWING = SoundRegistry.registerSoundEvent("entity.generic.swing_blade");
    public static DeferredHolder<SoundEvent, SoundEvent> SUMMONED_SWORDS_CHARGE = SoundRegistry.registerSoundEvent("spell.summon_swords.charge");
    public static DeferredHolder<SoundEvent, SoundEvent> SUMMONED_SWORDS_CAST = SoundRegistry.registerSoundEvent("spell.summon_swords.cast");
    public static DeferredHolder<SoundEvent, SoundEvent> POCKET_DIMENSION_TRAVEL = SoundRegistry.registerSoundEvent("spell.pocket_dimension.travel");
    public static DeferredHolder<SoundEvent, SoundEvent> ELDRITCH_PREPARE = SoundRegistry.registerSoundEvent("cast.generic.eldritch_prepare");
    public static DeferredHolder<SoundEvent, SoundEvent> ICE_SPIDER_HURT = SoundRegistry.registerSoundEvent("entity.ice_spider.hurt");
    public static DeferredHolder<SoundEvent, SoundEvent> ICE_SPIDER_BITE = SoundRegistry.registerSoundEvent("entity.ice_spider.bite");
    public static DeferredHolder<SoundEvent, SoundEvent> ICE_SPIDER_GRAPPLE_LATCH = SoundRegistry.registerSoundEvent("entity.ice_spider.grapple_latch");
    public static DeferredHolder<SoundEvent, SoundEvent> ICE_SPIDER_GRAPPLE_SPIT = SoundRegistry.registerSoundEvent("entity.ice_spider.grapple_spit");
    public static DeferredHolder<SoundEvent, SoundEvent> ICE_SPIDER_DEATH = SoundRegistry.registerSoundEvent("entity.ice_spider.death");
    public static DeferredHolder<SoundEvent, SoundEvent> ICE_SPIDER_AMBIENT = SoundRegistry.registerSoundEvent("entity.ice_spider.ambient");
    public static DeferredHolder<SoundEvent, SoundEvent> ICE_SPIDER_HOWL = SoundRegistry.registerSoundEvent("entity.ice_spider.howl");
    public static DeferredHolder<SoundEvent, SoundEvent> ICE_SPIDER_SWING = SoundRegistry.registerSoundEvent("entity.ice_spider.swing");
    public static DeferredHolder<SoundEvent, SoundEvent> FROSTBITE_FREEZE = SoundRegistry.registerSoundEvent("spell.frostbite.freeze");
    public static DeferredHolder<SoundEvent, SoundEvent> SPEAR_RETURN = SoundRegistry.registerSoundEvent("item.spear.loyalty_return");
    public static DeferredHolder<SoundEvent, SoundEvent> SPEAR_CHANNELING_STRIKE = SoundRegistry.registerSoundEvent("item.spear.channeling_strike");
    public static DeferredHolder<SoundEvent, SoundEvent> SHADOW_SLASH = SoundRegistry.registerSoundEvent("spell.shadow_slash.cast");
    public static DeferredHolder<SoundEvent, SoundEvent> SWING_WEAPON_WEIRD = SoundRegistry.registerSoundEvent("entity.generic.swing_weird");
    public static DeferredHolder<SoundEvent, SoundEvent> THROW_DAGGER = SoundRegistry.registerSoundEvent("entity.generic.throw_dagger");
    public static DeferredHolder<SoundEvent, SoundEvent> HELLRAZOR_SWING = SoundRegistry.registerSoundEvent("item.hellrazor.swing");
    public static DeferredHolder<SoundEvent, SoundEvent> FIRE_ERUPTION_SLAM = SoundRegistry.registerSoundEvent("entity.fire_eruption.slam");
    public static DeferredHolder<SoundEvent, SoundEvent> BOSS_STANCE_BREAK = SoundRegistry.registerSoundEvent("entity.fire_boss.stance_break");
    public static DeferredHolder<SoundEvent, SoundEvent> FIRE_BOSS_HURT = SoundRegistry.registerSoundEvent("entity.fire_boss.hurt");
    public static DeferredHolder<SoundEvent, SoundEvent> FIRE_BOSS_DEATH = SoundRegistry.registerSoundEvent("entity.fire_boss.death");
    public static DeferredHolder<SoundEvent, SoundEvent> FIRE_BOSS_ACCENT = SoundRegistry.registerSoundEvent("entity.fire_boss.death_final");
    public static DeferredHolder<SoundEvent, SoundEvent> FIRE_BOSS_TRANSITION_SOUL = SoundRegistry.registerSoundEvent("entity.fire_boss.enter_soul");
    public static DeferredHolder<SoundEvent, SoundEvent> FIRE_BOSS_SUMMON_SCYTHE = SoundRegistry.registerSoundEvent("entity.fire_boss.summon_scythe");
    public static DeferredHolder<SoundEvent, SoundEvent> FIRE_BOSS_FIREBALL = SoundRegistry.registerSoundEvent("entity.fire_boss.fireball");
    public static DeferredHolder<SoundEvent, SoundEvent> MUSIC_FIRE_BOSS_MELODY_A = SoundRegistry.registerSoundEvent("music.fire_boss.melody_a");
    public static DeferredHolder<SoundEvent, SoundEvent> MUSIC_FIRE_BOSS_MELODY_B = SoundRegistry.registerSoundEvent("music.fire_boss.melody_b");
    public static DeferredHolder<SoundEvent, SoundEvent> MUSIC_FIRE_BOSS_BELLS_A = SoundRegistry.registerSoundEvent("music.fire_boss.bells_a");
    public static DeferredHolder<SoundEvent, SoundEvent> MUSIC_FIRE_BOSS_BELLS_B = SoundRegistry.registerSoundEvent("music.fire_boss.bells_b");
    public static DeferredHolder<SoundEvent, SoundEvent> MUSIC_FIRE_BOSS_BACKTRACK = SoundRegistry.registerSoundEvent("music.fire_boss.backtrack");
    public static DeferredHolder<SoundEvent, SoundEvent> MUSIC_FIRE_BOSS_DRUMS = SoundRegistry.registerSoundEvent("music.fire_boss.drums");
    public static DeferredHolder<SoundEvent, SoundEvent> DEAD_KING_SWING = SoundRegistry.registerSoundEvent("entity.dead_king.attack_swing");
    public static DeferredHolder<SoundEvent, SoundEvent> DEAD_KING_SLAM = SoundRegistry.registerSoundEvent("entity.dead_king.attack_slam");
    public static DeferredHolder<SoundEvent, SoundEvent> DEAD_KING_HIT = SoundRegistry.registerSoundEvent("entity.dead_king.attack_hit");
    public static DeferredHolder<SoundEvent, SoundEvent> DEAD_KING_RESURRECT = SoundRegistry.registerSoundEvent("entity.dead_king.resurrect");
    public static DeferredHolder<SoundEvent, SoundEvent> DEAD_KING_SPAWN = SoundRegistry.registerSoundEvent("entity.dead_king.spawn");
    public static DeferredHolder<SoundEvent, SoundEvent> DEAD_KING_FAKE_DEATH = SoundRegistry.registerSoundEvent("entity.dead_king.fake_death");
    public static DeferredHolder<SoundEvent, SoundEvent> DEAD_KING_DEATH = SoundRegistry.registerSoundEvent("entity.dead_king.death");
    public static DeferredHolder<SoundEvent, SoundEvent> DEAD_KING_HURT = SoundRegistry.registerSoundEvent("entity.dead_king.hurt");
    public static DeferredHolder<SoundEvent, SoundEvent> DEAD_KING_EXPLODE = SoundRegistry.registerSoundEvent("entity.dead_king.explode");
    public static DeferredHolder<SoundEvent, SoundEvent> DEAD_KING_DRUM_LOOP = SoundRegistry.registerSoundEvent("entity.dead_king.music.drum_loop");
    public static DeferredHolder<SoundEvent, SoundEvent> DEAD_KING_AMBIENCE = SoundRegistry.registerSoundEvent("entity.dead_king.ambience");
    public static DeferredHolder<SoundEvent, SoundEvent> DEAD_KING_MUSIC_INTRO = SoundRegistry.registerSoundEvent("entity.dead_king.music.intro");
    public static DeferredHolder<SoundEvent, SoundEvent> DEAD_KING_FIRST_PHASE_MELODY = SoundRegistry.registerSoundEvent("entity.dead_king.music.first_phase_melody");
    public static DeferredHolder<SoundEvent, SoundEvent> DEAD_KING_FIRST_PHASE_ACCENT_01 = SoundRegistry.registerSoundEvent("entity.dead_king.music.first_phase_accent_01");
    public static DeferredHolder<SoundEvent, SoundEvent> DEAD_KING_SECOND_PHASE_MELODY_ALT = SoundRegistry.registerSoundEvent("entity.dead_king.music.second_phase_melody_alt");
    public static DeferredHolder<SoundEvent, SoundEvent> DEAD_KING_SUSPENSE = SoundRegistry.registerSoundEvent("entity.dead_king.music.suspense");
    public static DeferredHolder<SoundEvent, SoundEvent> FIRE_CAST = SoundRegistry.registerSoundEvent("cast.generic.fire");
    public static DeferredHolder<SoundEvent, SoundEvent> ICE_CAST = SoundRegistry.registerSoundEvent("cast.generic.ice");
    public static DeferredHolder<SoundEvent, SoundEvent> LIGHTNING_CAST = SoundRegistry.registerSoundEvent("cast.generic.lightning");
    public static DeferredHolder<SoundEvent, SoundEvent> HOLY_CAST = SoundRegistry.registerSoundEvent("cast.generic.holy");
    public static DeferredHolder<SoundEvent, SoundEvent> ENDER_CAST = SoundRegistry.registerSoundEvent("cast.generic.ender");
    public static DeferredHolder<SoundEvent, SoundEvent> BLOOD_CAST = SoundRegistry.registerSoundEvent("cast.generic.blood");
    public static DeferredHolder<SoundEvent, SoundEvent> EVOCATION_CAST = SoundRegistry.registerSoundEvent("cast.generic.evocation");
    public static DeferredHolder<SoundEvent, SoundEvent> NATURE_CAST = SoundRegistry.registerSoundEvent("cast.generic.nature");
    public static DeferredHolder<SoundEvent, SoundEvent> POISON_CAST = SoundRegistry.registerSoundEvent("cast.generic.poison");
    public static DeferredHolder<SoundEvent, SoundEvent> LEARN_ELDRITCH_SPELL = SoundRegistry.registerSoundEvent("ui.learn_eldritch_spell");
    public static DeferredHolder<SoundEvent, SoundEvent> UI_TICK = SoundRegistry.registerSoundEvent("ui.tick");
    public static DeferredHolder<SoundEvent, SoundEvent> EQUIP_SPELL_BOOK = SoundRegistry.registerSoundEvent("item.spell_book.equip");
    public static DeferredHolder<SoundEvent, SoundEvent> MUSIC_DISC_DEAD_KING_LULLABY = SoundRegistry.registerSoundEvent("music.dead_king_lullaby");
    public static DeferredHolder<SoundEvent, SoundEvent> MUSIC_DISC_FLAME_STILL_BURNS = SoundRegistry.registerSoundEvent("music.flame_still_burns");
    public static DeferredHolder<SoundEvent, SoundEvent> MUSIC_DISC_WHISPERS_OF_ICE = SoundRegistry.registerSoundEvent("music.whispers_of_ice");

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)name)));
    }
}

