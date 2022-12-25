package com.texstudio.rubidium_toolkit.mixins.Sodium;

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
public class ToolkitOptionsPage
{
    @Shadow
    @Final
    private List<OptionPage> pages;

    private static final SodiumOptionsStorage sodiumOpts = new SodiumOptionsStorage();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void RubidiumToolkit(Screen prevScreen, CallbackInfo ci)
    {
        List<OptionGroup> groups = new ArrayList<>();

        OptionImpl<SodiumGameOptions, Boolean> tureDarkness = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("rubidium_toolkit.darkness.ture.name"))
                .setTooltip(I18n.get("rubidium_toolkit.darkness.ture.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> RubidiumToolkitConfig.trueDarknessEnabled.set(value),
                        (options) -> RubidiumToolkitConfig.trueDarknessEnabled.get())
                .setImpact(OptionImpact.LOW)
                .build();

        Option<RubidiumToolkitConfig.DarknessOption> tureDarknessMode =  OptionImpl.createBuilder(RubidiumToolkitConfig.DarknessOption.class, sodiumOpts)
                .setName(I18n.get("rubidium_toolkit.darkness.ture.mode.name"))
                .setTooltip(I18n.get("rubidium_toolkit.darkness.ture.mode.tooltip"))
                .setControl(
                        (option) -> new CyclingControl<>(option, RubidiumToolkitConfig.DarknessOption.class, new String[] {
                                I18n.get("rubidium_toolkit.option.pitch_black"),
                                I18n.get("rubidium_toolkit.option.really_dark"),
                                I18n.get("rubidium_toolkit.option.dark"),
                                I18n.get("rubidium_toolkit.option.dim")
                        }
                        )
                )
                .setBinding(
                        (opts, value) -> RubidiumToolkitConfig.darknessOption.set(value),
                        (opts) -> RubidiumToolkitConfig.darknessOption.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> blockLightOnly = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("rubidium_toolkit.darkness.only_block_light.name"))
                //.setTooltip(I18n.get("rubidium_toolkit.darkness.only_block_light.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> RubidiumToolkitConfig.blockLightOnly.set(value),
                        (options) -> RubidiumToolkitConfig.blockLightOnly.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> ignoreMoonLight = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("rubidium_toolkit.darkness.ignore_moon_light.name"))
                //.setTooltip(I18n.get("rubidium_toolkit.darkness.only_block_light.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> RubidiumToolkitConfig.ignoreMoonPhase.set(value),
                        (options) -> RubidiumToolkitConfig.ignoreMoonPhase.get())
                .setImpact(OptionImpact.LOW)
                .build();
/*
        Option<Integer> minimumMoonLevel = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(I18n.get("rubidium_toolkit.darkness.position.name"))
                //.setTooltip(I18n.get("rubidium_toolkit.fps.position.tooltip"))
                .setControl((option) -> new SliderControl(option, 0, 1, 2, ControlValueFormatter.quantity("")))
                .setImpact(OptionImpact.LOW)
                .setBinding(
                        (opts, value) -> RubidiumToolkitConfig.minimumMoonLevel.set(Double.valueOf(value)),
                        (opts) -> RubidiumToolkitConfig.minimumMoonLevel.get())
                .build();
 */

        OptionImpl<SodiumGameOptions, Boolean> darkOverworld = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("rubidium_toolkit.darkness.overworld.name"))
                //.setTooltip(I18n.get("rubidium_toolkit.darkness.only_block_light.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> RubidiumToolkitConfig.darkOverworld.set(value),
                        (options) -> RubidiumToolkitConfig.darkOverworld.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> darkDefault = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("rubidium_toolkit.darkness.default.name"))
                //.setTooltip(I18n.get("rubidium_toolkit.darkness.only_block_light.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> RubidiumToolkitConfig.darkDefault.set(value),
                        (options) -> RubidiumToolkitConfig.darkDefault.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> darkNether = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("rubidium_toolkit.darkness.nether.name"))
                //.setTooltip(I18n.get("rubidium_toolkit.darkness.only_block_light.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> RubidiumToolkitConfig.darkNether.set(value),
                        (options) -> RubidiumToolkitConfig.darkNether.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> darkEnd = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("rubidium_toolkit.darkness.end.name"))
                //.setTooltip(I18n.get("rubidium_toolkit.darkness.only_block_light.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> RubidiumToolkitConfig.darkEnd.set(value),
                        (options) -> RubidiumToolkitConfig.darkEnd.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> darkSkyless = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("rubidium_toolkit.darkness.skyless.name"))
                //.setTooltip(I18n.get("rubidium_toolkit.darkness.only_block_light.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> RubidiumToolkitConfig.darkSkyless.set(value),
                        (options) -> RubidiumToolkitConfig.darkSkyless.get())
                .setImpact(OptionImpact.LOW)
                .build();


        groups.add(OptionGroup.createBuilder()
                .add(darkSkyless)
                .add(darkEnd)
                .add(darkNether)
                .add(darkDefault)
                .add(darkOverworld)
                //.add(minimumMoonLevel)
                .add(ignoreMoonLight)
                .add(blockLightOnly)
                .add(tureDarkness)
                .add(tureDarknessMode)
                .build());

        pages.add(new OptionPage(I18n.get("rubidium_toolkit.tools.options.name"),ImmutableList.copyOf(groups)));
    }
}
