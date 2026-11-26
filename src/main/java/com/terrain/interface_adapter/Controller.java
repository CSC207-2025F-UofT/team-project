package com.terrain.interface_adapter;

import com.terrain.entity.model.feature.Feature;
import com.terrain.entity.model.feature.FeatureType;
import com.terrain.generation.GenerationSettings;
import com.terrain.generation.TerrainGenerator;
import com.terrain.ui.MapView;
import com.terrain.ui.SettingsPanel;

// Connects the UI to generation.
public class Controller {
    private final GenerationSettings settings = new GenerationSettings();
    private final TerrainGenerator generator = new TerrainGenerator();
    private final SettingsPanel settingsPanel;
    private final MapView mapView;

    public Controller(SettingsPanel settingsPanel, MapView mapView) {
        this.settingsPanel = settingsPanel;
        this.mapView = mapView;
        settingsPanel.setWorldSettings(settings.world);
        settingsPanel.setOnWorldChanged(this::onWorldSizeChanged);
    }

    // "New feature" button.
    public void onNewFeature(FeatureType type) {
        var feature = settings.addFeature(type);
        // Not sure if this is the best place for setMapSize.
        feature.setMapSize(settings.world.mapWidth(), settings.world.mapHeight());
        settingsPanel.addFeatureSection(feature);
    }

    public void onRemoveFeature(Feature feature) {
        settings.removeFeature(feature);
    }

    // Call after the world dimensions change so existing coordinate parameters clamp immediately.
    public void onWorldSizeChanged() {
        settings.features.forEach(feature -> feature.setMapSize(settings.world.mapWidth(), settings.world.mapHeight()));
    }

    // "Generate" button.
    public void onGenerate() {
        generator.generate(settings);
        mapView.display(generator.map);
    }
}
