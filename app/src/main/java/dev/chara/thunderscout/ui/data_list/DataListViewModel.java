package dev.chara.thunderscout.ui.data_list;

import android.app.Application;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import dev.chara.thunderscout.R;
import dev.chara.thunderscout.model.Event;
import dev.chara.thunderscout.model.ScoutData;
import dev.chara.thunderscout.model.type.AllianceStation;
import dev.chara.thunderscout.storage.database.DataRepository;
import dev.chara.thunderscout.storage.preference.Preference;
import dev.chara.thunderscout.storage.preference.PreferenceRepository;

public final class DataListViewModel extends AndroidViewModel {

    private DataRepository dataRepository;

    private LiveData<Event> currentEvent;
    private LiveData<List<ScoutData>> currentData;

    @IdRes
    private int currentView;

    private MutableLiveData<Boolean> selectionMode;
    private List<ScoutData> selectedData;

    public DataListViewModel(@NonNull Application application) {
        super(application);

        dataRepository = DataRepository.getInstance(application.getApplicationContext());
        PreferenceRepository preferenceRepository = PreferenceRepository.getInstance(application.getApplicationContext());

        currentEvent = Transformations.switchMap(preferenceRepository.getLongObservable(Preference.SELECTED_EVENT),
                eventId -> dataRepository.getEventById(eventId));

        currentData = Transformations.switchMap(currentEvent,
                event -> dataRepository.getDataByEvent(event.id));

        currentView = R.id.button_show_matches;

        selectionMode = new MutableLiveData<>();
        selectionMode.setValue(false);

        selectedData = new ArrayList<>();
    }

    LiveData<Event> getCurrentEvent() {
        return currentEvent;
    }

    LiveData<List<ScoutData>> getScoutData() {
        return currentData;
    }

    void insertData(List<ScoutData> data) {
        dataRepository.insertData(data);
    }

    List<ScoutData> deleteData(List<ScoutData> data) {
        dataRepository.deleteData(data);
        return data;
    }

    List<ScoutData> deleteAllData() {
        List<ScoutData> data = currentData.getValue();
        dataRepository.deleteData(data);
        return data;
    }

    @IdRes
    int getCurrentView() {
        return currentView;
    }

    void setCurrentView(@IdRes int currentView) {
        this.currentView = currentView;
    }

    public LiveData<Boolean> isInSelectionMode() {
        return selectionMode;
    }

    private void setSelectionMode(boolean isSelectionMode) {
        selectionMode.setValue(isSelectionMode);
    }

    public List<ScoutData> getSelectedData() {
        return selectedData;
    }

    public boolean isSelected(ScoutData data) {
        return selectedData.contains(data);
    }

    public boolean areAllSelected(List<ScoutData> dataList) {
        return selectedData.containsAll(dataList);
    }

    public void setSelected(ScoutData data, boolean isSelected) {
        if (isSelected && !selectedData.contains(data)) {
            selectedData.add(data);
        } else if (!isSelected) {
            selectedData.remove(data);
        }

        if (selectedData.size() > 0) {
            setSelectionMode(true);
        } else {
            setSelectionMode(false);
        }
    }

    public void clearSelections() {
        selectedData.clear();
        setSelectionMode(false);
    }

    public void addMockData() {
        for (int match = 0; match < 100; match++) {
            for (int team = 0; team < 6; team++) {
                ScoutData data = new ScoutData();
                data.eventId =  currentEvent.getValue().id;

                data.matchNumber = match;
                data.teamNumber = String.valueOf(team);

                data.allianceStation = AllianceStation.values()[team];

                data.dateCreated = Instant.now();
                data.sourceDevice = "Debug";

                dataRepository.insertData(data);
            }
        }
    }
}
