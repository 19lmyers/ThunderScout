package dev.chara.thunderscout.ui.scouting_flow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dev.chara.thunderscout.databinding.ItemCategoryBinding;
import dev.chara.thunderscout.databinding.ItemTemplateHeaderBinding;
import dev.chara.thunderscout.model.Category;
import dev.chara.thunderscout.model.ScoutData;
import dev.chara.thunderscout.model.type.AllianceStation;
import dev.chara.thunderscout.model.type.FieldType;
import dev.chara.thunderscout.storage.preference.Preference;
import dev.chara.thunderscout.storage.preference.PreferenceRepository;

final class DataEditorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_TEMPLATE_HEADER = 1;
    private static final int VIEW_TYPE_CATEGORY = 2;

    private ScoutData data;

    private RecyclerView.RecycledViewPool viewPool;

    DataEditorAdapter() {
        super();

        // Temporary data object
        data = new ScoutData();
        data.categories = new ArrayList<>();

        viewPool = new RecyclerView.RecycledViewPool();
    }

    void setData(ScoutData data) {
        this.data = data;
        notifyDataSetChanged(); //TODO DiffUtil?
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_TEMPLATE_HEADER) {
            ItemTemplateHeaderBinding binding = ItemTemplateHeaderBinding.inflate(layoutInflater, parent, false);
            return new TemplateHeaderViewHolder(binding);
        } else {
            ItemCategoryBinding binding = ItemCategoryBinding.inflate(layoutInflater, parent, false);
            return new CategoryViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_TEMPLATE_HEADER) {
            TemplateHeaderViewHolder dataHeaderViewHolder = (TemplateHeaderViewHolder) holder;
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
            return VIEW_TYPE_TEMPLATE_HEADER;
        } else {
            return VIEW_TYPE_CATEGORY;
        }
    }

    static class TemplateHeaderViewHolder extends RecyclerView.ViewHolder {

        private final ItemTemplateHeaderBinding binding;

        TemplateHeaderViewHolder(@NonNull ItemTemplateHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ScoutData data) {
            binding.setData(data);
            binding.executePendingBindings();

            PreferenceRepository preferenceRepository = PreferenceRepository.getInstance(binding.getRoot().getContext().getApplicationContext());

            // Increment the last match number
            binding.matchNumberEditText.setText(String.valueOf(preferenceRepository.getInteger(Preference.LAST_USED_MATCH_NUMBER) + 1));

            // Set to the last used alliance station
            int ordinal = AllianceStation.valueOf(preferenceRepository.getString(Preference.LAST_USED_ALLIANCE_STATION)).ordinal();

            binding.allianceStation.setSelection(ordinal);
            binding.getData().allianceStation = AllianceStation.values()[ordinal];

            binding.allianceStation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    binding.getData().allianceStation = AllianceStation.values()[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
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
            GridLayoutManager layoutManager = new GridLayoutManager(binding.getRoot().getContext(), 2);
            binding.fieldListView.setLayoutManager(layoutManager);

            FieldEditorAdapter adapter = new FieldEditorAdapter(category.fields);
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
}
