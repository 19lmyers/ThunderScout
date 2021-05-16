package dev.chara.thunderscout.storage.preference;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

/**
 * Source: https://gist.github.com/idish/f46a8327da7f293f943a5bda31078c95
 */
abstract class PreferenceLiveData<T> extends LiveData<T> {

    private SharedPreferences sharedPrefs;
    private String key;
    private T defValue;

    PreferenceLiveData(SharedPreferences prefs, String key, T defValue) {
        this.sharedPrefs = prefs;
        this.key = key;
        this.defValue = defValue;
    }

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (PreferenceLiveData.this.key.equals(key)) {
                setValue(getValueFromPreferences(key, defValue));
            }
        }
    };

    abstract T getValueFromPreferences(String key, T defValue);

    @Override
    protected void onActive() {
        super.onActive();

        setValue(getValueFromPreferences(key, defValue));
        sharedPrefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    protected void onInactive() {
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);

        super.onInactive();
    }
}