package org.thinkingstudio.rubidium_toolkit.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import lombok.val;
import me.jellysquid.mods.sodium.client.gui.options.TextProvider;
import net.minecraftforge.common.ForgeConfigSpec;
//import static net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

import java.nio.file.Path;

public class RubidiumToolkitConfig
{
    public static ForgeConfigSpec ConfigSpec;

    // Ok Zoomer
    public static ZoomValues zoomValues = new ZoomValues();
    public static ForgeConfigSpec.ConfigValue<Boolean> lowerZoomSensitivity;
    public static ForgeConfigSpec.ConfigValue<String> zoomTransition;
    public static ForgeConfigSpec.ConfigValue<String> zoomMode;
    public static ForgeConfigSpec.ConfigValue<String> cinematicCameraMode;
    public static ForgeConfigSpec.ConfigValue<Boolean> zoomScrolling;
    public static ForgeConfigSpec.ConfigValue<Boolean> zoomOverlay;

    // Total Darkness
    public static double darkNetherFogEffective;
    public static double darkEndFogEffective;
    public static ForgeConfigSpec.BooleanValue trueDarknessEnabled;
    public static ForgeConfigSpec.EnumValue<DarknessOption> darknessOption;
    //advanced
    public static ForgeConfigSpec.DoubleValue darkNetherFogConfigured;
    public static ForgeConfigSpec.BooleanValue darkEnd;
    public static ForgeConfigSpec.DoubleValue darkEndFogConfigured;
    public static ForgeConfigSpec.BooleanValue darkSkyless;
    public static ForgeConfigSpec.BooleanValue blockLightOnly;
    public static ForgeConfigSpec.BooleanValue ignoreMoonPhase;
    public static ForgeConfigSpec.DoubleValue minimumMoonLevel;
    public static ForgeConfigSpec.DoubleValue maximumMoonLevel;
    public static ForgeConfigSpec.BooleanValue darkOverworld;
    public static ForgeConfigSpec.BooleanValue darkDefault;
    public static ForgeConfigSpec.BooleanValue darkNether;
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

        builder.Block("True Darkness", b -> {
            trueDarknessEnabled = b.define("Use True Darkness", false);
            darknessOption = b.defineEnum("Darkness Mode (PITCH_BLACK, REALLY_DARK, DARK, DIM)", DarknessOption.DARK);

            builder.Block("Advanced", b2 -> {
                blockLightOnly = b2.define("Only Effect Block Lighting", false);
                ignoreMoonPhase = b2.define("Ignore Moon Light", false);
                minimumMoonLevel = b2.defineInRange("Minimum Moon Brightness (0->1)", 0, 0, 1d);
                maximumMoonLevel = b2.defineInRange("Maximum Moon Brightness (0->1)", 0.25d, 0, 1d);
            });

            builder.Block("Dimension Settings", b2 -> {
                darkOverworld = b2.define("Dark Overworld", false);
                darkDefault = b2.define("Dark By Default", false);
                darkNether = b2.define("Dark Nether", false);
                darkNetherFogConfigured = b2.defineInRange("Dark Nether Fog Brightness (0->1)", .5, 0, 1d);
                darkEnd = b2.define("Dark End", false);
                darkEndFogConfigured = b.defineInRange("Dark End Fog Brightness (0->1)", 0, 0, 1d);
                darkSkyless = b2.define("Dark If No Skylight", false);
            });
        });

        builder.Block("Dynamic Lights", b -> {
            Quality = b.define("Quality Mode (OFF, SLOW, FAST, REALTIME)", "REALTIME");
            EntityLighting = b.define("Dynamic Entity Lighting", true);
            TileEntityLighting = b.define("Dynamic TileEntity Lighting", true);
            OnlyUpdateOnPositionChange = b.define("Only Update On Position Change", true);
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

    public enum DarknessOption {
        PITCH_BLACK(0f),
        REALLY_DARK (0.04f),
        DARK(0.08f),
        DIM(0.12f);

        public final float value;

        private DarknessOption(float value) {
            this.value = value;
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

        //@Setting.Constrain.Range(min = Double.MIN_NORMAL, max = 1.0)
        //@Setting(comment = "The multiplier used for smooth transitions.")
        public double smoothMultiplier = 0.75;

        //@Setting(comment = "The minimum value which the linear transition step can reach.")
        public double minimumLinearStep = 0.125;

        //@Setting.Constrain.Range(min = Double.MIN_NORMAL)
        //@Setting(comment = "The maximum value which the linear transition step can reach.")
        public double maximumLinearStep = 0.25;
    }
}
