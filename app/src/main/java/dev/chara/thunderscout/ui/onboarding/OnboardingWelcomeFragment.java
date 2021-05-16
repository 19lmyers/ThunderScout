package dev.chara.thunderscout.ui.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import dev.chara.thunderscout.databinding.FragmentOnboardingWelcomeBinding;
import dev.chara.thunderscout.storage.preference.Preference;
import dev.chara.thunderscout.storage.preference.PreferenceRepository;

public final class OnboardingWelcomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentOnboardingWelcomeBinding binding = FragmentOnboardingWelcomeBinding.inflate(inflater, container, false);

        // Navigation
        NavController controller = NavHostFragment.findNavController(this);
        OnboardingFragment parent = (OnboardingFragment) getParentFragment();

        binding.buttonGetStarted.setOnClickListener(v -> {
            parent.getViewPager().setCurrentItem(parent.getViewPager().getCurrentItem() + 1);
        });

        binding.buttonSkip.setOnClickListener(v -> {
            PreferenceRepository preferenceRepository = PreferenceRepository.getInstance(getActivity().getApplicationContext());
            preferenceRepository.setBoolean(Preference.SHOWN_FIRST_RUN_EXPERIENCE, true);
            controller.popBackStack();
        });

        return binding.getRoot();
    }
}
