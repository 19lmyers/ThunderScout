package dev.chara.thunderscout.ui.template_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dev.chara.thunderscout.R;
import dev.chara.thunderscout.databinding.FragmentTemplateListBinding;
import dev.chara.thunderscout.ui.utils.InsetUtils;

public final class TemplateListFragment extends Fragment {

    private FragmentTemplateListBinding binding;
    private TemplateListViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTemplateListBinding.inflate(inflater, container, false);

        // Instantiate ViewModel
        viewModel = new ViewModelProvider(this).get(TemplateListViewModel.class);

        // Setup and bind views
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        binding.toolbar.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.templateListView.setLayoutManager(mLayoutManager);

        TemplateAdapter adapter = new TemplateAdapter(viewModel);
        binding.templateListView.setAdapter(adapter);

        binding.templateListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    binding.buttonNewTemplate.shrink();
                } else if (dy < 0) {
                    binding.buttonNewTemplate.extend();
                }
            }
        });

        binding.buttonNewTemplate.setOnClickListener(v -> {
            NavDirections action = TemplateListFragmentDirections.toNewTemplateDialog();
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });

        // Bind adapter to view model
        viewModel.getTemplates().observe(getViewLifecycleOwner(), adapter::setTemplates);

        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.templateListView, (v, insets) -> {
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(),
                    InsetUtils.dpToPixel(80, getContext()) + insets.getSystemWindowInsetBottom());
            return insets;
        });

        ViewCompat.setOnApplyWindowInsetsListener(binding.buttonNewTemplate, (v, insets) -> {
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
}
