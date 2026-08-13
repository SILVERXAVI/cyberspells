/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.Holder
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.ComponentContents
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.network.chat.contents.TranslatableContents
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.alchemy.PotionContents
 *  net.minecraft.world.item.component.ItemAttributeModifiers
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent$LoggingOut
 *  net.neoforged.neoforge.client.event.MovementInputUpdateEvent
 *  net.neoforged.neoforge.client.event.RenderLivingEvent$Post
 *  net.neoforged.neoforge.client.event.RenderLivingEvent$Pre
 *  net.neoforged.neoforge.client.event.ScreenEvent$Opening
 *  net.neoforged.neoforge.client.event.ViewportEvent$ComputeFogColor
 *  net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerLoggedInEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerRespawnEvent
 *  net.neoforged.neoforge.event.tick.EntityTickEvent$Pre
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Pre
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package io.redspace.ironsspellbooks.player;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.util.FogManager;
import io.redspace.ironsspellbooks.api.util.MusicManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.effect.CustomDescriptionMobEffect;
import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.effect.guiding_bolt.GuidingBoltManager;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.UpgradeOrbItem;
import io.redspace.ironsspellbooks.item.armor.UpgradeOrbType;
import io.redspace.ironsspellbooks.network.casting.CancelCastPacket;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.player.SpinAttackType;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.UpgradeOrbTypeRegistry;
import io.redspace.ironsspellbooks.render.SpellRenderingHelper;
import io.redspace.ironsspellbooks.spells.blood.RayOfSiphoningSpell;
import io.redspace.ironsspellbooks.spells.ender.RecallSpell;
import io.redspace.ironsspellbooks.spells.fire.BurningDashSpell;
import io.redspace.ironsspellbooks.spells.fire.RaiseHellSpell;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(value={Dist.CLIENT})
public class ClientPlayerEvents {
    @SubscribeEvent
    public static void onCalculatePlayerSpeed(MovementInputUpdateEvent event) {
        if (ClientMagicData.isCasting()) {
            float baseCastingSpeed = 0.2f;
            float castingSpeedModifier = (float)event.getEntity().getAttributeValue(AttributeRegistry.CASTING_MOVESPEED);
            float speed = baseCastingSpeed + castingSpeedModifier - 1.0f;
            event.getInput().forwardImpulse *= speed;
            event.getInput().leftImpulse *= speed;
        }
    }

    @SubscribeEvent
    public static void onPlayerLogOut(ClientPlayerNetworkEvent.LoggingOut event) {
        IronsSpellbooks.LOGGER.debug("ClientPlayerNetworkEvent onPlayerLogOut");
        MusicManager.clear();
        GuidingBoltManager.handleClientLogout();
        ClientMagicData.spellSelectionManager = null;
        FogManager.clear();
        if (event.getPlayer() != null) {
            ClientMagicData.resetClientCastState(event.getPlayer().getUUID());
        }
    }

    @SubscribeEvent
    public static void onPlayerOpenScreen(ScreenEvent.Opening event) {
        if (ClientMagicData.isCasting()) {
            PacketDistributor.sendToServer((CustomPacketPayload)new CancelCastPacket(SpellRegistry.getSpell(ClientMagicData.getCastingSpellId()).getCastType() == CastType.CONTINUOUS), (CustomPacketPayload[])new CustomPacketPayload[0]);
        }
    }

