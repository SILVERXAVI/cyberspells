/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Holder
 *  net.minecraft.core.Holder$Reference
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.NonNullList
 *  net.minecraft.core.Position
 *  net.minecraft.core.Registry
 *  net.minecraft.core.RegistryAccess
 *  net.minecraft.core.Vec3i
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.Difficulty
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.NeutralMob
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.monster.Enemy
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ArmorItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.SwordItem
 *  net.minecraft.world.item.alchemy.Potion
 *  net.minecraft.world.item.alchemy.PotionContents
 *  net.minecraft.world.item.enchantment.ConditionalEffect
 *  net.minecraft.world.item.enchantment.Enchantment
 *  net.minecraft.world.item.enchantment.EnchantmentEffectComponents
 *  net.minecraft.world.item.enchantment.EnchantmentHelper
 *  net.minecraft.world.item.enchantment.Enchantments
 *  net.minecraft.world.item.enchantment.ItemEnchantments
 *  net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect
 *  net.minecraft.world.level.BlockCollisions
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.CollisionGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec2
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.scores.Team
 *  net.neoforged.bus.api.Event
 *  net.neoforged.neoforge.common.NeoForge
 *  net.neoforged.neoforge.common.Tags$Biomes
 *  net.neoforged.neoforge.entity.PartEntity
 *  net.neoforged.neoforge.event.EventHooks
 *  net.neoforged.neoforge.network.PacketDistributor
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.joml.Quaternionf
 *  org.joml.Vector3f
 *  org.joml.Vector3fc
 *  top.theillusivec4.curios.api.CuriosApi
 *  top.theillusivec4.curios.api.SlotContext
 *  top.theillusivec4.curios.api.SlotResult
 */
package io.redspace.ironsspellbooks.api.util;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.attribute.IMagicAttribute;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.events.SpellTeleportEvent;
import io.redspace.ironsspellbooks.api.item.UpgradeData;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainerMutable;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.root.PreventDismount;
import io.redspace.ironsspellbooks.entity.spells.shield.ShieldEntity;
import io.redspace.ironsspellbooks.item.CastingItem;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.UniqueItem;
import io.redspace.ironsspellbooks.network.casting.CancelCastPacket;
import io.redspace.ironsspellbooks.network.casting.SyncTargetingDataPacket;
import io.redspace.ironsspellbooks.particle.FallingBlockParticleOption;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Position;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.scores.Team;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;

public class Utils {
    public static final RandomSource random = RandomSource.createThreadSafe();
    public static final Predicate<Holder<Attribute>> ONLY_MAGIC_ATTRIBUTES = attribute -> attribute.value() instanceof IMagicAttribute;
    public static final Predicate<Holder<Attribute>> NON_BASE_ATTRIBUTES = attribute -> attribute != Attributes.ENTITY_INTERACTION_RANGE && attribute != Attributes.ATTACK_DAMAGE && attribute != Attributes.ATTACK_SPEED && attribute != Attributes.ATTACK_KNOCKBACK;

    public static long getServerTick() {
        return IronsSpellbooks.OVERWORLD.getGameTime();
    }

    public static String getStackTraceAsString() {
        Stream<StackTraceElement> trace = Arrays.stream(Thread.currentThread().getStackTrace());
        StringBuffer sb = new StringBuffer();
        trace.forEach(item -> {
            sb.append(item.toString());
            sb.append("\n");
        });
        return sb.toString();
    }

