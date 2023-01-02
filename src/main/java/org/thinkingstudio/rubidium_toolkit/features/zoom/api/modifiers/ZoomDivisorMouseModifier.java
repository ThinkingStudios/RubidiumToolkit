package org.thinkingstudio.rubidium_toolkit.features.zoom.api.modifiers;

import org.thinkingstudio.rubidium_toolkit.features.zoom.api.OkZoomerAPI;
import org.thinkingstudio.rubidium_toolkit.features.zoom.api.MouseModifier;
import net.minecraft.resources.ResourceLocation;

/**
 * A mouse modifier which reduces the cursor sensitivity with the transition mode's internal multiplier
 */
public class ZoomDivisorMouseModifier implements MouseModifier {
    private static final ResourceLocation MODIFIER_ID = new ResourceLocation(OkZoomerAPI.MOD_ID,"zoom_divisor");
    private boolean active;

    /**
     * Initializes an instance of the zoom divisor mouse modifier
    */
    public ZoomDivisorMouseModifier() {
        this.active = false;
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
        return cursorDeltaX * (this.active ? transitionMultiplier : 1.0);
    }

    @Override
    public double applyYModifier(double cursorDeltaY, double cursorSensitivity, double mouseUpdateTimeDelta, double targetDivisor, double transitionMultiplier) {
        return cursorDeltaY * (this.active ? transitionMultiplier : 1.0);
    }

    @Override
    public void tick(boolean active) {
        this.active = active;
    }
}
