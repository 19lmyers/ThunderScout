package dev.chara.thunderscout.ui.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.ViewCompat;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import dev.chara.thunderscout.R;
import dev.chara.thunderscout.databinding.FragmentSettingsBinding;

public class SettingsFragment extends PreferenceFragmentCompat {

    protected FragmentSettingsBinding binding;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentSettingsBinding.bind(view);

        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        binding.toolbar.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.listContainer, (v, insets) -> {
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), insets.getSystemWindowInsetBottom());
            return insets;
        });
    }

    @SuppressLint("InlinedApi")
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);

        findPreference(getResources().getString(R.string.pref_navigation_settings_general))
                .setOnPreferenceClickListener(preference -> {
                    NavDirections action = SettingsFragmentDirections.toGeneralSettings();
                    Navigation.findNavController(binding.getRoot()).navigate(action);
                    return true;
                });

        findPreference(getResources().getString(R.string.pref_navigation_event_list))
                .setOnPreferenceClickListener(preference -> {
                    NavDirections action = SettingsFragmentDirections.toEventList();
                    Navigation.findNavController(binding.getRoot()).navigate(action);
                    return true;
                });

        findPreference(getResources().getString(R.string.pref_navigation_template_list))
                .setOnPreferenceClickListener(preference -> {
                    NavDirections action = SettingsFragmentDirections.toTemplateList();
                    Navigation.findNavController(binding.getRoot()).navigate(action);
                    return true;
                });

        findPreference(getResources().getString(R.string.pref_enable_match_scouting))
                .setOnPreferenceClickListener(preference -> {
                    NavDirections action = SettingsFragmentDirections.toMatchScoutSettings();
                    Navigation.findNavController(binding.getRoot()).navigate(action);
                    return true;
                });

        findPreference(getResources().getString(R.string.pref_enable_bluetooth_server))
                .setOnPreferenceClickListener(preference -> {
                    NavDirections action = SettingsFragmentDirections.toBluetoothServerSettings();
                    Navigation.findNavController(binding.getRoot()).navigate(action);
                    return true;
                });

        findPreference(getResources().getString(R.string.pref_navigation_notifications))
                .setOnPreferenceClickListener(preference -> {
                    Intent intent = new Intent();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getActivity().getPackageName());
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                        intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getActivity().getPackageName());
                    } else {
                        intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                        intent.putExtra("app_package", getActivity().getPackageName());
                        intent.putExtra("app_uid", getActivity().getApplicationInfo().uid);
                    }
                    startActivity(intent);
                    return true;
                });

        findPreference(getResources().getString(R.string.pref_navigation_app_info))
                .setOnPreferenceClickListener(preference -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                    startActivity(intent);
                    return true;
                });
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener bindPreferenceSummaryToValueListener = (preference, value) -> {
        String stringValue = value.toString();

        if (preference instanceof EditTextPreference) {
            preference.setSummary(stringValue);
        }

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list.
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);

            // Set the summary to reflect the new value.
            preference.setSummary(
                    index >= 0
                            ? listPreference.getEntries()[index]
                            : null);

        }
        return true;
    };

    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(bindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        bindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    public static final class GeneralSettingsFragment extends SettingsFragment {

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            binding = FragmentSettingsBinding.bind(view);
            binding.toolbar.setTitle("General settings");
        }

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            addPreferencesFromResource(R.xml.preferences_general);

            bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.pref_device_name)));
            bindPreferenceSummaryToValue(findPreference(getResources().getString(R.string.pref_app_theme)));

            findPreference(getResources().getString(R.string.pref_app_theme))
                    .setOnPreferenceChangeListener((preference, newValue) -> {
                        bindPreferenceSummaryToValueListener.onPreferenceChange(preference, newValue);

                        AppCompatDelegate.setDefaultNightMode(Integer.valueOf((String) newValue));
                        getActivity().recreate();
                        return true;
                    });
        }
    }

    public static final class MatchScoutSettingsFragment extends SettingsFragment {
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            binding = FragmentSettingsBinding.bind(view);
            binding.toolbar.setTitle("Match scouting");
        }

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            addPreferencesFromResource(R.xml.preferences_match_scouting);
        }
    }

    public static final class BluetoothServerSettingsFragment extends SettingsFragment {
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            binding = FragmentSettingsBinding.bind(view);
            binding.toolbar.setTitle("Bluetooth server");
        }

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            addPreferencesFromResource(R.xml.preferences_bluetooth_server);
        }
    }
}
