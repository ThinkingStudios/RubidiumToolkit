package org.thinkingstudio.rubidium_toolkit;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thinkingstudio.rubidium_toolkit.config.ToolkitConfig;
@Mod(RubidiumToolkit.MODID)
public class RubidiumToolkit {
    public static final String MODID = "rubidium_toolkit";
    public static final Logger LOGGER = LogManager.getLogger();

    public RubidiumToolkit() {
        MinecraftForge.EVENT_BUS.register(this);

        ToolkitConfig.loadConfig(FMLPaths.CONFIGDIR.get().resolve(RubidiumToolkit.MODID + ".toml"));

        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }


}