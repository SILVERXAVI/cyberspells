/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Tuple
 */
package io.redspace.ironsspellbooks.util;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.Tuple;

public class CodeTimer {
    private final List<Tuple<String, Long>> timing = new ArrayList<Tuple<String, Long>>();

    public CodeTimer() {
        this.add("START");
    }

    public void add(String name) {
        this.timing.add((Tuple<String, Long>)new Tuple((Object)name, (Object)System.nanoTime()));
    }

    public String getOutput(String delimiter) {
        StringBuilder sb = new StringBuilder();
        long itemDelta = 0L;
        long totalDelta = 0L;
        for (int i = 0; i < this.timing.size(); ++i) {
            Tuple<String, Long> item = this.timing.get(i);
            if (i <= 0) continue;
            Tuple<String, Long> lastItem = this.timing.get(i - 1);
            itemDelta = (Long)item.getB() - (Long)lastItem.getB();
            sb.append(String.format("%s%s%s%s%f%s%f\n", lastItem.getA(), delimiter, item.getA(), delimiter, (double)itemDelta / 1000000.0, delimiter, (double)(totalDelta += itemDelta) / 1000000.0));
        }
        return sb.toString();
    }

    public String toString() {
        return this.getOutput("\t");
    }
}

