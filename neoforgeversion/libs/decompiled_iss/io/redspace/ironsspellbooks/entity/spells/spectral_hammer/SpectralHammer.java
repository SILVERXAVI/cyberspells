/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Vec3i
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.Container
 *  net.minecraft.world.Containers
 *  net.minecraft.world.SimpleContainer
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.HumanoidArm
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.bus.api.Event
 *  net.neoforged.neoforge.common.NeoForge
 *  net.neoforged.neoforge.event.level.BlockEvent$BreakEvent
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animatable.GeoEntity
 *  software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
 *  software.bernie.geckolib.animation.AnimatableManager$ControllerRegistrar
 *  software.bernie.geckolib.animation.AnimationController
 *  software.bernie.geckolib.animation.AnimationController$State
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.animation.PlayState
 *  software.bernie.geckolib.animation.RawAnimation
 *  software.bernie.geckolib.util.GeckoLibUtil
 */
package io.redspace.ironsspellbooks.entity.spells.spectral_hammer;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.particle.FallingBlockParticleOption;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SpectralHammer
extends LivingEntity
implements GeoEntity {
    private final int ticksToLive = 30;
    private final int doDamageTick = 13;
    private final int doAnimateTick = 20;
    private int depth = 0;
    private int radius = 0;
    private boolean didDamage = false;
    private boolean didAnimate = false;
    private int ticksAlive = 0;
    private boolean playSwingAnimation = true;
    private BlockHitResult blockHitResult;
    private float damageAmount;
    private Player owner;
    private final RawAnimation animationBuilder = RawAnimation.begin().thenPlay("hammer_swing");
    private final AnimationController animationController = new AnimationController((GeoAnimatable)this, "controller", 0, this::predicate);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache((GeoAnimatable)this);

    public SpectralHammer(EntityType<? extends SpectralHammer> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
        this.setInvulnerable(true);
    }

    public SpectralHammer(Level levelIn, LivingEntity owner, BlockHitResult blockHitResult, int depth, int radius) {
        this((EntityType<? extends SpectralHammer>)((EntityType)EntityRegistry.SPECTRAL_HAMMER.get()), levelIn);
        if (owner instanceof Player) {
            Player player;
            this.owner = player = (Player)owner;
        }
        this.blockHitResult = blockHitResult;
        this.depth = depth;
        this.radius = radius;
        int xRot = blockHitResult.getDirection().getAxis().isVertical() ? 90 : 0;
        float yRot = owner.getYRot();
        float yHeadRot = owner.getYHeadRot();
        this.setYRot(yRot);
        this.setXRot(xRot);
        this.setYBodyRot(yRot);
        this.setYHeadRot(yHeadRot);
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    public void tick() {
        if (++this.ticksAlive >= 30) {
            this.discard();
        }
        if (this.ticksAlive >= 20 && !this.didAnimate) {
            this.didAnimate = true;
        }
        if (this.ticksAlive == 11 && !this.didDamage) {
            Vec3 location = this.position();
            this.level.playSound(null, location.x, location.y, location.z, (SoundEvent)SoundRegistry.FORCE_IMPACT.get(), SoundSource.NEUTRAL, 2.0f, (float)this.random.nextIntBetweenInclusive(6, 8) * 0.1f);
            this.level.playSound(null, location.x, location.y, location.z, SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.NEUTRAL, 1.0f, (float)this.random.nextIntBetweenInclusive(6, 8) * 0.1f);
        }
        if (this.ticksAlive >= 13 && !this.didDamage) {
            BlockPos blockPos;
            BlockState blockState;
            if (this.blockHitResult != null && this.blockHitResult.getType() != HitResult.Type.MISS && (blockState = this.level.getBlockState(blockPos = this.blockHitResult.getBlockPos())).is(ModTags.SPECTRAL_HAMMER_MINEABLE)) {
                BlockCollectorHelper blockCollector = this.getBlockCollector(blockPos, this.blockHitResult.getDirection(), this.radius, this.depth, new HashSet<BlockPos>(), new HashSet<BlockPos>());
                this.collectBlocks(blockPos, blockCollector);
                if (!blockCollector.blocksToRemove.isEmpty()) {
                    RandomSource random = Utils.random;
                    AtomicInteger count = new AtomicInteger();
                    int maxPossibleStacks = this.radius * 2 * (1 + this.radius * 2) * (this.depth + 1);
                    SimpleContainer drops = new SimpleContainer(maxPossibleStacks);
                    blockCollector.blocksToRemove.forEach(pos -> {
                        int distance = blockCollector.origin.distManhattan((Vec3i)pos);
                        float missChance = random.nextFloat() * 20.0f;
                        float pct = (float)(distance * distance) / (100.0f * (float)this.radius);
                        BlockState blockstate = this.level.getBlockState(pos);
                        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(this.level, pos, blockstate, this.owner);
                        NeoForge.EVENT_BUS.post((Event)event);
                        if (!event.isCanceled()) {
                            boolean spawnFallingBlock;
                            boolean bl = spawnFallingBlock = missChance < pct;
                            if (spawnFallingBlock && !this.level.isClientSide) {
                                MagicManager.spawnParticles(this.level, new FallingBlockParticleOption(blockstate), pos.getX(), pos.getY(), pos.getZ(), 1, 0.0, 0.0, 0.0, 0.0, true);
                            }
                            if (count.incrementAndGet() % 5 == 0 && !spawnFallingBlock) {
                                this.level.destroyBlock(pos, false);
                            } else {
                                this.level.removeBlock(pos, false);
                            }
                            SpectralHammer.dropResources(blockstate, this.level, pos).forEach(arg_0 -> ((SimpleContainer)drops).addItem(arg_0));
                        }
                    });
                    Containers.dropContents((Level)this.level, (BlockPos)this.blockPosition(), (Container)drops);
                }
            }
            this.didDamage = true;
        }
        super.tick();
    }

    public static List<ItemStack> dropResources(BlockState pState, Level pLevel, BlockPos pos) {
        List<Object> drops = new ArrayList<ItemStack>();
        if (pLevel instanceof ServerLevel) {
            drops = Block.getDrops((BlockState)pState, (ServerLevel)((ServerLevel)pLevel), (BlockPos)pos, null);
            pState.spawnAfterBreak((ServerLevel)pLevel, pos, ItemStack.EMPTY, true);
        }
        return drops;
    }

    private void collectBlocks(BlockPos blockPos, BlockCollectorHelper bch) {
        Stack<BlockPos> stack = new Stack<BlockPos>();
        stack.push(blockPos);
        while (!stack.isEmpty()) {
            BlockPos currentPos = (BlockPos)stack.pop();
            if (bch.blocksChecked.contains(currentPos) || bch.blocksToRemove.contains(currentPos)) continue;
            if (bch.isValidBlockToCollect(this.level, currentPos)) {
                bch.blocksToRemove.add(currentPos);
                BlockPos tmpPos = currentPos.above();
                if (!bch.blocksChecked.contains(tmpPos) && !bch.blocksToRemove.contains(tmpPos)) {
                    stack.push(tmpPos);
                }
                if (!bch.blocksChecked.contains(tmpPos = currentPos.below()) && !bch.blocksToRemove.contains(tmpPos)) {
                    stack.push(tmpPos);
                }
                if (!bch.blocksChecked.contains(tmpPos = currentPos.north()) && !bch.blocksToRemove.contains(tmpPos)) {
                    stack.push(tmpPos);
                }
                if (!bch.blocksChecked.contains(tmpPos = currentPos.south()) && !bch.blocksToRemove.contains(tmpPos)) {
                    stack.push(tmpPos);
                }
                if (!bch.blocksChecked.contains(tmpPos = currentPos.east()) && !bch.blocksToRemove.contains(tmpPos)) {
                    stack.push(tmpPos);
                }
                if (bch.blocksChecked.contains(tmpPos = currentPos.west()) || bch.blocksToRemove.contains(tmpPos)) continue;
                stack.push(tmpPos);
                continue;
            }
            bch.blocksChecked.add(currentPos);
        }
    }

    private BlockCollectorHelper getBlockCollector(BlockPos origin, Direction direction, int radius, int depth, Set<BlockPos> blocksToRemove, Set<BlockPos> blocksChecked) {
        int minX = origin.getX() - radius;
        int maxX = origin.getX() + radius;
        int minY = origin.getY() - radius;
        int maxY = origin.getY() + radius;
        int minZ = origin.getZ() - radius;
        int maxZ = origin.getZ() + radius;
        switch (direction) {
            case WEST: {
                minX = origin.getX();
                maxX = origin.getX() + depth;
                break;
            }
            case EAST: {
                minX = origin.getX() - depth;
                maxX = origin.getX();
                break;
            }
            case SOUTH: {
                minZ = origin.getZ() - depth;
                maxZ = origin.getZ();
                break;
            }
            case NORTH: {
                minZ = origin.getZ();
                maxZ = origin.getZ() + depth;
                break;
            }
            case UP: {
                minY = origin.getY() - depth;
                maxY = origin.getY();
                break;
            }
            case DOWN: {
                minY = origin.getY();
                maxY = origin.getY() + depth;
            }
        }
        return new BlockCollectorHelper(origin, direction, radius, depth, minX, maxX, minY, maxY, minZ, maxZ, blocksToRemove, blocksChecked);
    }

    public boolean isPushable() {
        return false;
    }

    public boolean isInvulnerable() {
        return true;
    }

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return pDimensions.height() * 0.6f;
    }

    public boolean isNoGravity() {
        return true;
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes();
    }

    public Iterable<ItemStack> getArmorSlots() {
        return Collections.singleton(ItemStack.EMPTY);
    }

    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
    }

    public HumanoidArm getMainArm() {
        return HumanoidArm.LEFT;
    }

    private PlayState predicate(AnimationState event) {
        if (event.getController().getAnimationState() == AnimationController.State.STOPPED && this.playSwingAnimation) {
            event.getController().setAnimation(this.animationBuilder);
            this.playSwingAnimation = false;
        }
        return PlayState.CONTINUE;
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(this.animationController);
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private record BlockCollectorHelper(BlockPos origin, Direction originVector, int radius, int depth, int minX, int maxX, int minY, int maxY, int minZ, int maxZ, Set<BlockPos> blocksToRemove, Set<BlockPos> blocksChecked) {
        public boolean isValidBlockToCollect(Level level, BlockPos bp) {
            return level.getBlockState(bp).is(ModTags.SPECTRAL_HAMMER_MINEABLE) && bp.getX() >= this.minX && bp.getX() <= this.maxX && bp.getY() >= this.minY && bp.getY() <= this.maxY && bp.getZ() >= this.minZ && bp.getZ() <= this.maxZ;
        }
    }
}

