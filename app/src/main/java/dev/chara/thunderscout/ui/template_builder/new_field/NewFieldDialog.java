package dev.chara.thunderscout.ui.template_builder.new_field;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;
import java.util.UUID;

import dev.chara.thunderscout.R;
import dev.chara.thunderscout.databinding.DialogNewFieldBinding;
import dev.chara.thunderscout.model.Category;
import dev.chara.thunderscout.model.Field;
import dev.chara.thunderscout.model.type.FieldType;
import dev.chara.thunderscout.ui.template_builder.TemplateBuilderViewModel;

public final class NewFieldDialog extends DialogFragment {

    private DialogNewFieldBinding binding;
    private TemplateBuilderViewModel viewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle(R.string.action_new_field);

        //Navigation arguments
        final Category category = NewFieldDialogArgs.fromBundle(requireArguments()).getCategory();

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_new_field, null, false);

        // Get instance of existing ViewModel
        TemplateBuilderViewModel.Factory factory = new TemplateBuilderViewModel.Factory(getActivity().getApplication(), "");
        ViewModelStoreOwner storeOwner = NavHostFragment.findNavController(this).getViewModelStoreOwner(R.id.templateBuilder);
        viewModel = new ViewModelProvider(storeOwner, factory).get(TemplateBuilderViewModel.class);

        builder.setView(binding.getRoot());

        binding.setField(new Field());
        binding.fieldType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.getField().type = FieldType.values()[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setPositiveButton("Create", (dialog, which) -> {
            if (binding.getField().name == null || binding.getField().name.isEmpty()) {
                // We can't keep the dialog open, but nothing will happen
                return;
            }

            binding.getField().id = UUID.randomUUID();
            viewModel.addField(category, binding.getField());

            dismiss();

        });
        builder.setNegativeButton("Cancel", null);

        return builder.create();
    }
}
