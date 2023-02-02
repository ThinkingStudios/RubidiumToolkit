package org.thinkingstudio.rubidium_toolkit.mixins.sodium;


/*
@Pseudo
@Mixin(SodiumOptionsGUI.class)
public abstract class ZoomSettingsPage {

    @Shadow
    @Final
    private List<OptionPage> pages;
    private static final SodiumOptionsStorage sodiumOpts = new SodiumOptionsStorage();


    @Inject(method = "<init>", at = @At("RETURN"))
    private void DynamicLights(Screen prevScreen, CallbackInfo ci) {
        List<OptionGroup> groups = new ArrayList();

        //OptionImpl<SodiumGameOptions, Boolean> lowerSensitivity = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
        //        .setName("Lower zoom Sensitivity")
        //        .setTooltip("Lowers your sensitivity when zooming to make it feel more consistent.")
        //        .setControl(TickBoxControl::new)
        //        .setBinding(
        //                (options, value) -> ToolkitConfig.lowerZoomSensitivity.set(value),
        //                (options) -> ToolkitConfig.lowerZoomSensitivity.get())
        //        .setImpact(OptionImpact.LOW)
        //        .build();

        OptionImpl<SodiumGameOptions, Boolean> zoomScrolling = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName("zoom Scrolling")
                .setTooltip("Allows using scroll wheel to adjust zoom amount.")
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> ToolkitConfig.zoomScrolling.set(value),
                        (options) -> ToolkitConfig.zoomScrolling.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> zoomOverlay = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName("zoom Overlay")
                .setTooltip("Renders a vignette overlay when zooming.")
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> ToolkitConfig.zoomOverlay.set(value),
                        (options) -> ToolkitConfig.zoomOverlay.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                //.add(lowerSensitivity)
                .add(zoomScrolling)
                .add(zoomOverlay)
                .build()
        );



        Option<ToolkitConfig.ZoomTransitionOptions> zoomTransition =  OptionImpl.createBuilder(ToolkitConfig.ZoomTransitionOptions.class, sodiumOpts)
                .setName("zoom Transition Mode")
                .setTooltip("Controls how the game changes from normal to zoomed. Off will be an instant transition.")
                .setControl((option) -> new CyclingControl<>(option, ToolkitConfig.ZoomTransitionOptions.class, new String[] { "Off", "Smooth" }))
                .setBinding(
                        (opts, value) -> ToolkitConfig.zoomTransition.set(value.toString()),
                        (opts) -> ToolkitConfig.ZoomTransitionOptions.valueOf(ToolkitConfig.zoomTransition.get()))
                .setImpact(OptionImpact.LOW)
                .build();

        Option<ToolkitConfig.ZoomModes> zoomMode =  OptionImpl.createBuilder(ToolkitConfig.ZoomModes.class, sodiumOpts)
                .setName("zoom Keybind Mode")
                .setTooltip("Hold - zoom only while the key is down.\nToggle - Lock zoom until you press the key again\nPersistent - Always zoom, if you want that, for some reason.")
                .setControl((option) -> new CyclingControl<>(option, ToolkitConfig.ZoomModes.class, new String[] { "Hold", "Toggle", "Persistent"}))
                .setBinding(
                        (opts, value) -> ToolkitConfig.zoomMode.set(value.toString()),
                        (opts) -> ToolkitConfig.ZoomModes.valueOf(ToolkitConfig.zoomMode.get()))
                .setImpact(OptionImpact.LOW)
                .build();

        //Option<ToolkitConfig.CinematicCameraOptions> cinematicCameraMode =  OptionImpl.createBuilder(ToolkitConfig.CinematicCameraOptions.class, sodiumOpts)
        //        .setName("Cinematic Camera Options")
        //        .setTooltip("Cinematic Camera Mode")
        //        .setControl((option) -> new CyclingControl<>(option, ToolkitConfig.CinematicCameraOptions.class, new String[] { "Off", "Vanilla", "Multiplied"}))
        //        .setBinding(
        //                (opts, value) -> ToolkitConfig.cinematicCameraMode.set(value.toString()),
        //                (opts) -> ToolkitConfig.CinematicCameraOptions.valueOf(ToolkitConfig.cinematicCameraMode.get()))
        //        .setImpact(OptionImpact.LOW)
        //        .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(zoomTransition)
                .add(zoomMode)
                //.add(cinematicCameraMode)
                .build()
        );


        pages.add(new OptionPage("zoom", ImmutableList.copyOf(groups)));
    }
}

*/