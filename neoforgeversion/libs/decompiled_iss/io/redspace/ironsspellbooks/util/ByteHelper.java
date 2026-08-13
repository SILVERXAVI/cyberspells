/*
 * Decompiled with CFR 0.152.
 */
package io.redspace.ironsspellbooks.util;

import io.redspace.ironsspellbooks.datafix.ParallelMatcher;
import java.io.DataInputStream;
import java.io.IOException;

public class ByteHelper {
    public static int indexOf(byte[] array, int length, byte[] target) {
        if (array == null || target == null || target.length == 0 || length == 0) {
            return -1;
        }
        block0: for (int i = 0; i < length - target.length + 1; ++i) {
            for (int j = 0; j < target.length; ++j) {
                if (array[i + j] != target[j]) continue block0;
            }
            return i;
        }
        return -1;
    }

    public static int indexOf(DataInputStream dataInputStream, byte[] target) throws IOException {
        int data;
        if (dataInputStream == null || target == null || target.length == 0) {
            return -1;
        }
        int index = -1;
        block0: do {
            data = dataInputStream.read();
            ++index;
            int addlReadCount = 0;
            for (int j = 0; j < target.length; ++j) {
                if (data != target[j]) {
                    index += addlReadCount;
                    continue block0;
                }
                if (j >= target.length - 1) continue;
                data = dataInputStream.read();
                ++addlReadCount;
                if (data != -1) continue;
                return -1;
            }
            return index;
        } while (data != -1);
        return -1;
    }

    public static int indexOf(DataInputStream dataInputStream, ParallelMatcher parallelMatcher) throws IOException {
        int data;
        if (dataInputStream == null || parallelMatcher == null) {
            return -1;
        }
        int index = -1;
        do {
            data = dataInputStream.read();
            ++index;
            if (!parallelMatcher.pushValue(data)) continue;
            return index;
        } while (data != -1);
        return -1;
    }
}

