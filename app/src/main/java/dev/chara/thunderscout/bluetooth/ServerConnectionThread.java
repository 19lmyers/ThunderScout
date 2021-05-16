package dev.chara.thunderscout.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import dev.chara.thunderscout.model.Event;
import dev.chara.thunderscout.model.ScoutData;
import dev.chara.thunderscout.storage.database.DataRepository;
import dev.chara.thunderscout.storage.preference.Preference;
import dev.chara.thunderscout.storage.preference.PreferenceRepository;

final class ServerConnectionThread extends Thread {

    private BluetoothSocket socket;

    private Context appContext;

    ServerConnectionThread(BluetoothSocket socket, Context appContext) {
        this.socket = socket;
        this.appContext = appContext;
    }

    @Override
    public void run() {
        ObjectInputStream inputStream;
        ObjectOutputStream outputStream;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        ScoutData data = null;
        try {
            data = (ScoutData) inputStream.readObject();
        } catch (InvalidClassException versionError) {
            try {
                versionError.printStackTrace();
                outputStream.writeInt(ClientConnectionThread.RESULT_CODE_VERSION_MISMATCH);
                outputStream.flush();
                return;
            } catch (Exception another) {
                another.printStackTrace();
                return;
            }
        } catch (Exception other) {
            other.printStackTrace();
            return;
        }

        new Handler(Looper.getMainLooper()).post(() -> {
            Toast.makeText(appContext, "Bluetooth transmission successful", Toast.LENGTH_LONG).show();
        });

        PreferenceRepository preferenceRepository = PreferenceRepository.getInstance(appContext);

        // Set event ID to current event
        if (data.eventId != Event.EVENT_ID_TEMPLATES) {
            data.eventId = preferenceRepository.getLong(Preference.SELECTED_EVENT);
        }

        final ScoutData finalData = data;
        new Handler(Looper.getMainLooper()).post(() -> {

            // Local storage
            if (preferenceRepository.getBoolean(Preference.BT_SAVE_TO_LOCAL_DEVICE)) {
                DataRepository repository = DataRepository.getInstance(appContext);
                repository.insertData(finalData);
            }

            if (preferenceRepository.getBoolean(Preference.BT_SEND_TO_BLUETOOTH_SERVER)) {
                String address = preferenceRepository.getString(Preference.BT_BLUETOOTH_SERVER_DEVICE);

                if (address == null) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(appContext, "ERROR: Target device not specified", Toast.LENGTH_LONG).show();
                    });
                    return;
                }

                BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);

                ClientConnectionThread connectTask = new ClientConnectionThread(device, finalData, appContext);
                connectTask.start();
            }
        });

        try {
            outputStream.writeInt(ClientConnectionThread.RESULT_CODE_SUCCESSFUL);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
}
