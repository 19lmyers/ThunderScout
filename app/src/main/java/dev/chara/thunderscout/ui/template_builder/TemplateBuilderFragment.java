package dev.chara.thunderscout.ui.template_builder;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

import dev.chara.thunderscout.R;
import dev.chara.thunderscout.databinding.FragmentTemplateBuilderBinding;
import dev.chara.thunderscout.ui.MainActivity;
import dev.chara.thunderscout.ui.utils.InsetUtils;

public final class TemplateBuilderFragment extends Fragment implements Toolbar.OnMenuItemClickListener, MainActivity.OnBackPressedListener {

    private FragmentTemplateBuilderBinding binding;
    private TemplateBuilderViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTemplateBuilderBinding.inflate(inflater, container, false);

        //Navigation arguments
        String templateName = TemplateBuilderFragmentArgs.fromBundle(Objects.requireNonNull(getArguments())).getTemplateName();

        // Instantiate ViewModel
        TemplateBuilderViewModel.Factory factory = new TemplateBuilderViewModel.Factory(getActivity().getApplication(), templateName);
        ViewModelStoreOwner storeOwner = NavHostFragment.findNavController(this).getViewModelStoreOwner(R.id.templateBuilder);
        viewModel = new ViewModelProvider(storeOwner, factory).get(TemplateBuilderViewModel.class);

        // Setup and bind views
        binding.toolbar.setNavigationIcon(R.drawable.ic_close_24dp);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        binding.toolbar.inflateMenu(R.menu.menu_template_builder);
        binding.toolbar.setOnMenuItemClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.fieldGridView.setLayoutManager(mLayoutManager);

        CategoryBuilderAdapter adapter = new CategoryBuilderAdapter(this, viewModel);
        binding.fieldGridView.setAdapter(adapter);

        binding.fieldGridView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    binding.buttonNewCategory.shrink();
                } else if (dy < 0) {
                    binding.buttonNewCategory.extend();
                }
            }
        });

        binding.buttonNewCategory.setOnClickListener(v -> {
            NavDirections action = TemplateBuilderFragmentDirections.toNewCategoryDialog();
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });

        // Bind adapter to view model
        viewModel.getCategories().observe(getViewLifecycleOwner(), adapter::setCategories);

        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.fieldGridView, (v, insets) -> {
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(),
                    InsetUtils.dpToPixel(80, getContext()) + insets.getSystemWindowInsetBottom());
            return insets;
        });

        ViewCompat.setOnApplyWindowInsetsListener(binding.buttonNewCategory, (v, insets) -> {
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
    public boolean onMenuItemClick(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_finish) {
            viewModel.saveTemplate();
            NavHostFragment.findNavController(this).popBackStack();
        }

        return false;
    }

    @Override
    public boolean onBackPressed() {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Discard new template?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Discard", (dialog, which) -> NavHostFragment.findNavController(this).popBackStack())
                .show();
        return false;
    }
}
