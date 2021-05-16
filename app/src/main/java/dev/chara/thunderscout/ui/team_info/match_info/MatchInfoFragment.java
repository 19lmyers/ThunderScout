package dev.chara.thunderscout.ui.team_info.match_info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dev.chara.thunderscout.databinding.FragmentMatchInfoBinding;

public final class MatchInfoFragment extends Fragment {

    public static final String ARGUMENT_SCOUT_DATA_ID = "scoutData";

    private FragmentMatchInfoBinding binding;
    private MatchInfoViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMatchInfoBinding.inflate(inflater, container, false);

        //Navigation arguments
        long dataId = getArguments().getLong(ARGUMENT_SCOUT_DATA_ID);

        // Instantiate ViewModel
        MatchInfoViewModel.Factory factory = new MatchInfoViewModel.Factory(getActivity().getApplication(), dataId);
        viewModel = new ViewModelProvider(this, factory).get(MatchInfoViewModel.class);

        // Setup and bind views
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.dataTreeView.setLayoutManager(mLayoutManager);

        DataInfoAdapter adapter = new DataInfoAdapter();
        binding.dataTreeView.setAdapter(adapter);

        // Bind layout to viewmodel
        viewModel.getScoutData().observe(getViewLifecycleOwner(), data -> {
            // Initialization
            adapter.setData(data);

            binding.progressBar.setVisibility(View.GONE);
            binding.dataTreeView.setVisibility(View.VISIBLE);
        });

        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.dataTreeView, (v, insets) -> {
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), insets.getSystemWindowInsetBottom());
            return insets;
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
