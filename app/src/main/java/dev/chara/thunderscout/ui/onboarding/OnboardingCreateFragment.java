package dev.chara.thunderscout.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import dev.chara.thunderscout.bluetooth.BluetoothServerService;
import dev.chara.thunderscout.databinding.FragmentOnboardingCreateBinding;

public final class OnboardingCreateFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentOnboardingCreateBinding binding = FragmentOnboardingCreateBinding.inflate(inflater, container, false);

        // Bluetooth server
        getActivity().startService(new Intent(getContext().getApplicationContext(), BluetoothServerService.class));

        // Navigation
        OnboardingFragment parent = (OnboardingFragment) getParentFragment();

        binding.buttonCreateTemplate.setOnClickListener(v -> {
            NavDirections action = OnboardingFragmentDirections.toNewTemplateDialog();
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });

        binding.buttonCreateEvent.setOnClickListener(v -> {
            NavDirections action = OnboardingFragmentDirections.toNewEventDialog();
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });

        binding.buttonSelectEvent.setOnClickListener(v -> {
            NavDirections action = OnboardingFragmentDirections.toEventList();
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });

        binding.buttonBack.setOnClickListener(v -> {
            parent.getViewPager().setCurrentItem(parent.getViewPager().getCurrentItem() - 1);
        });

        binding.buttonNext.setOnClickListener(v -> {
            parent.getViewPager().setCurrentItem(parent.getViewPager().getCurrentItem() + 1);
        });

        return binding.getRoot();
    }
}
