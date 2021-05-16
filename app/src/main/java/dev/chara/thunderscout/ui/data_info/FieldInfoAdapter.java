package dev.chara.thunderscout.ui.data_info;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.chara.thunderscout.databinding.ItemFieldValueBinding;
import dev.chara.thunderscout.model.Field;

final class FieldInfoAdapter extends RecyclerView.Adapter<FieldInfoAdapter.FieldViewHolder> {

    private List<Field> fields;

    public FieldInfoAdapter(List<Field> fields) {
        super();

        this.fields = fields;
    }

    @NonNull
    @Override
    public FieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemFieldValueBinding binding = ItemFieldValueBinding.inflate(layoutInflater, parent, false);
        return new FieldViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FieldViewHolder holder, int position) {
        Field field = fields.get(position);
        holder.bind(field);
    }

    @Override
    public int getItemCount() {
        return fields.size();
    }

    static class FieldViewHolder extends RecyclerView.ViewHolder {

        private final ItemFieldValueBinding binding;

        FieldViewHolder(@NonNull ItemFieldValueBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Field field) {
            binding.setField(field);
            binding.executePendingBindings();
        }
    }
}
