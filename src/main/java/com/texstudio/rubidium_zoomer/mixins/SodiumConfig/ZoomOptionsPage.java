package com.texstudio.rubidium_zoomer.mixins.SodiumConfig;

import com.google.common.collect.ImmutableList;
import com.texstudio.rubidium_zoomer.config.RubidiumZoomerConfig;
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
import net.minecraft.client.resources.I18n;

import java.util.ArrayList;
import java.util.List;

@Pseudo
@Mixin(SodiumOptionsGUI.class)
public abstract class ZoomOptionsPage
{

    @Shadow
    @Final
    private List<OptionPage> pages;
    private static final SodiumOptionsStorage sodiumOpts = new SodiumOptionsStorage();


    @Inject(method = "<init>", at = @At("RETURN"))
    private void DynamicLights(Screen prevScreen, CallbackInfo ci)
    {
        List<OptionGroup> groups = new ArrayList<>();

        OptionImpl<SodiumGameOptions, Boolean> lowerSensitivity = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("rubidium_zoomer.zoom.lower_sensitivity.name"))
                .setTooltip(I18n.get("rubidium_zoomer.zoom.lower_sensitivity.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> RubidiumZoomerConfig.lowerZoomSensitivity.set(value),
                        (options) -> RubidiumZoomerConfig.lowerZoomSensitivity.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> zoomScrolling = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("rubidium_zoomer.zoom.scrolling.name"))
                .setTooltip(I18n.get("rubidium_zoomer.zoom.scrolling.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> RubidiumZoomerConfig.zoomScrolling.set(value),
                        (options) -> RubidiumZoomerConfig.zoomScrolling.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> zoomOverlay = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("rubidium_zoomer.zoom.overlay.name"))
                .setTooltip(I18n.get("rubidium_zoomer.zoom.overlay.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> RubidiumZoomerConfig.zoomOverlay.set(value),
                        (options) -> RubidiumZoomerConfig.zoomOverlay.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(lowerSensitivity)
                .add(zoomScrolling)
                .add(zoomOverlay)
                .build()
        );



        Option<RubidiumZoomerConfig.ZoomTransitionOptions> zoomTransition =  OptionImpl.createBuilder(RubidiumZoomerConfig.ZoomTransitionOptions.class, sodiumOpts)
                .setName(I18n.get("rubidium_zoomer.zoom.transition.name"))
                .setTooltip(I18n.get("rubidium_zoomer.zoom.transition.tooltip"))
                .setControl(
                        (option) -> new CyclingControl<>(option, RubidiumZoomerConfig.ZoomTransitionOptions.class, new String[] {
                        I18n.get("rubidium_zoomer.option.off"),
                        I18n.get("rubidium_zoomer.option.smooth")
                        }
                    )
                )
                .setBinding(
                        (opts, value) -> RubidiumZoomerConfig.zoomTransition.set(value.toString()),
                        (opts) -> RubidiumZoomerConfig.ZoomTransitionOptions.valueOf(RubidiumZoomerConfig.zoomTransition.get()))
                .setImpact(OptionImpact.LOW)
                .build();

        Option<RubidiumZoomerConfig.ZoomModes> zoomMode =  OptionImpl.createBuilder(RubidiumZoomerConfig.ZoomModes.class, sodiumOpts)
                .setName(I18n.get("rubidium_zoomer.zoom.keybind.name"))
                .setTooltip(I18n.get("rubidium_zoomer.zoom.keybind.tooltip"))
                .setControl(
                        (option) -> new CyclingControl<>(option, RubidiumZoomerConfig.ZoomModes.class, new String[] {
                        I18n.get("rubidium_zoomer.option.hold"),
                        I18n.get("rubidium_zoomer.option.toggle"),
                        I18n.get("rubidium_zoomer.option.persistent")
                        }
                    )
                )
                .setBinding(
                        (opts, value) -> RubidiumZoomerConfig.zoomMode.set(value.toString()),
                        (opts) -> RubidiumZoomerConfig.ZoomModes.valueOf(RubidiumZoomerConfig.zoomMode.get()))
                .setImpact(OptionImpact.LOW)
                .build();

        Option<RubidiumZoomerConfig.CinematicCameraOptions> cinematicCameraMode =  OptionImpl.createBuilder(RubidiumZoomerConfig.CinematicCameraOptions.class, sodiumOpts)
                .setName(I18n.get("rubidium_zoomer.zoom.cinematic_camera.name"))
                .setTooltip(I18n.get("rubidium_zoomer.zoom.cinematic_camera.tooltip"))
                .setControl((option) -> new CyclingControl<>(option, RubidiumZoomerConfig.CinematicCameraOptions.class, new String[] {
                        I18n.get("rubidium_zoomer.option.off"),
                        I18n.get("rubidium_zoomer.option.vanilla"),
                        I18n.get("rubidium_zoomer.option.multiplied")
                        }
                    )
                )
                .setBinding(
                        (opts, value) -> RubidiumZoomerConfig.cinematicCameraMode.set(value.toString()),
                        (opts) -> RubidiumZoomerConfig.CinematicCameraOptions.valueOf(RubidiumZoomerConfig.cinematicCameraMode.get()))
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(zoomTransition)
                .add(zoomMode)
                .add(cinematicCameraMode)
                .build()
        );


        pages.add(new OptionPage(I18n.get("rubidium_zoomer.zoom.option.name"), ImmutableList.copyOf(groups)));
    }
}