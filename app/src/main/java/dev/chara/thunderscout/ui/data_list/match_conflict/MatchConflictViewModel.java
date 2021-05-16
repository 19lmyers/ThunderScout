package dev.chara.thunderscout.ui.data_list.match_conflict;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import dev.chara.thunderscout.model.ScoutData;
import dev.chara.thunderscout.model.type.AllianceStation;
import dev.chara.thunderscout.storage.database.DataRepository;

final class MatchConflictViewModel extends AndroidViewModel {

    private DataRepository repository;

    private long eventId;
    private int matchNumber;
    private AllianceStation station;

    private MatchConflictViewModel(@NonNull Application application, long eventId, int matchNumber, AllianceStation station) {
        super(application);

        repository = DataRepository.getInstance(application.getApplicationContext());

        this.eventId = eventId;
        this.matchNumber = matchNumber;
        this.station = station;
    }

    public LiveData<List<ScoutData>> getDataList() {
        return repository.getDataBySlot(eventId, matchNumber, station);
    }

    public void deleteData(ScoutData data) {
        repository.deleteData(data);
    }

    public static final class Factory implements ViewModelProvider.Factory {

        private Application application;

        private long eventId;
        private int matchNumber;
        private AllianceStation station;

        Factory(Application application, long eventId, int matchNumber, AllianceStation station) {
            this.application = application;

            this.eventId = eventId;
            this.matchNumber = matchNumber;
            this.station = station;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MatchConflictViewModel(application, eventId, matchNumber, station);
        }
    }
}
