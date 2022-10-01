package com.texstudio.rubidium_toolkit.dynamic_lights.api;

public interface DynamicLightsInitializer
{
    /**
     * Method called when LambDynamicLights is initialized to register custom dynamic light handlers and item light sources.
     */
    void onInitializeDynamicLights();
}
