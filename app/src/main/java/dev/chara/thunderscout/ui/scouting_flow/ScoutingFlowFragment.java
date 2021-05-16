package dev.chara.thunderscout.ui.scouting_flow;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import dev.chara.thunderscout.R;
import dev.chara.thunderscout.databinding.FragmentMatchScoutBinding;
import dev.chara.thunderscout.ui.MainActivity;
import dev.chara.thunderscout.ui.utils.InsetUtils;

public final class ScoutingFlowFragment extends Fragment implements MainActivity.OnBackPressedListener {

    private FragmentMatchScoutBinding binding;
    private ScoutingFlowViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMatchScoutBinding.inflate(inflater, container, false);

        // Instantiate ViewModel
        viewModel = new ViewModelProvider(this).get(ScoutingFlowViewModel.class);

        // Setup and bind views
        binding.toolbar.setNavigationIcon(R.drawable.ic_close_24dp);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.fieldGridView.setLayoutManager(mLayoutManager);

        DataEditorAdapter adapter = new DataEditorAdapter();
        binding.fieldGridView.setAdapter(adapter);

        binding.fieldGridView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    binding.buttonFinish.shrink();
                } else if (dy < 0) {
                    binding.buttonFinish.extend();
                }
            }
        });

        // Bind adapter to view model
        viewModel.getScoutData().observe(getViewLifecycleOwner(), data -> {
            adapter.setData(data);

            binding.progressBar.setVisibility(View.GONE);
            binding.fieldGridView.setVisibility(View.VISIBLE);
        });

        // Navigation
        NavController controller = NavHostFragment.findNavController(this);
        binding.buttonFinish.setOnClickListener((v) -> {
            if (viewModel.getScoutData().getValue().teamNumber.isEmpty()) {
                return;
            }

            viewModel.saveData();
            controller.popBackStack();
        });

        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.fieldGridView, (v, insets) -> {
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(),
                    InsetUtils.dpToPixel(80, getContext()) + insets.getSystemWindowInsetBottom());
            return insets;
        });

        ViewCompat.setOnApplyWindowInsetsListener(binding.buttonFinish, (v, insets) -> {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) v.getLayoutParams();
            params.bottomMargin = InsetUtils.dpToPixel(16, getContext()) + insets.getSystemWindowInsetBottom();
            return insets;
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Register back button listener
        MainActivity activity = (MainActivity) requireActivity();
        activity.setOnBackPressedListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Unregister back button listener
        MainActivity activity = (MainActivity) requireActivity();
        activity.setOnBackPressedListener(null);
    }

    @Override
    public boolean onBackPressed() {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Discard data?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Discard", (dialog, which) -> NavHostFragment.findNavController(this).popBackStack())
                .show();
        return false;
    }
}
