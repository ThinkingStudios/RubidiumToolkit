package com.texstudio.rubidium_toolkit.dynamic_lights;

import com.texstudio.rubidium_toolkit.RubidiumToolkit;
import com.texstudio.rubidium_toolkit.dynamic_lights.accessor.WorldRendererAccessor;
import com.texstudio.rubidium_toolkit.dynamic_lights.api.DynamicLightHandlers;
import com.texstudio.rubidium_toolkit.dynamic_lights.api.item.ItemLightSources;
import com.texstudio.rubidium_toolkit.dynamic_lights.config.DynamicLightsConfig;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;

class ExecutorHelper {
    public static void onInitializeClient()
    {
        DynLightsResourceListener reloadListener = new DynLightsResourceListener();

        IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        if (resourceManager instanceof IReloadableResourceManager) {
            IReloadableResourceManager reloadableResourceManager = (IReloadableResourceManager) resourceManager;
            reloadableResourceManager.registerReloadListener(reloadListener);
        }

        //FabricLoader.getInstance().getEntrypointContainers("dynamiclights", DynamicLightsInitializer.class)
        //        .stream().map(EntrypointContainer::getEntrypoint)
        //        .forEach(DynamicLightsInitializer::onInitializeDynamicLights);


        DynamicLightHandlers.registerDefaultHandlers();
    }
}

@Mod(RubidiumDynamicLights.MODID)
public class RubidiumDynamicLights
{
    public static final String MODID = RubidiumToolkit.MODID;
    private static final double MAX_RADIUS = 7.75;
    private static final double MAX_RADIUS_SQUARED = MAX_RADIUS * MAX_RADIUS;

    private static RubidiumDynamicLights INSTANCE;
    public static final Logger logger = LogManager.getLogger(MODID);

    //private static final ConcurrentLinkedQueue<DynamicLightSource> dynamicLightSources = new ConcurrentLinkedQueue<>();
    private static final Set<DynamicLightSource> dynamicLightSources = new HashSet<>();
    private static final ReentrantReadWriteLock lightSourcesLock = new ReentrantReadWriteLock();

    private static long lastUpdate = System.currentTimeMillis();
    private static int lastUpdateCount = 0;

    public static boolean isEnabled() { return !Objects.equals(DynamicLightsConfig.Quality.get(), "OFF"); }