    @SubscribeEvent
    public static void onClientEntityTick(EntityTickEvent.Pre event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            for (MobEffectInstance inst : livingEntity.getActiveEffects()) {
                Object object = inst.getEffect().value();
                if (!(object instanceof ISyncedMobEffect)) continue;
                ISyncedMobEffect effect = (ISyncedMobEffect)object;
                effect.clientTick(livingEntity, inst);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (event.getEntity() == Minecraft.getInstance().player) {
            ClientLevel level = Minecraft.getInstance().level;
            ClientMagicData.getRecasts().tickRecasts();
            ClientMagicData.getCooldowns().tick(1);
            if (ClientMagicData.getCastDuration() > 0) {
                ClientMagicData.handleCastDuration();
            }
            if (level != null) {
                List spellcasters = level.getEntities((Entity)null, event.getEntity().getBoundingBox().inflate(64.0), mob -> mob instanceof Player || mob instanceof IMagicEntity);
                spellcasters.forEach(entity -> {
                    LivingEntity livingEntity = (LivingEntity)entity;
                    SyncedSpellData spellData = ClientMagicData.getSyncedSpellData(livingEntity);
                    if (livingEntity.isAutoSpinAttack() && spellData.getSpinAttackType() == SpinAttackType.FIRE) {
                        BurningDashSpell.ambientParticles(level, livingEntity);
                    }
                    if (spellData.isCasting()) {
                        if (spellData.getCastingSpellId().equals(SpellRegistry.RAY_OF_SIPHONING_SPELL.get().getSpellId())) {
                            Vec3 impact = Utils.raycastForEntity(entity.level, entity, RayOfSiphoningSpell.getRange(0), true).getLocation().subtract(0.0, 0.25, 0.0);
                            for (int i = 0; i < 8; ++i) {
                                Vec3 motion = new Vec3(Utils.getRandomScaled(0.2f), Utils.getRandomScaled(0.2f), Utils.getRandomScaled(0.2f));
                                entity.level.addParticle(ParticleHelper.SIPHON, impact.x + motion.x, impact.y + motion.y, impact.z + motion.z, motion.x, motion.y, motion.z);
                            }
                        } else if (spellData.getCastingSpellId().equals(SpellRegistry.RECALL_SPELL.get().getSpellId())) {
                            RecallSpell.ambientParticles(livingEntity, spellData);
                        } else if (spellData.getCastingSpellId().equals(SpellRegistry.RAISE_HELL_SPELL.get().getSpellId())) {
                            RaiseHellSpell.ambientParticles(livingEntity, spellData);
                        }
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player instanceof LocalPlayer) {
            LocalPlayer player2 = (LocalPlayer)player;
            ClientMagicData.spellSelectionManager = new SpellSelectionManager((Player)player2);
        }
    }

    @SubscribeEvent
    public static void beforeLivingRender(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity.hasEffect(MobEffectRegistry.TRUE_INVISIBILITY) && livingEntity.isInvisibleTo((Player)player)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void afterLivingRender(RenderLivingEvent.Post<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
        SyncedSpellData syncedData;
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Player && (syncedData = ClientMagicData.getSyncedSpellData(livingEntity)).isCasting()) {
            SpellRenderingHelper.renderSpellHelper(syncedData, livingEntity, event.getPoseStack(), event.getMultiBufferSource(), event.getPartialTick());
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        MinecraftInstanceHelper.ifPlayerPresent(player -> {
            if (player.getUUID().equals(event.getEntity().getUUID())) {
                ClientMagicData.updateSpellSelectionManager();
            }
        });
    }

    @SubscribeEvent
    public static void imbuedWeaponTooltips(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof Scroll) {
            return;
        }
        MinecraftInstanceHelper.ifPlayerPresent(player1 -> {
            LocalPlayer player = (LocalPlayer)player1;
            List lines = event.getToolTip();
            boolean advanced = event.getFlags().isAdvanced();
            if (stack.has(ComponentRegistry.UPGRADE_ORB_TYPE)) {
                ClientPlayerEvents.handleUpgradeOrbTooltip(stack, player, lines, advanced);
            }
            if (stack.has(ComponentRegistry.CASTING_IMPLEMENT)) {
                ClientPlayerEvents.handleCastingImplementTooltip(stack, player, lines, advanced);
            }
            if (ISpellContainer.isSpellContainer(stack) && !(stack.getItem() instanceof SpellBook)) {
                ClientPlayerEvents.handleImbuedSpellTooltip(stack, player, lines, advanced);
            }
            if (ISpellContainer.isSpellContainer(stack) && Utils.canImbue(stack)) {
                ISpellContainer spellContainer = ISpellContainer.get(stack);
                lines.add(1, Component.translatable((String)"tooltip.irons_spellbooks.can_be_imbued_frame", (Object[])new Object[]{Component.translatable((String)"tooltip.irons_spellbooks.can_be_imbued_number", (Object[])new Object[]{spellContainer.getActiveSpellCount(), spellContainer.getMaxSpellCount()}).withStyle(ChatFormatting.YELLOW)}).withStyle(ChatFormatting.GOLD));
            }
            if (stack.has(ComponentRegistry.MULTIHAND_WEAPON)) {
                Predicate<Holder<Attribute>> predicate = (Boolean)ServerConfigs.APPLY_ALL_MULTIHAND_ATTRIBUTES.get() != false ? Utils.NON_BASE_ATTRIBUTES : Utils.ONLY_MAGIC_ATTRIBUTES;
                int i = TooltipsUtils.indexOfComponent(lines, "item.modifiers.mainhand");
                if (i >= 0) {
                    int endIndex = 0;
                    ArrayList<Integer> linesToGrab = new ArrayList<Integer>();
                    for (int j = i; j < lines.size(); ++j) {
                        ComponentContents contents = ((Component)lines.get(j)).getContents();
                        if (contents instanceof TranslatableContents) {
                            TranslatableContents translatableContents = (TranslatableContents)contents;
                            if (translatableContents.getKey().startsWith("attribute.modifier")) {
                                endIndex = j;
                                for (Object arg : translatableContents.getArgs()) {
                                    TranslatableContents translatableContents2;
                                    Attribute atr;
                                    Component component;
                                    ComponentContents patt0$temp;
                                    if (!(arg instanceof Component) || !((patt0$temp = (component = (Component)arg).getContents()) instanceof TranslatableContents) || (atr = ClientPlayerEvents.getAttributeForDescriptionId((translatableContents2 = (TranslatableContents)patt0$temp).getKey())) == null || !predicate.test((Holder<Attribute>)BuiltInRegistries.ATTRIBUTE.wrapAsHolder((Object)atr))) continue;
                                    linesToGrab.add(j);
                                }
                                continue;
                            }
                            if (i == j || !translatableContents.getKey().startsWith("item.modifiers")) continue;
                            break;
                        }
                        for (Component line : ((Component)lines.get(j)).getSiblings()) {
                            TranslatableContents translatableContents;
                            ComponentContents patt0$temp = line.getContents();
                            if (!(patt0$temp instanceof TranslatableContents) || !(translatableContents = (TranslatableContents)patt0$temp).getKey().startsWith("attribute.modifier")) continue;
                            endIndex = j;
                        }
                    }
                    if (!linesToGrab.isEmpty()) {
                        lines.add(++endIndex, Component.empty());
                        lines.add(++endIndex, Component.translatable((String)"tooltip.irons_spellbooks.modifiers.multihand").withStyle(((Component)lines.get(i)).getStyle()));
                        for (Integer index : linesToGrab) {
                            lines.add(++endIndex, (Component)lines.get(index));
                        }
                        for (int j = linesToGrab.size() - 1; j >= 0; --j) {
                            lines.remove((Integer)linesToGrab.get(j));
                        }
                    }
                }
            }
        });
    }

    private static void handleImbuedSpellTooltip(ItemStack stack, LocalPlayer player, List<Component> lines, boolean advanced) {
        int tooltipInjectIndex;
        ISpellContainer spellContainer = ISpellContainer.get(stack);
        int n = tooltipInjectIndex = advanced ? TooltipsUtils.indexOfAdvancedText(lines, stack) : lines.size();
        if (!spellContainer.isEmpty()) {
            ArrayList<Component> additionalLines = new ArrayList<Component>();
            int spellCount = spellContainer.getActiveSpellCount();
            MutableComponent header = Component.translatable((String)(spellCount > 1 ? "tooltip.irons_spellbooks.imbued_tooltip_plural" : "tooltip.irons_spellbooks.imbued_tooltip")).withStyle(ChatFormatting.GRAY);
            if (spellCount > 3) {
                additionalLines.add((Component)Component.empty());
                SpellSelectionManager spellSelectionManager = ClientMagicData.getSpellSelectionManager();
                for (int i = 0; i < spellContainer.getActiveSpellCount(); ++i) {
                    SpellData spellSlot2 = spellContainer.getSpellAtIndex(i);
                    MutableComponent spellText = TooltipsUtils.getTitleComponent(spellSlot2, player).setStyle(Style.EMPTY);
                    SpellSelectionManager.SelectionOption option = spellSelectionManager.getSpellSlot(spellSelectionManager.getSelectionIndex());
                    if (option != null && option.slotIndex == i && (option.slot.equals("mainhand") && player.getMainHandItem() == stack || option.slot.equals("offhand") && player.getOffhandItem() == stack)) {
                        List<MutableComponent> shiftMessage = TooltipsUtils.formatActiveSpellTooltip(stack, spellSelectionManager.getSelectedSpellData(), CastSource.SPELLBOOK, player);
                        shiftMessage.remove(0);
                        TooltipsUtils.addShiftTooltip(additionalLines, (Component)Component.literal((String)"> ").append((Component)spellText).withStyle(ChatFormatting.YELLOW), shiftMessage.stream().map(component -> Component.literal((String)" ").append((Component)component)).collect(Collectors.toList()));
                        continue;
                    }
                    additionalLines.add((Component)Component.literal((String)" ").append((Component)spellText.withStyle(Style.EMPTY.withColor(0x8888FE))));
                }
            } else {
                spellContainer.getActiveSpells().forEach(spellSlot -> {
                    List<MutableComponent> spellTooltip = TooltipsUtils.formatActiveSpellTooltip(stack, spellSlot.spellData(), CastSource.SWORD, player);
                    spellTooltip.set(1, Component.literal((String)" ").append((Component)spellTooltip.get(1)));
                    additionalLines.addAll(spellTooltip);
                });
            }
            additionalLines.add(1, (Component)header);
            lines.addAll(tooltipInjectIndex < 0 ? lines.size() : tooltipInjectIndex, additionalLines);
        }
    }

    private static void handleCastingImplementTooltip(ItemStack stack, LocalPlayer player, List<Component> lines, boolean advanced) {
        SpellSelectionManager.SelectionOption spellSlot = ClientMagicData.getSpellSelectionManager().getSelection();
        if (spellSlot != null && spellSlot.spellData != SpellData.EMPTY) {
            List<MutableComponent> additionalLines = TooltipsUtils.formatActiveSpellTooltip(stack, spellSlot.spellData, spellSlot.getCastSource(), player);
            additionalLines.add(1, Component.translatable((String)"tooltip.irons_spellbooks.casting_implement_tooltip").withStyle(ChatFormatting.GRAY));
            additionalLines.set(2, Component.literal((String)" ").append((Component)additionalLines.get(2)));
            additionalLines.add(Component.literal((String)" ").append((Component)Component.translatable((String)"tooltip.irons_spellbooks.press_to_cast_active", (Object[])new Object[]{Component.keybind((String)"key.use")}).withStyle(ChatFormatting.GOLD)));
            int i = advanced ? TooltipsUtils.indexOfAdvancedText(lines, stack) : lines.size();
            lines.addAll(i < 0 ? lines.size() : i, additionalLines);
        }
    }

    private static void handleUpgradeOrbTooltip(ItemStack stack, LocalPlayer player, List<Component> lines, boolean advanced) {
        ResourceKey upgradeKey = (ResourceKey)stack.get(ComponentRegistry.UPGRADE_ORB_TYPE);
        if (upgradeKey != null) {
            UpgradeOrbType upgrade = (UpgradeOrbType)UpgradeOrbTypeRegistry.upgradeTypeRegistry(player.registryAccess()).get(upgradeKey.location());
            if (upgrade == null) {
                return;
            }
            ArrayList<Object> newlines = new ArrayList<Object>();
            newlines.add(Component.empty());
            newlines.add(UpgradeOrbItem.TOOLTIP_HEADER);
            MutableComponent text = Component.literal((String)" ").append((Component)Component.translatable((String)("attribute.modifier.plus." + upgrade.operation().id()), (Object[])new Object[]{ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(upgrade.amount() * (double)(upgrade.operation() == AttributeModifier.Operation.ADD_VALUE ? 1 : 100)), Component.translatable((String)((Attribute)upgrade.attribute().value()).getDescriptionId())}).withStyle(ChatFormatting.BLUE));
            newlines.add(text);
            int i = advanced ? TooltipsUtils.indexOfAdvancedText(lines, stack) : lines.size();
            lines.addAll(i < 0 ? lines.size() : i, newlines);
        }
    }

    private static Attribute getAttributeForDescriptionId(String descriptionId) {
        return BuiltInRegistries.ATTRIBUTE.stream().filter(attribute -> attribute.getDescriptionId().equals(descriptionId)).findFirst().orElse(null);
    }

    @SubscribeEvent
    public static void customPotionTooltips(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        PotionContents potionData = (PotionContents)stack.get(DataComponents.POTION_CONTENTS);
        if (potionData != null) {
            potionData.getAllEffects().forEach(mobEffectInstance -> {
                Object patt0$temp = mobEffectInstance.getEffect().value();
                if (patt0$temp instanceof CustomDescriptionMobEffect) {
                    CustomDescriptionMobEffect customDescriptionMobEffect = (CustomDescriptionMobEffect)patt0$temp;
                    CustomDescriptionMobEffect.handleCustomPotionTooltip(stack, event.getToolTip(), event.getFlags().isAdvanced(), mobEffectInstance, customDescriptionMobEffect);
                }
            });
        }
    }

    @SubscribeEvent
    public static void changeFogColor(ViewportEvent.ComputeFogColor event) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.hasEffect(MobEffectRegistry.PLANAR_SIGHT)) {
            int color = ((MobEffect)MobEffectRegistry.PLANAR_SIGHT.get()).getColor();
            float f = 0.0f;
            float f1 = 0.0f;
            float f2 = 0.0f;
            event.setRed((f += (float)(color >> 16 & 0xFF) / 255.0f) * 0.15f);
            event.setGreen((f1 += (float)(color >> 8 & 0xFF) / 255.0f) * 0.15f);
            event.setBlue((f2 += (float)(color >> 0 & 0xFF) / 255.0f) * 0.15f);
        }
    }
}

