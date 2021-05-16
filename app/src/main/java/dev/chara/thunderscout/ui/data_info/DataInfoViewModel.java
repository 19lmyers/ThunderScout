package dev.chara.thunderscout.ui.data_info;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dev.chara.thunderscout.model.ScoutData;
import dev.chara.thunderscout.storage.database.DataRepository;

final class DataInfoViewModel extends AndroidViewModel {

    private DataRepository repository;

    private LiveData<ScoutData> liveData;

    private DataInfoViewModel(@NonNull Application application, long dataId) {
        super(application);

        repository = DataRepository.getInstance(application.getApplicationContext());

        liveData = repository.getDataById(dataId);
    }

    LiveData<ScoutData> getScoutData() {
        return liveData;
    }

    public static final class Factory implements ViewModelProvider.Factory {

        private Application application;
        private long dataId;

        Factory(Application application, long dataId) {
            this.application = application;
            this.dataId = dataId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new DataInfoViewModel(application, dataId);
        }
    }
}
