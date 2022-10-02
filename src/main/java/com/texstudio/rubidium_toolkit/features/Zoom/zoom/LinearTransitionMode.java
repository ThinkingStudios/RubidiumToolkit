package com.texstudio.rubidium_toolkit.features.Zoom.zoom;

import com.texstudio.rubidium_toolkit.features.Zoom.api.OkZoomerAPI;
import com.texstudio.rubidium_toolkit.features.Zoom.api.TransitionMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

// The implementation of the linear transition
public class LinearTransitionMode implements TransitionMode {
    private static final ResourceLocation TRANSITION_ID = new ResourceLocation(OkZoomerAPI.MOD_ID, "linear_transition");
    private boolean active;
    private double minimumLinearStep;
    private double maximumLinearStep;
    private double linearStep;
    private double fovMultiplier;
    private float internalMultiplier;
    private float lastInternalMultiplier;

    public LinearTransitionMode(double minimumLinearStep, double maximumLinearStep) {
        this.active = false;
        this.minimumLinearStep = minimumLinearStep;
        this.maximumLinearStep = maximumLinearStep;
        this.internalMultiplier = 1.0F;
        this.lastInternalMultiplier = 1.0F;
    }

    @Override
    public ResourceLocation getId() {
        return TRANSITION_ID;
    }

    @Override
    public boolean getActive() {
        return this.active;
    }

    @Override
    public double applyZoom(double fov, float tickDelta) {
        fovMultiplier = Mth.lerp(tickDelta, this.lastInternalMultiplier, this.internalMultiplier);
        return fov * fovMultiplier;
    }

    @Override
    public void tick(boolean active, double divisor) {
        double zoomMultiplier = 1.0D / divisor;

        this.lastInternalMultiplier = this.internalMultiplier;

        this.linearStep = Mth.clamp(zoomMultiplier, this.minimumLinearStep, this.maximumLinearStep);
        this.internalMultiplier = Mth.approach((float)this.internalMultiplier, (float)zoomMultiplier, (float)linearStep);

        if ((!active && fovMultiplier == this.internalMultiplier) || active) {
            this.active = active;
        }
    }

    @Override
    public double getInternalMultiplier() {
        return this.internalMultiplier;
    }
}
