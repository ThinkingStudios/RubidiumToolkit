package org.thinkingstudio.rubidium_toolkit.mixin.zoom;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfigClient;
import org.thinkingstudio.rubidium_toolkit.features.zoom.APIImpl;
import org.thinkingstudio.rubidium_toolkit.features.zoom.ZoomKeyBinds;
import org.thinkingstudio.rubidium_toolkit.features.zoom.api.ZoomInstance;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.RubidiumToolkitNetwork;
import org.thinkingstudio.rubidium_toolkit.config.ConfigEnums;
import org.thinkingstudio.rubidium_toolkit.features.zoom.utils.ZoomUtils;
import net.minecraft.client.MouseHandler;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {

    @Unique
    private boolean modifyMouse;

    @Unique
    private double finalCursorDeltaX;

    @Unique
    private double finalCursorDeltaY;

    @Inject(
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/Options;invertYMouse:Z",
            opcode = Opcodes.GETFIELD
        ),
        method = "turnPlayer()V",
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void okzoomer$applyZoomChanges(CallbackInfo ci, double d, double e, double k, double l, double f, double g, double h, int m) {
        this.modifyMouse = false;
        if (APIImpl.shouldIterateZoom() || APIImpl.shouldIterateModifiers()) {
            for (ZoomInstance instance : APIImpl.getZoomInstances()) {
                if (instance.getMouseModifier() != null) {
                    boolean zoom = instance.getZoom();
                    if (zoom || instance.isModifierActive()) {
                        instance.getMouseModifier().tick(zoom);
                        double zoomDivisor = zoom ? instance.getZoomDivisor() : 1.0;
                        double transitionDivisor = instance.getTransitionMode().getInternalMultiplier();
                        k = instance.getMouseModifier().applyXModifier(k, h, e, zoomDivisor, transitionDivisor);
                        l = instance.getMouseModifier().applyYModifier(l, h, e, zoomDivisor, transitionDivisor);
                        this.modifyMouse = true;
                    }
                }
            }
        }
        this.finalCursorDeltaX = k;
        this.finalCursorDeltaY = l;
    }

    @ModifyVariable(
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/Options;invertYMouse:Z",
            opcode = Opcodes.GETFIELD
        ),
        method = "turnPlayer()V",
        ordinal = 2
    )
    private double okzoomer$modifyFinalCursorDeltaX(double k) {
        if (!this.modifyMouse) return k;
        return finalCursorDeltaX;
    }

    @ModifyVariable(
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/Options;invertYMouse:Z",
            opcode = Opcodes.GETFIELD
        ),
        method = "turnPlayer()V",
        ordinal = 3
    )
    private double okzoomer$modifyFinalCursorDeltaY(double l) {
        if (!this.modifyMouse) return l;
        return finalCursorDeltaY;
    }

    @Shadow
    private double accumulatedScroll;

    // Handles zoom scrolling
    @Inject(
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/MouseHandler;accumulatedScroll:D", ordinal = 7),
            method = "onScroll",
            cancellable = true
    )
    private void okzoomer$handleZoom(long l, double d, double e, CallbackInfo info) {
        if (this.accumulatedScroll != 0.0) {
            if (RubidiumToolkitConfigClient.ALLOW_SCROLLING.get() && !RubidiumToolkitNetwork.getDisableZoomScrolling()) {
                if (RubidiumToolkitConfigClient.ZOOM_MODE.get().equals(ConfigEnums.ZoomModes.PERSISTENT)) {
                    if (!ZoomKeyBinds.ZOOM_KEY.isDown()) return;
                }

                if (ZoomUtils.ZOOMER_ZOOM.getZoom()) {
                    ZoomUtils.changeZoomDivisor(this.accumulatedScroll > 0.0);
                    info.cancel();
                }
            }
        }
    }

    // Prevents the spyglass from working if zooming replaces its zoom
    @ModifyExpressionValue(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isScoping()Z"),
            method = "turnPlayer"
    )
    private boolean okzoomer$replaceSpyglassMouseMovement(boolean isUsingSpyglass) {
        if (switch (RubidiumToolkitNetwork.getSpyglassDependency()) {
            case REPLACE_ZOOM, BOTH -> true;
            default -> false;
        }) {
            return false;
        } else {
            return isUsingSpyglass;
        }
    }
}
