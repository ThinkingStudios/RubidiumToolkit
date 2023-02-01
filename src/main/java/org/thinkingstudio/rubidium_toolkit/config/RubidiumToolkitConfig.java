package org.thinkingstudio.rubidium_toolkit.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import lombok.val;
import me.jellysquid.mods.sodium.client.gui.options.TextProvider;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

public class RubidiumToolkitConfig
{
    public static ForgeConfigSpec ConfigSpec;

    public static ForgeConfigSpec.ConfigValue<Integer> maxTileEntityRenderDistanceSquare;
    public static ForgeConfigSpec.ConfigValue<Integer> maxTileEntityRenderDistanceY;

    public static ForgeConfigSpec.ConfigValue<Integer> maxEntityRenderDistanceSquare;
    public static ForgeConfigSpec.ConfigValue<Integer> maxEntityRenderDistanceY;

    public static ForgeConfigSpec.ConfigValue<Boolean> fog;
    public static ForgeConfigSpec.ConfigValue<Boolean> enableDistanceChecks;

    // Ok Zoomer
    public static ZoomValues zoomValues = new ZoomValues();
    public static ForgeConfigSpec.ConfigValue<Boolean> lowerZoomSensitivity;
    public static ForgeConfigSpec.ConfigValue<String> zoomTransition;
    public static ForgeConfigSpec.ConfigValue<String> zoomMode;
    public static ForgeConfigSpec.ConfigValue<String> cinematicCameraMode;
    public static ForgeConfigSpec.ConfigValue<Boolean> zoomScrolling;
    public static ForgeConfigSpec.ConfigValue<Boolean> zoomOverlay;

    // Dynamic Lights
    public static ForgeConfigSpec.ConfigValue<String> Quality;
    public static ForgeConfigSpec.ConfigValue<Boolean> EntityLighting;
    public static ForgeConfigSpec.ConfigValue<Boolean> TileEntityLighting;
    public static ForgeConfigSpec.ConfigValue<Boolean> OnlyUpdateOnPositionChange;

    static
    {
        val builder = new ConfigBuilder("Rubidium Toolkit Settings");

        builder.Block("Zoom", b -> {
            lowerZoomSensitivity = b.define("Lower Zoom Sensitivity", true);
            zoomScrolling = b.define("Zoom Scrolling Enabled", true);
            zoomTransition = b.define("Zoom Transition Mode (OFF, LINEAR, SMOOTH)", ZoomTransitionOptions.SMOOTH.toString());
            zoomMode = b.define("Zoom Transition Mode (TOGGLE, HOLD, PERSISTENT)", ZoomModes.HOLD.toString());
            cinematicCameraMode = b.define("Cinematic Camera Mode (OFF, VANILLA, MULTIPLIED)", CinematicCameraOptions.OFF.toString());
            zoomOverlay = b.define("Zoom Overlay", true);
            //zoomValues = b.define("Zoom Advanced Values", new ZoomValues());
        });

        builder.Block("Dynamic Lights", b -> {
            Quality = b.define("Quality Mode (OFF, SLOW, FAST, REALTIME)", "REALTIME");
            EntityLighting = b.define("Dynamic Entity Lighting", true);
            TileEntityLighting = b.define("Dynamic TileEntity Lighting", true);
            OnlyUpdateOnPositionChange = b.define("Only Update On Position Change", true);
        });

        builder.Block("Entity Distance", b -> {
            enableDistanceChecks = b.define("Enable Max Distance Checks", true);
            maxTileEntityRenderDistanceSquare = b.define("(TileEntity) Max Horizontal Render Distance [Squared, Default 64^2]", 4096);
            maxTileEntityRenderDistanceY = b.define("(TileEntity) Max Vertical Render Distance [Raw, Default 32]", 32);
            maxEntityRenderDistanceSquare = b.define("(Entity) Max Horizontal Render Distance [Squared, Default 64^2]", 4096);
            maxEntityRenderDistanceY = b.define("(Entity) Max Vertical Render Distance [Raw, Default 32]", 32);
        });

        builder.Block("Misc", b -> {
            fog = b.define("Render Fog", true);
        });

        ConfigSpec = builder.Save();
    }

    public static void loadConfig(Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync().autosave().writingMode(WritingMode.REPLACE).build();

        configData.load();
        ConfigSpec.setConfig(configData);
    }

    public enum QualityMode implements TextProvider
    {
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
    public static enum Complexity implements TextProvider
    {
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
