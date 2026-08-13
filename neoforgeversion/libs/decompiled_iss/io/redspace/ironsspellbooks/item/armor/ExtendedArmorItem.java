/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Suppliers
 *  javax.annotation.Nullable
 *  net.minecraft.client.model.HumanoidModel
 *  net.minecraft.core.Holder
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.EquipmentSlotGroup
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.item.ArmorItem
 *  net.minecraft.world.item.ArmorItem$Type
 *  net.minecraft.world.item.ArmorMaterial
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.ItemAttributeModifiers
 *  net.minecraft.world.item.component.ItemAttributeModifiers$Builder
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animatable.GeoItem
 *  software.bernie.geckolib.animatable.client.GeoRenderProvider
 *  software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
 *  software.bernie.geckolib.animation.AnimatableManager$ControllerRegistrar
 *  software.bernie.geckolib.animation.AnimationController
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.animation.PlayState
 *  software.bernie.geckolib.animation.RawAnimation
 *  software.bernie.geckolib.renderer.GeoArmorRenderer
 *  software.bernie.geckolib.util.GeckoLibUtil
 */
package io.redspace.ironsspellbooks.item.armor;

import com.google.common.base.Suppliers;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class ExtendedArmorItem
extends ArmorItem
implements GeoItem {
    private final Supplier<ItemAttributeModifiers> defaultModifiers;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache((GeoAnimatable)this);

    public ExtendedArmorItem(Holder<ArmorMaterial> pMaterial, ArmorItem.Type pType, Item.Properties pProperties, AttributeContainer ... attributes) {
        super(pMaterial, pType, pProperties);
        this.defaultModifiers = Suppliers.memoize(() -> {
            int i = ((ArmorMaterial)pMaterial.value()).getDefense(pType);
            float f = ((ArmorMaterial)pMaterial.value()).toughness();
            ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
            EquipmentSlotGroup equipmentslotgroup = EquipmentSlotGroup.bySlot((EquipmentSlot)pType.getSlot());
            ResourceLocation resourcelocation = ResourceLocation.withDefaultNamespace((String)("armor." + pType.getName()));
            builder.add(Attributes.ARMOR, new AttributeModifier(resourcelocation, (double)i, AttributeModifier.Operation.ADD_VALUE), equipmentslotgroup);
            builder.add(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(resourcelocation, (double)f, AttributeModifier.Operation.ADD_VALUE), equipmentslotgroup);
            float f1 = ((ArmorMaterial)pMaterial.value()).knockbackResistance();
            if (f1 > 0.0f) {
                builder.add(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(resourcelocation, (double)f1, AttributeModifier.Operation.ADD_VALUE), equipmentslotgroup);
            }
            for (AttributeContainer holder : attributes) {
                builder.add(holder.attribute(), holder.createModifier(pType.getSlot().getName()), equipmentslotgroup);
            }
            return builder.build();
        });
    }

    public static AttributeContainer[] schoolAttributes(Holder<Attribute> school) {
        return new AttributeContainer[]{new AttributeContainer((Holder<Attribute>)AttributeRegistry.MAX_MANA, 125.0, AttributeModifier.Operation.ADD_VALUE), new AttributeContainer(school, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), new AttributeContainer((Holder<Attribute>)AttributeRegistry.SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)};
    }

    public static AttributeContainer[] withManaAttribute(int mana) {
        return new AttributeContainer[]{new AttributeContainer((Holder<Attribute>)AttributeRegistry.MAX_MANA, mana, AttributeModifier.Operation.ADD_VALUE)};
    }

    public static AttributeContainer[] withManaAndSpellPowerAttribute(int mana, double spellPower) {
        return new AttributeContainer[]{new AttributeContainer((Holder<Attribute>)AttributeRegistry.MAX_MANA, mana, AttributeModifier.Operation.ADD_VALUE), new AttributeContainer((Holder<Attribute>)AttributeRegistry.SPELL_POWER, spellPower, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)};
    }

    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        return this.defaultModifiers.get();
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController((GeoAnimatable)this, "controller", 20, this::predicate));
    }

    private PlayState predicate(AnimationState<ExtendedArmorItem> extendedArmorItemAnimationState) {
        extendedArmorItemAnimationState.getController().setAnimation(RawAnimation.begin().thenLoop("idle"));
        return PlayState.CONTINUE;
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider(){
            private GeoArmorRenderer<?> renderer;

            public <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
                if (this.renderer == null) {
                    this.renderer = ExtendedArmorItem.this.supplyRenderer();
                }
                return this.renderer;
            }
        });
    }

    @OnlyIn(value=Dist.CLIENT)
    public abstract GeoArmorRenderer<?> supplyRenderer();
}

