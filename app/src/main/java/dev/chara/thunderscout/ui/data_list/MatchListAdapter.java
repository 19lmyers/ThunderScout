package dev.chara.thunderscout.ui.data_list;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.chara.thunderscout.R;
import dev.chara.thunderscout.databinding.ItemMatchBinding;
import dev.chara.thunderscout.model.ScoutData;
import dev.chara.thunderscout.model.type.AllianceStation;
import dev.chara.thunderscout.model.wrapper.MatchWrapper;

final class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.MatchViewHolder> {

    private DataListViewModel viewModel;

    private SparseArray<MatchWrapper> matchArray;

    MatchListAdapter(DataListViewModel viewModel) {
        super();

        this.viewModel = viewModel;

        matchArray = new SparseArray<>();
    }

    public void setData(List<ScoutData> newDataList) {
        matchArray.clear();

        for (ScoutData data : newDataList) {
            MatchWrapper wrapper = matchArray.get(data.matchNumber);

            if (wrapper == null) {
                wrapper = new MatchWrapper();
                wrapper.matchNumber = data.matchNumber;
                matchArray.put(data.matchNumber, wrapper);
            }

            wrapper.setData(data.allianceStation, data);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MatchListAdapter.MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMatchBinding binding = ItemMatchBinding.inflate(layoutInflater, parent, false);
        return new MatchViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchListAdapter.MatchViewHolder holder, int position) {
        MatchWrapper match = matchArray.get(matchArray.keyAt(position)); //Treat it like a list for this code
        holder.bind(match);
    }

    @Override
    public int getItemCount() {
        return matchArray.size();
    }

    class MatchViewHolder extends RecyclerView.ViewHolder {

        private final ItemMatchBinding binding;

        MatchViewHolder(@NonNull ItemMatchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(MatchWrapper match) {
            binding.setMatch(match);
            binding.executePendingBindings();

            for (final AllianceStation station : AllianceStation.values()) {
                TextView matchView = binding.getRoot().findViewById(station.getMatchCellId());

                if (match.getDataList(station) == null) {
                    matchView.setVisibility(View.INVISIBLE);

                    matchView.setOnClickListener(null);
                    matchView.setClickable(false);
                } else {
                    matchView.setVisibility(View.VISIBLE);

                    if (match.getDataList(station).size() == 1) { //Single match, treat as normal

                        matchView.setText(match.getDataList(station).get(0).teamNumber);

                        if (viewModel.isSelected(match.getDataList(station).get(0))) {
                            matchView.setBackgroundColor(binding.getRoot().getResources().getColor(R.color.selected_item));
                        } else {
                            matchView.setBackgroundColor(binding.getRoot().getResources().getColor(station.getColorStratified()));
                        }

                        matchView.setOnClickListener(v -> {
                            //noinspection ConstantConditions
                            if (viewModel.isInSelectionMode().getValue()) {
                                viewModel.setSelected(match.getDataList(station).get(0), !viewModel.isSelected(match.getDataList(station).get(0)));

                                if (viewModel.isSelected(match.getDataList(station).get(0))) {
                                    matchView.setBackgroundColor(binding.getRoot().getResources().getColor(R.color.selected_item));
                                } else {
                                    matchView.setBackgroundColor(binding.getRoot().getResources().getColor(station.getColorStratified()));
                                }
                            } else {
                                NavDirections action = DataListFragmentDirections.toDataInfo(match.getDataList(station).get(0).id);
                                Navigation.findNavController(binding.getRoot()).navigate(action);
                            }
                        });

                        matchView.setOnLongClickListener(v -> {
                            viewModel.setSelected(match.getDataList(station).get(0), !viewModel.isSelected(match.getDataList(station).get(0)));

                            if (viewModel.isSelected(match.getDataList(station).get(0))) {
                                matchView.setBackgroundColor(binding.getRoot().getResources().getColor(R.color.selected_item));
                            } else {
                                matchView.setBackgroundColor(binding.getRoot().getResources().getColor(station.getColorStratified()));
                            }
                            return true;
                        });

                    } else { //MULTIPLE matches in this slot - display error!
                        matchView.setText("!");

                        matchView.setBackgroundColor(binding.getRoot().getResources().getColor(R.color.match_conflict));

                        matchView.setOnClickListener(v -> {
                            //noinspection ConstantConditions
                            if (!viewModel.isInSelectionMode().getValue()) {
                                NavDirections action = DataListFragmentDirections.toMatchConflictDialog(match.getDataList(station).get(0).eventId, match.matchNumber, station);
                                Navigation.findNavController(binding.getRoot()).navigate(action);
                            }
                        });
                    }
                }
            }

            binding.getRoot().setOnClickListener(v -> {
                //noinspection ConstantConditions
                if (viewModel.isInSelectionMode().getValue()) {
                    for (AllianceStation station : AllianceStation.values()) {
                        if (match.getDataList(station) == null) {
                            continue;
                        }

                        for (ScoutData data : match.getDataList(station)) {
                            viewModel.setSelected(data, !viewModel.areAllSelected(match.getDataList(station)));
                        }

                        if (viewModel.areAllSelected(match.getDataList(station))) {
                            binding.getRoot().findViewById(station.getMatchCellId())
                                    .setBackgroundColor(binding.getRoot().getResources().getColor(R.color.selected_item));
                        } else {
                            binding.getRoot().findViewById(station.getMatchCellId())
                                    .setBackgroundColor(binding.getRoot().getResources().getColor(station.getColorStratified()));
                        }
                    }
                }
            });

            binding.getRoot().setOnLongClickListener(v -> {
                for (AllianceStation station : AllianceStation.values()) {
                    if (match.getDataList(station) == null) {
                        continue;
                    }

                    for (ScoutData data : match.getDataList(station)) {
                        viewModel.setSelected(data, !viewModel.areAllSelected(match.getDataList(station)));
                    }

                    if (viewModel.areAllSelected(match.getDataList(station))) {
                        binding.getRoot().findViewById(station.getMatchCellId())
                                .setBackgroundColor(binding.getRoot().getResources().getColor(R.color.selected_item));
                    } else {
                        binding.getRoot().findViewById(station.getMatchCellId())
                                .setBackgroundColor(binding.getRoot().getResources().getColor(station.getColorStratified()));
                    }
                }
                return true;
            });
        }
    }
}
