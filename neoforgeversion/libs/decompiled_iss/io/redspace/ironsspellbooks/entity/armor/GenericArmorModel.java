/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  org.jetbrains.annotations.Nullable
 *  software.bernie.geckolib.cache.GeckoLibCache
 *  software.bernie.geckolib.model.DefaultedItemGeoModel
 *  software.bernie.geckolib.renderer.GeoArmorRenderer
 *  software.bernie.geckolib.renderer.GeoRenderer
 */
package io.redspace.ironsspellbooks.entity.armor;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.item.armor.ExtendedArmorItem;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;

public class GenericArmorModel<T extends ExtendedArmorItem>
extends DefaultedItemGeoModel<T> {
    private final ResourceLocation model;
    private final ResourceLocation texture;
    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"animations/wizard_armor_animation.json");
    private final Map<String, ModelVariantResult> modelVariants;

    public GenericArmorModel(String modid, String name) {
        super(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)""));
        this.model = ResourceLocation.fromNamespaceAndPath((String)modid, (String)String.format("geo/%s_armor.geo.json", name));
        this.texture = ResourceLocation.fromNamespaceAndPath((String)modid, (String)String.format("textures/models/armor/%s.png", name));
        this.modelVariants = new HashMap<String, ModelVariantResult>();
    }

    public GenericArmorModel(String name) {
        this("irons_spellbooks", name);
    }

    public GenericArmorModel<T> variants(Map<String, ResourceLocation> modelVariants) {
        modelVariants.forEach((string, location) -> this.modelVariants.put((String)string, new ModelVariantResult((ResourceLocation)location, false)));
        return this;
    }

    public ResourceLocation getModelResource(T animatable, @Nullable GeoRenderer<T> renderer) {
        ModelVariantResult result;
        String transmogVariant;
        GeoArmorRenderer armorRenderer;
        if (renderer instanceof GeoArmorRenderer && (armorRenderer = (GeoArmorRenderer)renderer).getCurrentStack() != null && (transmogVariant = (String)armorRenderer.getCurrentStack().get(ComponentRegistry.CLOTHING_VARIANT)) != null && (result = this.modelVariants.get(transmogVariant)) != null && this.validateModelLocation(result, transmogVariant)) {
            return result.location;
        }
        return this.model;
    }

    private boolean validateModelLocation(ModelVariantResult result, String transmogVariant) {
        if (result.validated) {
            return true;
        }
        if (GeckoLibCache.getBakedModels().get(result.location) != null) {
            this.modelVariants.put(transmogVariant, new ModelVariantResult(result.location, true));
            return true;
        }
        this.modelVariants.remove(transmogVariant);
        IronsSpellbooks.LOGGER.error("Could not find model variant location \"{}\", ignoring for the future!", (Object)result.location);
        return false;
    }

    public ResourceLocation getTextureResource(T animatable) {
        return this.texture;
    }

    public ResourceLocation getAnimationResource(T animatable) {
        return ANIMATION;
    }

    record ModelVariantResult(ResourceLocation location, boolean validated) {
    }
}

