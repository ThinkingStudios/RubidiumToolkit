package org.thinkingstudio.rubidium_toolkit.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import lombok.val;
import lombok.var;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.thinkingstudio.rubidium_toolkit.RubidiumToolkit;

import java.nio.file.Path;

public class ToolkitConfig {
    public static ForgeConfigSpec SPEC;

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
        var builder = new ConfigBuilder("Rubidium Toolkit Settings");


        builder.Block("Entity Distance", b -> {
            enableDistanceChecks = b.define("Enable Max Distance Checks", true);

            maxBlockEntityRenderDistanceSquare = b.define("(BlockEntity) Max Horizontal Render Distance [Squared, Default 64^2]", 4096);
            maxBlockEntityRenderDistanceY = b.define("(BlockEntity) Max Vertical Render Distance [Raw, Default 32]", 32);

            maxEntityRenderDistanceSquare = b.define("(Entity) Max Horizontal Render Distance [Squared, Default 64^2]", 4096);
            maxEntityRenderDistanceY = b.define("(Entity) Max Vertical Render Distance [Raw, Default 32]", 32);
        });

        builder.Block("Zoom", b -> {
            lowerZoomSensitivity = b.define("Lower Zoom Sensitivity", true);
            zoomScrolling = b.define("Zoom Scrolling Enabled", true);
            zoomTransition = b.define("Zoom Transition Mode (OFF, LINEAR, SMOOTH)", ConfigEnum.ZoomTransitionOptions.SMOOTH.toString());
            zoomMode = b.define("Zoom Transition Mode (TOGGLE, HOLD, PERSISTENT)", ConfigEnum.ZoomModes.HOLD.toString());
            cinematicCameraMode = b.define("Cinematic Camera Mode (OFF, VANILLA, MULTIPLIED)", ConfigEnum.CinematicCameraOptions.OFF.toString());
            zoomOverlay = b.define("Zoom Overlay?", true);
            //zoomValues = b.define("Zoom Advanced Values", new ZoomValues());
        });

        builder.Block("Dynamic Lights", b -> {
            quality = b.define("Quality Mode (OFF, SLOW, FAST, REALTIME)", "REALTIME");
            entityLighting = b.define("Dynamic Entity Lighting", true);
            blockEntityLighting = b.define("Dynamic TileEntity Lighting", true);
            onlyUpdateOnPositionChange = b.define("Only Update On Position Change", true);
        });

        SPEC = builder.Save();
    }

    public static void loadConfig(Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

        configData.load();
        SPEC.setConfig(configData);
    }

}
