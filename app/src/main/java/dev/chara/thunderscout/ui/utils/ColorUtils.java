package dev.chara.thunderscout.ui.utils;

import android.content.Context;
import android.content.res.Configuration;

import com.google.android.material.elevation.ElevationOverlayProvider;

import dev.chara.thunderscout.model.type.AllianceStation;

public final class ColorUtils {

    public static int getToolbarColor(AllianceStation station, Context context) {
        if ((context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            return new ElevationOverlayProvider(context).compositeOverlay(context.getResources().getColor(station.getColorPrimary()), 8.0f);
        } else {
            return context.getResources().getColor(station.getColorPrimary());
        }
    }

    public static int getStatusBarColor(AllianceStation station, Context context) {
        if ((context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            return context.getResources().getColor(station.getColorPrimary());
        } else {
            return context.getResources().getColor(station.getColorPrimaryVariant());
        }
    }
}
