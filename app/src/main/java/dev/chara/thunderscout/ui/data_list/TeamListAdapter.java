package dev.chara.thunderscout.ui.data_list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.chara.thunderscout.R;
import dev.chara.thunderscout.databinding.ItemTeamBinding;
import dev.chara.thunderscout.model.ScoutData;
import dev.chara.thunderscout.model.wrapper.TeamWrapper;

final class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.TeamViewHolder> {

    private DataListViewModel viewModel;

    private ArrayList<TeamWrapper> teamList;

    TeamListAdapter(DataListViewModel viewModel) {
        super();

        this.viewModel = viewModel;

        teamList = new ArrayList<>();
    }

    public void setData(List<ScoutData> newDataList) {
        teamList.clear();

        data:
        for (ScoutData data : newDataList) {
            for (int i = 0; i < teamList.size(); i++) { //I wish there was an easier way, but there isn't
                TeamWrapper wrapper = teamList.get(i);

                if (wrapper.teamNumber.equals(data.teamNumber)) {
                    //Pre-existing team
                    wrapper.data.add(data);
                    continue data; //continues the outer loop labeled 'data'
                }
            }

            //New team
            TeamWrapper wrapper = new TeamWrapper();
            wrapper.teamNumber = data.teamNumber;
            wrapper.data.add(data);
            teamList.add(wrapper);
        }

        Collections.sort(teamList, (o1, o2) -> Integer.valueOf(o1.teamNumber).compareTo(Integer.valueOf(o2.teamNumber)));

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TeamListAdapter.TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemTeamBinding binding = ItemTeamBinding.inflate(layoutInflater, parent, false);
        return new TeamViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamListAdapter.TeamViewHolder holder, int position) {
        TeamWrapper team = teamList.get(position);
        holder.bind(team);
    }

    @Override
    public int getItemCount() {
        return teamList.size();
    }

    class TeamViewHolder extends RecyclerView.ViewHolder {

        private final ItemTeamBinding binding;

        TeamViewHolder(@NonNull ItemTeamBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(TeamWrapper team) {
            binding.setTeam(team);
            binding.executePendingBindings();

            if (viewModel.areAllSelected(team.data)) {
                binding.getRoot().setBackgroundColor(binding.getRoot().getResources().getColor(R.color.selected_item));
            } else {
                binding.getRoot().setBackgroundColor(0);
            }

            binding.getRoot().setOnClickListener(v -> {
                //noinspection ConstantConditions
                if (viewModel.isInSelectionMode().getValue()) {

                    boolean areAllSelected = viewModel.areAllSelected(team.data);
                    for (ScoutData data : team.data) {
                        viewModel.setSelected(data, !areAllSelected);
                    }

                    if (viewModel.areAllSelected(team.data)) {
                        binding.getRoot().setBackgroundColor(binding.getRoot().getResources().getColor(R.color.selected_item));
                    } else {
                        binding.getRoot().setBackgroundColor(0);
                    }
                } else {
                    NavDirections action = DataListFragmentDirections.toTeamInfo(team.data.get(0).eventId, team.teamNumber);
                    Navigation.findNavController(binding.getRoot()).navigate(action);
                }
            });

            binding.getRoot().setOnLongClickListener(v -> {
                boolean areAllSelected = viewModel.areAllSelected(team.data);
                for (ScoutData data : team.data) {
                    viewModel.setSelected(data, !areAllSelected);
                }

                if (viewModel.areAllSelected(team.data)) {
                    binding.getRoot().setBackgroundColor(binding.getRoot().getResources().getColor(R.color.selected_item));
                } else {
                    binding.getRoot().setBackgroundColor(0);
                }
                return true;
            });
        }
    }
}
