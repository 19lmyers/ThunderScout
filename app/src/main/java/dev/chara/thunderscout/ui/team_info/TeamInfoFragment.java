package dev.chara.thunderscout.ui.team_info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.tabs.TabLayoutMediator;

import dev.chara.thunderscout.R;
import dev.chara.thunderscout.databinding.FragmentTeamInfoBinding;

public final class TeamInfoFragment extends Fragment {

    private FragmentTeamInfoBinding binding;
    private TeamInfoViewModel viewModel;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTeamInfoBinding.inflate(inflater, container, false);

        // Navigation arguments
        long eventId = TeamInfoFragmentArgs.fromBundle(getArguments()).getEvent();
        String teamNumber = TeamInfoFragmentArgs.fromBundle(getArguments()).getTeamNumber();

        // Instantiate ViewModel
        TeamInfoViewModel.Factory factory = new TeamInfoViewModel.Factory(getActivity().getApplication(), eventId, teamNumber);
        viewModel = new ViewModelProvider(this, factory).get(TeamInfoViewModel.class);

        // Setup and bind views
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        binding.toolbar.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        binding.toolbar.setTitle("Match Info: Team " + teamNumber);

        MatchInfoAdapter adapter = new MatchInfoAdapter(this);
        binding.viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> tab.setText("Match " + adapter.getData().get(position).matchNumber)
        ).attach();

        // Bind layout to viewmodel
        viewModel.getMatches().observe(getViewLifecycleOwner(), adapter::setData);

        return binding.getRoot();
    }

}
