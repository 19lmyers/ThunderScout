package dev.chara.thunderscout.ui.scouting_flow;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.time.Instant;

import dev.chara.thunderscout.bluetooth.ClientConnectionThread;
import dev.chara.thunderscout.model.ScoutData;
import dev.chara.thunderscout.storage.database.DataRepository;
import dev.chara.thunderscout.storage.preference.Preference;
import dev.chara.thunderscout.storage.preference.PreferenceRepository;

public final class ScoutingFlowViewModel extends AndroidViewModel {

    private DataRepository dataRepository;
    private PreferenceRepository preferenceRepository;

    private long eventId;
    private LiveData<ScoutData> scoutData;

    public ScoutingFlowViewModel(@NonNull Application application) {
        super(application);

        dataRepository = DataRepository.getInstance(application.getApplicationContext());
        preferenceRepository = PreferenceRepository.getInstance(application.getApplicationContext());

        this.eventId = preferenceRepository.getLong(Preference.SELECTED_EVENT);

        // Init ScoutData instance
        scoutData = Transformations.switchMap(dataRepository.getEventById(eventId),
                event -> dataRepository.getDataById(event.templateId));

        scoutData.observeForever(new Observer<ScoutData>() {
            @Override
            public void onChanged(ScoutData data) {
                // Initialization
                data.teamNumber = "";

                scoutData.removeObserver(this);
            }
        });
    }

    LiveData<ScoutData> getScoutData() {
        return scoutData;
    }

    void saveData() {
        PreferenceRepository preferenceRepository = PreferenceRepository.getInstance(getApplication().getApplicationContext());

        // Init fields
        ScoutData data = scoutData.getValue();

        data.id = 0; //automatically generated
        data.eventId = eventId;

        data.dateCreated = Instant.now();
        data.sourceDevice = preferenceRepository.getString(Preference.DEVICE_NAME);

        // Local storage
        if (preferenceRepository.getBoolean(Preference.MS_SAVE_TO_LOCAL_DEVICE)) {
            dataRepository.insertData(data);
        }

        // Bluetooth server
        if (preferenceRepository.getBoolean(Preference.MS_SEND_TO_BLUETOOTH_SERVER)) {
            String address = preferenceRepository.getString(Preference.MS_BLUETOOTH_SERVER_DEVICE);

            if (address == null) {
                new MaterialAlertDialogBuilder(getApplication().getApplicationContext())
                        .setTitle("ERROR: Bluetooth device not set")
                        .setMessage("Please configure your server settings and try again")
                        .setPositiveButton("Ok", null)
                        .show();
                return;
            }

            BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);

            ClientConnectionThread connectTask = new ClientConnectionThread(device, data, getApplication().getApplicationContext());
            connectTask.start();
        }

        // Cache
        preferenceRepository.setInteger(Preference.LAST_USED_MATCH_NUMBER, data.matchNumber);
        preferenceRepository.setString(Preference.LAST_USED_ALLIANCE_STATION, data.allianceStation.name());
    }
}
