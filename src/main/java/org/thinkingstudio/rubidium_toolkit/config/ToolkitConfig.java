package org.thinkingstudio.rubidium_toolkit.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import lombok.val;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

public class ToolkitConfig {
    public static ForgeConfigSpec ConfigSpec;

    public static ForgeConfigSpec.ConfigValue<Integer> maxBlockEntityRenderDistanceSquare;
    public static ForgeConfigSpec.ConfigValue<Integer> maxBlockEntityRenderDistanceY;

    public static ForgeConfigSpec.ConfigValue<Integer> maxEntityRenderDistanceSquare;
    public static ForgeConfigSpec.ConfigValue<Integer> maxEntityRenderDistanceY;

    public static ForgeConfigSpec.ConfigValue<Boolean> enableDistanceChecks;


    // Ok Zoomer
    public static ConfigEnum.ZoomValues zoomValues = new ConfigEnum.ZoomValues();
    public static ForgeConfigSpec.ConfigValue<Boolean> lowerZoomSensitivity;
    public static ForgeConfigSpec.ConfigValue<String> zoomTransition;
    public static ForgeConfigSpec.ConfigValue<String> zoomMode;
    public static ForgeConfigSpec.ConfigValue<String> cinematicCameraMode;
    public static ForgeConfigSpec.ConfigValue<Boolean> zoomScrolling;
    public static ForgeConfigSpec.ConfigValue<Boolean> zoomOverlay;

    // Dynamic Lights
    public static ForgeConfigSpec.ConfigValue<String> quality;
    public static ForgeConfigSpec.ConfigValue<Boolean> entityLighting;
    public static ForgeConfigSpec.ConfigValue<Boolean> blockEntityLighting;
    public static ForgeConfigSpec.ConfigValue<Boolean> onlyUpdateOnPositionChange;

    static {
        val bulider = new ForgeConfigSpec.Builder();

        {
            bulider.push("Zoom");

            lowerZoomSensitivity = bulider.define("Lower zoom Sensitivity", true);
            zoomScrolling = bulider.define("Zoom Scrolling Enabled", true);
            zoomTransition = bulider.define("Zoom Transition Mode (OFF, LINEAR, SMOOTH)", ConfigEnum.ZoomTransitionOptions.SMOOTH.toString());
            zoomMode = bulider.define("Zoom Transition Mode (TOGGLE, HOLD, PERSISTENT)", ConfigEnum.ZoomModes.HOLD.toString());
            cinematicCameraMode = bulider.define("Cinematic Camera Mode (OFF, VANILLA, MULTIPLIED)", ConfigEnum.CinematicCameraOptions.OFF.toString());
            zoomOverlay = bulider.define("Zoom Overlay", true);
            //zoomValues = bulider.define("Zoom Advanced Values", new ZoomValues());

            bulider.pop();
        }

        {
            bulider.push("Dynamic Lights");

            quality = bulider.define("Quality Mode (OFF, SLOW, FAST, REALTIME)", "REALTIME");
            entityLighting = bulider.define("Dynamic Entity Lighting", true);
            blockEntityLighting = bulider.define("Dynamic TileEntity Lighting", true);
            onlyUpdateOnPositionChange = bulider.define("Only Update On Position Change", true);

            bulider.pop();
        }

        {
            bulider.push("Entity Distance");

            enableDistanceChecks = bulider.define("Enable Max Distance Checks", true);

            maxBlockEntityRenderDistanceSquare = bulider.define("(BlockEntity) Max Horizontal Render Distance [Squared, Default 64^2]", 4096);
            maxBlockEntityRenderDistanceY = bulider.define("(BlockEntity) Max Vertical Render Distance [Raw, Default 32]", 32);

            maxEntityRenderDistanceSquare = bulider.define("(Entity) Max Horizontal Render Distance [Squared, Default 64^2]", 4096);
            maxEntityRenderDistanceY = bulider.define("(Entity) Max Vertical Render Distance [Raw, Default 32]", 32);

            bulider.pop();
        }
    }

    public static void loadConfig(Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

        configData.load();
        ConfigSpec.setConfig(configData);
    }
}
