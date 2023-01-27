package org.thinkingstudio.rubidium_toolkit;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfig;

@Mod("milk_of_magnesium")
public class RubidiumToolkit
{
    public static final String MODID = "milk_of_magnesium";
    public static final Logger LOGGER = LogManager.getLogger();

    public RubidiumToolkit() {
        MinecraftForge.EVENT_BUS.register(this);

        RubidiumToolkitConfig.loadConfig(FMLPaths.CONFIGDIR.get().resolve("milk_of_magnesium.toml"));

        ModLoadingContext.get()
                .registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));


    }


}