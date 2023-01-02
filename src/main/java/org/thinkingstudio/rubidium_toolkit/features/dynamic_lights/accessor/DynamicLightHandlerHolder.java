package org.thinkingstudio.rubidium_toolkit.features.dynamic_lights.accessor;

import org.thinkingstudio.rubidium_toolkit.features.dynamic_lights.api.DynamicLightHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
@ApiStatus.NonExtendable
public interface DynamicLightHandlerHolder<T> {
	@Nullable DynamicLightHandler<T> lambdynlights$getDynamicLightHandler();

	void lambdynlights$setDynamicLightHandler(DynamicLightHandler<T> handler);

	Component lambdynlights$getName();

	@SuppressWarnings("unchecked")
	static <T extends Entity> DynamicLightHandlerHolder<T> cast(EntityType<T> entityType) {
		return (DynamicLightHandlerHolder<T>) entityType;
	}

	@SuppressWarnings("unchecked")
	static <T extends BlockEntity> DynamicLightHandlerHolder<T> cast(BlockEntityType<T> entityType) {
		return (DynamicLightHandlerHolder<T>) entityType;
	}
}
