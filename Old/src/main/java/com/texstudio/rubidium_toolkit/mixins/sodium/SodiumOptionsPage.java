package com.texstudio.rubidium_toolkit.mixins.sodium;

import com.google.common.collect.ImmutableList;
import com.texstudio.rubidium_toolkit.config.RubidiumToolkitConfig;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
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

        Option<RubidiumToolkitConfig.Complexity> displayFps =  OptionImpl.createBuilder(RubidiumToolkitConfig.Complexity.class, sodiumOpts)
                .setName(I18n.get("rubidium_toolkit.fps.display.name"))
                .setTooltip(I18n.get("rubidium_toolkit.fps.display.tooltip"))
                .setControl(
                        (option) -> new CyclingControl<>(option, RubidiumToolkitConfig.Complexity.class, new String[] {
                                I18n.get("rubidium_toolkit.option.off"),
                                I18n.get("rubidium_toolkit.option.simple"),
                                I18n.get("rubidium_toolkit.option.advanced")
                        }
                        )
                )
                .setBinding(
                        (opts, value) -> RubidiumToolkitConfig.fpsCounterMode.set(value.toString()),
                        (opts) -> RubidiumToolkitConfig.Complexity.valueOf(RubidiumToolkitConfig.fpsCounterMode.get()))
                .setImpact(OptionImpact.LOW)
                .build();


        Option<Integer> displayFpsPos = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(I18n.get("rubidium_toolkit.fps.position.name"))
                .setTooltip(I18n.get("rubidium_toolkit.fps.position.tooltip"))
                .setControl((option) -> new SliderControl(option, 4, 64, 2, ControlValueFormatter.quantity(I18n.get("rubidium_toolkit.option.unit.pixels"))))
                .setImpact(OptionImpact.LOW)
                .setBinding(
                        (opts, value) -> RubidiumToolkitConfig.fpsCounterPosition.set(value),
                        (opts) -> RubidiumToolkitConfig.fpsCounterPosition.get())
                .build();

        OptionImpl<SodiumGameOptions, Boolean> displayFpsAlignRight = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("rubidium_toolkit.fps.right_align.name"))
                .setTooltip(I18n.get("rubidium_toolkit.fps.right_align.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> RubidiumToolkitConfig.fpsCounterAlignRight.set(value),
                        (options) -> RubidiumToolkitConfig.fpsCounterAlignRight.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(displayFps)
                .add(displayFpsAlignRight)
                .add(displayFpsPos)
                .build());

        pages.add(new OptionPage(I18n.get("rubidium_toolkit.tools.options.name"),ImmutableList.copyOf(groups)));
    }
}
