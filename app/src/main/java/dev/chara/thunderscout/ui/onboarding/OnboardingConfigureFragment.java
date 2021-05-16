package dev.chara.thunderscout.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import dev.chara.thunderscout.bluetooth.BluetoothServerService;
import dev.chara.thunderscout.databinding.FragmentOnboardingConfigureBinding;
import dev.chara.thunderscout.storage.preference.Preference;
import dev.chara.thunderscout.storage.preference.PreferenceRepository;

public final class OnboardingConfigureFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentOnboardingConfigureBinding binding = FragmentOnboardingConfigureBinding.inflate(inflater, container, false);

        PreferenceRepository preferenceRepository = PreferenceRepository.getInstance(getActivity().getApplicationContext());

        binding.setDeviceName(preferenceRepository.getString(Preference.DEVICE_NAME));
        binding.onboardingDeviceNameEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                preferenceRepository.setString(Preference.DEVICE_NAME, binding.getDeviceName());
            }
        });

        binding.checkboxMatchScouting.setChecked(preferenceRepository.getBoolean(Preference.ENABLE_MATCH_SCOUTING));
        binding.checkboxMatchScouting.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceRepository.setBoolean(Preference.ENABLE_MATCH_SCOUTING, isChecked);
        });

        binding.checkboxBluetoothServer.setChecked(preferenceRepository.getBoolean(Preference.ENABLE_BLUETOOTH_SERVER));
        binding.checkboxBluetoothServer.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceRepository.setBoolean(Preference.ENABLE_BLUETOOTH_SERVER, isChecked);

            if (isChecked) {
                getActivity().startService(new Intent(getContext(), BluetoothServerService.class));
            } else {
                getActivity().stopService(new Intent(getContext(), BluetoothServerService.class));
            }
        });

        // Navigation
        OnboardingFragment parent = (OnboardingFragment) getParentFragment();

        binding.buttonBack.setOnClickListener(v -> {
            parent.getViewPager().setCurrentItem(parent.getViewPager().getCurrentItem() - 1);
        });

        binding.buttonNext.setOnClickListener(v -> {
            parent.getViewPager().setCurrentItem(parent.getViewPager().getCurrentItem() + 1);
        });

        return binding.getRoot();
    }
}
