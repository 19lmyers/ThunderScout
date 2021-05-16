package dev.chara.thunderscout.ui.template_list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import dev.chara.thunderscout.model.Event;
import dev.chara.thunderscout.model.ScoutData;
import dev.chara.thunderscout.storage.database.DataRepository;
import dev.chara.thunderscout.storage.preference.Preference;
import dev.chara.thunderscout.storage.preference.PreferenceRepository;

public final class TemplateListViewModel extends AndroidViewModel {

    private DataRepository dataRepository;
    private PreferenceRepository preferenceRepository;

    private LiveData<List<ScoutData>> liveData;

    public TemplateListViewModel(@NonNull Application application) {
        super(application);

        dataRepository = DataRepository.getInstance(application.getApplicationContext());
        preferenceRepository = PreferenceRepository.getInstance(application.getApplicationContext());

        liveData = dataRepository.getDataByEvent(Event.EVENT_ID_TEMPLATES);
    }

    LiveData<List<ScoutData>> getTemplates() {
        return liveData;
    }

    void deleteTemplate(ScoutData template) {
        LiveData<List<Event>> eventsByTemplate = dataRepository.getEventsByTemplate(template.id);

        eventsByTemplate.observeForever(new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                for (Event event : events) {
                    if (preferenceRepository.getLong(Preference.SELECTED_EVENT) == event.id) {
                        preferenceRepository.setLong(Preference.SELECTED_EVENT, Event.EVENT_ID_DEFAULT);
                    }
                }

                dataRepository.deleteData(template);

                eventsByTemplate.removeObserver(this);
            }
        });
    }

}
