package com.texstudio.rubidium_zoomer;

import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraftforge.common.MinecraftForge;
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
import com.texstudio.rubidium_zoomer.config.RubidiumZoomerConfig;

import java.lang.reflect.Field;

@Mod(RubidiumZoomer.MODID)
public class RubidiumZoomer
{
    public static final String MODID = "rubidium_zoomer";
    public static final Logger LOGGER = LogManager.getLogger();

    public RubidiumZoomer() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);

        RubidiumZoomerConfig.loadConfig(FMLPaths.CONFIGDIR.get().resolve("rubidium_zoomer.toml"));

        //MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get()
                .registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

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