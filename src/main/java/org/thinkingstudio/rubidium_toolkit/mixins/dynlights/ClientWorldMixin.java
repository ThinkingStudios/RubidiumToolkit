package org.thinkingstudio.rubidium_toolkit.mixins.dynlights;

import org.thinkingstudio.rubidium_toolkit.features.dynlights.DynamicLightSource;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(method = "finishRemovingEntity", at = @At("RETURN"))
    private void onFinishRemovingEntity(Entity entity, CallbackInfo ci) {
        DynamicLightSource dls = (DynamicLightSource) entity;
        dls.setDynamicLightEnabled(false);
    }
}
