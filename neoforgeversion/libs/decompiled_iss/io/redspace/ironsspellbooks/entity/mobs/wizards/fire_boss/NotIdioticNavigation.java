/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Position
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.navigation.GroundPathNavigation
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.pathfinder.Node
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.CollisionContext
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

public class NotIdioticNavigation
extends GroundPathNavigation {
    protected static final boolean debugparticles = false;

    public NotIdioticNavigation(Mob pMob, Level pLevel) {
        super(pMob, pLevel);
    }

    protected void trimPath() {
        super.trimPath();
        ArrayList<Node> dumbNodes = new ArrayList<Node>();
        if (this.path == null || this.path.getNextNodeIndex() >= this.path.nodes.size()) {
            return;
        }
        try {
            Vec3 lastImportantNode = this.path.getNextNode().asVec3();
            Vec3 finalNode = this.path.getEndNode().asVec3();
            if (Math.abs(lastImportantNode.y - finalNode.y) <= 2.0 && this.level.clip(new ClipContext(lastImportantNode.add(0.0, 0.75, 0.0), finalNode.add(0.0, 0.75, 0.0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty())).getType() == HitResult.Type.MISS && this.isTraversable(lastImportantNode, finalNode)) {
                for (int i = this.path.getNextNodeIndex() + 1; i < this.path.nodes.size() - 1; ++i) {
                    dumbNodes.add(this.path.getNode(i));
                }
            } else {
                for (int i = this.path.getNextNodeIndex() + 2; i < this.path.nodes.size(); ++i) {
                    Vec3 delta2;
                    Vec3 node1 = this.path.getNode(i - 1).asVec3();
                    Vec3 node2 = this.path.getNode(i).asVec3();
                    Vec3 delta1 = node1.subtract(lastImportantNode).multiply(1.0, 3.0, 1.0).normalize();
                    if (delta1.dot(delta2 = node2.subtract(node1).multiply(1.0, 3.0, 1.0).normalize()) > 0.88 && this.isTraversable(lastImportantNode, node2)) {
                        dumbNodes.add(this.path.getNode(i - 1));
                        continue;
                    }
                    lastImportantNode = node1;
                }
            }
            this.path.nodes.removeAll(dumbNodes);
        }
        catch (Exception e) {
            IronsSpellbooks.LOGGER.error(e.getMessage());
            this.path = null;
        }
    }

    protected boolean isTraversable(Vec3 pos1, Vec3 pos2) {
        Vec3 step = pos2.subtract(pos1);
        double distance = step.length();
        step = step.scale(1.0 / distance);
        int i = 0;
        while ((double)i < distance) {
            BlockPos currentPos = BlockPos.containing((Position)pos1.add(step.scale((double)i)));
            if (this.mob.getType().isBlockDangerous(this.level.getBlockState(currentPos))) {
                return false;
            }
            if (!this.level.getBlockState(currentPos.below()).isFaceSturdy((BlockGetter)this.level, currentPos.below(), Direction.UP)) {
                return false;
            }
            ++i;
        }
        return true;
    }

    public void tick() {
        super.tick();
    }
}

