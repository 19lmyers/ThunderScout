package dev.chara.thunderscout.ui.template_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import dev.chara.thunderscout.bluetooth.ClientConnectionThread;
import dev.chara.thunderscout.bluetooth.utils.BluetoothDeviceManager;
import dev.chara.thunderscout.databinding.ItemTemplateBinding;
import dev.chara.thunderscout.model.ScoutData;

public final class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.TemplateViewHolder> {

    private TemplateListViewModel viewModel;

    private ArrayList<ScoutData> templates;

    TemplateAdapter(TemplateListViewModel viewModel) {
        this.viewModel = viewModel;

        templates = new ArrayList<>();
    }

    @NonNull
    @Override
    public TemplateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemTemplateBinding binding = ItemTemplateBinding.inflate(layoutInflater, parent, false);
        return new TemplateViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateViewHolder holder, int position) {
        ScoutData template = templates.get(position);
        holder.bind(template);
    }

    @Override
    public int getItemCount() {
        return templates.size();
    }

    void setTemplates(List<ScoutData> newTemplates) {
        TemplateDiffCallback templateDiffCallback = new TemplateDiffCallback(templates, newTemplates);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(templateDiffCallback);

        templates.clear();
        templates.addAll(newTemplates);

        diffResult.dispatchUpdatesTo(this);
        notifyDataSetChanged();
    }

    class TemplateViewHolder extends RecyclerView.ViewHolder {

        private final ItemTemplateBinding binding;

        TemplateViewHolder(ItemTemplateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ScoutData template) {
            binding.setTemplate(template);
            binding.executePendingBindings();

            if (template.id == ScoutData.TEMPLATE_ID_DEFAULT) {
                binding.buttonShare.setVisibility(View.GONE);
                binding.buttonDelete.setVisibility(View.GONE);
            } else {
                binding.buttonShare.setOnClickListener(v -> {
                    new BluetoothDeviceManager(binding.getRoot().getContext()).pickDevice(device -> {
                        if (device != null) {
                            ClientConnectionThread thread = new ClientConnectionThread(device, template, binding.getRoot().getContext().getApplicationContext());
                            thread.start();
                        }
                    });
                });

                binding.buttonDelete.setOnClickListener(v -> new MaterialAlertDialogBuilder(binding.getRoot().getContext())
                        .setTitle("Delete template?")
                        .setMessage("WARNING: This will delete all events using the specified template!")
                        .setPositiveButton("Delete", (dialog, which) -> viewModel.deleteTemplate(template))
                        .setNegativeButton("Cancel", null)
                        .show());
            }
        }
    }

    static class TemplateDiffCallback extends DiffUtil.Callback {

        private final List<ScoutData> oldList, newList;

        TemplateDiffCallback(List<ScoutData> oldList, List<ScoutData> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).id == newList.get(newItemPosition).id;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }
}
