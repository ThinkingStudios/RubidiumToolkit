package org.thinkingstudio.rubidium_toolkit;

import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfig;

import java.lang.reflect.Field;

@Mod(RubidiumToolkit.MODID)
public class RubidiumToolkit
{
    public static final String MODID = "rubidium_toolkit";
    public static final Logger LOGGER = LogManager.getLogger("RubidiumToolkit");

    public RubidiumToolkit() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        RubidiumToolkitConfig.loadConfig(FMLPaths.CONFIGDIR.get().resolve(RubidiumToolkit.MODID + ".toml"));

        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        try {
            final Field sodiumOptsField = SodiumGameOptionPages.class.getDeclaredField("sodiumOpts");
            sodiumOptsField.setAccessible(true);
            SodiumOptionsStorage sodiumOpts = (SodiumOptionsStorage) sodiumOptsField.get(null);
            sodiumOpts.save();
        }
        catch (Throwable t) {
            LOGGER.error("Could not retrieve sodiumOptsField");
        }

    }

    private void setup(final FMLCommonSetupEvent event)
    {

    }
}