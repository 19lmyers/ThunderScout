package dev.chara.thunderscout;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import dev.chara.thunderscout.bluetooth.BluetoothServerService;
import dev.chara.thunderscout.storage.preference.Preference;
import dev.chara.thunderscout.storage.preference.PreferenceRepository;

public class ThunderScout extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {

    private PreferenceRepository preferenceRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        preferenceRepository = PreferenceRepository.getInstance(getApplicationContext());

        if (!preferenceRepository.containsKey(Preference.DEVICE_NAME)) {
            preferenceRepository.setString(Preference.DEVICE_NAME, Build.MANUFACTURER + " " + Build.MODEL);
        }

        boolean runServer = preferenceRepository.getBoolean(Preference.ENABLE_BLUETOOTH_SERVER);

        if (runServer) { //I must be launching multiple instances?
            startService(new Intent(this, BluetoothServerService.class));
        }

        // NOTE: This does NOT work with PreferenceLiveData OR with a lambda - why?
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

        AppCompatDelegate.setDefaultNightMode(Integer.parseInt(preferenceRepository.getString(Preference.APP_THEME)));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Preference.ENABLE_BLUETOOTH_SERVER.getPrefName(this))) {

            if (preferenceRepository.getBoolean(Preference.ENABLE_BLUETOOTH_SERVER)) {
                startService(new Intent(this, BluetoothServerService.class));

            } else {
                stopService(new Intent(this, BluetoothServerService.class));
            }
        }
    }

}
