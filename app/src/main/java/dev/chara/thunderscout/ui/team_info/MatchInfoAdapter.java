package dev.chara.thunderscout.ui.team_info;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.chara.thunderscout.model.ScoutData;
import dev.chara.thunderscout.ui.team_info.match_info.MatchInfoFragment;

final class MatchInfoAdapter extends FragmentStateAdapter {

    private ArrayList<ScoutData> dataList;

    MatchInfoAdapter(@NonNull Fragment fragment) {
        super(fragment);

        dataList = new ArrayList<>();
    }

    public List<ScoutData> getData() {
        return dataList;
    }

    public void setData(List<ScoutData> newDataList) {
        DataDiffCallback dataDiffCallback = new DataDiffCallback(dataList, newDataList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(dataDiffCallback);

        dataList.clear();
        dataList.addAll(newDataList);

        Collections.sort(dataList, (o1, o2) -> Integer.compare(o1.matchNumber, o2.matchNumber));

        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        MatchInfoFragment fragment = new MatchInfoFragment();

        Bundle args = new Bundle();
        args.putLong(MatchInfoFragment.ARGUMENT_SCOUT_DATA_ID, dataList.get(position).id);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static final class DataDiffCallback extends DiffUtil.Callback {

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
