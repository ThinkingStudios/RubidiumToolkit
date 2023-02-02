package org.thinkingstudio.rubidium_toolkit.mixin.zoom;

import net.minecraft.client.Options;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.thinkingstudio.rubidium_toolkit.toolkit.features.zoom.WiZoom;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/client/Options;fov:D"), method = "getFov")
    private double getFov(Options instance) {
        return WiZoom.INSTANCE.changeFovBasedOnZoom(instance.fov);
    }
}