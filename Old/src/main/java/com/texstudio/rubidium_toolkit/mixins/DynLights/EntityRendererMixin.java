package com.texstudio.rubidium_toolkit.mixins.DynLights;

import com.texstudio.rubidium_toolkit.dynamic_lights.DynamicLightSource;
import com.texstudio.rubidium_toolkit.dynamic_lights.RubidiumDynamicLights;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity>
{
    @Inject(method = "getBlockLightLevel", at = @At("RETURN"), cancellable = true)
    private void onGetBlockLight(T entity, BlockPos pos, CallbackInfoReturnable<Integer> cir)
    {
        if (!RubidiumDynamicLights.isEnabled())
            return; // Do not touch to the value.

        int vanilla = cir.getReturnValueI();
        int entityLuminance = ((DynamicLightSource) entity).getLuminance();
        if (entityLuminance >= 15)
            cir.setReturnValue(entityLuminance);

        int posLuminance = (int) RubidiumDynamicLights.getDynamicLightLevel(pos);

        cir.setReturnValue(Math.max(Math.max(vanilla, entityLuminance), posLuminance));
    }
}
