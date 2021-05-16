package dev.chara.thunderscout.ui.event_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import dev.chara.thunderscout.databinding.ItemEventBinding;
import dev.chara.thunderscout.model.Event;

public final class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private EventListViewModel viewModel;

    private ArrayList<Event> events;

    EventAdapter(EventListViewModel viewModel) {
        this.viewModel = viewModel;

        events = new ArrayList<>();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemEventBinding binding = ItemEventBinding.inflate(layoutInflater, parent, false);
        return new EventViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    void setEvents(List<Event> newEvents) {
        EventDiffCallback eventDiffCallback = new EventDiffCallback(events, newEvents);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(eventDiffCallback);

        events.clear();
        events.addAll(newEvents);

        diffResult.dispatchUpdatesTo(this);
    }

    class EventViewHolder extends RecyclerView.ViewHolder {

        private final ItemEventBinding binding;

        EventViewHolder(ItemEventBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Event event) {
            binding.setEvent(event);
            binding.executePendingBindings();

            binding.getRoot().setOnClickListener(v -> {
                viewModel.setSelectedEvent(event);
                Navigation.findNavController(binding.getRoot()).popBackStack();
            });

            if (event.id == Event.EVENT_ID_DEFAULT) {
                binding.buttonDelete.setVisibility(View.GONE);
            } else {
                binding.buttonDelete.setOnClickListener(v -> new MaterialAlertDialogBuilder(binding.getRoot().getContext())
                        .setTitle("Delete event?")
                        .setMessage("WARNING: This will delete all data for the specified event!")
                        .setPositiveButton("Delete", (dialog, which) -> viewModel.deleteEvent(event))
                        .setNegativeButton("Cancel", null)
                        .show());
            }
        }
    }

    static class EventDiffCallback extends DiffUtil.Callback {

        private final List<Event> oldList, newList;

        EventDiffCallback(List<Event> oldList, List<Event> newList) {
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
