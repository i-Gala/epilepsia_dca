package com.ua.igala.epilepsia_dca;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.view.View;
import android.widget.TextView;

import com.angel.sdk.BleDevice;
import com.ua.igala.epilepsia_dca.sqlite.OperacionesBD;

import junit.framework.Assert;

/**
 * Se trata de la HOME de la app, desde aquí se conectará la pulsera,
 * se mostrará un resumen de los datos actuales y se dará acceso
 * a todas las demás pantallas
 */

public class HomeActivity extends AppCompatActivity {
    private OperacionesBD database;
    Smartband smartband = Smartband.getInstance();
    Global global = Global.getInstance();

    // Definimos los estados en los que puede estar la conexión con la smartband
    private static final int IDLE = 0;
    private static final int SCANNING = 1;
    private static final int CONNECTED = 2;

    private int NOTIF_REF = 1;
    private NotificationManager manager;

    //Definimos intervalos
    private static final int RSSI_UPDATE_INTERVAL = 1000; // Milliseconds

    private Dialog mDeviceListDialog;
    private ListItemsAdapter mDeviceListAdapter;

    private Handler handler;
    private Runnable lectorPeriodico;

    private CountDownTimer temporizador_ataque;
    private CountDownTimer temporizador_falsa_alarma;
    private boolean ataque_detectado;
    private boolean falsa_alarma;
    private long falsa_alarma_MS;

    ImageView ICON_CONNECTED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ICON_CONNECTED = (ImageView) findViewById(R.id.bluetooth_connected);

        database = OperacionesBD.obtenerInstancia(getApplicationContext());
        updateDetectarAtaque();

        handler = new Handler(this.getMainLooper());
        lectorPeriodico = new Runnable() {
            @Override
            public void run() {
                global.getDispositivoBle().readRemoteRssi();
                smartband.readAccelerationEnergyMagnitude();
                mostrarCalorias();
                handler.postDelayed(lectorPeriodico, RSSI_UPDATE_INTERVAL);
            }
        };

