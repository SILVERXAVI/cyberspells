/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Vector3f
 *  software.bernie.geckolib.animation.state.BoneSnapshot
 *  software.bernie.geckolib.cache.object.GeoBone
 */
package io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import org.joml.Vector3f;
import software.bernie.geckolib.animation.state.BoneSnapshot;
import software.bernie.geckolib.cache.object.GeoBone;

public class TransformStack {
    private final Map<GeoBone, Stack<Vector3f>> positionStack = new HashMap<GeoBone, Stack<Vector3f>>();
    private final Map<GeoBone, Stack<Vector3f>> rotationStack = new HashMap<GeoBone, Stack<Vector3f>>();
    private final Set<GeoBone> toReset = new HashSet<GeoBone>();

    public void pushPosition(GeoBone bone, Vector3f appendVec) {
        Stack<Vector3f> stack = this.positionStack.getOrDefault(bone, new Stack());
        stack.push(appendVec);
        this.positionStack.put(bone, stack);
    }

    public void resetDirty() {
        this.toReset.forEach(bone -> {
            BoneSnapshot snapshot = bone.getInitialSnapshot();
            bone.updatePosition(snapshot.getOffsetX(), snapshot.getOffsetY(), snapshot.getOffsetZ());
            bone.updateRotation(snapshot.getRotX(), snapshot.getRotY(), snapshot.getRotZ());
            bone.resetStateChanges();
        });
        this.toReset.clear();
    }

    public void pushPosition(GeoBone bone, float x, float y, float z) {
        this.pushPosition(bone, new Vector3f(x, y, z));
    }

    public void pushRotation(GeoBone bone, Vector3f appendVec) {
        Stack<Vector3f> stack = this.rotationStack.getOrDefault(bone, new Stack());
        stack.push(appendVec);
        this.rotationStack.put(bone, stack);
    }

    public void pushRotation(GeoBone bone, float x, float y, float z) {
        this.pushRotation(bone, new Vector3f(x, y, z));
    }

    public void pushRotationDegrees(GeoBone bone, float x, float y, float z) {
        this.pushRotation(bone, new Vector3f(x * ((float)Math.PI / 180), y * ((float)Math.PI / 180), z * ((float)Math.PI / 180)));
    }

    public void popStack() {
        this.positionStack.forEach((bone, stack) -> {
            this.toReset.add((GeoBone)bone);
            Vector3f position = bone.getPositionVector().get(new Vector3f());
            stack.forEach(arg_0 -> ((Vector3f)position).add(arg_0));
            this.setPosImpl((GeoBone)bone, position);
        });
        this.rotationStack.forEach((bone, stack) -> {
            this.toReset.add((GeoBone)bone);
            Vector3f rotation = bone.getRotationVector().get(new Vector3f(0.0f, 0.0f, 0.0f));
            stack.forEach(arg_0 -> ((Vector3f)rotation).add(arg_0));
            this.setRotImpl((GeoBone)bone, rotation);
        });
        this.positionStack.clear();
        this.rotationStack.clear();
    }

    public void setRotImpl(GeoBone bone, Vector3f vector3f) {
        bone.updateRotation(TransformStack.wrapRadians(vector3f.x()), TransformStack.wrapRadians(vector3f.y()), TransformStack.wrapRadians(vector3f.z()));
    }

    public void setPosImpl(GeoBone bone, Vector3f vector3f) {
        bone.updatePosition(vector3f.x, vector3f.y, vector3f.z);
    }

    public static float wrapRadians(float pValue) {
        float twoPi = 6.2831f;
        float f = pValue % twoPi;
        float pi = 3.14155f;
        if (f >= pi) {
            f -= twoPi;
        }
        if (f < -pi) {
            f += twoPi;
        }
        return f;
    }
}