    public static void spawnInWorld(Level level, BlockPos pos, ItemStack remaining) {
        if (!remaining.isEmpty()) {
            ItemEntity itemEntity = new ItemEntity(level, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, remaining);
            itemEntity.setPickUpDelay(40);
            itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().multiply(0.0, 1.0, 0.0));
            level.addFreshEntity((Entity)itemEntity);
        }
    }

    public static boolean canBeUpgraded(ItemStack stack) {
        Item item = stack.getItem();
        boolean isUpgradeable = stack.is(ModTags.CAN_BE_UPGRADED);
        return !ServerConfigs.UPGRADE_BLACKLIST_ITEMS.contains(item) && (stack.getItem() instanceof SpellBook || stack.getItem() instanceof ArmorItem || stack.getItem() instanceof CastingItem || ServerConfigs.UPGRADE_WHITELIST_ITEMS.contains(item) || isUpgradeable);
    }

    public static String timeFromTicks(float ticks, int decimalPlaces) {
        float ticks_to_seconds = 20.0f;
        float seconds_to_minutes = 60.0f;
        String affix = "s";
        float time = ticks / ticks_to_seconds;
        if (time > seconds_to_minutes) {
            time /= seconds_to_minutes;
            affix = "m";
        }
        return Utils.stringTruncation(time, decimalPlaces) + affix;
    }

    public static boolean handleSpellTeleport(AbstractSpell spell, Entity entity, Vec3 destination) {
        SpellTeleportEvent event = new SpellTeleportEvent(spell, entity, destination.x, destination.y, destination.z);
        NeoForge.EVENT_BUS.post((Event)event);
        boolean canceled = event.isCanceled();
        if (!canceled) {
            entity.teleportTo(event.getTargetX(), event.getTargetY(), event.getTargetZ());
        }
        return canceled;
    }

    public static double softCapFormula(double x) {
        return x <= 1.5 ? x : -0.25 * (1.0 / (x - 1.0)) + 2.0;
    }

    @Nullable
    public static ItemStack getPlayerSpellbookStack(@NotNull Player player) {
        return CuriosApi.getCuriosInventory((LivingEntity)player).flatMap(curios -> curios.findCurio(Curios.SPELLBOOK_SLOT, 0).map(SlotResult::stack)).orElse(null);
    }

    public static void setPlayerSpellbookStack(@NotNull Player player, ItemStack itemStack) {
        CuriosApi.getCuriosInventory((LivingEntity)player).ifPresent(curios -> curios.setEquippedCurio(Curios.SPELLBOOK_SLOT, 0, itemStack));
    }

    public static String stringTruncation(double f, int decimalPlaces) {
        if (f == Math.floor(f)) {
            return Integer.toString((int)f);
        }
        double multiplier = Math.pow(10.0, decimalPlaces);
        double truncatedValue = Math.floor(f * multiplier) / multiplier;
        String result = Double.toString(truncatedValue);
        result = (result = result.replaceAll("0*$", "")).endsWith(".") ? result.substring(0, result.length() - 1) : result;
        return result;
    }

    public static float intPow(float f, int exponent) {
        if (exponent == 0) {
            return 1.0f;
        }
        float b = f;
        for (int i = 1; i < Math.abs(exponent); ++i) {
            b *= f;
        }
        return exponent < 0 ? 1.0f / b : b;
    }

    public static double intPow(double d, int exponent) {
        if (exponent == 0) {
            return 1.0;
        }
        double b = d;
        for (int i = 1; i < Math.abs(exponent); ++i) {
            b *= d;
        }
        return exponent < 0 ? 1.0 / b : b;
    }

    public static float getAngle(Vec2 a, Vec2 b) {
        return Utils.getAngle(a.x, a.y, b.x, b.y);
    }

    public static float getAngle(double ax, double ay, double bx, double by) {
        return (float)Math.atan2(by - ay, bx - ax) + 3.141f;
    }

    public static BlockHitResult getTargetOld(Level level, Player player, ClipContext.Fluid clipContext, double reach) {
        float f = player.getXRot();
        float f1 = player.getYRot();
        Vec3 vec3 = player.getEyePosition();
        float f2 = Mth.cos((float)(-f1 * ((float)Math.PI / 180) - (float)Math.PI));
        float f3 = Mth.sin((float)(-f1 * ((float)Math.PI / 180) - (float)Math.PI));
        float f4 = -Mth.cos((float)(-f * ((float)Math.PI / 180)));
        float f5 = Mth.sin((float)(-f * ((float)Math.PI / 180)));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        Vec3 vec31 = vec3.add((double)f6 * reach, (double)f5 * reach, (double)f7 * reach);
        return level.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, clipContext, (Entity)player));
    }

    public static BlockHitResult getTargetBlock(Level level, LivingEntity entity, ClipContext.Fluid clipContext, double reach) {
        Vec3 rotation = entity.getLookAngle().normalize().scale(reach);
        Vec3 pos = entity.getEyePosition();
        Vec3 dest = rotation.add(pos);
        return level.clip(new ClipContext(pos, dest, ClipContext.Block.COLLIDER, clipContext, (Entity)entity));
    }

    public static boolean hasLineOfSight(Level level, Vec3 start, Vec3 end, boolean checkForShields) {
        HitResult shieldImpact;
        List shieldEntities;
        if (checkForShields && (shieldEntities = level.getEntitiesOfClass(ShieldEntity.class, new AABB(start, end))).size() > 0 && (shieldImpact = Utils.checkEntityIntersecting((Entity)shieldEntities.get(0), start, end, 0.0f)).getType() != HitResult.Type.MISS) {
            end = shieldImpact.getLocation();
        }
        return level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty())).getType() == HitResult.Type.MISS;
    }

    public static boolean hasLineOfSight(Level level, Entity entity1, Entity entity2, boolean checkForShields) {
        return Utils.hasLineOfSight(level, entity1.getEyePosition(), entity2.getBoundingBox().getCenter(), checkForShields);
    }

    public static BlockHitResult raycastForBlock(Level level, Vec3 start, Vec3 end, ClipContext.Fluid clipContext) {
        return level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, clipContext, CollisionContext.empty()));
    }

    public static HitResult checkEntityIntersecting(Entity entity, Vec3 start, Vec3 end, float bbInflation) {
        Vec3 hitPos = null;
        if (entity.isMultipartEntity()) {
            for (PartEntity p : entity.getParts()) {
                Vec3 hit = p.getBoundingBox().inflate((double)bbInflation).clip(start, end).orElse(null);
                if (hit == null) continue;
                hitPos = hit;
                break;
            }
        } else {
            hitPos = entity.getBoundingBox().inflate((double)bbInflation).clip(start, end).orElse(null);
        }
        if (hitPos != null) {
            return new EntityHitResult(entity, hitPos);
        }
        return BlockHitResult.miss((Vec3)end, (Direction)Direction.UP, (BlockPos)BlockPos.containing((Position)end));
    }

    public static Vec3 getPositionFromEntityLookDirection(Entity originEntity, float distance) {
        Vec3 start = originEntity.getEyePosition();
        return originEntity.getLookAngle().normalize().scale((double)distance).add(start);
    }

    public static HitResult raycastForEntity(Level level, Entity originEntity, float distance, boolean checkForBlocks) {
        Vec3 start = originEntity.getEyePosition();
        Vec3 end = originEntity.getLookAngle().normalize().scale((double)distance).add(start);
        return Utils.raycastForEntity(level, originEntity, start, end, checkForBlocks);
    }

    public static HitResult raycastForEntity(Level level, Entity originEntity, float distance, boolean checkForBlocks, float bbInflation) {
        Vec3 start = originEntity.getEyePosition();
        Vec3 end = originEntity.getLookAngle().normalize().scale((double)distance).add(start);
        return Utils.internalRaycastForEntity(level, originEntity, start, end, checkForBlocks, bbInflation, Utils::canHitWithRaycast);
    }

    public static HitResult raycastForEntity(Level level, Entity originEntity, Vec3 start, Vec3 end, boolean checkForBlocks) {
        return Utils.internalRaycastForEntity(level, originEntity, start, end, checkForBlocks, 0.0f, Utils::canHitWithRaycast);
    }

    public static HitResult raycastForEntity(Level level, Entity originEntity, Vec3 start, Vec3 end, boolean checkForBlocks, float bbInflation, Predicate<? super Entity> filter) {
        return Utils.internalRaycastForEntity(level, originEntity, start, end, checkForBlocks, bbInflation, filter);
    }

    public static HitResult raycastForEntityOfClass(Level level, Entity originEntity, Vec3 start, Vec3 end, boolean checkForBlocks, Class<? extends Entity> c) {
        return Utils.internalRaycastForEntity(level, originEntity, start, end, checkForBlocks, 0.0f, entity -> entity.getClass() == c);
    }

    public static void releaseUsingHelper(LivingEntity entity, ItemStack itemStack, int ticksUsed) {
        ServerPlayer serverPlayer;
        MagicData pmd;
        if (entity instanceof ServerPlayer && (pmd = MagicData.getPlayerMagicData((LivingEntity)(serverPlayer = (ServerPlayer)entity))).isCasting()) {
            Utils.serverSideCancelCast(serverPlayer);
            serverPlayer.stopUsingItem();
        }
    }

    public static boolean serverSideInitiateCast(ServerPlayer serverPlayer) {
        SpellSelectionManager ssm = new SpellSelectionManager((Player)serverPlayer);
        SpellSelectionManager.SelectionOption spellItem = ssm.getSelection();
        if (spellItem != null) {
            SpellData spellData = ssm.getSelectedSpellData();
            if (spellData != SpellData.EMPTY) {
                MagicData playerMagicData = MagicData.getPlayerMagicData((LivingEntity)serverPlayer);
                if (playerMagicData.isCasting() && !playerMagicData.getCastingSpellId().equals(spellData.getSpell().getSpellId())) {
                    CancelCastPacket.cancelCast(serverPlayer, playerMagicData.getCastType() != CastType.LONG);
                }
                return spellData.getSpell().attemptInitiateCast(ItemStack.EMPTY, spellData.getSpell().getLevelFor(spellData.getLevel(), (LivingEntity)serverPlayer), serverPlayer.level, (Player)serverPlayer, spellItem.getCastSource(), true, spellItem.slot);
            }
        } else if (Utils.getPlayerSpellbookStack((Player)serverPlayer) == null) {
            Item item;
            ItemStack heldSpellbookStack = serverPlayer.getMainHandItem();
            if (!(heldSpellbookStack.getItem() instanceof SpellBook)) {
                heldSpellbookStack = serverPlayer.getOffhandItem();
            }
            if ((item = heldSpellbookStack.getItem()) instanceof SpellBook) {
                SpellBook spellBook = (SpellBook)item;
                spellBook.onEquipFromUse(new SlotContext(Curios.SPELLBOOK_SLOT, (LivingEntity)serverPlayer, 0, false, true), heldSpellbookStack);
                Utils.setPlayerSpellbookStack((Player)serverPlayer, heldSpellbookStack.split(1));
            }
        }
        return false;
    }

    public static double signedMin(double a, double b) {
        return (double)(a < 0.0 ? -1 : 1) * Math.min(Math.abs(a), Math.abs(b));
    }

    public static boolean serverSideInitiateQuickCast(ServerPlayer serverPlayer, int slot) {
        SpellData spellData;
        SpellSelectionManager.SelectionOption spellSelection = new SpellSelectionManager((Player)serverPlayer).getSpellSlot(slot);
        if (spellSelection != null && (spellData = spellSelection.spellData) != SpellData.EMPTY) {
            MagicData playerMagicData = MagicData.getPlayerMagicData((LivingEntity)serverPlayer);
            if (playerMagicData.isCasting() && !playerMagicData.getCastingSpellId().equals(spellData.getSpell().getSpellId())) {
                CancelCastPacket.cancelCast(serverPlayer, playerMagicData.getCastType() != CastType.LONG);
            }
            return spellData.getSpell().attemptInitiateCast(ItemStack.EMPTY, spellData.getSpell().getLevelFor(spellData.getLevel(), (LivingEntity)serverPlayer), serverPlayer.level, (Player)serverPlayer, spellSelection.getCastSource(), true, spellSelection.slot);
        }
        return false;
    }

    private static HitResult internalRaycastForEntity(Level level, Entity originEntity, Vec3 start, Vec3 end, boolean checkForBlocks, float bbInflation, Predicate<? super Entity> filter) {
        BlockHitResult blockHitResult = null;
        if (checkForBlocks) {
            blockHitResult = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, originEntity));
            end = blockHitResult.getLocation();
        }
        AABB range = originEntity.getBoundingBox().expandTowards(end.subtract(start));
        ArrayList<HitResult> hits = new ArrayList<HitResult>();
        List entities = level.getEntities(originEntity, range, filter);
        for (Entity target : entities) {
            HitResult hit = Utils.checkEntityIntersecting(target, start, end, bbInflation);
            if (hit.getType() == HitResult.Type.MISS) continue;
            hits.add(hit);
        }
        if (!hits.isEmpty()) {
            hits.sort(Comparator.comparingDouble(o -> o.getLocation().distanceToSqr(start)));
            return (HitResult)hits.get(0);
        }
        if (checkForBlocks) {
            return blockHitResult;
        }
        return BlockHitResult.miss((Vec3)end, (Direction)Direction.UP, (BlockPos)BlockPos.containing((Position)end));
    }

    public static void serverSideCancelCast(ServerPlayer serverPlayer) {
        CancelCastPacket.cancelCast(serverPlayer, MagicData.getPlayerMagicData((LivingEntity)serverPlayer).getCastingSpell().getSpell().getCastType() == CastType.CONTINUOUS);
    }

    public static void serverSideCancelCast(ServerPlayer serverPlayer, boolean triggerCooldown) {
        CancelCastPacket.cancelCast(serverPlayer, triggerCooldown);
    }

    public static float smoothstep(float a, float b, float x) {
        x = 6.0f * (x * x * x * x * x) - 15.0f * (x * x * x * x) + 10.0f * (x * x * x);
        return a + (b - a) * x;
    }

    public static boolean canHitWithRaycast(Entity entity) {
        return entity.isPickable() && entity.isAlive() && !entity.isSpectator();
    }

    public static int applyCooldownReduction(int baseTicks, @Nullable LivingEntity livingEntity) {
        double modifier = livingEntity == null ? 1.0 : livingEntity.getAttributeValue(AttributeRegistry.COOLDOWN_REDUCTION);
        return (int)((double)baseTicks * (2.0 - Utils.softCapFormula(modifier)));
    }

    public static Vec2 rotationFromDirection(Vec3 vector) {
        float pitch = (float)Math.asin(vector.y);
        float yaw = (float)Math.atan2(vector.x, vector.z);
        return new Vec2(pitch, yaw);
    }

    public static boolean doMeleeAttack(Mob attacker, Entity target, DamageSource damageSource) {
        boolean flag;
        if (attacker.level.isClientSide) {
            return false;
        }
        float f = (float)attacker.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float f1 = (float)attacker.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        if (target instanceof LivingEntity) {
            f = EnchantmentHelper.modifyDamage((ServerLevel)((ServerLevel)attacker.level), (ItemStack)attacker.getMainHandItem(), (Entity)((LivingEntity)target), (DamageSource)damageSource, (float)f);
            f1 = EnchantmentHelper.modifyKnockback((ServerLevel)((ServerLevel)attacker.level), (ItemStack)attacker.getMainHandItem(), (Entity)((LivingEntity)target), (DamageSource)damageSource, (float)f1);
        }
        if (flag = DamageSources.applyDamage(target, f, damageSource)) {
            if (f1 > 0.0f && target instanceof LivingEntity) {
                LivingEntity livingTarget = (LivingEntity)target;
                ((LivingEntity)target).knockback((double)(f1 * 0.5f), (double)Mth.sin((float)(attacker.getYRot() * ((float)Math.PI / 180))), (double)(-Mth.cos((float)(attacker.getYRot() * ((float)Math.PI / 180)))));
                attacker.setDeltaMovement(attacker.getDeltaMovement().multiply(0.6, 1.0, 0.6));
                livingTarget.setLastHurtByMob((LivingEntity)attacker);
            }
            EnchantmentHelper.doPostAttackEffects((ServerLevel)((ServerLevel)attacker.level), (Entity)attacker, (DamageSource)damageSource);
            attacker.setLastHurtMob(target);
        }
        return flag;
    }

    public static double getRandomScaled(double scale) {
        return (2.0 * Math.random() - 1.0) * scale;
    }

    public static Vec3 getRandomVec3(double scale) {
        return new Vec3(Utils.getRandomScaled(scale), Utils.getRandomScaled(scale), Utils.getRandomScaled(scale));
    }

    public static Vector3f getRandomVec3f(double scale) {
        return new Vector3f((float)Utils.getRandomScaled(scale), (float)Utils.getRandomScaled(scale), (float)Utils.getRandomScaled(scale));
    }

    public static Vector3f v3f(Vec3 vec3) {
        return new Vector3f((float)vec3.x, (float)vec3.y, (float)vec3.z);
    }

    public static Vec3 v3d(Vector3f vec3) {
        return new Vec3((double)vec3.x, (double)vec3.y, (double)vec3.z);
    }

    public static Vec3 lerp(float f, Vec3 a, Vec3 b) {
        return a.add(b.subtract(a).scale((double)f));
    }

    @Deprecated(forRemoval=true)
    public static boolean shouldHealEntity(LivingEntity healer, LivingEntity target) {
        return Utils.shouldHealEntity((Entity)healer, (Entity)target);
    }

    public static boolean shouldHealEntity(Entity healer, Entity target) {
        if (healer instanceof NeutralMob) {
            LivingEntity livingEntity;
            NeutralMob neutralMob = (NeutralMob)healer;
            if (target instanceof LivingEntity && neutralMob.isAngryAt(livingEntity = (LivingEntity)target)) {
                return false;
            }
        }
        if (healer == target) {
            return true;
        }
        if (target.getType().is(ModTags.ALWAYS_HEAL) && !(healer instanceof Enemy)) {
            return true;
        }
        if (target.isAlliedTo(healer) || healer.isAlliedTo(target)) {
            return true;
        }
        if (healer.getTeam() != null) {
            return target.isAlliedTo((Team)healer.getTeam());
        }
        if (healer instanceof Player) {
            return target instanceof Player;
        }
        return healer.getType().getCategory() == target.getType().getCategory() && healer instanceof Enemy ^ target instanceof Enemy;
    }

    public static boolean canImbue(ItemStack itemStack) {
        if (itemStack.getItem() instanceof UniqueItem) {
            return false;
        }
        Item item = itemStack.getItem();
        if (ServerConfigs.IMBUE_BLACKLIST_ITEMS.contains(item)) {
            return false;
        }
        if (ServerConfigs.IMBUE_WHITELIST_ITEMS.contains(item)) {
            return true;
        }
        if (itemStack.getItem() instanceof SwordItem) {
            return true;
        }
        if (ISpellContainer.isSpellContainer(itemStack) && !(itemStack.getItem() instanceof Scroll) && !(itemStack.getItem() instanceof SpellBook)) {
            return true;
        }
        return itemStack.is(ModTags.CAN_BE_IMBUED);
    }

    public static ItemStack handleShriving(ItemStack baseStack) {
        ItemStack result = baseStack.copy();
        if (result.is((Item)ItemRegistry.SCROLL.get())) {
            return ItemStack.EMPTY;
        }
        boolean hasResult = false;
        if (ISpellContainer.isSpellContainer(result) && !(result.getItem() instanceof SpellBook) && !(result.getItem() instanceof UniqueItem)) {
            if (result.getItem() instanceof IPresetSpellContainer) {
                ISpellContainerMutable spellContainer = ISpellContainer.get(result).mutableCopy();
                spellContainer.getActiveSpells().forEach(spellData -> spellContainer.removeSpell(spellData.getSpell()));
                ISpellContainer.set(result, spellContainer.toImmutable());
            } else {
                ISpellContainer.remove(result);
            }
            hasResult = true;
        }
        if (UpgradeData.hasUpgradeData(result)) {
            UpgradeData.removeUpgradeData(result);
            hasResult = true;
        }
        if (hasResult) {
            return result;
        }
        return ItemStack.EMPTY;
    }

    public static boolean validAntiMagicTarget(Entity entity) {
        return !entity.isSpectator() && (entity instanceof AntiMagicSusceptible || entity instanceof Player || entity instanceof IMagicEntity);
    }

    public static float findRelativeGroundLevel(Level level, Vec3 start, int maxSteps) {
        if (level.getBlockState(BlockPos.containing((Position)start)).isSuffocating((BlockGetter)level, BlockPos.containing((Position)start))) {
            for (int i = 0; i < maxSteps; ++i) {
                BlockPos pos = BlockPos.containing((Position)(start = start.add(0.0, 1.0, 0.0)));
                if (level.getBlockState(pos).isSuffocating((BlockGetter)level, pos)) continue;
                return pos.getY();
            }
        }
        return (float)level.clip((ClipContext)new ClipContext((Vec3)start, (Vec3)start.add((double)0.0, (double)((double)(-maxSteps)), (double)0.0), (ClipContext.Block)ClipContext.Block.COLLIDER, (ClipContext.Fluid)ClipContext.Fluid.NONE, (CollisionContext)CollisionContext.empty())).getLocation().y;
    }

    public static Vec3 moveToRelativeGroundLevel(Level level, Vec3 start, int maxSteps) {
        return Utils.moveToRelativeGroundLevel(level, start, maxSteps, maxSteps);
    }

    public static Vec3 moveToRelativeGroundLevel(Level level, Vec3 start, int maxStepsUp, int maxStepsDown) {
        BlockCollisions blockcollisions = new BlockCollisions((CollisionGetter)level, null, new AABB(0.0, 0.0, 0.0, 0.5, 0.5, 0.5).move(start), true, (p_286215_, p_286216_) -> p_286216_);
        if (blockcollisions.hasNext()) {
            for (int i = 1; i < maxStepsUp; ++i) {
                blockcollisions = new BlockCollisions((CollisionGetter)level, null, new AABB(0.0, 0.0, 0.0, 0.5, 0.5, 0.5).move(start.add(0.0, (double)i, 0.0)), true, (p_286215_, p_286216_) -> p_286216_);
                if (blockcollisions.hasNext()) continue;
                start = start.add(0.0, (double)i, 0.0);
                break;
            }
        }
        return level.clip(new ClipContext(start, start.add(0.0, (double)(-maxStepsDown), 0.0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty())).getLocation();
    }

    public static boolean checkMonsterSpawnRules(ServerLevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return !pLevel.getBiome(pPos).is(Tags.Biomes.NO_DEFAULT_MONSTERS) && pLevel.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn((ServerLevelAccessor)pLevel, (BlockPos)pPos, (RandomSource)pRandom) && Monster.checkMobSpawnRules((EntityType)((EntityType)EntityRegistry.NECROMANCER.get()), (LevelAccessor)pLevel, (MobSpawnType)pSpawnType, (BlockPos)pPos, (RandomSource)pRandom);
    }

    public static void sendTargetedNotification(ServerPlayer target, LivingEntity caster, AbstractSpell spell) {
        target.connection.send((Packet)new ClientboundSetActionBarTextPacket((Component)Component.translatable((String)"ui.irons_spellbooks.spell_target_warning", (Object[])new Object[]{caster.getDisplayName().getString(), spell.getDisplayName((Player)target)}).withStyle(ChatFormatting.LIGHT_PURPLE)));
    }

    public static boolean preCastTargetHelper(Level level, LivingEntity caster, MagicData playerMagicData, AbstractSpell spell, int range, float aimAssist) {
        return Utils.preCastTargetHelper(level, caster, playerMagicData, spell, range, aimAssist, true);
    }

    public static boolean preCastTargetHelper(Level level, LivingEntity caster, MagicData playerMagicData, AbstractSpell spell, int range, float aimAssist, boolean sendFailureMessage) {
        return Utils.preCastTargetHelper(level, caster, playerMagicData, spell, range, aimAssist, sendFailureMessage, x -> true);
    }

    public static boolean preCastTargetHelper(Level level, LivingEntity caster, MagicData playerMagicData, AbstractSpell spell, int range, float aimAssist, boolean sendFailureMessage, Predicate<LivingEntity> filter) {
        HitResult target = Utils.raycastForEntity(caster.level, (Entity)caster, range, true, aimAssist);
        LivingEntity livingTarget = null;
        if (target instanceof EntityHitResult) {
            LivingEntity livingEntity;
            EntityHitResult entityHit = (EntityHitResult)target;
            Entity entity = entityHit.getEntity();
            if (entity instanceof LivingEntity && filter.test(livingEntity = (LivingEntity)entity)) {
                livingTarget = livingEntity;
            } else {
                Entity entity2;
                LivingEntity livingParent;
                PartEntity partEntity;
                entity = entityHit.getEntity();
                if (entity instanceof PartEntity && (entity = (partEntity = (PartEntity)entity).getParent()) instanceof LivingEntity && !caster.equals((Object)(livingParent = (LivingEntity)entity)) && filter.test(livingParent)) {
                    livingTarget = livingParent;
                } else if (entityHit.getEntity() instanceof PreventDismount && (entity2 = entityHit.getEntity().getFirstPassenger()) instanceof LivingEntity) {
                    LivingEntity livingRooted;
                    livingTarget = livingRooted = (LivingEntity)entity2;
                }
            }
        }
        if (livingTarget != null) {
            ServerPlayer serverPlayer;
            playerMagicData.setAdditionalCastData(new TargetEntityCastData(livingTarget));
            if (caster instanceof ServerPlayer) {
                serverPlayer = (ServerPlayer)caster;
                if (spell.getCastType() != CastType.INSTANT) {
                    PacketDistributor.sendToPlayer((ServerPlayer)serverPlayer, (CustomPacketPayload)new SyncTargetingDataPacket(livingTarget, spell), (CustomPacketPayload[])new CustomPacketPayload[0]);
                }
                serverPlayer.connection.send((Packet)new ClientboundSetActionBarTextPacket((Component)Component.translatable((String)"ui.irons_spellbooks.spell_target_success", (Object[])new Object[]{livingTarget.getDisplayName().getString(), spell.getDisplayName((Player)serverPlayer)}).withStyle(ChatFormatting.GREEN)));
            }
            if (livingTarget instanceof ServerPlayer) {
                serverPlayer = (ServerPlayer)livingTarget;
                Utils.sendTargetedNotification(serverPlayer, caster, spell);
            }
            return true;
        }
        if (sendFailureMessage && caster instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)caster;
            serverPlayer.connection.send((Packet)new ClientboundSetActionBarTextPacket((Component)Component.translatable((String)"ui.irons_spellbooks.cast_error_target").withStyle(ChatFormatting.RED)));
        }
        return false;
    }

    public static void doMobBreakSuffocatingBlocks(LivingEntity entity) {
        Utils.doMobBreakSuffocatingBlocks(entity, Vec3.ZERO);
    }

    public static void doMobBreakSuffocatingBlocks(LivingEntity entity, Vec3 offset) {
        if (EventHooks.canEntityGrief((Level)entity.level, (Entity)entity)) {
            int l = Mth.floor((float)(entity.getBbWidth() / 2.0f + 1.0f));
            int i1 = Mth.ceil((float)entity.getBbHeight());
            Vec3i o = new Vec3i(Math.round((float)offset.x), Math.round((float)offset.y), Math.round((float)offset.z));
            for (BlockPos blockpos : BlockPos.betweenClosed((int)(entity.getBlockX() - l + o.getX()), (int)(entity.getBlockY() + o.getY()), (int)(entity.getBlockZ() - l + o.getZ()), (int)(entity.getBlockX() + l + o.getX()), (int)(entity.getBlockY() + i1 + o.getY()), (int)(entity.getBlockZ() + l + o.getZ()))) {
                BlockState blockstate = entity.level.getBlockState(blockpos);
                if (!blockstate.canEntityDestroy((BlockGetter)entity.level(), blockpos, (Entity)entity) || !EventHooks.onEntityDestroyBlock((LivingEntity)entity, (BlockPos)blockpos, (BlockState)blockstate) || !entity.level.destroyBlock(blockpos, true, (Entity)entity)) continue;
                entity.level.levelEvent(null, 1022, entity.blockPosition(), 0);
            }
        }
    }

    public static Vector3f deconstructRGB(int color) {
        int red = color >> 16 & 0xFF;
        int green = color >> 8 & 0xFF;
        int blue = color & 0xFF;
        return new Vector3f((float)red / 255.0f, (float)green / 255.0f, (float)blue / 255.0f);
    }

    public static int packRGB(Vector3f color) {
        int red = (int)(color.x() * 255.0f);
        int green = (int)(color.y() * 255.0f);
        int blue = (int)(color.z() * 255.0f);
        return red << 16 | green << 8 | blue;
    }

    public static CompoundTag saveAllItems(CompoundTag pTag, NonNullList<ItemStack> pItems, String location, HolderLookup.Provider pLevelRegistry) {
        ListTag listtag = new ListTag();
        for (int i = 0; i < pItems.size(); ++i) {
            ItemStack itemstack = (ItemStack)pItems.get(i);
            if (itemstack.isEmpty()) continue;
            CompoundTag compoundtag = new CompoundTag();
            compoundtag.putByte("Slot", (byte)i);
            listtag.add((Object)itemstack.save(pLevelRegistry, (Tag)compoundtag));
        }
        if (!listtag.isEmpty()) {
            pTag.put(location, (Tag)listtag);
        }
        return pTag;
    }

    public static void loadAllItems(CompoundTag pTag, NonNullList<ItemStack> pItems, String location, HolderLookup.Provider pLevelRegistry) {
        ListTag listtag = pTag.getList(location, 10);
        for (int i = 0; i < listtag.size(); ++i) {
            CompoundTag compoundtag = listtag.getCompound(i);
            int j = compoundtag.getByte("Slot") & 0xFF;
            if (j < 0 || j >= pItems.size()) continue;
            pItems.set(j, (Object)ItemStack.parse((HolderLookup.Provider)pLevelRegistry, (Tag)compoundtag).orElse(ItemStack.EMPTY));
        }
    }

    public static float getWeaponDamage(LivingEntity entity) {
        if (entity != null) {
            float fistDamage;
            float weaponDamage = (float)entity.getAttributeValue(Attributes.ATTACK_DAMAGE);
            if (weaponDamage <= (fistDamage = (float)entity.getAttributeBaseValue(Attributes.ATTACK_DAMAGE))) {
                return 0.0f;
            }
            ItemStack weaponItem = entity.getWeaponItem();
            if (!weaponItem.isEmpty() && weaponItem.has(DataComponents.ENCHANTMENTS)) {
                weaponDamage += Utils.processEnchantment(entity.level, (ResourceKey<Enchantment>)Enchantments.SHARPNESS, (DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>>)EnchantmentEffectComponents.DAMAGE, (ItemEnchantments)weaponItem.get(DataComponents.ENCHANTMENTS));
            }
            return weaponDamage;
        }
        return 0.0f;
    }

    public static float clampedKnockbackResistanceFactor(Entity entity, float min, float max) {
        if (entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity)entity;
            return Mth.clamp((float)(1.0f - (float)living.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)), (float)min, (float)max);
        }
        return max;
    }

    public static float processEnchantment(Level level, ResourceKey<Enchantment> enchantmentKey, DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> component, ItemEnchantments enchantments) {
        Holder.Reference enchantment;
        Registry reg;
        if (enchantments != null && (reg = (Registry)level.registryAccess().registry(Registries.ENCHANTMENT).orElse(null)) != null && (enchantment = (Holder.Reference)reg.getHolder(enchantmentKey).orElse(null)) != null && enchantments.keySet().contains(enchantment)) {
            int enchantmentLevel = enchantments.getLevel((Holder)enchantment);
            List effectList = (List)((Enchantment)enchantment.value()).effects().get(component);
            if (effectList != null && !effectList.isEmpty()) {
                return ((EnchantmentValueEffect)((ConditionalEffect)effectList.getFirst()).effect()).process(enchantmentLevel, random, 0.0f);
            }
        }
        return 0.0f;
    }

    public static int getEnchantmentLevel(Level level, ResourceKey<Enchantment> enchantmentKey, ItemEnchantments enchantments) {
        Holder<Enchantment> enchantment;
        if (enchantments != null && (enchantment = Utils.enchantmentFromKey(level.registryAccess(), enchantmentKey)) != null) {
            return enchantments.getLevel(enchantment);
        }
        return 0;
    }

    public static int getEnchantmentLevel(Level level, ItemStack stack, ResourceKey<Enchantment> enchantmentKey) {
        Holder<Enchantment> enchantment = Utils.enchantmentFromKey(level.registryAccess(), enchantmentKey);
        if (enchantment != null) {
            return stack.getEnchantmentLevel(enchantment);
        }
        return 0;
    }

    @Nullable
    public static Holder<Enchantment> enchantmentFromKey(RegistryAccess registryAccess, ResourceKey<Enchantment> enchantmentkey) {
        Enchantment enchantment;
        Registry reg = registryAccess.registry(Registries.ENCHANTMENT).orElse(null);
        if (reg != null && (enchantment = (Enchantment)reg.get(enchantmentkey)) != null) {
            return reg.wrapAsHolder((Object)enchantment);
        }
        return null;
    }

    public static void enchant(ItemStack stack, RegistryAccess access, ResourceKey<Enchantment> enchantmentKey, int level) {
        Holder<Enchantment> enchantment = Utils.enchantmentFromKey(access, enchantmentKey);
        if (enchantment != null) {
            stack.enchant(enchantment, level);
        }
    }

    public static void createTremorBlock(Level level, BlockPos blockPos, float impulseStrength) {
        if (level.isClientSide) {
            return;
        }
        if (level.getBlockState(blockPos.above()).isAir() || level.getBlockState(blockPos.above().above()).isAir()) {
            MagicManager.spawnParticles(level, new FallingBlockParticleOption(level.getBlockState(blockPos), new Vec3(0.0, (double)impulseStrength, 0.0)), (double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5, 1, 0.0, 0.0, 0.0, 0.0, true);
            if (!level.getBlockState(blockPos.above()).isAir()) {
                MagicManager.spawnParticles(level, new FallingBlockParticleOption(level.getBlockState(blockPos.above()), new Vec3(0.0, (double)impulseStrength, 0.0)), (double)blockPos.getX() + 0.5, blockPos.getY() + 1, (double)blockPos.getZ() + 0.5, 1, 0.0, 0.0, 0.0, 0.0, true);
            }
        }
    }

    public static void createTremorBlockWithState(Level level, BlockState state, BlockPos blockPos, float impulseStrength) {
        MagicManager.spawnParticles(level, new FallingBlockParticleOption(state, new Vec3(0.0, (double)impulseStrength, 0.0)), (double)blockPos.getX() + 0.5, blockPos.getY() + 1, (double)blockPos.getZ() + 0.5, 1, 0.0, 0.0, 0.0, 0.0, true);
    }

    public static ItemStack setPotion(ItemStack itemStack, Holder<Potion> potion) {
        itemStack.set(DataComponents.POTION_CONTENTS, (Object)new PotionContents(potion));
        return itemStack;
    }

    public static void performTaunt(LivingEntity newTarget, float range, Predicate<Entity> selector) {
        Utils.performTaunt(newTarget, newTarget.level.getEntities((Entity)newTarget, newTarget.getBoundingBox().inflate((double)range, (double)range, (double)range), entity -> entity.distanceToSqr((Entity)newTarget) < (double)(range * range) && selector.test((Entity)entity)));
    }

    public static void performTaunt(LivingEntity newTarget, List<Entity> targets) {
        targets.forEach(entity -> {
            if (entity instanceof Mob) {
                Mob tauntmob = (Mob)entity;
                MagicManager.spawnParticles(tauntmob.level, (ParticleOptions)ParticleTypes.ANGRY_VILLAGER, tauntmob.getX(), tauntmob.getEyeY() + (tauntmob.getBoundingBox().maxY - tauntmob.getEyeY()) * 2.0, tauntmob.getZ(), 5, 0.3, 0.3, 0.3, 0.0, false);
                tauntmob.setTarget(newTarget);
            }
        });
    }

    public static void particleTrail(Level level, Vec3 a, Vec3 b, ParticleOptions particleType) {
        double d = a.distanceTo(b) * 4.0;
        int i = 0;
        while ((double)i < d) {
            double p = (double)i / d;
            Vec3 vec = a.add(b.subtract(a).scale(p));
            MagicManager.spawnParticles(level, particleType, vec.x, vec.y, vec.z, 1, 0.0, 0.0, 0.0, 0.0, true);
            ++i;
        }
    }

    public static Quaternionf rotationBetweenVectors(Vector3f from, Vector3f to) {
        Vector3f toNorm;
        Vector3f fromNorm = new Vector3f((Vector3fc)from).normalize();
        float dot = fromNorm.dot((Vector3fc)(toNorm = new Vector3f((Vector3fc)to).normalize()));
        if (dot >= 0.9999f) {
            return new Quaternionf().identity();
        }
        if (dot <= -0.9999f) {
            Vector3f perpendicular = new Vector3f(1.0f, 0.0f, 0.0f);
            if (Math.abs(fromNorm.x) > 0.9f) {
                perpendicular.set(0.0f, 1.0f, 0.0f);
            }
            perpendicular.cross((Vector3fc)fromNorm).normalize();
            return new Quaternionf().rotationAxis((float)Math.PI, (Vector3fc)perpendicular);
        }
        Vector3f axis = new Vector3f((Vector3fc)fromNorm).cross((Vector3fc)toNorm).normalize();
        float angle = (float)Math.acos(dot);
        return new Quaternionf().rotationAxis(angle, (Vector3fc)axis);
    }

    public static void addFreezeTicks(LivingEntity target, int ticks) {
        Utils.addFreezeTicks(target, ticks, target.getTicksRequiredToFreeze() * 5);
    }

    public static void addFreezeTicks(LivingEntity target, int ticks, int cap) {
        target.setTicksFrozen(Math.min(target.getTicksFrozen() + ticks, cap < 0 ? Integer.MAX_VALUE : cap));
    }

    public static Vec3 slerp(double t, Vec3 from, Vec3 to) {
        from = from.normalize();
        to = to.normalize();
        double dot = from.dot(to);
        double theta = Math.acos(dot) * t;
        Vec3 relative = to.subtract(from.scale(dot)).normalize();
        Vec3 result = from.scale(Math.cos(theta)).add(relative.scale(Math.sin(theta)));
        return result;
    }

    public static boolean isSameItemSameComponentsIgnoreDurability(ItemStack a, ItemStack b) {
        a = a.copy();
        b = b.copy();
        a.remove(DataComponents.DAMAGE);
        b.remove(DataComponents.DAMAGE);
        return ItemStack.isSameItemSameComponents((ItemStack)a, (ItemStack)b);
    }
}

