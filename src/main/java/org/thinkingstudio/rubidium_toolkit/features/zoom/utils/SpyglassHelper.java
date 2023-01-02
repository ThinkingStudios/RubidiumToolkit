package org.thinkingstudio.rubidium_toolkit.features.zoom.utils;

import org.thinkingstudio.rubidium_toolkit.features.zoom.api.OkZoomerAPI;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * A utility class whose sole purpose is to hold the spyglass tag
 */
public class SpyglassHelper {
    /**
     * The spyglass tag, which is used internally in order to unhardcode behavior specific to vanilla spyglasses
     */
    public static final TagKey<Item> SPYGLASSES = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(OkZoomerAPI.MOD_ID, "spyglasses"));
}
