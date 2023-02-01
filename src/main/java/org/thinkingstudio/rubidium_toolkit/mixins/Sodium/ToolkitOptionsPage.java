package org.thinkingstudio.rubidium_toolkit.mixins.Sodium;

import com.google.common.collect.ImmutableList;
import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfig;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.resources.I18n;

import java.util.ArrayList;
import java.util.List;

@Mixin(SodiumOptionsGUI.class)
public class ToolkitOptionsPage
{
    @Shadow
    @Final
    private List<OptionPage> pages;

    private static final SodiumOptionsStorage sodiumOpts = new SodiumOptionsStorage();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void Toolkit(Screen prevScreen, CallbackInfo ci) {
        List<OptionGroup> groups = new ArrayList<>();

        OptionImpl<SodiumGameOptions, Boolean> fog = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("rubidium_toolkit.tools.fog.name"))
                .setTooltip(I18n.get("rubidium_toolkit.tools.fog.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> RubidiumToolkitConfig.fog.set(value),
                        (options) -> RubidiumToolkitConfig.fog.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(fog)
                .build());

        OptionImpl<SodiumGameOptions, Boolean> enableDistanceChecks = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("rubidium_toolkit.tools.enable_max_entity_distance.name"))
                .setTooltip(I18n.get("rubidium_toolkit.tools.enable_max_entity_distance.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> RubidiumToolkitConfig.enableDistanceChecks.set(value),
                        (options) -> RubidiumToolkitConfig.enableDistanceChecks.get())
                .setImpact(OptionImpact.LOW)
                .build();


        groups.add(OptionGroup
                .createBuilder()
                .add(enableDistanceChecks)
                .build());

        OptionImpl<SodiumGameOptions, Integer> maxEntityDistance = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(I18n.get("rubidium_toolkit.tools.max_entity_distance.name"))
                .setTooltip(I18n.get("rubidium_toolkit.tools.max_entity_distance.tooltip"))
                .setControl((option) -> new SliderControl(option, 16, 192, 8, ControlValueFormatter.quantity(I18n.get("rubidium_toolkit.options.unit.blocks"))))
                .setBinding(
                        (options, value) -> RubidiumToolkitConfig.maxEntityRenderDistanceSquare.set(value * value),
                        (options) ->  Math.toIntExact(Math.round(Math.sqrt(RubidiumToolkitConfig.maxEntityRenderDistanceSquare.get()))))
                .setImpact(OptionImpact.EXTREME)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxEntityDistanceVertical = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(I18n.get("rubidium_toolkit.tools.vertical_entity_distance.name"))
                .setTooltip(I18n.get("rubidium_toolkit.tools.vertical_entity_distance.tooltip"))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.quantity(I18n.get("rubidium_toolkit.options.unit.blocks"))))
                .setBinding(
                        (options, value) -> RubidiumToolkitConfig.maxEntityRenderDistanceY.set(value ),
                        (options) -> RubidiumToolkitConfig.maxEntityRenderDistanceY.get())
                .setImpact(OptionImpact.EXTREME)
                .build();


        groups.add(OptionGroup
                .createBuilder()
                .add(maxEntityDistance)
                .add(maxEntityDistanceVertical)
                .build());

        OptionImpl<SodiumGameOptions, Integer> maxTileEntityDistance = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(I18n.get("rubidium_toolkit.tools.max_block_distance.name"))
                .setTooltip(I18n.get("rubidium_toolkit.tools.max_block_distance.tooltip"))
                .setControl((option) -> new SliderControl(option, 16, 256, 8, ControlValueFormatter.quantity(I18n.get("rubidium_toolkit.options.unit.blocks"))))
                .setBinding(
                        (options, value) -> RubidiumToolkitConfig.maxTileEntityRenderDistanceSquare.set(value * value),
                        (options) -> Math.toIntExact(Math.round(Math.sqrt(RubidiumToolkitConfig.maxTileEntityRenderDistanceSquare.get()))))
                .setImpact(OptionImpact.HIGH)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxTileEntityDistanceVertical = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(I18n.get("rubidium_toolkit.tools.vertical_block_distance.name"))
                .setTooltip(I18n.get("rubidium_toolkit.tools.vertical_block_distance.tooltip"))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.quantity(I18n.get("rubidium_toolkit.options.unit.blocks"))))
                .setBinding(
                        (options, value) -> RubidiumToolkitConfig.maxTileEntityRenderDistanceY.set(value ),
                        (options) -> RubidiumToolkitConfig.maxTileEntityRenderDistanceY.get())
                .setImpact(OptionImpact.HIGH)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(maxTileEntityDistance)
                .add(maxTileEntityDistanceVertical)
                .build()
        );


        pages.add(new OptionPage(I18n.get("rubidium_toolkit.tools.options.name"),ImmutableList.copyOf(groups)));
    }
}
