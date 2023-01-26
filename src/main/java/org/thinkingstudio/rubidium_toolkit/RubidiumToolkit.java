package org.thinkingstudio.rubidium_toolkit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfigClient;
import org.thinkingstudio.rubidium_toolkit.features.zoom.ToolkitZoom;
import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfigServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;

@Mod(RubidiumToolkit.MODID)
public class RubidiumToolkit {

    public static final String MODID = "rubidium_toolkit";

    public static final String MODNAME= "RubidiumToolkit";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public RubidiumToolkit() {

        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, RubidiumToolkitConfigClient.SPEC, "RubidiumToolkit/" + RubidiumToolkit.MODID + "-Client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, RubidiumToolkitConfigServer.SPEC, "RubidiumToolkit/" + RubidiumToolkit.MODID + "-Sever.toml");

        final var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(RubidiumToolkit::commonSetup);
        modBus.register(RubidiumToolkitConfigClient.class);
        modBus.register(RubidiumToolkitConfigServer.class);

        MinecraftForge.EVENT_BUS.addListener(ToolkitZoom::registerClientCommands);
        MinecraftForge.EVENT_BUS.addListener(ToolkitZoom::onPlayerLogout);
        MinecraftForge.EVENT_BUS.addListener(ToolkitZoom::onPlayerLogin);
    }

    static void commonSetup(final FMLCommonSetupEvent event) {
        ToolkitZoom.zoomPacketRegister();
    }
}
