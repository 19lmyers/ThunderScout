package dev.chara.thunderscout.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import java.io.IOException;
import java.util.UUID;

import dev.chara.thunderscout.bluetooth.utils.BluetoothInfo;

final class ServerListenerThread extends Thread {

    private final BluetoothServerSocket serverSocket;

    private Context appContext;

    ServerListenerThread(Context appContext) {
        this.appContext = appContext;

        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        BluetoothServerSocket tmp = null;

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        try {
            // MY_UUID is the app's UUID string, also used by the client code
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(BluetoothInfo.SERVICE_NAME, UUID.fromString(BluetoothInfo.UUID));
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverSocket = tmp;
    }

    @Override
    public void run() {
        // Keep listening until exception occurs
        while (true) {
            final BluetoothSocket socket;
            try {
                //System.err.println(this.getClass().getName() + " Listening for incoming connections");
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            // If a connection was accepted
            if (socket != null) {
                // Do work to manage the connection (in a separate thread)

                //System.err.println(this.getClass().getName() + " Connected to " + socket.getRemoteDevice().getName());

                ServerConnectionThread readThread = new ServerConnectionThread(socket, appContext);
                readThread.start();
            }
        }
    }

    /**
     * Will cancel the listening socket, and cause the thread to finish
     */
    public void cancel() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            //ignored
        }
    }
}
