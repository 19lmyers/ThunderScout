package dev.chara.thunderscout.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

import dev.chara.thunderscout.bluetooth.utils.BluetoothInfo;
import dev.chara.thunderscout.model.ScoutData;

public final class ClientConnectionThread extends Thread {

    static final int RESULT_CODE_SUCCESSFUL = 1;
    static final int RESULT_CODE_VERSION_MISMATCH = 2;

    private BluetoothDevice device;
    private ScoutData data;

    private Context appContext;

    public ClientConnectionThread(BluetoothDevice device, ScoutData data, Context appContext) {
        this.device = device;
        this.data = data;
        this.appContext = appContext;
    }

    @Override
    public void run() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();

        BluetoothSocket socket = null;
        try {
            socket = device.createRfcommSocketToServiceRecord(UUID.fromString(BluetoothInfo.UUID));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            socket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                socket.close();
            } catch (IOException closeException) {
                closeException.printStackTrace();
            }
            return;
        }

        System.err.println(this.getClass().getName() + " Connection to server device successful");

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

        System.err.println(this.getClass().getName() + " Attempting to send data");
        try {
            outputStream.writeObject(data);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        int resultCode;
        try {
            resultCode = inputStream.readInt();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (resultCode == RESULT_CODE_SUCCESSFUL) { //Server reported that the operation succeeded
            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(appContext, "Bluetooth transmission successful", Toast.LENGTH_LONG).show();
            });

        } else if (resultCode == RESULT_CODE_VERSION_MISMATCH) { //Server caught the InvalidClassException and responded
            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(appContext, "App version mismatch; data rejected by server", Toast.LENGTH_LONG).show();
            });

        } else { //No result code or improper value (?)
            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(appContext, "Failed to receive confirmation from server", Toast.LENGTH_LONG).show();
            });
        }
    }
}
