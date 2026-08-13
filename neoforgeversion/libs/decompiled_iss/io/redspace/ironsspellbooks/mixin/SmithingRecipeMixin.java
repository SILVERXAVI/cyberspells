/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.tags.ItemTags
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.SmithingRecipeInput
 *  net.minecraft.world.item.crafting.SmithingTransformRecipe
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainerMutable;
import io.redspace.ironsspellbooks.api.spells.SpellSlot;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={SmithingTransformRecipe.class})
public class SmithingRecipeMixin {
    @Inject(method={"Lnet/minecraft/world/item/crafting/SmithingTransformRecipe;assemble(Lnet/minecraft/world/item/crafting/SmithingRecipeInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;"}, at={@At(value="RETURN")}, cancellable=true)
    public void fixSpellbookSlotCount(SmithingRecipeInput pInput, HolderLookup.Provider pRegistries, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack result = (ItemStack)cir.getReturnValue();
        ItemStack input = pInput.base();
        ISpellContainer defaultResultContainer = (ISpellContainer)result.getItem().getDefaultInstance().get(ComponentRegistry.SPELL_CONTAINER);
        ISpellContainer baseContainer = (ISpellContainer)input.get(ComponentRegistry.SPELL_CONTAINER);
        if (defaultResultContainer != null && baseContainer != null) {
            ISpellContainerMutable mutable = defaultResultContainer.mutableCopy();
            for (SpellSlot slot : baseContainer.getActiveSpells()) {
                mutable.addSpellAtIndex(slot.getSpell(), slot.getLevel(), slot.index(), slot.isLocked());
            }
            ISpellContainer.set(result, mutable.toImmutable());
            cir.setReturnValue((Object)result);
        }
        if (input.is(ItemTags.DYEABLE) && !result.is(ItemTags.DYEABLE) && input.has(DataComponents.DYED_COLOR)) {
            result.remove(DataComponents.DYED_COLOR);
            cir.setReturnValue((Object)result);
        }
    }
}

