package org.thinkingstudio.rubidium_toolkit.features.DynamicLights;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import org.thinkingstudio.rubidium_toolkit.features.DynamicLights.api.item.ItemLightSources;

import java.util.function.Predicate;

public class DynamicLightsResourceListener implements ISelectiveResourceReloadListener
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().setLenient().create();

    @Override
    public void onResourceManagerReload(IResourceManager manager, Predicate<IResourceType> resourcePredicate)
    {
        ItemLightSources.load(manager);
    }
}

