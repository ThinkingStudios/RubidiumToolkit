package org.thinkingstudio.rubidium_toolkit.features.dynlights;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resource.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.api.item.ItemLightSources;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;

import java.util.function.Predicate;

public class DynamicLightsResourceListener implements ISelectiveResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().setLenient().create();

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager manager, @NotNull Predicate<IResourceType> resourcePredicate)
    {
        ItemLightSources.load(manager);
    }
}

