package dev.chara.thunderscout.ui.event_list.new_event;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dev.chara.thunderscout.model.Event;
import dev.chara.thunderscout.model.ScoutData;
import dev.chara.thunderscout.storage.database.DataRepository;

public final class NewEventViewModel extends AndroidViewModel {

    private DataRepository repository;

    private LiveData<List<ScoutData>> liveData;

    public NewEventViewModel(@NonNull Application application) {
        super(application);

        repository = DataRepository.getInstance(application.getApplicationContext());

        liveData = repository.getDataByEvent(Event.EVENT_ID_TEMPLATES);
    }

    LiveData<List<ScoutData>> getScoutData() {
        return liveData;
    }

    public void insertEvent(Event event) {
        repository.insertEvent(event);
    }
}
