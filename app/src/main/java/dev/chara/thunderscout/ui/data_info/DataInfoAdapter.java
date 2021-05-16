package dev.chara.thunderscout.ui.data_info;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dev.chara.thunderscout.databinding.ItemCategoryBinding;
import dev.chara.thunderscout.databinding.ItemDataHeaderBinding;
import dev.chara.thunderscout.model.Category;
import dev.chara.thunderscout.model.ScoutData;

final class DataInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_DATA_HEADER = 1;
    private static final int VIEW_TYPE_CATEGORY = 2;

    private ScoutData data;

    private RecyclerView.RecycledViewPool viewPool;

    DataInfoAdapter() {
        super();

        viewPool = new RecyclerView.RecycledViewPool();
    }

    void setData(ScoutData data) {
        this.data = data;
        notifyDataSetChanged(); //Todo DiffUtil?
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_DATA_HEADER) {
            ItemDataHeaderBinding binding = ItemDataHeaderBinding.inflate(layoutInflater, parent, false);
            return new DataHeaderViewHolder(binding);
        } else {
            ItemCategoryBinding binding = ItemCategoryBinding.inflate(layoutInflater, parent, false);
            return new CategoryViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_DATA_HEADER) {
            DataHeaderViewHolder dataHeaderViewHolder = (DataHeaderViewHolder) holder;
            dataHeaderViewHolder.bind(data);
        } else {
            Category category = data.categories.get(position - 1);
            CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
            categoryViewHolder.bind(category);
        }
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 1;
        }
        return data.categories.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_DATA_HEADER;
        } else {
            return VIEW_TYPE_CATEGORY;
        }
    }

    static class DataHeaderViewHolder extends RecyclerView.ViewHolder {

        private final ItemDataHeaderBinding binding;

        DataHeaderViewHolder(@NonNull ItemDataHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ScoutData data) {
            binding.setData(data);
            binding.executePendingBindings();
        }
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        private final ItemCategoryBinding binding;

        CategoryViewHolder(@NonNull ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Category category) {
            binding.setCategory(category);
            binding.executePendingBindings();

            // Configure child RecyclerView
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(binding.getRoot().getContext());
            binding.fieldListView.setLayoutManager(mLayoutManager);

            FieldInfoAdapter adapter = new FieldInfoAdapter(category.fields);
            binding.fieldListView.setAdapter(adapter);

            binding.fieldListView.setNestedScrollingEnabled(false);
            binding.fieldListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            binding.fieldListView.setRecycledViewPool(viewPool);
        }
    }
}
