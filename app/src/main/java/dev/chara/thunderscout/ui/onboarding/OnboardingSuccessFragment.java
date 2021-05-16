package dev.chara.thunderscout.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import dev.chara.thunderscout.bluetooth.BluetoothServerService;
import dev.chara.thunderscout.databinding.FragmentOnboardingSuccessBinding;
import dev.chara.thunderscout.storage.preference.Preference;
import dev.chara.thunderscout.storage.preference.PreferenceRepository;

public final class OnboardingSuccessFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentOnboardingSuccessBinding binding = FragmentOnboardingSuccessBinding.inflate(inflater, container, false);

        PreferenceRepository preferenceRepository = PreferenceRepository.getInstance(getActivity().getApplicationContext());

        // Bluetooth server
        if (preferenceRepository.getBoolean(Preference.ENABLE_BLUETOOTH_SERVER)) {
            getActivity().startService(new Intent(getContext(), BluetoothServerService.class));
        } else {
            getActivity().stopService(new Intent(getContext(), BluetoothServerService.class));
        }

        // Navigation
        NavController controller = NavHostFragment.findNavController(this);
        OnboardingFragment parent = (OnboardingFragment) getParentFragment();

        binding.buttonBack.setOnClickListener(v -> {
            parent.getViewPager().setCurrentItem(parent.getViewPager().getCurrentItem() - 1);
        });

        binding.buttonDone.setOnClickListener(v -> {
            preferenceRepository.setBoolean(Preference.SHOWN_FIRST_RUN_EXPERIENCE, true);
            controller.popBackStack();
        });

        return binding.getRoot();
    }
}
