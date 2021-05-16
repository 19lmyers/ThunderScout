package dev.chara.thunderscout.ui.onboarding;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Objects;

import dev.chara.thunderscout.databinding.FragmentOnboardingBinding;
import dev.chara.thunderscout.storage.preference.Preference;
import dev.chara.thunderscout.storage.preference.PreferenceRepository;
import dev.chara.thunderscout.ui.MainActivity;

public final class OnboardingFragment extends Fragment implements MainActivity.OnBackPressedListener {

    private FragmentOnboardingBinding binding;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentOnboardingBinding.inflate(inflater, container, false);

        // Bind layout to adapter
        binding.pager.setAdapter(new OnboardingAdapter(this));

        ViewCompat.setOnApplyWindowInsetsListener(binding.pager, (v, insets) -> {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) v.getLayoutParams();
            params.bottomMargin = insets.getSystemWindowInsetBottom();
            return insets;
        });

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Register back button listener
        MainActivity activity = (MainActivity) Objects.requireNonNull(getActivity());
        activity.setOnBackPressedListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Unregister back button listener
        MainActivity activity = (MainActivity) Objects.requireNonNull(getActivity());
        activity.setOnBackPressedListener(null);
    }

    @Override
    public boolean onBackPressed() {
        if (binding.pager.getCurrentItem() > 0) {
            binding.pager.setCurrentItem(binding.pager.getCurrentItem() - 1);
            return false;
        } else {
            PreferenceRepository preferenceRepository = PreferenceRepository.getInstance(getActivity().getApplicationContext());
            preferenceRepository.setBoolean(Preference.SHOWN_FIRST_RUN_EXPERIENCE, true);
            return true;
        }
    }

    ViewPager2 getViewPager() {
        return binding.pager;
    }

    static final class OnboardingAdapter extends FragmentStateAdapter {

        OnboardingAdapter(@NonNull OnboardingFragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new OnboardingWelcomeFragment();
            } else if (position == 1) {
                return new OnboardingCreateFragment();
            } else if (position == 2) {
                return new OnboardingConfigureFragment();
            } else {
                return new OnboardingSuccessFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}
