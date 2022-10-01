package com.texstudio.rubidium_toolkit.dynamic_lights.api.item;

import com.google.gson.JsonObject;
import com.texstudio.rubidium_toolkit.dynamic_lights.RubidiumDynamicLights;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.aperlambda.lambdacommon.LambdaConstants;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents an item light sources manager.
 *
 * @author LambdAurora
 * @version 1.3.2
 * @since 1.3.0
 */
public final class ItemLightSources
{
    private static final List<ItemLightSource> ITEM_LIGHT_SOURCES = new ArrayList<>();
    private static final List<ItemLightSource> STATIC_ITEM_LIGHT_SOURCES = new ArrayList<>();

    private ItemLightSources()
    {
        throw new UnsupportedOperationException("ItemLightSources only contains static definitions.");
    }

    /**
     * Loads the item light source data from resource pack.
     *
     * @param resourceManager The resource manager.
     */
    public static void load(@NotNull IResourceManager resourceManager)
    {
        ITEM_LIGHT_SOURCES.clear();

        resourceManager.listResources("dynamiclights/item", path -> path.endsWith(".json")).forEach(id -> load(resourceManager, id));

        ITEM_LIGHT_SOURCES.addAll(STATIC_ITEM_LIGHT_SOURCES);
    }

    private static void load(@NotNull IResourceManager resourceManager, @NotNull ResourceLocation resourceId)
    {
        ResourceLocation id = new ResourceLocation(resourceId.getNamespace(), resourceId.getPath().replace(".json", ""));
        try {
            InputStream stream = resourceManager.getResource(resourceId).getInputStream();
            JsonObject json = LambdaConstants.JSON_PARSER.parse(new InputStreamReader(stream)).getAsJsonObject();

            Optional<ItemLightSource> result = ItemLightSource.fromJson(id, json);
            if (!result.isPresent()) {
                return;
            }

            ItemLightSource data = result.get();
            if (STATIC_ITEM_LIGHT_SOURCES.contains(data))
                return;
            register(data);
        } catch (IOException | IllegalStateException e) {
            RubidiumDynamicLights.warn("Failed to load item light source \"" + id + "\".");
        }
    }

    /**
     * Registers an item light source data.
     *
     * @param data The item light source data.
     */
    private static void register(@NotNull ItemLightSource data)
    {
        for (ItemLightSource other : ITEM_LIGHT_SOURCES) {
            if (other.item == data.item) {
                RubidiumDynamicLights.warn("Failed to register item light source \"" + data.id + "\", duplicates item found in \"" + other.id + "\".");
                return;
            }
        }

        ITEM_LIGHT_SOURCES.add(data);
    }

    /**
     * Registers an item light source data.
     *
     * @param data The item light source data.
     */
    public static void registerItemLightSource(@NotNull ItemLightSource data)
    {
        for (ItemLightSource other : STATIC_ITEM_LIGHT_SOURCES) {
            if (other.item == data.item) {
                RubidiumDynamicLights.warn("Failed to register item light source \"" + data.id + "\", duplicates item found in \"" + other.id + "\".");
                return;
            }
        }

        STATIC_ITEM_LIGHT_SOURCES.add(data);
    }

    /**
     * Returns the luminance of the item in the stack.
     *
     * @param stack The item stack.
     * @param submergedInWater True if the stack is submerged in water, else false.
     * @return A luminance value.
     */
    public static int getLuminance(@NotNull ItemStack stack, boolean submergedInWater)
    {
        for (ItemLightSource data : ITEM_LIGHT_SOURCES) {
            if (data.item == stack.getItem()) {
                return data.getLuminance(stack, submergedInWater);
            }
        }
        if (stack.getItem() instanceof BlockItem)
            return ((BlockItem) stack.getItem()).getBlock().defaultBlockState().getLightEmission();
        return 0;
    }
}
