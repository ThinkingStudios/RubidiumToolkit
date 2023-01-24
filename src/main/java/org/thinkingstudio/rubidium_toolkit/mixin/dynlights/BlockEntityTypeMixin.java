package org.thinkingstudio.rubidium_toolkit.mixin.dynlights;

import org.thinkingstudio.rubidium_toolkit.features.dynamic_lights.accessor.DynamicLightHandlerHolder;
import org.thinkingstudio.rubidium_toolkit.features.dynamic_lights.api.DynamicLightHandler;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin<T extends BlockEntity> implements DynamicLightHandlerHolder<T> {
	@Unique
	private DynamicLightHandler<T> lambdynlights$lightHandler;

	@Override
	public @Nullable DynamicLightHandler<T> lambdynlights$getDynamicLightHandler() {
		return this.lambdynlights$lightHandler;
	}

	@Override
	public void lambdynlights$setDynamicLightHandler(DynamicLightHandler<T> handler) {
		this.lambdynlights$lightHandler = handler;
	}

	@Override
	public Component lambdynlights$getName() {
		var self = (BlockEntityType<?>) (Object) this;
		var id = Registry.BLOCK_ENTITY_TYPE.getKey(self);
		if (id == null) {
			return TextComponent.EMPTY;
		}
		return new TextComponent(id.getNamespace() + ':' + id.getPath());
	}
}
