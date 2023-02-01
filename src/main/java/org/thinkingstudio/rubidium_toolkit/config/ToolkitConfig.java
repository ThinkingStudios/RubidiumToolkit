package org.thinkingstudio.rubidium_toolkit.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import lombok.val;
import net.minecraftforge.common.ForgeConfigSpec;
import java.nio.file.Path;

public class ToolkitConfig {
    public static ForgeConfigSpec ConfigSpec;

    public static ForgeConfigSpec.ConfigValue<Integer> maxTileEntityRenderDistanceSquare;
    public static ForgeConfigSpec.ConfigValue<Integer> maxTileEntityRenderDistanceY;

    public static ForgeConfigSpec.ConfigValue<Integer> maxEntityRenderDistanceSquare;
    public static ForgeConfigSpec.ConfigValue<Integer> maxEntityRenderDistanceY;

    public static ForgeConfigSpec.ConfigValue<Boolean> fog;
    public static ForgeConfigSpec.ConfigValue<Boolean> enableDistanceChecks;


    // Ok Zoomer
    public static ConfigEnum.ZoomValues zoomValues = new ConfigEnum.ZoomValues();
    public static ForgeConfigSpec.ConfigValue<Boolean> lowerZoomSensitivity;
    public static ForgeConfigSpec.ConfigValue<String> zoomTransition;
    public static ForgeConfigSpec.ConfigValue<String> zoomMode;
    public static ForgeConfigSpec.ConfigValue<String> cinematicCameraMode;
    public static ForgeConfigSpec.ConfigValue<Boolean> zoomScrolling;
    public static ForgeConfigSpec.ConfigValue<Boolean> zoomOverlay;

    static {
        val builder = new ConfigBuilder("RubidiumToolkit");

        builder.Block("Misc", b -> {
            fog = b.define("Render Fog", true);
        });

        builder.Block("Entity Distance", b -> {
            enableDistanceChecks = b.define("Enable Max Distance Checks", true);

            maxTileEntityRenderDistanceSquare = b.define("(TileEntity) Max Horizontal Render Distance [Squared, Default 64^2]", 4096);
            maxTileEntityRenderDistanceY = b.define("(TileEntity) Max Vertical Render Distance [Raw, Default 32]", 32);

            maxEntityRenderDistanceSquare = b.define("(Entity) Max Horizontal Render Distance [Squared, Default 64^2]", 4096);
            maxEntityRenderDistanceY = b.define("(Entity) Max Vertical Render Distance [Raw, Default 32]", 32);
        });

        builder.Block("Zoom", b -> {
            lowerZoomSensitivity = b.define("Lower zoom Sensitivity", true);
            zoomScrolling = b.define("zoom Scrolling Enabled", true);
            zoomTransition = b.define("zoom Transition Mode (OFF, LINEAR, SMOOTH)", ConfigEnum.ZoomTransitionOptions.SMOOTH.toString());
            zoomMode = b.define("zoom Transition Mode (TOGGLE, HOLD, PERSISTENT)", ConfigEnum.ZoomModes.HOLD.toString());
            cinematicCameraMode = b.define("Cinematic Camera Mode (OFF, VANILLA, MULTIPLIED)", ConfigEnum.CinematicCameraOptions.OFF.toString());
            zoomOverlay = b.define("zoom Overlay?", true);
            //zoomValues = b.define("zoom Advanced Values", new ZoomValues());
        });

        ConfigSpec = builder.Save();
    }

    public static void loadConfig(Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

        configData.load();
        ConfigSpec.setConfig(configData);
    }
}
