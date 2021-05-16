package dev.chara.thunderscout.ui.template_builder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.chara.thunderscout.BR;
import dev.chara.thunderscout.databinding.ItemFieldBooleanBinding;
import dev.chara.thunderscout.databinding.ItemFieldCounterBinding;
import dev.chara.thunderscout.databinding.ItemFieldTextBinding;
import dev.chara.thunderscout.model.Field;
import dev.chara.thunderscout.model.type.FieldType;

final class FieldBuilderAdapter extends RecyclerView.Adapter<FieldBuilderAdapter.FieldViewHolder> {

    private List<Field> fields;

    FieldBuilderAdapter(List<Field> fields) {
        super();

        this.fields = fields;
    }

    @NonNull
    @Override
    public FieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        ViewDataBinding binding;

        switch (FieldType.values()[viewType]) {
            case INTEGER:
                binding = ItemFieldCounterBinding.inflate(layoutInflater, parent, false);
                break;
            case BOOLEAN:
                binding = ItemFieldBooleanBinding.inflate(layoutInflater, parent, false);
                break;
            case TEXT:
                binding = ItemFieldTextBinding.inflate(layoutInflater, parent, false);
                break;
            default:
                throw new IllegalStateException("Field type missing binding!");
        }

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

    @Override
    public int getItemViewType(int position) {
        return fields.get(position).type.ordinal();
    }

    static class FieldViewHolder extends RecyclerView.ViewHolder {

        private final ViewDataBinding binding;

        FieldViewHolder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Field field) {
            binding.setVariable(BR.field, field);
            binding.executePendingBindings();
        }
    }
}
