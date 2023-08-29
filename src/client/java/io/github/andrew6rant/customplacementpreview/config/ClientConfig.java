package io.github.andrew6rant.customplacementpreview.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class ClientConfig extends MidnightConfig {
    @Entry(width = 9, min = 9) public static String outlineColorAARRGGBB = "#66000000";
    @Entry(width = 9, min = 9) public static String invalidColorAARRGGBB = "#77FF0000";

    @Entry(width = 9, min = 9) public static String replaceableColorAARRGGBB = "#44000000";

    @Entry public static boolean hideInvalidEqualBlocks = true;

    @Entry public static WireFrameStyle wireframeStyle = WireFrameStyle.PLACEMENT_PREVIEW;
    public enum WireFrameStyle {
        PLACEMENT_PREVIEW, PREVIEW_AND_OULINE, OUTLINE_ONLY
    }
}
