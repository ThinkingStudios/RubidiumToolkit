package org.thinkingstudio.rubidium_toolkit.features.zoom.zoom;

import org.thinkingstudio.rubidium_toolkit.features.zoom.api.MouseModifier;
import org.thinkingstudio.rubidium_toolkit.features.zoom.api.OkZoomerAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.SmoothDouble;

// The implementation of the multiplied cinematic camera
public class MultipliedCinematicCameraMouseModifier implements MouseModifier {
    private static final ResourceLocation MODIFIER_ID = new ResourceLocation(OkZoomerAPI.MOD_ID, "multiplied_cinematic_camera");
    private boolean active;
    private final Minecraft client;
    private boolean cinematicCameraEnabled;
    private final SmoothDouble cursorXZoomSmoother = new SmoothDouble();
    private final SmoothDouble cursorYZoomSmoother = new SmoothDouble();
    private double cinematicCameraMultiplier;

    public MultipliedCinematicCameraMouseModifier(double cinematicCameraMultiplier) {
        this.cinematicCameraMultiplier = cinematicCameraMultiplier;
        this.client = Minecraft.getInstance();
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
        double smoother = mouseUpdateTimeDelta * cinematicCameraMultiplier * cursorSensitivity;
        return this.cursorXZoomSmoother.getNewDeltaValue(cursorDeltaX, smoother);
    }

    @Override
    public double applyYModifier(double cursorDeltaY, double cursorSensitivity, double mouseUpdateTimeDelta, double targetDivisor, double transitionMultiplier) {
        if (this.cinematicCameraEnabled) {
            this.cursorYZoomSmoother.reset();
            return cursorDeltaY;
        }
        double smoother = mouseUpdateTimeDelta * cinematicCameraMultiplier * cursorSensitivity;
        return this.cursorYZoomSmoother.getNewDeltaValue(cursorDeltaY, smoother);
    }

    @Override
    public void tick(boolean active) {
        this.cinematicCameraEnabled = this.client.options.smoothCamera;
        if (!active && active != this.active) {
            this.cursorXZoomSmoother.reset();
            this.cursorYZoomSmoother.reset();
        }
        this.active = active;
    }
}
