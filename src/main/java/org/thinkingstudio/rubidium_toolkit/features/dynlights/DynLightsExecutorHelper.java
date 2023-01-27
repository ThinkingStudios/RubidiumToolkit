package org.thinkingstudio.rubidium_toolkit.features.dynlights;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.api.DynamicLightHandlers;

public class DynLightsExecutorHelper {
    public static void onInitializeClient()
    {
        DynLightsResourceListener reloadListener = new DynLightsResourceListener();

        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        if (resourceManager instanceof ReloadableResourceManager reloadableResourceManager) {
            reloadableResourceManager.registerReloadListener(reloadListener);
        }

        DynamicLightHandlers.registerDefaultHandlers();
    }
}
