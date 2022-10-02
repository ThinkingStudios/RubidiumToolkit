package com.texstudio.rubidium_toolkit.mixin.DynLights.lightsource;

import com.texstudio.rubidium_toolkit.features.DynLights.DynamicLightSource;
import com.texstudio.rubidium_toolkit.features.DynLights.RubidiumDynLights;
import com.texstudio.rubidium_toolkit.features.DynLights.api.DynamicLightHandlers;
import com.texstudio.rubidium_toolkit.features.DynLights.config.DynamicLightsConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HangingEntity.class)
public abstract class AbstractDecorationEntityMixin extends Entity implements DynamicLightSource {
	public AbstractDecorationEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void onTick(CallbackInfo ci) {
		// We do not want to update the entity on the server.
		if (this.getCommandSenderWorld().isClientSide()) {
			if (this.isRemoved()) {
				this.setDynamicLightEnabled(false);
			} else {
				if (!DynamicLightsConfig.TileEntityLighting.get() || !DynamicLightHandlers.canLightUp(this))
					this.resetDynamicLight();
				else
					this.dynamicLightTick();
				RubidiumDynLights.updateTracking(this);
			}
		}
	}
}
