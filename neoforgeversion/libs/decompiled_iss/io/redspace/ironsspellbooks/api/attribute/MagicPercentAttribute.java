/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.neoforge.common.PercentageAttribute
 */
package io.redspace.ironsspellbooks.api.attribute;

import io.redspace.ironsspellbooks.api.attribute.IMagicAttribute;
import net.neoforged.neoforge.common.PercentageAttribute;

public class MagicPercentAttribute
extends PercentageAttribute
implements IMagicAttribute {
    public MagicPercentAttribute(String pDescriptionId, double pDefaultValue, double pMin, double pMax) {
        super(pDescriptionId, pDefaultValue, pMin, pMax);
    }
}

