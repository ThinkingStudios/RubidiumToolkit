package org.thinkingstudio.rubidium_toolkit.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

public class ToolkitConfig {
    public static ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.ConfigValue<Integer> maxTileEntityRenderDistanceSquare;
    public static ForgeConfigSpec.ConfigValue<Integer> maxTileEntityRenderDistanceY;

    public static ForgeConfigSpec.ConfigValue<Integer> maxEntityRenderDistanceSquare;
    public static ForgeConfigSpec.ConfigValue<Integer> maxEntityRenderDistanceY;

    public static ForgeConfigSpec.ConfigValue<Boolean> fog;
    public static ForgeConfigSpec.ConfigValue<Boolean> enableDistanceChecks;

    public static ForgeConfigSpec.EnumValue<ConfigEnum.QualityMode> Quality;
    public static ForgeConfigSpec.ConfigValue<Boolean> EntityLighting;
    public static ForgeConfigSpec.ConfigValue<Boolean> TileEntityLighting;

    public static ForgeConfigSpec.ConfigValue<Boolean> OnlyUpdateOnPositionChange;

    static {
        var builder = new ConfigBuilder("RubidiumToolkit");

        builder.Block("Misc", b -> {
            fog = b.define("Render Fog", true);
        });

        builder.Block("Entity Distance", b -> {
            enableDistanceChecks = b.define("Enable Max Distance Checks", true);

            maxTileEntityRenderDistanceSquare = b.define("(BlockEntity) Max Horizontal Render Distance [Squared, Default 64^2]", 4096);
            maxTileEntityRenderDistanceY = b.define("(BlockEntity) Max Vertical Render Distance [Raw, Default 32]", 32);

            maxEntityRenderDistanceSquare = b.define("(Entity) Max Horizontal Render Distance [Squared, Default 64^2]", 4096);
            maxEntityRenderDistanceY = b.define("(Entity) Max Vertical Render Distance [Raw, Default 32]", 32);
        });

        builder.Block("Lighting Settings", b -> {
            Quality = b.defineEnum("Quality Mode (OFF, SLOW, FAST, REALTIME)", ConfigEnum.QualityMode.REALTIME);
            EntityLighting = b.define("Dynamic Entity Lighting", true);
            TileEntityLighting = b.define("Dynamic TileEntity Lighting", true);
            OnlyUpdateOnPositionChange = b.define("Only Update On Position Change", true);
        });

        SPEC = builder.Save();
    }

    public static void loadConfig(Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

        configData.load();
        SPEC.setConfig(configData);
    }
}
