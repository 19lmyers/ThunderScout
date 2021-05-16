package dev.chara.thunderscout.ui.data_list.match_conflict;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

import dev.chara.thunderscout.databinding.DialogMatchConflictBinding;

public final class MatchConflictDialog extends DialogFragment {

    private DialogMatchConflictBinding binding;
    private MatchConflictViewModel viewModel;

    private MatchConflictAdapter adapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Resolve conflict...");

        // Navigation arguments
        MatchConflictDialogArgs arguments = MatchConflictDialogArgs.fromBundle(Objects.requireNonNull(getArguments()));

        binding = DialogMatchConflictBinding.inflate(LayoutInflater.from(getContext()), null, false);

        // Instantiate ViewModel
        MatchConflictViewModel.Factory factory = new MatchConflictViewModel.Factory(getActivity().getApplication(), arguments.getEvent(), arguments.getMatchNumber(), arguments.getStation());
        viewModel = new ViewModelProvider(this, factory).get(MatchConflictViewModel.class);

        builder.setView(binding.getRoot());
        builder.setNegativeButton("Close", null);

        Dialog dialog = builder.create();

        binding.matchConflictView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));

        adapter = new MatchConflictAdapter(this);
        binding.matchConflictView.setAdapter(adapter);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel.getDataList().observe(getViewLifecycleOwner(), adapter::setData);

        return binding.getRoot();
    }

    MatchConflictViewModel getViewModel() {
        return viewModel;
    }
}
