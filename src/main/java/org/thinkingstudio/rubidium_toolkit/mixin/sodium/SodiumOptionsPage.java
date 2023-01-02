package org.thinkingstudio.rubidium_toolkit.mixin.sodium;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
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
import org.thinkingstudio.rubidium_toolkit.config.ClientConfig;
import org.thinkingstudio.rubidium_toolkit.config.ConfigEnums;
import net.minecraft.client.resources.language.I18n;

import java.util.ArrayList;
import java.util.List;

@Mixin(SodiumOptionsGUI.class)
public class SodiumOptionsPage
{
    @Shadow
    @Final
    private List<OptionPage> pages;

    private static final SodiumOptionsStorage sodiumOpts = new SodiumOptionsStorage();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void RubidiumToolkit(Screen prevScreen, CallbackInfo ci)
    {
        List<OptionGroup> groups = new ArrayList<>();

/*
        Option<ConfigEnums.Complexity> displayFps =  OptionImpl.createBuilder(ConfigEnums.Complexity.class, sodiumOpts)
                .setName(Component.nullToEmpty(I18n.get("rubidium_toolkit.fps.display.name")))
                .setTooltip(Component.nullToEmpty(I18n.get("rubidium_toolkit.fps.display.tooltip")))
                .setControl((option) -> new CyclingControl<>(option, ConfigEnums.Complexity.class, new Component[] {
                        Component.nullToEmpty(I18n.get("rubidium_toolkit.option.off")),
                        Component.nullToEmpty(I18n.get("rubidium_toolkit.option.simple")),
                        Component.nullToEmpty(I18n.get("rubidium_toolkit.option.advanced"))
                }))
                .setBinding(
                        (opts, value) -> ClientConfig.FPS_COUNTER_MODE.set(ConfigEnums.Complexity.valueOf(value.toString())),
                        (opts) -> ConfigEnums.Complexity.valueOf(String.valueOf(ClientConfig.FPS_COUNTER_MODE.get())))
                .setImpact(OptionImpact.LOW)
                .build();


        Option<Integer> displayFpsPos = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.nullToEmpty(I18n.get("rubidium_toolkit.fps.position.name")))
                .setTooltip(Component.nullToEmpty(I18n.get("rubidium_toolkit.fps.position.tooltip")))
                .setControl((option) -> new SliderControl(option, 4, 64, 2, ControlValueFormatter.translateVariable("rubidium_toolkit.options.unit.pixels")))
                .setImpact(OptionImpact.LOW)
                .setBinding(
                        (opts, value) -> ClientConfig.FPS_COUNTER_POSITION.set(value),
                        (opts) -> ClientConfig.FPS_COUNTER_POSITION.get())
                .build();

        OptionImpl<SodiumGameOptions, Boolean> displayFpsAlignRight = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.nullToEmpty(I18n.get("rubidium_toolkit.fps.right_align.name")))
                .setTooltip(Component.nullToEmpty(I18n.get("rubidium_toolkit.fps.right_align.tooltip")))
            .setControl(TickBoxControl::new)
            .setBinding(
                    (options, value) -> ClientConfig.FPS_COUNTER_ALIGN_RIGHT.set(value),
                    (options) -> ClientConfig.FPS_COUNTER_ALIGN_RIGHT.get())
            .setImpact(OptionImpact.LOW)
            .build();

        groups.add(OptionGroup.createBuilder()
                .add(displayFps)
                .add(displayFpsAlignRight)
                .add(displayFpsPos)
                .build());

 */




        OptionImpl<SodiumGameOptions, Boolean> totalDarkness = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.nullToEmpty(I18n.get("rubidium_toolkit.ture_darkness.name")))
                .setTooltip(Component.nullToEmpty(I18n.get("rubidium_toolkit.ture_darkness.tooltip")))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> ClientConfig.TRUE_DARKNESS_ENABLE.set(value),
                        (options) -> ClientConfig.TRUE_DARKNESS_ENABLE.get())
                .setImpact(OptionImpact.LOW)
                .build();

        Option<ConfigEnums.DarknessOptions> totalDarknessSetting =  OptionImpl.createBuilder(ConfigEnums.DarknessOptions.class, sodiumOpts)
                .setName(Component.nullToEmpty(I18n.get("rubidium_toolkit.ture_darkness_mode.name")))
                .setTooltip(Component.nullToEmpty(I18n.get("rubidium_toolkit.ture_darkness_mode.tooltip")))
                .setControl((option) -> new CyclingControl<>(option, ConfigEnums.DarknessOptions.class, new Component[] {
                        Component.nullToEmpty(I18n.get("rubidium_toolkit.options.pitch_black")),
                        Component.nullToEmpty(I18n.get("rubidium_toolkit.options.really_dark")),
                        Component.nullToEmpty(I18n.get("rubidium_toolkit.options.dark")),
                        Component.nullToEmpty(I18n.get("rubidium_toolkit.options.dim"))
                }))
                .setBinding(
                        (opts, value) -> ClientConfig.DARKNESS_OPTIONS.set(value),
                        (opts) -> ClientConfig.DARKNESS_OPTIONS.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(totalDarkness)
                .add(totalDarknessSetting)
                .build());



        OptionImpl<SodiumGameOptions, Boolean> enableDistanceChecks = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.nullToEmpty("Enable Max Entity Distance"))
                .setTooltip(Component.nullToEmpty("Toggles off entity culling."))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> ClientConfig.enableDistanceChecks.set(value),
                        (options) -> ClientConfig.enableDistanceChecks.get())
                .setImpact(OptionImpact.LOW)
                .build();


        groups.add(OptionGroup
                .createBuilder()
                .add(enableDistanceChecks)
                .build()
        );

        OptionImpl<SodiumGameOptions, Integer> maxEntityDistance = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.nullToEmpty("Max Entity Distance"))
                .setTooltip(Component.nullToEmpty("Hides and does not tick entities beyond this many blocks. Huge performance increase, especially around modded farms."))
                .setControl((option) -> new SliderControl(option, 16, 192, 8, ControlValueFormatter.translateVariable("Blocks")))
                .setBinding(
                        (options, value) -> ClientConfig.maxEntityRenderDistanceSquare.set(value * value),
                        (options) ->  Math.toIntExact(Math.round(Math.sqrt(ClientConfig.maxEntityRenderDistanceSquare.get()))))
                .setImpact(OptionImpact.HIGH)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxEntityDistanceVertical = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.nullToEmpty("Vertical Entity Distance"))
                .setTooltip(Component.nullToEmpty("Hides and does not tick entities underneath this many blocks, improving performance above caves. This should ideally be set lower than the horizontal distance."))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.translateVariable("Blocks")))
                .setBinding(
                        (options, value) -> ClientConfig.maxEntityRenderDistanceY.set(value ),
                        (options) -> ClientConfig.maxEntityRenderDistanceY.get())
                .setImpact(OptionImpact.HIGH)
                .build();


        groups.add(OptionGroup
                .createBuilder()
                .add(maxEntityDistance)
                .add(maxEntityDistanceVertical)
                .build()
        );





        OptionImpl<SodiumGameOptions, Integer> maxTileEntityDistance = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.nullToEmpty("Max Tile Distance"))
                .setTooltip(Component.nullToEmpty("Hides block entities beyond this many blocks. Huge performance increase, especially around lots of modded machines."))
                .setControl((option) -> new SliderControl(option, 16, 256, 8, ControlValueFormatter.translateVariable("Blocks")))
                .setBinding(
                        (options, value) -> ClientConfig.maxTileEntityRenderDistanceSquare.set(value * value),
                        (options) -> Math.toIntExact(Math.round(Math.sqrt(ClientConfig.maxTileEntityRenderDistanceSquare.get()))))
                .setImpact(OptionImpact.HIGH)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxTileEntityDistanceVertical = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.nullToEmpty("Vertical Tile Distance"))
                .setTooltip(Component.nullToEmpty("Hides block entities underneath this many blocks, improving performance above caves (if you have your machines in caves, for some reason). This should ideally be set lower than the horizontal distance."))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.translateVariable("Blocks")))
                .setBinding(
                        (options, value) -> ClientConfig.maxTileEntityRenderDistanceY.set(value ),
                        (options) -> ClientConfig.maxTileEntityRenderDistanceY.get())
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
