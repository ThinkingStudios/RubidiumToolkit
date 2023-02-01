package org.thinkingstudio.rubidium_toolkit.mixins.sodium;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;

import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraft.client.resources.I18n;
import org.lwjgl.system.CallbackI;
import org.thinkingstudio.rubidium_toolkit.features.dynamiclights.DynamicLightsFeatures;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.thinkingstudio.rubidium_toolkit.config.ConfigEnum;
import org.thinkingstudio.rubidium_toolkit.config.ToolkitConfig;

import java.util.ArrayList;
import java.util.List;

@Pseudo
@Mixin(SodiumOptionsGUI.class)
public abstract class DynLightsSettingsPage {

    @Shadow
    @Final
    private List<OptionPage> pages;

    private static final SodiumOptionsStorage dynamicLightsOpts = new SodiumOptionsStorage();


    @Inject(method = "<init>", at = @At("RETURN"))
    private void DynamicLights(Screen prevScreen, CallbackInfo ci) {
        List<OptionGroup> groups = new ArrayList<>();

        OptionImpl<SodiumGameOptions, ConfigEnum.QualityMode> qualityMode = OptionImpl.createBuilder(ConfigEnum.QualityMode.class, dynamicLightsOpts)
                .setName(I18n.get("rubidium_toolkit.dynlights.speed.name"))
                .setTooltip(I18n.get("rubidium_toolkit.dynlights.speed.tooltip"))
                .setControl(
                        (option) -> new CyclingControl<>(option, ConfigEnum.QualityMode.class, new String[] {
                                I18n.get("rubidium_toolkit.options.off"),
                                I18n.get("rubidium_toolkit.options.slow"),
                                I18n.get("rubidium_toolkit.options.fast"),
                                I18n.get("rubidium_toolkit.options.realtime")
                        }))
                .setBinding((options, value) -> {
                    ToolkitConfig.Quality.set(value.toString());
                    DynamicLightsFeatures.clearLightSources();
                    }, (options) -> ConfigEnum.QualityMode.valueOf(ToolkitConfig.Quality.get()))
                .setImpact(OptionImpact.MEDIUM)
                .build();


        OptionImpl<SodiumGameOptions, Boolean> entityLighting = OptionImpl.createBuilder(Boolean.class, dynamicLightsOpts)
                .setName(I18n.get("rubidium_toolkit.dynlights.entity_lights.name"))
                .setTooltip(I18n.get("rubidium_toolkit.dynlights.entity_lights.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> ToolkitConfig.EntityLighting.set(value),
                        (options) -> ToolkitConfig.EntityLighting.get())
                .setImpact(OptionImpact.MEDIUM)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> tileEntityLighting = OptionImpl.createBuilder(Boolean.class, dynamicLightsOpts)
                .setName(I18n.get("rubidium_toolkit.dynlights.block_lights.name"))
                .setTooltip(I18n.get("rubidium_toolkit.dynlights.block_lights.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> ToolkitConfig.TileEntityLighting.set(value),
                        (options) -> ToolkitConfig.TileEntityLighting.get())
                .setImpact(OptionImpact.MEDIUM)
                .build();

        groups.add(OptionGroup
            .createBuilder()
                .add(qualityMode)
                .add(entityLighting)
                .add(tileEntityLighting)
            .build()
        );

        pages.add(new OptionPage(I18n.get("rubidium_toolkit.dynlights.options.name"), ImmutableList.copyOf(groups)));
    }


}