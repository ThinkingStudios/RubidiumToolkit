package org.thinkingstudio.rubidium_toolkit.mixin.features.sodium;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfigClient;
import net.minecraft.client.resources.language.I18n;

import java.util.ArrayList;
import java.util.List;

@Mixin(SodiumOptionsGUI.class)
public class ToolkitOptionsPage {
    @Shadow
    @Final
    private List<OptionPage> pages;

    private static final SodiumOptionsStorage sodiumOpts = new SodiumOptionsStorage();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void RubidiumToolkit(Screen prevScreen, CallbackInfo ci) {
        List<OptionGroup> groups = new ArrayList<>();

        OptionImpl<SodiumGameOptions, Boolean> enableDistanceChecks = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.nullToEmpty(I18n.get("rubidium_toolkit.tools.enable_max_entity_distance.name")))
                .setTooltip(Component.nullToEmpty(I18n.get("rubidium_toolkit.tools.enable_max_entity_distance.tooltip")))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> RubidiumToolkitConfigClient.enableDistanceChecks.set(value),
                        (options) -> RubidiumToolkitConfigClient.enableDistanceChecks.get())
                .setImpact(OptionImpact.LOW)
                .build();


        groups.add(OptionGroup
                .createBuilder()
                .add(enableDistanceChecks)
                .build()
        );

        OptionImpl<SodiumGameOptions, Integer> maxEntityDistance = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.nullToEmpty(I18n.get("rubidium_toolkit.tools.max_entity_distance.name")))
                .setTooltip(Component.nullToEmpty(I18n.get("rubidium_toolkit.tools.max_entity_distance.tooltip")))
                .setControl((option) -> new SliderControl(option, 16, 192, 8, ControlValueFormatter.translateVariable("rubidium_toolkit.options.unit.blocks")))
                .setBinding(
                        (options, value) -> RubidiumToolkitConfigClient.maxEntityRenderDistanceSquare.set(value * value),
                        (options) ->  Math.toIntExact(Math.round(Math.sqrt(RubidiumToolkitConfigClient.maxEntityRenderDistanceSquare.get()))))
                .setImpact(OptionImpact.HIGH)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxEntityDistanceVertical = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.nullToEmpty(I18n.get("rubidium_toolkit.tools.vertical_entity_distance.name")))
                .setTooltip(Component.nullToEmpty(I18n.get("rubidium_toolkit.tools.vertical_entity_distance.tooltip")))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.translateVariable("rubidium_toolkit.options.unit.blocks")))
                .setBinding(
                        (options, value) -> RubidiumToolkitConfigClient.maxEntityRenderDistanceY.set(value ),
                        (options) -> RubidiumToolkitConfigClient.maxEntityRenderDistanceY.get())
                .setImpact(OptionImpact.HIGH)
                .build();


        groups.add(OptionGroup
                .createBuilder()
                .add(maxEntityDistance)
                .add(maxEntityDistanceVertical)
                .build()
        );


        OptionImpl<SodiumGameOptions, Integer> maxTileEntityDistance = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.nullToEmpty(I18n.get("rubidium_toolkit.tools.max_block_distance.name")))
                .setTooltip(Component.nullToEmpty(I18n.get("rubidium_toolkit.tools.max_block_distance.tooltip")))
                .setControl((option) -> new SliderControl(option, 16, 256, 8, ControlValueFormatter.translateVariable("rubidium_toolkit.options.unit.blocks")))
                .setBinding(
                        (options, value) -> RubidiumToolkitConfigClient.maxBlockEntityRenderDistanceSquare.set(value * value),
                        (options) -> Math.toIntExact(Math.round(Math.sqrt(RubidiumToolkitConfigClient.maxBlockEntityRenderDistanceSquare.get()))))
                .setImpact(OptionImpact.HIGH)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxTileEntityDistanceVertical = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.nullToEmpty(I18n.get("rubidium_toolkit.tools.vertical_block_distance.name")))
                .setTooltip(Component.nullToEmpty(I18n.get("rubidium_toolkit.tools.vertical_block_distance.tooltip")))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.translateVariable("rubidium_toolkit.options.unit.blocks")))
                .setBinding(
                        (options, value) -> RubidiumToolkitConfigClient.maxBlockEntityRenderDistanceY.set(value ),
                        (options) -> RubidiumToolkitConfigClient.maxBlockEntityRenderDistanceY.get())
                .setImpact(OptionImpact.HIGH)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(maxTileEntityDistance)
                .add(maxTileEntityDistanceVertical)
                .build()
        );


        pages.add(new OptionPage( Component.nullToEmpty(I18n.get("rubidium_toolkit.tools.option.name")), ImmutableList.copyOf(groups)));
    }
}
