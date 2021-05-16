package dev.chara.thunderscout.ui.data_list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev.chara.thunderscout.R;
import dev.chara.thunderscout.databinding.ItemDataListingBinding;
import dev.chara.thunderscout.model.ScoutData;

@Deprecated // Used to show individual data objects. Not surfaced by UI.
final class DataListAdapter extends RecyclerView.Adapter<DataListAdapter.DataViewHolder> {

    private DataListViewModel viewModel;

    private ArrayList<ScoutData> dataList;

    DataListAdapter(DataListViewModel viewModel) {
        super();

        this.viewModel = viewModel;

        dataList = new ArrayList<>();
    }

    public void setData(List<ScoutData> newDataList) {
        DataDiffCallback dataDiffCallback = new DataDiffCallback(dataList, newDataList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(dataDiffCallback);

        dataList.clear();
        dataList.addAll(newDataList);

        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public DataListAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemDataListingBinding binding = ItemDataListingBinding.inflate(layoutInflater, parent, false);
        return new DataViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        ScoutData data = dataList.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class DataViewHolder extends RecyclerView.ViewHolder {

        private final ItemDataListingBinding binding;

        DataViewHolder(@NonNull ItemDataListingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ScoutData data) {
            binding.setData(data);
            binding.executePendingBindings();

            if (viewModel.isSelected(data)) {
                binding.getRoot().setBackgroundColor(binding.getRoot().getResources().getColor(R.color.selected_item));
            } else {
                binding.getRoot().setBackgroundColor(0);
            }

            binding.getRoot().setOnClickListener(v -> {
                //noinspection ConstantConditions
                if (viewModel.isInSelectionMode().getValue()) {
                    viewModel.setSelected(data, !viewModel.isSelected(data));

                    if (viewModel.isSelected(data)) {
                        binding.getRoot().setBackgroundColor(binding.getRoot().getResources().getColor(R.color.selected_item));
                    } else {
                        binding.getRoot().setBackgroundColor(0);
                    }
                } else {
                    NavDirections action = DataListFragmentDirections.toDataInfo(data.id);
                    Navigation.findNavController(binding.getRoot()).navigate(action);
                }
            });

            binding.getRoot().setOnLongClickListener(v -> {
                viewModel.setSelected(data, !viewModel.isSelected(data));

                if (viewModel.isSelected(data)) {
                    binding.getRoot().setBackgroundColor(binding.getRoot().getResources().getColor(R.color.selected_item));
                } else {
                    binding.getRoot().setBackgroundColor(0);
                }
                return true;
            });
        }
    }

    static class DataDiffCallback extends DiffUtil.Callback {

        private final List<ScoutData> oldList, newList;

        DataDiffCallback(List<ScoutData> oldList, List<ScoutData> newList) {
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
