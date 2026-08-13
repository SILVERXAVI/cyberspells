/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.ai.attributes.RangedAttribute
 */
package io.redspace.ironsspellbooks.api.attribute;

import io.redspace.ironsspellbooks.api.attribute.IMagicAttribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class MagicRangedAttribute
extends RangedAttribute
implements IMagicAttribute {
    public MagicRangedAttribute(String pDescriptionId, double pDefaultValue, double pMin, double pMax) {
        super(pDescriptionId, pDefaultValue, pMin, pMax);
    }
}

