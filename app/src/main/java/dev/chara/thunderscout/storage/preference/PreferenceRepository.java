package dev.chara.thunderscout.storage.preference;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;

public final class PreferenceRepository {

    private static PreferenceRepository instance;

    private Context appContext;
    private SharedPreferences preferences;

    private PreferenceRepository(Context appContext) {
        this.appContext = appContext;
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    public static PreferenceRepository getInstance(Context appContext) {
        if (instance == null) {
            instance = new PreferenceRepository(appContext);
        }
        return instance;
    }

    public boolean containsKey(Preference preference) {
        return preferences.contains(preference.getPrefName(appContext));
    }

    public boolean getBoolean(Preference preference) {
        return preferences.getBoolean(preference.getPrefName(appContext), (Boolean) preference.getDefaultValue());
    }

    public LiveData<Boolean> getBooleanObservable(Preference preference) {
        return new PreferenceLiveData<Boolean>(preferences, preference.getPrefName(appContext), (Boolean) preference.getDefaultValue()) {
            @Override
            Boolean getValueFromPreferences(String key, Boolean defValue) {
                return preferences.getBoolean(key, defValue);
            }
        };
    }

    public void setBoolean(Preference preference, boolean value) {
        preferences.edit().putBoolean(preference.getPrefName(appContext), value).apply();
    }

    public int getInteger(Preference preference) {
        return preferences.getInt(preference.getPrefName(appContext), (Integer) preference.getDefaultValue());
    }

    public LiveData<Integer> getIntegerObservable(Preference preference) {
        return new PreferenceLiveData<Integer>(preferences, preference.getPrefName(appContext), (Integer) preference.getDefaultValue()) {
            @Override
            Integer getValueFromPreferences(String key, Integer defValue) {
                return preferences.getInt(key, defValue);
            }
        };
    }

    public void setInteger(Preference preference, int value) {
        preferences.edit().putInt(preference.getPrefName(appContext), value).apply();
    }

    public long getLong(Preference preference) {
        return preferences.getLong(preference.getPrefName(appContext), (Long) preference.getDefaultValue());
    }

    public LiveData<Long> getLongObservable(Preference preference) {
        return new PreferenceLiveData<Long>(preferences, preference.getPrefName(appContext), (Long) preference.getDefaultValue()) {
            @Override
            Long getValueFromPreferences(String key, Long defValue) {
                return preferences.getLong(key, defValue);
            }
        };
    }

    public void setLong(Preference preference, long value) {
        preferences.edit().putLong(preference.getPrefName(appContext), value).apply();
    }

    public String getString(Preference preference) {
        return preferences.getString(preference.getPrefName(appContext), (String) preference.getDefaultValue());
    }

    public LiveData<String> getStringObservable(Preference preference) {
        return new PreferenceLiveData<String>(preferences, preference.getPrefName(appContext), (String) preference.getDefaultValue()) {
            @Override
            String getValueFromPreferences(String key, String defValue) {
                return preferences.getString(key, defValue);
            }
        };
    }

    public void setString(Preference preference, String value) {
        preferences.edit().putString(preference.getPrefName(appContext), value).apply();
    }
}
