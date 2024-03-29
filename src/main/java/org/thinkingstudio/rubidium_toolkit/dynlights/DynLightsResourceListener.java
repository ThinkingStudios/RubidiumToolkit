package org.thinkingstudio.rubidium_toolkit.dynlights;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.thinkingstudio.rubidium_toolkit.dynlights.api.item.ItemLightSources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class DynLightsResourceListener implements ResourceManagerReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().setLenient().create();

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        System.out.println("Reloading Dynamic Lights");

        ItemLightSources.load(manager);
    }
}

