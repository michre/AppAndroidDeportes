package gcm;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import main.ActionsOnReceive;


public class GCMIntentService extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * El servicio se ejecuta hasta que es parado explicitamente, por eso se devuelve START_STICKY
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent !=null){

            // Mensaje para indicar que se va a ejecutar el servicio
            Toast.makeText(this, "Servicio en Ejecucion", Toast.LENGTH_SHORT).show();

            //Obtiene el mensaje push enviado por GCM y obtiene el json
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
            String messageType = gcm.getMessageType(intent);
            Bundle extras = intent.getExtras();

            //Comprueba si el mensaje es correcto (no vacio)
            if (!extras.isEmpty())
            {
                if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
                {
                    //llama a la funcion que hara la llamada para realizar la accion: obtiene el texto del campo mensaje del json que ha llegado por GCM
                    try {
                        accionMensaje( extras.getString("mensaje"), intent);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }

        return START_STICKY;

    }


    /**
     * Llama a la clase ActionsOnReceive para que realice la accion oportuna en funcion del contenido del mensaje
     * @param msg
     * @param intent
     * @throws Exception
     */
    private void accionMensaje(String msg, Intent intent) throws Exception
    {
        //Llamada y ejecucion de la accion
        ActionsOnReceive accion = new ActionsOnReceive(this);
        accion.execute(msg);

        //Indica que el servicio se ha completado
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }


}



