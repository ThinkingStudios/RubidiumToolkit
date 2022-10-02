package com.texstudio.rubidium_toolkit.dynamic_lights.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import lombok.val;
import me.jellysquid.mods.sodium.client.gui.options.TextProvider;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

public class DynamicLightsConfig
{
    public static ForgeConfigSpec ConfigSpec;

    public static ForgeConfigSpec.ConfigValue<String> Quality;
    public static ForgeConfigSpec.ConfigValue<Boolean> EntityLighting;
    public static ForgeConfigSpec.ConfigValue<Boolean> TileEntityLighting;

    public static ForgeConfigSpec.ConfigValue<Boolean> OnlyUpdateOnPositionChange;

    static
    {
        val builder = new ConfigBuilder("Dynamic Lights Settings");

        builder.Block("Lighting Settings", b -> {
            Quality = b.define("Quality Mode (OFF, SLOW, FAST, REALTIME)", "REALTIME");
            EntityLighting = b.define("Dynamic Entity Lighting", true);
            TileEntityLighting = b.define("Dynamic TileEntity Lighting", true);
            OnlyUpdateOnPositionChange = b.define("Only Update On Position Change", true);
        });

        ConfigSpec = builder.Save();
    }

    public static void loadConfig(Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

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
}
