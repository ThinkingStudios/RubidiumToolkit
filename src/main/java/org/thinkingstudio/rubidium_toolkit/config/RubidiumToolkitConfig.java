package org.thinkingstudio.rubidium_toolkit.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import java.nio.file.Path;

import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class RubidiumToolkitConfig
{
    public static ForgeConfigSpec ConfigSpec;

    public static ConfigValue<Integer> cloudHeight;

    public static ConfigValue<Integer> maxBlockEntityRenderDistanceSquare;
    public static ConfigValue<Integer> maxBlockEntityRenderDistanceY;

    public static ConfigValue<Integer> maxEntityRenderDistanceSquare;
    public static ConfigValue<Integer> maxEntityRenderDistanceY;

    public static ConfigValue<Boolean> fog;
    public static ConfigValue<Boolean> enableDistanceChecks;

    static {
        final var builder = new ForgeConfigSpec.Builder();

        {
            builder.push("Misc");

            fog = builder.define("Render Fog", true);

            builder.pop();
        }

        {
            builder.push("Entity Distance");

            enableDistanceChecks = builder.define("Enable Max Distance Checks", true);

            maxBlockEntityRenderDistanceSquare = builder.define("(BlockEntity) Max Horizontal Render Distance [Squared, Default 64^2]", 4096);
            maxBlockEntityRenderDistanceY = builder.define("(BlockEntity) Max Vertical Render Distance [Raw, Default 32]", 32);

            maxEntityRenderDistanceSquare = builder.define("(Entity) Max Horizontal Render Distance [Squared, Default 64^2]", 4096);
            maxEntityRenderDistanceY = builder.define("(Entity) Max Vertical Render Distance [Raw, Default 32]", 32);

            builder.pop();
        }
    }

    public static void loadConfig(Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

        configData.load();
        ConfigSpec.setConfig(configData);
    }
}
