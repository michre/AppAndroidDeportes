package main;

/**
 * Clase ActionsOnReceive: realiza la accion recibida: activar/desactivar wifi, bluetooth y gps y eliminar notificaciones
 * @author Miguel Rojo Esteva
 */

import com.example.miguel.appdeportes.MainActivity;
import com.example.miguel.appdeportes.R;

import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import actions.PickerCalification;
import database.DatabaseAdapter;

public class ActionsOnReceive {

    Context context;

    public ActionsOnReceive (Context c){
        context = c;

    }



    /**
     * Parsea una cadena para obtener las acciones y ejecuta la adecuada
     * @param action accion que se desea realizar
     * @throws Exception
     */
    public void execute (String action) throws Exception{

        toggleZumbido();



        //Parseo: la cadena esta separada por &
        String fragmentos[] = action.split("\\$");
        Log.d("recAction", "fragmentos: "+fragmentos);

        //El primer fragmento contiene la accion a realizar
        //Ejemplo notificacion
        if (fragmentos[0].equals("calificacion")){

            //Se añade la aplicación a la lista de entrenamientos
            //Base de datos
            DatabaseAdapter db = new DatabaseAdapter(context);
            db.open();
            db.insertTraining(fragmentos[1], fragmentos[2], fragmentos[3], fragmentos[4]);
            db.close();

            NotificationManager mNotificationManager;

            Intent intent=new Intent("actions.PICKERCALIFICATION");
            //Intent intent = new Intent(context, PickerCalification.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.d("recAction", "recibido: "+fragmentos[1]);
            intent.putExtra("idTrainingPlayer", fragmentos[1]);
           // context.startActivity(intent);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    intent, 0);


            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.common_signin_btn_icon_light)
                          //  .setOngoing(true)
                            .setContentTitle(fragmentos[2]+" "+ fragmentos[3])
                            .setContentText("Califique el entrenamiento");


            mBuilder.setContentIntent(pendingIntent);
            mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(Integer.parseInt(fragmentos[1]), mBuilder.build());

        }else{
            //Ejemeplo notificacion
            NotificationManager nManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder ncomp = new NotificationCompat.Builder(context);
            ncomp.setContentTitle("Ejemplo de notificación");
            ncomp.setContentText("Lalala");
            ncomp.setContentInfo("Titulo notificación");
            //ncomp.setTicker(notiText);
            ncomp.setSmallIcon(R.drawable.common_signin_btn_icon_dark);
            ncomp.setAutoCancel(true);
            nManager.notify((int)System.currentTimeMillis(),ncomp.build());
        }

    }

    /**
     * Activa zumbido: el movil vibra durante 3 segundos
     */
    public void toggleZumbido (){
        Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
        //Vibra 3 segundos
        v.vibrate(3000);

    }


}