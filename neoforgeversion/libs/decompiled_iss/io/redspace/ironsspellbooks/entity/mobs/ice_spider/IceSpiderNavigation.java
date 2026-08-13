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
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.pathfinder.Node
 *  net.minecraft.world.level.pathfinder.Path
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.mobs.ice_spider;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class IceSpiderNavigation
extends GroundPathNavigation {
    public IceSpiderNavigation(Mob pMob, Level pLevel) {
        super(pMob, pLevel);
    }

    public void setPath(Path path) {
        this.path = path;
    }

    protected void trimPath() {
        if (this.path == null || this.path.getNextNodeIndex() >= this.path.nodes.size()) {
            return;
        }
        try {
            double baseY = this.mob.getY();
            float maxStepUp = this.mob.maxUpStep();
            Vec3 finalNode = this.path.getEndNode().asVec3();
            if (finalNode.y - baseY > (double)maxStepUp) {
                Vec3 directionVector = finalNode.subtract(this.mob.position());
                for (int i = this.path.getNextNodeIndex(); i < this.path.nodes.size(); ++i) {
                    Vec3 node = this.path.getNode(i).asVec3();
                    if (!(finalNode.subtract(node).dot(directionVector) > 0.8) || !this.isTraversable(node, finalNode)) continue;
                    ArrayList<Node> inbetweenNodes = new ArrayList<Node>();
                    for (int j = i + 1; j < this.path.nodes.size() - 1; ++j) {
                        inbetweenNodes.add((Node)this.path.nodes.get(j));
                    }
                    this.path.nodes.removeAll(inbetweenNodes);
                    return;
                }
            }
        }
        catch (Exception e) {
            IronsSpellbooks.LOGGER.error(e.getMessage());
            this.path = null;
        }
        super.trimPath();
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
}

