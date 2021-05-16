package dev.chara.thunderscout.ui.data_list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import dev.chara.thunderscout.BuildConfig;
import dev.chara.thunderscout.R;
import dev.chara.thunderscout.databinding.FragmentDataListBinding;
import dev.chara.thunderscout.model.ScoutData;
import dev.chara.thunderscout.storage.preference.Preference;
import dev.chara.thunderscout.storage.preference.PreferenceRepository;
import dev.chara.thunderscout.ui.MainActivity;
import dev.chara.thunderscout.ui.utils.InsetUtils;

public final class DataListFragment extends Fragment implements Toolbar.OnMenuItemClickListener, MaterialButtonToggleGroup.OnButtonCheckedListener, MainActivity.OnBackPressedListener {

    private final String KEY_SCROLL_STATE = "dev.chara.thunderscout.KEY_SCROLL_STATE";

    private FragmentDataListBinding binding;
    private DataListViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDataListBinding.inflate(inflater, container, false);

        // Instantiate ViewModel
        viewModel = new ViewModelProvider(this).get(DataListViewModel.class);

        // Setup and bind views
        binding.toolbar.inflateMenu(R.menu.menu_data_list);
        binding.toolbar.setOnMenuItemClickListener(this);

        /*if (binding.toolbar.getMenu() instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) binding.toolbar.getMenu();
            //noinspection RestrictedApi
            m.setOptionalIconsVisible(true);
        }*/

        MenuCompat.setGroupDividerEnabled(binding.toolbar.getMenu(), true);

        binding.buttonToggle.check(viewModel.getCurrentView());
        binding.buttonToggle.addOnButtonCheckedListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.dataListView.setLayoutManager(mLayoutManager);

