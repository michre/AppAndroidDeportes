package register;

/**
 * Clase RegisterGCM: registra el dispositivo en el servidor de google para obtemer la id de recepcion de google cloud messaging
 * @author Miguel Rojo Esteva
 */


import java.io.IOException;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

//import com.google.android.gms.gcm.GoogleCloudMessaging;


import com.example.miguel.appdeportes.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import com.example.miguel.appdeportes.MainActivity;
import preferences.Preferences;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class RegisterGCM extends Activity{
    String TAG = "Debug_RegisterGCM";


    private String NEW_ID_PAGE = "http://www.appdeportesprueba.esy.es/mobile/deviceRegister.php";
    RegistroGCM regGCM = new RegistroGCM();
    String SENDERIDGCM = "62585979586";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.vacia);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		/*Comprueba si ya esta registrado*/
        if(Preferences.getUsuConectado(RegisterGCM.this)){
			/*Comprueba si la fecha de registro es inferior a una semana*/
            if(!checkIdGCMExpires ()){
                regGCM.execute();
            }else{
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }else{
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    /**
     * Comprueba si ha expirado el tiempo de id GCM
     * @param
     * @return true si no ha expirado, false si ha expirado
     */
    public boolean checkIdGCMExpires (){
        long timeExpire = Preferences.geExpiretIdGCM (RegisterGCM.this);

        if (System.currentTimeMillis() > timeExpire)
        {
            Log.d(TAG, "Registro GCM expirado.");
            return false;
        }

        return true;
    }


    /**
     * Clase RegistroGCM: crea nueva id GCM.
     * @author Miguel Rojo Esteva
     */
    private class RegistroGCM extends AsyncTask<String,Integer,String>
    {
        private static final long EXPIRATION_TIME = 1000 * 3600 * 24 * 7; //Milisegundos de  una semana
        private ProgressDialog waitDialog;

        /**
         * Muestra un mensaje mientras se registra el dispositivo
         * @see android.os.AsyncTask#onPreExecute()
         */
        protected void onPreExecute() {

            waitDialog = ProgressDialog.show(RegisterGCM.this, "Por favor, espere", "Registrando el dispositivo", true, true);
            waitDialog.setCancelable(false);
        }


        /**
         * Comprueba si el procedimiento ha finalizado con exito
         * @see android.os.AsyncTask#onPreExecute()
         */
        protected void onPostExecute(Boolean result) {

            waitDialog.dismiss();
            if (!isCancelled()) {
                Log.d(TAG, "Nuevo usuario añadido.");

                //Si el registro se ha efectuado con exito -> lo registra en GCM, si no -> muestra el error
                if (result) {
                    startActivity(new Intent(RegisterGCM.this, MainActivity.class));
                    //finish();

                }else{
                    Toast.makeText(RegisterGCM.this, "Error al registrar el dispositivo GCM.",
                            Toast.LENGTH_LONG).show();
                }
            }



        }
        @Override
        protected String doInBackground(String... params)
        {

            try
            {

                GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(RegisterGCM.this);


                //Nos registramos en los servidores de GCM
                String id = gcm.register(SENDERIDGCM);

                Log.d(TAG, "Registro GCM con id: " + id);

                //Obtiene la id anterior (si existia)
                String oldId = Preferences.getIdGCM(RegisterGCM.this);

                //Almacena la id en las preferencias
                Preferences.setIdGCM(RegisterGCM.this, id);

                //Almacena tiempo de expiracion
                Preferences.setDateIdGCM(RegisterGCM.this, System.currentTimeMillis() + EXPIRATION_TIME);

                //Registro en el servidor
                registroServer (id, oldId);


            }
            catch (IOException ex)
            {
                Log.d(TAG, "Error registro en GCM:" + ex.getMessage());
            }

            return "";
        }

        private boolean registroServer (String id, String oldId)
        {
            boolean reg = false;

		    /*Rellena los campos de la url: usu y pass*/
            Vector<NameValuePair> vars = new Vector<NameValuePair>();
            vars.add(new BasicNameValuePair("id", id));
            vars.add(new BasicNameValuePair("oldid", oldId));
            vars.add(new BasicNameValuePair(Preferences.CORREO_KEY, Preferences.getCorreoUsuario(RegisterGCM.this)));

			/*Crea la url*/
            String url = NEW_ID_PAGE + "?"
                    + URLEncodedUtils.format(vars, null);

            HttpGet request = new HttpGet(url);

            Log.d(TAG, "url: "+url);


            try {
				/*Envia la peticion al servidor con la url*/
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                HttpClient client = new DefaultHttpClient();
                String responseBody = client.execute(request, responseHandler);
                Log.d(TAG, "responsebody: "+responseBody);



            } catch (ClientProtocolException e) {
                Log.e(TAG, "Failed to set Id (protocol): ", e);
            } catch (IOException e) {
                Log.e(TAG, "Failed to set Id (io): ", e);
            }

            waitDialog.dismiss();
            startActivity(new Intent(RegisterGCM.this, MainActivity.class));

            return true;
        }
    }

}
