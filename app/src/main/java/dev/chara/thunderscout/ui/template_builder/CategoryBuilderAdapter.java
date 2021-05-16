package dev.chara.thunderscout.ui.template_builder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import dev.chara.thunderscout.databinding.ItemCategoryBinding;
import dev.chara.thunderscout.model.Category;
import dev.chara.thunderscout.model.type.FieldType;

final class CategoryBuilderAdapter extends RecyclerView.Adapter<CategoryBuilderAdapter.CategoryViewHolder> {

    private TemplateBuilderFragment fragment;
    private TemplateBuilderViewModel viewModel;

    private ArrayList<Category> categories;

    private RecyclerView.RecycledViewPool viewPool;

    CategoryBuilderAdapter(TemplateBuilderFragment fragment, TemplateBuilderViewModel viewModel) {
        super();

        this.fragment = fragment;
        this.viewModel = viewModel;

        categories = new ArrayList<>();

        viewPool = new RecyclerView.RecycledViewPool();
    }

    void setCategories(List<Category> newCategories) {
        // TODO DiffUtil is currently broken for this
        //CategoryDiffCallback categoryDiffCallback = new CategoryDiffCallback(categories, newCategories);
        //DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(categoryDiffCallback);

        categories.clear();
        categories.addAll(newCategories);

        notifyDataSetChanged();

        //diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public CategoryBuilderAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(layoutInflater, parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryBuilderAdapter.CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
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

            // Set action listeners
            binding.buttonNewField.setVisibility(View.VISIBLE);
            binding.buttonNewField.setOnClickListener(v -> {
                NavDirections action = TemplateBuilderFragmentDirections.toNewFieldDialog(category);
                Navigation.findNavController(binding.getRoot()).navigate(action);
            });

            binding.buttonDelete.setVisibility(View.VISIBLE);
            binding.buttonDelete.setOnClickListener(v -> new MaterialAlertDialogBuilder(binding.getRoot().getContext())
                    .setTitle("Delete category?")
                    .setPositiveButton("Delete", (dialog, which) -> viewModel.removeCategory(category))
                    .setNegativeButton("Cancel", null)
                    .show());

            // Configure child RecyclerView
            GridLayoutManager layoutManager = new GridLayoutManager(binding.getRoot().getContext(), 2);
            binding.fieldListView.setLayoutManager(layoutManager);

            FieldBuilderAdapter adapter = new FieldBuilderAdapter(category.fields);
            binding.fieldListView.setAdapter(adapter);

            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (FieldType.values()[adapter.getItemViewType(position)] == FieldType.TEXT) {
                        return 2;
                    }
                    return 1;
                }
            });

            binding.fieldListView.setNestedScrollingEnabled(false);
            binding.fieldListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            binding.fieldListView.setRecycledViewPool(viewPool);
        }
    }

    static class CategoryDiffCallback extends DiffUtil.Callback {

        private final List<Category> oldList, newList;

        CategoryDiffCallback(List<Category> oldList, List<Category> newList) {
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
