package io.github.andrew6rant.customplacementpreview;

import eu.midnightdust.lib.config.MidnightConfig;
import io.github.andrew6rant.customplacementpreview.config.ClientConfig;
import net.fabricmc.api.ClientModInitializer;

public class CustomPlacementPreviewClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MidnightConfig.init("custom-placement-preview", ClientConfig.class);
	}
}