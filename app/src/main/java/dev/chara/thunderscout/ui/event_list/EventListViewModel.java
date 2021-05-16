package dev.chara.thunderscout.ui.event_list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dev.chara.thunderscout.model.Event;
import dev.chara.thunderscout.storage.database.DataRepository;
import dev.chara.thunderscout.storage.preference.Preference;
import dev.chara.thunderscout.storage.preference.PreferenceRepository;

public final class EventListViewModel extends AndroidViewModel {

    private DataRepository dataRepository;
    private PreferenceRepository preferenceRepository;

    private LiveData<List<Event>> liveData;

    public EventListViewModel(@NonNull Application application) {
        super(application);

        dataRepository = DataRepository.getInstance(application.getApplicationContext());
        preferenceRepository = PreferenceRepository.getInstance(application.getApplicationContext());

        liveData = dataRepository.getEvents();
    }

    LiveData<List<Event>> getEvents() {
        return liveData;
    }

    void insertEvent(Event event) {
        dataRepository.insertEvent(event);
    }

    void deleteEvent(Event event) {
        dataRepository.deleteEvent(event);

        if (preferenceRepository.getLong(Preference.SELECTED_EVENT) == event.id) {
            preferenceRepository.setLong(Preference.SELECTED_EVENT, Event.EVENT_ID_DEFAULT);
        }
    }

    void setSelectedEvent(Event event) {
        preferenceRepository.setLong(Preference.SELECTED_EVENT, event.id);
    }
}
