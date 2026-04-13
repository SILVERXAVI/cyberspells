/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.resources.PlayerSkin$Model
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.TagKey
 *  net.minecraft.util.FastColor$ARGB32
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.HumanoidArm
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 */
package com.perigrine3.createcybernetics.client.skin;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.client.skin.CybereyeOverlayHandler;
import com.perigrine3.createcybernetics.client.skin.FaceplateSkinOverrideClient;
import com.perigrine3.createcybernetics.client.skin.SkinHighlight;
import com.perigrine3.createcybernetics.client.skin.SkinHighlightRender;
import com.perigrine3.createcybernetics.client.skin.SkinModifier;
import com.perigrine3.createcybernetics.client.skin.SkinModifierState;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.compat.ModCompats;
import com.perigrine3.createcybernetics.compat.curios.CuriosCompat;
import com.perigrine3.createcybernetics.event.custom.FullBorgHandler;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.util.ModTags;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SkinModifierManager {
    private static final Map<UUID, SkinModifierState> PLAYER_STATES = new HashMap<UUID, SkinModifierState>();
    private static final String ENTITY_HOLO_SNAPSHOT_KEY = "cc_holo_snapshot";
    private static boolean SNAP_REFLECT_READY = false;
    private static boolean SNAP_REFLECT_FAILED = false;
    private static Constructor<?> SNAP_CTOR;
    private static Method SNAP_DESERIALIZE_1;
    private static Method SNAP_DESERIALIZE_2;
    private static final ResourceLocation MERMOD_SEA_NECKLACE_ID;
    private static DataComponentType<?> MERMOD_NECKLACE_MODIFIERS_COMPONENT;
    private static boolean MERMOD_REFLECTION_READY;
    private static boolean MERMOD_REFLECTION_FAILED;
    private static Method MERMOD_COMPONENT_MODIFIERS_METHOD;
    private static Method MERMOD_MODIFIER_ID_METHOD;
    private static final ResourceLocation MISSING_SKIN_TEXTURE;
    private static final ResourceLocation CYBEREYES_PRIMARY;
    private static final ResourceLocation CYBEREYES_SECONDARY;
    private static final ResourceLocation RIGHT_CYBERLEG_TEXTURE;
    private static final ResourceLocation COPPER_PLATED_RIGHT_CYBERLEG;
    private static final ResourceLocation IRON_PLATED_RIGHT_CYBERLEG;
    private static final ResourceLocation GOLD_PLATED_RIGHT_CYBERLEG;
    private static final ResourceLocation RIGHT_CYBERLEG_PRIMARY;
    private static final ResourceLocation RIGHT_CYBERLEG_SECONDARY;
    private static final ResourceLocation LEFT_CYBERLEG_TEXTURE;
    private static final ResourceLocation COPPER_PLATED_LEFT_CYBERLEG;
    private static final ResourceLocation IRON_PLATED_LEFT_CYBERLEG;
    private static final ResourceLocation GOLD_PLATED_LEFT_CYBERLEG;
    private static final ResourceLocation LEFT_CYBERLEG_PRIMARY;
    private static final ResourceLocation LEFT_CYBERLEG_SECONDARY;
    private static final ResourceLocation POLAR_BEAR_FUR_TEXTURE;
    private static final ResourceLocation SPINAL_INJECTOR_TEXTURE;
    private static final ResourceLocation SPINAL_INJECTOR_HIGHLIGHT_TEXTURE;
    private static final ResourceLocation SANDEVISTAN_TEXTURE;
    private static final ResourceLocation SANDEVISTAN_HIGHLIGHT_TEXTURE;
    private static final ResourceLocation DEPLOYABLE_ELYTRA_TEXTURE;
    private static final ResourceLocation DEPLOYABLE_ELYTRA_HIGHLIGHT_TEXTURE;
    private static final ResourceLocation CYBERDECK_TEXTURE;
    private static final ResourceLocation GILLS_TEXTURE;
    private static final ResourceLocation CHIPWARE_INACTIVE;
    private static final ResourceLocation CHIPWARE_ACTIVE;
    private static final ResourceLocation FURNACE;
    private static final ResourceLocation FURNACE_ACTIVE;
    private static final ResourceLocation FURNACE_HIGHLIGHT;
    private static final ResourceLocation SPIDER_EYES;
    private static final ResourceLocation SAMSON_EYES_DYED;
    private static final ResourceLocation ECLIPSE_EYES_DYED;
    private static final ResourceLocation SPYDER_EYES_DYED;
    private static final ResourceLocation AQUARIUS_EYES_DYED;
    private static final ResourceLocation DYMOND_EYES_DYED;
    private static final ResourceLocation DRAGOON_EYES_DYED;
    private static final ResourceLocation COPERNICUS_EYES_DYED;
    private static final ResourceLocation GENOS_EYES_DYED;
    private static final ResourceLocation GENOS_HIGHLIGHT;
    private static final ResourceLocation ECLIPSE_VISOR_TRIMMED;
    private static final ResourceLocation SPYDER_VISOR_TRIMMED;
    private static final ResourceLocation LEFT_CYBERARM_TEXTURE_WIDE;
    private static final ResourceLocation COPPER_PLATED_LEFT_CYBERARM_WIDE;
    private static final ResourceLocation IRON_PLATED_LEFT_CYBERARM_WIDE;
    private static final ResourceLocation GOLD_PLATED_LEFT_CYBERARM_WIDE;
    private static final ResourceLocation LEFT_CYBERARM_PRIMARY_WIDE;
    private static final ResourceLocation LEFT_CYBERARM_SECONDARY_WIDE;
    private static final ResourceLocation RIGHT_CYBERARM_TEXTURE_WIDE;
    private static final ResourceLocation COPPER_PLATED_RIGHT_CYBERARM_WIDE;
    private static final ResourceLocation IRON_PLATED_RIGHT_CYBERARM_WIDE;
    private static final ResourceLocation GOLD_PLATED_RIGHT_CYBERARM_WIDE;
    private static final ResourceLocation RIGHT_CYBERARM_PRIMARY_WIDE;
    private static final ResourceLocation RIGHT_CYBERARM_SECONDARY_WIDE;
    private static final ResourceLocation KNUCKLES_LARM_WIDE;
    private static final ResourceLocation KNUCKLES_RARM_WIDE;
    private static final ResourceLocation SYNTHSKIN_TEXTURE_WIDE;
    private static final ResourceLocation NETHERPLATED_SKIN_TEXTURE_WIDE;
    private static final ResourceLocation FIRESTARTER_LARM_WIDE;
    private static final ResourceLocation FIRESTARTER_RARM_WIDE;
    private static final ResourceLocation FLYWHEEL_LARM_WIDE;
    private static final ResourceLocation FLYWHEEL_RARM_WIDE;
    private static final ResourceLocation MANASKIN_WIDE;
    private static final ResourceLocation SAMSON_WIDE;
    private static final ResourceLocation SAMSON_WIDE_DYED;
    private static final ResourceLocation ECLIPSE_WIDE;
    private static final ResourceLocation ECLIPSE_WIDE_DYED;
    private static final ResourceLocation SPYDER_WIDE;
    private static final ResourceLocation SPYDER_WIDE_DYED;
    private static final ResourceLocation WINGMAN_WIDE;
    private static final ResourceLocation WINGMAN_WIDE_DYED;
    private static final ResourceLocation AQUARIUS_WIDE;
    private static final ResourceLocation AQUARIUS_WIDE_DYED;
    private static final ResourceLocation DYMOND_WIDE;
    private static final ResourceLocation DYMOND_WIDE_DYED;
    private static final ResourceLocation DRAGOON_WIDE;
    private static final ResourceLocation DRAGOON_WIDE_DYED;
    private static final ResourceLocation COPERNICUS_WIDE;
    private static final ResourceLocation COPERNICUS_WIDE_DYED;
    private static final ResourceLocation GENOS_WIDE;
    private static final ResourceLocation GENOS_WIDE_DYED;
    private static final ResourceLocation LEFT_CYBERARM_TEXTURE_SLIM;
    private static final ResourceLocation COPPER_PLATED_LEFT_CYBERARM_SLIM;
    private static final ResourceLocation IRON_PLATED_LEFT_CYBERARM_SLIM;
    private static final ResourceLocation GOLD_PLATED_LEFT_CYBERARM_SLIM;
    private static final ResourceLocation LEFT_CYBERARM_PRIMARY_SLIM;
    private static final ResourceLocation LEFT_CYBERARM_SECONDARY_SLIM;
    private static final ResourceLocation RIGHT_CYBERARM_TEXTURE_SLIM;
    private static final ResourceLocation COPPER_PLATED_RIGHT_CYBERARM_SLIM;
    private static final ResourceLocation IRON_PLATED_RIGHT_CYBERARM_SLIM;
    private static final ResourceLocation GOLD_PLATED_RIGHT_CYBERARM_SLIM;
    private static final ResourceLocation RIGHT_CYBERARM_PRIMARY_SLIM;
    private static final ResourceLocation RIGHT_CYBERARM_SECONDARY_SLIM;
    private static final ResourceLocation KNUCKLES_LARM_SLIM;
    private static final ResourceLocation KNUCKLES_RARM_SLIM;
    private static final ResourceLocation SYNTHSKIN_TEXTURE_SLIM;
    private static final ResourceLocation NETHERPLATED_SKIN_TEXTURE_SLIM;
    private static final ResourceLocation FIRESTARTER_LARM_SLIM;
    private static final ResourceLocation FIRESTARTER_RARM_SLIM;
    private static final ResourceLocation FLYWHEEL_LARM_SLIM;
    private static final ResourceLocation FLYWHEEL_RARM_SLIM;
    private static final ResourceLocation MANASKIN_SLIM;
    private static final ResourceLocation SAMSON_SLIM;
    private static final ResourceLocation SAMSON_SLIM_DYED;
    private static final ResourceLocation ECLIPSE_SLIM;
    private static final ResourceLocation ECLIPSE_SLIM_DYED;
    private static final ResourceLocation SPYDER_SLIM;
    private static final ResourceLocation SPYDER_SLIM_DYED;
    private static final ResourceLocation WINGMAN_SLIM;
    private static final ResourceLocation WINGMAN_SLIM_DYED;
    private static final ResourceLocation AQUARIUS_SLIM;
    private static final ResourceLocation AQUARIUS_SLIM_DYED;
    private static final ResourceLocation DYMOND_SLIM;
    private static final ResourceLocation DYMOND_SLIM_DYED;
    private static final ResourceLocation DRAGOON_SLIM;
    private static final ResourceLocation DRAGOON_SLIM_DYED;
    private static final ResourceLocation COPERNICUS_SLIM;
    private static final ResourceLocation COPERNICUS_SLIM_DYED;
    private static final ResourceLocation GENOS_SLIM;
    private static final ResourceLocation GENOS_SLIM_DYED;
    private static final ResourceLocation BOLT_LEFT_LEG;
    private static final ResourceLocation BOLT_LEFT_SLIM;
    private static final ResourceLocation BOLT_LEFT_WIDE;
    private static final ResourceLocation BOLT_RIGHT_LEG;
    private static final ResourceLocation BOLT_RIGHT_SLIM;
    private static final ResourceLocation BOLT_RIGHT_WIDE;
    private static final ResourceLocation BOLT_FULL_BODY;
    private static final ResourceLocation BOLT_SLIM_FULL_BODY;
    private static final ResourceLocation COAST_LEFT_LEG;
    private static final ResourceLocation COAST_LEFT_SLIM;
    private static final ResourceLocation COAST_LEFT_WIDE;
    private static final ResourceLocation COAST_RIGHT_LEG;
    private static final ResourceLocation COAST_RIGHT_SLIM;
    private static final ResourceLocation COAST_RIGHT_WIDE;
    private static final ResourceLocation COAST_FULL_BODY;
    private static final ResourceLocation COAST_SLIM_FULL_BODY;
    private static final ResourceLocation DUNE_LEFT_LEG;
    private static final ResourceLocation DUNE_LEFT_SLIM;
    private static final ResourceLocation DUNE_LEFT_WIDE;
    private static final ResourceLocation DUNE_RIGHT_LEG;
    private static final ResourceLocation DUNE_RIGHT_SLIM;
    private static final ResourceLocation DUNE_RIGHT_WIDE;
    private static final ResourceLocation DUNE_FULL_BODY;
    private static final ResourceLocation DUNE_SLIM_FULL_BODY;
    private static final ResourceLocation EYE_LEFT_LEG;
    private static final ResourceLocation EYE_LEFT_SLIM;
    private static final ResourceLocation EYE_LEFT_WIDE;
    private static final ResourceLocation EYE_RIGHT_LEG;
    private static final ResourceLocation EYE_RIGHT_SLIM;
    private static final ResourceLocation EYE_RIGHT_WIDE;
    private static final ResourceLocation EYE_FULL_BODY;
    private static final ResourceLocation EYE_SLIM_FULL_BODY;
    private static final ResourceLocation FLOW_LEFT_LEG;
    private static final ResourceLocation FLOW_LEFT_SLIM;
    private static final ResourceLocation FLOW_LEFT_WIDE;
    private static final ResourceLocation FLOW_RIGHT_LEG;
    private static final ResourceLocation FLOW_RIGHT_SLIM;
    private static final ResourceLocation FLOW_RIGHT_WIDE;
    private static final ResourceLocation FLOW_FULL_BODY;
    private static final ResourceLocation FLOW_SLIM_FULL_BODY;
    private static final ResourceLocation HOST_LEFT_LEG;
    private static final ResourceLocation HOST_LEFT_SLIM;
    private static final ResourceLocation HOST_LEFT_WIDE;
    private static final ResourceLocation HOST_RIGHT_LEG;
    private static final ResourceLocation HOST_RIGHT_SLIM;
    private static final ResourceLocation HOST_RIGHT_WIDE;
    private static final ResourceLocation HOST_FULL_BODY;
    private static final ResourceLocation HOST_SLIM_FULL_BODY;
    private static final ResourceLocation RAISER_LEFT_LEG;
    private static final ResourceLocation RAISER_LEFT_SLIM;
    private static final ResourceLocation RAISER_LEFT_WIDE;
    private static final ResourceLocation RAISER_RIGHT_LEG;
    private static final ResourceLocation RAISER_RIGHT_SLIM;
    private static final ResourceLocation RAISER_RIGHT_WIDE;
    private static final ResourceLocation RAISER_FULL_BODY;
    private static final ResourceLocation RAISER_SLIM_FULL_BODY;
    private static final ResourceLocation RIB_LEFT_LEG;
    private static final ResourceLocation RIB_LEFT_SLIM;
    private static final ResourceLocation RIB_LEFT_WIDE;
    private static final ResourceLocation RIB_RIGHT_LEG;
    private static final ResourceLocation RIB_RIGHT_SLIM;
    private static final ResourceLocation RIB_RIGHT_WIDE;
    private static final ResourceLocation RIB_FULL_BODY;
    private static final ResourceLocation RIB_SLIM_FULL_BODY;
    private static final ResourceLocation SENTRY_LEFT_LEG;
    private static final ResourceLocation SENTRY_LEFT_SLIM;
    private static final ResourceLocation SENTRY_LEFT_WIDE;
    private static final ResourceLocation SENTRY_RIGHT_LEG;
    private static final ResourceLocation SENTRY_RIGHT_SLIM;
    private static final ResourceLocation SENTRY_RIGHT_WIDE;
    private static final ResourceLocation SENTRY_FULL_BODY;
    private static final ResourceLocation SENTRY_SLIM_FULL_BODY;
    private static final ResourceLocation SHAPER_LEFT_LEG;
    private static final ResourceLocation SHAPER_LEFT_SLIM;
    private static final ResourceLocation SHAPER_LEFT_WIDE;
    private static final ResourceLocation SHAPER_RIGHT_LEG;
    private static final ResourceLocation SHAPER_RIGHT_SLIM;
    private static final ResourceLocation SHAPER_RIGHT_WIDE;
    private static final ResourceLocation SHAPER_FULL_BODY;
    private static final ResourceLocation SHAPER_SLIM_FULL_BODY;
    private static final ResourceLocation SILENCE_LEFT_LEG;
    private static final ResourceLocation SILENCE_LEFT_SLIM;
    private static final ResourceLocation SILENCE_LEFT_WIDE;
    private static final ResourceLocation SILENCE_RIGHT_LEG;
    private static final ResourceLocation SILENCE_RIGHT_SLIM;
    private static final ResourceLocation SILENCE_RIGHT_WIDE;
    private static final ResourceLocation SILENCE_FULL_BODY;
    private static final ResourceLocation SILENCE_SLIM_FULL_BODY;
    private static final ResourceLocation SNOUT_LEFT_LEG;
    private static final ResourceLocation SNOUT_LEFT_SLIM;
    private static final ResourceLocation SNOUT_LEFT_WIDE;
    private static final ResourceLocation SNOUT_RIGHT_LEG;
    private static final ResourceLocation SNOUT_RIGHT_SLIM;
    private static final ResourceLocation SNOUT_RIGHT_WIDE;
    private static final ResourceLocation SNOUT_FULL_BODY;
    private static final ResourceLocation SNOUT_SLIM_FULL_BODY;
    private static final ResourceLocation SPIRE_LEFT_LEG;
    private static final ResourceLocation SPIRE_LEFT_SLIM;
    private static final ResourceLocation SPIRE_LEFT_WIDE;
    private static final ResourceLocation SPIRE_RIGHT_LEG;
    private static final ResourceLocation SPIRE_RIGHT_SLIM;
    private static final ResourceLocation SPIRE_RIGHT_WIDE;
    private static final ResourceLocation SPIRE_FULL_BODY;
    private static final ResourceLocation SPIRE_SLIM_FULL_BODY;
    private static final ResourceLocation TIDE_LEFT_LEG;
    private static final ResourceLocation TIDE_LEFT_SLIM;
    private static final ResourceLocation TIDE_LEFT_WIDE;
    private static final ResourceLocation TIDE_RIGHT_LEG;
    private static final ResourceLocation TIDE_RIGHT_SLIM;
    private static final ResourceLocation TIDE_RIGHT_WIDE;
    private static final ResourceLocation TIDE_FULL_BODY;
    private static final ResourceLocation TIDE_SLIM_FULL_BODY;
    private static final ResourceLocation VEX_LEFT_LEG;
    private static final ResourceLocation VEX_LEFT_SLIM;
    private static final ResourceLocation VEX_LEFT_WIDE;
    private static final ResourceLocation VEX_RIGHT_LEG;
    private static final ResourceLocation VEX_RIGHT_SLIM;
    private static final ResourceLocation VEX_RIGHT_WIDE;
    private static final ResourceLocation VEX_FULL_BODY;
    private static final ResourceLocation VEX_SLIM_FULL_BODY;
    private static final ResourceLocation WARD_LEFT_LEG;
    private static final ResourceLocation WARD_LEFT_SLIM;
    private static final ResourceLocation WARD_LEFT_WIDE;
    private static final ResourceLocation WARD_RIGHT_LEG;
    private static final ResourceLocation WARD_RIGHT_SLIM;
    private static final ResourceLocation WARD_RIGHT_WIDE;
    private static final ResourceLocation WARD_FULL_BODY;
    private static final ResourceLocation WARD_SLIM_FULL_BODY;
    private static final ResourceLocation WAYFINDER_LEFT_LEG;
    private static final ResourceLocation WAYFINDER_LEFT_SLIM;
    private static final ResourceLocation WAYFINDER_LEFT_WIDE;
    private static final ResourceLocation WAYFINDER_RIGHT_LEG;
    private static final ResourceLocation WAYFINDER_RIGHT_SLIM;
    private static final ResourceLocation WAYFINDER_RIGHT_WIDE;
    private static final ResourceLocation WAYFINDER_FULL_BODY;
    private static final ResourceLocation WAYFINDER_SLIM_FULL_BODY;
    private static final ResourceLocation WILD_LEFT_LEG;
    private static final ResourceLocation WILD_LEFT_SLIM;
    private static final ResourceLocation WILD_LEFT_WIDE;
    private static final ResourceLocation WILD_RIGHT_LEG;
    private static final ResourceLocation WILD_RIGHT_SLIM;
    private static final ResourceLocation WILD_RIGHT_WIDE;
    private static final ResourceLocation WILD_FULL_BODY;
    private static final ResourceLocation WILD_SLIM_FULL_BODY;

    public static SkinModifierState getPlayerSkinState(AbstractClientPlayer player) {
        ResourceLocation tex;
        ResourceLocation tex2;
        String alias;
        FaceplateSkinOverrideClient.ResolvedSkin resolved;
        PlayerCyberwareData data = PlayerCyberwareData.getForVisual((Player)player, (HolderLookup.Provider)player.registryAccess());
        if (data == null) {
            data = SkinModifierManager.tryBuildCyberwareDataFromSnapshot(player);
        }
        if (data == null) {
            return null;
        }
        UUID playerId = player.getUUID();
        SkinModifierState state = PLAYER_STATES.computeIfAbsent(playerId, k -> new SkinModifierState());
        state.clearModifiers();
        Component custom = player.getCustomName();
        if (custom != null && (resolved = FaceplateSkinOverrideClient.getOrRequest(alias = custom.getString())) != null) {
            state.addModifier(new SkinModifier(resolved.texture(), resolved.texture(), -1, true));
        }
        boolean mermodTailActive = false;
        if (ModCompats.isInstalled("mermod")) {
            ResourceLocation key;
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            ItemStack necklaceCurio = CuriosCompat.findFirstCurio((LivingEntity)player, "necklace", st -> {
                ResourceLocation k = BuiltInRegistries.ITEM.getKey((Object)st.getItem());
                return MERMOD_SEA_NECKLACE_ID.equals((Object)k);
            }).orElse(ItemStack.EMPTY);
            boolean hasNecklaceEquipped = false;
            ItemStack necklaceStack = ItemStack.EMPTY;
            if (!chest.isEmpty() && MERMOD_SEA_NECKLACE_ID.equals((Object)(key = BuiltInRegistries.ITEM.getKey((Object)chest.getItem())))) {
                hasNecklaceEquipped = true;
                necklaceStack = chest;
            }
            if (!hasNecklaceEquipped && !necklaceCurio.isEmpty()) {
                hasNecklaceEquipped = true;
                necklaceStack = necklaceCurio;
            }
            if (hasNecklaceEquipped) {
                boolean inWater = player.isInWaterOrBubble();
                TagKey moisturizerTag = TagKey.create((ResourceKey)Registries.ITEM, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"mermod", (String)"tail_moisturizer_modifier"));
                boolean hasMoisturizerByItemTag = necklaceStack.getTags().anyMatch(t -> t.equals((Object)moisturizerTag));
                boolean hasMoisturizerByComponent = SkinModifierManager.hasMermodMoisturizerComponent(necklaceStack);
                boolean bl = mermodTailActive = inWater || hasMoisturizerByItemTag || hasMoisturizerByComponent;
            }
        }
        if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES) && data.isDyed((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES)) {
            int tint = data.dyeColor((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES);
            ResourceLocation dyn = CybereyeOverlayHandler.getOrBuildOverlay((Player)player);
            if (dyn != null) {
                state.addModifier(new SkinModifier(dyn, dyn, tint, false));
                state.addHighlight(new SkinHighlight(dyn, dyn, tint, true, true));
            }
        }
        boolean hasSynthSkin = data.hasSpecificItem((Item)ModItems.SKINUPGRADES_SYNTHSKIN.get(), CyberwareSlot.SKIN);
        boolean hasNetheritePlating = data.hasSpecificItem((Item)ModItems.SKINUPGRADES_NETHERITEPLATING.get(), CyberwareSlot.SKIN);
        if (!hasSynthSkin || !hasNetheritePlating) {
            if (hasSynthSkin) {
                int alpha = 100;
                int tint = FastColor.ARGB32.color((int)alpha, (int)255, (int)255, (int)255);
                state.removeModifier(new SkinModifier(LEFT_CYBERLEG_TEXTURE, LEFT_CYBERLEG_TEXTURE));
                state.removeModifier(new SkinModifier(RIGHT_CYBERLEG_TEXTURE, RIGHT_CYBERLEG_TEXTURE));
                state.removeModifier(new SkinModifier(LEFT_CYBERARM_TEXTURE_WIDE, LEFT_CYBERARM_TEXTURE_SLIM));
                state.removeModifier(new SkinModifier(RIGHT_CYBERARM_TEXTURE_WIDE, RIGHT_CYBERARM_TEXTURE_SLIM));
                if (data.hasSpecificItem((Item)ModItems.WETWARE_POLARBEARFUR.get(), CyberwareSlot.SKIN)) {
                    state.addModifier(new SkinModifier(POLAR_BEAR_FUR_TEXTURE, POLAR_BEAR_FUR_TEXTURE));
                }
                state.addModifier(new SkinModifier(SYNTHSKIN_TEXTURE_WIDE, SYNTHSKIN_TEXTURE_SLIM, tint, false, false, EnumSet.noneOf(SkinModifier.HideVanilla.class), EnumSet.noneOf(HumanoidArm.class), true));
                return state;
            }
            if (hasNetheritePlating) {
                state.removeModifier(new SkinModifier(LEFT_CYBERLEG_TEXTURE, LEFT_CYBERLEG_TEXTURE));
                state.removeModifier(new SkinModifier(RIGHT_CYBERLEG_TEXTURE, RIGHT_CYBERLEG_TEXTURE));
                state.removeModifier(new SkinModifier(LEFT_CYBERARM_TEXTURE_WIDE, LEFT_CYBERARM_TEXTURE_SLIM));
                state.removeModifier(new SkinModifier(RIGHT_CYBERARM_TEXTURE_WIDE, RIGHT_CYBERARM_TEXTURE_SLIM));
                state.addModifier(new SkinModifier(NETHERPLATED_SKIN_TEXTURE_WIDE, NETHERPLATED_SKIN_TEXTURE_SLIM, -1, true));
                return state;
            }
        }
        if (data.hasSpecificItem((Item)ModItems.WETWARE_WATERBREATHINGLUNGS.get(), CyberwareSlot.LUNGS)) {
            state.addModifier(new SkinModifier(GILLS_TEXTURE, GILLS_TEXTURE, -1, false));
        }
        if (!data.hasAnyTagged(ModTags.Items.SKIN_ITEMS, CyberwareSlot.SKIN)) {
            state.addModifier(new SkinModifier(MISSING_SKIN_TEXTURE, MISSING_SKIN_TEXTURE, -1, true));
        }
        if (!mermodTailActive) {
            if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG)) {
                state.addModifier(new SkinModifier(LEFT_CYBERLEG_TEXTURE, LEFT_CYBERLEG_TEXTURE, -1, false, EnumSet.of(SkinModifier.HideVanilla.LEFT_PANTS)));
                if (data.isDyed((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG)) {
                    int tint = data.dyeColor((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG);
                    state.addModifier(new SkinModifier(LEFT_CYBERLEG_PRIMARY, LEFT_CYBERLEG_PRIMARY, tint, false, EnumSet.of(SkinModifier.HideVanilla.LEFT_PANTS)));
                }
                if (data.isTrimmed((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG)) {
                    ResourceLocation patternId = data.trimPatternId((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG);
                    int tint = data.trimColor((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG);
                    tex2 = SkinModifierManager.resolveTrimOverlay(patternId, true, Limb.LEG, false);
                    if (tex2 != null) {
                        state.addModifier(new SkinModifier(tex2, tex2, tint, false, EnumSet.of(SkinModifier.HideVanilla.LEFT_PANTS)));
                    }
                }
            }
            if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG_COPPERPLATED.get(), CyberwareSlot.LLEG)) {
                state.addModifier(new SkinModifier(COPPER_PLATED_LEFT_CYBERLEG, COPPER_PLATED_LEFT_CYBERLEG, -1, false, EnumSet.of(SkinModifier.HideVanilla.LEFT_PANTS)));
                if (data.isTrimmed((Item)ModItems.BASECYBERWARE_LEFTLEG_COPPERPLATED.get(), CyberwareSlot.LLEG)) {
                    ResourceLocation patternId = data.trimPatternId((Item)ModItems.BASECYBERWARE_LEFTLEG_COPPERPLATED.get(), CyberwareSlot.LLEG);
                    int tint = data.trimColor((Item)ModItems.BASECYBERWARE_LEFTLEG_COPPERPLATED.get(), CyberwareSlot.LLEG);
                    tex2 = SkinModifierManager.resolveTrimOverlay(patternId, true, Limb.LEG, false);
                    if (tex2 != null) {
                        state.addModifier(new SkinModifier(tex2, tex2, tint, false, EnumSet.of(SkinModifier.HideVanilla.LEFT_PANTS)));
                    }
                }
            }
            if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG_IRONPLATED.get(), CyberwareSlot.LLEG)) {
                state.addModifier(new SkinModifier(IRON_PLATED_LEFT_CYBERLEG, IRON_PLATED_LEFT_CYBERLEG, -1, false, EnumSet.of(SkinModifier.HideVanilla.LEFT_PANTS)));
                if (data.isTrimmed((Item)ModItems.BASECYBERWARE_LEFTLEG_IRONPLATED.get(), CyberwareSlot.LLEG)) {
                    ResourceLocation patternId = data.trimPatternId((Item)ModItems.BASECYBERWARE_LEFTLEG_IRONPLATED.get(), CyberwareSlot.LLEG);
                    int tint = data.trimColor((Item)ModItems.BASECYBERWARE_LEFTLEG_IRONPLATED.get(), CyberwareSlot.LLEG);
                    tex2 = SkinModifierManager.resolveTrimOverlay(patternId, true, Limb.LEG, false);
                    if (tex2 != null) {
                        state.addModifier(new SkinModifier(tex2, tex2, tint, false, EnumSet.of(SkinModifier.HideVanilla.LEFT_PANTS)));
                    }
                }
            }
            if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG_GOLDPLATED.get(), CyberwareSlot.LLEG)) {
                state.addModifier(new SkinModifier(GOLD_PLATED_LEFT_CYBERLEG, GOLD_PLATED_LEFT_CYBERLEG, -1, false, EnumSet.of(SkinModifier.HideVanilla.LEFT_PANTS)));
                if (data.isTrimmed((Item)ModItems.BASECYBERWARE_LEFTLEG_GOLDPLATED.get(), CyberwareSlot.LLEG)) {
                    ResourceLocation patternId = data.trimPatternId((Item)ModItems.BASECYBERWARE_LEFTLEG_GOLDPLATED.get(), CyberwareSlot.LLEG);
                    int tint = data.trimColor((Item)ModItems.BASECYBERWARE_LEFTLEG_GOLDPLATED.get(), CyberwareSlot.LLEG);
                    tex2 = SkinModifierManager.resolveTrimOverlay(patternId, true, Limb.LEG, false);
                    if (tex2 != null) {
                        state.addModifier(new SkinModifier(tex2, tex2, tint, false, EnumSet.of(SkinModifier.HideVanilla.LEFT_PANTS)));
                    }
                }
            }
        }
        if (!mermodTailActive) {
            if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG)) {
                state.addModifier(new SkinModifier(RIGHT_CYBERLEG_TEXTURE, RIGHT_CYBERLEG_TEXTURE, -1, false, EnumSet.of(SkinModifier.HideVanilla.RIGHT_PANTS)));
                if (data.isDyed((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG)) {
                    int tint = data.dyeColor((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG);
                    state.addModifier(new SkinModifier(RIGHT_CYBERLEG_PRIMARY, RIGHT_CYBERLEG_PRIMARY, tint, false, EnumSet.of(SkinModifier.HideVanilla.RIGHT_PANTS)));
                }
                if (data.isTrimmed((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG)) {
                    ResourceLocation patternId = data.trimPatternId((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG);
                    int tint = data.trimColor((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG);
                    tex2 = SkinModifierManager.resolveTrimOverlay(patternId, false, Limb.LEG, false);
                    if (tex2 != null) {
                        state.addModifier(new SkinModifier(tex2, tex2, tint, false, EnumSet.of(SkinModifier.HideVanilla.RIGHT_PANTS)));
                    }
                }
            }
            if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG_COPPERPLATED.get(), CyberwareSlot.RLEG)) {
                state.addModifier(new SkinModifier(COPPER_PLATED_RIGHT_CYBERLEG, COPPER_PLATED_RIGHT_CYBERLEG, -1, false, EnumSet.of(SkinModifier.HideVanilla.RIGHT_PANTS)));
                if (data.isTrimmed((Item)ModItems.BASECYBERWARE_RIGHTLEG_COPPERPLATED.get(), CyberwareSlot.RLEG)) {
                    ResourceLocation patternId = data.trimPatternId((Item)ModItems.BASECYBERWARE_RIGHTLEG_COPPERPLATED.get(), CyberwareSlot.RLEG);
                    int tint = data.trimColor((Item)ModItems.BASECYBERWARE_RIGHTLEG_COPPERPLATED.get(), CyberwareSlot.RLEG);
                    tex2 = SkinModifierManager.resolveTrimOverlay(patternId, false, Limb.LEG, false);
                    if (tex2 != null) {
                        state.addModifier(new SkinModifier(tex2, tex2, tint, false, EnumSet.of(SkinModifier.HideVanilla.RIGHT_PANTS)));
                    }
                }
            }
            if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG_IRONPLATED.get(), CyberwareSlot.RLEG)) {
                state.addModifier(new SkinModifier(IRON_PLATED_RIGHT_CYBERLEG, IRON_PLATED_RIGHT_CYBERLEG, -1, false, EnumSet.of(SkinModifier.HideVanilla.RIGHT_PANTS)));
                if (data.isTrimmed((Item)ModItems.BASECYBERWARE_RIGHTLEG_IRONPLATED.get(), CyberwareSlot.RLEG)) {
                    ResourceLocation patternId = data.trimPatternId((Item)ModItems.BASECYBERWARE_RIGHTLEG_IRONPLATED.get(), CyberwareSlot.RLEG);
                    int tint = data.trimColor((Item)ModItems.BASECYBERWARE_RIGHTLEG_IRONPLATED.get(), CyberwareSlot.RLEG);
                    tex2 = SkinModifierManager.resolveTrimOverlay(patternId, false, Limb.LEG, false);
                    if (tex2 != null) {
                        state.addModifier(new SkinModifier(tex2, tex2, tint, false, EnumSet.of(SkinModifier.HideVanilla.RIGHT_PANTS)));
                    }
                }
            }
            if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG_GOLDPLATED.get(), CyberwareSlot.RLEG)) {
                state.addModifier(new SkinModifier(GOLD_PLATED_RIGHT_CYBERLEG, GOLD_PLATED_RIGHT_CYBERLEG, -1, false, EnumSet.of(SkinModifier.HideVanilla.RIGHT_PANTS)));
                if (data.isTrimmed((Item)ModItems.BASECYBERWARE_RIGHTLEG_GOLDPLATED.get(), CyberwareSlot.RLEG)) {
                    ResourceLocation patternId = data.trimPatternId((Item)ModItems.BASECYBERWARE_RIGHTLEG_GOLDPLATED.get(), CyberwareSlot.RLEG);
                    int tint = data.trimColor((Item)ModItems.BASECYBERWARE_RIGHTLEG_GOLDPLATED.get(), CyberwareSlot.RLEG);
                    tex2 = SkinModifierManager.resolveTrimOverlay(patternId, false, Limb.LEG, false);
                    if (tex2 != null) {
                        state.addModifier(new SkinModifier(tex2, tex2, tint, false, EnumSet.of(SkinModifier.HideVanilla.RIGHT_PANTS)));
                    }
                }
            }
        }
        if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM)) {
            state.addModifier(new SkinModifier(LEFT_CYBERARM_TEXTURE_WIDE, LEFT_CYBERARM_TEXTURE_SLIM, -1, false, EnumSet.of(SkinModifier.HideVanilla.LEFT_SLEEVE), EnumSet.of(HumanoidArm.LEFT)));
            if (data.isDyed((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM)) {
                int tint = data.dyeColor((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM);
                state.addModifier(new SkinModifier(LEFT_CYBERARM_PRIMARY_WIDE, LEFT_CYBERARM_PRIMARY_SLIM, tint, false, EnumSet.of(SkinModifier.HideVanilla.LEFT_SLEEVE)));
            }
            boolean slim = SkinModifierManager.isSlimArms(player);
            if (data.isTrimmed((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM)) {
                ResourceLocation patternId = data.trimPatternId((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM);
                int tint = data.trimColor((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM);
                tex = SkinModifierManager.resolveTrimOverlay(patternId, true, Limb.ARM, slim);
                if (tex != null) {
                    state.addModifier(new SkinModifier(tex, tex, tint, false, EnumSet.of(SkinModifier.HideVanilla.LEFT_SLEEVE)));
                }
            }
        }
        if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM_COPPERPLATED.get(), CyberwareSlot.LARM)) {
            state.addModifier(new SkinModifier(COPPER_PLATED_LEFT_CYBERARM_WIDE, COPPER_PLATED_LEFT_CYBERARM_SLIM, -1, false, EnumSet.of(SkinModifier.HideVanilla.LEFT_SLEEVE)));
            if (data.isTrimmed((Item)ModItems.BASECYBERWARE_LEFTARM_COPPERPLATED.get(), CyberwareSlot.LARM)) {
                ResourceLocation patternId = data.trimPatternId((Item)ModItems.BASECYBERWARE_LEFTARM_COPPERPLATED.get(), CyberwareSlot.LARM);
                int tint = data.trimColor((Item)ModItems.BASECYBERWARE_LEFTARM_COPPERPLATED.get(), CyberwareSlot.LARM);
                boolean slim = SkinModifierManager.isSlimArms(player);
                tex = SkinModifierManager.resolveTrimOverlay(patternId, true, Limb.ARM, slim);
                if (tex != null) {
                    state.addModifier(new SkinModifier(tex, tex, tint, false, EnumSet.of(SkinModifier.HideVanilla.LEFT_SLEEVE)));
                }
            }
        }
        if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM_IRONPLATED.get(), CyberwareSlot.LARM)) {
            state.addModifier(new SkinModifier(IRON_PLATED_LEFT_CYBERARM_WIDE, IRON_PLATED_LEFT_CYBERARM_SLIM, -1, false, EnumSet.of(SkinModifier.HideVanilla.LEFT_SLEEVE)));
            if (data.isTrimmed((Item)ModItems.BASECYBERWARE_LEFTARM_IRONPLATED.get(), CyberwareSlot.LARM)) {
                ResourceLocation patternId = data.trimPatternId((Item)ModItems.BASECYBERWARE_LEFTARM_IRONPLATED.get(), CyberwareSlot.LARM);
                int tint = data.trimColor((Item)ModItems.BASECYBERWARE_LEFTARM_IRONPLATED.get(), CyberwareSlot.LARM);
                boolean slim = SkinModifierManager.isSlimArms(player);
                tex = SkinModifierManager.resolveTrimOverlay(patternId, true, Limb.ARM, slim);
                if (tex != null) {
                    state.addModifier(new SkinModifier(tex, tex, tint, false, EnumSet.of(SkinModifier.HideVanilla.LEFT_SLEEVE)));
                }
            }
        }
        if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM_GOLDPLATED.get(), CyberwareSlot.LARM)) {
            state.addModifier(new SkinModifier(GOLD_PLATED_LEFT_CYBERARM_WIDE, GOLD_PLATED_LEFT_CYBERARM_SLIM, -1, false, EnumSet.of(SkinModifier.HideVanilla.LEFT_SLEEVE)));
            if (data.isTrimmed((Item)ModItems.BASECYBERWARE_LEFTARM_GOLDPLATED.get(), CyberwareSlot.LARM)) {
                ResourceLocation patternId = data.trimPatternId((Item)ModItems.BASECYBERWARE_LEFTARM_GOLDPLATED.get(), CyberwareSlot.LARM);
                int tint = data.trimColor((Item)ModItems.BASECYBERWARE_LEFTARM_GOLDPLATED.get(), CyberwareSlot.LARM);
                boolean slim = SkinModifierManager.isSlimArms(player);
                tex = SkinModifierManager.resolveTrimOverlay(patternId, true, Limb.ARM, slim);
                if (tex != null) {
                    state.addModifier(new SkinModifier(tex, tex, tint, false, EnumSet.of(SkinModifier.HideVanilla.LEFT_SLEEVE)));
                }
            }
        }
        if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM)) {
            state.addModifier(new SkinModifier(RIGHT_CYBERARM_TEXTURE_WIDE, RIGHT_CYBERARM_TEXTURE_SLIM, -1, false, EnumSet.of(SkinModifier.HideVanilla.RIGHT_SLEEVE), EnumSet.of(HumanoidArm.RIGHT)));
            if (data.isDyed((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM)) {
                int tint = data.dyeColor((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM);
                state.addModifier(new SkinModifier(RIGHT_CYBERARM_PRIMARY_WIDE, RIGHT_CYBERARM_PRIMARY_SLIM, tint, false, EnumSet.of(SkinModifier.HideVanilla.RIGHT_SLEEVE)));
            }
            boolean slim = SkinModifierManager.isSlimArms(player);
            if (data.isTrimmed((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM)) {
                ResourceLocation patternId = data.trimPatternId((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM);
                int tint = data.trimColor((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM);
                tex = SkinModifierManager.resolveTrimOverlay(patternId, false, Limb.ARM, slim);
                if (tex != null) {
                    state.addModifier(new SkinModifier(tex, tex, tint, false, EnumSet.of(SkinModifier.HideVanilla.RIGHT_SLEEVE), EnumSet.of(HumanoidArm.RIGHT)));
                }
            }
        }
        if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM_COPPERPLATED.get(), CyberwareSlot.RARM)) {
            state.addModifier(new SkinModifier(COPPER_PLATED_RIGHT_CYBERARM_WIDE, COPPER_PLATED_RIGHT_CYBERARM_SLIM, -1, false, EnumSet.of(SkinModifier.HideVanilla.RIGHT_SLEEVE)));
            if (data.isTrimmed((Item)ModItems.BASECYBERWARE_RIGHTARM_COPPERPLATED.get(), CyberwareSlot.RARM)) {
                ResourceLocation patternId = data.trimPatternId((Item)ModItems.BASECYBERWARE_RIGHTARM_COPPERPLATED.get(), CyberwareSlot.RARM);
                int tint = data.trimColor((Item)ModItems.BASECYBERWARE_RIGHTARM_COPPERPLATED.get(), CyberwareSlot.RARM);
                boolean slim = SkinModifierManager.isSlimArms(player);
                tex = SkinModifierManager.resolveTrimOverlay(patternId, false, Limb.ARM, slim);
                if (tex != null) {
                    state.addModifier(new SkinModifier(tex, tex, tint, false, EnumSet.of(SkinModifier.HideVanilla.RIGHT_SLEEVE)));
                }
            }
        }
        if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM_IRONPLATED.get(), CyberwareSlot.RARM)) {
            state.addModifier(new SkinModifier(IRON_PLATED_RIGHT_CYBERARM_WIDE, IRON_PLATED_RIGHT_CYBERARM_SLIM, -1, false, EnumSet.of(SkinModifier.HideVanilla.RIGHT_SLEEVE)));
            if (data.isTrimmed((Item)ModItems.BASECYBERWARE_RIGHTARM_IRONPLATED.get(), CyberwareSlot.RARM)) {
                ResourceLocation patternId = data.trimPatternId((Item)ModItems.BASECYBERWARE_RIGHTARM_IRONPLATED.get(), CyberwareSlot.RARM);
                int tint = data.trimColor((Item)ModItems.BASECYBERWARE_RIGHTARM_IRONPLATED.get(), CyberwareSlot.RARM);
                boolean slim = SkinModifierManager.isSlimArms(player);
                tex = SkinModifierManager.resolveTrimOverlay(patternId, false, Limb.ARM, slim);
                if (tex != null) {
                    state.addModifier(new SkinModifier(tex, tex, tint, false, EnumSet.of(SkinModifier.HideVanilla.RIGHT_SLEEVE)));
                }
            }
        }
        if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM_GOLDPLATED.get(), CyberwareSlot.RARM)) {
            state.addModifier(new SkinModifier(GOLD_PLATED_RIGHT_CYBERARM_WIDE, GOLD_PLATED_RIGHT_CYBERARM_SLIM, -1, false, EnumSet.of(SkinModifier.HideVanilla.RIGHT_SLEEVE)));
            if (data.isTrimmed((Item)ModItems.BASECYBERWARE_RIGHTARM_GOLDPLATED.get(), CyberwareSlot.RARM)) {
                ResourceLocation patternId = data.trimPatternId((Item)ModItems.BASECYBERWARE_RIGHTARM_GOLDPLATED.get(), CyberwareSlot.RARM);
                int tint = data.trimColor((Item)ModItems.BASECYBERWARE_RIGHTARM_GOLDPLATED.get(), CyberwareSlot.RARM);
                boolean slim = SkinModifierManager.isSlimArms(player);
                tex = SkinModifierManager.resolveTrimOverlay(patternId, false, Limb.ARM, slim);
                if (tex != null) {
                    state.addModifier(new SkinModifier(tex, tex, tint, false, EnumSet.of(SkinModifier.HideVanilla.RIGHT_SLEEVE)));
                }
            }
        }
        if (FullBorgHandler.isSamson(data)) {
            state.removeModifier(new SkinModifier(LEFT_CYBERLEG_TEXTURE, LEFT_CYBERLEG_TEXTURE));
            state.removeModifier(new SkinModifier(RIGHT_CYBERLEG_TEXTURE, RIGHT_CYBERLEG_TEXTURE));
            state.removeModifier(new SkinModifier(LEFT_CYBERARM_TEXTURE_WIDE, LEFT_CYBERARM_TEXTURE_SLIM));
            state.removeModifier(new SkinModifier(RIGHT_CYBERARM_TEXTURE_WIDE, RIGHT_CYBERARM_TEXTURE_SLIM));
            state.removeModifier(new SkinModifier(CYBEREYES_PRIMARY, CYBEREYES_PRIMARY));
            state.clearHighlights();
            state.clearModifiers();
            state.addModifier(new SkinModifier(SAMSON_WIDE, SAMSON_SLIM, -1, true));
            if (data.isDyed((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN)) {
                int tint = data.dyeColor((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN);
                state.addModifier(new SkinModifier(SAMSON_WIDE_DYED, SAMSON_SLIM_DYED, tint, true));
            }
            if (data.isDyed((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES)) {
                int tint = data.dyeColor((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES);
                state.addModifier(new SkinModifier(SAMSON_EYES_DYED, SAMSON_EYES_DYED, tint, true));
                state.addHighlight(new SkinHighlight(SAMSON_EYES_DYED, SAMSON_EYES_DYED, tint, true, true));
            }
        }
        if (FullBorgHandler.isEclipse(data)) {
            state.removeModifier(new SkinModifier(LEFT_CYBERLEG_TEXTURE, LEFT_CYBERLEG_TEXTURE));
            state.removeModifier(new SkinModifier(RIGHT_CYBERLEG_TEXTURE, RIGHT_CYBERLEG_TEXTURE));
            state.removeModifier(new SkinModifier(LEFT_CYBERARM_TEXTURE_WIDE, LEFT_CYBERARM_TEXTURE_SLIM));
            state.removeModifier(new SkinModifier(RIGHT_CYBERARM_TEXTURE_WIDE, RIGHT_CYBERARM_TEXTURE_SLIM));
            state.removeModifier(new SkinModifier(CYBEREYES_PRIMARY, CYBEREYES_PRIMARY));
            state.clearHighlights();
            state.clearModifiers();
            state.addModifier(new SkinModifier(ECLIPSE_WIDE, ECLIPSE_SLIM, -1, true));
            if (data.isDyed((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN)) {
                int tint = data.dyeColor((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN);
                state.addModifier(new SkinModifier(ECLIPSE_WIDE_DYED, ECLIPSE_SLIM_DYED, tint, true));
            }
            if (data.isDyed((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES)) {
                int tint = data.dyeColor((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES);
                state.addModifier(new SkinModifier(ECLIPSE_EYES_DYED, ECLIPSE_EYES_DYED, tint, true));
                state.addHighlight(new SkinHighlight(ECLIPSE_EYES_DYED, ECLIPSE_EYES_DYED, tint, true, true));
            }
            if (data.isTrimmed((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN)) {
                int tint = data.trimColor((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN);
                state.addModifier(new SkinModifier(ECLIPSE_VISOR_TRIMMED, ECLIPSE_VISOR_TRIMMED, tint, false));
            }
        }
        if (FullBorgHandler.isSpyder(data)) {
            state.removeModifier(new SkinModifier(LEFT_CYBERLEG_TEXTURE, LEFT_CYBERLEG_TEXTURE));
            state.removeModifier(new SkinModifier(RIGHT_CYBERLEG_TEXTURE, RIGHT_CYBERLEG_TEXTURE));
            state.removeModifier(new SkinModifier(LEFT_CYBERARM_TEXTURE_WIDE, LEFT_CYBERARM_TEXTURE_SLIM));
            state.removeModifier(new SkinModifier(RIGHT_CYBERARM_TEXTURE_WIDE, RIGHT_CYBERARM_TEXTURE_SLIM));
            state.removeModifier(new SkinModifier(CYBEREYES_PRIMARY, CYBEREYES_PRIMARY));
            state.clearHighlights();
            state.clearModifiers();
            state.addModifier(new SkinModifier(SPYDER_WIDE, SPYDER_SLIM, -1, true));
            if (data.isDyed((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN)) {
                int tint = data.dyeColor((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN);
                state.addModifier(new SkinModifier(SPYDER_WIDE_DYED, SPYDER_SLIM_DYED, tint, true));
            }
            if (data.isDyed((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES)) {
                int tint = data.dyeColor((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES);
                state.addModifier(new SkinModifier(SPYDER_EYES_DYED, SPYDER_EYES_DYED, tint, true));
                state.addHighlight(new SkinHighlight(SPYDER_EYES_DYED, SPYDER_EYES_DYED, tint, true, true));
            }
            if (data.isTrimmed((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN)) {
                int tint = data.trimColor((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN);
                state.addModifier(new SkinModifier(SPYDER_VISOR_TRIMMED, SPYDER_VISOR_TRIMMED, tint, false));
            }
        }
        if (FullBorgHandler.isWingman(data)) {
            state.removeModifier(new SkinModifier(LEFT_CYBERLEG_TEXTURE, LEFT_CYBERLEG_TEXTURE));
            state.removeModifier(new SkinModifier(RIGHT_CYBERLEG_TEXTURE, RIGHT_CYBERLEG_TEXTURE));
            state.removeModifier(new SkinModifier(LEFT_CYBERARM_TEXTURE_WIDE, LEFT_CYBERARM_TEXTURE_SLIM));
            state.removeModifier(new SkinModifier(RIGHT_CYBERARM_TEXTURE_WIDE, RIGHT_CYBERARM_TEXTURE_SLIM));
            state.removeModifier(new SkinModifier(CYBEREYES_PRIMARY, CYBEREYES_PRIMARY));
            state.clearHighlights();
            state.clearModifiers();
            state.addModifier(new SkinModifier(WINGMAN_WIDE, WINGMAN_SLIM, -1, true));
            if (data.isDyed((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN)) {
                int tint = data.dyeColor((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN);
                state.addModifier(new SkinModifier(WINGMAN_WIDE_DYED, WINGMAN_SLIM_DYED, tint, true));
            }
        }
        if (FullBorgHandler.isAquarius(data)) {
            state.removeModifier(new SkinModifier(LEFT_CYBERLEG_TEXTURE, LEFT_CYBERLEG_TEXTURE));
            state.removeModifier(new SkinModifier(RIGHT_CYBERLEG_TEXTURE, RIGHT_CYBERLEG_TEXTURE));
            state.removeModifier(new SkinModifier(LEFT_CYBERARM_TEXTURE_WIDE, LEFT_CYBERARM_TEXTURE_SLIM));
            state.removeModifier(new SkinModifier(RIGHT_CYBERARM_TEXTURE_WIDE, RIGHT_CYBERARM_TEXTURE_SLIM));
            state.removeModifier(new SkinModifier(CYBEREYES_PRIMARY, CYBEREYES_PRIMARY));
            state.clearHighlights();
            state.clearModifiers();
            state.addModifier(new SkinModifier(AQUARIUS_WIDE, AQUARIUS_SLIM, -1, true));
            if (data.isDyed((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN)) {
                int tint = data.dyeColor((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN);
                state.addModifier(new SkinModifier(AQUARIUS_WIDE_DYED, AQUARIUS_SLIM_DYED, tint, true));
            }
            if (data.isDyed((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES)) {
                int tint = data.dyeColor((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES);
                state.addModifier(new SkinModifier(AQUARIUS_EYES_DYED, AQUARIUS_EYES_DYED, tint, true));
                state.addHighlight(new SkinHighlight(AQUARIUS_EYES_DYED, AQUARIUS_EYES_DYED, tint, true, true));
            }
        }
        if (FullBorgHandler.isDymond(data)) {
            state.removeModifier(new SkinModifier(LEFT_CYBERLEG_TEXTURE, LEFT_CYBERLEG_TEXTURE));
            state.removeModifier(new SkinModifier(RIGHT_CYBERLEG_TEXTURE, RIGHT_CYBERLEG_TEXTURE));
            state.removeModifier(new SkinModifier(LEFT_CYBERARM_TEXTURE_WIDE, LEFT_CYBERARM_TEXTURE_SLIM));
            state.removeModifier(new SkinModifier(RIGHT_CYBERARM_TEXTURE_WIDE, RIGHT_CYBERARM_TEXTURE_SLIM));
            state.removeModifier(new SkinModifier(CYBEREYES_PRIMARY, CYBEREYES_PRIMARY));
            state.clearHighlights();
            state.clearModifiers();
            state.addModifier(new SkinModifier(DYMOND_WIDE, DYMOND_SLIM, -1, true));
            if (data.isDyed((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN)) {
                int tint = data.dyeColor((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN);
                state.addModifier(new SkinModifier(DYMOND_WIDE_DYED, DYMOND_SLIM_DYED, tint, true));
            }
            if (data.isDyed((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES)) {
                int tint = data.dyeColor((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES);
                state.addModifier(new SkinModifier(DYMOND_EYES_DYED, DYMOND_EYES_DYED, tint, true));
                state.addHighlight(new SkinHighlight(DYMOND_EYES_DYED, DYMOND_EYES_DYED, tint, true, true));
            }
        }
        if (FullBorgHandler.isDragoon(data)) {
            state.removeModifier(new SkinModifier(LEFT_CYBERLEG_TEXTURE, LEFT_CYBERLEG_TEXTURE));
            state.removeModifier(new SkinModifier(RIGHT_CYBERLEG_TEXTURE, RIGHT_CYBERLEG_TEXTURE));
            state.removeModifier(new SkinModifier(LEFT_CYBERARM_TEXTURE_WIDE, LEFT_CYBERARM_TEXTURE_SLIM));
            state.removeModifier(new SkinModifier(RIGHT_CYBERARM_TEXTURE_WIDE, RIGHT_CYBERARM_TEXTURE_SLIM));
            state.removeModifier(new SkinModifier(CYBEREYES_PRIMARY, CYBEREYES_PRIMARY));
            state.clearHighlights();
            state.clearModifiers();
            state.addModifier(new SkinModifier(DRAGOON_WIDE, DRAGOON_SLIM, -1, true));
            if (data.isDyed((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN)) {
                int tint = data.dyeColor((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN);
                state.addModifier(new SkinModifier(DRAGOON_WIDE_DYED, DRAGOON_SLIM_DYED, tint, true));
            }
            if (data.isDyed((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES)) {
                int tint = data.dyeColor((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES);
                state.addModifier(new SkinModifier(DRAGOON_EYES_DYED, DRAGOON_EYES_DYED, tint, true));
                state.addHighlight(new SkinHighlight(DRAGOON_EYES_DYED, DRAGOON_EYES_DYED, tint, true, true));
            }
        }
        if (ModCompats.isInstalled("northstar") && FullBorgHandler.isCopernicus(data)) {
            state.removeModifier(new SkinModifier(LEFT_CYBERLEG_TEXTURE, LEFT_CYBERLEG_TEXTURE));
            state.removeModifier(new SkinModifier(RIGHT_CYBERLEG_TEXTURE, RIGHT_CYBERLEG_TEXTURE));
            state.removeModifier(new SkinModifier(LEFT_CYBERARM_TEXTURE_WIDE, LEFT_CYBERARM_TEXTURE_SLIM));
            state.removeModifier(new SkinModifier(RIGHT_CYBERARM_TEXTURE_WIDE, RIGHT_CYBERARM_TEXTURE_SLIM));
            state.removeModifier(new SkinModifier(NETHERPLATED_SKIN_TEXTURE_WIDE, NETHERPLATED_SKIN_TEXTURE_SLIM));
            state.removeModifier(new SkinModifier(CYBEREYES_PRIMARY, CYBEREYES_PRIMARY));
            state.clearHighlights();
            state.clearModifiers();
            state.addModifier(new SkinModifier(COPERNICUS_WIDE, COPERNICUS_SLIM, -1, true));
            if (data.isDyed((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN)) {
                int tint = data.dyeColor((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN);
                state.addModifier(new SkinModifier(COPERNICUS_WIDE_DYED, COPERNICUS_SLIM_DYED, tint, true));
            }
            if (data.isDyed((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES)) {
                int tint = data.dyeColor((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES);
                state.addModifier(new SkinModifier(COPERNICUS_EYES_DYED, COPERNICUS_EYES_DYED, tint, true));
            }
        }
        if (FullBorgHandler.isGenos(data)) {
            state.removeModifier(new SkinModifier(LEFT_CYBERLEG_TEXTURE, LEFT_CYBERLEG_TEXTURE));
            state.removeModifier(new SkinModifier(RIGHT_CYBERLEG_TEXTURE, RIGHT_CYBERLEG_TEXTURE));
            state.removeModifier(new SkinModifier(LEFT_CYBERARM_TEXTURE_WIDE, LEFT_CYBERARM_TEXTURE_SLIM));
            state.removeModifier(new SkinModifier(RIGHT_CYBERARM_TEXTURE_WIDE, RIGHT_CYBERARM_TEXTURE_SLIM));
            state.clearModifiers();
            state.addModifier(new SkinModifier(GENOS_WIDE, GENOS_SLIM, -1, false, EnumSet.of(SkinModifier.HideVanilla.RIGHT_PANTS, SkinModifier.HideVanilla.LEFT_PANTS, SkinModifier.HideVanilla.RIGHT_SLEEVE, SkinModifier.HideVanilla.LEFT_SLEEVE, SkinModifier.HideVanilla.JACKET)));
            state.addHighlight(new SkinHighlight(GENOS_HIGHLIGHT, GENOS_HIGHLIGHT, 0xFFFFFF, true, false));
            if (data.isDyed((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN)) {
                int tint = data.dyeColor((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN);
                state.addModifier(new SkinModifier(GENOS_WIDE_DYED, GENOS_SLIM_DYED, tint, false, EnumSet.of(SkinModifier.HideVanilla.RIGHT_PANTS, SkinModifier.HideVanilla.LEFT_PANTS, SkinModifier.HideVanilla.RIGHT_SLEEVE, SkinModifier.HideVanilla.LEFT_SLEEVE, SkinModifier.HideVanilla.JACKET)));
            }
            if (data.isDyed((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES)) {
                int tint = data.dyeColor((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES);
                state.clearHighlights();
                state.addModifier(new SkinModifier(GENOS_EYES_DYED, GENOS_EYES_DYED, tint, false, EnumSet.of(SkinModifier.HideVanilla.RIGHT_PANTS, SkinModifier.HideVanilla.LEFT_PANTS, SkinModifier.HideVanilla.RIGHT_SLEEVE, SkinModifier.HideVanilla.LEFT_SLEEVE, SkinModifier.HideVanilla.JACKET)));
                state.addHighlight(new SkinHighlight(GENOS_EYES_DYED, GENOS_EYES_DYED, tint, true, true));
                state.addModifier(new SkinModifier(CYBEREYES_PRIMARY, CYBEREYES_PRIMARY, tint, false));
                state.addHighlight(new SkinHighlight(CYBEREYES_PRIMARY, CYBEREYES_PRIMARY, tint, true, true));
            }
        }
        if (data.hasSpecificItem((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN) && data.isTrimmed((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN)) {
            ResourceLocation patternId = data.trimPatternId((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN);
            int tint = data.trimColor((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN);
            ResourceLocation wide = SkinModifierManager.resolveBodyTrimOverlay(patternId, false);
            ResourceLocation slim = SkinModifierManager.resolveBodyTrimOverlay(patternId, true);
            if (wide != null && slim != null) {
                tint = tint & 0xFFFFFF | 0xFF000000;
                state.addModifier(new SkinModifier(wide, slim, tint, false, false, EnumSet.noneOf(SkinModifier.HideVanilla.class), EnumSet.noneOf(HumanoidArm.class), true));
            }
        }
        if (data.hasSpecificItem((Item)ModItems.WETWARE_POLARBEARFUR.get(), CyberwareSlot.SKIN)) {
            state.addModifier(new SkinModifier(POLAR_BEAR_FUR_TEXTURE, POLAR_BEAR_FUR_TEXTURE, -1, true));
        }
        if (ModItems.SKINUPGRADES_MANASKIN != null && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_MANASKIN.get(), CyberwareSlot.SKIN)) {
            state.addModifier(new SkinModifier(MANASKIN_WIDE, MANASKIN_SLIM, -1, true, false, null, null, false));
        }
        if (data.hasSpecificItem((Item)ModItems.BONEUPGRADES_SPINALINJECTOR.get(), CyberwareSlot.BONE)) {
            state.addModifier(new SkinModifier(SPINAL_INJECTOR_TEXTURE, SPINAL_INJECTOR_TEXTURE, -1, false));
            SkinHighlightRender.apply(state, true, SPINAL_INJECTOR_HIGHLIGHT_TEXTURE, SPINAL_INJECTOR_HIGHLIGHT_TEXTURE, -1, true);
        }
        if (ModItems.BONEUPGRADES_ELYTRA != null && data.hasSpecificItem((Item)ModItems.BONEUPGRADES_ELYTRA.get(), CyberwareSlot.BONE)) {
            state.addModifier(new SkinModifier(DEPLOYABLE_ELYTRA_TEXTURE, DEPLOYABLE_ELYTRA_TEXTURE, -1, false));
            SkinHighlightRender.apply(state, true, DEPLOYABLE_ELYTRA_HIGHLIGHT_TEXTURE, DEPLOYABLE_ELYTRA_HIGHLIGHT_TEXTURE, -1, true);
        }
        if (data.hasSpecificItem((Item)ModItems.BONEUPGRADES_SANDEVISTAN.get(), CyberwareSlot.BONE)) {
            state.addModifier(new SkinModifier(SANDEVISTAN_TEXTURE, SANDEVISTAN_TEXTURE, -1, false));
            SkinHighlightRender.apply(state, true, SANDEVISTAN_HIGHLIGHT_TEXTURE, SANDEVISTAN_HIGHLIGHT_TEXTURE, -1, true);
        }
        if (data.hasSpecificItem((Item)ModItems.BRAINUPGRADES_CHIPWARESLOTS.get(), CyberwareSlot.BRAIN)) {
            if (data.hasChipwareShard(ModTags.Items.DATA_SHARDS)) {
                state.addModifier(new SkinModifier(CHIPWARE_ACTIVE, CHIPWARE_ACTIVE, -1, false));
                state.addHighlight(new SkinHighlight(CHIPWARE_ACTIVE, CHIPWARE_ACTIVE, -1, true));
            } else {
                state.addModifier(new SkinModifier(CHIPWARE_INACTIVE, CHIPWARE_INACTIVE, -1, false));
                state.addHighlight(new SkinHighlight(CHIPWARE_INACTIVE, CHIPWARE_INACTIVE, -1, true));
            }
        }
        if (data.hasSpecificItem((Item)ModItems.ARMUPGRADES_REINFORCEDKNUCKLES.get(), CyberwareSlot.LARM)) {
            state.addModifier(new SkinModifier(KNUCKLES_LARM_WIDE, KNUCKLES_LARM_SLIM, -1, false));
        }
        if (data.hasSpecificItem((Item)ModItems.ARMUPGRADES_REINFORCEDKNUCKLES.get(), CyberwareSlot.RARM)) {
            state.addModifier(new SkinModifier(KNUCKLES_RARM_WIDE, KNUCKLES_RARM_SLIM, -1, false));
        }
        if (data.hasSpecificItem((Item)ModItems.ARMUPGRADES_FIRESTARTER.get(), CyberwareSlot.LARM)) {
            state.addModifier(new SkinModifier(FIRESTARTER_LARM_WIDE, FIRESTARTER_LARM_SLIM, -1, false));
        }
        if (data.hasSpecificItem((Item)ModItems.ARMUPGRADES_FIRESTARTER.get(), CyberwareSlot.RARM)) {
            state.addModifier(new SkinModifier(FIRESTARTER_RARM_WIDE, FIRESTARTER_RARM_SLIM, -1, false));
        }
        if (data.hasSpecificItem((Item)ModItems.ARMUPGRADES_FLYWHEEL.get(), CyberwareSlot.LARM)) {
            state.addModifier(new SkinModifier(FLYWHEEL_LARM_WIDE, FLYWHEEL_LARM_SLIM, -1, false));
        }
        if (data.hasSpecificItem((Item)ModItems.ARMUPGRADES_FLYWHEEL.get(), CyberwareSlot.RARM)) {
            state.addModifier(new SkinModifier(FLYWHEEL_RARM_WIDE, FLYWHEEL_RARM_SLIM, -1, false));
        }
        if (data.hasSpecificItem((Item)ModItems.BRAINUPGRADES_CYBERDECK.get(), CyberwareSlot.BRAIN)) {
            state.addModifier(new SkinModifier(CYBERDECK_TEXTURE, CYBERDECK_TEXTURE, -1, true));
            state.addHighlight(new SkinHighlight(CYBERDECK_TEXTURE, CYBERDECK_TEXTURE, -1, true));
        }
        if (data.hasSpecificItem((Item)ModItems.ORGANSUPGRADES_HEATENGINE.get(), CyberwareSlot.ORGANS)) {
            state.addModifier(new SkinModifier(FURNACE, FURNACE, -1, false));
            if (data.isHeatEngineActive()) {
                state.addModifier(new SkinModifier(FURNACE_ACTIVE, FURNACE_ACTIVE, -1, false));
                state.addHighlight(new SkinHighlight(FURNACE_HIGHLIGHT, FURNACE_HIGHLIGHT, -1, true));
            }
        }
        if (data.hasSpecificItem((Item)ModItems.WETWARE_SPIDEREYES.get(), CyberwareSlot.EYES)) {
            state.addModifier(new SkinModifier(SPIDER_EYES, SPIDER_EYES, -1, false));
            state.addHighlight(new SkinHighlight(SPIDER_EYES, SPIDER_EYES, -1, true));
        }
        return state;
    }

    private static ResourceLocation resolveTrimOverlay(ResourceLocation patternId, boolean left, Limb limb, boolean isSlimModelForArmsOrBody) {
        String side;
        if (patternId == null) {
            return null;
        }
        String pattern = patternId.getPath();
        String string = side = left ? "left" : "right";
        String file = limb == Limb.LEG ? pattern + "_" + side + "_leg.png" : pattern + "_" + side + "_" + (isSlimModelForArmsOrBody ? "slim" : "wide") + ".png";
        return ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)("textures/entity/trims/" + file));
    }

    private static boolean isSlimArms(AbstractClientPlayer player) {
        return player.getSkin().model() == PlayerSkin.Model.SLIM;
    }

    private static TrimInfo getLimbTrim(PlayerCyberwareData data, AbstractClientPlayer player, CyberwareSlot slot, Item item) {
        if (!data.isTrimmed(item, slot)) {
            return null;
        }
        ResourceLocation patternId = data.trimPatternId(item, slot);
        int tint = data.trimColor(item, slot);
        if (patternId == null) {
            return null;
        }
        return new TrimInfo(patternId, tint);
    }

    private static TrimInfo getUnifiedLimbTrim(PlayerCyberwareData data, AbstractClientPlayer player) {
        TrimInfo lArm = SkinModifierManager.getLimbTrim(data, player, CyberwareSlot.LARM, (Item)ModItems.BASECYBERWARE_LEFTARM.get());
        TrimInfo rArm = SkinModifierManager.getLimbTrim(data, player, CyberwareSlot.RARM, (Item)ModItems.BASECYBERWARE_RIGHTARM.get());
        TrimInfo lLeg = SkinModifierManager.getLimbTrim(data, player, CyberwareSlot.LLEG, (Item)ModItems.BASECYBERWARE_LEFTLEG.get());
        TrimInfo rLeg = SkinModifierManager.getLimbTrim(data, player, CyberwareSlot.RLEG, (Item)ModItems.BASECYBERWARE_RIGHTLEG.get());
        if (lArm == null || rArm == null || lLeg == null || rLeg == null) {
            return null;
        }
        if (!lArm.patternId().equals((Object)rArm.patternId())) {
            return null;
        }
        if (!lArm.patternId().equals((Object)lLeg.patternId())) {
            return null;
        }
        if (!lArm.patternId().equals((Object)rLeg.patternId())) {
            return null;
        }
        if (lArm.tint() != rArm.tint() || lArm.tint() != lLeg.tint() || lArm.tint() != rLeg.tint()) {
            return null;
        }
        return lArm;
    }

    private static ResourceLocation resolveBodyTrimOverlay(ResourceLocation patternId, boolean slim) {
        if (patternId == null) {
            return null;
        }
        String pattern = patternId.getPath();
        String file = slim ? pattern + "_slim_full_body.png" : pattern + "_full_body.png";
        return ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)("textures/entity/trims/" + file));
    }

    private static boolean hasMermodMoisturizerComponent(ItemStack necklace) {
        DataComponentType<?> type = SkinModifierManager.getMermodNecklaceModifiersComponentType();
        if (type == null) {
            return false;
        }
        Object componentValue = necklace.get(type);
        if (componentValue == null) {
            return false;
        }
        return SkinModifierManager.componentContainsMoisturizer(componentValue);
    }

    private static DataComponentType<?> getMermodNecklaceModifiersComponentType() {
        if (MERMOD_NECKLACE_MODIFIERS_COMPONENT != null) {
            return MERMOD_NECKLACE_MODIFIERS_COMPONENT;
        }
        DataComponentType t = (DataComponentType)BuiltInRegistries.DATA_COMPONENT_TYPE.get(ResourceLocation.fromNamespaceAndPath((String)"mermod", (String)"necklace_modifiers"));
        if (t != null) {
            MERMOD_NECKLACE_MODIFIERS_COMPONENT = t;
            return MERMOD_NECKLACE_MODIFIERS_COMPONENT;
        }
        t = (DataComponentType)BuiltInRegistries.DATA_COMPONENT_TYPE.get(ResourceLocation.fromNamespaceAndPath((String)"mermod", (String)"necklace_modifiers_component_type"));
        if (t != null) {
            MERMOD_NECKLACE_MODIFIERS_COMPONENT = t;
            return MERMOD_NECKLACE_MODIFIERS_COMPONENT;
        }
        t = (DataComponentType)BuiltInRegistries.DATA_COMPONENT_TYPE.get(ResourceLocation.fromNamespaceAndPath((String)"mermod", (String)"necklace_modifiers_component"));
        if (t != null) {
            MERMOD_NECKLACE_MODIFIERS_COMPONENT = t;
            return MERMOD_NECKLACE_MODIFIERS_COMPONENT;
        }
        DataComponentType best = null;
        int bestScore = -1;
        for (ResourceLocation id : BuiltInRegistries.DATA_COMPONENT_TYPE.keySet()) {
            DataComponentType candidate;
            if (!"mermod".equals(id.getNamespace())) continue;
            String p = id.getPath();
            int score = 0;
            if (p.contains("necklace")) {
                score += 3;
            }
            if (p.contains("modifier")) {
                score += 3;
            }
            if (p.contains("modifiers")) {
                score += 2;
            }
            if (p.contains("component")) {
                ++score;
            }
            if (score <= bestScore || (candidate = (DataComponentType)BuiltInRegistries.DATA_COMPONENT_TYPE.get(id)) == null) continue;
            best = candidate;
            bestScore = score;
        }
        MERMOD_NECKLACE_MODIFIERS_COMPONENT = best;
        return best;
    }

    private static boolean componentContainsMoisturizer(Object necklaceModifiersComponent) {
        if (MERMOD_REFLECTION_FAILED) {
            return false;
        }
        if (!MERMOD_REFLECTION_READY) {
            try {
                MERMOD_COMPONENT_MODIFIERS_METHOD = necklaceModifiersComponent.getClass().getMethod("modifiers", new Class[0]);
                Class<?> necklaceModifierClass = Class.forName("io.github.thatpreston.mermod.item.modifier.NecklaceModifier");
                MERMOD_MODIFIER_ID_METHOD = necklaceModifierClass.getMethod("id", new Class[0]);
                MERMOD_REFLECTION_READY = true;
            }
            catch (Throwable t) {
                MERMOD_REFLECTION_FAILED = true;
                return false;
            }
        }
        try {
            Object mapObj = MERMOD_COMPONENT_MODIFIERS_METHOD.invoke(necklaceModifiersComponent, new Object[0]);
            if (!(mapObj instanceof Map)) {
                return false;
            }
            Map map = (Map)mapObj;
            Collection values = map.values();
            for (Object mod : values) {
                String rawId;
                Object idObj;
                if (mod == null || !((idObj = MERMOD_MODIFIER_ID_METHOD.invoke(mod, new Object[0])) instanceof String) || !SkinModifierManager.isMoisturizerId(rawId = (String)idObj)) continue;
                return true;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return false;
    }

    private static boolean isMoisturizerId(String rawId) {
        if (rawId == null || rawId.isBlank()) {
            return false;
        }
        String s = rawId;
        int colon = s.indexOf(58);
        if (colon >= 0 && colon + 1 < s.length()) {
            s = s.substring(colon + 1);
        }
        if (s.endsWith("_modifier")) {
            s = s.substring(0, s.length() - "_modifier".length());
        }
        return "tail_moisturizer".equals(s);
    }

    private static PlayerCyberwareData tryBuildCyberwareDataFromSnapshot(AbstractClientPlayer player) {
        if (SNAP_REFLECT_FAILED) {
            return null;
        }
        CompoundTag snap = player.getPersistentData().getCompound(ENTITY_HOLO_SNAPSHOT_KEY);
        if (snap == null || snap.isEmpty()) {
            return null;
        }
        try {
            if (!SNAP_REFLECT_READY) {
                Class<PlayerCyberwareData> cls = PlayerCyberwareData.class;
                SNAP_CTOR = cls.getDeclaredConstructor(new Class[0]);
                SNAP_CTOR.setAccessible(true);
                try {
                    SNAP_DESERIALIZE_1 = cls.getMethod("deserializeNBT", HolderLookup.Provider.class, CompoundTag.class);
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    // empty catch block
                }
                try {
                    SNAP_DESERIALIZE_2 = cls.getMethod("deserializeNBT", CompoundTag.class);
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    // empty catch block
                }
                SNAP_REFLECT_READY = true;
            }
            Object obj = SNAP_CTOR.newInstance(new Object[0]);
            PlayerCyberwareData data = (PlayerCyberwareData)obj;
            if (SNAP_DESERIALIZE_1 != null) {
                SNAP_DESERIALIZE_1.invoke((Object)data, player.registryAccess(), snap);
                return data;
            }
            if (SNAP_DESERIALIZE_2 != null) {
                SNAP_DESERIALIZE_2.invoke((Object)data, snap);
                return data;
            }
            SNAP_REFLECT_FAILED = true;
            return null;
        }
        catch (Throwable t) {
            SNAP_REFLECT_FAILED = true;
            return null;
        }
    }

    static {
        MERMOD_SEA_NECKLACE_ID = ResourceLocation.fromNamespaceAndPath((String)"mermod", (String)"sea_necklace");
        MERMOD_REFLECTION_READY = false;
        MERMOD_REFLECTION_FAILED = false;
        MISSING_SKIN_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/playermuscles_wide.png");
        CYBEREYES_PRIMARY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/cybereyes_dye_primary.png");
        CYBEREYES_SECONDARY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/cybereyes_dye_secondary.png");
        RIGHT_CYBERLEG_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/right_cyberleg.png");
        COPPER_PLATED_RIGHT_CYBERLEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/copper_rightleg.png");
        IRON_PLATED_RIGHT_CYBERLEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/iron_rightleg.png");
        GOLD_PLATED_RIGHT_CYBERLEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/gold_rightleg.png");
        RIGHT_CYBERLEG_PRIMARY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/right_cyberleg_dye_primary.png");
        RIGHT_CYBERLEG_SECONDARY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/right_cyberleg_dye_secondary.png");
        LEFT_CYBERLEG_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/left_cyberleg.png");
        COPPER_PLATED_LEFT_CYBERLEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/copper_leftleg.png");
        IRON_PLATED_LEFT_CYBERLEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/iron_leftleg.png");
        GOLD_PLATED_LEFT_CYBERLEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/gold_leftleg.png");
        LEFT_CYBERLEG_PRIMARY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/left_cyberleg_dye_primary.png");
        LEFT_CYBERLEG_SECONDARY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/left_cyberleg_dye_secondary.png");
        POLAR_BEAR_FUR_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/polar_bear_fur.png");
        SPINAL_INJECTOR_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/spinal_injector.png");
        SPINAL_INJECTOR_HIGHLIGHT_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/spinal_injector_highlight.png");
        SANDEVISTAN_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/sandevistan.png");
        SANDEVISTAN_HIGHLIGHT_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/sandevistan_highlight.png");
        DEPLOYABLE_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/deployable_elytra.png");
        DEPLOYABLE_ELYTRA_HIGHLIGHT_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/deployable_elytra_highlight.png");
        CYBERDECK_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/cyberdeck.png");
        GILLS_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/gills.png");
        CHIPWARE_INACTIVE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/chipware_inactive.png");
        CHIPWARE_ACTIVE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/chipware_active.png");
        FURNACE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/furnace.png");
        FURNACE_ACTIVE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/furnace_lit.png");
        FURNACE_HIGHLIGHT = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/furnace_lit_highlight.png");
        SPIDER_EYES = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/spider_eyes.png");
        SAMSON_EYES_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/samson_eyes_dyed.png");
        ECLIPSE_EYES_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/eclipse_eyes_dyed.png");
        SPYDER_EYES_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/spyder_eyes_dyed.png");
        AQUARIUS_EYES_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/aquarius_eyes_dyed.png");
        DYMOND_EYES_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/dymond_eyes_dyed.png");
        DRAGOON_EYES_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/dragoon_eyes_dyed.png");
        COPERNICUS_EYES_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/copernicus_eyes_dyed.png");
        GENOS_EYES_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/genos_eyes_dyed.png");
        GENOS_HIGHLIGHT = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/genos_highlight.png");
        ECLIPSE_VISOR_TRIMMED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/eclipse_visor_trimmed.png");
        SPYDER_VISOR_TRIMMED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/spyder_visor_trimmed.png");
        LEFT_CYBERARM_TEXTURE_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/left_cyberarm_wide.png");
        COPPER_PLATED_LEFT_CYBERARM_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/copper_leftarm_wide.png");
        IRON_PLATED_LEFT_CYBERARM_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/iron_leftarm_wide.png");
        GOLD_PLATED_LEFT_CYBERARM_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/gold_leftarm_wide.png");
        LEFT_CYBERARM_PRIMARY_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/left_cyberarm_dye_primary_wide.png");
        LEFT_CYBERARM_SECONDARY_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/left_cyberarm_dye_secondary_wide.png");
        RIGHT_CYBERARM_TEXTURE_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/right_cyberarm_wide.png");
        COPPER_PLATED_RIGHT_CYBERARM_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/copper_rightarm_wide.png");
        IRON_PLATED_RIGHT_CYBERARM_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/iron_rightarm_wide.png");
        GOLD_PLATED_RIGHT_CYBERARM_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/gold_rightarm_wide.png");
        RIGHT_CYBERARM_PRIMARY_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/right_cyberarm_dye_primary_wide.png");
        RIGHT_CYBERARM_SECONDARY_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/right_cyberarm_dye_secondary_wide.png");
        KNUCKLES_LARM_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/knuckles_larm_wide.png");
        KNUCKLES_RARM_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/knuckles_rarm_wide.png");
        SYNTHSKIN_TEXTURE_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/synthskin_wide.png");
        NETHERPLATED_SKIN_TEXTURE_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/isothermal_skin_wide.png");
        FIRESTARTER_LARM_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/firestarter_larm_wide.png");
        FIRESTARTER_RARM_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/firestarter_rarm_wide.png");
        FLYWHEEL_LARM_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/flywheel_larm_wide.png");
        FLYWHEEL_RARM_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/flywheel_rarm_wide.png");
        MANASKIN_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/manaskin.png");
        SAMSON_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/samson_wide.png");
        SAMSON_WIDE_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/samson_wide_dyed.png");
        ECLIPSE_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/eclipse_wide.png");
        ECLIPSE_WIDE_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/eclipse_wide_dyed.png");
        SPYDER_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/spyder_wide.png");
        SPYDER_WIDE_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/spyder_wide_dyed.png");
        WINGMAN_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/wingman_wide.png");
        WINGMAN_WIDE_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/wingman_wide_dyed.png");
        AQUARIUS_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/aquarius_wide.png");
        AQUARIUS_WIDE_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/aquarius_wide_dyed.png");
        DYMOND_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/dymond_wide.png");
        DYMOND_WIDE_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/dymond_wide_dyed.png");
        DRAGOON_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/dragoon_wide.png");
        DRAGOON_WIDE_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/dragoon_wide_dyed.png");
        COPERNICUS_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/copernicus_wide.png");
        COPERNICUS_WIDE_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/copernicus_wide_dyed.png");
        GENOS_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/genos_wide.png");
        GENOS_WIDE_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/genos_wide_dyed.png");
        LEFT_CYBERARM_TEXTURE_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/left_cyberarm_slim.png");
        COPPER_PLATED_LEFT_CYBERARM_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/copper_leftarm_slim.png");
        IRON_PLATED_LEFT_CYBERARM_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/iron_leftarm_slim.png");
        GOLD_PLATED_LEFT_CYBERARM_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/gold_leftarm_slim.png");
        LEFT_CYBERARM_PRIMARY_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/left_cyberarm_dye_primary_slim.png");
        LEFT_CYBERARM_SECONDARY_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/left_cyberarm_dye_secondary_slim.png");
        RIGHT_CYBERARM_TEXTURE_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/right_cyberarm_slim.png");
        COPPER_PLATED_RIGHT_CYBERARM_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/copper_rightarm_slim.png");
        IRON_PLATED_RIGHT_CYBERARM_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/iron_rightarm_slim.png");
        GOLD_PLATED_RIGHT_CYBERARM_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/gold_rightarm_slim.png");
        RIGHT_CYBERARM_PRIMARY_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/right_cyberarm_dye_primary_slim.png");
        RIGHT_CYBERARM_SECONDARY_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/right_cyberarm_dye_secondary_slim.png");
        KNUCKLES_LARM_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/knuckles_larm_slim.png");
        KNUCKLES_RARM_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/knuckles_rarm_slim.png");
        SYNTHSKIN_TEXTURE_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/synthskin_slim.png");
        NETHERPLATED_SKIN_TEXTURE_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/isothermal_skin_slim.png");
        FIRESTARTER_LARM_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/firestarter_larm_slim.png");
        FIRESTARTER_RARM_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/firestarter_rarm_slim.png");
        FLYWHEEL_LARM_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/flywheel_larm_slim.png");
        FLYWHEEL_RARM_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/flywheel_rarm_slim.png");
        MANASKIN_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/manaskin_slim.png");
        SAMSON_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/samson_slim.png");
        SAMSON_SLIM_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/samson_slim_dyed.png");
        ECLIPSE_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/eclipse_slim.png");
        ECLIPSE_SLIM_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/eclipse_slim_dyed.png");
        SPYDER_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/spyder_slim.png");
        SPYDER_SLIM_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/spyder_slim_dyed.png");
        WINGMAN_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/wingman_slim.png");
        WINGMAN_SLIM_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/wingman_slim_dyed.png");
        AQUARIUS_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/aquarius_slim.png");
        AQUARIUS_SLIM_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/aquarius_slim_dyed.png");
        DYMOND_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/dymond_slim.png");
        DYMOND_SLIM_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/dymond_slim_dyed.png");
        DRAGOON_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/dragoon_slim.png");
        DRAGOON_SLIM_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/dragoon_slim_dyed.png");
        COPERNICUS_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/copernicus_slim.png");
        COPERNICUS_SLIM_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/copernicus_slim_dyed.png");
        GENOS_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/genos_slim.png");
        GENOS_SLIM_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/genos_slim_dyed.png");
        BOLT_LEFT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/bolt_left_leg.png");
        BOLT_LEFT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/bolt_left_slim.png");
        BOLT_LEFT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/bolt_left_wide.png");
        BOLT_RIGHT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/bolt_right_leg.png");
        BOLT_RIGHT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/bolt_right_slim.png");
        BOLT_RIGHT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/bolt_right_wide.png");
        BOLT_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/bolt_full_body.png");
        BOLT_SLIM_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/bolt_slim_full_body.png");
        COAST_LEFT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/coast_left_leg.png");
        COAST_LEFT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/coast_left_slim.png");
        COAST_LEFT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/coast_left_wide.png");
        COAST_RIGHT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/coast_right_leg.png");
        COAST_RIGHT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/coast_right_slim.png");
        COAST_RIGHT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/coast_right_wide.png");
        COAST_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/coast_full_body.png");
        COAST_SLIM_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/coast_slim_full_body.png");
        DUNE_LEFT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/dune_left_leg.png");
        DUNE_LEFT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/dune_left_slim.png");
        DUNE_LEFT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/dune_left_wide.png");
        DUNE_RIGHT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/dune_right_leg.png");
        DUNE_RIGHT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/dune_right_slim.png");
        DUNE_RIGHT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/dune_right_wide.png");
        DUNE_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/dune_full_body.png");
        DUNE_SLIM_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/dune_slim_full_body.png");
        EYE_LEFT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/eye_left_leg.png");
        EYE_LEFT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/eye_left_slim.png");
        EYE_LEFT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/eye_left_wide.png");
        EYE_RIGHT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/eye_right_leg.png");
        EYE_RIGHT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/eye_right_slim.png");
        EYE_RIGHT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/eye_right_wide.png");
        EYE_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/eye_full_body.png");
        EYE_SLIM_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/eye_slim_full_body.png");
        FLOW_LEFT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/flow_left_leg.png");
        FLOW_LEFT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/flow_left_slim.png");
        FLOW_LEFT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/flow_left_wide.png");
        FLOW_RIGHT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/flow_right_leg.png");
        FLOW_RIGHT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/flow_right_slim.png");
        FLOW_RIGHT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/flow_right_wide.png");
        FLOW_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/flow_full_body.png");
        FLOW_SLIM_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/flow_slim_full_body.png");
        HOST_LEFT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/host_left_leg.png");
        HOST_LEFT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/host_left_slim.png");
        HOST_LEFT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/host_left_wide.png");
        HOST_RIGHT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/host_right_leg.png");
        HOST_RIGHT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/host_right_slim.png");
        HOST_RIGHT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/host_right_wide.png");
        HOST_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/host_full_body.png");
        HOST_SLIM_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/host_slim_full_body.png");
        RAISER_LEFT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/raiser_left_leg.png");
        RAISER_LEFT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/raiser_left_slim.png");
        RAISER_LEFT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/raiser_left_wide.png");
        RAISER_RIGHT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/raiser_right_leg.png");
        RAISER_RIGHT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/raiser_right_slim.png");
        RAISER_RIGHT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/raiser_right_wide.png");
        RAISER_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/raiser_full_body.png");
        RAISER_SLIM_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/raiser_slim_full_body.png");
        RIB_LEFT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/rib_left_leg.png");
        RIB_LEFT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/rib_left_slim.png");
        RIB_LEFT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/rib_left_wide.png");
        RIB_RIGHT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/rib_right_leg.png");
        RIB_RIGHT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/rib_right_slim.png");
        RIB_RIGHT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/rib_right_wide.png");
        RIB_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/rib_full_body.png");
        RIB_SLIM_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/rib_slim_full_body.png");
        SENTRY_LEFT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/sentry_left_leg.png");
        SENTRY_LEFT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/sentry_left_slim.png");
        SENTRY_LEFT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/sentry_left_wide.png");
        SENTRY_RIGHT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/sentry_right_leg.png");
        SENTRY_RIGHT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/sentry_right_slim.png");
        SENTRY_RIGHT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/sentry_right_wide.png");
        SENTRY_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/sentry_full_body.png");
        SENTRY_SLIM_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/sentry_slim_full_body.png");
        SHAPER_LEFT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/shaper_left_leg.png");
        SHAPER_LEFT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/shaper_left_slim.png");
        SHAPER_LEFT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/shaper_left_wide.png");
        SHAPER_RIGHT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/shaper_right_leg.png");
        SHAPER_RIGHT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/shaper_right_slim.png");
        SHAPER_RIGHT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/shaper_right_wide.png");
        SHAPER_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/shaper_full_body.png");
        SHAPER_SLIM_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/shaper_slim_full_body.png");
        SILENCE_LEFT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/silence_left_leg.png");
        SILENCE_LEFT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/silence_left_slim.png");
        SILENCE_LEFT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/silence_left_wide.png");
        SILENCE_RIGHT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/silence_right_leg.png");
        SILENCE_RIGHT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/silence_right_slim.png");
        SILENCE_RIGHT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/silence_right_wide.png");
        SILENCE_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/silence_full_body.png");
        SILENCE_SLIM_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/silence_slim_full_body.png");
        SNOUT_LEFT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/snout_left_leg.png");
        SNOUT_LEFT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/snout_left_slim.png");
        SNOUT_LEFT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/snout_left_wide.png");
        SNOUT_RIGHT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/snout_right_leg.png");
        SNOUT_RIGHT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/snout_right_slim.png");
        SNOUT_RIGHT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/snout_right_wide.png");
        SNOUT_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/snout_full_body.png");
        SNOUT_SLIM_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/snout_slim_full_body.png");
        SPIRE_LEFT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/spire_left_leg.png");
        SPIRE_LEFT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/spire_left_slim.png");
        SPIRE_LEFT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/spire_left_wide.png");
        SPIRE_RIGHT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/spire_right_leg.png");
        SPIRE_RIGHT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/spire_right_slim.png");
        SPIRE_RIGHT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/spire_right_wide.png");
        SPIRE_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/spire_full_body.png");
        SPIRE_SLIM_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/spire_slim_full_body.png");
        TIDE_LEFT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/tide_left_leg.png");
        TIDE_LEFT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/tide_left_slim.png");
        TIDE_LEFT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/tide_left_wide.png");
        TIDE_RIGHT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/tide_right_leg.png");
        TIDE_RIGHT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/tide_right_slim.png");
        TIDE_RIGHT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/tide_right_wide.png");
        TIDE_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/tide_full_body.png");
        TIDE_SLIM_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/tide_slim_full_body.png");
        VEX_LEFT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/vex_left_leg.png");
        VEX_LEFT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/vex_left_slim.png");
        VEX_LEFT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/vex_left_wide.png");
        VEX_RIGHT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/vex_right_leg.png");
        VEX_RIGHT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/vex_right_slim.png");
        VEX_RIGHT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/vex_right_wide.png");
        VEX_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/vex_full_body.png");
        VEX_SLIM_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/vex_slim_full_body.png");
        WARD_LEFT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/ward_left_leg.png");
        WARD_LEFT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/ward_left_slim.png");
        WARD_LEFT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/ward_left_wide.png");
        WARD_RIGHT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/ward_right_leg.png");
        WARD_RIGHT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/ward_right_slim.png");
        WARD_RIGHT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/ward_right_wide.png");
        WARD_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/ward_full_body.png");
        WARD_SLIM_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/ward_slim_full_body.png");
        WAYFINDER_LEFT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/wayfinder_left_leg.png");
        WAYFINDER_LEFT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/wayfinder_left_slim.png");
        WAYFINDER_LEFT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/wayfinder_left_wide.png");
        WAYFINDER_RIGHT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/wayfinder_right_leg.png");
        WAYFINDER_RIGHT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/wayfinder_right_slim.png");
        WAYFINDER_RIGHT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/wayfinder_right_wide.png");
        WAYFINDER_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/wayfinder_full_body.png");
        WAYFINDER_SLIM_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/wayfinder_slim_full_body.png");
        WILD_LEFT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/wild_left_leg.png");
        WILD_LEFT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/wild_left_slim.png");
        WILD_LEFT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/wild_left_wide.png");
        WILD_RIGHT_LEG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/wild_right_leg.png");
        WILD_RIGHT_SLIM = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/wild_right_slim.png");
        WILD_RIGHT_WIDE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/wild_right_wide.png");
        WILD_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/wild_full_body.png");
        WILD_SLIM_FULL_BODY = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/trims/wild_slim_full_body.png");
    }

    private static enum Limb {
        ARM("arm"),
        LEG("leg");

        final String id;

        private Limb(String id) {
            this.id = id;
        }
    }

    private record TrimInfo(ResourceLocation patternId, int tint) {
    }
}

