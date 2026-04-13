/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.core.Direction
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.Position
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.Container
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.SimpleContainer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.item.PrimedTnt
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.AbstractArrow
 *  net.minecraft.world.entity.projectile.FireworkRocketEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.entity.projectile.ProjectileUtil
 *  net.minecraft.world.entity.projectile.SmallFireball
 *  net.minecraft.world.entity.projectile.windcharge.WindCharge
 *  net.minecraft.world.item.ArrowItem
 *  net.minecraft.world.item.FireworkRocketItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.ProjectileItem
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.common.Tags$Items
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$RightClickBlock
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$RightClickEmpty
 *  net.neoforged.neoforge.event.tick.EntityTickEvent$Post
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.entity.ModEntities;
import com.perigrine3.createcybernetics.entity.projectile.NuggetProjectile;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.network.payload.ArmCannonFirePayload;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Position;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class ArmCannonItem
extends Item
implements ICyberwareItem {
    public static final int SLOT_COUNT = 4;
    private static final String STACK_ROOT = "cc_arm_cannon";
    private static final String STACK_INV = "inv";
    private static final String PD_ARM_CANNON = "cc_arm_cannon_projectile";
    private static final String PD_LAST_FIRE_TICK = "cc_arm_cannon_last_fire_tick";
    private static final String PD_COOLDOWNS_ROOT = "cc_arm_cannon_cooldowns";
    private static final int TICKS_PER_SECOND = 20;
    private static final int CD_NUGGETS = 10;
    private static final int CD_ARROWS = 40;
    private static final int CD_CHARGES = 60;
    private static final int CD_FIREWORKS = 100;
    private static final int CD_TNT = 100;
    private final int humanityCost;

    public ArmCannonItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public Set<TagKey<Item>> requiresCyberwareTags(ItemStack installedStack, CyberwareSlot slot) {
        return switch (slot) {
            case CyberwareSlot.RLEG -> Set.of(ModTags.Items.RIGHTARM_ITEMS);
            case CyberwareSlot.LLEG -> Set.of(ModTags.Items.LEFTARM_ITEMS);
            default -> Set.of();
        };
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.LARM, CyberwareSlot.RARM);
    }

    @Override
    public boolean replacesOrgan() {
        return false;
    }

    @Override
    public Set<CyberwareSlot> getReplacedOrgans() {
        return Set.of();
    }

    public static boolean isValidStoredItem(ItemStack stack) {
        return stack != null && !stack.isEmpty() && stack.is(ModTags.Items.ARM_CANNON_AMMO);
    }

    private static CompoundTag getOrCreateRoot(ItemStack cannonStack) {
        CustomData cd = (CustomData)cannonStack.getOrDefault(DataComponents.CUSTOM_DATA, (Object)CustomData.EMPTY);
        CompoundTag all = cd.copyTag();
        if (!all.contains(STACK_ROOT, 10)) {
            all.put(STACK_ROOT, (Tag)new CompoundTag());
        }
        return all;
    }

    private static CompoundTag getRootView(ItemStack cannonStack) {
        CustomData cd = (CustomData)cannonStack.getOrDefault(DataComponents.CUSTOM_DATA, (Object)CustomData.EMPTY);
        CompoundTag all = cd.copyTag();
        return all.contains(STACK_ROOT, 10) ? all.getCompound(STACK_ROOT) : new CompoundTag();
    }

    private static void writeBack(ItemStack cannonStack, CompoundTag all) {
        cannonStack.set(DataComponents.CUSTOM_DATA, (Object)CustomData.of((CompoundTag)all));
    }

    public static void loadFromInstalledStack(ItemStack cannonStack, HolderLookup.Provider provider, Container intoInv) {
        for (int i = 0; i < 4; ++i) {
            intoInv.setItem(i, ItemStack.EMPTY);
        }
        if (cannonStack == null || cannonStack.isEmpty()) {
            return;
        }
        CompoundTag root = ArmCannonItem.getRootView(cannonStack);
        if (!root.contains(STACK_INV, 9)) {
            return;
        }
        ListTag list = root.getList(STACK_INV, 10);
        for (int i = 0; i < 4 && i < list.size(); ++i) {
            CompoundTag c = list.getCompound(i);
            ItemStack st = ItemStack.parseOptional((HolderLookup.Provider)provider, (CompoundTag)c);
            if (st.isEmpty() || !ArmCannonItem.isValidStoredItem(st)) {
                intoInv.setItem(i, ItemStack.EMPTY);
                continue;
            }
            int cap = Math.max(1, st.getMaxStackSize());
            if (st.getCount() > cap) {
                st.setCount(cap);
            }
            intoInv.setItem(i, st);
        }
    }

    public static void saveIntoInstalledStack(ItemStack cannonStack, HolderLookup.Provider provider, Container fromInv) {
        if (cannonStack == null || cannonStack.isEmpty()) {
            return;
        }
        ListTag list = new ListTag();
        for (int i = 0; i < 4; ++i) {
            ItemStack st = fromInv.getItem(i);
            if (!st.isEmpty() && ArmCannonItem.isValidStoredItem(st)) {
                ItemStack copy = st.copy();
                int cap = Math.max(1, copy.getMaxStackSize());
                if (copy.getCount() > cap) {
                    copy.setCount(cap);
                }
                list.add((Object)copy.save(provider));
                continue;
            }
            list.add((Object)new CompoundTag());
        }
        CompoundTag all = ArmCannonItem.getOrCreateRoot(cannonStack);
        CompoundTag root = all.getCompound(STACK_ROOT);
        root.put(STACK_INV, (Tag)list);
        all.put(STACK_ROOT, (Tag)root);
        ArmCannonItem.writeBack(cannonStack, all);
    }

    public static void dropAndClearInstalledStack(ServerPlayer sp, HolderLookup.Provider provider, ItemStack cannonStack) {
        if (cannonStack == null || cannonStack.isEmpty()) {
            return;
        }
        SimpleContainer tmp = new SimpleContainer(4);
        ArmCannonItem.loadFromInstalledStack(cannonStack, provider, (Container)tmp);
        for (int i = 0; i < 4; ++i) {
            ItemStack st = tmp.getItem(i);
            if (st.isEmpty()) continue;
            sp.drop(st, false);
        }
        CustomData cd = (CustomData)cannonStack.getOrDefault(DataComponents.CUSTOM_DATA, (Object)CustomData.EMPTY);
        CompoundTag all = cd.copyTag();
        all.remove(STACK_ROOT);
        cannonStack.set(DataComponents.CUSTOM_DATA, (Object)CustomData.of((CompoundTag)all));
    }

    @Override
    public void onRemoved(Player player) {
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer sp = (ServerPlayer)player;
        if (!sp.hasData(ModAttachments.CYBERWARE)) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)sp.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (int i = 0; i < arr.length; ++i) {
                ItemStack st;
                InstalledCyberware inst = arr[i];
                if (inst == null || (st = inst.getItem()) == null || st.isEmpty() || st.getItem() != this) continue;
                ArmCannonItem.dropAndClearInstalledStack(sp, (HolderLookup.Provider)sp.level().registryAccess(), st);
            }
        }
        data.setDirty();
        sp.syncData(ModAttachments.CYBERWARE);
    }

    public static boolean fireLoaded(ServerPlayer sp) {
        long nextAllowed;
        if (sp == null) {
            return false;
        }
        if (!sp.getOffhandItem().isEmpty()) {
            return false;
        }
        long now = sp.level().getGameTime();
        CompoundTag pd = sp.getPersistentData();
        if (pd.getLong(PD_LAST_FIRE_TICK) == now) {
            return false;
        }
        pd.putLong(PD_LAST_FIRE_TICK, now);
        if (!sp.hasData(ModAttachments.CYBERWARE)) {
            return false;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)sp.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return false;
        }
        CannonRef ref = ArmCannonItem.findInstalledArmCannon(data);
        if (ref == null) {
            return false;
        }
        if (!data.isEnabled(ref.slot(), ref.index())) {
            return false;
        }
        ItemStack cannonStack = ref.stack();
        if (cannonStack.isEmpty()) {
            return false;
        }
        int selected = data.getArmCannonSelected();
        if (selected < 0 || selected >= 4) {
            return false;
        }
        SimpleContainer inv = new SimpleContainer(4);
        ArmCannonItem.loadFromInstalledStack(cannonStack, (HolderLookup.Provider)sp.level().registryAccess(), (Container)inv);
        ItemStack ammo = inv.getItem(selected);
        if (ammo.isEmpty() || !ArmCannonItem.isValidStoredItem(ammo)) {
            return false;
        }
        int cooldownTicks = ArmCannonItem.getCooldownTicks(ammo);
        String cooldownKey = ArmCannonItem.getCooldownKey(ammo);
        if (cooldownTicks > 0 && cooldownKey != null && now < (nextAllowed = ArmCannonItem.getNextAllowedTick(pd, cooldownKey))) {
            return false;
        }
        ItemStack ammoOne = ammo.copyWithCount(1);
        boolean fired = ArmCannonItem.spawnAmmoProjectile(sp, ammoOne);
        if (!fired) {
            return false;
        }
        if (cooldownTicks > 0 && cooldownKey != null) {
            ArmCannonItem.setNextAllowedTick(pd, cooldownKey, now + (long)cooldownTicks);
        }
        ammo.shrink(1);
        inv.setItem(selected, ammo.isEmpty() ? ItemStack.EMPTY : ammo);
        ArmCannonItem.saveIntoInstalledStack(cannonStack, (HolderLookup.Provider)sp.level().registryAccess(), (Container)inv);
        data.setDirty();
        sp.syncData(ModAttachments.CYBERWARE);
        return true;
    }

    private static CannonRef findInstalledArmCannon(PlayerCyberwareData data) {
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            CyberwareSlot slot = entry.getKey();
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (int i = 0; i < arr.length; ++i) {
                ItemStack st;
                InstalledCyberware cw = arr[i];
                if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || st.getItem() != ModItems.ARMUPGRADES_ARMCANNON.get()) continue;
                return new CannonRef(slot, i, st);
            }
        }
        return null;
    }

    private static int getCooldownTicks(ItemStack ammo) {
        if (ammo == null || ammo.isEmpty()) {
            return 0;
        }
        if (ammo.is(Tags.Items.NUGGETS)) {
            return 10;
        }
        if (ammo.is(Items.WIND_CHARGE) || ammo.is(Items.FIRE_CHARGE)) {
            return 60;
        }
        if (ammo.getItem() instanceof FireworkRocketItem) {
            return 100;
        }
        if (ammo.is(Items.TNT)) {
            return 100;
        }
        if (ammo.getItem() instanceof ArrowItem) {
            return 40;
        }
        return 0;
    }

    private static String getCooldownKey(ItemStack ammo) {
        if (ammo == null || ammo.isEmpty()) {
            return null;
        }
        if (ammo.is(Tags.Items.NUGGETS)) {
            return "nuggets";
        }
        if (ammo.is(Items.WIND_CHARGE)) {
            return "wind_charge";
        }
        if (ammo.is(Items.FIRE_CHARGE)) {
            return "fire_charge";
        }
        if (ammo.getItem() instanceof FireworkRocketItem) {
            return "firework_rocket";
        }
        if (ammo.is(Items.TNT)) {
            return "tnt";
        }
        if (ammo.getItem() instanceof ArrowItem) {
            return "arrows";
        }
        return null;
    }

    private static long getNextAllowedTick(CompoundTag playerPd, String key) {
        if (playerPd == null || key == null) {
            return Long.MIN_VALUE;
        }
        CompoundTag root = playerPd.getCompound(PD_COOLDOWNS_ROOT);
        return root.getLong(key);
    }

    private static void setNextAllowedTick(CompoundTag playerPd, String key, long tick) {
        if (playerPd == null || key == null) {
            return;
        }
        CompoundTag root = playerPd.getCompound(PD_COOLDOWNS_ROOT);
        root.putLong(key, tick);
        playerPd.put(PD_COOLDOWNS_ROOT, (Tag)root);
    }

    private static boolean spawnAmmoProjectile(ServerPlayer sp, ItemStack ammoOne) {
        Level level = sp.level();
        Vec3 look = sp.getLookAngle().normalize();
        Vec3 start = sp.getEyePosition().add(look.scale(1.0));
        float genericSpeed = 4.0f;
        float arrowSpeed = 4.0f;
        float fireworkSpeed = 2.2f;
        double tntSpeed = 1.8;
        float nuggetSpeed = 5.0f;
        double hurtingProjectileSpeed = 4.0;
        float inaccuracy = 0.0f;
        if (ammoOne.is(Tags.Items.NUGGETS)) {
            Entity target;
            NuggetProjectile bullet = new NuggetProjectile(ModEntities.NUGGET_PROJECTILE.get(), level, (LivingEntity)sp, ammoOne);
            bullet.setOwner((Entity)sp);
            bullet.setPos(start.x, start.y, start.z);
            bullet.shoot(look.x, look.y, look.z, 5.0f, 0.0f);
            level.addFreshEntity((Entity)bullet);
            double range = 64.0;
            Vec3 eye = sp.getEyePosition();
            Vec3 end = eye.add(look.scale(range));
            BlockHitResult blockHit = level.clip(new ClipContext(eye, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)sp));
            Vec3 cappedEnd = blockHit.getType() == HitResult.Type.MISS ? end : blockHit.getLocation();
            EntityHitResult entityHit = ProjectileUtil.getEntityHitResult((Level)level, (Entity)sp, (Vec3)eye, (Vec3)cappedEnd, (AABB)sp.getBoundingBox().expandTowards(look.scale(range)).inflate(1.0), e -> e.isPickable() && e != sp);
            if (entityHit != null && (target = entityHit.getEntity()) instanceof LivingEntity) {
                LivingEntity living = (LivingEntity)target;
                float damage = 6.0f;
                living.hurt(sp.damageSources().playerAttack((Player)sp), damage);
            }
            return true;
        }
        if (ammoOne.is(Items.TNT)) {
            PrimedTnt tnt = new PrimedTnt(level, start.x, start.y, start.z, (LivingEntity)sp);
            tnt.setFuse(60);
            tnt.setDeltaMovement(look.scale(1.8));
            level.addFreshEntity((Entity)tnt);
            return true;
        }
        if (ammoOne.is(Items.FIRE_CHARGE)) {
            Vec3 vel = look.scale(4.0);
            SmallFireball fb = new SmallFireball(level, (LivingEntity)sp, vel);
            fb.setOwner((Entity)sp);
            fb.setPos(start.x, start.y, start.z);
            fb.setDeltaMovement(vel);
            level.addFreshEntity((Entity)fb);
            return true;
        }
        if (ammoOne.is(Items.WIND_CHARGE)) {
            Vec3 vel = look.scale(4.0);
            WindCharge wc = new WindCharge(level, start.x, start.y, start.z, vel);
            wc.setOwner((Entity)sp);
            wc.setPos(start.x, start.y, start.z);
            wc.setDeltaMovement(vel);
            level.addFreshEntity((Entity)wc);
            return true;
        }
        if (ammoOne.getItem() instanceof FireworkRocketItem) {
            FireworkRocketEntity rocket = new FireworkRocketEntity(level, ammoOne, (Entity)sp, start.x, start.y, start.z, true);
            rocket.setOwner((Entity)sp);
            rocket.setPos(start.x, start.y, start.z);
            rocket.shoot(look.x, look.y, look.z, 2.2f, 0.0f);
            rocket.getPersistentData().putBoolean(PD_ARM_CANNON, true);
            level.addFreshEntity((Entity)rocket);
            return true;
        }
        Item wc = ammoOne.getItem();
        if (wc instanceof ProjectileItem) {
            ProjectileItem projItem = (ProjectileItem)wc;
            Direction dir = Direction.getNearest((double)look.x, (double)look.y, (double)look.z);
            Projectile proj = projItem.asProjectile(level, (Position)start, ammoOne, dir);
            if (proj == null) {
                return false;
            }
            proj.setOwner((Entity)sp);
            proj.setPos(start.x, start.y, start.z);
            float speed = proj instanceof AbstractArrow ? 4.0f : 4.0f;
            projItem.shoot(proj, look.x, look.y, look.z, speed, 0.0f);
            level.addFreshEntity((Entity)proj);
            return true;
        }
        return false;
    }

    private record CannonRef(CyberwareSlot slot, int index, ItemStack stack) {
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class ArmCannonRocketImpactFix {
        private ArmCannonRocketImpactFix() {
        }

        @SubscribeEvent
        public static void onEntityTick(EntityTickEvent.Post event) {
            boolean hitSomething;
            Entity e = event.getEntity();
            if (e.level().isClientSide) {
                return;
            }
            if (!(e instanceof FireworkRocketEntity)) {
                return;
            }
            FireworkRocketEntity rocket = (FireworkRocketEntity)e;
            if (rocket.isRemoved()) {
                return;
            }
            CompoundTag pd = rocket.getPersistentData();
            if (!pd.getBoolean(ArmCannonItem.PD_ARM_CANNON)) {
                return;
            }
            Vec3 from = rocket.position();
            Vec3 motion = rocket.getDeltaMovement();
            Vec3 to = from.add(motion);
            BlockHitResult blockHit = rocket.level().clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)rocket));
            EntityHitResult entityHit = ProjectileUtil.getEntityHitResult((Level)rocket.level(), (Entity)rocket, (Vec3)from, (Vec3)to, (AABB)rocket.getBoundingBox().expandTowards(motion).inflate(0.25), hit -> hit.isPickable() && hit != rocket.getOwner());
            boolean bl = hitSomething = entityHit != null || blockHit.getType() != HitResult.Type.MISS || rocket.horizontalCollision || rocket.verticalCollision;
            if (!hitSomething) {
                return;
            }
            Vec3 at = entityHit != null ? entityHit.getLocation() : (blockHit.getType() != HitResult.Type.MISS ? blockHit.getLocation() : rocket.position());
            rocket.setPos(at.x, at.y, at.z);
            rocket.setDeltaMovement(Vec3.ZERO);
            rocket.level().broadcastEntityEvent((Entity)rocket, (byte)17);
            rocket.discard();
        }
    }

    @EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT}, bus=EventBusSubscriber.Bus.GAME)
    public static final class ClientFireTrigger {
        private static long lastSentGameTime = Long.MIN_VALUE;

        private ClientFireTrigger() {
        }

        @SubscribeEvent
        public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
            Player p = event.getEntity();
            if (p == null) {
                return;
            }
            if (!p.getOffhandItem().isEmpty()) {
                return;
            }
            if (Minecraft.getInstance().screen != null) {
                return;
            }
            long now = p.level().getGameTime();
            if (now == lastSentGameTime) {
                return;
            }
            lastSentGameTime = now;
            PacketDistributor.sendToServer((CustomPacketPayload)new ArmCannonFirePayload(), (CustomPacketPayload[])new CustomPacketPayload[0]);
        }

        @SubscribeEvent
        public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            Player p = event.getEntity();
            if (p == null) {
                return;
            }
            if (event.getHand() != InteractionHand.MAIN_HAND) {
                return;
            }
            if (!p.getMainHandItem().isEmpty()) {
                return;
            }
            if (!p.getOffhandItem().isEmpty()) {
                return;
            }
            if (Minecraft.getInstance().screen != null) {
                return;
            }
            long now = p.level().getGameTime();
            if (now == lastSentGameTime) {
                return;
            }
            lastSentGameTime = now;
            PacketDistributor.sendToServer((CustomPacketPayload)new ArmCannonFirePayload(), (CustomPacketPayload[])new CustomPacketPayload[0]);
        }
    }
}

