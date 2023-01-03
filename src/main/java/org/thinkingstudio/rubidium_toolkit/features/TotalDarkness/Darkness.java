package org.thinkingstudio.rubidium_toolkit.features.TotalDarkness;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.thinkingstudio.rubidium_toolkit.RubidiumToolkit;
import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfig;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;


@Mod.EventBusSubscriber(modid = RubidiumToolkit.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class Darkness
{
	@SubscribeEvent
	public static void onConfigChange(ModConfig.ModConfigEvent e) {
		bake();
	}

	public static void bake() {
		RubidiumToolkitConfig.darkNetherFogEffective = RubidiumToolkitConfig.darkNether.get() ? RubidiumToolkitConfig.darkNetherFogConfigured.get() : 1.0;
		RubidiumToolkitConfig.darkEndFogEffective = RubidiumToolkitConfig.darkEnd.get() ? RubidiumToolkitConfig.darkEndFogConfigured.get() : 1.0;
	}

	public static boolean blockLightOnly() {
		return RubidiumToolkitConfig.blockLightOnly.get();
	}

	public static double darkNetherFog() {
		return RubidiumToolkitConfig.darkNetherFogEffective;
	}

	public static double darkEndFog() {
		return RubidiumToolkitConfig.darkEndFogEffective;
	}

	public static void getDarkenedFogColor(CallbackInfoReturnable<Vector3d> ci, double factor)
	{
		if (factor != 1.0) {
			Vector3d result = ci.getReturnValue();
			double MIN = 0.03D;
			result = new Vector3d(Math.max(MIN, result.x * factor), Math.max(MIN, result.y * factor), Math.max(MIN, result.z * factor));
			ci.setReturnValue(result);
		}
	}

	private static boolean isDark(World world) {
		if (!RubidiumToolkitConfig.trueDarknessEnabled.get())
			return false;

		final RegistryKey<World> dimType = world.getRegistryKey();
		if (dimType == World.OVERWORLD) {
			return RubidiumToolkitConfig.darkOverworld.get();
		} else if (dimType == World.NETHER) {
			return RubidiumToolkitConfig.darkNether.get();
		} else if (dimType == World.END) {
			return RubidiumToolkitConfig.darkEnd.get();
		} else if (world.getDimension().hasSkyLight()) {
			return RubidiumToolkitConfig.darkDefault.get();
		} else {
			return RubidiumToolkitConfig.darkSkyless.get();
		}
	}

	private static float skyFactor(World world) {
		if (!RubidiumToolkitConfig.blockLightOnly.get() && isDark(world)) {
			if (world.getDimension().hasSkyLight()) {
				final float angle = world.getSkyAngle(0);
				if (angle > 0.25f && angle < 0.75f) {
					final float oldWeight = Math.max(0, (Math.abs(angle - 0.5f) - 0.2f)) * 20;
					final float moon = RubidiumToolkitConfig.ignoreMoonPhase.get() ? 0 : world.getMoonSize();
					final float moonInterpolated = (float) MathHelper.lerp(moon, RubidiumToolkitConfig.minimumMoonLevel.get(), RubidiumToolkitConfig.maximumMoonLevel.get());
					return MathHelper.lerp(oldWeight * oldWeight * oldWeight, moonInterpolated, 1f) ;
				} else {
					return 1;
				}
			} else {
				return 0;
			}
		} else {
			return 1;
		}
	}

	public static boolean enabled = false;
	private static final float[][] LUMINANCE = new float[16][16];

	public static int darken(int c, int blockIndex, int skyIndex) {
		final float lTarget = LUMINANCE[blockIndex][skyIndex];
		final float r = (c & 0xFF) / 255f;
		final float g = ((c >> 8) & 0xFF) / 255f;
		final float b = ((c >> 16) & 0xFF) / 255f;
		final float l = luminance(r, g, b);
		final float f = l > 0 ? Math.min(1, lTarget / l) : 0;

		return f == 1f ? c : 0xFF000000 | Math.round(f * r * 255) | (Math.round(f * g * 255) << 8) | (Math.round(f * b * 255) << 16);
	}

	public static float luminance(float r, float g, float b) {
		return r * 0.2126f + g * 0.7152f + b * 0.0722f;
	}

	public static void updateLuminance(float tickDelta, MinecraftClient client, GameRenderer worldRenderer, float prevFlicker) {
		final ClientWorld world = client.world;
		if (world != null) {

			if (!isDark(world) || client.player.hasStatusEffect(StatusEffects.NIGHT_VISION) ||
							(client.player.hasStatusEffect(StatusEffects.CONDUIT_POWER) && client.player.getUnderwaterVisibility() > 0) || world.getLightningTicksLeft() > 0) {
				enabled = false;
				return;
			} else {
				enabled = true;
			}

			final float dimSkyFactor = Darkness.skyFactor(world);
			final float ambient = world.method_23783(1.0F);
			final DimensionType dim = world.getDimension();
			final boolean blockAmbient = !Darkness.isDark(world);

			for (int skyIndex = 0; skyIndex < 16; ++skyIndex) {
				float skyFactor = 1f - skyIndex / 15f;
				skyFactor = 1 - skyFactor * skyFactor * skyFactor * skyFactor;
				skyFactor *= dimSkyFactor;

				float min = Math.max(skyFactor * 0.05f, RubidiumToolkitConfig.darknessOption.get().value);
				final float rawAmbient = ambient * skyFactor;
				final float minAmbient = rawAmbient * (1 - min) + min;
				final float skyBase = dim.method_28516(skyIndex) * minAmbient;

				min = Math.max(0.35f * skyFactor, RubidiumToolkitConfig.darknessOption.get().value);
				float v = skyBase * (rawAmbient * (1 - min) + min);
				float skyRed = v;
				float skyGreen = v;
				float skyBlue = skyBase;

				if (worldRenderer.getSkyDarkness(tickDelta) > 0.0F) {
					final float skyDarkness = worldRenderer.getSkyDarkness(tickDelta);
					skyRed = skyRed * (1.0F - skyDarkness) + skyRed * 0.7F * skyDarkness;
					skyGreen = skyGreen * (1.0F - skyDarkness) + skyGreen * 0.6F * skyDarkness;
					skyBlue = skyBlue * (1.0F - skyDarkness) + skyBlue * 0.6F * skyDarkness;
				}

				for (int blockIndex = 0; blockIndex < 16; ++blockIndex) {
					float blockFactor = 1f;
					if (!blockAmbient) {
						blockFactor = 1f - blockIndex / 15f;
						blockFactor = 1 - blockFactor * blockFactor * blockFactor * blockFactor;
					}

					final float blockBase = blockFactor * dim.method_28516(blockIndex) * (prevFlicker * 0.1F + 1.5F);
					min = 0.4f * blockFactor;
					final float blockGreen = blockBase * ((blockBase * (1 - min) + min) * (1 - min) + min);
					final float blockBlue = blockBase * (blockBase * blockBase * (1 - min) + min);

					float red = skyRed + blockBase;
					float green = skyGreen + blockGreen;
					float blue = skyBlue + blockBlue;

					final float f = Math.max(skyFactor, blockFactor);
					min = 0.03f * f;
					red = red * (0.99F - min) + min;
					green = green * (0.99F - min) + min;
					blue = blue * (0.99F - min) + min;

					//the end
					if (world.getRegistryKey() == World.END) {
						red = skyFactor * 0.22F + blockBase * 0.75f;
						green = skyFactor * 0.28F + blockGreen * 0.75f;
						blue = skyFactor * 0.25F + blockBlue * 0.75f;
					}

					if (red > 1.0F) {
						red = 1.0F;
					}

					if (green > 1.0F) {
						green = 1.0F;
					}

					if (blue > 1.0F) {
						blue = 1.0F;
					}

					final float gamma = (float) client.options.gamma * f;
					float invRed = 1.0F - red;
					float invGreen = 1.0F - green;
					float invBlue = 1.0F - blue;
					invRed = 1.0F - invRed * invRed * invRed * invRed;
					invGreen = 1.0F - invGreen * invGreen * invGreen * invGreen;
					invBlue = 1.0F - invBlue * invBlue * invBlue * invBlue;
					red = red * (1.0F - gamma) + invRed * gamma;
					green = green * (1.0F - gamma) + invGreen * gamma;
					blue = blue * (1.0F - gamma) + invBlue * gamma;

					min = Math.max(0.03f * f, RubidiumToolkitConfig.darknessOption.get().value);
					red = red * (0.99F - min) + min;
					green = green * (0.99F - min) + min;
					blue = blue * (0.99F - min) + min;

					if (red > 1.0F) {
						red = 1.0F;
					}

					if (green > 1.0F) {
						green = 1.0F;
					}

					if (blue > 1.0F) {
						blue = 1.0F;
					}

					if (red < 0.0F) {
						red = 0.0F;
					}

					if (green < 0.0F) {
						green = 0.0F;
					}

					if (blue < 0.0F) {
						blue = 0.0F;
					}

					LUMINANCE[blockIndex][skyIndex] = Darkness.luminance(red, green, blue);
				}
			}
		}
	} 
}