        binding.dataListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    binding.buttonMatchScout.shrink();
                } else if (dy < 0) {
                    binding.buttonMatchScout.extend();
                }
            }
        });

        PreferenceRepository preferenceRepository = PreferenceRepository.getInstance(getActivity().getApplicationContext());

        if (preferenceRepository.getBoolean(Preference.ENABLE_MATCH_SCOUTING)) {
            binding.buttonMatchScout.setVisibility(View.VISIBLE);
        } else {
            binding.buttonMatchScout.setVisibility(View.GONE);
        }

        // Bind adapter to view model
        viewModel.getCurrentEvent().observe(getViewLifecycleOwner(), binding::setEvent);
        onButtonChecked(binding.buttonToggle, viewModel.getCurrentView(), true);

        // Navigation
        NavController controller = NavHostFragment.findNavController(this);

        if (!preferenceRepository.getBoolean(Preference.SHOWN_FIRST_RUN_EXPERIENCE)) {
            NavDirections action = DataListFragmentDirections.toOnboarding();
            controller.navigate(action);
        }

        binding.buttonMatchScout.setOnClickListener((v) -> {
            NavDirections action = DataListFragmentDirections.toMatchScout();
            controller.navigate(action);
        });

        // Debug: ScoutData generator
        if (BuildConfig.DEBUG) {
            binding.buttonMatchScout.setOnLongClickListener(v -> {
                viewModel.addMockData();
                return true;
            });
        }

        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.dataListView, (v, insets) -> {
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(),
                    InsetUtils.dpToPixel(96, getContext()) + insets.getSystemWindowInsetBottom());
            return insets;
        });
        ViewCompat.setOnApplyWindowInsetsListener(binding.buttonMatchScout, (v, insets) -> {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) v.getLayoutParams();
            params.bottomMargin = InsetUtils.dpToPixel(16, getContext()) + insets.getSystemWindowInsetBottom();
            return insets;
        });

        // Selection mode
        viewModel.isInSelectionMode().observe(getViewLifecycleOwner(), selectionMode -> {
            binding.toolbar.getMenu().clear();
            if (selectionMode) {
                binding.toolbar.setTitle("Select items...");
                binding.toolbar.setNavigationIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_close_24dp));
                binding.toolbar.setNavigationContentDescription("Cancel");
                binding.toolbar.setNavigationOnClickListener(v -> {
                    viewModel.clearSelections();
                    binding.dataListView.getAdapter().notifyDataSetChanged();
                });
                binding.toolbar.inflateMenu(R.menu.menu_data_list_selection);
                binding.buttonMatchScout.hide();
            } else {
                binding.toolbar.setTitle(R.string.app_name);
                binding.toolbar.setNavigationIcon(null);
                binding.toolbar.inflateMenu(R.menu.menu_data_list);
                if (preferenceRepository.getBoolean(Preference.ENABLE_MATCH_SCOUTING)) {
                    binding.buttonMatchScout.show();
                }
            }
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
        if (viewModel != null && viewModel.isInSelectionMode().getValue()) {
            viewModel.clearSelections();
            binding.dataListView.getAdapter().notifyDataSetChanged();
            return false;
        }
        return true;
    }

    @Override
    public boolean onMenuItemClick(@NonNull MenuItem item) {

        // Normal mode: Actions
        if (item.getItemId() == R.id.action_delete_all) {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Delete all data?")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Delete", (dialog, which) -> {
                        List<ScoutData> data = viewModel.deleteAllData();
                        Snackbar.make(requireView(), "Data deleted", Snackbar.LENGTH_LONG)
                                .setAction("Undo", view -> viewModel.insertData(data))
                                .show();
                    })
                    .show();
        }

        // Normal mode: Settings
        else if (item.getItemId() == R.id.action_events) {
            NavDirections action = DataListFragmentDirections.toEventList();
            Navigation.findNavController(binding.getRoot()).navigate(action);

        } else if (item.getItemId() == R.id.action_settings) {
            NavDirections action = DataListFragmentDirections.toSettings();
            Navigation.findNavController(binding.getRoot()).navigate(action);

        } else if (item.getItemId() == R.id.action_about) {
            NavDirections action = DataListFragmentDirections.toAbout();
            Navigation.findNavController(binding.getRoot()).navigate(action);
        }

        // Selection mode
        else if (item.getItemId() == R.id.action_delete_selected) {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Delete selected data?")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Delete", (dialog, which) -> {
                        List<ScoutData> data = viewModel.deleteData(viewModel.getSelectedData());
                        Snackbar.make(requireView(), "Data deleted", Snackbar.LENGTH_LONG)
                                .setAction("Undo", view -> {
                                    viewModel.insertData(data);
                                })
                                .addCallback(new Snackbar.Callback() {
                                    @Override
                                    public void onDismissed(Snackbar transientBottomBar, int event) {
                                        super.onDismissed(transientBottomBar, event);
                                        viewModel.clearSelections();
                                    }
                                })
                                .show();
                    })
                    .show();
        }

        return false;
    }

    @Override
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        if (!isChecked) {
            return;
        }

        if (checkedId == R.id.button_show_matches) {
            MatchListAdapter adapter = new MatchListAdapter(viewModel);
            binding.dataListView.setAdapter(adapter);

            viewModel.getScoutData().removeObservers(getViewLifecycleOwner());
            viewModel.getScoutData().observe(getViewLifecycleOwner(), adapter::setData);

            viewModel.setCurrentView(R.id.button_show_matches);

        } else if (checkedId == R.id.button_show_rankings) {
            TeamListAdapter adapter = new TeamListAdapter(viewModel);
            binding.dataListView.setAdapter(adapter);

            viewModel.getScoutData().removeObservers(getViewLifecycleOwner());
            viewModel.getScoutData().observe(getViewLifecycleOwner(), adapter::setData);

            viewModel.setCurrentView(R.id.button_show_rankings);

        } /*else {
            DataListAdapter adapter = new DataListAdapter(viewModel);
            binding.dataListView.setAdapter(adapter);
            binding.dataListView.getLayoutManager().onRestoreInstanceState(scrollState);

            viewModel.getScoutData().removeObservers(getViewLifecycleOwner());
            viewModel.getScoutData().observe(getViewLifecycleOwner(), adapter::setData);

            viewModel.setCurrentView(R.id.button_show_default);
        }*/
    }
}
