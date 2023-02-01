package org.thinkingstudio.rubidium_toolkit.config;

import me.jellysquid.mods.sodium.client.gui.options.TextProvider;

public class ConfigEnum {
    public static enum Complexity implements TextProvider {
        OFF("Off"),
        SIMPLE("Simple"),
        ADVANCED("Advanced");

        private final String name;

        private Complexity(String name) {
            this.name = name;
        }

        public String getLocalizedName() {
            return this.name;
        }
    }

    public static enum Quality implements TextProvider {
        OFF("Off"),
        FAST("Fast"),
        FANCY("Fancy");

        private final String name;

        private Quality(String name) {
            this.name = name;
        }

        public String getLocalizedName() {
            return this.name;
        }
    }

    public enum QualityMode implements TextProvider {
        OFF("Off"),
        SLOW("Slow"),
        FAST("Fast"),
        REALTIME("Realtime");

        private final String name;

        private QualityMode(String name) {
            this.name = name;
        }

        public String getLocalizedName() {
            return this.name;
        }
    }

    public enum ZoomTransitionOptions {
        OFF,
        SMOOTH
    }

    public enum ZoomModes {
        HOLD,
        TOGGLE,
        PERSISTENT
    }

    public enum CinematicCameraOptions {
        OFF,
        VANILLA,
        MULTIPLIED
    }

    public static class ZoomValues {
        //@Setting.Constrain.Range(min = Double.MIN_NORMAL)
        //@Setting(comment = "The divisor applied to the FOV when zooming.")
        public double zoomDivisor = 4.0;

        //@Setting.Constrain.Range(min = Double.MIN_NORMAL)
        //@Setting(comment = "The minimum value that you can scroll down.")
        public double minimumZoomDivisor = 1.0;

        //@Setting.Constrain.Range(min = Double.MIN_NORMAL)
        //@Setting(comment = "The maximum value that you can scroll up.")
        public double maximumZoomDivisor = 50.0;

        //@Setting.Constrain.Range(min = 0.0)
        //@Setting(comment = "The number which is de/incremented by zoom scrolling. Used when the zoom divisor is above the starting point.")
        public double scrollStep = 1.0;

        //"The number which is de/incremented by zoom scrolling. Used when the zoom divisor is below the starting point.")
        public double lesserScrollStep = 0.5;

        //@Setting.Constrain.Range(min = Double.MIN_NORMAL)
        //@Setting(comment = "The multiplier used for the multiplied cinematic camera.")
        public double cinematicMultiplier = 4.0;

        ////@Setting.Constrain.Range(min = Double.MIN_NORMAL, max = 1.0)
        //@Setting(comment = "The multiplier used for smooth transitions.")
        public double smoothMultiplier = 0.75;

        //@Setting(comment = "The minimum value which the linear transition step can reach.")
        public double minimumLinearStep = 0.125;

        //@Setting.Constrain.Range(min = Double.MIN_NORMAL)
        // @Setting(comment = "The maximum value which the linear transition step can reach.")
        public double maximumLinearStep = 0.25;
    }
}
