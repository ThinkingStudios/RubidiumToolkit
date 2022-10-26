package com.texstudio.rubidium_toolkit.mixins.DynamicLights;

import com.texstudio.rubidium_toolkit.DynamicLightSource;
import com.texstudio.rubidium_toolkit.features.DynamicLights.DynamicLightsFeature;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends Entity implements DynamicLightSource
{
    public AbstractMinecartEntityMixin(EntityType<?> type, World world)
    {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci)
    {
        // We do not want to update the entity on the server.
        if (this.getCommandSenderWorld().isClientSide()) {
            if (this.removed) {
                this.setDynamicLightEnabled(false);
            } else {
                this.dynamicLightTick();
                DynamicLightsFeature.updateTracking(this);
            }
        }
    }
}
