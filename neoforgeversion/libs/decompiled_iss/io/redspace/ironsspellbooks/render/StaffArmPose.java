/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.HumanoidModel$ArmPose
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.HumanoidArm
 *  net.neoforged.fml.common.asm.enumextension.EnumProxy
 */
package io.redspace.ironsspellbooks.render;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

public class StaffArmPose {
    public static final EnumProxy<HumanoidModel.ArmPose> STAFF_ARM_POSE = new EnumProxy(HumanoidModel.ArmPose.class, new Object[]{false, (model, entity, arm) -> {
        (arm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm).xRot = Mth.lerp((float)0.85f, (float)(arm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm).xRot, (float)(-0.8975979f + model.head.xRot / 2.0f));
    }});
}

