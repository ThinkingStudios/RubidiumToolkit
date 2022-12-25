package org.thinkingstudio.rubidium_toolkit.features.DynamicLights;

import org.thinkingstudio.rubidium_toolkit.features.DynamicLights.api.DynamicLightHandlers;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;

public class ExecutorHelper {
    public static void onInitializeClient()
    {
        DynamicLightsResourceListener reloadListener = new DynamicLightsResourceListener();

        IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        if (resourceManager instanceof IReloadableResourceManager) {
            IReloadableResourceManager reloadableResourceManager = (IReloadableResourceManager) resourceManager;
            reloadableResourceManager.registerReloadListener(reloadListener);
        }

        DynamicLightHandlers.registerDefaultHandlers();
    }
}
