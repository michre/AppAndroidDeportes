package gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GCMBroadcastReceiver extends WakefulBroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {

        //Al recibir el mensaje push llama  a GCMIntentService
        ComponentName comp = new ComponentName(context.getPackageName(),
                GCMIntentService.class.getName());

        //Inicia el servicio para que realice el trabajo: este proceso mantiene el telefono activo (wake lock)
        startWakefulService(context, (intent.setComponent(comp)));

        setResultCode(Activity.RESULT_OK);
    }


}