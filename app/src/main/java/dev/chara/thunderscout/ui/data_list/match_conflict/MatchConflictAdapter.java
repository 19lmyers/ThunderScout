package dev.chara.thunderscout.ui.data_list.match_conflict;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import dev.chara.thunderscout.databinding.ItemDataListingBinding;
import dev.chara.thunderscout.model.ScoutData;

public final class MatchConflictAdapter extends RecyclerView.Adapter<MatchConflictAdapter.DataViewHolder> {

    private List<ScoutData> dataList;

    private MatchConflictDialog parentDialog;

    public MatchConflictAdapter(MatchConflictDialog parentDialog) {
        this.parentDialog = parentDialog;

        dataList = new ArrayList<>();
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ItemDataListingBinding binding = ItemDataListingBinding.inflate(inflater, parent, false);

        return new DataViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int i) {
        ScoutData data = dataList.get(i);

        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setData(List<ScoutData> newDataList) {
        MatchConflictAdapter.DataDiffCallback dataDiffCallback = new MatchConflictAdapter.DataDiffCallback(dataList, newDataList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(dataDiffCallback);

        dataList.clear();
        dataList.addAll(newDataList);

        diffResult.dispatchUpdatesTo(this);
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        private ItemDataListingBinding binding;

        public DataViewHolder(ItemDataListingBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public void bind(ScoutData data) {
            binding.setData(data);

            binding.buttonInfo.setOnClickListener(v -> {
                NavDirections action = MatchConflictDialogDirections.toDataInfo(data.id);
                NavHostFragment.findNavController(parentDialog).navigate(action);
            });

            binding.buttonDelete.setOnClickListener(v -> new MaterialAlertDialogBuilder(binding.getRoot().getContext())
                    .setTitle("Delete this match?")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Delete", (dialog, which) -> parentDialog.getViewModel().deleteData(data)).show());
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

