package org.thinkingstudio.rubidium_toolkit.mixins.DynamicLights;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.thinkingstudio.rubidium_toolkit.features.DynamicLights.DynamicLightSource;

@Mixin(ClientWorld.class)
public class ClientWorldMixin
{
    @Inject(method = "onEntityRemoved", at = @At("RETURN"))
    private void onFinishRemovingEntity(Entity entity, CallbackInfo ci)
    {
        DynamicLightSource dls = (DynamicLightSource) entity;
        dls.setDynamicLightEnabled(false);
    }
}