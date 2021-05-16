package dev.chara.thunderscout.ui.team_info;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import dev.chara.thunderscout.model.ScoutData;
import dev.chara.thunderscout.storage.database.DataRepository;

final class TeamInfoViewModel extends AndroidViewModel {

    private DataRepository repository;

    private LiveData<List<ScoutData>> liveData;

    private TeamInfoViewModel(@NonNull Application application, long eventId, String teamNumber) {
        super(application);

        repository = DataRepository.getInstance(application.getApplicationContext());

        liveData = repository.getDataByTeamNumber(eventId, teamNumber);
    }

    LiveData<List<ScoutData>> getMatches() {
        return liveData;
    }

    public static final class Factory implements ViewModelProvider.Factory {

        private Application application;

        private long eventId;
        private String teamNumber;

        Factory(Application application, long eventId, String teamNumber) {
            this.eventId = eventId;
            this.application = application;
            this.teamNumber = teamNumber;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new TeamInfoViewModel(application, eventId, teamNumber);
        }
    }
}