    public RubidiumDynamicLights() {
        INSTANCE = this;
        log("Initializing Rubidium Dynamic Lights...");
        //this.config.load();
        DynamicLightsConfig.loadConfig(FMLPaths.CONFIGDIR.get().resolve(RubidiumToolkit.MODNAME + "/DynamicLights.toml"));

        //MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get()
                .registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));


        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ExecutorHelper::onInitializeClient);
    }


    private static long lambdynlights_lastUpdate = 0;

    public static boolean ShouldUpdateDynamicLights()
    {
        String mode = DynamicLightsConfig.Quality.get();
        if (Objects.equals(mode, "OFF"))
            return false;

        long currentTime = System.currentTimeMillis();

        if (Objects.equals(mode, "SLOW") && currentTime < lambdynlights_lastUpdate + 500)
            return false;


        if (Objects.equals(mode, "FAST") && currentTime < lambdynlights_lastUpdate + 200)
            return false;

        lambdynlights_lastUpdate = currentTime;
        return true;
    }


    /**
     * Updates all light sources.
     *
     * @param renderer the renderer
     */
    public static void updateAll(@NotNull WorldRenderer renderer)
    {
        if (!RubidiumDynamicLights.isEnabled())
            return;

        long now = System.currentTimeMillis();
        if (now >= lastUpdate + 50) {
            lastUpdate = now;
            lastUpdateCount = 0;

            lightSourcesLock.readLock().lock();
            for (DynamicLightSource lightSource : dynamicLightSources) {
                if (lightSource.lambdynlights_updateDynamicLight(renderer)) lastUpdateCount++;
            }
            lightSourcesLock.readLock().unlock();

        }
    }

    /**
     * Returns the last number of dynamic light source updates.
     *
     * @return the last number of dynamic light source updates
     */
    public static int getLastUpdateCount() {
        return lastUpdateCount;
    }

    /**
     * Returns the lightmap with combined light levels.
     *
     * @param pos the position
     * @param lightmap the vanilla lightmap coordinates
     * @return the modified lightmap coordinates
     */
    public static int getLightmapWithDynamicLight(@NotNull BlockPos pos, int lightmap)
    {

        return getLightmapWithDynamicLight(getDynamicLightLevel(pos), lightmap);
    }

    /**
     * Returns the lightmap with combined light levels.
     *
     * @param entity the entity
     * @param lightmap the vanilla lightmap coordinates
     * @return the modified lightmap coordinates
     */
    public static int getLightmapWithDynamicLight(@NotNull Entity entity, int lightmap)
    {

        int posLightLevel = (int) getDynamicLightLevel(entity.blockPosition());
        int entityLuminance = ((DynamicLightSource) entity).getLuminance();

        return getLightmapWithDynamicLight(Math.max(posLightLevel, entityLuminance), lightmap);
    }

    /**
     * Returns the lightmap with combined light levels.
     *
     * @param dynamicLightLevel the dynamic light level
     * @param lightmap the vanilla lightmap coordinates
     * @return the modified lightmap coordinates
     */
    public static int getLightmapWithDynamicLight(double dynamicLightLevel, int lightmap) {
        if (dynamicLightLevel > 0) {
            // lightmap is (skyLevel << 20 | blockLevel << 4)

            // Get vanilla block light level.
            int blockLevel = LightTexture.block(lightmap);
            if (dynamicLightLevel > blockLevel) {
                // Equivalent to a << 4 bitshift with a little quirk: this one ensure more precision (more decimals are saved).
                int luminance = (int) (dynamicLightLevel * 16.0);
                lightmap &= 0xfff00000;
                lightmap |= luminance & 0x000fffff;
            }
        }

        return lightmap;
    }

    /**
     * Returns the dynamic light level at the specified position.
     *
     * @param pos the position
     * @return the dynamic light level at the specified position
     */
    public static double getDynamicLightLevel(@NotNull BlockPos pos) {
        double result = 0;
        lightSourcesLock.readLock().lock();
        for (DynamicLightSource lightSource : dynamicLightSources) {
            result = maxDynamicLightLevel(pos, lightSource, result);
        }
        lightSourcesLock.readLock().unlock();

        return MathHelper.clamp(result, 0, 15);
    }

    /**
     * Returns the dynamic light level generated by the light source at the specified position.
     *
     * @param pos the position
     * @param lightSource the light source
     * @param currentLightLevel the current surrounding dynamic light level
     * @return the dynamic light level at the specified position
     */
    public static double maxDynamicLightLevel(@NotNull BlockPos pos, @NotNull DynamicLightSource lightSource, double currentLightLevel) {
        int luminance = lightSource.getLuminance();
        if (luminance > 0) {
            // Can't use Entity#squaredDistanceTo because of eye Y coordinate.
            double dx = pos.getX() - lightSource.getDynamicLightX() + 0.5;
            double dy = pos.getY() - lightSource.getDynamicLightY() + 0.5;
            double dz = pos.getZ() - lightSource.getDynamicLightZ() + 0.5;

            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

            double distanceSquared = dx * dx + dy * dy + dz * dz;


            // 7.75 because else we would have to update more chunks and that's not a good idea.
            // 15 (max range for blocks) would be too much and a bit cheaty.
            if (distanceSquared <= MAX_RADIUS_SQUARED) {
                double multiplier = 1.0 - Math.sqrt(distanceSquared) / MAX_RADIUS;
                double lightLevel = multiplier * (double) luminance;
                if (lightLevel > currentLightLevel) {
                    return lightLevel;
                }
            }
        }
        return currentLightLevel;
    }

    /**
     * Adds the light source to the tracked light sources.
     *
     * @param lightSource the light source to add
     */
    public static void addLightSource(@NotNull DynamicLightSource lightSource) {
        if (!lightSource.getDynamicLightWorld().isClientSide())
            return;
        if (!RubidiumDynamicLights.isEnabled())
            return;
        if (containsLightSource(lightSource))
            return;

        lightSourcesLock.readLock().lock();
        dynamicLightSources.add(lightSource);
        lightSourcesLock.readLock().unlock();

    }

    /**
     * Returns whether the light source is tracked or not.
     *
     * @param lightSource the light source to check
     * @return {@code true} if the light source is tracked, else {@code false}
     */
    public static boolean containsLightSource(@NotNull DynamicLightSource lightSource) {
        if (!lightSource.getDynamicLightWorld().isClientSide())
            return false;

        boolean result;
        lightSourcesLock.readLock().lock();
        result = dynamicLightSources.contains(lightSource);
        lightSourcesLock.readLock().unlock();
        return result;
    }

    /**
     * Returns the number of dynamic light sources that currently emit lights.
     *
     * @return the number of dynamic light sources emitting light
     */
    public static int getLightSourcesCount() {
        int result = 0;
        lightSourcesLock.readLock().lock();
        result = dynamicLightSources.size();
        lightSourcesLock.readLock().unlock();
        return result;
    }

    /**
     * Removes the light source from the tracked light sources.
     *
     * @param lightSource the light source to remove
     */
    public static void removeLightSource(@NotNull DynamicLightSource lightSource) {
        lightSourcesLock.readLock().lock();

        Iterator<DynamicLightSource> LightSources = dynamicLightSources.iterator();
        DynamicLightSource it;
        while (LightSources.hasNext()) {
            it = LightSources.next();
            if (it.equals(lightSource)) {
                LightSources.remove();
                if (Minecraft.getInstance().levelRenderer != null)
                    lightSource.lambdynlights_scheduleTrackedChunksRebuild(Minecraft.getInstance().levelRenderer);
                break;
            }
        }

        lightSourcesLock.readLock().unlock();
    }

    /**
     * Clears light sources.
     */
    public static void clearLightSources()
    {
        lightSourcesLock.readLock().lock();

        Iterator<DynamicLightSource> LightSources = dynamicLightSources.iterator();
        DynamicLightSource it;
        while (LightSources.hasNext()) {
            it = LightSources.next();
            LightSources.remove();
            if (Minecraft.getInstance().levelRenderer != null) {
                if (it.getLuminance() > 0)
                    it.resetDynamicLight();
                it.lambdynlights_scheduleTrackedChunksRebuild(Minecraft.getInstance().levelRenderer);
            }
        }

        lightSourcesLock.readLock().unlock();
    }

    /**
     * Removes light sources if the filter matches.
     *
     * @param filter the removal filter
     */
    public static void removeLightSources(@NotNull Predicate<DynamicLightSource> filter)
    {
        lightSourcesLock.readLock().lock();

        Iterator<DynamicLightSource> LightSources = dynamicLightSources.iterator();
        DynamicLightSource it;
        while (LightSources.hasNext()) {
            it = LightSources.next();
            if (filter.test(it)) {
                LightSources.remove();
                if (Minecraft.getInstance().levelRenderer != null) {
                    if (it.getLuminance() > 0)
                        it.resetDynamicLight();
                    it.lambdynlights_scheduleTrackedChunksRebuild(Minecraft.getInstance().levelRenderer);
                }
                break;
            }
        }
        lightSourcesLock.readLock().unlock();
    }

    /**
     * Removes entities light source from tracked light sources.
     */
    public static void removeEntitiesLightSource() {
        removeLightSources(lightSource -> (lightSource instanceof Entity && !(lightSource instanceof PlayerEntity)));
    }

    /**
     * Removes Creeper light sources from tracked light sources.
     */
    public static void removeCreeperLightSources() {
        removeLightSources(entity -> entity instanceof CreeperEntity);
    }

    /**
     * Removes TNT light sources from tracked light sources.
     */
    public static void removeTntLightSources() {
        removeLightSources(entity -> entity instanceof TNTEntity);
    }

    /**
     * Removes block entities light source from tracked light sources.
     */
    public static void removeBlockEntitiesLightSource() {
        removeLightSources(lightSource -> lightSource instanceof TileEntity);
    }

    /**
     * Prints a message to the terminal.
     *
     * @param info the message to print
     */
    public static void log(String info) {
        logger.info("[LambDynLights] " + info);
    }

    /**
     * Prints a warning message to the terminal.
     *
     * @param info the message to print
     */
    public static void warn(String info) {
        logger.warn("[LambDynLights] " + info);
    }

    /**
     * Schedules a chunk rebuild at the specified chunk position.
     *
     * @param renderer the renderer
     * @param chunkPos the chunk position
     */
    public static void scheduleChunkRebuild(@NotNull WorldRenderer renderer, @NotNull BlockPos chunkPos) {
        scheduleChunkRebuild(renderer, chunkPos.getX(), chunkPos.getY(), chunkPos.getZ());
    }

    /**
     * Schedules a chunk rebuild at the specified chunk position.
     *
     * @param renderer the renderer
     * @param chunkPos the packed chunk position
     */
    public static void scheduleChunkRebuild(@NotNull WorldRenderer renderer, long chunkPos) {
        scheduleChunkRebuild(renderer, BlockPos.getX(chunkPos), BlockPos.getY(chunkPos), BlockPos.getZ(chunkPos));
    }

    public static void scheduleChunkRebuild(@NotNull WorldRenderer renderer, int x, int y, int z) {
        if (Minecraft.getInstance().level != null)
            ((WorldRendererAccessor) renderer).dynlights_setSectionDirty(x, y, z, false);
    }

    /**
     * Updates the tracked chunk sets.
     *
     * @param chunkPos the packed chunk position
     * @param old the set of old chunk coordinates to remove this chunk from it
     * @param newPos the set of new chunk coordinates to add this chunk to it
     */
    public static void updateTrackedChunks(@NotNull BlockPos chunkPos, @Nullable LongOpenHashSet old, @Nullable LongOpenHashSet newPos) {
        if (old != null || newPos != null) {
            long pos = chunkPos.asLong();
            if (old != null)
                old.remove(pos);
            if (newPos != null)
                newPos.add(pos);
        }
    }

    /**
     * Updates the dynamic lights tracking.
     *
     * @param lightSource the light source
     */
    public static void updateTracking(@NotNull DynamicLightSource lightSource) {
        boolean enabled = lightSource.isDynamicLightEnabled();
        int luminance = lightSource.getLuminance();

        if (!enabled && luminance > 0) {
            lightSource.setDynamicLightEnabled(true);
        } else if (enabled && luminance < 1) {
            lightSource.setDynamicLightEnabled(false);
        }
    }

    /**
     * Returns the luminance from an item stack.
     *
     * @param stack the item stack
     * @param submergedInWater {@code true} if the stack is submerged in water, else {@code false}
     * @return the luminance of the item
     */
    public static int getLuminanceFromItemStack(@NotNull ItemStack stack, boolean submergedInWater) {
        return ItemLightSources.getLuminance(stack, submergedInWater);
    }

}
