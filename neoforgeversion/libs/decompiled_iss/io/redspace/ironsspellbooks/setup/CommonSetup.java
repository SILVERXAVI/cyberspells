/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.SpawnPlacementTypes
 *  net.minecraft.world.entity.animal.PolarBear
 *  net.minecraft.world.entity.monster.Vindicator
 *  net.minecraft.world.entity.monster.Zombie
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.levelgen.Heightmap$Types
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.fml.config.ModConfig$Type
 *  net.neoforged.fml.event.config.ModConfigEvent$Loading
 *  net.neoforged.fml.event.config.ModConfigEvent$Reloading
 *  net.neoforged.neoforge.capabilities.Capabilities$FluidHandler
 *  net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
 *  net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
 *  net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent
 *  net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent$Operation
 */
package io.redspace.ironsspellbooks.setup;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.entity.mobs.SummonedHorse;
import io.redspace.ironsspellbooks.entity.mobs.SummonedSkeleton;
import io.redspace.ironsspellbooks.entity.mobs.SummonedVex;
import io.redspace.ironsspellbooks.entity.mobs.SummonedZombie;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingBoss;
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
import io.redspace.ironsspellbooks.entity.spells.root.RootEntity;
import io.redspace.ironsspellbooks.entity.spells.spectral_hammer.SpectralHammer;
import io.redspace.ironsspellbooks.entity.spells.summoned_weapons.SummonedClaymoreEntity;
import io.redspace.ironsspellbooks.entity.spells.summoned_weapons.SummonedRapierEntity;
import io.redspace.ironsspellbooks.entity.spells.summoned_weapons.SummonedSwordEntity;
import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import io.redspace.ironsspellbooks.entity.spells.wisp.WispEntity;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@EventBusSubscriber(modid="irons_spellbooks", bus=EventBusSubscriber.Bus.MOD)
public class CommonSetup {
    @SubscribeEvent
    public static void onModConfigLoadingEvent(ModConfigEvent.Loading event) {
        if (event.getConfig().getType() == ModConfig.Type.SERVER) {
            SpellRegistry.onConfigReload();
            ServerConfigs.onConfigReload();
        } else if (event.getConfig().getType() == ModConfig.Type.CLIENT) {
            ClientConfigs.onConfigReload();
        }
    }

    @SubscribeEvent
    public static void onModConfigReloadingEvent(ModConfigEvent.Reloading event) {
        if (event.getConfig().getType() == ModConfig.Type.SERVER) {
            SpellRegistry.onConfigReload();
            ServerConfigs.onConfigReload();
        } else if (event.getConfig().getType() == ModConfig.Type.CLIENT) {
            ClientConfigs.onConfigReload();
        }
    }

    @SubscribeEvent
    public static void registerCapabilitiesEvent(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, (BlockEntityType)BlockRegistry.ALCHEMIST_CAULDRON_TILE.get(), (be, context) -> {
            if (be.fluidCapability == null) {
                be.refreshCapabilities();
            }
            return be.fluidCapability;
        });
    }

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put((EntityType)EntityRegistry.DEBUG_WIZARD.get(), DebugWizard.prepareAttributes().build());
        event.put((EntityType)EntityRegistry.PYROMANCER.get(), PyromancerEntity.prepareAttributes().build());
        event.put((EntityType)EntityRegistry.NECROMANCER.get(), NecromancerEntity.prepareAttributes().build());
        event.put((EntityType)EntityRegistry.SPECTRAL_STEED.get(), SummonedHorse.prepareAttributes().build());
        event.put((EntityType)EntityRegistry.WISP.get(), WispEntity.prepareAttributes().build());
        event.put((EntityType)EntityRegistry.SPECTRAL_HAMMER.get(), SpectralHammer.prepareAttributes().build());
        event.put((EntityType)EntityRegistry.SUMMONED_VEX.get(), SummonedVex.createAttributes().build());
        event.put((EntityType)EntityRegistry.SUMMONED_ZOMBIE.get(), SummonedZombie.createAttributes().build());
        event.put((EntityType)EntityRegistry.SUMMONED_SKELETON.get(), SummonedSkeleton.createAttributes().build());
        event.put((EntityType)EntityRegistry.FROZEN_HUMANOID.get(), FrozenHumanoid.prepareAttributes().build());
        event.put((EntityType)EntityRegistry.SUMMONED_POLAR_BEAR.get(), PolarBear.createAttributes().build());
        event.put((EntityType)EntityRegistry.DEAD_KING.get(), DeadKingBoss.prepareAttributes().build());
        event.put((EntityType)EntityRegistry.DEAD_KING_CORPSE.get(), DeadKingBoss.prepareAttributes().build());
        event.put((EntityType)EntityRegistry.CATACOMBS_ZOMBIE.get(), Zombie.createAttributes().build());
        event.put((EntityType)EntityRegistry.MAGEHUNTER_VINDICATOR.get(), Vindicator.createAttributes().build());
        event.put((EntityType)EntityRegistry.ARCHEVOKER.get(), ArchevokerEntity.prepareAttributes().build());
        event.put((EntityType)EntityRegistry.PRIEST.get(), PriestEntity.prepareAttributes().build());
        event.put((EntityType)EntityRegistry.KEEPER.get(), KeeperEntity.prepareAttributes().build());
        event.put((EntityType)EntityRegistry.SCULK_TENTACLE.get(), VoidTentacle.createLivingAttributes().build());
        event.put((EntityType)EntityRegistry.CRYOMANCER.get(), CryomancerEntity.prepareAttributes().build());
        event.put((EntityType)EntityRegistry.ROOT.get(), RootEntity.createLivingAttributes().build());
        event.put((EntityType)EntityRegistry.FIREFLY_SWARM.get(), WispEntity.prepareAttributes().build());
        event.put((EntityType)EntityRegistry.APOTHECARIST.get(), ApothecaristEntity.prepareAttributes().build());
        event.put((EntityType)EntityRegistry.CULTIST.get(), CultistEntity.prepareAttributes().build());
        event.put((EntityType)EntityRegistry.FIRE_BOSS.get(), FireBossEntity.prepareAttributes().build());
        event.put((EntityType)EntityRegistry.CURSED_ARMOR_STAND.get(), CursedArmorStandEntity.prepareAttributes().build());
        event.put((EntityType)EntityRegistry.SUMMONED_SWORD.get(), SummonedSwordEntity.prepareAttributes().build());
        event.put((EntityType)EntityRegistry.SUMMONED_CLAYMORE.get(), SummonedClaymoreEntity.prepareAttributes().build());
        event.put((EntityType)EntityRegistry.SUMMONED_RAPIER.get(), SummonedRapierEntity.prepareAttributes().build());
        event.put((EntityType)EntityRegistry.ICE_SPIDER.get(), IceSpiderEntity.prepareAttributes().build());
    }

    @SubscribeEvent
    public static void spawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register((EntityType)EntityRegistry.NECROMANCER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type, serverLevelAccessor, spawnType, blockPos, random) -> Utils.checkMonsterSpawnRules(serverLevelAccessor, spawnType, blockPos, random), RegisterSpawnPlacementsEvent.Operation.OR);
    }
}

