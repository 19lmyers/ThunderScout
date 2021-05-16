package dev.chara.thunderscout.ui.data_info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dev.chara.thunderscout.R;
import dev.chara.thunderscout.databinding.FragmentDataInfoBinding;

public final class DataInfoFragment extends Fragment {

    private FragmentDataInfoBinding binding;
    private DataInfoViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDataInfoBinding.inflate(inflater, container, false);

        //Navigation arguments
        long dataId = DataInfoFragmentArgs.fromBundle(requireArguments()).getScoutData();

        // Instantiate ViewModel
        DataInfoViewModel.Factory factory = new DataInfoViewModel.Factory(getActivity().getApplication(), dataId);
        viewModel = new ViewModelProvider(this, factory).get(DataInfoViewModel.class);

        // Setup and bind views
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        binding.toolbar.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.dataTreeView.setLayoutManager(mLayoutManager);

        DataInfoAdapter adapter = new DataInfoAdapter();
        binding.dataTreeView.setAdapter(adapter);

        // Bind layout to viewmodel
        viewModel.getScoutData().observe(getViewLifecycleOwner(), data -> {
            // Initialization
            binding.setMatch(data);
            binding.toolbar.setTitle("Match Info: Team " + data.teamNumber);

            /*binding.toolbar.setBackground(new ColorDrawable(ColorUtils.getToolbarColor(data.allianceStation, getContext())));
            getActivity().getWindow().setStatusBarColor(ColorUtils.getStatusBarColor(data.allianceStation, getContext()));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if ((getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                        != Configuration.UI_MODE_NIGHT_YES) {
                    getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                } else {
                    getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                }
            }*/

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

        /*getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                    != Configuration.UI_MODE_NIGHT_YES) {
                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            }
        }*/
    }

}
