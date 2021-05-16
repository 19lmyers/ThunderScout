package dev.chara.thunderscout.bluetooth;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavDeepLinkBuilder;

import java.util.Objects;

import dev.chara.thunderscout.R;
import dev.chara.thunderscout.bluetooth.utils.NotificationIdFactory;
import dev.chara.thunderscout.storage.preference.Preference;
import dev.chara.thunderscout.storage.preference.PreferenceRepository;

public final class BluetoothServerService extends Service {

    private static int SERVER_NOTIFICATION_ID = NotificationIdFactory.getNewNotificationId();

    private BroadcastReceiver receiver;

    private ServerListenerThread acceptThread;

    @Override
    public void onCreate() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Objects.equals(intent.getAction(), BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                            BluetoothAdapter.ERROR);

                    switch (state) {
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            if (acceptThread != null) {
                                acceptThread.cancel();
                            }
                            PreferenceRepository.getInstance(getApplicationContext()).setBoolean(Preference.ENABLE_BLUETOOTH_SERVER, false);
                            stopSelf();
                            Toast.makeText(BluetoothServerService.this, "Bluetooth disabled, stopping server", Toast.LENGTH_LONG).show();
                            break;
                        case BluetoothAdapter.STATE_ON:
                            acceptThread = new ServerListenerThread(getApplicationContext());
                            acceptThread.start();
                            Toast.makeText(BluetoothServerService.this, "Starting server", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel serverChannel = new NotificationChannel("bluetooth_server", "Bluetooth server", NotificationManager.IMPORTANCE_LOW);
            serverChannel.setDescription("Persistent notification for the Bluetooth server");
            NotificationManagerCompat.from(this).createNotificationChannel(serverChannel);
        }

        PendingIntent openSettingsIntent = new NavDeepLinkBuilder(this)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.bluetoothServerSettingsFragment)
                .createPendingIntent();

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "bluetooth_server")
                .setSmallIcon(R.drawable.ic_bluetooth_searching_24dp)
                .setContentTitle("Bluetooth server")
                .setContentText("Waiting for incoming data")
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColorized(true)
                .setColor(getResources().getColor(R.color.primary))
                .setShowWhen(false)
                .setContentIntent(openSettingsIntent)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setGroup("BT_SERVER");

        //System.err.println(this.getClass().getName() + " Starting Bluetooth server");

        startForeground(SERVER_NOTIFICATION_ID, notification.build());

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        registerReceiver(receiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));

        if (mBluetoothAdapter == null) {
            PreferenceRepository.getInstance(getApplicationContext()).setBoolean(Preference.ENABLE_BLUETOOTH_SERVER, false);
            stopSelf();
            Toast.makeText(this, "Bluetooth support not present, stopping server", Toast.LENGTH_LONG).show();

        } else if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBluetoothIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(enableBluetoothIntent);

        } else {
            acceptThread = new ServerListenerThread(getApplicationContext());
            acceptThread.start();
            Toast.makeText(BluetoothServerService.this, "Starting server", Toast.LENGTH_LONG).show();
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        //System.err.println(this.getClass().getName() + " Stopping Bluetooth server");

        unregisterReceiver(receiver);

        stopForeground(true);

        if (acceptThread != null) {
            acceptThread.cancel();
        }
    }
}
