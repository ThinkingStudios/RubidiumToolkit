package org.thinkingstudio.rubidium_toolkit.mixins.sodium;

import com.google.common.collect.ImmutableList;
import net.minecraft.text.TranslatableText;
import org.thinkingstudio.rubidium_toolkit.config.ConfigEnum;
import org.thinkingstudio.rubidium_toolkit.config.ToolkitConfig;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Pseudo
@Mixin(SodiumOptionsGUI.class)
public abstract class ZoomOptionsPage {

    @Shadow
    @Final
    private List<OptionPage> pages;
    private static final SodiumOptionsStorage sodiumOpts = new SodiumOptionsStorage();


    @Inject(method = "<init>", at = @At("RETURN"))
    private void DynamicLights(Screen prevScreen, CallbackInfo ci) {
        List<OptionGroup> groups = new ArrayList<>();

        OptionImpl<SodiumGameOptions, Boolean> lowerSensitivity = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName((new TranslatableText("rubidium_toolkit.zoom.lower_sensitivity.name")).getString())
                .setTooltip((new TranslatableText("rubidium_toolkit.zoom.lower_sensitivity.tooltip")).getString())
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> ToolkitConfig.lowerZoomSensitivity.set(value),
                        (options) -> ToolkitConfig.lowerZoomSensitivity.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> zoomScrolling = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName((new TranslatableText("rubidium_toolkit.zoom.scrolling.name")).getString())
                .setTooltip((new TranslatableText("rubidium_toolkit.zoom.scrolling.tooltip")).getString())
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> ToolkitConfig.zoomScrolling.set(value),
                        (options) -> ToolkitConfig.zoomScrolling.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> zoomOverlay = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName((new TranslatableText("rubidium_toolkit.zoom.overlay.name")).getString())
                .setTooltip((new TranslatableText("rubidium_toolkit.zoom.overlay.tooltip")).getString())
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> ToolkitConfig.zoomOverlay.set(value),
                        (options) -> ToolkitConfig.zoomOverlay.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(lowerSensitivity)
                .add(zoomScrolling)
                .add(zoomOverlay)
                .build()
        );



        Option<ConfigEnum.ZoomTransitionOptions> zoomTransition =  OptionImpl.createBuilder(ConfigEnum.ZoomTransitionOptions.class, sodiumOpts)
                .setName((new TranslatableText("rubidium_toolkit.zoom.transition.name")).getString())
                .setTooltip((new TranslatableText("rubidium_toolkit.zoom.transition.tooltip")).getString())
                .setControl((option) -> new CyclingControl<>(option, ConfigEnum.ZoomTransitionOptions.class, new String[] {
                                (new TranslatableText("rubidium_toolkit.option.off")).getString(),
                                (new TranslatableText("rubidium_toolkit.option.smooth")).getString()
                        }
                    )
                )
                .setBinding(
                        (opts, value) -> ToolkitConfig.zoomTransition.set(value.toString()),
                        (opts) -> ConfigEnum.ZoomTransitionOptions.valueOf(ToolkitConfig.zoomTransition.get()))
                .setImpact(OptionImpact.LOW)
                .build();

        Option<ConfigEnum.ZoomModes> zoomMode =  OptionImpl.createBuilder(ConfigEnum.ZoomModes.class, sodiumOpts)
                .setName((new TranslatableText("rubidium_toolkit.zoom.keybind.name")).getString())
                .setTooltip((new TranslatableText("rubidium_toolkit.zoom.keybind.tooltip")).getString())
                .setControl((option) -> new CyclingControl<>(option, ConfigEnum.ZoomModes.class, new String[] {
                            (new TranslatableText("rubidium_toolkit.option.hold")).getString(),
                            (new TranslatableText("rubidium_toolkit.option.toggle")).getString(),
                            (new TranslatableText("rubidium_toolkit.option.persistent")).getString()
                        }
                    )
                )
                .setBinding(
                        (opts, value) -> ToolkitConfig.zoomMode.set(value.toString()),
                        (opts) -> ConfigEnum.ZoomModes.valueOf(ToolkitConfig.zoomMode.get()))
                .setImpact(OptionImpact.LOW)
                .build();

        Option<ConfigEnum.CinematicCameraOptions> cinematicCameraMode =  OptionImpl.createBuilder(ConfigEnum.CinematicCameraOptions.class, sodiumOpts)
                .setName((new TranslatableText("rubidium_toolkit.zoom.cinematic_camera.name")).getString())
                .setTooltip((new TranslatableText("rubidium_toolkit.zoom.cinematic_camera.tooltip")).getString())
                .setControl((option) -> new CyclingControl<>(option, ConfigEnum.CinematicCameraOptions.class, new String[] {
                            (new TranslatableText("rubidium_toolkit.option.off")).getString(),
                            (new TranslatableText("rubidium_toolkit.option.vanilla")).getString(),
                            (new TranslatableText("rubidium_toolkit.option.multiplied")).getString()
                        }
                    )
                )
                .setBinding(
                        (opts, value) -> ToolkitConfig.cinematicCameraMode.set(value.toString()),
                        (opts) -> ConfigEnum.CinematicCameraOptions.valueOf(ToolkitConfig.cinematicCameraMode.get()))
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(zoomTransition)
                .add(zoomMode)
                .add(cinematicCameraMode)
                .build()
        );


        pages.add(new OptionPage((new TranslatableText("rubidium_toolkit.zoom.options.name")).getString(), ImmutableList.copyOf(groups)));
    }
}