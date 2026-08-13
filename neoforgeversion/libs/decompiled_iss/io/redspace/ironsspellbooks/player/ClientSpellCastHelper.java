/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.kosmx.playerAnim.api.IPlayable
 *  dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration
 *  dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode
 *  dev.kosmx.playerAnim.api.layered.IAnimation
 *  dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer
 *  dev.kosmx.playerAnim.api.layered.ModifierLayer
 *  dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier
 *  dev.kosmx.playerAnim.core.data.KeyframeAnimation
 *  dev.kosmx.playerAnim.core.util.Ease
 *  dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess
 *  dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.particles.BlockParticleOption
 *  net.minecraft.core.particles.ColorParticleOption
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleType
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Vector3f
 */
package io.redspace.ironsspellbooks.player;

import dev.kosmx.playerAnim.api.IPlayable;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.gui.EldritchResearchScreen;
import io.redspace.ironsspellbooks.network.casting.CastErrorPacket;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.player.ClientInputEvents;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.setup.IronsAdjustmentModifier;
import io.redspace.ironsspellbooks.spells.ender.TeleportSpell;
import io.redspace.ironsspellbooks.spells.ice.FrostStepSpell;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ClientSpellCastHelper {
    private static boolean suppressRightClicks;

    public static boolean shouldSuppressRightClicks() {
        return suppressRightClicks;
    }

    public static void setSuppressRightClicks(boolean suppressRightClicks) {
        ClientSpellCastHelper.suppressRightClicks = suppressRightClicks;
    }

    public static void openEldritchResearchScreen(InteractionHand hand) {
        Minecraft.getInstance().setScreen((Screen)new EldritchResearchScreen((Component)Component.empty(), hand));
    }

    public static void handleCastErrorMessage(CastErrorPacket packet) {
        AbstractSpell spell = SpellRegistry.getSpell(packet.spellId);
        if (packet.errorType == CastErrorPacket.ErrorType.COOLDOWN) {
            if (ClientInputEvents.hasReleasedSinceCasting) {
                Minecraft.getInstance().gui.setOverlayMessage((Component)Component.translatable((String)"ui.irons_spellbooks.cast_error_cooldown", (Object[])new Object[]{spell.getDisplayName((Player)Minecraft.getInstance().player)}).withStyle(ChatFormatting.RED), false);
            }
        } else {
            Minecraft.getInstance().gui.setOverlayMessage((Component)Component.translatable((String)"ui.irons_spellbooks.cast_error_mana", (Object[])new Object[]{spell.getDisplayName((Player)Minecraft.getInstance().player)}).withStyle(ChatFormatting.RED), false);
        }
    }

    public static void handleClientboundBloodSiphonParticles(Vec3 pos1, Vec3 pos2) {
        if (Minecraft.getInstance().player == null) {
            return;
        }
        Level level = Minecraft.getInstance().player.level;
        Vec3 direction = pos2.subtract(pos1).scale((double)0.1f);
        for (int i = 0; i < 40; ++i) {
            Vec3 scaledDirection = direction.scale(1.0 + Utils.getRandomScaled(0.35));
            Vec3 random = new Vec3(Utils.getRandomScaled(0.08f), Utils.getRandomScaled(0.08f), Utils.getRandomScaled(0.08f));
            level.addParticle(ParticleHelper.BLOOD, pos1.x, pos1.y, pos1.z, scaledDirection.x + random.x, scaledDirection.y + random.y, scaledDirection.z + random.z);
        }
    }

    public static void handleClientboundFlamethrowerParticles(Vec3 pos, Vec3 dir) {
        if (Minecraft.getInstance().player == null) {
            return;
        }
        Level level = Minecraft.getInstance().player.level;
        int particleCount = dir.lengthSqr() < 0.25 ? 3 : 5;
        for (int i = 0; i < particleCount; ++i) {
            Vec3 spread = Utils.getRandomVec3(0.025);
            Vec3 traverse = dir.scale((double)Utils.random.nextFloat()).add(pos);
            Vec3 motion = dir.scale((double)((float)Utils.random.nextIntBetweenInclusive(8, 11) * 0.08f)).add(spread);
            level.addParticle(ParticleHelper.FIRE_EMITTER, traverse.x, traverse.y, traverse.z, motion.x, motion.y, motion.z);
        }
    }

    public static void handleClientboundShockwaveParticle(Vec3 pos, float radius, ParticleType<?> particleType) {
        if (Minecraft.getInstance().player == null || !(particleType instanceof ParticleOptions)) {
            return;
        }
        Level level = Minecraft.getInstance().player.level;
        int count = (int)((float)Math.PI * 2 * radius) * 2;
        float angle = 360.0f / (float)count * ((float)Math.PI / 180);
        for (int i = 0; i < count; ++i) {
            Vec3 motion = new Vec3((double)(Mth.cos((float)(angle * (float)i)) * radius), 0.0, (double)(Mth.sin((float)(angle * (float)i)) * radius)).scale((double)Utils.random.nextIntBetweenInclusive(50, 70) * 0.00155);
            level.addParticle((ParticleOptions)particleType, pos.x + motion.x * 4.0, pos.y, pos.z + motion.z * 4.0, motion.x, motion.y, motion.z);
        }
    }

    public static void handleClientsideHealParticles(Vec3 pos) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Level level = Minecraft.getInstance().player.level;
            for (int j = 0; j < 15; ++j) {
                level.addParticle((ParticleOptions)ClientSpellCastHelper.coloredMobEffect(((MobEffect)MobEffects.HEAL.value()).getColor()), pos.x + Utils.getRandomScaled(0.25), pos.y + Utils.getRandomScaled(1.0) + 1.0, pos.z + Utils.getRandomScaled(0.25), Utils.getRandomScaled(0.005), Utils.getRandomScaled(0.025), Utils.getRandomScaled(0.005));
            }
        }
    }

    public static ColorParticleOption coloredMobEffect(int color) {
        double d0 = (double)(color >> 16 & 0xFF) / 255.0;
        double d1 = (double)(color >> 8 & 0xFF) / 255.0;
        double d2 = (double)(color >> 0 & 0xFF) / 255.0;
        return ColorParticleOption.create((ParticleType)ParticleTypes.ENTITY_EFFECT, (float)((float)d0), (float)((float)d1), (float)((float)d2));
    }

    public static void handleClientsideAbsorptionParticles(Vec3 pos) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Level level = Minecraft.getInstance().player.level;
            for (int j = 0; j < 15; ++j) {
                level.addParticle((ParticleOptions)ClientSpellCastHelper.coloredMobEffect(((MobEffect)MobEffectRegistry.FORTIFY.get()).getColor()), pos.x + Utils.getRandomScaled(0.25), pos.y + Utils.getRandomScaled(1.0), pos.z + Utils.getRandomScaled(0.25), 0.0, 0.0, 0.0);
            }
        }
    }

    public static void handleClientboundOakskinParticles(Vec3 pos) {
        LocalPlayer player = Minecraft.getInstance().player;
        RandomSource randomsource = player.getRandom();
        for (int i = 0; i < 50; ++i) {
            double d0 = Mth.randomBetween((RandomSource)randomsource, (float)-0.5f, (float)0.5f);
            double d1 = Mth.randomBetween((RandomSource)randomsource, (float)0.0f, (float)2.0f);
            double d2 = Mth.randomBetween((RandomSource)randomsource, (float)-0.5f, (float)0.5f);
            ParticleOptions particleType = randomsource.nextFloat() < 0.1f ? ParticleHelper.FIREFLY : new BlockParticleOption(ParticleTypes.BLOCK, Blocks.OAK_WOOD.defaultBlockState());
            player.level.addParticle(particleType, pos.x + d0, pos.y + d1, pos.z + d2, d0 * 0.05, 0.05, d2 * 0.05);
        }
    }

    public static void handleClientsideRegenCloudParticles(Vec3 pos) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Level level = player.level;
            int ySteps = 16;
            int xSteps = 48;
            float yDeg = 180.0f / (float)ySteps * ((float)Math.PI / 180);
            float xDeg = 360.0f / (float)xSteps * ((float)Math.PI / 180);
            for (int x = 0; x < xSteps; ++x) {
                for (int y = 0; y < ySteps; ++y) {
                    Vec3 offset = new Vec3(0.0, 0.0, 5.0).yRot((float)y * yDeg).xRot((float)x * xDeg).zRot(-1.5707964f).multiply(1.0, (double)0.85f, 1.0);
                    level.addParticle((ParticleOptions)ClientSpellCastHelper.coloredMobEffect(((MobEffect)MobEffects.HEAL.value()).getColor()), pos.x + offset.x, pos.y + offset.y, pos.z + offset.z, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    public static void handleClientsideFortifyAreaParticles(Vec3 pos) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Level level = player.level;
            int ySteps = 128;
            float yDeg = 180.0f / (float)ySteps * ((float)Math.PI / 180);
            for (int y = 0; y < ySteps; ++y) {
                Vec3 offset = new Vec3(0.0, 0.0, 8.0).yRot((float)y * yDeg);
                Vec3 motion = new Vec3(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).scale(0.1);
                level.addParticle(ParticleHelper.WISP, pos.x + offset.x, 1.0 + pos.y + offset.y, pos.z + offset.z, motion.x, motion.y, motion.z);
            }
        }
    }

    public static void handleClientboundOnClientCast(String spellId, int level, CastSource castSource, ICastData castData) {
        AbstractSpell spell = SpellRegistry.getSpell(spellId);
        spell.onClientCast(Minecraft.getInstance().player.level, level, (LivingEntity)Minecraft.getInstance().player, castData);
    }

    public static void handleClientboundTeleport(Vec3 pos1, Vec3 pos2) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Level level = Minecraft.getInstance().player.level;
            TeleportSpell.particleCloud(level, pos1);
            TeleportSpell.particleCloud(level, pos2);
        }
    }

    public static void handleClientboundFieryExplosion(Vec3 pos, float radius) {
        MinecraftInstanceHelper.ifPlayerPresent(player -> {
            Vec3 motion;
            int i;
            Vec3 posOffset;
            Level level = player.level;
            double x = pos.x;
            double y = pos.y;
            double z = pos.z;
            level.addParticle((ParticleOptions)new BlastwaveParticleOptions(new Vector3f(1.0f, 0.6f, 0.3f), radius + 1.0f), x, y, z, 0.0, 0.0, 0.0);
            int c = (int)(6.28 * (double)radius) * 2;
            float step = 360.0f / (float)c * ((float)Math.PI / 180);
            float speed = (0.06f + 0.01f * radius) * 2.0f;
            for (int i2 = 0; i2 < c; ++i2) {
                Vec3 vec3 = new Vec3((double)Mth.cos((float)(step * (float)i2)), 0.0, (double)Mth.sin((float)(step * (float)i2))).scale((double)speed);
                posOffset = Utils.getRandomVec3(0.5).add(vec3.scale(10.0));
                vec3 = vec3.add(Utils.getRandomVec3(0.01));
                level.addParticle(ParticleHelper.FIERY_SMOKE, x + posOffset.x, y + posOffset.y, z + posOffset.z, vec3.x, vec3.y, vec3.z);
            }
            int cloudDensity = 50 + (int)(25.0f * radius);
            for (i = 0; i < cloudDensity; ++i) {
                posOffset = Utils.getRandomVec3(1.0).scale((double)(radius * 0.125f));
                motion = posOffset.normalize().scale((double)(speed * 0.5f));
                posOffset = posOffset.add(motion.scale(Utils.getRandomScaled(1.0)));
                motion = motion.add(Utils.getRandomVec3(speed * 0.1f));
                level.addParticle(ParticleHelper.FIERY_SMOKE, x + posOffset.x, y + posOffset.y, z + posOffset.z, motion.x, motion.y, motion.z);
            }
            for (i = 0; i < cloudDensity; i += 2) {
                posOffset = Utils.getRandomVec3(1.0).scale((double)(radius * 0.4f));
                motion = posOffset.normalize().scale((double)(speed * 0.5f));
                motion = motion.add(Utils.getRandomVec3(0.25));
                level.addParticle(ParticleHelper.EMBERS, true, x + posOffset.x, y + posOffset.y, z + posOffset.z, motion.x, motion.y, motion.z);
                level.addParticle(ParticleHelper.FIRE, x + posOffset.x * 0.5, y + posOffset.y * 0.5, z + posOffset.z * 0.5, motion.x, motion.y, motion.z);
            }
            for (i = 0; i < cloudDensity; i += 2) {
                posOffset = Utils.getRandomVec3(radius).scale((double)0.2f);
                motion = posOffset.normalize().scale(0.8);
                motion = motion.add(Utils.getRandomVec3(0.18));
                level.addParticle(ParticleHelper.FIERY_SPARKS, x + posOffset.x * 0.5, y + posOffset.y * 0.5, z + posOffset.z * 0.5, motion.x, motion.y, motion.z);
            }
        });
    }

    public static void handleClientboundFrostStep(Vec3 pos1, Vec3 pos2) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Level level = Minecraft.getInstance().player.level;
            FrostStepSpell.particleCloud(level, pos1);
            FrostStepSpell.particleCloud(level, pos2);
        }
    }

    public static void handleClientBoundOnCastStarted(UUID castingEntityId, String spellId, int spellLevel) {
        Player player = Minecraft.getInstance().player.level.getPlayerByUUID(castingEntityId);
        AbstractSpell spell = SpellRegistry.getSpell(spellId);
        spell.getCastStartAnimation().getForPlayer().ifPresent(resourceLocation -> ClientSpellCastHelper.animatePlayerStart(player, resourceLocation));
        spell.onClientPreCast(player.level, spellLevel, (LivingEntity)player, player.getUsedItemHand(), null);
    }

    public static void handleClientBoundOnCastFinished(UUID castingEntityId, String spellId, boolean cancelled) {
        ModifierLayer animation;
        ClientMagicData.resetClientCastState(castingEntityId);
        Player player = Minecraft.getInstance().player.level.getPlayerByUUID(castingEntityId);
        AbstractSpell spell = SpellRegistry.getSpell(spellId);
        AnimationHolder finishAnimation = spell.getCastFinishAnimation();
        if (finishAnimation.getForPlayer().isPresent() && !cancelled) {
            ClientSpellCastHelper.animatePlayerStart(player, finishAnimation.getForPlayer().get());
        } else if ((finishAnimation != AnimationHolder.pass() || cancelled) && (animation = (ModifierLayer)PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer)((AbstractClientPlayer)player)).get(SpellAnimations.ANIMATION_RESOURCE)) != null) {
            animation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn((int)4, (Ease)Ease.INOUTSINE), null, false);
            IronsAdjustmentModifier.INSTANCE.fadeOut(5);
        }
        if (cancelled && spell.stopSoundOnCancel()) {
            spell.getCastStartSound().ifPresent(soundEvent -> Minecraft.getInstance().getSoundManager().stop(soundEvent.getLocation(), null));
        }
        if (castingEntityId.equals(Minecraft.getInstance().player.getUUID()) && ClientInputEvents.isUseKeyDown) {
            ClientInputEvents.hasReleasedSinceCasting = false;
        }
    }

    public static void animatePlayerStart(Player player, ResourceLocation resourceLocation) {
        IPlayable rawanimation = PlayerAnimationRegistry.getAnimation((ResourceLocation)resourceLocation);
        if (rawanimation instanceof KeyframeAnimation) {
            KeyframeAnimation keyframeAnimation = (KeyframeAnimation)rawanimation;
            ModifierLayer playerAnimationData = (ModifierLayer)PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer)((AbstractClientPlayer)player)).get(SpellAnimations.ANIMATION_RESOURCE);
            if (playerAnimationData != null) {
                KeyframeAnimationPlayer animation = new KeyframeAnimationPlayer(keyframeAnimation){

                    public void tick() {
                        if (this.getCurrentTick() == this.getStopTick() - 2) {
                            IronsAdjustmentModifier.INSTANCE.fadeOut(3);
                        }
                        super.tick();
                    }
                };
                Boolean armsFlag = (Boolean)ClientConfigs.SHOW_FIRST_PERSON_ARMS.get();
                Boolean itemsFlag = (Boolean)ClientConfigs.SHOW_FIRST_PERSON_ITEMS.get();
                if (armsFlag.booleanValue() || itemsFlag.booleanValue()) {
                    animation.setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);
                    animation.setFirstPersonConfiguration(new FirstPersonConfiguration(armsFlag.booleanValue(), armsFlag.booleanValue(), itemsFlag.booleanValue(), itemsFlag.booleanValue()));
                } else {
                    animation.setFirstPersonMode(FirstPersonMode.DISABLED);
                }
                playerAnimationData.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn((int)2, (Ease)Ease.INOUTSINE), (IAnimation)animation, true);
            }
        }
    }
}