        smartband.setActivity(this);
        smartband.configurateStates(IDLE, SCANNING, CONNECTED);
        mostrarDispositivoEnlazado();
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        ataque_detectado = false;
        falsa_alarma = false;
        falsa_alarma_MS = 0;
    }

    protected void onStart() {
        super.onStart();
        if(!global.getDispositivoBleDireccion().equals(""))
            conectar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_home);
        ICON_CONNECTED = (ImageView) findViewById(R.id.bluetooth_connected);
        if(global.getDispositivoBle() == null)
            smartband.setSmartbandState(IDLE);
        else
            smartband.setSmartbandState(CONNECTED);
        mostrarDispositivoEnlazado();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mostrarSenyal(0);
        unscheduleUpdaters();
        desconectar();
    }

    /****************************************************************
     *                            ONCLICK                           *
     ****************************************************************/

    protected void scanOnClick(View v) {
        mDeviceListAdapter = new ListItemsAdapter(this, R.layout.list_item);
        smartband.updateDeviceListAdapter(mDeviceListAdapter);
        switch (smartband.getSmartbandState()) {
            case IDLE:      startScan();    break;
            case SCANNING:  stopScan();     break;
            case CONNECTED: desconectar();  break;
        }
    }

    protected void logoutOnClick(View v) {
        mostrarSenyal(0);
        unscheduleUpdaters();
        if(!global.getDispositivoBleDireccion().equals(""))
            desconectar();

        mostrarSenyal(0);
        unscheduleUpdaters();
        desconectar();

        Global.getInstance().setOnlineUser(false);
        Global.getInstance().setIDUserOnline(null);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void profileOnClick(View v) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    protected void lastStatsOnClick(View v) {
    }

    protected void alarmOnClick(View v) {
        Intent intent = new Intent(this, AlarmActivity.class);
        startActivity(intent);
    }

    private void conectar() {
        if(global.getDispositivoBle() != null)
            desconectar();

        global.setDispositivoBle(new BleDevice(this, deviceLifecycleCallback, handler));

        smartband.registrarServicios(global.getDispositivoBle());
        smartband.setSmartbandState(CONNECTED);
        mostrarDispositivoEnlazado();
        global.getDispositivoBle().connect(global.getDispositivoBleDireccion());
        scheduleUpdaters();
        mostrarDesconexion();
    }

    private void desconectar() {
        if(global.getDispositivoBle() != null && smartband.getSmartbandState() == CONNECTED) {
            global.getDispositivoBle().disconnect();
            smartband.setSmartbandState(IDLE);
            mostrarDispositivoEnlazado();
            mostrarBateria(-99);
            mostrarSenyal(0);
        }
    }

    private final BleDevice.LifecycleCallback deviceLifecycleCallback = new BleDevice.LifecycleCallback() {
        @Override
        public void onBluetoothServicesDiscovered(BleDevice dispositivo) {
            dispositivo = smartband.getServicios(dispositivo);

            //dibujar todos los servicios
            mostrarBateria(smartband.getValueBattery());
            mostrarHR();
            mostrarTemperatura();
            /*mostrarStepCount();*/
        }

        @Override
        public void onBluetoothDeviceDisconnected() {
            mostrarDesconexion();
            unscheduleUpdaters();
            conectar();
        }

        public void onReadRemoteRssi(final int rssi) {
            mostrarSenyal(rssi);
            mostrarBateria(smartband.getValueBattery());
            mostrarHR();
            mostrarTemperatura();
        }


    };

    /****************************************************************
     *                       ESCANER BLUETOOTH                      *
     ****************************************************************/

    private void startScan() {
        smartband.buscarDispositivoSmartband();
        smartband.setSmartbandState(SCANNING);
        smartband.startScan();
        showDeviceListDialog();
    }

    private void stopScan() {
        smartband.stopScan();
        smartband.setSmartbandState(IDLE);
    }

    private void showDeviceListDialog() {
        mDeviceListAdapter = smartband.getDeviceListAdapter();
        mDeviceListDialog = new Dialog(this);
        mDeviceListDialog.setTitle(R.string.bluetooth_devicelist);
        mDeviceListDialog.setContentView(R.layout.device_list);
        ListView lv = (ListView) mDeviceListDialog.findViewById(R.id.lv);
        lv.setAdapter(mDeviceListAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
                smartband.stopScan();
                mDeviceListDialog.dismiss();

                BluetoothDevice bluetoothDevice = mDeviceListAdapter.getItem(position).getBluetoothDevice();
                Assert.assertTrue(bluetoothDevice != null);
                global.setDispositivoBleDireccion(bluetoothDevice.getAddress());

                if(!global.getDispositivoBleDireccion().equals("")) {
                    smartband.setSmartbandState(CONNECTED);
                    mostrarDispositivoEnlazado();

                    conectar();
                }
            }
        });

        mDeviceListDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                smartband.stopScan();
            }
        });
        mDeviceListDialog.show();
    }

    /****************************************************************
     *                      APARTADO GRÁFICO                        *
     ****************************************************************/

    private void mostrarCalorias() {
        int accelerationEnergyMagnitude = smartband.getValueAccelerationEnergyMagnitude();
    }

    private void mostrarHR() {
        int value_hr = smartband.getValueHR();
        Log.d("value_hr", value_hr+"");
        TextView info =(TextView) findViewById(R.id.hr_medida);
        info.setText(value_hr + "bpm");
        detectarAtaque(value_hr);
    }

    private void mostrarTemperatura() {
        double value_temperatura = smartband.getValueTemperature();
        Log.d("value_temperatura", value_temperatura+"");

        TextView info =(TextView) findViewById(R.id.temperatura_medida);
        info.setText(String.format("%.1f", value_temperatura) + "º");

    }

    private void mostrarStepCount() {
        int value_stepcount = smartband.getValueStepCount();
    }

    private void mostrarSenyal(int fuerza_senyal) {
        int idIcono = R.drawable.senyal_0_white_t;
        Log.d("fuerza_senyal", fuerza_senyal+"");

        if (fuerza_senyal > -70)
            idIcono = R.drawable.senyal_100_white_t;
        else if (fuerza_senyal > - 80)
            idIcono = R.drawable.senyal_75_white_t;
        else if (fuerza_senyal > - 85)
            idIcono = R.drawable.senyal_50_white_t;
        else if (fuerza_senyal > - 87)
            idIcono = R.drawable.senyal_25_white_t;
        else
            idIcono = R.drawable.senyal_0_white_t;

        ImageView icono = (ImageView) findViewById(R.id.signal_icon);
        icono.setBackgroundResource(idIcono);
        TextView info =(TextView) findViewById(R.id.signal_medida);
        info.setText(fuerza_senyal + "db");
    }

    private void mostrarBateria(int nivel_bateria) {
        int idIcono = R.drawable.bateria_0_white_t;
        Log.d("nivel_bateria", nivel_bateria+"");

        if(nivel_bateria <= 0)
            idIcono = R.drawable.bateria_0_white_t;
        else if(nivel_bateria <= 25)
            idIcono = R.drawable.bateria_25_white_t;
        else if(nivel_bateria <= 50)
            idIcono = R.drawable.bateria_50_white_t;
        else if(nivel_bateria <= 75)
            idIcono = R.drawable.bateria_75_white_t;
        else if(nivel_bateria <= 100)
            idIcono = R.drawable.bateria_100_white_t;

        ImageView icono = (ImageView) findViewById(R.id.battery_icon);
        icono.setBackgroundResource(idIcono);
        TextView info =(TextView) findViewById(R.id.battery_medida);
        if(nivel_bateria < 0)
            info.setText("-%");
        else
            info.setText(nivel_bateria + "%");
    }

    private void mostrarDesconexion() {
        mostrarSenyal(-99);
        mostrarBateria(-99);
    }

    private void mostrarDispositivoEnlazado() {
        switch (smartband.getSmartbandState()) {
            case IDLE:      ICON_CONNECTED.setBackgroundResource(R.drawable.desconectado_white);  break;
            case CONNECTED: ICON_CONNECTED.setBackgroundResource(R.drawable.conectado_white);     break;
            default:        break;
        }
    }

    /****************************************************************
     *                           HANDLER                            *
     ****************************************************************/

    private void scheduleUpdaters() {
        handler.post(lectorPeriodico);
    }

    private void unscheduleUpdaters() {
        handler.removeCallbacks(lectorPeriodico);
    }

    /****************************************************************
     *                      NOTIFICACIONES                          *
     ****************************************************************/

    private Notification getNotification(Notification.Builder builder) {
        long[] pattern = new long[]{1000,500,1000};
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        builder
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker(R.string.alerta_titulo + " " + R.string.app_name)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getString(R.string.alerta_titulo) + " " + getString(R.string.app_name))
                .setContentText(getString(R.string.alerta_info))
                .setLights(0xff00ff00, 1, 0)
                .setVibrate(pattern)
                .setOngoing(true)
                .setSound(defaultSound);

        return builder.build();
    }

    private void crearNotificationBle() {
        Notification notification = null;
        Notification.Builder builder = new Notification.Builder(this);

        notification = getNotification(builder);
        notification.flags = notification.flags | Notification.FLAG_INSISTENT;
        manager.notify(NOTIF_REF++, notification);
    }

    private void updateDetectarAtaque() {
        Cursor cursor = database.getUsuarioByID(global.getIDUserOnline());
        global.setMaxHR(Integer.parseInt(database.getUserMaxHR(cursor, false)));
        global.setMinHR(Integer.parseInt(database.getUserMinHR(cursor, false)));
        global.setTiempoEspera(Integer.parseInt(database.getUserTiempoEspera(cursor, true)));
    }

    /****************************************************************
     *                      DETECTAR ATAQUE                         *
     ****************************************************************/

    private void detectarAtaque(int hr) {
        boolean ataque = false;

        if(hr >= global.getMinHR() && hr <= global.getMaxHR())
            ataque = false;
        else if(hr != 0)    // Entendemos que si es 0 es porque no está activada la detección de pulso cardiaco
            ataque = true;

        if(ataque) {    // Detecta un ataque
            if(!ataque_detectado) {   // Primera vez que se sale de los parámetros
                ataque_detectado = true;
                temporizador_ataque = new CountDownTimer(global.getTiempoEspera(), 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        if(falsa_alarma) {
                            temporizador_falsa_alarma.cancel();
                            falsa_alarma = false;
                        }
                        crearNotificationBle();
                    }
                }.start();
            } else {
                if(falsa_alarma) {  // Si se había detectado una falsa alarma con anterioridad
                    falsa_alarma = false;
                    temporizador_falsa_alarma.cancel();
                    falsa_alarma_MS = 0;
                }
            }
        } else {    // No se detecta ataque
            if(ataque_detectado) {
                if(!falsa_alarma) { // Primera vez que se detecta una falsa alarma dentro del ataque
                    falsa_alarma = true;
                    temporizador_falsa_alarma = new CountDownTimer(30000, 1000) {
                        @Override
                        public void onTick(long l) {
                            falsa_alarma_MS = l;
                        }

                        @Override
                        public void onFinish() {
                            ataque_detectado = false;
                            temporizador_ataque.cancel();
                        }
                    }.start();
                }
            }
        }
    }
}
