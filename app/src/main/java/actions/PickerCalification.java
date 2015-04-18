package actions;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.miguel.appdeportes.R;
import android.app.Activity;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.Vector;

import database.DatabaseAdapter;
import preferences.Preferences;


public class PickerCalification extends Activity implements NumberPicker.OnValueChangeListener {

    private String CALIFICATION_PAGE = "http://appdeportesprueba.esy.es/mobile/updateCalification.php";
    private UpdateCalification updateCalification;
    private static TextView tv;
    static Dialog d ;
    static String idTrainingPlayer;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Obtiene la id del entrenamiento del que actualizar la calificacion
        idTrainingPlayer = getIntent().getExtras().getString("idTrainingPlayer");


        setContentView(R.layout.dialog);
        tv = (TextView) findViewById(R.id.textView1);

      //  pickerCalification();
        final NumberPicker np=
                (NumberPicker) findViewById(R.id.numberPicker1);
        np.setMaxValue(10);
        np.setMinValue(1);
        int color = Color.argb(255, 255, 255, 255);
        np.setBackgroundColor(color);

        np.setWrapSelectorWheel(false);
        Button b1 = (Button) findViewById(R.id.button1);

        updateCalification = new UpdateCalification();
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //Elimina notificaciones
                tv.setText(String.valueOf(np.getValue()));
                NotificationManager notifManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notifManager.cancelAll();
                //Elimina el elemento de la base de datos
                DatabaseAdapter db = new DatabaseAdapter(PickerCalification.this);
                db.open();
                db.removeTraining(idTrainingPlayer);
                db.close();

                //Actualiza la calificación en el servidor
                updateCalification.execute(idTrainingPlayer,np.getValue()+"");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.finish();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        Log.i("value is", "" + newVal);

    }

    /**
     * Clase UpdateCalification: actualiza la califiación de un entrenamiento.
     */
    private class UpdateCalification extends AsyncTask<Object, String, Boolean> {
        private String DEBUG_TAG ="DEBUG_PICKER";
        private ProgressDialog waitDialog;

        /**
         * Muestra un mensaje mientras se añade el usuario
         * @see android.os.AsyncTask#onPreExecute()
         */
        protected void onPreExecute() {

            waitDialog = ProgressDialog.show(PickerCalification.this, "Por favor, espere", "Enviando su califación", true, true);
            waitDialog.setCancelable(false);
        }

        /**
         * Comprueba si el procedimiento ha finalizado con exito
         * @see android.os.AsyncTask#onPreExecute()
         */
        protected void onPostExecute(Boolean result) {
            waitDialog.dismiss();

            if (!isCancelled()) {
                Log.d(DEBUG_TAG, "Calificación actualizada.");

                if (result) {
                    finish();
                }else{
                    Toast.makeText(PickerCalification.this, "Error al actalizar la calificación.",
                            Toast.LENGTH_LONG).show();
                }
            } else
                Log.d(DEBUG_TAG, "Inside onPostExecute(), but cancelled.");


        }

        @Override
        protected Boolean doInBackground(Object... params) {
            boolean result=false;

           // if (Preferences.getRedDisponible(PickerCalification.this))
                result = postSettingsToServer((String)params[0], (String)params[1]);
            return result;
        }

        private boolean postSettingsToServer(String idTrainingPlayer, String calification) {
            boolean state = false;

			/*Rellena los campos de la url: usu y pass*/
            Vector<NameValuePair> vars = new Vector<NameValuePair>();
            vars.add(new BasicNameValuePair("idTrainingPlayer", idTrainingPlayer));
            vars.add(new BasicNameValuePair("calification", calification));

			/*Crea la url*/
            String url = CALIFICATION_PAGE + "?"
                    + URLEncodedUtils.format(vars, null);

            HttpGet request = new HttpGet(url);

            Log.d("debug", "url: "+url);


            try {
				/*Envia la peticion al servidor con la url*/
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                HttpClient client = new DefaultHttpClient();
                String responseBody = client.execute(request, responseHandler);
                Log.d(DEBUG_TAG,"responsebody: "+responseBody);

                //Caso de error: calificacion no actualizada
                if(responseBody != null &&  responseBody.length()==0){
                    state = false;

                }else{
                    state=true;
                }

            } catch (ClientProtocolException e) {
                Log.e(DEBUG_TAG, "Failed to get playerId (protocol): ", e);
            } catch (IOException e) {
                Log.e(DEBUG_TAG, "Failed to get playerId (io): ", e);
            }

            return state;
        }



    }

}
