package dev.chara.thunderscout.ui.template_list.new_template;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import dev.chara.thunderscout.R;
import dev.chara.thunderscout.databinding.DialogNewTemplateBinding;

public final class NewTemplateDialog extends DialogFragment {

    private DialogNewTemplateBinding binding;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle(R.string.action_new_template);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_new_template, null, false);
        builder.setView(binding.getRoot());

        builder.setPositiveButton("Create", (dialog, which) -> {
            if (binding.getTemplateName() == null || binding.getTemplateName().isEmpty()) {
                // We can't keep the dialog open, but nothing will happen
                return;
            }

            NavDirections action = NewTemplateDialogDirections.toTemplateBuilder(binding.getTemplateName());
            NavHostFragment.findNavController(this).navigate(action);

            dismiss();

        });
        builder.setNegativeButton("Cancel", null);

        return builder.create();
    }
}
