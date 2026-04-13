/*
 * Decompiled with CFR 0.152.
 */
package com.perigrine3.createcybernetics.api;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import java.util.Map;

public interface ICyberwareData {
    public InstalledCyberware get(CyberwareSlot var1, int var2);

    public void set(CyberwareSlot var1, int var2, InstalledCyberware var3);

    public InstalledCyberware remove(CyberwareSlot var1, int var2);

    public Map<CyberwareSlot, InstalledCyberware[]> getAll();

    public int getHumanity();

    public void setHumanity(int var1);

    public void clear();

    public void setDirty();
}

