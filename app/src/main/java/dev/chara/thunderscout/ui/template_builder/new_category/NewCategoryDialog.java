package dev.chara.thunderscout.ui.template_builder.new_category;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.UUID;

import dev.chara.thunderscout.R;
import dev.chara.thunderscout.databinding.DialogNewCategoryBinding;
import dev.chara.thunderscout.model.Category;
import dev.chara.thunderscout.ui.template_builder.TemplateBuilderViewModel;

public final class NewCategoryDialog extends DialogFragment {

    private DialogNewCategoryBinding binding;
    private TemplateBuilderViewModel viewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle(R.string.action_new_category);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_new_category, null, false);

        // Get instance of existing ViewModel
        TemplateBuilderViewModel.Factory factory = new TemplateBuilderViewModel.Factory(getActivity().getApplication(), "");
        ViewModelStoreOwner storeOwner = NavHostFragment.findNavController(this).getViewModelStoreOwner(R.id.templateBuilder);
        viewModel = new ViewModelProvider(storeOwner, factory).get(TemplateBuilderViewModel.class);

        builder.setView(binding.getRoot());

        binding.setCategory(new Category());

        builder.setPositiveButton("Create", (dialog, which) -> {
            if (binding.getCategory().name == null || binding.getCategory().name.isEmpty()) {
                // We can't keep the dialog open, but nothing will happen
                return;
            }

            binding.getCategory().id = UUID.randomUUID();
            viewModel.addCategory(binding.getCategory());

            dismiss();
        });
        builder.setNegativeButton("Cancel", null);

        return builder.create();
    }
}
