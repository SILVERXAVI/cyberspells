/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Registry
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  net.neoforged.neoforge.registries.NewRegistryEvent
 *  net.neoforged.neoforge.registries.RegistryBuilder
 */
package io.redspace.ironsspellbooks.api.registry;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.spells.NoneSpell;
import io.redspace.ironsspellbooks.spells.blood.AcupunctureSpell;
import io.redspace.ironsspellbooks.spells.blood.BloodNeedlesSpell;
import io.redspace.ironsspellbooks.spells.blood.BloodSlashSpell;
import io.redspace.ironsspellbooks.spells.blood.BloodStepSpell;
import io.redspace.ironsspellbooks.spells.blood.DevourSpell;
import io.redspace.ironsspellbooks.spells.blood.HeartstopSpell;
import io.redspace.ironsspellbooks.spells.blood.RaiseDeadSpell;
import io.redspace.ironsspellbooks.spells.blood.RayOfSiphoningSpell;
import io.redspace.ironsspellbooks.spells.blood.SacrificeSpell;
import io.redspace.ironsspellbooks.spells.blood.WitherSkullSpell;
import io.redspace.ironsspellbooks.spells.eldritch.AbyssalShroudSpell;
import io.redspace.ironsspellbooks.spells.eldritch.EldritchBlastSpell;
import io.redspace.ironsspellbooks.spells.eldritch.PlanarSightSpell;
import io.redspace.ironsspellbooks.spells.eldritch.PocketDimensionSpell;
import io.redspace.ironsspellbooks.spells.eldritch.SculkTentaclesSpell;
import io.redspace.ironsspellbooks.spells.eldritch.SonicBoomSpell;
import io.redspace.ironsspellbooks.spells.eldritch.TelekinesisSpell;
import io.redspace.ironsspellbooks.spells.ender.BlackHoleSpell;
import io.redspace.ironsspellbooks.spells.ender.CounterspellSpell;
import io.redspace.ironsspellbooks.spells.ender.DragonBreathSpell;
import io.redspace.ironsspellbooks.spells.ender.EchoingStrikesSpell;
import io.redspace.ironsspellbooks.spells.ender.EvasionSpell;
import io.redspace.ironsspellbooks.spells.ender.MagicArrowSpell;
import io.redspace.ironsspellbooks.spells.ender.MagicMissileSpell;
import io.redspace.ironsspellbooks.spells.ender.PortalSpell;
import io.redspace.ironsspellbooks.spells.ender.RecallSpell;
import io.redspace.ironsspellbooks.spells.ender.ShadowSlashSpell;
import io.redspace.ironsspellbooks.spells.ender.StarfallSpell;
import io.redspace.ironsspellbooks.spells.ender.SummonEnderChestSpell;
import io.redspace.ironsspellbooks.spells.ender.SummonSwordsSpell;
import io.redspace.ironsspellbooks.spells.ender.TeleportSpell;
import io.redspace.ironsspellbooks.spells.evocation.ArrowVolleySpell;
import io.redspace.ironsspellbooks.spells.evocation.ChainCreeperSpell;
import io.redspace.ironsspellbooks.spells.evocation.FangStrikeSpell;
import io.redspace.ironsspellbooks.spells.evocation.FangWardSpell;
import io.redspace.ironsspellbooks.spells.evocation.FirecrackerSpell;
import io.redspace.ironsspellbooks.spells.evocation.GustSpell;
import io.redspace.ironsspellbooks.spells.evocation.InvisibilitySpell;
import io.redspace.ironsspellbooks.spells.evocation.LobCreeperSpell;
import io.redspace.ironsspellbooks.spells.evocation.ShieldSpell;
import io.redspace.ironsspellbooks.spells.evocation.SlowSpell;
import io.redspace.ironsspellbooks.spells.evocation.SpectralHammerSpell;
import io.redspace.ironsspellbooks.spells.evocation.SummonHorseSpell;
import io.redspace.ironsspellbooks.spells.evocation.SummonVexSpell;
import io.redspace.ironsspellbooks.spells.evocation.ThrowSpell;
import io.redspace.ironsspellbooks.spells.evocation.WololoSpell;
import io.redspace.ironsspellbooks.spells.fire.BlazeStormSpell;
import io.redspace.ironsspellbooks.spells.fire.BurningDashSpell;
import io.redspace.ironsspellbooks.spells.fire.FireArrowSpell;
import io.redspace.ironsspellbooks.spells.fire.FireBreathSpell;
import io.redspace.ironsspellbooks.spells.fire.FireballSpell;
import io.redspace.ironsspellbooks.spells.fire.FireboltSpell;
import io.redspace.ironsspellbooks.spells.fire.FlamingBarrageSpell;
import io.redspace.ironsspellbooks.spells.fire.FlamingStrikeSpell;
import io.redspace.ironsspellbooks.spells.fire.HeatSurgeSpell;
import io.redspace.ironsspellbooks.spells.fire.MagmaBombSpell;
import io.redspace.ironsspellbooks.spells.fire.RaiseHellSpell;
import io.redspace.ironsspellbooks.spells.fire.ScorchSpell;
import io.redspace.ironsspellbooks.spells.fire.WallOfFireSpell;
import io.redspace.ironsspellbooks.spells.holy.AngelWingsSpell;
import io.redspace.ironsspellbooks.spells.holy.BlessingOfLifeSpell;
import io.redspace.ironsspellbooks.spells.holy.CleanseSpell;
import io.redspace.ironsspellbooks.spells.holy.CloudOfRegenerationSpell;
import io.redspace.ironsspellbooks.spells.holy.DivineSmiteSpell;
import io.redspace.ironsspellbooks.spells.holy.FortifySpell;
import io.redspace.ironsspellbooks.spells.holy.GreaterHealSpell;
import io.redspace.ironsspellbooks.spells.holy.GuidingBoltSpell;
import io.redspace.ironsspellbooks.spells.holy.HasteSpell;
import io.redspace.ironsspellbooks.spells.holy.HealSpell;
import io.redspace.ironsspellbooks.spells.holy.HealingCircleSpell;
import io.redspace.ironsspellbooks.spells.holy.SunbeamSpell;
import io.redspace.ironsspellbooks.spells.holy.WispSpell;
import io.redspace.ironsspellbooks.spells.ice.ConeOfColdSpell;
import io.redspace.ironsspellbooks.spells.ice.FrostStepSpell;
import io.redspace.ironsspellbooks.spells.ice.FrostbiteSpell;
import io.redspace.ironsspellbooks.spells.ice.FrostwaveSpell;
import io.redspace.ironsspellbooks.spells.ice.IceBlockSpell;
import io.redspace.ironsspellbooks.spells.ice.IceSpikesSpell;
import io.redspace.ironsspellbooks.spells.ice.IceTombSpell;
import io.redspace.ironsspellbooks.spells.ice.IcicleSpell;
import io.redspace.ironsspellbooks.spells.ice.RayOfFrostSpell;
import io.redspace.ironsspellbooks.spells.ice.SnowballSpell;
import io.redspace.ironsspellbooks.spells.ice.SummonPolarBearSpell;
import io.redspace.ironsspellbooks.spells.lightning.AscensionSpell;
import io.redspace.ironsspellbooks.spells.lightning.BallLightningSpell;
import io.redspace.ironsspellbooks.spells.lightning.ChainLightningSpell;
import io.redspace.ironsspellbooks.spells.lightning.ChargeSpell;
import io.redspace.ironsspellbooks.spells.lightning.ElectrocuteSpell;
import io.redspace.ironsspellbooks.spells.lightning.LightningBoltSpell;
import io.redspace.ironsspellbooks.spells.lightning.LightningLanceSpell;
import io.redspace.ironsspellbooks.spells.lightning.ShockwaveSpell;
import io.redspace.ironsspellbooks.spells.lightning.ThunderstormSpell;
import io.redspace.ironsspellbooks.spells.lightning.VoltStrikeSpell;
import io.redspace.ironsspellbooks.spells.nature.AcidOrbSpell;
import io.redspace.ironsspellbooks.spells.nature.BlightSpell;
import io.redspace.ironsspellbooks.spells.nature.EarthquakeSpell;
import io.redspace.ironsspellbooks.spells.nature.FireflySwarmSpell;
import io.redspace.ironsspellbooks.spells.nature.GluttonySpell;
import io.redspace.ironsspellbooks.spells.nature.OakskinSpell;
import io.redspace.ironsspellbooks.spells.nature.PoisonArrowSpell;
import io.redspace.ironsspellbooks.spells.nature.PoisonBreathSpell;
import io.redspace.ironsspellbooks.spells.nature.PoisonSplashSpell;
import io.redspace.ironsspellbooks.spells.nature.RootSpell;
import io.redspace.ironsspellbooks.spells.nature.SpiderAspectSpell;
import io.redspace.ironsspellbooks.spells.nature.StompSpell;
import io.redspace.ironsspellbooks.spells.nature.TouchDigSpell;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class SpellRegistry {
    public static final ResourceKey<Registry<AbstractSpell>> SPELL_REGISTRY_KEY = ResourceKey.createRegistryKey((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"spells"));
    private static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SPELL_REGISTRY_KEY, (String)"irons_spellbooks");
    public static final Registry<AbstractSpell> REGISTRY = new RegistryBuilder(SPELL_REGISTRY_KEY).create();
    private static final NoneSpell noneSpell = new NoneSpell();
    private static final Map<SchoolType, List<AbstractSpell>> SCHOOLS_TO_SPELLS = new HashMap<SchoolType, List<AbstractSpell>>();
    public static final Supplier<AbstractSpell> ACUPUNCTURE_SPELL = SpellRegistry.registerSpell(new AcupunctureSpell());
    public static final Supplier<AbstractSpell> BLOOD_NEEDLES_SPELL = SpellRegistry.registerSpell(new BloodNeedlesSpell());
    public static final Supplier<AbstractSpell> BLOOD_SLASH_SPELL = SpellRegistry.registerSpell(new BloodSlashSpell());
    public static final Supplier<AbstractSpell> BLOOD_STEP_SPELL = SpellRegistry.registerSpell(new BloodStepSpell());
    public static final Supplier<AbstractSpell> DEVOUR_SPELL = SpellRegistry.registerSpell(new DevourSpell());
    public static final Supplier<AbstractSpell> HEARTSTOP_SPELL = SpellRegistry.registerSpell(new HeartstopSpell());
    public static final Supplier<AbstractSpell> RAISE_DEAD_SPELL = SpellRegistry.registerSpell(new RaiseDeadSpell());
    public static final Supplier<AbstractSpell> RAY_OF_SIPHONING_SPELL = SpellRegistry.registerSpell(new RayOfSiphoningSpell());
    public static final Supplier<AbstractSpell> WITHER_SKULL_SPELL = SpellRegistry.registerSpell(new WitherSkullSpell());
    public static final Supplier<AbstractSpell> SACRIFICE_SPELL = SpellRegistry.registerSpell(new SacrificeSpell());
    public static final Supplier<AbstractSpell> COUNTERSPELL_SPELL = SpellRegistry.registerSpell(new CounterspellSpell());
    public static final Supplier<AbstractSpell> DRAGON_BREATH_SPELL = SpellRegistry.registerSpell(new DragonBreathSpell());
    public static final Supplier<AbstractSpell> EVASION_SPELL = SpellRegistry.registerSpell(new EvasionSpell());
    public static final Supplier<AbstractSpell> MAGIC_ARROW_SPELL = SpellRegistry.registerSpell(new MagicArrowSpell());
    public static final Supplier<AbstractSpell> MAGIC_MISSILE_SPELL = SpellRegistry.registerSpell(new MagicMissileSpell());
    public static final Supplier<AbstractSpell> STARFALL_SPELL = SpellRegistry.registerSpell(new StarfallSpell());
    public static final Supplier<AbstractSpell> TELEPORT_SPELL = SpellRegistry.registerSpell(new TeleportSpell());
    public static final Supplier<AbstractSpell> SUMMON_ENDER_CHEST_SPELL = SpellRegistry.registerSpell(new SummonEnderChestSpell());
    public static final Supplier<AbstractSpell> RECALL_SPELL = SpellRegistry.registerSpell(new RecallSpell());
    public static final Supplier<AbstractSpell> PORTAL_SPELL = SpellRegistry.registerSpell(new PortalSpell());
    public static final Supplier<AbstractSpell> ECHOING_STRIKES_SPELL = SpellRegistry.registerSpell(new EchoingStrikesSpell());
    public static final Supplier<AbstractSpell> BLACK_HOLE_SPELL = SpellRegistry.registerSpell(new BlackHoleSpell());
    public static final Supplier<AbstractSpell> SUMMON_SWORDS = SpellRegistry.registerSpell(new SummonSwordsSpell());
    public static final Supplier<AbstractSpell> SHADOW_SLASH = SpellRegistry.registerSpell(new ShadowSlashSpell());
    public static final Supplier<AbstractSpell> CHAIN_CREEPER_SPELL = SpellRegistry.registerSpell(new ChainCreeperSpell());
    public static final Supplier<AbstractSpell> FANG_STRIKE_SPELL = SpellRegistry.registerSpell(new FangStrikeSpell());
    public static final Supplier<AbstractSpell> FANG_WARD_SPELL = SpellRegistry.registerSpell(new FangWardSpell());
    public static final Supplier<AbstractSpell> FIRECRACKER_SPELL = SpellRegistry.registerSpell(new FirecrackerSpell());
    public static final Supplier<AbstractSpell> GUST_SPELL = SpellRegistry.registerSpell(new GustSpell());
    public static final Supplier<AbstractSpell> INVISIBILITY_SPELL = SpellRegistry.registerSpell(new InvisibilitySpell());
    public static final Supplier<AbstractSpell> LOB_CREEPER_SPELL = SpellRegistry.registerSpell(new LobCreeperSpell());
    public static final Supplier<AbstractSpell> SHIELD_SPELL = SpellRegistry.registerSpell(new ShieldSpell());
    public static final Supplier<AbstractSpell> SPECTRAL_HAMMER_SPELL = SpellRegistry.registerSpell(new SpectralHammerSpell());
    public static final Supplier<AbstractSpell> SUMMON_HORSE_SPELL = SpellRegistry.registerSpell(new SummonHorseSpell());
    public static final Supplier<AbstractSpell> SUMMON_VEX_SPELL = SpellRegistry.registerSpell(new SummonVexSpell());
    public static final Supplier<AbstractSpell> SLOW_SPELL = SpellRegistry.registerSpell(new SlowSpell());
    public static final Supplier<AbstractSpell> ARROW_VOLLEY_SPELL = SpellRegistry.registerSpell(new ArrowVolleySpell());
    public static final Supplier<AbstractSpell> WOLOLO_SPELL = SpellRegistry.registerSpell(new WololoSpell());
    public static final Supplier<AbstractSpell> THROW_SPELL = SpellRegistry.registerSpell(new ThrowSpell());
    public static final Supplier<AbstractSpell> BLAZE_STORM_SPELL = SpellRegistry.registerSpell(new BlazeStormSpell());
    public static final Supplier<AbstractSpell> BURNING_DASH_SPELL = SpellRegistry.registerSpell(new BurningDashSpell());
    public static final Supplier<AbstractSpell> FIREBALL_SPELL = SpellRegistry.registerSpell(new FireballSpell());
    public static final Supplier<AbstractSpell> FIREBOLT_SPELL = SpellRegistry.registerSpell(new FireboltSpell());
    public static final Supplier<AbstractSpell> FIRE_BREATH_SPELL = SpellRegistry.registerSpell(new FireBreathSpell());
    public static final Supplier<AbstractSpell> MAGMA_BOMB_SPELL = SpellRegistry.registerSpell(new MagmaBombSpell());
    public static final Supplier<AbstractSpell> WALL_OF_FIRE_SPELL = SpellRegistry.registerSpell(new WallOfFireSpell());
    public static final Supplier<AbstractSpell> HEAT_SURGE_SPELL = SpellRegistry.registerSpell(new HeatSurgeSpell());
    public static final Supplier<AbstractSpell> FLAMING_STRIKE_SPELL = SpellRegistry.registerSpell(new FlamingStrikeSpell());
    public static final Supplier<AbstractSpell> SCORCH_SPELL = SpellRegistry.registerSpell(new ScorchSpell());
    public static final Supplier<AbstractSpell> FLAMING_BARRAGE_SPELL = SpellRegistry.registerSpell(new FlamingBarrageSpell());
    public static final Supplier<AbstractSpell> FIRE_ARROW_SPELL = SpellRegistry.registerSpell(new FireArrowSpell());
    public static final Supplier<AbstractSpell> RAISE_HELL_SPELL = SpellRegistry.registerSpell(new RaiseHellSpell());
    public static final Supplier<AbstractSpell> ANGEL_WINGS_SPELL = SpellRegistry.registerSpell(new AngelWingsSpell());
    public static final Supplier<AbstractSpell> BLESSING_OF_LIFE_SPELL = SpellRegistry.registerSpell(new BlessingOfLifeSpell());
    public static final Supplier<AbstractSpell> CLOUD_OF_REGENERATION_SPELL = SpellRegistry.registerSpell(new CloudOfRegenerationSpell());
    public static final Supplier<AbstractSpell> FORTIFY_SPELL = SpellRegistry.registerSpell(new FortifySpell());
    public static final Supplier<AbstractSpell> GREATER_HEAL_SPELL = SpellRegistry.registerSpell(new GreaterHealSpell());
    public static final Supplier<AbstractSpell> GUIDING_BOLT_SPELL = SpellRegistry.registerSpell(new GuidingBoltSpell());
    public static final Supplier<AbstractSpell> HEALING_CIRCLE_SPELL = SpellRegistry.registerSpell(new HealingCircleSpell());
    public static final Supplier<AbstractSpell> HEAL_SPELL = SpellRegistry.registerSpell(new HealSpell());
    public static final Supplier<AbstractSpell> SUNBEAM_SPELL = SpellRegistry.registerSpell(new SunbeamSpell());
    public static final Supplier<AbstractSpell> WISP_SPELL = SpellRegistry.registerSpell(new WispSpell());
    public static final Supplier<AbstractSpell> DIVINE_SMITE_SPELL = SpellRegistry.registerSpell(new DivineSmiteSpell());
    public static final Supplier<AbstractSpell> HASTE_SPELL = SpellRegistry.registerSpell(new HasteSpell());
    public static final Supplier<AbstractSpell> CLEANSE_SPELL = SpellRegistry.registerSpell(new CleanseSpell());
    public static final Supplier<AbstractSpell> CONE_OF_COLD_SPELL = SpellRegistry.registerSpell(new ConeOfColdSpell());
    public static final Supplier<AbstractSpell> FROST_STEP_SPELL = SpellRegistry.registerSpell(new FrostStepSpell());
    public static final Supplier<AbstractSpell> ICE_BLOCK_SPELL = SpellRegistry.registerSpell(new IceBlockSpell());
    public static final Supplier<AbstractSpell> ICICLE_SPELL = SpellRegistry.registerSpell(new IcicleSpell());
    public static final Supplier<AbstractSpell> SUMMON_POLAR_BEAR_SPELL = SpellRegistry.registerSpell(new SummonPolarBearSpell());
    public static final Supplier<AbstractSpell> RAY_OF_FROST_SPELL = SpellRegistry.registerSpell(new RayOfFrostSpell());
    public static final Supplier<AbstractSpell> FROSTWAVE_SPELL = SpellRegistry.registerSpell(new FrostwaveSpell());
    public static final Supplier<AbstractSpell> ICE_SPIKES_SPELL = SpellRegistry.registerSpell(new IceSpikesSpell());
    public static final Supplier<AbstractSpell> ICE_TOMB_SPELL = SpellRegistry.registerSpell(new IceTombSpell());
    public static final Supplier<AbstractSpell> SNOWBALL_SPELL = SpellRegistry.registerSpell(new SnowballSpell());
    public static final Supplier<AbstractSpell> FROSTBITE_SPELL = SpellRegistry.registerSpell(new FrostbiteSpell());
    public static final Supplier<AbstractSpell> ASCENSION_SPELL = SpellRegistry.registerSpell(new AscensionSpell());
    public static final Supplier<AbstractSpell> CHAIN_LIGHTNING_SPELL = SpellRegistry.registerSpell(new ChainLightningSpell());
    public static final Supplier<AbstractSpell> CHARGE_SPELL = SpellRegistry.registerSpell(new ChargeSpell());
    public static final Supplier<AbstractSpell> ELECTROCUTE_SPELL = SpellRegistry.registerSpell(new ElectrocuteSpell());
    public static final Supplier<AbstractSpell> LIGHTNING_BOLT_SPELL = SpellRegistry.registerSpell(new LightningBoltSpell());
    public static final Supplier<AbstractSpell> LIGHTNING_LANCE_SPELL = SpellRegistry.registerSpell(new LightningLanceSpell());
    public static final Supplier<AbstractSpell> SHOCKWAVE_SPELL = SpellRegistry.registerSpell(new ShockwaveSpell());
    public static final Supplier<AbstractSpell> THUNDERSTORM_SPELL = SpellRegistry.registerSpell(new ThunderstormSpell());
    public static final Supplier<AbstractSpell> BALL_LIGHTNING_SPELL = SpellRegistry.registerSpell(new BallLightningSpell());
    public static final Supplier<AbstractSpell> VOLT_STRIKE_SPELL = SpellRegistry.registerSpell(new VoltStrikeSpell());
    public static final Supplier<AbstractSpell> ACID_ORB_SPELL = SpellRegistry.registerSpell(new AcidOrbSpell());
    public static final Supplier<AbstractSpell> BLIGHT_SPELL = SpellRegistry.registerSpell(new BlightSpell());
    public static final Supplier<AbstractSpell> POISON_ARROW_SPELL = SpellRegistry.registerSpell(new PoisonArrowSpell());
    public static final Supplier<AbstractSpell> POISON_BREATH_SPELL = SpellRegistry.registerSpell(new PoisonBreathSpell());
    public static final Supplier<AbstractSpell> POISON_SPLASH_SPELL = SpellRegistry.registerSpell(new PoisonSplashSpell());
    public static final Supplier<AbstractSpell> ROOT_SPELL = SpellRegistry.registerSpell(new RootSpell());
    public static final Supplier<AbstractSpell> SPIDER_ASPECT_SPELL = SpellRegistry.registerSpell(new SpiderAspectSpell());
    public static final Supplier<AbstractSpell> FIREFLY_SWARM_SPELL = SpellRegistry.registerSpell(new FireflySwarmSpell());
    public static final Supplier<AbstractSpell> OAKSKIN_SPELL = SpellRegistry.registerSpell(new OakskinSpell());
    public static final Supplier<AbstractSpell> EARTHQUAKE_SPELL = SpellRegistry.registerSpell(new EarthquakeSpell());
    public static final Supplier<AbstractSpell> STOMP_SPELL = SpellRegistry.registerSpell(new StompSpell());
    public static final Supplier<AbstractSpell> GLUTTONY_SPELL = SpellRegistry.registerSpell(new GluttonySpell());
    public static final Supplier<AbstractSpell> TOUCH_DIG = SpellRegistry.registerSpell(new TouchDigSpell());
    public static final Supplier<AbstractSpell> ABYSSAL_SHROUD_SPELL = SpellRegistry.registerSpell(new AbyssalShroudSpell());
    public static final Supplier<AbstractSpell> SCULK_TENTACLES_SPELL = SpellRegistry.registerSpell(new SculkTentaclesSpell());
    public static final Supplier<AbstractSpell> SONIC_BOOM_SPELL = SpellRegistry.registerSpell(new SonicBoomSpell());
    public static final Supplier<AbstractSpell> PLANAR_SIGHT_SPELL = SpellRegistry.registerSpell(new PlanarSightSpell());
    public static final Supplier<AbstractSpell> TELEKINESIS_SPELL = SpellRegistry.registerSpell(new TelekinesisSpell());
    public static final Supplier<AbstractSpell> ELDRITCH_BLAST_SPELL = SpellRegistry.registerSpell(new EldritchBlastSpell());
    public static final Supplier<AbstractSpell> POCKET_DIMENSION_SPELL = SpellRegistry.registerSpell(new PocketDimensionSpell());

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    public static void registerRegistry(NewRegistryEvent event) {
        IronsSpellbooks.LOGGER.debug("SpellRegistry.registerRegistry");
        event.register(REGISTRY);
    }

    public static NoneSpell none() {
        return noneSpell;
    }

    private static Supplier<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

    public static AbstractSpell getSpell(String spellId) {
        return SpellRegistry.getSpell(ResourceLocation.parse((String)spellId));
    }

    public static List<AbstractSpell> getEnabledSpells() {
        return REGISTRY.stream().filter(AbstractSpell::isEnabled).toList();
    }

    public static List<AbstractSpell> getSpellsForSchool(SchoolType schoolType) {
        return SCHOOLS_TO_SPELLS.computeIfAbsent(schoolType, school -> REGISTRY.stream().filter(spell -> spell.getSchoolType() == school).collect(Collectors.toList()));
    }

    public static AbstractSpell getSpell(ResourceLocation resourceLocation) {
        AbstractSpell spell = (AbstractSpell)REGISTRY.get(resourceLocation);
        if (spell == null) {
            return noneSpell;
        }
        return spell;
    }

    public static void onConfigReload() {
        SCHOOLS_TO_SPELLS.clear();
    }
}

