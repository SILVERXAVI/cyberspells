/*
 * Decompiled with CFR 0.152.
 */
package com.perigrine3.createcybernetics.client.model;

import com.perigrine3.createcybernetics.client.model.PlayerAttachment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PlayerAttachmentState {
    private final List<PlayerAttachment> attachments = new ArrayList<PlayerAttachment>();

    public void clear() {
        this.attachments.clear();
    }

    public void add(PlayerAttachment a) {
        this.attachments.add(a);
    }

    public boolean isEmpty() {
        return this.attachments.isEmpty();
    }

    public List<PlayerAttachment> all() {
        return Collections.unmodifiableList(this.attachments);
    }
}

