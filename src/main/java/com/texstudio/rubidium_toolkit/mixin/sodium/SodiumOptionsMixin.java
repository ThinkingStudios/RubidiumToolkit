package com.texstudio.rubidium_toolkit.mixin.sodium;

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
import com.texstudio.rubidium_toolkit.config.ClientConfig;
import com.texstudio.rubidium_toolkit.config.ConfigEnums;
import net.minecraft.client.resources.language.I18n;

import java.util.ArrayList;
import java.util.List;

@Mixin(SodiumOptionsGUI.class)
public class SodiumOptionsMixin
{
    @Shadow
    @Final
    private List<OptionPage> pages;

    private static final SodiumOptionsStorage sodiumOpts = new SodiumOptionsStorage();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void RubidiumToolkit(Screen prevScreen, CallbackInfo ci)
    {
        List<OptionGroup> groups = new ArrayList<>();

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

        pages.add(new OptionPage( Component.nullToEmpty(I18n.get("rubidium_toolkit.tools.option.name")), ImmutableList.copyOf(groups)));
    }
}
