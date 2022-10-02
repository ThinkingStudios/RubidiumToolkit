package com.texstudio.rubidium_toolkit.features.Zoom.api.modifiers;

import com.texstudio.rubidium_toolkit.features.Zoom.api.MouseModifier;
import com.texstudio.rubidium_toolkit.features.Zoom.api.OkZoomerAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.SmoothDouble;

/**
 * An implemenation of Vanilla's Cinematic Camera as a mouse modifier
 */
public class CinematicCameraMouseModifier implements MouseModifier {
    private static final ResourceLocation MODIFIER_ID = new ResourceLocation(OkZoomerAPI.MOD_ID, "cinematic_camera");
    private boolean active;
    private final Minecraft mc;
    private boolean cinematicCameraEnabled;
    private final SmoothDouble cursorXZoomSmoother = new SmoothDouble();
    private final SmoothDouble cursorYZoomSmoother = new SmoothDouble();

    /**
     * Initializes an instance of the cinematic camera mouse modifier
    */
    public CinematicCameraMouseModifier() {
        this.active = false;
        this.mc = Minecraft.getInstance();
    }
    
    @Override
    public ResourceLocation getId() {
        return MODIFIER_ID;
    }

    @Override
    public boolean getActive() {
        return this.active;
    }

    @Override
    public double applyXModifier(double cursorDeltaX, double cursorSensitivity, double mouseUpdateTimeDelta, double targetDivisor, double transitionMultiplier) {
        if (this.cinematicCameraEnabled) {
            this.cursorXZoomSmoother.reset();
            return cursorDeltaX;
        }
        double smoother = mouseUpdateTimeDelta * cursorSensitivity;
        return this.cursorXZoomSmoother.getNewDeltaValue(cursorDeltaX, smoother);
    }

    @Override
    public double applyYModifier(double cursorDeltaY, double cursorSensitivity, double mouseUpdateTimeDelta, double targetDivisor, double transitionMultiplier) {
        if (this.cinematicCameraEnabled) {
            this.cursorYZoomSmoother.reset();
            return cursorDeltaY;
        }
        double smoother = mouseUpdateTimeDelta * cursorSensitivity;
        return this.cursorYZoomSmoother.getNewDeltaValue(cursorDeltaY, smoother);
    }

    @Override
    public void tick(boolean active) {
        this.cinematicCameraEnabled = this.mc.options.smoothCamera;
        if (!active && this.active) {
            this.cursorXZoomSmoother.reset();
            this.cursorYZoomSmoother.reset();
        }
        this.active = active;
    }
}