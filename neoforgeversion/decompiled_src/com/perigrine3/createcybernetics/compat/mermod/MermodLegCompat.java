/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.model.PlayerModel
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.renderer.entity.LivingEntityRenderer
 *  net.minecraft.client.renderer.entity.player.PlayerRenderer
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.EventPriority
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.ModList
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.RenderLivingEvent$Post
 *  net.neoforged.neoforge.client.event.RenderLivingEvent$Pre
 */
package com.perigrine3.createcybernetics.compat.mermod;

import com.perigrine3.createcybernetics.compat.curios.CuriosCompat;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;

@EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT}, bus=EventBusSubscriber.Bus.GAME)
public final class MermodLegCompat {
    private static final String MERMOD_MODID = "mermod";
    private static final ResourceLocation SEA_NECKLACE_ID = ResourceLocation.fromNamespaceAndPath((String)"mermod", (String)"sea_necklace");
    private static final TagKey<Item> TAIL_MOISTURIZER_TAG = TagKey.create((ResourceKey)Registries.ITEM, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"mermod", (String)"tail_moisturizer_modifier"));
    private static final String MOISTURIZER_BASE_ID = "tail_moisturizer";
    private static final Map<Integer, VisibilitySnapshot> SNAPSHOTS = new HashMap<Integer, VisibilitySnapshot>();
    private static DataComponentType<?> CACHED_COMPONENT;
    private static boolean REFLECTION_READY;
    private static Method COMPONENT_MODIFIERS_METHOD;
    private static Method MAP_VALUES_METHOD;
    private static Method MODIFIER_ID_METHOD;

    private MermodLegCompat() {
    }

    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void onRenderLivingPre(RenderLivingEvent.Pre<?, ?> event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof AbstractClientPlayer)) {
            return;
        }
        AbstractClientPlayer player = (AbstractClientPlayer)livingEntity;
        LivingEntityRenderer livingEntityRenderer = event.getRenderer();
        if (!(livingEntityRenderer instanceof PlayerRenderer)) {
            return;
        }
        PlayerRenderer renderer = (PlayerRenderer)livingEntityRenderer;
        EntityModel entityModel = renderer.getModel();
        if (!(entityModel instanceof PlayerModel)) {
            return;
        }
        PlayerModel model = (PlayerModel)entityModel;
        if (!MermodLegCompat.shouldHideLegs(player)) {
            return;
        }
        SNAPSHOTS.put(player.getId(), VisibilitySnapshot.capture(model));
        model.leftLeg.visible = false;
        model.rightLeg.visible = false;
        model.leftPants.visible = false;
        model.rightPants.visible = false;
    }

    @SubscribeEvent
    public static void onRenderLivingPost(RenderLivingEvent.Post<?, ?> event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof AbstractClientPlayer)) {
            return;
        }
        AbstractClientPlayer player = (AbstractClientPlayer)livingEntity;
        VisibilitySnapshot snap = SNAPSHOTS.remove(player.getId());
        if (snap == null) {
            return;
        }
        LivingEntityRenderer livingEntityRenderer = event.getRenderer();
        if (!(livingEntityRenderer instanceof PlayerRenderer)) {
            return;
        }
        PlayerRenderer renderer = (PlayerRenderer)livingEntityRenderer;
        EntityModel entityModel = renderer.getModel();
        if (!(entityModel instanceof PlayerModel)) {
            return;
        }
        PlayerModel model = (PlayerModel)entityModel;
        snap.restore(model);
    }

    private static boolean shouldHideLegs(AbstractClientPlayer player) {
        if (!ModList.get().isLoaded(MERMOD_MODID)) {
            return false;
        }
        ItemStack necklace = MermodLegCompat.findEquippedSeaNecklace(player);
        if (necklace.isEmpty()) {
            return false;
        }
        if (player.isInWaterOrBubble()) {
            return true;
        }
        boolean hasMoisturizerByItemTag = necklace.getTags().anyMatch(t -> t.equals(TAIL_MOISTURIZER_TAG));
        if (hasMoisturizerByItemTag) {
            return true;
        }
        return MermodLegCompat.hasMoisturizerModifierViaComponent(necklace);
    }

    private static ItemStack findEquippedSeaNecklace(AbstractClientPlayer player) {
        ResourceLocation id;
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (!chest.isEmpty() && SEA_NECKLACE_ID.equals((Object)(id = BuiltInRegistries.ITEM.getKey((Object)chest.getItem())))) {
            return chest;
        }
        return CuriosCompat.findFirstCurioAnywhere((LivingEntity)player, st -> {
            if (st == null || st.isEmpty()) {
                return false;
            }
            ResourceLocation id = BuiltInRegistries.ITEM.getKey((Object)st.getItem());
            return SEA_NECKLACE_ID.equals((Object)id);
        }).map(CuriosCompat.FoundCurio::stack).orElse(ItemStack.EMPTY);
    }

    private static boolean hasMoisturizerModifierViaComponent(ItemStack necklace) {
        Object componentValue = MermodLegCompat.getNecklaceModifiersComponentValue(necklace);
        if (componentValue == null) {
            return false;
        }
        MermodLegCompat.ensureReflectionReady(componentValue.getClass());
        if (!REFLECTION_READY) {
            return false;
        }
        try {
            Object mapObj = COMPONENT_MODIFIERS_METHOD.invoke(componentValue, new Object[0]);
            if (mapObj == null) {
                return false;
            }
            Object valuesObj = MAP_VALUES_METHOD.invoke(mapObj, new Object[0]);
            if (!(valuesObj instanceof Collection)) {
                return false;
            }
            Collection values = (Collection)valuesObj;
            for (Object modifierObj : values) {
                String rawId;
                Object idObj;
                if (modifierObj == null || !((idObj = MODIFIER_ID_METHOD.invoke(modifierObj, new Object[0])) instanceof String) || !MermodLegCompat.isMoisturizerId(rawId = (String)idObj)) continue;
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
        return MOISTURIZER_BASE_ID.equals(s);
    }

    private static Object getNecklaceModifiersComponentValue(ItemStack stack) {
        DataComponentType<?> type = MermodLegCompat.getNecklaceModifiersComponentType();
        if (type == null) {
            return null;
        }
        return stack.get(type);
    }

    private static DataComponentType<?> getNecklaceModifiersComponentType() {
        if (CACHED_COMPONENT != null) {
            return CACHED_COMPONENT;
        }
        DataComponentType t = (DataComponentType)BuiltInRegistries.DATA_COMPONENT_TYPE.get(ResourceLocation.fromNamespaceAndPath((String)MERMOD_MODID, (String)"necklace_modifiers"));
        if (t != null) {
            CACHED_COMPONENT = t;
            return CACHED_COMPONENT;
        }
        t = (DataComponentType)BuiltInRegistries.DATA_COMPONENT_TYPE.get(ResourceLocation.fromNamespaceAndPath((String)MERMOD_MODID, (String)"necklace_modifiers_component_type"));
        if (t != null) {
            CACHED_COMPONENT = t;
            return CACHED_COMPONENT;
        }
        t = (DataComponentType)BuiltInRegistries.DATA_COMPONENT_TYPE.get(ResourceLocation.fromNamespaceAndPath((String)MERMOD_MODID, (String)"necklace_modifiers_component"));
        if (t != null) {
            CACHED_COMPONENT = t;
            return CACHED_COMPONENT;
        }
        DataComponentType best = null;
        int bestScore = -1;
        for (ResourceLocation key : BuiltInRegistries.DATA_COMPONENT_TYPE.keySet()) {
            DataComponentType candidate;
            if (!MERMOD_MODID.equals(key.getNamespace())) continue;
            String p = key.getPath();
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
            if (score <= bestScore || (candidate = (DataComponentType)BuiltInRegistries.DATA_COMPONENT_TYPE.get(key)) == null) continue;
            best = candidate;
            bestScore = score;
        }
        CACHED_COMPONENT = best;
        return CACHED_COMPONENT;
    }

    private static void ensureReflectionReady(Class<?> necklaceModifiersClass) {
        if (REFLECTION_READY) {
            return;
        }
        try {
            COMPONENT_MODIFIERS_METHOD = necklaceModifiersClass.getMethod("modifiers", new Class[0]);
            MAP_VALUES_METHOD = Map.class.getMethod("values", new Class[0]);
            Class<?> necklaceModifierClass = Class.forName("io.github.thatpreston.mermod.item.modifier.NecklaceModifier");
            MODIFIER_ID_METHOD = necklaceModifierClass.getMethod("id", new Class[0]);
            REFLECTION_READY = true;
        }
        catch (Throwable t) {
            REFLECTION_READY = false;
            COMPONENT_MODIFIERS_METHOD = null;
            MAP_VALUES_METHOD = null;
            MODIFIER_ID_METHOD = null;
        }
    }

    static {
        REFLECTION_READY = false;
    }

    private record VisibilitySnapshot(boolean leftLeg, boolean rightLeg, boolean leftPants, boolean rightPants) {
        static VisibilitySnapshot capture(PlayerModel<?> model) {
            return new VisibilitySnapshot(model.leftLeg.visible, model.rightLeg.visible, model.leftPants.visible, model.rightPants.visible);
        }

        void restore(PlayerModel<?> model) {
            model.leftLeg.visible = this.leftLeg;
            model.rightLeg.visible = this.rightLeg;
            model.leftPants.visible = this.leftPants;
            model.rightPants.visible = this.rightPants;
        }
    }
}

