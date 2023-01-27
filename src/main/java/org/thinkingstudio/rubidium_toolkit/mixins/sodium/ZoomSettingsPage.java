package org.thinkingstudio.rubidium_toolkit.mixins.sodium;


/*
@Pseudo
@Mixin(SodiumOptionsGUI.class)
public abstract class ZoomSettingsPage
{

    @Shadow
    @Final
    private List<OptionPage> pages;
    private static final SodiumOptionsStorage sodiumOpts = new SodiumOptionsStorage();


    @Inject(method = "<init>", at = @At("RETURN"))
    private void DynamicLights(Screen prevScreen, CallbackInfo ci)
    {
        List<OptionGroup> groups = new ArrayList();

        //OptionImpl<SodiumGameOptions, Boolean> lowerSensitivity = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
        //        .setName("Lower zoom Sensitivity")
        //        .setTooltip("Lowers your sensitivity when zooming to make it feel more consistent.")
        //        .setControl(TickBoxControl::new)
        //        .setBinding(
        //                (options, value) -> RubidiumToolkitConfig.lowerZoomSensitivity.set(value),
        //                (options) -> RubidiumToolkitConfig.lowerZoomSensitivity.get())
        //        .setImpact(OptionImpact.LOW)
        //        .build();

        OptionImpl<SodiumGameOptions, Boolean> zoomScrolling = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName("zoom Scrolling")
                .setTooltip("Allows using scroll wheel to adjust zoom amount.")
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> RubidiumToolkitConfig.zoomScrolling.set(value),
                        (options) -> RubidiumToolkitConfig.zoomScrolling.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> zoomOverlay = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName("zoom Overlay")
                .setTooltip("Renders a vignette overlay when zooming.")
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> RubidiumToolkitConfig.zoomOverlay.set(value),
                        (options) -> RubidiumToolkitConfig.zoomOverlay.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                //.add(lowerSensitivity)
                .add(zoomScrolling)
                .add(zoomOverlay)
                .build()
        );



        Option<RubidiumToolkitConfig.ZoomTransitionOptions> zoomTransition =  OptionImpl.createBuilder(RubidiumToolkitConfig.ZoomTransitionOptions.class, sodiumOpts)
                .setName("zoom Transition Mode")
                .setTooltip("Controls how the game changes from normal to zoomed. Off will be an instant transition.")
                .setControl((option) -> new CyclingControl<>(option, RubidiumToolkitConfig.ZoomTransitionOptions.class, new String[] { "Off", "Smooth" }))
                .setBinding(
                        (opts, value) -> RubidiumToolkitConfig.zoomTransition.set(value.toString()),
                        (opts) -> RubidiumToolkitConfig.ZoomTransitionOptions.valueOf(RubidiumToolkitConfig.zoomTransition.get()))
                .setImpact(OptionImpact.LOW)
                .build();

        Option<RubidiumToolkitConfig.ZoomModes> zoomMode =  OptionImpl.createBuilder(RubidiumToolkitConfig.ZoomModes.class, sodiumOpts)
                .setName("zoom Keybind Mode")
                .setTooltip("Hold - zoom only while the key is down.\nToggle - Lock zoom until you press the key again\nPersistent - Always zoom, if you want that, for some reason.")
                .setControl((option) -> new CyclingControl<>(option, RubidiumToolkitConfig.ZoomModes.class, new String[] { "Hold", "Toggle", "Persistent"}))
                .setBinding(
                        (opts, value) -> RubidiumToolkitConfig.zoomMode.set(value.toString()),
                        (opts) -> RubidiumToolkitConfig.ZoomModes.valueOf(RubidiumToolkitConfig.zoomMode.get()))
                .setImpact(OptionImpact.LOW)
                .build();

        //Option<RubidiumToolkitConfig.CinematicCameraOptions> cinematicCameraMode =  OptionImpl.createBuilder(RubidiumToolkitConfig.CinematicCameraOptions.class, sodiumOpts)
        //        .setName("Cinematic Camera Options")
        //        .setTooltip("Cinematic Camera Mode")
        //        .setControl((option) -> new CyclingControl<>(option, RubidiumToolkitConfig.CinematicCameraOptions.class, new String[] { "Off", "Vanilla", "Multiplied"}))
        //        .setBinding(
        //                (opts, value) -> RubidiumToolkitConfig.cinematicCameraMode.set(value.toString()),
        //                (opts) -> RubidiumToolkitConfig.CinematicCameraOptions.valueOf(RubidiumToolkitConfig.cinematicCameraMode.get()))
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