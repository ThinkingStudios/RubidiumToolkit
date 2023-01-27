package org.thinkingstudio.rubidium_toolkit;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfig;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.DynamicLightsResourceListener;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.api.DynamicLightHandlers;

@Mod(RubidiumToolkit.MODID)
public class RubidiumToolkit
{
    public static final String MODID = "rubidium_toolkit";
    public static final Logger LOGGER = LogManager.getLogger("RubidiumToolkit");

    public RubidiumToolkit() {
        RubidiumToolkitConfig.loadConfig(FMLPaths.CONFIGDIR.get().resolve(RubidiumToolkit.MODID + ".toml"));

        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> this::onInitializeClient);
    }

    public void onInitializeClient() {
        DynamicLightsResourceListener reloadListener = new DynamicLightsResourceListener();

        ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
        if (resourceManager instanceof ReloadableResourceManager) {
            ReloadableResourceManager reloadableResourceManager = (ReloadableResourceManager) resourceManager;
            reloadableResourceManager.registerReloader(reloadListener);
        }

        DynamicLightHandlers.registerDefaultHandlers();
    }
}