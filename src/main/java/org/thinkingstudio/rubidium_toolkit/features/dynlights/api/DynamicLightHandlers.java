package org.thinkingstudio.rubidium_toolkit.features.dynlights.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.thinkingstudio.rubidium_toolkit.config.ToolkitConfig;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.DynamicLightsFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class DynamicLightHandlers {
    private static final Map<EntityType<? extends Entity>, DynamicLightHandler<? extends Entity>> ENTITES_HANDLER = new HashMap<>();
    private static final Map<BlockEntityType<? extends BlockEntity>, DynamicLightHandler<? extends BlockEntity>> BLOCK_ENTITIES_HANDLER = new HashMap<>();

    private DynamicLightHandlers() {
        throw new UnsupportedOperationException("DynamicLightHandlers only contains static definitions.");
    }

    /**
     * Registers the default handlers.
     */
    public static void registerDefaultHandlers() {
        registerDynamicLightHandler(EntityType.BLAZE, DynamicLightHandler.makeHandler(blaze -> 10, blaze -> true));
        registerDynamicLightHandler(EntityType.CREEPER, DynamicLightHandler.makeCreeperEntityHandler(null));
        registerDynamicLightHandler(EntityType.ENDERMAN, entity -> {
            int luminance = 0;
            if (entity.getCarriedBlock() != null)
                luminance = entity.getCarriedBlock().getLightEmission();
            return luminance;
        });
        registerDynamicLightHandler(EntityType.ITEM, entity -> DynamicLightsFeature.getLuminanceFromItemStack(entity.getItem(), entity.isUnderWater()));
        registerDynamicLightHandler(EntityType.ITEM_FRAME, entity -> {
            Level world = entity.getCommandSenderWorld();
            return DynamicLightsFeature.getLuminanceFromItemStack(entity.getItem(), !world.getFluidState(entity.getPos()).isEmpty());
        });
        registerDynamicLightHandler(EntityType.MAGMA_CUBE, entity -> (entity.squish > 0.6) ? 11 : 8);
        registerDynamicLightHandler(EntityType.SPECTRAL_ARROW, entity -> 8);
    }

    /**
     * Registers an entity dynamic light handler.
     *
     * @param type The entity type.
     * @param handler The dynamic light handler.
     * @param <T> The type of the entity.
     */
    public static <T extends Entity> void registerDynamicLightHandler(@NotNull EntityType<T> type, @NotNull DynamicLightHandler<T> handler) {
        DynamicLightHandler<T> registeredHandler = getDynamicLightHandler(type);
        if (registeredHandler != null) {
            DynamicLightHandler<T> newHandler = entity -> Math.max(registeredHandler.getLuminance(entity), handler.getLuminance(entity));
            ENTITES_HANDLER.put(type, newHandler);
        }
        ENTITES_HANDLER.put(type, handler);
    }

    /**
     * Registers a block entity dynamic light handler.
     *
     * @param type The block entity type.
     * @param handler The dynamic light handler.
     * @param <T> The type of the block entity.
     */
    public static <T extends BlockEntity> void registerDynamicLightHandler(@NotNull BlockEntityType<T> type, @NotNull DynamicLightHandler<T> handler) {
        DynamicLightHandler<T> registeredHandler = getDynamicLightHandler(type);
        if (registeredHandler != null) {
            DynamicLightHandler<T> newHandler = entity -> Math.max(registeredHandler.getLuminance(entity), handler.getLuminance(entity));
            BLOCK_ENTITIES_HANDLER.put(type, newHandler);
        }
        BLOCK_ENTITIES_HANDLER.put(type, handler);
    }

    /**
     * Returns the registered dynamic light handler of the specified entity.
     *
     * @param type The entity type.
     * @param <T> The type of the entity.
     * @return The registered dynamic light handler.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Entity> @Nullable DynamicLightHandler<T> getDynamicLightHandler(@NotNull EntityType<T> type) {
        return (DynamicLightHandler<T>) ENTITES_HANDLER.get(type);
    }

    /**
     * Returns the registered dynamic light handler of the specified block entity.
     *
     * @param type The block entity type.
     * @param <T> The type of the block entity.
     * @return The registered dynamic light handler.
     */
    @SuppressWarnings("unchecked")
    public static <T extends BlockEntity> @Nullable DynamicLightHandler<T> getDynamicLightHandler(@NotNull BlockEntityType<T> type) {
        return (DynamicLightHandler<T>) BLOCK_ENTITIES_HANDLER.get(type);
    }

    /**
     * Returns the luminance from an entity.
     *
     * @param entity The entity.
     * @param <T> The type of the entity.
     * @return The luminance.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Entity> int getLuminanceFrom(@NotNull T entity) {
        if (!ToolkitConfig.entityLighting.get())
            return 0;

        DynamicLightHandler<T> handler = (DynamicLightHandler<T>) getDynamicLightHandler(entity.getType());
        if (handler == null)
            return 0;
        if (handler.isWaterSensitive(entity) && !entity.getCommandSenderWorld().getFluidState(new BlockPos(entity.getX(), entity.getEyeY(), entity.getZ())).isEmpty())
            return 0;
        return handler.getLuminance(entity);
    }

    /**
     * Returns the luminance from a block entity.
     *
     * @param entity The block entity.
     * @param <T> The type of the block entity.
     * @return The luminance.
     */
    @SuppressWarnings("unchecked")
    public static <T extends BlockEntity> int getLuminanceFrom(@NotNull T entity) {
        if (!ToolkitConfig.blockEntityLighting.get())
            return 0;
        DynamicLightHandler<T> handler = (DynamicLightHandler<T>) getDynamicLightHandler(entity.getType());
        if (handler == null)
            return 0;
        if (handler.isWaterSensitive(entity) && entity.getLevel() != null && !entity.getLevel().getFluidState(entity.getBlockPos()).isEmpty())
            return 0;
        return handler.getLuminance(entity);
    }
}
