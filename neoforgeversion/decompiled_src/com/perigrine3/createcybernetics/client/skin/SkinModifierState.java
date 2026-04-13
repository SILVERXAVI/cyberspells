/*
 * Decompiled with CFR 0.152.
 */
package com.perigrine3.createcybernetics.client.skin;

import com.perigrine3.createcybernetics.client.skin.SkinHighlight;
import com.perigrine3.createcybernetics.client.skin.SkinModifier;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class SkinModifierState {
    private final List<SkinModifier> modifiers = new ArrayList<SkinModifier>();
    private boolean hideVanillaLayers = false;
    private final List<SkinHighlight> highlights = new ArrayList<SkinHighlight>();

    public void addModifier(SkinModifier modifier) {
        this.modifiers.add(modifier);
        if (modifier.shouldHideVanillaLayers()) {
            this.hideVanillaLayers = true;
        }
    }

    public boolean removeModifier(SkinModifier modifier) {
        boolean removed = this.modifiers.remove(modifier);
        if (removed && modifier.shouldHideVanillaLayers()) {
            this.hideVanillaLayers = this.modifiers.stream().anyMatch(SkinModifier::shouldHideVanillaLayers);
        }
        return removed;
    }

    public void clearModifiers() {
        this.modifiers.clear();
        this.hideVanillaLayers = false;
        this.highlights.clear();
    }

    public boolean hasModifiers() {
        return !this.modifiers.isEmpty();
    }

    public List<SkinModifier> getModifiers() {
        return this.modifiers;
    }

    public boolean shouldHideVanillaLayers() {
        return this.hideVanillaLayers;
    }

    public EnumSet<SkinModifier.HideVanilla> getHideMask() {
        if (this.hideVanillaLayers) {
            return EnumSet.allOf(SkinModifier.HideVanilla.class);
        }
        EnumSet<SkinModifier.HideVanilla> mask = EnumSet.noneOf(SkinModifier.HideVanilla.class);
        for (SkinModifier m : this.modifiers) {
            mask.addAll(m.getHideMask());
        }
        return mask;
    }

    public void addHighlight(SkinHighlight highlight) {
        if (highlight != null) {
            this.highlights.add(highlight);
        }
    }

    public void clearHighlights() {
        this.highlights.clear();
    }

    public boolean hasHighlights() {
        return !this.highlights.isEmpty();
    }

    public List<SkinHighlight> getHighlights() {
        return this.highlights;
    }
}

