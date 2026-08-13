/*
 * Decompiled with CFR 0.152.
 */
package io.redspace.ironsspellbooks.api.util;

public interface IMusicHandler {
    public void init();

    public void stop();

    public void tick();

    public boolean isDone();

    public void hardStop();

    public void triggerResume();
}

