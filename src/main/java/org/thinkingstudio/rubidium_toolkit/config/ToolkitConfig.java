package org.thinkingstudio.rubidium_toolkit.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import java.nio.file.Path;

import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class ToolkitConfig {
    public static ForgeConfigSpec SPEC;

    public static ConfigValue<Integer> maxBlockEntityRenderDistanceSquare;
    public static ConfigValue<Integer> maxBlockEntityRenderDistanceY;

    public static ConfigValue<Integer> maxEntityRenderDistanceSquare;
    public static ConfigValue<Integer> maxEntityRenderDistanceY;

    public static ConfigValue<Boolean> fog;
    public static ConfigValue<Boolean> enableDistanceChecks;

    public static ForgeConfigSpec.EnumValue<ConfigEnum.QualityMode> quality;
    public static ForgeConfigSpec.ConfigValue<Boolean> entityLighting;
    public static ForgeConfigSpec.ConfigValue<Boolean> blockEntityLighting;
    public static ForgeConfigSpec.ConfigValue<Boolean> onlyUpdateOnPositionChange;

    static {
        final var builder = new ConfigBuilder("RubidiumToolkit");

        builder.Block("Misc", b -> {
            fog = b.define("Render Fog", true);
        });

        builder.Block("Entity Distance", b -> {
            enableDistanceChecks = b.define("Enable Max Distance Checks", true);

            maxBlockEntityRenderDistanceSquare = b.define("(BlockEntity) Max Horizontal Render Distance [Squared, Default 64^2]", 4096);
            maxBlockEntityRenderDistanceY = b.define("(BlockEntity) Max Vertical Render Distance [Raw, Default 32]", 32);

            maxEntityRenderDistanceSquare = b.define("(Entity) Max Horizontal Render Distance [Squared, Default 64^2]", 4096);
            maxEntityRenderDistanceY = b.define("(Entity) Max Vertical Render Distance [Raw, Default 32]", 32);
        });

        builder.Block("Lighting Settings", b -> {
            quality = b.defineEnum("Quality Mode (OFF, SLOW, FAST, REALTIME)", ConfigEnum.QualityMode.REALTIME);
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
