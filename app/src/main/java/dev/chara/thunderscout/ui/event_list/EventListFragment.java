package dev.chara.thunderscout.ui.event_list;

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
import dev.chara.thunderscout.databinding.FragmentEventListBinding;
import dev.chara.thunderscout.ui.utils.InsetUtils;

public final class EventListFragment extends Fragment {

    private FragmentEventListBinding binding;
    private EventListViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEventListBinding.inflate(inflater, container, false);

        // Instantiate ViewModel
        viewModel = new ViewModelProvider(this).get(EventListViewModel.class);

        // Setup and bind views
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        binding.toolbar.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.eventListView.setLayoutManager(mLayoutManager);

        EventAdapter adapter = new EventAdapter(viewModel);
        binding.eventListView.setAdapter(adapter);

        binding.eventListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    binding.buttonNewEvent.shrink();
                } else if (dy < 0) {
                    binding.buttonNewEvent.extend();
                }
            }
        });

        binding.buttonNewEvent.setOnClickListener(v -> {
            NavDirections action = EventListFragmentDirections.toNewEventDialog();
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });

        // Bind adapter to view model
        viewModel.getEvents().observe(getViewLifecycleOwner(), adapter::setEvents);

        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.eventListView, (v, insets) -> {
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(),
                    InsetUtils.dpToPixel(80, getContext()) + insets.getSystemWindowInsetBottom());
            return insets;
        });

        ViewCompat.setOnApplyWindowInsetsListener(binding.buttonNewEvent, (v, insets) -> {
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
