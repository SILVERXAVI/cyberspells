/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Holder
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.SoundType
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraft.world.level.gameevent.GameEvent$Context
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.block.ice_spider_egg;

import com.mojang.serialization.MapCodec;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.ice_spider.IceSpiderEntity;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IceSpiderEggBlock
extends Block {
    public static final MapCodec<IceSpiderEggBlock> CODEC = IceSpiderEggBlock.simpleCodec(IceSpiderEggBlock::new);
    public static final BooleanProperty EGG_FROSTED = BooleanProperty.create((String)"frosted");
    static final VoxelShape SHAPE = Block.box((double)3.0, (double)0.0, (double)2.0, (double)13.0, (double)14.0, (double)14.0);
    static final VoxelShape SHAPE_FROSTED = Block.box((double)2.0, (double)0.0, (double)1.0, (double)14.0, (double)15.0, (double)15.0);
    static int raycastCount = 0;

    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    public IceSpiderEggBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)EGG_FROSTED, (Comparable)Boolean.valueOf(false)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(new Property[]{EGG_FROSTED});
    }

    public void playerDestroy(Level level, @NotNull Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        if (state.is(BlockRegistry.ICE_SPIDER_EGG)) {
            boolean isFrosted = (Boolean)state.getValue((Property)EGG_FROSTED);
            if (isFrosted && this.summonSpiderAround(player)) {
                IronsSpellbooks.LOGGER.debug("summonSpiderAround rcc: {}", (Object)raycastCount);
                level.setBlock(pos, (BlockState)state.setValue((Property)EGG_FROSTED, (Comparable)Boolean.valueOf(false)), 2);
                level.gameEvent((Holder)GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of((BlockState)state));
                level.levelEvent(2001, pos, Block.getId((BlockState)state));
            } else {
                level.destroyBlock(pos, false);
            }
        }
    }

    @NotNull
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.hasProperty((Property)EGG_FROSTED) && (Boolean)state.getValue((Property)EGG_FROSTED) != false ? SHAPE_FROSTED : SHAPE;
    }

    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        return state.hasProperty((Property)EGG_FROSTED) && (Boolean)state.getValue((Property)EGG_FROSTED) != false ? SoundType.GLASS : SoundType.HONEY_BLOCK;
    }

    private <T> void shuffle(T[] ary) {
        Random rand = new Random();
        for (int i = ary.length - 1; i > 0; --i) {
            int j = rand.nextInt(i + 1);
            T temp = ary[i];
            ary[i] = ary[j];
            ary[j] = temp;
        }
    }

    private boolean summonSpiderAround(Player player) {
        BlockPos center = player.blockPosition();
        Vec3 origin = player.getBoundingBox().getCenter();
        Level level = player.level;
        int range = 24;
        IceSpiderEntity spider = new IceSpiderEntity(level);
        spider.setPersistenceRequired();
        Vec3[] probeDirections = new Vec3[]{new Vec3(1.0, 0.0, 0.0), new Vec3(-1.0, 0.0, 0.0), new Vec3(0.0, 0.0, 1.0), new Vec3(0.0, 0.0, -1.0), new Vec3(0.7, 0.0, 0.7), new Vec3(-0.7, 0.0, 0.7), new Vec3(-0.7, 0.0, -0.7), new Vec3(0.7, 0.0, -0.7)};
        this.shuffle(probeDirections);
        double stepLength = 5.0;
        raycastCount = 0;
        Vec3 farthest = origin;
        for (Vec3 initialProbe : probeDirections) {
            Vec3 initialCast = initialProbe.scale(stepLength);
            BlockHitResult bhr = this.castRayTowardsEmptySpace(level, origin, origin.add(initialCast));
            Vec3 currentPosition = this.hoverAboveGround(level, bhr.getLocation());
            int maxItr = 6;
            Vec3 bias = initialCast;
            for (int i = 0; i < maxItr; ++i) {
                bhr = this.pickFarthestRayFromRadialCascade(level, currentPosition, bias, probeDirections, stepLength, 2.0);
                Vec3 node = bhr.getLocation();
                if (bhr.getType() != HitResult.Type.MISS) {
                    node = node.subtract(node.subtract(currentPosition).normalize());
                }
                node = this.hoverAboveGround(level, node);
                bias = node.subtract(currentPosition);
                currentPosition = node;
                if (currentPosition.distanceToSqr(origin) > (double)(range * range)) {
                    if (!this.tryPlaceSpiderInWorld(spider, currentPosition, player)) continue;
                    return true;
                }
                if (!(currentPosition.distanceToSqr(origin) > farthest.distanceToSqr(origin)) || !this.tryMoveSpider(spider, currentPosition)) continue;
                farthest = currentPosition;
            }
        }
        return this.tryPlaceSpiderInWorld(spider, farthest, player);
    }

    boolean tryMoveSpider(IceSpiderEntity spider, Vec3 pos) {
        Level level = spider.level;
        Vec3 originalPos = spider.position();
        pos = Utils.moveToRelativeGroundLevel(level, pos, 2);
        spider.moveTo(pos);
        Vec3 adjustedPos = level.findFreePosition((Entity)spider, Shapes.create((AABB)spider.getBoundingBox()), pos, 0.25, 0.25, 0.25).orElse(pos);
        spider.moveTo(adjustedPos);
        AABB bb = spider.getBoundingBox();
        if (level.noCollision(bb) && !level.containsAnyLiquid(bb)) {
            return true;
        }
        spider.moveTo(originalPos);
        return false;
    }

    boolean tryPlaceSpiderInWorld(IceSpiderEntity spider, Vec3 pos, Player player) {
        Level level = spider.level;
        if (this.tryMoveSpider(spider, pos)) {
            level.playSound(null, spider.blockPosition(), (SoundEvent)SoundRegistry.ICE_SPIDER_HOWL.get(), SoundSource.HOSTILE, 4.0f, 1.0f);
            spider.setEmergeFromGround();
            spider.setTarget((LivingEntity)player);
            spider.setYRot(Utils.getAngle(pos.x, pos.z, player.getX(), player.getZ()) * 57.295776f + 90.0f);
            spider.setYBodyRot(spider.getYRot());
            level.addFreshEntity((Entity)spider);
            return true;
        }
        return false;
    }

    Vec3 hoverAboveGround(Level level, Vec3 vec3) {
        return Utils.moveToRelativeGroundLevel(level, vec3, 1, 12).add(0.0, 1.0, 0.0);
    }

    BlockHitResult pickFarthestRayFromRadialCascade(Level level, Vec3 origin, Vec3 bias, Vec3[] probeDirections, double stepLength, double randomness) {
        ArrayList hits = new ArrayList(probeDirections.length);
        for (Vec3 dir : probeDirections) {
            hits.add(this.castRayTowardsEmptySpace(level, origin, origin.add(dir.scale(stepLength)).add(bias.scale(0.5)).add(Utils.getRandomVec3(randomness))));
        }
        hits.sort(Comparator.comparingDouble(hit -> hit.getLocation().distanceToSqr(origin)));
        return (BlockHitResult)hits.getLast();
    }

    BlockHitResult castRayTowardsEmptySpace(Level level, Vec3 start, Vec3 target) {
        ++raycastCount;
        return level.clip(new ClipContext(start, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, CollisionContext.empty()));
    }
}

