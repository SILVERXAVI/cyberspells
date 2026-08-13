/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EntityType$Builder
 *  net.minecraft.world.entity.MobCategory
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package io.redspace.ironsspellbooks.registries;

import io.redspace.ironsspellbooks.entity.VisualFallingBlockEntity;
import io.redspace.ironsspellbooks.entity.mobs.CatacombsZombie;
import io.redspace.ironsspellbooks.entity.mobs.MagehunterVindicator;
import io.redspace.ironsspellbooks.entity.mobs.SummonedHorse;
import io.redspace.ironsspellbooks.entity.mobs.SummonedPolarBear;
import io.redspace.ironsspellbooks.entity.mobs.SummonedSkeleton;
import io.redspace.ironsspellbooks.entity.mobs.SummonedVex;
import io.redspace.ironsspellbooks.entity.mobs.SummonedZombie;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingBoss;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingCorpseEntity;
import io.redspace.ironsspellbooks.entity.mobs.debug_wizard.DebugWizard;
import io.redspace.ironsspellbooks.entity.mobs.frozen_humanoid.FrozenHumanoid;
import io.redspace.ironsspellbooks.entity.mobs.ice_spider.IceSpiderEntity;
import io.redspace.ironsspellbooks.entity.mobs.keeper.KeeperEntity;
import io.redspace.ironsspellbooks.entity.mobs.necromancer.NecromancerEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.alchemist.ApothecaristEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.archevoker.ArchevokerEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.cryomancer.CryomancerEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.cultist.CultistEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.cursed_armor_stand.CursedArmorStandEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.priest.PriestEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.pyromancer.PyromancerEntity;
import io.redspace.ironsspellbooks.entity.spells.ArrowVolleyEntity;
import io.redspace.ironsspellbooks.entity.spells.ChainLightning;
import io.redspace.ironsspellbooks.entity.spells.EarthquakeAoe;
import io.redspace.ironsspellbooks.entity.spells.EchoingStrikeEntity;
import io.redspace.ironsspellbooks.entity.spells.FireEruptionAoe;
import io.redspace.ironsspellbooks.entity.spells.HealingAoe;
import io.redspace.ironsspellbooks.entity.spells.LightningStrike;
import io.redspace.ironsspellbooks.entity.spells.StompAoe;
import io.redspace.ironsspellbooks.entity.spells.WitherSkullProjectile;
import io.redspace.ironsspellbooks.entity.spells.acid_orb.AcidOrb;
import io.redspace.ironsspellbooks.entity.spells.ball_lightning.BallLightning;
import io.redspace.ironsspellbooks.entity.spells.black_hole.BlackHole;
import io.redspace.ironsspellbooks.entity.spells.blood_needle.BloodNeedle;
import io.redspace.ironsspellbooks.entity.spells.blood_slash.BloodSlashProjectile;
import io.redspace.ironsspellbooks.entity.spells.comet.Comet;
import io.redspace.ironsspellbooks.entity.spells.cone_of_cold.ConeOfColdProjectile;
import io.redspace.ironsspellbooks.entity.spells.creeper_head.CreeperHeadProjectile;
import io.redspace.ironsspellbooks.entity.spells.devour_jaw.DevourJaw;
import io.redspace.ironsspellbooks.entity.spells.dragon_breath.DragonBreathPool;
import io.redspace.ironsspellbooks.entity.spells.dragon_breath.DragonBreathProjectile;
import io.redspace.ironsspellbooks.entity.spells.eldritch_blast.EldritchBlastVisualEntity;
import io.redspace.ironsspellbooks.entity.spells.electrocute.ElectrocuteProjectile;
import io.redspace.ironsspellbooks.entity.spells.fiery_dagger.FieryDaggerEntity;
import io.redspace.ironsspellbooks.entity.spells.fire_arrow.FireArrowProjectile;
import io.redspace.ironsspellbooks.entity.spells.fire_breath.FireBreathProjectile;
import io.redspace.ironsspellbooks.entity.spells.fireball.MagicFireball;
import io.redspace.ironsspellbooks.entity.spells.fireball.SmallMagicFireball;
import io.redspace.ironsspellbooks.entity.spells.firebolt.FireboltProjectile;
import io.redspace.ironsspellbooks.entity.spells.firefly_swarm.FireflySwarmProjectile;
import io.redspace.ironsspellbooks.entity.spells.guiding_bolt.GuidingBoltProjectile;
import io.redspace.ironsspellbooks.entity.spells.gust.GustCollider;
import io.redspace.ironsspellbooks.entity.spells.ice_block.IceBlockProjectile;
import io.redspace.ironsspellbooks.entity.spells.ice_spike.IceSpikeEntity;
import io.redspace.ironsspellbooks.entity.spells.ice_tomb.IceTombEntity;
import io.redspace.ironsspellbooks.entity.spells.icicle.IcicleProjectile;
import io.redspace.ironsspellbooks.entity.spells.lightning_lance.LightningLanceProjectile;
import io.redspace.ironsspellbooks.entity.spells.magic_arrow.MagicArrowProjectile;
import io.redspace.ironsspellbooks.entity.spells.magic_missile.MagicMissileProjectile;
import io.redspace.ironsspellbooks.entity.spells.magma_ball.FireBomb;
import io.redspace.ironsspellbooks.entity.spells.magma_ball.FireField;
import io.redspace.ironsspellbooks.entity.spells.poison_arrow.PoisonArrow;
import io.redspace.ironsspellbooks.entity.spells.poison_breath.PoisonBreathProjectile;
import io.redspace.ironsspellbooks.entity.spells.poison_cloud.PoisonCloud;
import io.redspace.ironsspellbooks.entity.spells.poison_cloud.PoisonSplash;
import io.redspace.ironsspellbooks.entity.spells.portal.PortalEntity;
import io.redspace.ironsspellbooks.entity.spells.ray_of_frost.RayOfFrostVisualEntity;
import io.redspace.ironsspellbooks.entity.spells.root.RootEntity;
import io.redspace.ironsspellbooks.entity.spells.shield.ShieldEntity;
import io.redspace.ironsspellbooks.entity.spells.small_magic_arrow.SmallMagicArrow;
import io.redspace.ironsspellbooks.entity.spells.snowball.FrostField;
import io.redspace.ironsspellbooks.entity.spells.snowball.Snowball;
import io.redspace.ironsspellbooks.entity.spells.spectral_hammer.SpectralHammer;
import io.redspace.ironsspellbooks.entity.spells.summoned_weapons.SummonedClaymoreEntity;
import io.redspace.ironsspellbooks.entity.spells.summoned_weapons.SummonedRapierEntity;
import io.redspace.ironsspellbooks.entity.spells.summoned_weapons.SummonedSwordEntity;
import io.redspace.ironsspellbooks.entity.spells.sunbeam.SunbeamEntity;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.entity.spells.thrown_item.ThrownItemProjectile;
import io.redspace.ironsspellbooks.entity.spells.thrown_spear.ThrownSpear;
import io.redspace.ironsspellbooks.entity.spells.thunderstep.ThunderstepProjectile;
import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import io.redspace.ironsspellbooks.entity.spells.wall_of_fire.WallOfFireEntity;
import io.redspace.ironsspellbooks.entity.spells.wisp.WispEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EntityRegistry {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create((ResourceKey)Registries.ENTITY_TYPE, (String)"irons_spellbooks");
    public static final DeferredHolder<EntityType<?>, EntityType<WispEntity>> WISP = ENTITIES.register("wisp", () -> EntityType.Builder.of(WispEntity::new, (MobCategory)MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"wisp").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<SpectralHammer>> SPECTRAL_HAMMER = ENTITIES.register("spectral_hammer", () -> EntityType.Builder.of(SpectralHammer::new, (MobCategory)MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"spectral_hammer").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<MagicMissileProjectile>> MAGIC_MISSILE_PROJECTILE = ENTITIES.register("magic_missile", () -> EntityType.Builder.of(MagicMissileProjectile::new, (MobCategory)MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"magic_missile").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<ThrownItemProjectile>> THROWN_ITEM = ENTITIES.register("thrown_item", () -> EntityType.Builder.of(ThrownItemProjectile::new, (MobCategory)MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"thrown_item").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<ConeOfColdProjectile>> CONE_OF_COLD_PROJECTILE = ENTITIES.register("cone_of_cold", () -> EntityType.Builder.of(ConeOfColdProjectile::new, (MobCategory)MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"cone_of_cold").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<BloodSlashProjectile>> BLOOD_SLASH_PROJECTILE = ENTITIES.register("blood_slash", () -> EntityType.Builder.of(BloodSlashProjectile::new, (MobCategory)MobCategory.MISC).sized(2.0f, 0.5f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"blood_slash").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<ElectrocuteProjectile>> ELECTROCUTE_PROJECTILE = ENTITIES.register("electrocute", () -> EntityType.Builder.of(ElectrocuteProjectile::new, (MobCategory)MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"electrocute").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<FireboltProjectile>> FIREBOLT_PROJECTILE = ENTITIES.register("firebolt", () -> EntityType.Builder.of(FireboltProjectile::new, (MobCategory)MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"firebolt").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<IcicleProjectile>> ICICLE_PROJECTILE = ENTITIES.register("icicle", () -> EntityType.Builder.of(IcicleProjectile::new, (MobCategory)MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"icicle").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<FireBreathProjectile>> FIRE_BREATH_PROJECTILE = ENTITIES.register("fire_breath", () -> EntityType.Builder.of(FireBreathProjectile::new, (MobCategory)MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"fire_breath").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<DragonBreathProjectile>> DRAGON_BREATH_PROJECTILE = ENTITIES.register("dragon_breath", () -> EntityType.Builder.of(DragonBreathProjectile::new, (MobCategory)MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"dragon_breath").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<DebugWizard>> DEBUG_WIZARD = ENTITIES.register("debug_wizard", () -> EntityType.Builder.of(DebugWizard::new, (MobCategory)MobCategory.MONSTER).sized(0.6f, 1.8f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"simple_wizard").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<SummonedHorse>> SPECTRAL_STEED = ENTITIES.register("spectral_steed", () -> EntityType.Builder.of(SummonedHorse::new, (MobCategory)MobCategory.CREATURE).sized(1.3964844f, 1.6f).clientTrackingRange(10).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"spectral_steed").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<ShieldEntity>> SHIELD_ENTITY = ENTITIES.register("shield", () -> EntityType.Builder.of(ShieldEntity::new, (MobCategory)MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"shield").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<WallOfFireEntity>> WALL_OF_FIRE_ENTITY = ENTITIES.register("wall_of_fire", () -> EntityType.Builder.of(WallOfFireEntity::new, (MobCategory)MobCategory.MISC).sized(10.0f, 1.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"wall_of_fire").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<SummonedVex>> SUMMONED_VEX = ENTITIES.register("summoned_vex", () -> EntityType.Builder.of(SummonedVex::new, (MobCategory)MobCategory.CREATURE).sized(0.4f, 0.8f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"summoned_vex").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<PyromancerEntity>> PYROMANCER = ENTITIES.register("pyromancer", () -> EntityType.Builder.of(PyromancerEntity::new, (MobCategory)MobCategory.MONSTER).sized(0.6f, 1.8f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"pyromancer").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<CryomancerEntity>> CRYOMANCER = ENTITIES.register("cryomancer", () -> EntityType.Builder.of(CryomancerEntity::new, (MobCategory)MobCategory.MONSTER).sized(0.6f, 1.8f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"cryomancer").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<LightningLanceProjectile>> LIGHTNING_LANCE_PROJECTILE = ENTITIES.register("lightning_lance", () -> EntityType.Builder.of(LightningLanceProjectile::new, (MobCategory)MobCategory.MISC).sized(1.25f, 1.25f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"lightning_lance").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<NecromancerEntity>> NECROMANCER = ENTITIES.register("necromancer", () -> EntityType.Builder.of(NecromancerEntity::new, (MobCategory)MobCategory.MONSTER).sized(0.6f, 1.8f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"necromancer").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<SummonedZombie>> SUMMONED_ZOMBIE = ENTITIES.register("summoned_zombie", () -> EntityType.Builder.of(SummonedZombie::new, (MobCategory)MobCategory.MISC).sized(0.6f, 1.8f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"summoned_zombie").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<SummonedSkeleton>> SUMMONED_SKELETON = ENTITIES.register("summoned_skeleton", () -> EntityType.Builder.of(SummonedSkeleton::new, (MobCategory)MobCategory.MISC).sized(0.6f, 1.8f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"summoned_skeleton").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<WitherSkullProjectile>> WITHER_SKULL_PROJECTILE = ENTITIES.register("wither_skull", () -> EntityType.Builder.of(WitherSkullProjectile::new, (MobCategory)MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"wither_skull").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<MagicArrowProjectile>> MAGIC_ARROW_PROJECTILE = ENTITIES.register("magic_arrow", () -> EntityType.Builder.of(MagicArrowProjectile::new, (MobCategory)MobCategory.MISC).sized(0.8f, 0.8f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"magic_arrow").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<CreeperHeadProjectile>> CREEPER_HEAD_PROJECTILE = ENTITIES.register("creeper_head", () -> EntityType.Builder.of(CreeperHeadProjectile::new, (MobCategory)MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"creeper_head").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<FrozenHumanoid>> FROZEN_HUMANOID = ENTITIES.register("frozen_humanoid", () -> EntityType.Builder.of(FrozenHumanoid::new, (MobCategory)MobCategory.MISC).sized(0.6f, 1.8f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"frozen_humanoid").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<SmallMagicFireball>> SMALL_FIREBALL_PROJECTILE = ENTITIES.register("small_fireball", () -> EntityType.Builder.of(SmallMagicFireball::new, (MobCategory)MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"small_fireball").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<MagicFireball>> MAGIC_FIREBALL = ENTITIES.register("fireball", () -> EntityType.Builder.of(MagicFireball::new, (MobCategory)MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(4).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"fireball").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<SummonedPolarBear>> SUMMONED_POLAR_BEAR = ENTITIES.register("summoned_polar_bear", () -> EntityType.Builder.of(SummonedPolarBear::new, (MobCategory)MobCategory.CREATURE).immuneTo(new Block[]{Blocks.POWDER_SNOW}).sized(1.4f, 1.4f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"summoned_polar_bear").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<DeadKingBoss>> DEAD_KING = ENTITIES.register("dead_king", () -> EntityType.Builder.of(DeadKingBoss::new, (MobCategory)MobCategory.MONSTER).sized(0.9f, 3.5f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"dead_king").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<DeadKingCorpseEntity>> DEAD_KING_CORPSE = ENTITIES.register("dead_king_corpse", () -> EntityType.Builder.of(DeadKingCorpseEntity::new, (MobCategory)MobCategory.MISC).sized(1.5f, 0.95f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"dead_king_corpse").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<CatacombsZombie>> CATACOMBS_ZOMBIE = ENTITIES.register("catacombs_zombie", () -> EntityType.Builder.of(CatacombsZombie::new, (MobCategory)MobCategory.MONSTER).sized(1.5f, 0.95f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"catacombs_zombie").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<ArchevokerEntity>> ARCHEVOKER = ENTITIES.register("archevoker", () -> EntityType.Builder.of(ArchevokerEntity::new, (MobCategory)MobCategory.MONSTER).sized(0.6f, 2.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"archevoker").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<MagehunterVindicator>> MAGEHUNTER_VINDICATOR = ENTITIES.register("magehunter_vindicator", () -> EntityType.Builder.of(MagehunterVindicator::new, (MobCategory)MobCategory.MONSTER).sized(1.5f, 0.95f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"magehunter_vindicator").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<KeeperEntity>> KEEPER = ENTITIES.register("citadel_keeper", () -> EntityType.Builder.of(KeeperEntity::new, (MobCategory)MobCategory.MONSTER).sized(0.85f, 2.3f).clientTrackingRange(64).eyeHeight(2.3f).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"citadel_keeper").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<FireBossEntity>> FIRE_BOSS = ENTITIES.register("fire_boss", () -> EntityType.Builder.of(FireBossEntity::new, (MobCategory)MobCategory.MONSTER).sized(0.85f, 2.1f).clientTrackingRange(64).fireImmune().build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"fire_boss").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<VoidTentacle>> SCULK_TENTACLE = ENTITIES.register("sculk_tentacle", () -> EntityType.Builder.of(VoidTentacle::new, (MobCategory)MobCategory.MISC).sized(2.5f, 5.5f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"sculk_tentacle").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<IceBlockProjectile>> ICE_BLOCK_PROJECTILE = ENTITIES.register("ice_block_projectile", () -> EntityType.Builder.of(IceBlockProjectile::new, (MobCategory)MobCategory.MISC).sized(1.25f, 1.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"ice_block_projectile").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<PoisonCloud>> POISON_CLOUD = ENTITIES.register("poison_cloud", () -> EntityType.Builder.of(PoisonCloud::new, (MobCategory)MobCategory.MISC).sized(4.0f, 1.2f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"poison_cloud").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<SunbeamEntity>> SUNBEAM = ENTITIES.register("sunbeam", () -> EntityType.Builder.of(SunbeamEntity::new, (MobCategory)MobCategory.MISC).sized(1.5f, 14.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"sunbeam").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<DragonBreathPool>> DRAGON_BREATH_POOL = ENTITIES.register("dragon_breath_pool", () -> EntityType.Builder.of(DragonBreathPool::new, (MobCategory)MobCategory.MISC).sized(4.0f, 1.2f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"dragon_breath_pool").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<PoisonBreathProjectile>> POISON_BREATH_PROJECTILE = ENTITIES.register("poison_breath", () -> EntityType.Builder.of(PoisonBreathProjectile::new, (MobCategory)MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"poison_breath").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<PoisonArrow>> POISON_ARROW = ENTITIES.register("poison_arrow", () -> EntityType.Builder.of(PoisonArrow::new, (MobCategory)MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"poison_arrow").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<SmallMagicArrow>> SMALL_MAGIC_ARROW = ENTITIES.register("small_magic_arrow", () -> EntityType.Builder.of(SmallMagicArrow::new, (MobCategory)MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"small_magic_arrow").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<PoisonSplash>> POISON_SPLASH = ENTITIES.register("poison_splash", () -> EntityType.Builder.of(PoisonSplash::new, (MobCategory)MobCategory.MISC).sized(3.5f, 4.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"poison_splash").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<AcidOrb>> ACID_ORB = ENTITIES.register("acid_orb", () -> EntityType.Builder.of(AcidOrb::new, (MobCategory)MobCategory.MISC).sized(0.75f, 0.75f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"acid_orb").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<RootEntity>> ROOT = ENTITIES.register("root", () -> EntityType.Builder.of(RootEntity::new, (MobCategory)MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"root").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<BlackHole>> BLACK_HOLE = ENTITIES.register("black_hole", () -> EntityType.Builder.of(BlackHole::new, (MobCategory)MobCategory.MISC).sized(11.0f, 11.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"black_hole").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<BloodNeedle>> BLOOD_NEEDLE = ENTITIES.register("blood_needle", () -> EntityType.Builder.of(BloodNeedle::new, (MobCategory)MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"blood_needle").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<FireField>> FIRE_FIELD = ENTITIES.register("fire_field", () -> EntityType.Builder.of(FireField::new, (MobCategory)MobCategory.MISC).sized(4.0f, 1.2f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"fire_field").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<FireBomb>> FIRE_BOMB = ENTITIES.register("magma_ball", () -> EntityType.Builder.of(FireBomb::new, (MobCategory)MobCategory.MISC).sized(0.75f, 0.75f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"magma_ball").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<Comet>> COMET = ENTITIES.register("comet", () -> EntityType.Builder.of(Comet::new, (MobCategory)MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"comet").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<TargetedAreaEntity>> TARGET_AREA_ENTITY = ENTITIES.register("target_area", () -> EntityType.Builder.of(TargetedAreaEntity::new, (MobCategory)MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"target_area").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<HealingAoe>> HEALING_AOE = ENTITIES.register("healing_aoe", () -> EntityType.Builder.of(HealingAoe::new, (MobCategory)MobCategory.MISC).sized(4.0f, 0.8f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"healing_aoe").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<EarthquakeAoe>> EARTHQUAKE_AOE = ENTITIES.register("earthquake_aoe", () -> EntityType.Builder.of(EarthquakeAoe::new, (MobCategory)MobCategory.MISC).sized(4.0f, 0.8f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"earthquake_aoe").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<PriestEntity>> PRIEST = ENTITIES.register("priest", () -> EntityType.Builder.of(PriestEntity::new, (MobCategory)MobCategory.CREATURE).sized(0.6f, 2.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"priest").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<VisualFallingBlockEntity>> FALLING_BLOCK = ENTITIES.register("visual_falling_block", () -> EntityType.Builder.of(VisualFallingBlockEntity::new, (MobCategory)MobCategory.MISC).sized(0.98f, 0.98f).clientTrackingRange(10).updateInterval(20).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"visual_falling_block").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<GuidingBoltProjectile>> GUIDING_BOLT = ENTITIES.register("guiding_bolt", () -> EntityType.Builder.of(GuidingBoltProjectile::new, (MobCategory)MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"guiding_bolt").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<GustCollider>> GUST_COLLIDER = ENTITIES.register("gust", () -> EntityType.Builder.of(GustCollider::new, (MobCategory)MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"gust").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<ChainLightning>> CHAIN_LIGHTNING = ENTITIES.register("chain_lightning", () -> EntityType.Builder.of(ChainLightning::new, (MobCategory)MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"chain_lightning").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<RayOfFrostVisualEntity>> RAY_OF_FROST_VISUAL_ENTITY = ENTITIES.register("ray_of_frost", () -> EntityType.Builder.of(RayOfFrostVisualEntity::new, (MobCategory)MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"ray_of_frost").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<EldritchBlastVisualEntity>> ELDRITCH_BLAST_VISUAL_ENTITY = ENTITIES.register("eldritch_blast", () -> EntityType.Builder.of(EldritchBlastVisualEntity::new, (MobCategory)MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"eldritch_blast").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<DevourJaw>> DEVOUR_JAW = ENTITIES.register("devour_jaw", () -> EntityType.Builder.of(DevourJaw::new, (MobCategory)MobCategory.MISC).sized(2.0f, 2.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"devour_jaw").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<FireflySwarmProjectile>> FIREFLY_SWARM = ENTITIES.register("firefly_swarm", () -> EntityType.Builder.of(FireflySwarmProjectile::new, (MobCategory)MobCategory.MISC).sized(0.9f, 0.9f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"firefly_swarm").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<ArrowVolleyEntity>> ARROW_VOLLEY_ENTITY = ENTITIES.register("arrow_volley", () -> EntityType.Builder.of(ArrowVolleyEntity::new, (MobCategory)MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"arrow_volley").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<PortalEntity>> PORTAL = ENTITIES.register("portal", () -> EntityType.Builder.of(PortalEntity::new, (MobCategory)MobCategory.MISC).sized(0.8f, 2.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"portal").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<StompAoe>> STOMP_AOE = ENTITIES.register("stomp_aoe", () -> EntityType.Builder.of(StompAoe::new, (MobCategory)MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"stomp_aoe").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<LightningStrike>> LIGHTNING_STRIKE = ENTITIES.register("lightning_strike", () -> EntityType.Builder.of(LightningStrike::new, (MobCategory)MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"lightning_strike").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<ApothecaristEntity>> APOTHECARIST = ENTITIES.register("apothecarist", () -> EntityType.Builder.of(ApothecaristEntity::new, (MobCategory)MobCategory.MONSTER).sized(0.6f, 1.8f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"apothecarist").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<EchoingStrikeEntity>> ECHOING_STRIKE = ENTITIES.register("echoing_strike", () -> EntityType.Builder.of(EchoingStrikeEntity::new, (MobCategory)MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"echoing_strike").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<CultistEntity>> CULTIST = ENTITIES.register("cultist", () -> EntityType.Builder.of(CultistEntity::new, (MobCategory)MobCategory.MONSTER).sized(0.6f, 1.8f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"cultist").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<BallLightning>> BALL_LIGHTNING = ENTITIES.register("ball_lightning", () -> EntityType.Builder.of(BallLightning::new, (MobCategory)MobCategory.MISC).sized(1.1f, 1.1f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"ball_lightning").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<IceSpikeEntity>> ICE_SPIKE = ENTITIES.register("ice_spike", () -> EntityType.Builder.of(IceSpikeEntity::new, (MobCategory)MobCategory.MISC).sized(1.0f, 2.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"ice_spike").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<FireArrowProjectile>> FIRE_ARROW_PROJECTILE = ENTITIES.register("fire_arrow", () -> EntityType.Builder.of(FireArrowProjectile::new, (MobCategory)MobCategory.MISC).sized(0.8f, 0.8f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"fire_arrow").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<FireEruptionAoe>> FIRE_ERUPTION_AOE = ENTITIES.register("fire_eruption", () -> EntityType.Builder.of(FireEruptionAoe::new, (MobCategory)MobCategory.MISC).sized(4.0f, 0.8f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"fire_eruption").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<FieryDaggerEntity>> FIERY_DAGGER_PROJECTILE = ENTITIES.register("fiery_dagger", () -> EntityType.Builder.of(FieryDaggerEntity::new, (MobCategory)MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"fiery_dagger").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<CursedArmorStandEntity>> CURSED_ARMOR_STAND = ENTITIES.register("cursed_armor_stand", () -> EntityType.Builder.of(CursedArmorStandEntity::new, (MobCategory)MobCategory.MONSTER).sized(0.6f, 1.8f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"cursed_armor_stand").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<ThunderstepProjectile>> THUNDERSTEP_PROJECTILE = ENTITIES.register("thunderstep_orb", () -> EntityType.Builder.of(ThunderstepProjectile::new, (MobCategory)MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"thunderstep_orb").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<SummonedSwordEntity>> SUMMONED_SWORD = ENTITIES.register("summoned_sword", () -> EntityType.Builder.of(SummonedSwordEntity::new, (MobCategory)MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"summoned_sword").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<SummonedClaymoreEntity>> SUMMONED_CLAYMORE = ENTITIES.register("summoned_claymore", () -> EntityType.Builder.of(SummonedClaymoreEntity::new, (MobCategory)MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"summoned_claymore").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<SummonedRapierEntity>> SUMMONED_RAPIER = ENTITIES.register("summoned_rapier", () -> EntityType.Builder.of(SummonedRapierEntity::new, (MobCategory)MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"summoned_rapier").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<IceSpiderEntity>> ICE_SPIDER = ENTITIES.register("ice_spider", () -> EntityType.Builder.of(IceSpiderEntity::new, (MobCategory)MobCategory.MONSTER).sized(1.75f, 1.9f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"ice_spider").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<IceTombEntity>> ICE_TOMB = ENTITIES.register("ice_tomb", () -> EntityType.Builder.of(IceTombEntity::new, (MobCategory)MobCategory.MISC).sized(1.0f, 2.2f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"ice_tomb").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<Snowball>> SNOWBALL = ENTITIES.register("snowball", () -> EntityType.Builder.of(Snowball::new, (MobCategory)MobCategory.MISC).sized(0.75f, 0.75f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"snowball").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<FrostField>> FROST_FIELD = ENTITIES.register("frost_field", () -> EntityType.Builder.of(FrostField::new, (MobCategory)MobCategory.MISC).sized(4.0f, 1.2f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"frost_field").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<ThrownSpear>> THROWN_SPEAR = ENTITIES.register("spear", () -> EntityType.Builder.of(ThrownSpear::new, (MobCategory)MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(64).build(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"spear").toString()));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}

