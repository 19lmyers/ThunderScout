package dev.chara.thunderscout.ui.event_list.new_event;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import dev.chara.thunderscout.R;
import dev.chara.thunderscout.databinding.DialogNewEventBinding;
import dev.chara.thunderscout.model.Event;
import dev.chara.thunderscout.model.ScoutData;

public final class NewEventDialog extends DialogFragment {

    private DialogNewEventBinding binding;
    private NewEventViewModel viewModel;

    private List<ScoutData> templateList;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle(R.string.action_new_event);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_new_event, null, false);

        // Instantiate ViewModel
        viewModel = new ViewModelProvider(this).get(NewEventViewModel.class);

        builder.setView(binding.getRoot());

        binding.setEvent(new Event());
        binding.eventTemplateSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.getEvent().templateId = templateList.get(position).id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setPositiveButton("Create", (dialog, which) -> {
            if (binding.getEvent().name == null || binding.getEvent().name.isEmpty()) {
                // We can't keep the dialog open, but nothing will happen
                return;
            }
            viewModel.insertEvent(binding.getEvent());
        });
        builder.setNegativeButton("Cancel", null);

        return builder.create();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel.getScoutData().observe(getViewLifecycleOwner(), data -> {
            ArrayAdapter<ScoutData> templateAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, data);
            binding.eventTemplateSelector.setAdapter(templateAdapter);

            templateList = data;
        });
        return binding.getRoot();
    }
}
