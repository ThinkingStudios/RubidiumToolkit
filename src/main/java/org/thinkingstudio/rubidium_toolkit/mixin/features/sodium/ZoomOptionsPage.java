package org.thinkingstudio.rubidium_toolkit.mixin.features.sodium;

import com.google.common.collect.ImmutableList;
import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfigClient;
import org.thinkingstudio.rubidium_toolkit.config.ConfigEnums;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.resources.language.I18n;

import java.util.ArrayList;
import java.util.List;

@Pseudo
@Mixin(SodiumOptionsGUI.class)
public class ZoomOptionsPage
{
    @Shadow
    @Final
    private List<OptionPage> pages;
    private static final SodiumOptionsStorage sodiumOpts = new SodiumOptionsStorage();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void OKZoomer(Screen prevScreen, CallbackInfo ci)
    {
        List<OptionGroup> groups = new ArrayList<>();

        OptionImpl<SodiumGameOptions, Boolean> reduceSensitivity = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.nullToEmpty(I18n.get("rubidium_toolkit.zoom.lower_sensitivity.name")))
                .setTooltip(Component.nullToEmpty(I18n.get("rubidium_toolkit.zoom.lower_sensitivity.tooltip")))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> RubidiumToolkitConfigClient.REDUCE_SENSITIVITY.set(value),
                        (options) -> RubidiumToolkitConfigClient.REDUCE_SENSITIVITY.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> zoomScrolling = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.nullToEmpty(I18n.get("rubidium_toolkit.zoom.scrolling.name")))
                .setTooltip(Component.nullToEmpty(I18n.get("rubidium_toolkit.zoom.scrolling.tooltip")))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> RubidiumToolkitConfigClient.ALLOW_SCROLLING.set(value),
                        (options) -> RubidiumToolkitConfigClient.ALLOW_SCROLLING.get())
                .setImpact(OptionImpact.LOW)
                .build();

        Option<ConfigEnums.ZoomOverlays> zoomOverlay = OptionImpl.createBuilder(ConfigEnums.ZoomOverlays.class, sodiumOpts)
                .setName(Component.nullToEmpty(I18n.get("rubidium_toolkit.zoom.overlay.name")))
                .setTooltip(Component.nullToEmpty(I18n.get("rubidium_toolkit.zoom.overlay.tooltip")))
                .setControl(
                        (option) -> new CyclingControl<>(option, ConfigEnums.ZoomOverlays.class, new Component[] {
                                Component.nullToEmpty(I18n.get("rubidium_toolkit.options.off")),
                                Component.nullToEmpty(I18n.get("rubidium_toolkit.options.vignette")),
                                Component.nullToEmpty(I18n.get("rubidium_toolkit.options.spyglass"))
                        })
                )
                .setBinding(
                        (options, value) -> RubidiumToolkitConfigClient.ZOOM_OVERLAY.set(ConfigEnums.ZoomOverlays.valueOf(value.toString())),
                        (options) -> ConfigEnums.ZoomOverlays.valueOf(String.valueOf(RubidiumToolkitConfigClient.ZOOM_OVERLAY.get()))
                )
                .setImpact(OptionImpact.LOW)
                .build();

        Option<ConfigEnums.ZoomTransitionOptions> zoomTransition =  OptionImpl.createBuilder(ConfigEnums.ZoomTransitionOptions.class, sodiumOpts)
                .setName(Component.nullToEmpty(I18n.get("rubidium_toolkit.zoom.transition.name")))
                .setTooltip(Component.nullToEmpty(I18n.get("rubidium_toolkit.zoom.transition.tooltip")))
                .setControl(
                        (option) -> new CyclingControl<>(option, ConfigEnums.ZoomTransitionOptions.class, new Component[] {
                                Component.nullToEmpty(I18n.get("rubidium_toolkit.options.off")),
                                Component.nullToEmpty(I18n.get("rubidium_toolkit.options.smooth")),
                                Component.nullToEmpty(I18n.get("rubidium_toolkit.options.linear"))
                        }
                        )
                )
                .setBinding(
                        (opts, value) -> RubidiumToolkitConfigClient.ZOOM_TRANSITION.set(ConfigEnums.ZoomTransitionOptions.valueOf(value.toString())),
                        (opts) -> ConfigEnums.ZoomTransitionOptions.valueOf(String.valueOf(RubidiumToolkitConfigClient.ZOOM_TRANSITION.get())))
                .setImpact(OptionImpact.LOW)
                .build();

        Option<ConfigEnums.ZoomModes> zoomMode =  OptionImpl.createBuilder(ConfigEnums.ZoomModes.class, sodiumOpts)
                .setName(Component.nullToEmpty(I18n.get("rubidium_toolkit.zoom.keybind.name")))
                .setTooltip(Component.nullToEmpty(I18n.get("rubidium_toolkit.zoom.keybind.tooltip")))
                .setControl(
                        (option) -> new CyclingControl<>(option, ConfigEnums.ZoomModes.class, new Component[] {
                                Component.nullToEmpty(I18n.get("rubidium_toolkit.options.hold")),
                                Component.nullToEmpty(I18n.get("rubidium_toolkit.options.toggle")),
                                Component.nullToEmpty(I18n.get("rubidium_toolkit.options.persistent"))
                        }
                        )
                )
                .setBinding(
                        (opts, value) -> RubidiumToolkitConfigClient.ZOOM_MODE.set(ConfigEnums.ZoomModes.valueOf(value.toString())),
                        (opts) -> ConfigEnums.ZoomModes.valueOf(String.valueOf(RubidiumToolkitConfigClient.ZOOM_MODE.get())))
                .setImpact(OptionImpact.LOW)
                .build();

        Option<ConfigEnums.CinematicCameraOptions> cinematicCameraMode =  OptionImpl.createBuilder(ConfigEnums.CinematicCameraOptions.class, sodiumOpts)
                .setName(Component.nullToEmpty(I18n.get("rubidium_toolkit.zoom.cinematic_camera.name")))
                .setTooltip(Component.nullToEmpty(I18n.get("rubidium_toolkit.zoom.cinematic_camera.tooltip")))
                .setControl(
                        (option) -> new CyclingControl<>(option, ConfigEnums.CinematicCameraOptions.class, new Component[] {
                                Component.nullToEmpty(I18n.get("rubidium_toolkit.options.off")),
                                Component.nullToEmpty(I18n.get("rubidium_toolkit.options.vanilla")),
                                Component.nullToEmpty(I18n.get("rubidium_toolkit.options.multiplied"))
                        }
                        )
                )
                .setBinding(
                        (opts, value) -> RubidiumToolkitConfigClient.CINEMATIC_CAMERA.set(ConfigEnums.CinematicCameraOptions.valueOf(value.toString())),
                        (opts) -> ConfigEnums.CinematicCameraOptions.valueOf(String.valueOf(RubidiumToolkitConfigClient.CINEMATIC_CAMERA.get())))
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> allowScrolling = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.nullToEmpty(I18n.get("rubidium_toolkit.zoom.allow_scrolling.name")))
                .setTooltip(Component.nullToEmpty(I18n.get("rubidium_toolkit.zoom.allow_scrolling.tooltip")))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> RubidiumToolkitConfigClient.ALLOW_SCROLLING.set(value),
                        (options) -> RubidiumToolkitConfigClient.ALLOW_SCROLLING.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> extraKeyBinds = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.nullToEmpty(I18n.get("rubidium_toolkit.zoom.extra_key_binds.name")))
                .setTooltip(Component.nullToEmpty(I18n.get("rubidium_toolkit.zoom.extra_key_binds.tooltip")))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> RubidiumToolkitConfigClient.EXTRA_KEY_BINDS.set(value),
                        (options) -> RubidiumToolkitConfigClient.EXTRA_KEY_BINDS.get())
                .setImpact(OptionImpact.LOW)
                .build();

        Option<ConfigEnums.SpyglassDependency> spyglassDependency =  OptionImpl.createBuilder(ConfigEnums.SpyglassDependency.class, sodiumOpts)
                .setName(Component.nullToEmpty(I18n.get("rubidium_toolkit.zoom.spyglass_dependency.name")))
                .setTooltip(Component.nullToEmpty(I18n.get("rubidium_toolkit.zoom.spyglass_dependency.tooltip")))
                .setControl(
                        (option) -> new CyclingControl<>(option, ConfigEnums.SpyglassDependency.class, new Component[] {
                                Component.nullToEmpty(I18n.get("rubidium_toolkit.options.require_item")),
                                Component.nullToEmpty(I18n.get("rubidium_toolkit.options.replace_zoom")),
                                Component.nullToEmpty(I18n.get("rubidium_toolkit.options.both"))
                        }
                        )
                )
                .setBinding(
                        (opts, value) -> RubidiumToolkitConfigClient.SPYGLASS_DEPENDENCY.set(ConfigEnums.SpyglassDependency.valueOf(value.toString())),
                        (opts) -> ConfigEnums.SpyglassDependency.valueOf(String.valueOf(RubidiumToolkitConfigClient.SPYGLASS_DEPENDENCY.get())))
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> disableOverlayNoHUD = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.nullToEmpty(I18n.get("rubidium_toolkit.zoom.disable_overlay_no_hud.name")))
                .setTooltip(Component.nullToEmpty(I18n.get("rubidium_toolkit.zoom.disable_overlay_no_hud.tooltip")))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> RubidiumToolkitConfigClient.DISABLE_OVERLAY_NO_HUD.set(value),
                        (options) -> RubidiumToolkitConfigClient.DISABLE_OVERLAY_NO_HUD.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(zoomTransition)
                .add(zoomMode)
                .add(cinematicCameraMode)
                .add(reduceSensitivity)
                .add(zoomScrolling)
                .add(zoomOverlay)
                .add(allowScrolling)
                .add(extraKeyBinds)
                .add(spyglassDependency)
                .add(disableOverlayNoHUD)
                .build()
        );


        pages.add(new OptionPage( Component.nullToEmpty(I18n.get("rubidium_toolkit.zoom.option.name")), ImmutableList.copyOf(groups)));
    }
}
