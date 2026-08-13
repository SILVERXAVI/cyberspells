/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.packs.PackLocationInfo
 *  net.minecraft.server.packs.PackSelectionConfig
 *  net.minecraft.server.packs.PackType
 *  net.minecraft.server.packs.PathPackResources
 *  net.minecraft.server.packs.repository.BuiltInPackSource
 *  net.minecraft.server.packs.repository.Pack
 *  net.minecraft.server.packs.repository.Pack$Position
 *  net.minecraft.server.packs.repository.Pack$ResourcesSupplier
 *  net.minecraft.server.packs.repository.PackSource
 *  net.minecraft.server.packs.resources.PreparableReloadListener
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.fml.ModContainer
 *  net.neoforged.fml.ModList
 *  net.neoforged.fml.common.Mod
 *  net.neoforged.fml.config.IConfigSpec
 *  net.neoforged.fml.config.ModConfig$Type
 *  net.neoforged.fml.event.lifecycle.InterModEnqueueEvent
 *  net.neoforged.fml.event.lifecycle.InterModProcessEvent
 *  net.neoforged.neoforge.common.NeoForge
 *  net.neoforged.neoforge.event.AddPackFindersEvent
 *  net.neoforged.neoforge.event.AddReloadListenerEvent
 *  org.jetbrains.annotations.NotNull
 *  org.slf4j.Logger
 */
package io.redspace.ironsspellbooks;

import com.mojang.logging.LogUtils;
import io.redspace.ironsspellbooks.api.config.SpellConfigManager;
import io.redspace.ironsspellbooks.api.magic.MagicHelper;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.registries.ArmorMaterialRegistry;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.CommandArgumentRegistry;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.registries.CreativeTabRegistry;
import io.redspace.ironsspellbooks.registries.DataAttachmentRegistry;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.FeatureRegistry;
import io.redspace.ironsspellbooks.registries.FluidRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.LootRegistry;
import io.redspace.ironsspellbooks.registries.MenuRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import io.redspace.ironsspellbooks.registries.PoiTypeRegistry;
import io.redspace.ironsspellbooks.registries.PotionRegistry;
import io.redspace.ironsspellbooks.registries.RecipeRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.registries.StructureElementRegistry;
import io.redspace.ironsspellbooks.registries.StructureProcessorRegistry;
import io.redspace.ironsspellbooks.registries.UpgradeOrbTypeRegistry;
import io.redspace.ironsspellbooks.setup.ModSetup;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(value="irons_spellbooks")
public class IronsSpellbooks {
    public static final String MODID = "irons_spellbooks";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static MagicManager MAGIC_MANAGER;
    public static MinecraftServer MCS;
    public static ServerLevel OVERWORLD;

    public IronsSpellbooks(IEventBus modEventBus, ModContainer modContainer) {
        ModSetup.setup();
        MAGIC_MANAGER = new MagicManager();
        MagicHelper.MAGIC_MANAGER = MAGIC_MANAGER;
        modEventBus.addListener(ModSetup::init);
        modEventBus.addListener(this::enqueueIMC);
        modEventBus.addListener(this::processIMC);
        modEventBus.addListener(SchoolRegistry::registerRegistry);
        modEventBus.addListener(SpellRegistry::registerRegistry);
        modEventBus.addListener(UpgradeOrbTypeRegistry::registerDatapackRegistries);
        SchoolRegistry.register(modEventBus);
        SpellRegistry.register(modEventBus);
        ItemRegistry.register(modEventBus);
        AttributeRegistry.register(modEventBus);
        BlockRegistry.register(modEventBus);
        MenuRegistry.register(modEventBus);
        EntityRegistry.register(modEventBus);
        LootRegistry.register(modEventBus);
        MobEffectRegistry.register(modEventBus);
        ParticleRegistry.register(modEventBus);
        SoundRegistry.register(modEventBus);
        FeatureRegistry.register(modEventBus);
        PotionRegistry.register(modEventBus);
        CommandArgumentRegistry.register(modEventBus);
        StructureProcessorRegistry.register(modEventBus);
        StructureElementRegistry.register(modEventBus);
        CreativeTabRegistry.register(modEventBus);
        DataAttachmentRegistry.register(modEventBus);
        ArmorMaterialRegistry.register(modEventBus);
        ComponentRegistry.register(modEventBus);
        PoiTypeRegistry.register(modEventBus);
        FluidRegistry.register(modEventBus);
        RecipeRegistry.register(modEventBus);
        modEventBus.addListener(this::addPackFinders);
        NeoForge.EVENT_BUS.addListener(this::addServerDataListeners);
        modContainer.registerConfig(ModConfig.Type.CLIENT, (IConfigSpec)ClientConfigs.SPEC, String.format("%s-client.toml", MODID));
        modContainer.registerConfig(ModConfig.Type.SERVER, (IConfigSpec)ServerConfigs.SPEC, String.format("%s-server.toml", MODID));
    }

    public void addServerDataListeners(AddReloadListenerEvent event) {
        SpellConfigManager.INSTANCE = new SpellConfigManager();
        event.addListener((PreparableReloadListener)SpellConfigManager.INSTANCE);
    }

    public void addPackFinders(AddPackFindersEvent event) {
        LOGGER.debug("addPackFinders");
        try {
            if (event.getPackType() == PackType.CLIENT_RESOURCES) {
                IronsSpellbooks.addBuiltinPack(event, "legacy_dead_king_resource_pack", (Component)Component.literal((String)"Legacy Dead King"));
            }
        }
        catch (IOException ex) {
            LOGGER.error("Failed to load a builtin resource pack! If you are seeing this message, please report an issue to https://github.com/iron431/Irons-Spells-n-Spellbooks/issues");
        }
    }

    private static void addBuiltinPack(AddPackFindersEvent event, String filename, Component displayName) throws IOException {
        filename = "builtin_resource_packs/" + (String)filename;
        String id = "builtin/" + (String)filename;
        Path resourcePath = ModList.get().getModFileById(MODID).getFile().findResource(new String[]{filename});
        Pack pack = Pack.readMetaAndCreate((PackLocationInfo)new PackLocationInfo(id, displayName, PackSource.BUILT_IN, Optional.empty()), (Pack.ResourcesSupplier)BuiltInPackSource.fromName(path -> new PathPackResources(path, resourcePath)), (PackType)PackType.CLIENT_RESOURCES, (PackSelectionConfig)new PackSelectionConfig(false, Pack.Position.TOP, false));
        event.addRepositorySource(packConsumer -> packConsumer.accept(pack));
    }

    private void enqueueIMC(InterModEnqueueEvent event) {
    }

    private void processIMC(InterModProcessEvent event) {
        LOGGER.info("Got IMC {}", event.getIMCStream().map(m -> m.messageSupplier().get()).collect(Collectors.toList()));
    }

    public static ResourceLocation id(@NotNull String path) {
        return ResourceLocation.fromNamespaceAndPath((String)MODID, (String)path);
    }
}

