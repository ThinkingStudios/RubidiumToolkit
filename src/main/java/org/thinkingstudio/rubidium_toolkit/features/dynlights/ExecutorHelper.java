package org.thinkingstudio.rubidium_toolkit.features.dynlights;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ResourceManager;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.api.DynamicLightHandlers;

public class ExecutorHelper {
    public static void onInitializeClient()
    {
        DynamicLightsResourceListener reloadListener = new DynamicLightsResourceListener();

        ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
        if (resourceManager instanceof ReloadableResourceManager) {
            ReloadableResourceManager reloadableResourceManager = (ReloadableResourceManager) resourceManager;
            reloadableResourceManager.registerReloader(reloadListener);
        }

        DynamicLightHandlers.registerDefaultHandlers();
    }
}
