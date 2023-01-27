package org.thinkingstudio.rubidium_toolkit;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfig;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.DynLightsResourceListener;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.api.DynamicLightHandlers;

@Mod(RubidiumToolkit.MODID)
public class RubidiumToolkit {
    public static final String MODID = "rubidium_toolkit";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public RubidiumToolkit() {
        MinecraftForge.EVENT_BUS.register(this);

        RubidiumToolkitConfig.loadConfig(FMLPaths.CONFIGDIR.get().resolve(RubidiumToolkit.MODID + ".toml"));
        RubidiumToolkitConfig.loadConfig(FMLPaths.CONFIGDIR.get().resolve(RubidiumToolkit.MODID + "-DynLights.toml"));

        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));


        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> RubidiumToolkit::onInitializeClient);
    }

    public static void onInitializeClient() {
        DynLightsResourceListener reloadListener = new DynLightsResourceListener();

        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        if (resourceManager instanceof ReloadableResourceManager reloadableResourceManager) {
            reloadableResourceManager.registerReloadListener(reloadListener);
        }

        DynamicLightHandlers.registerDefaultHandlers();
    }
}