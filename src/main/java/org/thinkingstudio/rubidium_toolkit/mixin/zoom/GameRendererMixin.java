package org.thinkingstudio.rubidium_toolkit.mixin.zoom;

import org.thinkingstudio.rubidium_toolkit.features.zoom.APIImpl;
import org.thinkingstudio.rubidium_toolkit.features.zoom.api.ZoomInstance;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(
        at = @At("HEAD"),
        method = "tick()V"
    )
    private void okzoomer$tickInstances(CallbackInfo info) {
        boolean iterateZoom = false;
        boolean iterateTransitions = false;
        boolean iterateModifiers = false;
        boolean iterateOverlays = false;
        
        for (ZoomInstance instance : APIImpl.getZoomInstances()) {
            boolean zoom = instance.getZoom();
            if (zoom || (instance.isTransitionActive() || instance.isOverlayActive())) {
                double divisor = zoom ? instance.getZoomDivisor() : 1.0;
                if (instance.getZoomOverlay() != null) {
                    instance.getZoomOverlay().tick(zoom, divisor, instance.getTransitionMode().getInternalMultiplier());
                }
                instance.getTransitionMode().tick(zoom, divisor);
            }

            iterateZoom = iterateZoom || zoom;
            iterateTransitions = iterateTransitions || instance.isTransitionActive();
            iterateModifiers = iterateModifiers || instance.isModifierActive();
            iterateOverlays = iterateOverlays || instance.isOverlayActive();
        }

        APIImpl.setIterateZoom(iterateZoom);
        APIImpl.setIterateTransitions(iterateTransitions);
        APIImpl.setIterateModifiers(iterateModifiers);
        APIImpl.setIterateOverlays(iterateOverlays);
    }

    @Inject(
        at = @At("RETURN"),
        method = "getFov(Lnet/minecraft/client/Camera;FZ)D",
        cancellable = true
    )
    private void okzoomer$getZoomedFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        double fov = cir.getReturnValue();
        double zoomedFov = fov;
        
        if (APIImpl.shouldIterateTransitions()) {
            for (ZoomInstance instance : APIImpl.getZoomInstances()) {
                if (instance.isTransitionActive()) {
                    zoomedFov = instance.getTransitionMode().applyZoom(zoomedFov, tickDelta);   
                }
            }
        }

        if (fov != zoomedFov) {
            cir.setReturnValue(zoomedFov);
        }
    }
}
