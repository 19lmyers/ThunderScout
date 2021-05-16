package dev.chara.thunderscout.storage.preference;

import android.content.Context;

import androidx.annotation.StringRes;

import dev.chara.thunderscout.R;
import dev.chara.thunderscout.model.type.AllianceStation;

public enum Preference {

    // System
    SHOWN_FIRST_RUN_EXPERIENCE(R.string.pref_shown_first_run_experience, false),

    SELECTED_EVENT(R.string.pref_selected_event, 1L),

    LAST_USED_MATCH_NUMBER(R.string.pref_last_used_match_number, 0),
    LAST_USED_ALLIANCE_STATION(R.string.pref_last_used_alliance_station,  AllianceStation.RED_1.name()),

    // General
    DEVICE_NAME(R.string.pref_device_name, ""),
    APP_THEME(R.string.pref_app_theme, "-1"),

    // Match scouting
    ENABLE_MATCH_SCOUTING(R.string.pref_enable_match_scouting, true),

    MS_SAVE_TO_LOCAL_DEVICE(R.string.pref_ms_save_to_local_device, true),
    MS_SEND_TO_BLUETOOTH_SERVER(R.string.pref_ms_send_to_bluetooth_server, false),
    MS_BLUETOOTH_SERVER_DEVICE(R.string.pref_ms_bluetooth_server_device, null),

    // Bluetooth server
    ENABLE_BLUETOOTH_SERVER(R.string.pref_enable_bluetooth_server, false),

    BT_SAVE_TO_LOCAL_DEVICE(R.string.pref_bt_save_to_local_device, true),
    BT_SEND_TO_BLUETOOTH_SERVER(R.string.pref_bt_send_to_bluetooth_server, false),
    BT_BLUETOOTH_SERVER_DEVICE(R.string.pref_bt_bluetooth_server_device, null);

    @StringRes
    private final int prefId;
    private final Object defaultValue;

    Preference(@StringRes int prefId, Object defaultValue) {
        this.prefId = prefId;
        this.defaultValue = defaultValue;
    }

    public String getPrefName(Context context) {
        return context.getResources().getString(prefId);
    }

    public Object getDefaultValue() {
        return defaultValue;
    }
}
